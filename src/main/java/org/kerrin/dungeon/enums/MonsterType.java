package org.kerrin.dungeon.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All the possible monster difficulty types
 * 
 * @author Kerrin
 *
 */
public enum MonsterType {
	NONE(0,"None!", ""),
	TRASH(1, "Trash", "monsterTypeTrash"), 
	ELITE(2, "Elite", "monsterTypeElite"), 
	BOSS(3, "Boss", "monsterTypeBoss"), 
	/** A selection of any monster type */
	RANDOM_ALL(4, "Trash, Elite or Boss", ""),
	/** A selection of any monster type except boss */
	RANDOM_NOT_BOSS(5, "Trash or Elite", ""); 
	
	private static final Logger logger = LoggerFactory.getLogger(MonsterType.class);
	
	/** Number of monster types, excluding NONE and random */
	private static final int MONSTER_TYPE_COUNT = 3;
	
	private int id;
	private String niceName;
	private String htmlClass;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	MonsterType (int id, String niceName, String htmlClass) {
		this.id = id;
		this.niceName = niceName;
		this.htmlClass = htmlClass;
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
	 * Get the nice name
	 * 
	 * @return
	 */
	public String getNiceName() {
		return niceName;
	}
	
	/**
	 * Get the html class
	 * 
	 * @return
	 */
	public String getHtmlClass() {
		return htmlClass;
	}
	
	/**
	 * Get a character class from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static MonsterType fromId(int id) {
		for(MonsterType thisDamageType:values()) {
			if(thisDamageType.id == id) return thisDamageType;
		}
		return NONE;
	}

	/**
	 * Get a random type of monster, excluding NONE and random
	 * 
	 * @return
	 */
	public static MonsterType getRandomType() {
		int chance = MONSTER_TYPE_COUNT - 1;
		for(MonsterType thisSlot:values()) {
			if(thisSlot == MonsterType.NONE) continue;
			int random = (int) (Math.random()*chance);
			if(random < 1) return thisSlot;
			chance--;
		}
		logger.error("No equipment type rolled randomly, chance is still " + chance);
		return NONE;
	}

	public int getTypeMultiplier() {
		return id;
	}

	/**
	 * Checks if this monster type comes after checked monster type
	 * @param checkMonsterType
	 * @return
	 */
	public boolean after(MonsterType checkMonsterType) {
		return checkMonsterType.id < id;
	}

	/**
	 * Checks if this monster type comes before checked monster type
	 * @param checkMonsterType
	 * @return
	 */
	public boolean before(MonsterType checkMonsterType) {
		return checkMonsterType.id > id;
	}
}
