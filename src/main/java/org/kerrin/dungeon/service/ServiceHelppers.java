package org.kerrin.dungeon.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonEvent;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Hiscore;
import org.kerrin.dungeon.repository.BoostItemRepo;
import org.kerrin.dungeon.repository.EquipmentRepo;
import org.springframework.transaction.annotation.Transactional;

public class ServiceHelppers {

	public ServiceHelppers() {
		super();
	}

	/**
	 * Load all the character slots and the equipment in them before closing the database session
	 * 
	 * @param characterEquipment
	 */
	@Transactional
	protected void loadCharSlotsLinkedTables(EquipmentRepo equipmentRepo, CharacterEquipment characterEquipment) {
		if(characterEquipment == null) return;
		Map<CharSlot, Equipment> charSlots = characterEquipment.getCharacterSlots();
		for(CharSlot slot:charSlots.keySet()) {
			Equipment equipment = charSlots.get(slot);
			Hibernate.initialize(equipment.getAttributes(false));
			checkVersion(equipmentRepo, equipment);
		}
	}

	/**
	 * Load all the linked tables
	 * Character must be not null
	 * 
	 * @param character
	 */
	@Transactional
	protected static void loadCharacterLinkedTables(EquipmentRepo equipmentRepo, Character character) {
		if(character == null) {
			return;
		}
		Account account = character.getAccount();
		if(account != null) {
			loadAccountLinkedTables(account);
		}
		Dungeon dungeon = character.getDungeon();
		if(dungeon != null) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
		}
	}

	@Transactional
	protected void loadEquipmentLinkedTables(EquipmentRepo equipmentRepo, List<Equipment> foundEquipments) {
		if(foundEquipments == null) return;
		// Load all the linked tables
		for(Equipment foundEquipment: foundEquipments) {
			loadEquipmentLinkedTables(equipmentRepo, foundEquipment);
		}
	}
	
	@Transactional
	protected void loadEquipmentLinkedTables(EquipmentRepo equipmentRepo, Equipment equipment) {
		if(equipment == null) return;
		// Load all the linked tables
		Hibernate.initialize(equipment.getAttributes(false));
		checkVersion(equipmentRepo, equipment);
	}
	
	@Transactional
	protected void loadBoostItemLinkedTables(BoostItemRepo boostItemRepo, BoostItem boostItem) {
		if(boostItem == null) return;
		// Load all the linked tables
		Account account = boostItem.getAccount();
		if(account != null) {
			loadAccountLinkedTables(account);
			boostItem.setAccount(account);
		}
	}

    /**
     * Load all the linked tables
     * Dungeon must be not null
     * 
     * @param dungeon
     */
	@Transactional
	protected static void loadDungeonLinkedTables(EquipmentRepo equipmentRepo, Dungeon dungeon) {
		if(dungeon == null) return;
		Account account = dungeon.getAccount();
		if(account != null) {
			loadAccountLinkedTables(account);
			dungeon.setAccount(account);
		}
		if(dungeon.getCharacters() != null) {
	        Hibernate.initialize(dungeon.getCharacters());
	        Hibernate.initialize(dungeon.getMonsters());
	        Hibernate.initialize(dungeon.getDeadMonsters());
			//Hibernate.initialize(dungeon.getItemRewards());
			Map<Equipment, Boolean> itemRewards = dungeon.getItemRewards();
			if(itemRewards != null) {
				for(Equipment equipment:itemRewards.keySet()) {
					Hibernate.initialize(equipment.getAttributes(false));
					checkVersion(equipmentRepo, equipment);
				}
			}
			Hibernate.initialize(dungeon.getBoostItemRewards());			
		}
	}

    /**
     * Load all the linked tables
     * DungeonEvent must be not null
     * 
     * @param dungeonEvent
     */
	@Transactional
	protected void loadDungeonEventsLinkedTables(DungeonEvent dungeonEvent) {
		if(dungeonEvent == null) return;
		Hibernate.initialize(dungeonEvent.getCharacter());
	}
	
	/**
     * Load all the linked tables
     * Dungeon must be not null
     * 
     * @param account
     */
	@Transactional
	protected static void loadAccountLinkedTables(Account account) {
		if(account == null) {
			return;
		}
		Hibernate.initialize(account.getLastProcessedMap());
	}
	
	/**
	 * Load all the linked tables
	 * 
	 * @param character
	 */
	@Transactional
	protected static void loadHiscoreLinkedTables(Hiscore hiscore) {
		if(hiscore == null) {
			return;
		}
		Account account = hiscore.getAccount();
		if(account != null) {
			loadAccountLinkedTables(account);
		}
	}
	
	/**
	 * Load all the linked tables
	 * 
	 * @param character
	 */
	@Transactional
	protected static void loadAchievementLinkedTables(Achievement achievement) {
		if(achievement == null) {
			return;
		}
		Account account = achievement.getAccount();
		if(account != null) {
			loadAccountLinkedTables(account);
		}
	}

	@Transactional
	private static void checkVersion(EquipmentRepo equipmentRepo, Equipment equipment) {
		if(equipment == null) return;
		if(equipment.setAttributes(equipment.getVersion(), equipment.getAttributes(false))) {
			equipment.setVersion((short)1);
			equipmentRepo.save(equipment);
		}
	}
}