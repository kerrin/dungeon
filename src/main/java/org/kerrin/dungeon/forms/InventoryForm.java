package org.kerrin.dungeon.forms;

/**
 * Form data for a inventory
 * 
 * @author Kerrin
 *
 */
public class InventoryForm {
	/** The item Id */
	private long itemId = -1;
	
	/** Is this record for equipment (otherwise a boost item) */
	private boolean equipment = true;
	
	/** Is this record for hardcore characters */
	private boolean hardcore = false;
	
	/** Is this record for ironborn characters */
	private boolean ironborn = false;

	public InventoryForm() {};
		
	public InventoryForm(long itemId, boolean hardcore, boolean ironborn) {
		this.itemId = itemId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
	}
	
	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public boolean isEquipment() {
		return equipment;
	}

	public void setEquipment(boolean equipment) {
		this.equipment = equipment;
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

	@Override
	public String toString() {
		return "InventoryForm [" +
				"itemId=" + itemId + ", " +
				"equipment=" + (equipment?"Yes":"No") + ", " +
				"hardcore=" + (hardcore?"Yes":"No") + ", " + 
				"ironborn=" + (ironborn?"Yes":"No") + ", " + 
				"]";
	}
}
