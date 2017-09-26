package org.kerrin.dungeon.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.kerrin.dungeon.model.Character;

/**
 * All the equipment slots for a character
 * 
 * @author Kerrin
 *
 */
public enum BoostItemType {
	UNKNOWN			(0,"Unknown!", "Broken", "None",				
			false, 	Integer.MAX_VALUE, 0, 0),
	IMPROVE_QUALITY	(1, "Improve Item Quality", "Increase an items quality level, increasing the attributes for equipment upto level <LEVEL>",
			"Drop the item of <LEVEL> level or lower that you want to improve the quality of on the enchant, then select improve quality",
			true, 	10, 5, 2000), 
	DUNGEON_TOKENS	(2, "Dungeon Tokens", 	 	"<LEVEL> dungeon tokens",	
			"Press the Redeem button on the item to add <LEVEL> tokens to your total Dungeon Tokens",
			false, 	4, 200, 50), 
	DUNGEON_SPEED	(3, "Dungeon Speed", 	 	"Reduce the time left on a dungeon by <LEVELasMins> minutes",	
			"Press the 'Rush with Boost' on a dungeons details page of a dungeon with <LEVELasMins> minutes or more left to remove <LEVELasMins> minutes from the time left",
			true, 	4, 200, 50),
	LEVEL_UP		(4, "Level Up", 	 		"Level up a character that is this level <LEVEL> or lower",
			"Press the 'Level Up with Boost' on a character details page of a character of level <LEVEL> or lower",
			true, 	4, 20, 500),
	RESURRECTION	(5, "Resurrection", 	 	"Resurrect a character for characters upto level <LEVEL>",	
			"Press the 'Resurrect with Boost' on a dead characters details page of a character of the <LEVEL> level or lower",
			true, 	4, 100, 100, false), 
	ENCHANT_RANGE	(6, "Enchant Range", 	 	"Reroll an items attribute range for free for equipment upto level <LEVEL>",	
			"Drop the item of the <LEVEL> level or lower on to the enchant, then select enchant range",
			true, 	10, 100, 100), 
	ENCHANT_IMPROVE_RANGE(7, "Improve Enchant Range",  "Improve an items attribute range for free for equipment upto level <LEVEL>",
			"Drop the item of the <LEVEL> level or lower on to the enchant, then select improve range",
			true, 	20, 10, 1000), 
	ENCHANT_REMOVE_CURSE(8, "Remove Cursed Attribute",  "Changes a cursed attribute in to a positive attribute for equipment upto level <LEVEL>",
			"Drop the item of <LEVEL> level or lower on to the enchant, then select remove curse on a cursed attribute",
			true, 	30, 10, 1000),
	ENCHANT_TYPE	(9, "Enchant Type", 		 "Reroll an items attribute type and range for free for equipment upto level <LEVEL>",
			"Drop the item of the <LEVEL> level or lower on to the enchant, then select enchant type",
			true, 	10, 500, 20),
	XP_BOOST		(10, "XP Boost Potion", 	 "Doubles all experience gained for <LEVELx10> minutes",
			"Press the 'Redeem' button on the item and your XP gains from dungeons started while it is active will be doubled for <LEVELx10> minutes",
			false, 	4, 50, 200),
	MAGIC_FIND		(11, "Magic Find Potion", 	 "Increases magic find in all dungeons for <LEVELx10> minutes",
			"Press the 'Redeem' button on the item and your magic find in dungeons finished while it is active will be increased for <LEVELx10> minutes",
			false, 	4, 50, 200),
	CHANGE_NAME		(12, "Character Name Change", "Allows you to rename a character of level <LEVEL> or lower",
			"Press the 'Rename' symbol next to the character name on a character sheet of a character of level <LEVEL> or lower",
			false, 	10, 5, 2000),
	;
	
	public final static int TOTAL_CHANCE = 1250;
	
	private static final Logger logger = LoggerFactory.getLogger(BoostItemType.class);
	
