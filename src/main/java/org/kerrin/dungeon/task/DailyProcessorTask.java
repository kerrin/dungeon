package org.kerrin.dungeon.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.kerrin.dungeon.enums.AccountTask;
import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.DailyItem;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Hiscore;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.repository.AccountMessageRepo;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.HiscoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
public class DailyProcessorTask extends SuperAccountTask {
	private static final Logger logger = LoggerFactory.getLogger(DailyProcessorTask.class);

	private static final long DAY_MILLIS = 24*60*60*1000;

	private static final String CHARACTER_DETAILS_PANEL = "characterDetailsFrame";

	/** Chance in 1000 of boost item being found each time */
	private static final int DAILY_FIND_BOOST_ITEM_CHANCE = 50;

	/** Chance in 1000 of equipment being an artifact instead of legendary */
	private static final int DAILY_ARTIFACT_CHANCE = 250;
	
	private AccountCurrencyService accountCurrencyService;
	
	private CharacterService characterService;
	
	private HiscoreService hiscoreService;
	
	private AccountMessageRepo accountMessageRepo;
	
	private BoostItemService boostItemService;
	
	private EquipmentService equipmentService;
	
	private ServletContext servletContext;
		
	protected DailyProcessorTask() {}
	
	public DailyProcessorTask(
			AccountService accountService,
			AccountMessageRepo accountMessageRepo,
			AccountCurrencyService accountCurrencyService,
			CharacterService characterService,
			HiscoreService hiscoreService,
			BoostItemService boostItemService,
			EquipmentService equipmentService,
			ServletContext servletContext,
			Account account) {
		super(accountService, account, AccountTask.DAILY_CHECKS);
		this.accountCurrencyService = accountCurrencyService;
		this.characterService = characterService;
		this.hiscoreService = hiscoreService;
		this.accountMessageRepo = accountMessageRepo;
		this.boostItemService = boostItemService;
		this.equipmentService = equipmentService;
		this.servletContext = servletContext;
	}
	
	/**
	 * Actually run the daily process
	 */
	@Transactional
	@Override
	protected void realRun() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date yesterdayEnd = new Date(cal.getTimeInMillis()-1);
		Date yesterdayStart = new Date(cal.getTimeInMillis()-DAY_MILLIS);
		
