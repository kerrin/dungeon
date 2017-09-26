package org.kerrin.dungeon.task;

import java.util.List;
import java.util.Map;

import org.kerrin.dungeon.enums.AccountTask;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.service.AccountBoostService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generate found items for a finished dungeon
 * 
 * 
 * @author Kerrin
 *
 */
@Configurable
public class DungeonProcessorTask extends SuperAccountTask {
	private static final Logger logger = LoggerFactory.getLogger(DungeonProcessorTask.class);

	/** Number of chances to find additional equipment in a successful dungeon */
	public static final int FOUND_EQUIPMENT_CHANCES = 5;
	
	/** Chance in 1000 that an additional item is found for each check */
	public static final int BASE_MAGIC_FIND = 100;
	
	/** Number of chances to find boost items in a successful dungeon */
	public static final int FOUND_BOOST_ITEM_CHANCES = 1;
	
	/** Chance in 1000 of boost item being found each time */
	public static final int FIND_BOOST_ITEM_CHANCE = 5;
	
	private DungeonService dungeonService;
	
	private EquipmentService equipmentService;
	
	private BoostItemService boostItemService;
	
	private AccountBoostService accountBoostService;
	
	private CharacterEquipmentService characterEquipmentService;
	
	protected DungeonProcessorTask() {}
	
	public DungeonProcessorTask(
			AccountService accountService,
			DungeonService dungeonService, 
			EquipmentService equipmentService, 
			BoostItemService boostItemService,
			AccountBoostService accountBoostService,
			CharacterEquipmentService characterEquipmentService,
			Account account) {
		super(accountService, account, AccountTask.DUNGEON_PROCESSING);
		this.dungeonService = dungeonService;
		this.equipmentService = equipmentService;
		this.boostItemService = boostItemService;
		this.accountBoostService = accountBoostService;
		this.characterEquipmentService = characterEquipmentService;
	}
	
	/**
	 * Actually process the dungeons
	 */
	@Transactional
	@Override
	protected void realRun() {
		// Process dungeons
		List<Dungeon> dungeons = dungeonService.findAllByAccountAndFinished(account);
		for(Dungeon dungeon:dungeons) {
			if(!dungeon.hasGeneratedFoundItemRewards()) {
				List<Character> chars = dungeon.getCharacters(true);				
				if(chars.size() >= 1) {
					int magicFind = BASE_MAGIC_FIND;
					for(Character thisChar:chars) {
						CharacterEquipment thisCharsEquipment = characterEquipmentService.findById(thisChar.getId());
						magicFind += thisCharsEquipment.getTotalAttributeValue(EquipmentAttribute.MAGIC_FIND);
					}
					if(accountBoostService.getMagicFindBoostExpires(account, dungeon.isHardcore(), dungeon.isIronborn()) != null) {
						logger.debug("Applying magic find boost");
						magicFind *= 2;
					}
					findEquipment(dungeon, chars, magicFind);
					findBoostItems(dungeon, chars);
				}
			}
		}
	}

	/**
	 * Check if the player found additional equipment
	 * 
	 * @param dungeon
	 * @param chars
	 * @param magicFind
	 */
	protected void findEquipment(Dungeon dungeon, List<Character> chars, int magicFind) {
		int items = 0;
		if(dungeon.getType() == DungeonType.ADVENTURE) {
			items += chars.size();
		}
		for(int chance=0; chance < FOUND_EQUIPMENT_CHANCES; chance++) {
			if(((int)(Math.random()*1000) < magicFind)) {
				items++;
			}
		}
		List<Equipment> foundItems = dungeon.generateFoundItemRewards(items);
		try {
		    Map<Equipment, Boolean> rewardItemsMap = dungeon.getItemRewards();
		    for(Equipment equipment:foundItems) {
				equipment = equipmentService.create(equipment);
				rewardItemsMap.put(equipment, true);
			}
			dungeon.setItemRewards(rewardItemsMap);
			dungeonService.update(dungeon);
		} catch (DungeonNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (AccountIdMismatch e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (EquipmentNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (BoostItemNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Check if the player found any boost items
	 * 
	 * @param dungeon
	 * @param chars
	 */
	protected void findBoostItems(Dungeon dungeon, List<Character> chars) {
		if(dungeon.getType() == DungeonType.ADVENTURE) {
			return;
		}
		int items = 0;
		if(((int)(Math.random()*1000) < (FIND_BOOST_ITEM_CHANCE * chars.size() * 2))) {
			items++;
		}
		for(int chance=0; chance < FOUND_BOOST_ITEM_CHANCES; chance++) {
			if(((int)(Math.random()*1000) < FIND_BOOST_ITEM_CHANCE)) {
				items++;
			}
		}
		List<BoostItem> foundItems = dungeon.generateBoostItemRewards(items);
		try {
		    List<BoostItem> rewardBoostItems = dungeon.getBoostItemRewards();
		    for(BoostItem boostItem:foundItems) {
		    	boostItem = boostItemService.create(boostItem);
				rewardBoostItems.add(boostItem);
			}
			dungeon.setBoostItemRewards(rewardBoostItems);
			dungeonService.update(dungeon);
		} catch (DungeonNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (AccountIdMismatch e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (EquipmentNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (BoostItemNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
