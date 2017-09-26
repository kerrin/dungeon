package org.kerrin.dungeon.enums;

public enum BooleanOptions {
	BOTH(null),
	TRUE(true),
	FALSE(false);
	
	private Boolean bool;

	private BooleanOptions(Boolean bool) {
		this.bool = bool;
	}

	public Boolean getBooleanValue() {
		return bool;
	}

	public String getName() {
		return super.name();
	}

	public static BooleanOptions fromBoolean(Boolean findBool) {
		for(BooleanOptions boolOptions:values()) {
			if(boolOptions.getBooleanValue() == findBool) return boolOptions;
		}
		return BOTH;
	}	
}
