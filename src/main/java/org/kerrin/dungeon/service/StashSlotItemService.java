package org.kerrin.dungeon.service;

import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.EquipmentLocation;
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
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.springframework.stereotype.Service;

@Service
public interface StashSlotItemService {
	/**
	 * Swap equipment locations
	 * Both equipment must be defined and in swappable locations
	 * @param account
	 * @param equipment1
	 * @param equipment2
	 * @return ReloadPanels or null if swap failed
	 * @throws CantEquipToMessage 
	 * @throws MessageEquipmentNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 */
	public ReloadPanels swapItem(Account account, StashSlotItemSuper stashSlotItem1, StashSlotItemSuper stashSlotItem2)
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, 
				InventoryNotFound, InventoryException, CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound;
	/**
	 * Swap equipment with item in accounts inventory slot 'slotIndex'
	 * Check the equipment is owned by the account first
	 * 
	 * @param account
	 * @param equipment
	 * @param slotIndex
	 * @return ReloadPanels or null if swap failed
	 * @throws CantEquipToMessage 
	 * @throws MessageEquipmentNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 */
	public ReloadPanels swapItemWithInventory(Account account, StashSlotItemSuper stashSlotItem, int slotIndex) 
			throws InventoryNotFound, DungeonNotFound, AccountIdMismatch, EquipmentNotFound, 
			InventoryException, CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound;
	/**
	 * 
	 * @param newStashSlotItem
	 * @param location
	 * @param locationId
	 * @param account
	 * @return
	 * @throws MessageEquipmentNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 */
	public ReloadPanels moveItem(StashSlotItemSuper newStashSlotItem, EquipmentLocation location, long locationId, Account account)
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, BoostItemNotFound, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound;
	
	/**
	 * Removes an stash slot item from the dungeon
	 * 
	 * @param item
	 * @throws DungeonNotFound
	 * @throws AccountIdMismatch
	 * @throws BoostItemNotFound
	 * @throws EquipmentNotFound
	 */
	public void removeItemFromDungeon(StashSlotItemSuper item) 
			throws DungeonNotFound, AccountIdMismatch, BoostItemNotFound, EquipmentNotFound;
	
	/**
	 * Remove an item from the game (db)
	 * 
	 * @param stashSlotItem
	 * @param account
	 * @throws DungeonNotFound
	 * @throws AccountIdMismatch
	 * @throws EquipmentNotFound
	 * @throws InventoryNotFound
	 * @throws InventoryException
	 * @throws BoostItemNotFound
	 * @throws MessageEquipmentNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 */
	public void doRemove(StashSlotItemSuper stashSlotItem, Account account)
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, BoostItemNotFound, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound;
	
	/**
	 * Add the item to the game (db) on the account
	 * 
	 * @param equipment			Equipment to add
	 * @param account			Account to add to
	 * @param character			Character (if new location is character)
	 * @param charSlot			Character Slot (if new location is character)
	 * @param inventorySlotId	Inventory slot (if new location is inventory)
	 * @throws DungeonNotFound
	 * @throws AccountIdMismatch
	 * @throws EquipmentNotFound
	 * @throws InventoryNotFound
	 * @throws InventoryException
	 * @throws CantEquipToDungeon
	 * @throws CantEquipToMessage 
	 */
	public void doAdd(StashSlotItemSuper stashSlotItem, Account account, Character character, CharSlot charSlot, int inventorySlotId)
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, BoostItemNotFound, InventoryNotFound, InventoryException, CantEquipToDungeon, CantEquipToMessage;

	/**
	 * Attempt to swap the equipment with dungeon equipment
	 * 
	 * @param account
	 * @param equipment1
	 * @param equipment2
	 * @return
	 * @throws DungeonNotFound
	 * @throws AccountIdMismatch
	 * @throws EquipmentNotFound
	 * @throws InventoryNotFound
	 * @throws InventoryException
	 * @throws CantEquipToDungeon
	 * @throws BoostItemNotFound
	 * @throws CantEquipToMessage 
	 * @throws MessageEquipmentNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 */
	public ReloadPanels tryDungeonSwapEquipment(Account account, Equipment equipment1, Equipment equipment2)
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound;
}