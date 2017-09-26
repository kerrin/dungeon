package org.kerrin.dungeon.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kerrin.dungeon.enums.AccountTask;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.service.AccountBoostService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
public class CreateDungeonsTask extends SuperAccountTask {
	private static final Logger logger = LoggerFactory.getLogger(CreateDungeonsTask.class);
			
	private DungeonService dungeonService;
	
	private EquipmentService equipmentService;
	
	private AccountBoostService accountBoostService;
	
	protected CreateDungeonsTask() {}
	
	public CreateDungeonsTask(
			AccountService accountService, 
			DungeonService dungeonService, 
			EquipmentService equipmentService, 
			AccountBoostService accountBoostService,
			Account account) {
		super(accountService, account, AccountTask.CREATE_DUNGEONS);
		this.account = account;
		this.accountService = accountService;
		this.dungeonService = dungeonService;
		this.equipmentService = equipmentService;
		this.accountBoostService = accountBoostService;
	}
	
	/**
	 * Actually run the creation of dungeons
	 */
	@Transactional
	@Override
	protected void realRun() {
		// Create some dungeons
		boolean hardcore = false;
		boolean ironborn = false;
		/* This loop should try all 4 permutations of hardcore and ironborn
		 Order will be: hardcore,ironborn
		 				false,false
						true,false
						false,true
						true,true
		 */
		for(int i=0; i<4; i++) {
			int accountLevel = accountService.getMaxCharacterLevel(account, hardcore, ironborn);
			if(accountLevel > account.getLevel()) {
				account.setLevel(accountLevel);
				try {
					accountService.update(account, false);
				} catch (AccountNotFound e) {
					logger.error("Error updating account level to {} for account {}", accountLevel, account);
					e.printStackTrace();
				}
			}
			if(accountLevel >= DungeonType.DUNGEON.getMinLevel()) { 
				List<Dungeon> dungeons = dungeonService.findAllByAccount(account, hardcore, ironborn);
				int createDungeons = accountLevel - dungeons.size();
				int allowLevel = accountLevel + (accountLevel / 5);
				while(createDungeons > 0) {
					DungeonType dungeonType = DungeonType.getRandomNotAdventure(accountLevel);
					// Let the dungeons be slightly higer level than the max character level as they get higher level
					int level = (int)(Math.random()*allowLevel)+1;
					long xpReward = dungeonType.getXpBase() * level;
					
					// Don't check XP boost now, as we apply it when dungeons are started
					
					Map<Monster, MonsterType> monsters = dungeonService.generateMonsters(level, dungeonType, 0);
					Dungeon dungeon = new Dungeon(-1, dungeonType, account, hardcore, ironborn, level, 
							xpReward, 
							monsters, 
							-1,
							0);
					dungeon = dungeonService.create(dungeon);
					List<Equipment> rewardItems = dungeon.generateItemRewards(false, hardcore, ironborn);
			        Map<Equipment, Boolean> rewardItemsMap = new HashMap<Equipment, Boolean>();
			        for(Equipment equipment:rewardItems) {
						equipment = equipmentService.create(equipment);
						rewardItemsMap.put(equipment, false);
					}
			        
					dungeon.setItemRewards(rewardItemsMap);
					logger.debug("Dungeon: "+dungeon.toString());
					try {
						dungeon = dungeonService.update(dungeon);
					} catch (DungeonNotFound e) {
						logger.error("Failed to save back dungeon after creating reward items: {}", dungeon);
					} catch (AccountIdMismatch e) {
						logger.error("Failed to save back dungeon after creating reward items: {}", dungeon);
					} catch (EquipmentNotFound e) {
						logger.error("Failed to save back dungeon after creating reward items: {}", dungeon);
					} catch (BoostItemNotFound e) {
						logger.error("Failed to save back dungeon after creating reward items: {}", dungeon);
					}
					createDungeons--;
				}
			}
			
			// Toggle the booleans
			if(hardcore) ironborn = !ironborn;
			hardcore = !hardcore;
		}
	}

}
