package org.kerrin.dungeon.enums;

/**
 * The types of hiscore
 * 
 * @author Kerrin
 *
 */
public enum HiscoreType {
	/** Highest character level (base level + prestige level) */
	HIGHEST_LEVEL(0, "Highest Level", "Highest character level (A combination of level and prestige)"),
	/** Highest total level of all characters including prestige */
	TOTAL_LEVEL(1, "Total Level", "Highest total level of all characters including prestige"),
	/** Fastest character from 1 to max level only using dungeons */
	FASTEST_MAX_LEVEL(2, "Fastest to Maximum Level", "Shortest time to get a character from 1 to max level only using dungeons"),
	/** Fastest character from 1 to max level only using dungeons after an account reset */
	FASTEST_MAX_LEVEL_AFTER_RESET(3, "Fastest to Maximum Level - New Account", "Fastest character from 1 to max level only using dungeons after an account reset"),
	/** Most tokens earnt */
	TOKENS_EARNT(4, "Total Tokens Earnt", "Most tokens earnt in game"),
	/** Most tokens purchased for real money */
	TOKENS_PURCHASED(5, "Total Tokens Purchased", "Most tokens purchased for real money"),
	/** Most achievement points */
	ACHIEVEMENT_POINTS(6, "Total Achievement Points", "Most Achievement Points", true),
	;
	
	private int id;
	private String niceName;
	private String description;
	private boolean modeless = false;

	private HiscoreType(int id, String niceName, String description) {
		this(id, niceName, description, false);
	}
	
	private HiscoreType(int id, String niceName, String description, boolean modeless) {
		this.id = id;
		this.niceName = niceName;
		this.description = description;
	}
	
	public static HiscoreType fromId(int id) {
		for(HiscoreType thisType:values()) {
			if(thisType.id == id) return thisType;
		}
		return null;
	}
	
	public String getNiceName() {
		return niceName;
	}
	
	public String getDescription() {
		return description;
	}

	public boolean isModeless() {
		return modeless;
	}
}
