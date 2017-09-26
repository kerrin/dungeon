package org.kerrin.dungeon.service;

import java.util.List;
import java.util.Map;

import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CantEquipToDungeon;
import org.kerrin.dungeon.exception.CantEquipToMessage;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryException;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.exception.MessageEquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Equipment;
import org.springframework.stereotype.Service;

@Service
public interface EquipmentService {
	public Equipment findById(long id);
	public Equipment create(Equipment equipment);
    public Equipment delete(Account account, Equipment equipment) 
    		throws EquipmentNotFound, BoostItemNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound, 
    			CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound, InventoryException;
    /**
     * Update the equipment in the database
     * 
     * @param equipment				Equipment to update
     * @param populateAttributes	If we need to load the attributes of the equipment
     * 
     * @return
     * @throws EquipmentNotFound
     */
    public Equipment update(Equipment equipment, boolean populateAttributes) throws EquipmentNotFound;
	public List<Equipment> findAllByQualityId(int qualityId);
	public List<Equipment> findByLevelGreaterThanAndLevelLessThan(int greaterLevel, int lessThanLevel);
	
	/**
	 * Creates the equipment a new character starts with
	 * 
	 * @param character
	 * @param characterSlots
	 * @throws EquipmentNotFound 
	 */
	public void generateStarterEquipment(Character character, Map<CharSlot, Equipment> characterSlots) 
			throws EquipmentNotFound;
	
	/**
	 * Swap equipment where 'currentEquipment' is in character slot slotId on character
	 * 'currentEquipment' can be null, then slotId on character is used for the new location of newEquipment
	 * Check the equipment is owned by the account first
	 * 
	 * @param account
	 * @param newEquipment
	 * @param currentEquipment
	 * @param character
	 * @param slotId
	 * @return ReloadPanels or null if swap failed
	 * @throws CantEquipToMessage 
	 * @throws MessageEquipmentNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 */
	public ReloadPanels swapEquipmentInCharacterSlot(Account account, Equipment newStashSlotItem, Character character, CharSlot slotId)
			 throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound;
	
	public void removeItemFromMessage(AccountMessage message) 
			throws CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, 
				EquipmentNotFound, BoostItemNotFound, DungeonNotFound, AccountIdMismatch,
				InventoryNotFound, MessageEquipmentNotFound, InventoryException;
	
}