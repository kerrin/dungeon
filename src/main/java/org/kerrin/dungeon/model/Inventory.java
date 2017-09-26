package org.kerrin.dungeon.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

/**
 * Database record for a characters equipment
 * Lists the items in each slot
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="inventory")
public class Inventory {
	private static final int START_INVENTORY_SLOTS = 10;

	/** The identifier */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	/** The account identifier this inventory is for */
	private long accountId;
	
	/** Is this record for a hardcore character */
	private boolean hardcore;
	
	/** Is this record for an ironborn character */
	private boolean ironborn;
	
	/** How many slots the user has purchased */
	private int size;
	
	/** 
	 * Map of all the characters equipment
	 * Slots are numbered 0 to inventory size - 1
	 */
	@ElementCollection
	@MapKeyColumn(name="slot_index")
    @Column(name="item")
	private Map<Integer,StashSlotItemSuper> inventorySlots;

	protected Inventory() {};
	
	public Inventory(long accountId, boolean hardcore, boolean ironborn, int size, Map<Integer,StashSlotItemSuper> inventorySlots) {
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.size = size;
		this.inventorySlots = inventorySlots;
	}
	
	public Inventory(Account account, boolean hardcore, boolean ironborn) {
		this(account.getId(), hardcore, ironborn, START_INVENTORY_SLOTS, new HashMap<Integer,StashSlotItemSuper>(START_INVENTORY_SLOTS));
	}

	public Inventory(Inventory copyInventory, Map<Integer, StashSlotItemSuper> inventorySlots) {
		this(copyInventory.accountId, copyInventory.hardcore, copyInventory.ironborn, copyInventory.size, inventorySlots);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccount(Account account) {
		this.accountId = account.getId();
	}

	public boolean isHardcore() {
		return hardcore;
	}

	public void setHardcore(boolean hardcore) {
		this.hardcore = hardcore;
	}

	public boolean isIronborn() {
		return ironborn;
	}

	public void setIronborn(boolean ironborn) {
		this.ironborn = ironborn;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Map<Integer,StashSlotItemSuper> getInventorySlots() {
		return inventorySlots;
	}

	public void setInventorySlots(Map<Integer,StashSlotItemSuper> inventorySlots) {
		this.inventorySlots = inventorySlots;
	}
	
	/**
	 * Puts the equipment in the requested slot
	 * @param item	Item to put in slot
	 * @param slot	Slot number (0 indexed) to put the item in
	 * @return	What was in the slot before, or passed in item if slot not purchased
	 */
	public StashSlotItemSuper putItemInSlot(StashSlotItemSuper item, int slot) {
		// Is the inventory large enough?
		if(slot >= size) return item; // No, just return the item
		
		// What is currently in the slot?
		StashSlotItemSuper currentSlot = inventorySlots.get(slot);
		
		// Replace the slot with the new item
		inventorySlots.put(slot, item);
				
		// Return what was in the slot
		return currentSlot;
	}
	
	/**
	 * Increase the inventory by the requested number
	 * @param purchasedSlots
	 * @return	New inventory size
	 */
	public int purchaseSlots(int purchasedSlots) {
		size += purchasedSlots;
		return size;
	}
}
