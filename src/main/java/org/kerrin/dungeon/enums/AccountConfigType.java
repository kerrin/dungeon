package org.kerrin.dungeon.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AccountConfigType {
	EQUIPMENT_COMPARE,
	TOOL_TIPS;

	private static final Logger logger = LoggerFactory.getLogger(AccountConfigType.class);
			
	private AccountConfigType() {}
}
