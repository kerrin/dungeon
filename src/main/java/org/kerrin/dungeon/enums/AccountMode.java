package org.kerrin.dungeon.enums;

public enum AccountMode {
	NORMAL,
	HARDCORE,
	IRONBORN,
	EXTREME;
	
	public static AccountMode fromModeFlags(boolean hardcore, boolean ironborn) {
		if(hardcore) {
			if(ironborn) {
				return AccountMode.EXTREME;
			} else {
				return AccountMode.HARDCORE;
			}
		} else {
			if(ironborn) {
				return AccountMode.IRONBORN;
			} else {
				return AccountMode.NORMAL;
			}
		}
	}
}
