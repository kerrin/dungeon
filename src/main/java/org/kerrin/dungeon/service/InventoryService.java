package org.kerrin.dungeon.service;

import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {
	public Inventory create(Inventory inventory);
    public Inventory delete(Account account, boolean hardcore, boolean ironborn) throws InventoryNotFound;
    public Inventory update(Inventory inventory, boolean populateEquipment) throws InventoryNotFound, AccountNotFound;
	public Inventory findByAccount(Account account, boolean hardcore, boolean ironborn);
	public StashSlotItemSuper getItemInSlot(Account account, boolean hardcore, boolean ironborn, Integer slotId) throws InventoryNotFound;
	/**
	 * Puts an item in the slot, assumes the slot is empty
	 * 
	 * @param account
	 * @param equipment
	 * @param slotId
	 * @return
	 * @throws InventoryNotFound
	 */
	public boolean putItemInSlot(Account account, StashSlotItemSuper stashSlotItem, Integer slotId) throws InventoryNotFound;
	public boolean removeFromInventory(Account account, StashSlotItemSuper stashSlotItem) throws InventoryNotFound;
}
