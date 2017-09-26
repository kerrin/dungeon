package org.kerrin.dungeon.enums;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All the equipment slots for a character
 * 
 * @author Kerrin
 *
 */
public enum EquipmentQuality {
	USELESS(	0, "Useless!", 	0, 		new int[]{0,0,0,0},			"Black"),
	BROKEN(		1, "Broken", 	545, 	new int[]{0,1,1,1},			"DimGrey"), 
	INFERIOR(	2, "Inferior", 	200, 	new int[]{0,249,35,17},		"LightGrey"),
	COMMON(		3, "Common", 	120, 	new int[]{0,397,214,125},	"#004400"), // Really Dark Green 
	SUPERIOR(	4, "Superior", 	70, 	new int[]{0,214,397,306},	"Blue"), 
	EPIC(		5, "Epic", 		38, 	new int[]{0,112,214,305},	"Purple"),
	LEGENDARY(	6, "Legendary", 20, 	new int[]{0,27,112,163},	"Orange"),
	ARTIFACT(	7, "Artifact", 	7, 		new int[]{0,0,27,83},		"Brown"),
	;

	private static final Logger logger = LoggerFactory.getLogger(EquipmentQuality.class);
	
	public final static int NUMBER_OF_QUALITIES = 8;
	
	/** The id is also the number of attributes this quality has */
	private int id;
	/** Name of quality level */
	private String niceName;
	/** Chance in 1000 of the quality rolling for a world drop */
	private int dropRollChance;
	/** Chance in 1000 of the quality rolling as a reward for each type of dungeon */
	private int[] rewardRollChances;
	/** The colour this displays as, as a html colour */
	private String htmlColour;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	EquipmentQuality (int id, String niceName, int dropRollChance, int[] rewardRollChances, String htmlColour) {
		this.id = id;
		this.niceName = niceName;
		this.dropRollChance = dropRollChance;
		this.rewardRollChances = rewardRollChances;
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
	
	public String getName() {
		return name();
	}
	
	public int getDropRollChance() {
		return dropRollChance;
	}

	public int getRewardRollChance(DungeonType dungeonType) {
		return rewardRollChances[dungeonType.getId()];
	}

	public String getHtmlColour() {
		return htmlColour;
	}

	/**
	 * Get a character slot from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static EquipmentQuality fromId(int id) {
		for(EquipmentQuality thisQuality:values()) {
			if(thisQuality.id == id) return thisQuality;
		}
		return USELESS;
	}
	
	/**
	 * Get a drop quality based on chance
	 * @return
	 */
	public static EquipmentQuality getRandomDrop() {
		int chance = 1000;
		for(EquipmentQuality thisQuality:values()) {
			int random = (int) (Math.random()*chance);
			if(random < thisQuality.dropRollChance) return thisQuality;
			chance -= thisQuality.dropRollChance;
		}
		logger.error("No quality rolled for drop, still had chance "+chance);
		return EquipmentQuality.USELESS;
	}
	
	/**
	 * Get a reward quality based on chance
	 * @return
	 */
	public static EquipmentQuality getRandomReward(DungeonType dungeonType) {
		int chance = 1000;
		for(EquipmentQuality thisQuality:values()) {
			int random = (int) (Math.random()*chance);
			if(random < thisQuality.rewardRollChances[dungeonType.getId()]) return thisQuality;
			chance -= thisQuality.rewardRollChances[dungeonType.getId()];
		}
		logger.error("No quality rolled for reward, still had chance "+chance);
		return EquipmentQuality.USELESS;
	}

	/**
	 * Reduce the quality of an item
	 * @return
	 */
	public EquipmentQuality decrease() {
		List<EquipmentQuality> values = Arrays.asList(values());
		Iterator<EquipmentQuality> iter = values.iterator();
		EquipmentQuality previousQuality = null;
		while(iter.hasNext()) {
			EquipmentQuality thisQuality = iter.next();
			if(thisQuality.id == id) {
				if(previousQuality != null) {
					logger.debug("Previous quality {}", previousQuality);
					return previousQuality;
				}
				logger.debug("No previous quality for {}", this);
				return this;
			}
			previousQuality = thisQuality;
		}
		// Just return the same quality
		logger.error("Couldn't find quality {}", this);
		return this;
	}

	/**
	 * Increase the quality of an item
	 * @return
	 */
	public EquipmentQuality increase() {
		List<EquipmentQuality> values = Arrays.asList(values());
		Iterator<EquipmentQuality> iter = values.iterator();
		while(iter.hasNext()) {
			EquipmentQuality thisQuality = iter.next();
			if(thisQuality.id == id) {
				if(iter.hasNext()) {
					// Get the next quality
					thisQuality = iter.next();
					logger.debug("Next quality {}", thisQuality);
					return thisQuality;
				}
				logger.debug("No next quality for {}", this);
				return this;
			}
		}
		// Just return the same quality
		logger.error("Couldn't find quality {}", this);
		return this;
	}
}