	/** Unique identifier */
	private int id;
	/** Name to display to player */
	private String niceName;
	/** Description to display to player */
	private String description;
	/** Description of how to use */
	private String usageInstructions;
	/** Item can only be used on things of it's level or lower */
	private boolean levelRestricted;
	/** Won't appear as a reward below this level */
	private int minimumLevel;
	/** Relative chance this type will drop */
	private int dropChance;
	/** Cost in dungeon tokens to purchase from in game shop */
	private int cost;
	/** If this item can drop in hard core */
	private boolean dropsInHardcore;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param niceName
	 * @param description
	 * @param levelRestricted
	 * @param minimumLevel
	 * @param dropChance
	 * @param cost
	 */
	BoostItemType (int id, String niceName, String description, String usageInstructions,
			boolean levelRestricted, int minimumLevel, int dropChance, int cost) {
		this(id, niceName, description, usageInstructions, levelRestricted, minimumLevel, dropChance, cost, true);
	}
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param niceName
	 * @param description
	 * @param levelRestricted
	 * @param minimumLevel
	 * @param dropChance
	 * @param cost
	 * @param dropsInHardcore
	 */
	BoostItemType (int id, String niceName, String description, String usageInstructions,
			boolean levelRestricted, int minimumLevel, int dropChance, int cost, boolean dropsInHardcore) {
		this.id = id;
		this.niceName = niceName;
		this.description = description;
		this.usageInstructions = usageInstructions;
		this.levelRestricted = levelRestricted;
		this.minimumLevel = minimumLevel;
		this.dropChance = dropChance;
		this.cost = cost;
		this.dropsInHardcore = dropsInHardcore;
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
	 * For JSTL, to get a name without spaces
	 * @return
	 */
	public String getName() {
		return name();
	}

	public String getMaxLevelDescription() {
		return replaceLevel(description, Character.MAX_LEVEL);
	}
	
	public String getDescription(int level) {
		return replaceLevel(description, level);
	}

	public String getMaxLevelUsageInstructions() {
		return replaceLevel(usageInstructions, Character.MAX_LEVEL);
	}
	
	public String getUsageInstructions(int level) {
		return replaceLevel(usageInstructions, level);
	}
	
	private String replaceLevel(String text, int level) {
		String levelAsMins = Integer.toString(level/2);
		if(level%2 == 1) {
			levelAsMins += " 1/2";
		}
		return text.replace("<LEVEL>", Integer.toString(level))
							.replace("<LEVELx2>", Integer.toString(level*2))
							.replace("<LEVELx10>", Integer.toString(level*10))
							.replace("<LEVELx30>", Integer.toString(level*30))
							.replace("<LEVELasMins>", levelAsMins);
	}

	public int getMinimumLevel() {
		return minimumLevel;
	}

	/**
	 * Get a character slot from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static BoostItemType fromId(int id) {
		for(BoostItemType thisSlot:values()) {
			if(thisSlot.id == id) return thisSlot;
		}
		return UNKNOWN;
	}

	public static BoostItemType getRandomType(int level, boolean isHardcore) {
		int chance = TOTAL_CHANCE;
		boolean skippedDueToLevelRequirements = false;
		boolean skippedDueToHardcore = false;
		for(BoostItemType thisBoostItemType:values()) {
			if(thisBoostItemType == BoostItemType.UNKNOWN) continue;
			int random = (int) (Math.random()*chance);
			if(random < thisBoostItemType.dropChance) {
				if(thisBoostItemType.dropsInHardcore || !isHardcore) {
					if(thisBoostItemType.minimumLevel <= level) {
						return thisBoostItemType;
					}
					skippedDueToLevelRequirements = true;
				} else {
					skippedDueToHardcore = true;
				}
			}
			chance -= thisBoostItemType.dropChance;
		}
		if(!skippedDueToLevelRequirements && !skippedDueToHardcore) {
			logger.error("No boost item type rolled randomly, chance is still " + chance);
		}
		return UNKNOWN;
	}

	public boolean isLevelRestricted() {
		return levelRestricted;
	}

	public int getDropChance() {
		return dropChance;
	}

	public int getTotalChance() {
		return TOTAL_CHANCE;
	}

	public int getCost() {
		return cost;
	}
}
