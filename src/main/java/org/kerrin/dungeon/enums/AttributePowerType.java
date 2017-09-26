package org.kerrin.dungeon.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Each equipment attribute effects the character strengths differently
 * 
 * @author Kerrin
 *
 */
public enum AttributePowerType {
	/** No effect on strength, e.g. reduces time to run dungeon, find equipment, etc */
	NONE("None"), 
	/** Increases attack power */
	ATTACK("Attack"), 
	/** Increases defence power */
	DEFENCE("Defence"), 
	/** Increases recovery of health or mana */
	RECOVERY("Recovery"),
	/** Increases attack power for specific classes (other wise no significant effect) */
	ATTACK_CLASS("Attack (Class Specific)"), 
	/** Increases defence power for specific classes (other wise no significant effect) */
	DEFENCE_CLASS("Defence (Class Specific)");
	
	private static final Logger logger = LoggerFactory.getLogger(AttributePowerType.class);

	private final String niceName;

	/**
	 * Constructor
	 * 
	 * @param niceName
	 */
	private AttributePowerType(String niceName) {
		this.niceName = niceName;
	}

	/**
	 * Get the nice name
	 * 
	 * @return
	 */
	public String getNiceName() {
		return niceName;
	}
}