		// Process the daily tokens
		if(account.getPreviousLogin() != null && 
				account.getPreviousLogin().before(yesterdayEnd) && !account.isOnHoliday()) {
			// Calculate tokens to award
			logger.trace("Yesterday: "+yesterdayStart+" to "+yesterdayEnd);
			logger.trace("Previous: "+account.getPreviousLogin());
			if(accountCurrencyService.haveAwardedDailyTokensToday(account, false, false)) {
				logger.debug("Not running daily process for account {}, as it has run already", account.getId());
				return;
			}
			if(account.getPreviousLogin().after(yesterdayStart) && account.getPreviousLogin().before(yesterdayEnd)) {
				logger.trace("Increasing consecutive days for "+account.getUsername());
				account.incrementLoginTokens();
			} else {
				// Calculate how many days to remove
				long daysLoggedOff = (yesterdayStart.getTime() - account.getPreviousLogin().getTime())/DAY_MILLIS;
				if(daysLoggedOff > Integer.MAX_VALUE) {
					logger.error("Apparently "+account.getUsername()+" hasn't logged on in "+daysLoggedOff+" days!");
					daysLoggedOff = Integer.MAX_VALUE-1; // Just incase ;)
				}
				logger.trace("Decreasing consecutive days for "+account.getUsername()+" by "+daysLoggedOff);
				account.decreaseLoginTokens((int)daysLoggedOff);
			}
			
			accountCurrencyService.adjustCurrency(account, false, false, account.getLoginTokens(), 
					ModificationType.GAIN_DAILY,
					accountCurrencyService.getDailyTokenReference(account, false, false));
			hiscoreService.tokensEarnt(account, false, false, account.getLoginTokens());
			accountCurrencyService.adjustCurrency(account, true, false, account.getLoginTokens(), 
					ModificationType.GAIN_DAILY,
					accountCurrencyService.getDailyTokenReference(account, true, false));
			hiscoreService.tokensEarnt(account, true, false, account.getLoginTokens());
			accountCurrencyService.adjustCurrency(account, false, true, account.getLoginTokens(), 
					ModificationType.GAIN_DAILY,
					accountCurrencyService.getDailyTokenReference(account, false, true));
			hiscoreService.tokensEarnt(account, false, true, account.getLoginTokens());
			accountCurrencyService.adjustCurrency(account, true, true, account.getLoginTokens(), 
					ModificationType.GAIN_DAILY,
					accountCurrencyService.getDailyTokenReference(account, true, true));
			hiscoreService.tokensEarnt(account, true, true, account.getLoginTokens());
			
			AccountMessage accountMessage = new AccountMessage(
					account.getId(), "Daily Login Tokens Awarded: "+account.getLoginTokens(), null, null, null);
	        accountMessageRepo.save(accountMessage);
		
	        // Generate a random items
	        boolean hardcore = false;
			boolean ironborn = false;
			if(account.getLevel() >= 4) {
				/* This loop should try all 4 permutations of hardcore and ironborn
				 Order will be: hardcore,ironborn
				 				false,false
								true,false
								false,true
								true,true
				 */
				for(int i=0; i<4; i++) {
			        DailyItem dailyItem = generateDailyItem(servletContext.getContextPath(), account, hardcore, ironborn);
			        if(dailyItem != null) {
				        StashSlotItemSuper item = dailyItem.getItem();
						accountMessage = new AccountMessage(
								account.getId(), dailyItem.getItemMessage(), dailyItem.getItemCharacterUrl(), 
								CHARACTER_DETAILS_PANEL, item);
				        accountMessageRepo.save(accountMessage);
				        switch (item.getStashSlotType()) {
						case BOOST_ITEM:
							BoostItem boostItem = (BoostItem)item;
							boostItem.setMessageId(accountMessage.getId());
							boostItem = boostItemService.create(boostItem);
							accountMessage.setAttachedItemId(boostItem.getId());
							break;
						case EQUIPMENT:
							Equipment equipment = (Equipment)item;
							equipment.setEquipmentLocation(EquipmentLocation.MESSAGE);
							equipment.setEquipmentLocationId(accountMessage.getId());
							equipment = equipmentService.create(equipment);
							accountMessage.setAttachedItemId(equipment.getId());
							break;
						default:
							break;
						}
				        // Update the equipment Id
				        accountMessageRepo.save(accountMessage);
			        }
			        
			        // Toggle the booleans
					if(hardcore) ironborn = !ironborn;
					hardcore = !hardcore;
				}
			}
			List<Character> characters = characterService.findAllByAccountOrderByLevel(account);
			// Free resurrect all characters once a day, that aren't in dungeons
			int[] resurectedCount = {0,0}; 
			for(Character character:characters) {
				if(character.getDungeon() == null && !character.isHardcore()) {
					if(character.isCurrentlyDead()) {
						resurectedCount[character.isIronborn()?1:0]++;
						character.setAlive();
						try {
							characterService.update(character);
						} catch (CharacterNotFound e) {
							logger.error("Attempt to make sure character is alive failed at update {}", character);
							e.printStackTrace();
						}
					}
				}
			}
			StringBuilder message;
			if(resurectedCount[0] > 0) {
				message = new StringBuilder();
				message.append(resurectedCount[0]);
				message.append(" Normal Characters Free resurrected.");
				accountMessage = new AccountMessage(account.getId(), message.toString(), null, null, null);
		        accountMessageRepo.save(accountMessage);
			}
			if(resurectedCount[1] > 0) {
				message = new StringBuilder();
				message.append(resurectedCount[1]);
				message.append(" Ironborn Characters Free resurrected.");
				accountMessage = new AccountMessage(account.getId(), message.toString(), null, null, null);
		        accountMessageRepo.save(accountMessage);
			}
			// No hardcore can be resurrected, so just toggle ironborn
			ironborn = false;
			for(int i=0; i < 2; i++) {
				if(resurectedCount[i] > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");	
					String day = sdf.format(new Date());
					String reference = "free-resurrection-" + account.getUsername() +
							(ironborn?"-ironborn":"") + "-" + day;	
					accountCurrencyService.adjustCurrency(account, false, ironborn, account.getLoginTokens(), 
							ModificationType.FREE_RESURRECTION,
							reference);
				}
				ironborn = !ironborn;
			}
			
			// Score the players total level
			hardcore = false;
			ironborn = false;
			/* This loop should try all 4 permutations of hardcore and ironborn
			 Order will be: hardcore,ironborn
			 				false,false
							true,false
							false,true
							true,true
			 */
			for(int i=0; i<4; i++) {
				long totalLevel = characterService.getTotalLevel(account, hardcore, ironborn);
				Hiscore hiscore = hiscoreService.totalLevel(account, hardcore, ironborn);
				if(hiscore.getScore() < totalLevel) {
					hiscore.setScore(totalLevel);
					hiscoreService.update(hiscore);
				}
				// Toggle the booleans
				if(hardcore) ironborn = !ironborn;
				hardcore = !hardcore;
			}
		}
	}

	/**
	 * Create the daily item
	 * 
	 * @param contextRoot
	 * @param account
	 * @param hardcore
	 * @param ironborn
	 * @return
	 */
	private DailyItem generateDailyItem(String contextRoot, Account account, boolean hardcore, boolean ironborn) {
		StashSlotItemSuper item;
		String message;
		String charaterUrl = null;
		
		if(((int)(Math.random()*1000) < DAILY_FIND_BOOST_ITEM_CHANCE)) {
			item = BoostItem.createRandom(account, Character.MAX_LEVEL, hardcore, ironborn, -1, -1, -1);
			message = "Your daily item today is a rare boost item!";
		} else {
			List<Character> characterList = characterService.findAllByAccountOrderByLevel(account, hardcore, ironborn);
			if(characterList.size() <= 0) {
				return null;
			}
			Character character = characterList.get((int)(Math.random()*characterList.size()));			
			CharSlot charSlot = CharSlot.getRandom();
			EquipmentQuality quality = ((int)(Math.random()*1000) < DAILY_ARTIFACT_CHANCE)?EquipmentQuality.ARTIFACT:EquipmentQuality.LEGENDARY;
			message = "Your daily item today is "+
					"<font color='" + quality.getHtmlColour() + "'>" + quality.getNiceName() + "</font> ";
			List<EquipmentType> validTypes = EquipmentType.getValidTypesFromCharSlot(charSlot);			
			EquipmentType equipmentType = validTypes.get((int)(Math.random()*validTypes.size()));
			message +=  equipmentType.getNiceName() + " for " + character.getName();
			List<EquipmentAttribute> requiredAttributes = new ArrayList<EquipmentAttribute>();
			CharClass charClass = character.getCharClass();
			requiredAttributes.add(charClass.getMainAttribute());
			requiredAttributes.add(charClass.getDefenceAttribute());
			try {
				item = Equipment.createRandom(equipmentType, quality, character.getLevel(), hardcore, ironborn, requiredAttributes);
			} catch (EquipmentNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				// Just let them have a boost item then
				item = BoostItem.createRandom(account, Character.MAX_LEVEL, hardcore, ironborn, -1, -1, -1);
				message = "Your daily item today is a rare boost item!";
			}
			StringBuilder linkUrl = new StringBuilder();
			if(contextRoot != null) {
				linkUrl.append(contextRoot);
				linkUrl.append("/play/character/");
				linkUrl.append(character.getId());
			}
			charaterUrl = linkUrl.toString();
		}
		
		return new DailyItem(item, message, charaterUrl); 
	}
}
