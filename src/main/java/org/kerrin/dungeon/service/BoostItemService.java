package org.kerrin.dungeon.service;

import java.util.List;
import java.util.Locale;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.springframework.stereotype.Service;

@Service
public interface BoostItemService {
	public BoostItem findById(long id);
	public List<BoostItem> findAllByAccount(Account account);
	public BoostItem create(BoostItem BoostItem);
    public BoostItem delete(Account account, BoostItem BoostItem) 
    		throws BoostItemNotFound, EquipmentNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound;
    
    /**
     * Update the BoostItem in the database
     * 
     * @param BoostItem				BoostItem to update
     * 
     * @return
     * @throws BoostItemNotFound
     */
    public BoostItem update(BoostItem boostItem) throws BoostItemNotFound;
    
    /**
     * Get all the boost items within a level range
     *  
     * @param greaterThan
     * @param lessThan
     * @return
     */
	public List<BoostItem> findByLevelGreaterThanAndLevelLessThan(int greaterThan, int lessThan);
	/**
	 * Remove the boost item from the stash, dungeon and message 
	 * 
	 * @param boostItem 
	 * @throws InventoryNotFound 
	 * @throws DungeonNotFound 
	 * @throws BoostItemNotFound 
	 * @throws EquipmentNotFound 
	 * @throws AccountIdMismatch 
	 */
	public void clearLocations(BoostItem boostItem) 
			throws DungeonNotFound, InventoryNotFound, AccountIdMismatch, EquipmentNotFound, BoostItemNotFound;
	
	/**
	 * Get all the redeemed boost items on an account, which includes their levels
	 * 
	 * @param account
	 * @param locale
	 * @param ironborn 
	 * @param hardcore 
	 * 
	 * @return
	 */
	public List<BoostItem> getBoostItems(Account account, boolean hardcore, boolean ironborn);

	/**
	 * Try to get a boost item of the passed type
	 * 
	 * @param account	Account the boost item must be on
	 * @param hardcore	Hardcore mode
	 * @param ironborn	Ironborn mode
	 * @param type		Boost item type to find
	 * @param level		Level the item can be
	 * @param minimum	If true, returns the lowest level match, otherwise the highest match
	 * 
	 * @return	Best BoostItem that matches (lowest/highest level match is returned) 
	 */
	public BoostItem getValidBoostItemType(Account account, boolean hardcore, boolean ironborn, BoostItemType type, int level, boolean minimum);
}
