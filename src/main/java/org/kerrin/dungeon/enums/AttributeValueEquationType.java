package org.kerrin.dungeon.enums;

public enum AttributeValueEquationType {
	/** Final value is level times value */
	LEVEL_MULTI(0),
	/** Final value is value */
	VALUE(1), 
	;
	
	private int id;
	
	private AttributeValueEquationType (int id) {
		this.id = id;
	}
	
	/**
	 * Get the identifier
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Get the AttributeValueEquationType from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static AttributeValueEquationType fromId(int id) {
		for(AttributeValueEquationType thisSlot:values()) {
			if(thisSlot.id == id) return thisSlot;
		}
		return LEVEL_MULTI;
	}
	
	public String getAsString() {
		switch(this) {
		case LEVEL_MULTI: return "Multiplicative";
		case VALUE: return "No Effect";
		default: return "Error: " + this.name();
		}
	}
}
