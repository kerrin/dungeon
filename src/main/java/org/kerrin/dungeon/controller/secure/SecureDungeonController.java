package org.kerrin.dungeon.controller.secure;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.exception.DifferentGameStates;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.DungeonNotRunable;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryException;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.exception.MessageEquipmentNotFound;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.DungeonAdventureStartForm;
import org.kerrin.dungeon.forms.DungeonStartForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonDifficulty;
import org.kerrin.dungeon.model.DungeonEvent;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.service.AccountBoostService;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.DungeonEventService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.EmailService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.service.InventoryService;
import org.kerrin.dungeon.utils.Facebook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/play/dungeon")
public class SecureDungeonController extends SuperSecurePublic {
	
	private static final Logger logger = LoggerFactory.getLogger(SecureDungeonController.class);
	
	private final DungeonService dungeonService;
	private final DungeonEventService dungeonEventService;
	private final CharacterService characterService;
	private final CharacterEquipmentService characterEquipmentService;
	private final EquipmentService equipmentService;
	private final BoostItemService boostItemService;
	private final AccountBoostService accountBoostService;
	private final InventoryService inventoryService;
	private final HiscoreService hiscoreService;
	private final EmailService emailService;
	private final AchievementService achievementService;
	private final boolean localMode;
	
	@Autowired
	public SecureDungeonController(AccountService accountService, 
			AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService, 
			Facebook facebook, DungeonService dungeonService, DungeonEventService dungeonEventService,
			CharacterService characterService, CharacterEquipmentService characterEquipmentService,
			EquipmentService equipmentService, BoostItemService boostItemService, AccountBoostService accountBoostService,
			InventoryService inventoryService, HiscoreService hiscoreService, EmailService emailService, 
			AchievementService achievementService, AccountMessageService accountMessageService,
			boolean localMode) {
		super(accountService, accountConfigService, accountCurrencyService, accountMessageService, facebook);
		this.dungeonService = dungeonService;
		this.dungeonEventService = dungeonEventService;
		this.characterService = characterService;
		this.characterEquipmentService = characterEquipmentService;
		this.equipmentService = equipmentService;
		this.boostItemService = boostItemService;
		this.accountBoostService = accountBoostService;
		this.inventoryService = inventoryService;
		this.hiscoreService = hiscoreService;
		this.emailService = emailService;
		this.achievementService = achievementService;
		this.localMode = localMode;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String getDungeons(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Get Dungeons");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		List<Dungeon> dungeons = dungeonService.findAllByAccount(account, hardcore, ironborn);
		model.addAttribute("dungeons", dungeons);
		int accountLevel = accountService.getMaxCharacterLevel(account, hardcore, ironborn);
		model.addAttribute("refreshCost", accountLevel);
		model.addAttribute("magicFindBoostExpires", accountBoostService.getMagicFindBoostExpires(account, hardcore, ironborn));
		model.addAttribute("xpBoostExpires", accountBoostService.getXpBoostExpires(account, hardcore, ironborn));
		model.addAttribute("localMode", localMode);
		
		return "play/dungeons";
	}
	
	/**
	 * Look at the details of a dungeon
	 */
	@RequestMapping(value = "/{dungeonId}", method = RequestMethod.GET)
	public String getDungeonDetails(Locale locale, Model model, Principal principle, 
			@PathVariable long dungeonId,
			@RequestParam(value="fullPage", required=false) Boolean fullPage
			) {
		logger.debug("Dungeon Details");
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		// Make sure we're processed
		accountService.processAccount(account);
		
		Dungeon dungeon = dungeonService.findById(dungeonId);
		
		if(dungeon == null || !dungeon.getAccount().equals(account)) {
			model.addAttribute("error", "Dungeon not found");
			ViewTypeForm viewTypeForm = new ViewTypeForm(hardcore, ironborn, false);
			return getDungeons(locale, model, principle, viewTypeForm, null);
		}
		hardcore = dungeon.isHardcore();
		ironborn = dungeon.isIronborn();
		setUpViewTypeModel(model);

		List<DungeonEvent> dungeonEvents = dungeonEventService.findAllByDungeon(dungeon);
		model.addAttribute("dungeon", dungeon);
		model.addAttribute("dungeonEvents", dungeonEvents);
		model.addAttribute("closable", (
						(dungeon.getItemRewards().isEmpty() && dungeon.getBoostItemRewards().isEmpty()) || 
						dungeon.isFailed()
				) && 
				dungeon.isFinished());
		model.addAttribute("fullPage", fullPage);
		model.addAttribute("magicFindBoostExpires", accountBoostService.getMagicFindBoostExpires(account, hardcore, ironborn));
		model.addAttribute("xpBoostExpires", accountBoostService.getXpBoostExpires(account, hardcore, ironborn));
		BoostItem usableRushBoost = boostItemService.getValidBoostItemType(
				account, hardcore, ironborn, BoostItemType.DUNGEON_SPEED, dungeon.getRushCostBoostItemLevel(), false);
		if(usableRushBoost != null) {
			model.addAttribute("usableRushBoost", usableRushBoost);
		}

		return "play/dungeonDetails";
	}
	
	@RequestMapping(value="/prepareAdventure", method = RequestMethod.GET)
	public String prepareAdventure(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Prepare Adventure");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}

		model.addAttribute("dungeonType", DungeonType.ADVENTURE);
		model.addAttribute("dungeonAdventureStartForm", new DungeonAdventureStartForm());
		model.addAttribute("magicFindBoostExpires", accountBoostService.getMagicFindBoostExpires(account, hardcore, ironborn));
		model.addAttribute("xpBoostExpires", accountBoostService.getXpBoostExpires(account, hardcore, ironborn));
		
		return "play/dungeonDetailsAdventure";
	}
	
	@RequestMapping(value="/createAdventure", method = RequestMethod.POST)
	public String createAdventure(Locale locale, Model model, Principal principle,
			@ModelAttribute("dungeonAdventureStartForm") @Valid DungeonAdventureStartForm dungeonAdventureStartForm, 
			BindingResult bindingResult) {
		logger.debug("Create Adventure");
		hardcore = dungeonAdventureStartForm.isHardcore(); // TODO: Do we need this
		ironborn = dungeonAdventureStartForm.isIronborn(); // TODO: Do we need this
		setUpViewTypeModel(model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		if (bindingResult.hasErrors()) {
			return getDungeons(locale,model, principle, dungeonAdventureStartForm.asViewTypeForm(), bindingResult);
        }

		// Find the lowest character level in the party
		int level = Character.MAX_LEVEL;
		long[] characterIds = dungeonAdventureStartForm.getCharacterIds();
		List<Character> characters = new ArrayList<Character>();
		for(long characterId:characterIds) {
			if(characterId > 0) {
				Character character = characterService.findById(characterId);
				if(character != null && account.equals(character.getAccount()) && character.getDungeon() == null) {
					characters.add(character);
					if(level > character.getLevel()) {
						level = character.getLevel();
					}
				}
			}
		}
		if(characters.size() < 1) {
			model.addAttribute("error", "Not enough valid characters");
			return getDungeons(locale,model, principle, dungeonAdventureStartForm.asViewTypeForm(), bindingResult);
		}
		
		long xpReward = DungeonType.ADVENTURE.getXpBase() * level;
		
		if(accountBoostService.getXpBoostExpires(account, dungeonAdventureStartForm.isHardcore(), dungeonAdventureStartForm.isIronborn()) != null) {
			logger.debug("Applying XP boost");
			xpReward *= 2;
		}
		
		Map<Monster, MonsterType> monsters = dungeonService.generateMonsters(level, DungeonType.ADVENTURE, characters.size());
		Dungeon dungeon = new Dungeon(-1, DungeonType.ADVENTURE, account, 
				dungeonAdventureStartForm.isHardcore(), dungeonAdventureStartForm.isIronborn(), level , 
				xpReward, 
				monsters, 
				characters.size(),
				0);
		try {
			dungeon.setCharacters(characters);
		} catch (AccountIdMismatch e) {
			model.addAttribute("error", "Internal error h4x4xA");
			return getDungeons(locale,model, principle, dungeonAdventureStartForm.asViewTypeForm(), bindingResult);
		} catch (DifferentGameStates e) {
			model.addAttribute("error", "Internal error h4x4xHI");
			return getDungeons(locale,model, principle, dungeonAdventureStartForm.asViewTypeForm(), bindingResult);
		}
		// No items rewarded
		dungeon.setItemRewards(new HashMap<Equipment, Boolean>());
		dungeon = dungeonService.create(dungeon);
		
		DungeonStartForm dungeonStartForm = new DungeonStartForm(dungeon);
		dungeonStartForm.setPanelCharacterId(dungeonAdventureStartForm.getPanelCharacterId());
		dungeonStartForm.setHardcore(hardcore);
		dungeonStartForm.setIronborn(ironborn);

		return startDungeon(locale, model, principle, dungeon.getId(), dungeonStartForm, bindingResult);
	}
	

	
	@RequestMapping(value="/allAdventure", method = RequestMethod.GET)
	@Transactional
	public String createAllAdventure(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Create Adventures with all idle characters");
		setUpViewTypeModel(model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			setupModelAttributes(model, viewTypeForm);
			
			return "play/index";
		}
		if (bindingResult.hasErrors()) {
			setupModelAttributes(model, viewTypeForm);
			
			return "play/index";
        }

		// Find all the characters not in a dungeon and group them by level
		List<Character> idleCharacters = characterService.findAllIdleByAccountOrderByLevel(account, hardcore, ironborn);
		Map<Integer, List<Character>> idleCharactersbyLevel = new HashMap<Integer, List<Character>>();
		for(Character character:idleCharacters) {
			if(idleCharactersbyLevel.containsKey(character.getLevel())) {
				List<Character> existingList = idleCharactersbyLevel.get(character.getLevel());
				existingList.add(character);
				idleCharactersbyLevel.put(character.getLevel(), existingList);
			} else {
				List<Character> newList = new ArrayList<Character>();
				newList.add(character);
				idleCharactersbyLevel.put(character.getLevel(), newList);
			}
		}
		
		for(Integer level:idleCharactersbyLevel.keySet()) {
			List<Character> charactersAtLevel = idleCharactersbyLevel.get(level);
			while(charactersAtLevel.size() > 0) {
				List<Character> characters = new ArrayList<Character>();
				while(charactersAtLevel.size() > 0 && characters.size() < 4) {
					characters.add(charactersAtLevel.remove(0));
				}
				
				long[] characterIds = new long[characters.size()];
				for(int i=0; i < characters.size(); i++) {
					characterIds[i] = characters.get(i).getId();					
				}
				
				long xpReward = DungeonType.ADVENTURE.getXpBase() * level;
				
				Map<Monster, MonsterType> monsters = dungeonService.generateMonsters(level, DungeonType.ADVENTURE, characters.size());
				Dungeon dungeon = new Dungeon(-1, DungeonType.ADVENTURE, account, 
						viewTypeForm.isHardcore(), viewTypeForm.isIronborn(), level , 
						xpReward, 
						monsters, 
						characters.size(),
						0);
				try {
					dungeon.setCharacters(characters);
				} catch (AccountIdMismatch e) {
					model.addAttribute("error", "Internal error h4x4xA");
	
					setupModelAttributes(model, viewTypeForm);
					
					return "play/index";
				} catch (DifferentGameStates e) {
					model.addAttribute("error", "Internal error h4x4xHI");
	
					setupModelAttributes(model, viewTypeForm);
					
					return "play/index";
				}
				// No items rewarded
				dungeon.setItemRewards(new HashMap<Equipment, Boolean>());
				dungeon = dungeonService.create(dungeon);
				
				try {
					dungeon = initialiseDungeon(characterIds, account, dungeon, 0);
				} catch (DungeonNotRunable e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					model.addAttribute("error", "Dungeon not runnable");
					setupModelAttributes(model, viewTypeForm);
					
					return "play/index";
				} catch (CharacterNotFound e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					model.addAttribute("error", "Character not found");
					setupModelAttributes(model, viewTypeForm);
					
					return "play/index";
				} catch (AccountIdMismatch e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					model.addAttribute("error", "Account not correct");
					setupModelAttributes(model, viewTypeForm);
					
					return "play/index";
				} catch (DungeonNotFound e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					model.addAttribute("error", "Dungeon not found");
					setupModelAttributes(model, viewTypeForm);
					
					return "play/index";
				} catch (EquipmentNotFound e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					model.addAttribute("error", "Character equipment not found");
					setupModelAttributes(model, viewTypeForm);
					
					return "play/index";
				} catch (DifferentGameStates e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					model.addAttribute("error", "Hardcore or Ironborn mismatch");
					setupModelAttributes(model, viewTypeForm);
					
					return "play/index";
				} catch (BoostItemNotFound e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					model.addAttribute("error", "Character boost item not found");
					setupModelAttributes(model, viewTypeForm);
					
					return "play/index";
				}
			}
		}
		
		setupModelAttributes(model, viewTypeForm);
		
		return "play/index";
	}

	@RequestMapping(value="/{dungeonId}/start", method = RequestMethod.POST)
	@Transactional
	public String startDungeon(Locale locale, Model model, Principal principle, @PathVariable long dungeonId,
			@ModelAttribute("dungeonStartForm") @Valid DungeonStartForm dungeonStartForm, BindingResult bindingResult) {
		logger.debug("Start Dungeon {}", dungeonId);
		Account account = accountService.findByPrinciple(principle);
		Dungeon dungeon = dungeonService.findById(dungeonId);
		
		// We are doing a full page reload, as dungeon, characters and character details all need updating
		if(account == null || dungeon == null || !dungeon.getAccount().equals(account)) {
			model.addAttribute("error", "Dungeon not found");
			setupModelAttributes(model, dungeonId, dungeonStartForm);
			
			return "play/index";
		}
		if (bindingResult.hasErrors()) {
			return getDungeonDetails(locale, model, principle, dungeonId, false);
        }
		hardcore = dungeon.isHardcore();
		ironborn = dungeon.isIronborn();
		setUpViewTypeModel(model);
		
		try {
			dungeon = initialiseDungeon(dungeonStartForm.getCharacterIds(), account, dungeon, 
					dungeonStartForm.getDifficultyModifier());
		} catch (DungeonNotRunable e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Dungeon not runnable");
			setupModelAttributes(model, dungeonStartForm, dungeon);
			
			return "play/index";
		} catch (CharacterNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Character not found");
			setupModelAttributes(model, dungeonStartForm, dungeon);
			
			return "play/index";
		} catch (AccountIdMismatch e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Account not correct");
			setupModelAttributes(model, dungeonStartForm, dungeon);
			
			return "play/index";
		} catch (DungeonNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Dungeon not found");
			setupModelAttributes(model, dungeonStartForm, dungeon);
			
			return "play/index";
		} catch (EquipmentNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Character equipment not found");
			setupModelAttributes(model, dungeonStartForm, dungeon);
			
			return "play/index";
		} catch (DifferentGameStates e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Hardcore or Ironborn mismatch");
			setupModelAttributes(model, dungeonStartForm, dungeon);
			
			return "play/index";
		} catch (BoostItemNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Character boost item not found");
			setupModelAttributes(model, dungeonStartForm, dungeon);
			
			return "play/index";
		}
		
		setupModelAttributes(model, dungeonStartForm, dungeon);
		
		return "play/index";
	}

	/**
	 * Initialise and start the dungeon
	 * 
	 * @param characterIds
	 * @param account
	 * @param dungeon
	 * @param difficultyModifier
	 * @return
	 * @throws CharacterNotFound
	 * @throws AccountIdMismatch
	 * @throws DungeonNotFound
	 * @throws DungeonNotRunable
	 * @throws EquipmentNotFound
	 * @throws DifferentGameStates
	 * @throws BoostItemNotFound 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={
			CharacterNotFound.class, AccountIdMismatch.class, DungeonNotFound.class, DungeonNotRunable.class, 
			EquipmentNotFound.class, BoostItemNotFound.class})
	protected Dungeon initialiseDungeon(long[] characterIds, Account account, Dungeon dungeon, int difficultyModifier)
			throws CharacterNotFound, AccountIdMismatch, DungeonNotFound, DungeonNotRunable, EquipmentNotFound,
					DifferentGameStates, BoostItemNotFound {
		DungeonDifficulty difficulty = dungeon.getDifficulty(difficultyModifier);
		if(difficulty.getCost() > 0) {
			String reference = "dungeon-upgrade-"+account.getUsername()+"-"+dungeon.getId();			
			accountCurrencyService.adjustCurrency(account, hardcore, ironborn, 
					-difficulty.getCost(), ModificationType.SPEND_DUNGEON_LEVEL_INCREASE, reference);
		}
		if(accountBoostService.getXpBoostExpires(account, hardcore, ironborn) != null) {
			logger.debug("Applying XP boost");
			dungeon.setXpReward(dungeon.getXpReward() * 2);
		}
		dungeon = dungeonService.startDungeon(dungeon, characterIds, difficulty.getLevelAdjustment(),
				equipmentService, characterEquipmentService);
		// First send me an email if things look weird
		if(dungeon.isFailed()) {
			List<Character> allCharacters = dungeon.getCharacters(false);
			int validCharacters = 0;
			for(Character character:allCharacters) {
				if(character.getLevel() >= dungeon.getLevel()) {
					validCharacters++;
				}
			}
			if(dungeon.getType().getMinCharacters() <= validCharacters) {
				StringBuilder emailBody = new StringBuilder();
				emailBody.append(dungeon.toString(false, true));
				emailBody.append("\n\n");
				List<DungeonEvent> dungeonEvents = dungeonEventService.findAllByDungeon(dungeon);
				for(DungeonEvent dungeonEvent:dungeonEvents) {
					emailBody.append(dungeonEvent.toString(false, true));
					emailBody.append("\n");
				}
				emailService.sendPlainTextEmail(EmailService.ADMIN_EMAIL_ADDRESS, 
						"Dungeon Anomoly", 
						emailBody.toString());
			}
		}
		accountService.processDungeons(account, dungeon.getDoneDate());
		return dungeon;
	}
	
	@RequestMapping(value="/{dungeonId}/close", method = RequestMethod.GET)
	@Transactional
	public String closeDungeon(HttpServletRequest request, Locale locale, Model model, Principal principle, @PathVariable long dungeonId,
			@ModelAttribute("charId") long characterId) {
		logger.debug("Close Dungeon {}", dungeonId);
		Account account = accountService.findByPrinciple(principle);
		Dungeon dungeon = dungeonService.findById(dungeonId);

		if(account == null || dungeon == null || !dungeon.getAccount().equals(account)) {
			model.addAttribute("error", "Dungeon not found");
			model.addAttribute("dungeonId", dungeonId);
			model.addAttribute("charId", characterId);
			setUpViewTypeModel(model);
			
			return "play/index";
		}
		
		hardcore = dungeon.isHardcore();
		ironborn = dungeon.isIronborn();
		setUpViewTypeModel(model);
		
		if(!dungeon.isFinished() || 
					( !dungeon.isFailed() &&
							( !dungeon.getItemRewards().isEmpty() || !dungeon.getBoostItemRewards().isEmpty() )
					)
				) {
			return reloadPage(model, characterId, dungeon);
		}
		int newAchievementPoints = 0;
		if(!dungeon.isFailed()) {
			List<Character> aliveCharacters = dungeon.getCharacters(true);
			if(aliveCharacters.size() > 0) {
				long xpEach = dungeon.getXpReward() / aliveCharacters.size();
				for(Character character:aliveCharacters) {
					if(character.giveXp(xpEach) > 0) {
						accountMessageService.messageLevelUp(request.getContextPath(), character.getId(), account, character);
						List<Achievement> newAchievements = achievementService.levelUp(request.getContextPath(), character);
						for(Achievement achievement:newAchievements) {
							newAchievementPoints += achievement.getPoints();
						}
					}
				}
			}
			String reference = "dungeon-close-"+account.getUsername()+"-"+dungeonId;
			
			accountCurrencyService.adjustCurrency(account, hardcore, ironborn, 
					dungeon.getRewardTokens(), ModificationType.GAIN_DUNGEON_REWARD, reference);
			hiscoreService.tokensEarnt(account, hardcore, ironborn, dungeon.getLevel());
		} else {
			if(characterService.allAccountCharactersDead(account, hardcore, ironborn)) {
				List<Character> characters = characterService.findAllByAccountOrderByLevel(account, hardcore, ironborn);
				Character character = characters.get(characters.size()-1);
				character.setAlive();
				try {
					characterService.update(character);
				} catch (CharacterNotFound e) {
					logger.error("Character {} not found during free character resurection on close dungeon {}", account, dungeon);
					
					return reloadPage(model, characterId, dungeon);
				}
				model.addAttribute("error", "All your characters were dead, so we resurrected "+character.getName()+" for free");
			}
		}
		List<Character> allCharacters = dungeon.getCharacters(false);
		if(allCharacters.size() > 0) {			
			long xpEach = dungeon.getKilledXp() / allCharacters.size();
			if(accountBoostService.getXpBoostExpires(account, hardcore, hardcore) != null) {
				logger.debug("Applying XP boost");
				xpEach *= 2;
			}
			for(Character character:allCharacters) {
				character.setDungeon(null);
				int levelsGained = character.giveXp(xpEach);
				try {
					characterService.update(character);
				} catch (CharacterNotFound e) {
					logger.error("Character {} not found during update on close dungeon {}", character, dungeon);
					
					return reloadPage(model, characterId, dungeon);
				}
				if(levelsGained> 0) {
					List<Achievement> newAchievements = achievementService.levelUp(request.getContextPath(), character);
					for(Achievement achievement:newAchievements) {
						newAchievementPoints += achievement.getPoints();
					}
				}
			}
		} else {
			logger.error("Dungeon {} had no characters as close", dungeon);
		}
		
		try {
			dungeonService.delete(dungeon);
			// Is there space to create a new dungeon, because of this one closing and maybe the max character level increasing?
			accountService.createDungeons(account);
		} catch (DungeonNotFound e) {
			logger.error("Dungeon not found during dungeon delete {}", dungeon);
			
			return reloadPage(model, characterId, dungeon);
		} catch (EquipmentNotFound e) {
			logger.error("Equipment not found during dungeon delete {}", dungeon);
			
			return reloadPage(model, characterId, dungeon);
		}
		
		if(newAchievementPoints > 0) {
			hiscoreService.achievement(account, newAchievementPoints);
		}
		List<Achievement> newAchievements = achievementService.dungeonClose(request.getContextPath(), dungeon);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
		
		model.addAttribute("dungeonId", 0);
		model.addAttribute("charId", characterId);
		
		return "play/index";
	}
	
	@RequestMapping(value="/{dungeonId}/rush", method = RequestMethod.GET)
	public String rushDungeon(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long dungeonId) {
		logger.debug("Rush Dungeon {}", dungeonId);
		Account account = accountService.findByPrinciple(principle);
		Dungeon dungeon = dungeonService.findById(dungeonId);
		
		// We are doing a full page reload, as dungeon, characters and character details all need updating
		if(account == null || dungeon == null || !dungeon.getAccount().equals(account)) {
			model.addAttribute("error", "Dungeon not found");
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		}
		hardcore = dungeon.isHardcore();
		ironborn = dungeon.isIronborn();
		setUpViewTypeModel(model);
		
		// Reduce tokens
		int rushCost = dungeon.getRushCost();
		if(rushCost < 1) {
			model.addAttribute("error", 
					"No rush cost.");
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		}
		String reference = "dungeon-rush-"+account.getUsername()+"-"+dungeonId;			
		if(!accountCurrencyService.adjustCurrency(account, dungeon.isHardcore(), dungeon.isIronborn(), -rushCost, 
				ModificationType.SPEND_RUSH, reference)) {
			model.addAttribute("error", 
					"Not enough dungeon tokens to purchase a rush for that dungeon yet. You need " + rushCost + " tokens.");
		    return "play/addTokens";
		}
		
		// Adjust the started date to finishes it 1 millisecond before now
		Calendar newStarted = Calendar.getInstance();
		newStarted.setTime(new Date());
		newStarted.add(Calendar.MINUTE, -dungeon.getLevel());
		newStarted.add(Calendar.MILLISECOND, -1);
		dungeon.setStarted(newStarted.getTime());
				
		try {
			// Check for dead characters
			List<Character> characters = dungeon.getCharacters();
			for(Character character:characters) {
				if(character.getDeathTime() != null) {
					character.setDiedTime(new Date());
					characterService.update(character);
				}
			}
			
			dungeon = dungeonService.update(dungeon);			
			
			// Finally process the dungeon
			accountService.processDungeons(account, dungeon.getDoneDate());
		} catch (AccountIdMismatch e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // TODO: Change
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		} catch (DungeonNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // TODO: Change
			model.addAttribute("dungeonId", dungeon.getId());
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		} catch (EquipmentNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // TODO: Change
			model.addAttribute("dungeonId", dungeon.getId());
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		} catch (CharacterNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // TODO: Change
			model.addAttribute("dungeonId", dungeon.getId());
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		} catch (BoostItemNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // TODO: Change
			model.addAttribute("dungeonId", dungeon.getId());
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		}
		List<Achievement> newAchievements = achievementService.rushDungeon(request.getContextPath(), dungeon);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}

		return getDungeonDetails(locale, model, principle, dungeonId, false);
	}
	
	@RequestMapping(value="/{dungeonId}/rush/boostItem", method = RequestMethod.GET)
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {InventoryNotFound.class})
	public String rushDungeonWithBoostItem(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long dungeonId) throws InventoryNotFound {
		logger.debug("Rush Dungeon {}", dungeonId);
		Account account = accountService.findByPrinciple(principle);
		Dungeon dungeon = dungeonService.findById(dungeonId);
		
		// We are doing a full page reload, as dungeon, characters and character details all need updating
		if(account == null || dungeon == null || !dungeon.getAccount().equals(account)) {
			model.addAttribute("error", "Dungeon not found");
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		}
		hardcore = dungeon.isHardcore();
		ironborn = dungeon.isIronborn();
		setUpViewTypeModel(model);
		
		// Reduce tokens
		int rushCost = dungeon.getRushCost();
		if(rushCost < 1) {
			model.addAttribute("error", 
					"No rush cost.");
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		}
		
		BoostItem boostItem = boostItemService.getValidBoostItemType(
				account, hardcore, ironborn, BoostItemType.DUNGEON_SPEED, dungeon.getRushCostBoostItemLevel(), false);
		
		if(boostItem == null || !boostItem.getAccount().equals(account) && boostItem.isHardcore() == hardcore) {
			model.addAttribute("error", "No valid boost found. You probably waited too long.");
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		}
		
		if(boostItem.getBoostItemType() != BoostItemType.DUNGEON_SPEED) {
			model.addAttribute("error", "Found Boost Item of wrong type, please try again.");
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		}
		
		if(boostItem.getLevel() > dungeon.getRushCostBoostItemLevel()) {
			model.addAttribute("error", "Found Item too high level, please try again.");
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		}
		
		if(boostItem.isHardcore() != hardcore || boostItem.isIronborn() != ironborn) {
			model.addAttribute("error", "Found boost item of incorrect mode");
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		}
		
		// Adjust the started date to finishes it 1 millisecond before now
		Calendar newStarted = Calendar.getInstance();
		newStarted.setTime(dungeon.getStarted());
		// Take off 30 seconds per level of the boost item
		int secondsToAdjust = (boostItem.getLevel() * 30);
		newStarted.add(Calendar.SECOND, -secondsToAdjust);
		dungeon.setStarted(newStarted.getTime());
				
		try {
			// Check for dead characters
			List<Character> characters = dungeon.getCharacters();
			for(Character character:characters) {
				if(character.getDeathTime() != null) {
					Calendar newDeathTime = Calendar.getInstance();
					newDeathTime.setTime(character.getDeathTime());
					newDeathTime.add(Calendar.SECOND, -secondsToAdjust);
					character.setDiedTime(newDeathTime.getTime());
					characterService.update(character);
				}
			}
			
			boostItemService.delete(account, boostItem);
			dungeon = dungeonService.update(dungeon);
		} catch (AccountIdMismatch e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // TODO: Change
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		} catch (DungeonNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // TODO: Change
			model.addAttribute("dungeonId", dungeon.getId());
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		} catch (EquipmentNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // TODO: Change
			model.addAttribute("dungeonId", dungeon.getId());
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		} catch (CharacterNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // TODO: Change
			model.addAttribute("dungeonId", dungeon.getId());
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		} catch (BoostItemNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", e.getMessage()); // TODO: Change
			model.addAttribute("dungeonId", dungeon.getId());
			return getDungeonDetails(locale, model, principle, dungeonId, false);
		}
		List<Achievement> newAchievements = achievementService.rushDungeon(request.getContextPath(), dungeon);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}

		return getDungeonDetails(locale, model, principle, dungeonId, false);
	}
	
	@RequestMapping(value="/refresh", method = RequestMethod.GET)
	public String rushDungeon(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Refresh Dungeons");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		
		if(account == null) {
			model.addAttribute("error", "Account not found");
			return getDungeons(locale, model, principle, viewTypeForm, bindingResult);
		}
		
		int refreshCost = accountService.getMaxCharacterLevel(account, hardcore, ironborn);
		// Reduce tokens
		if(refreshCost < 1) {
			model.addAttribute("error", 
					"No refresh cost.");
			return getDungeons(locale, model, principle, viewTypeForm, bindingResult);
		}
		String reference = "dungeon-refresh-"+account.getUsername()+(hardcore?"-hardcore":"")+(ironborn?"-ironborn":"");			
		if(!accountCurrencyService.adjustCurrency(account, hardcore, ironborn, -refreshCost, 
				ModificationType.SPEND_DUNGEON_REFRESH, reference)) {
			model.addAttribute("error", 
					"Not enough dungeon tokens to purchase a refresh of dungeons. You need " + refreshCost + " tokens.");
		    return "play/addTokens";
		}
		
		dungeonService.deleteAllByAccountAndHardcoreAndIronborn(account, hardcore, ironborn);
		
		// Finally process the account to add all the dungeons back
		accountService.processAccount(account);
		
		return getDungeons(locale, model, principle, viewTypeForm, bindingResult);
	}
	
	@RequestMapping(value="/{dungeonId}/stashAll", method = RequestMethod.GET)
	@Transactional
	public String stashAllDungeon(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long dungeonId,
			@ModelAttribute("charId") long characterId) {
		logger.debug("Stash All Dungeon {}", dungeonId);
		Account account = accountService.findByPrinciple(principle);
		Dungeon dungeon = dungeonService.findById(dungeonId);
		
		// We are doing a full page reload, as dungeon, characters and character details all need updating
		if(account == null || dungeon == null || !dungeon.getAccount().equals(account)) {
			model.addAttribute("error", "Dungeon not found");
			return reloadPage(model, characterId, dungeon);
		}
		hardcore = dungeon.isHardcore();
		ironborn = dungeon.isIronborn();
		setUpViewTypeModel(model);
		
		if(dungeon.isFailed() || !dungeon.isFinished()) {
			model.addAttribute("error", "You can't claim that equipment");
			return reloadPage(model, characterId, dungeon);
		}
		
		// For each equipment move to empty stash slot
		// Need a copy of the set, as removing equipment from dungeon would modify this set otherwise
		Set<StashSlotItemSuper> items = new HashSet<StashSlotItemSuper>(dungeon.getItemRewardsAsSet());
		items.addAll(dungeon.getBoostItemRewards());
		Inventory inventory = inventoryService.findByAccount(account, hardcore, ironborn);
		int stashSize = inventory.getSize();
		int stashSlotId = 0;
		Map<Integer, StashSlotItemSuper> inventorySlots = inventory.getInventorySlots();
		for(StashSlotItemSuper item: items) {
			// Check for empty stash slot
			while(stashSlotId < stashSize && inventorySlots.get(stashSlotId) != null) {
				stashSlotId++;
			}
			if(stashSlotId >= stashSize) {
				// No More Free Slots
				return reloadPage(model, characterId, dungeon);
			}
			// Remove equipment from existing locations first
			if(item instanceof Equipment) {
				dungeon.removeItemReward((Equipment)item);
			} else if(item instanceof BoostItem) {
				dungeon.removeBoostItemReward((BoostItem)item);
			} else {
				logger.error("Item of unknown type {}", item);
			}
			try {
				dungeonService.update(dungeon);
			} catch (DungeonNotFound e) {
				model.addAttribute("error", "Dungeon not found");
				logger.error("Update dungeon failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (AccountIdMismatch e) {
				model.addAttribute("error", "Not your account");
				logger.error("Update dungeon failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (EquipmentNotFound e) {
				model.addAttribute("error", "Equipment not found");
				logger.error("Update dungeon failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (BoostItemNotFound e) {
				model.addAttribute("error", "Boost Item not found");
				logger.error("Update dungeon failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			}
			
			if(item instanceof Equipment) {
				Equipment equipment = (Equipment)item;
				// Then update equipment to it's new location
				equipment.setEquipmentLocation(EquipmentLocation.INVENTORY);
				equipment.setEquipmentLocationId(stashSlotId);
			} else if(item instanceof BoostItem) {
				BoostItem boostItem = (BoostItem)item;
				// Then update equipment to it's new location
				boostItem.setDungeonId(-1);
				boostItem.setStashSlotId(stashSlotId);
			} else {
				logger.error("Item of unknown type {}", item);
			}
			
			// Then add it in the new location in the stash
			try {
				inventoryService.putItemInSlot(account, item, stashSlotId);
			} catch (InventoryNotFound e) {
				model.addAttribute("error", "Inventory not found");
				logger.error("Update stash slot failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			}

			// Save the equipment location change
			try {
				if(item instanceof Equipment) {
					equipmentService.update((Equipment)item, false);
				} else if(item instanceof BoostItem) {
					boostItemService.update((BoostItem)item);
				} else {
					logger.error("Item of unknown type {}", item);
				}
			} catch (EquipmentNotFound e) {
				model.addAttribute("error", "Equipment not found");
				logger.error("Update equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (BoostItemNotFound e) {
				model.addAttribute("error", "Boost Item not found");
				logger.error("Update boost item failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			}
			stashSlotId++;
		}

		return closeDungeon(request, locale, model, principle, dungeonId, characterId);
	}
	
	@RequestMapping(value="/{dungeonId}/salvageAll", method = RequestMethod.GET)
	@Transactional
	public String salvageAllDungeon(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long dungeonId,
			@ModelAttribute("charId") long characterId) {
		logger.debug("Salvage All Dungeon {}", dungeonId);
		Account account = accountService.findByPrinciple(principle);
		Dungeon dungeon = dungeonService.findById(dungeonId);
		
		// We are doing a full page reload, as dungeon, characters and character details all need updating
		if(account == null || dungeon == null || !dungeon.getAccount().equals(account)) {
			model.addAttribute("error", "Dungeon not found");
			return reloadPage(model, characterId, dungeon);
		}
		hardcore = dungeon.isHardcore();
		ironborn = dungeon.isIronborn();
		setUpViewTypeModel(model);
		
		if(dungeon.isFailed() || !dungeon.isFinished()) {
			model.addAttribute("error", "You can't claim that equipment");
			return reloadPage(model, characterId, dungeon);
		}
		
		// For each item, salvage
		Set<StashSlotItemSuper> items = new HashSet<StashSlotItemSuper>(dungeon.getItemRewardsAsSet());
		items.addAll(dungeon.getBoostItemRewards());
		for(StashSlotItemSuper item:items) {
			try {
				if(item instanceof Equipment) {
					Equipment equipment = (Equipment) item;
					equipmentService.delete(account, equipment);
					String reference = "api-salvage-equipment-"+account.getUsername()+"-"+item.getId();	
					int salvageValue = equipment.getSalvageValue();
					accountCurrencyService.adjustCurrency(account, item.isHardcore(),item.isIronborn(), 
							salvageValue, 
							ModificationType.GAIN_SALVAGE, reference);
					hiscoreService.tokensEarnt(account, item.isHardcore(),item.isIronborn(), salvageValue);
				} else if(item instanceof BoostItem) {
					BoostItem boostItem = (BoostItem) item;
					boostItemService.delete(account, boostItem);
					int salvageValue = boostItem.getSalvageValue();
					String reference = "api-salvage-boost-"+account.getUsername()+"-"+item.getId();			
					accountCurrencyService.adjustCurrency(account, item.isHardcore(),item.isIronborn(), 
							salvageValue, 
							ModificationType.GAIN_SALVAGE, reference);
					hiscoreService.tokensEarnt(account, item.isHardcore(),item.isIronborn(), salvageValue);
				} else {
					logger.error("Item of unknown type {}", item);
				}
			} catch (EquipmentNotFound e) {
				model.addAttribute("error", "Equipment not found");
				logger.error("Salvage equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (DungeonNotFound e) {
				model.addAttribute("error", "Dungeon not found");
				logger.error("Salvage equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (AccountIdMismatch e) {
				model.addAttribute("error", "Not your account");
				logger.error("Salvage equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (InventoryNotFound e) {
				model.addAttribute("error", "Inventory missing");
				logger.error("Salvage equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (CharacterNotFound e) {
				model.addAttribute("error", "Character not found");
				logger.error("Salvage equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (CharacterSlotNotFound e) {
				model.addAttribute("error", "Character Slot not found");
				logger.error("Salvage equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (CharacterEquipmentNotFound e) {
				model.addAttribute("error", "Character Equipment not found");
				logger.error("Salvage equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (BoostItemNotFound e) {
				model.addAttribute("error", "Boost Item not found");
				logger.error("Salvage equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (MessageEquipmentNotFound e) {
				model.addAttribute("error", "Message Item not found");
				logger.error("Salvage equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			} catch (InventoryException e) {
				model.addAttribute("error", "Inventory error");
				logger.error("Salvage equipment failed: {}", e.getMessage());
				return reloadPage(model, characterId, dungeon);
			}
		}
		
		return closeDungeon(request, locale, model, principle, dungeonId, characterId);
	}

	protected String reloadPage(Model model, long characterId, Dungeon dungeon) {
		if(dungeon != null) {
			model.addAttribute("dungeonId", dungeon.getId());
		} else {
			model.addAttribute("dungeonId", -1);
		}
		model.addAttribute("charId", characterId);
		
		return "play/index";
	}

	protected void setupModelAttributes(Model model, long dungeonId, DungeonStartForm dungeonStartForm) {
		model.addAttribute("dungeonId", dungeonId);
		model.addAttribute("charId", dungeonStartForm.getPanelCharacterId());
	}

	protected void setupModelAttributes(Model model, DungeonStartForm dungeonStartForm, Dungeon dungeon) {
		model.addAttribute("dungeonId", dungeon.getId());
		model.addAttribute("charId", dungeonStartForm.getPanelCharacterId());
	}
	
	protected void setupModelAttributes(Model model, ViewTypeForm viewTypeForm) {
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		model.addAttribute("charId", viewTypeForm.getCharId());
	}
	
	@ModelAttribute("now")
    public Date getNow(Principal principle) {
		return new Date();
	}
}
