package org.kerrin.dungeon.enums;

public enum AttributeValueMaxType {
	ONE_HUNDRED(0), // 1 to 100
	LEVEL(1), // 1 to item level-1
	SLOTS(2), // 100 / number of slots (13) = 10 (actually 130 if all maxed)
	THREE(3),   // 1 to 3 ticks
	;
	
	private int id;
	
	private AttributeValueMaxType (int id) {
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
	public static AttributeValueMaxType fromId(int id) {
		for(AttributeValueMaxType thisSlot:values()) {
			if(thisSlot.id == id) return thisSlot;
		}
		return ONE_HUNDRED;
	}
	
	public String getAsString() {
		switch(this) {
		case LEVEL: return "1 to item level";
		case ONE_HUNDRED: return "1 to 100";
		case SLOTS: return "1 to 10";
		case THREE: return "1 to 3";
		default: return "Error: " + this.name();
		}
	}

	/**
	 * Check if the value passed in is already max value
	 * 
	 * @param level
	 * @param currentValue
	 * @return
	 */
	public boolean isMaxValue(int level, int currentValue) {
		switch(this) {
		case LEVEL: return currentValue >= level;
		case ONE_HUNDRED: return currentValue >= 100;
		case SLOTS: return currentValue >= 10;
		case THREE: return currentValue >= 3;
		default: return true;
		}
	}
}
