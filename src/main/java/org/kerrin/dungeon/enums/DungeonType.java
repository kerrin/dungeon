package org.kerrin.dungeon.enums;

import java.util.ArrayList;
import java.util.List;

import org.kerrin.dungeon.model.Character;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All the possible character classes
 * 
 * @author Kerrin
 *
 */
public enum DungeonType {
	//			id, Name,		Players		Monsters 	Min
	//							Min, Max, 	Min, Max, 	level,	Xp,						HTML Colour
	NONE(		0,	"None!", 	0, 0, 		0, 0,		101, 	0,						"black"),
	/** Trash, elites, bosses randomly for time period */
	ADVENTURE(	1, 	"Adventure",1, 4, 		1, 4, 		1, 		Character.LEVEL1_XP/4,	"green"),
	/** Trash, elites randomly, end with boss */
	DUNGEON(	2, 	"Dungeon", 	2, 4, 		2, 3, 		4, 		Character.LEVEL1_XP,	"yellow"),
	/** Trash, elites randomly, multiple bosses with trash and elites between, end with boss */
	RAID(		3, 	"Raid", 	4, 9,		6, 8,		10, 	Character.LEVEL1_XP*2,	"red");
	
	private static final Logger logger = LoggerFactory.getLogger(DungeonType.class);
			
	private final int id;
	private final String niceName;
	/** Minimum number of characters to run */
	private final int minCharacters;
	/** Maximum number of characters to run */
	private final int maxCharacters;
	/** Minimum number of monsters to run (fills to this number when random type selected) */
	private final int minMonsters;
	/** Maximum number of monsters to run (fills to this number when random type selected) */
	private final int maxMonsters;
	/** Min level of account */
	private final int minLevel;
	/** The XP earnt per level of dungeon, before monster additions */
	private final long xpBase;
	/** The html colour code to display the text as */
	private final String htmlColour;
	
	private List<MonsterType> monsterTypes = new ArrayList<MonsterType>();
	
	static {
		ADVENTURE.monsterTypes.add(MonsterType.RANDOM_NOT_BOSS);
		DUNGEON.monsterTypes.add(MonsterType.TRASH); // 1 Trash
		DUNGEON.monsterTypes.add(MonsterType.RANDOM_NOT_BOSS); // 1 to 2 more monsters
		DUNGEON.monsterTypes.add(MonsterType.BOSS); // A boss
		RAID.monsterTypes.add(MonsterType.TRASH);
		RAID.monsterTypes.add(MonsterType.ELITE);
		RAID.monsterTypes.add(MonsterType.RANDOM_NOT_BOSS);
		RAID.monsterTypes.add(MonsterType.BOSS);
		RAID.monsterTypes.add(MonsterType.BOSS);
	}
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	DungeonType (int id, String niceName, int minPlayers, int maxPlayers, int minMonsters, int maxMonsters, int minLevel, long xpBase, String htmlColour) {
		this.id = id;
		this.niceName = niceName;
		this.minCharacters = minPlayers;
		this.maxCharacters = maxPlayers;
		this.minMonsters = minMonsters;
		this.maxMonsters = maxMonsters;
		this.minLevel = minLevel;
		this.xpBase = xpBase;
		this.htmlColour = htmlColour;
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

	public int getMinCharacters() {
		return minCharacters;
	}

	public int getMaxCharacters() {
		return maxCharacters;
	}

	public int getMinMonsters() {
		return minMonsters;
	}

	public int getMaxMonsters() {
		return maxMonsters;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public long getXpBase() {
		return xpBase;
	}

	public String getHtmlColour() {
		return htmlColour;
	}

	public List<MonsterType> getMonsterTypes() {
		return monsterTypes;
	}

	/**
	 * Get a character class from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static DungeonType fromId(int id) {
		for(DungeonType thisDamageType:values()) {
			if(thisDamageType.id == id) return thisDamageType;
		}
		return NONE;
	}

	/**
	 * Randomly get a valid dungeon type that has a minimum level of less than level
	 * This will not return Dungeon Type NONE
	 * 
	 * @param level
	 * @return
	 */
	public static DungeonType getRandom(int level) {
		if(level < 1) {
			logger.error("Attempt to get level {} dungeon", level);
			level = 1;
		}
		int typeCount = values().length - 1;
		DungeonType dungeonType = DungeonType.NONE;
		while(dungeonType.minLevel > level || dungeonType == NONE) {
			int index = (int)(Math.random()*typeCount)+1;
			dungeonType = fromId(index);
		}
		return dungeonType;
	}
	
	/**
	 * Randomly get a valid dungeon type
	 * This will not return Dungeon Type NONE or ADVENTURE
	 * @return
	 */
	public static DungeonType getRandomNotAdventure(int level) {
		DungeonType type = DungeonType.ADVENTURE;
		while(type == ADVENTURE) {
			type = getRandom(level);
		}
		return type;
	}
}
