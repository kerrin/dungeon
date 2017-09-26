package org.kerrin.dungeon.enums;

public enum ModificationType {
	// Nice text must be 20 characters or less
	/** Administration adjustment */
	ADMIN("Account Correction"),
	/** Account creation */
	GAIN_CREATE("Account Creation"),
	/** Daily login reward */
	GAIN_DAILY("Daily Login Reward"),
	/** Close successful dungeon reward */
	GAIN_DUNGEON_REWARD("Dungeon Reward"),
	/** Salvage item */
	GAIN_SALVAGE("Equipment Salvage"),
	/** Purchase for real money */
	GAIN_PURCHASE("Tokens Purchased"),
	/** Bonus item in dungeon redeem */
	GAIN_ITEM("Token Item"),
	/** Spent on levelling a character */
	SPEND_LEVELUP("Character Level Up"),
	/** Spent on resurrecting a character */
	SPEND_RESURRECT("Character Resurrect"),
	/** Spent on a boost (XP, unlock temporary buttons, increase dungeon level) */
	SPEND_BOOST("Temporary Boost"),
	/** Spent on a permanent upgrade (stash space, unlock buttons) */
	SPEND_UPGRADE("Permanent Upgrade"),
	/** Purchase a new character */
	SPEND_CHARACTER("New Character"),
	/** Spent on enchanting equipment */
	SPEND_ENCHANT("Equipment Enchant"),
	/** Spent on rushing a dungeon */
	SPEND_RUSH("Dungeon Rush"),
	/** Spent on refreshing available dungeons */
	SPEND_DUNGEON_REFRESH("Refresh Dungeon List"),
	/** Spent on increasing dungeon level */
	SPEND_DUNGEON_LEVEL_INCREASE("Modify dungeon level"),
	/** Account reset */
	ACCOUNT_RESET("Account Reset"),
	/** Free character resurrection for all characters not in a dungeon */
	FREE_RESURRECTION("Daily Resurrection"),
	;
	
	private String niceName;
	
	private ModificationType(String niceName) {
		this.niceName = niceName;
	}
	
	public String getNiceName() {
		return niceName;
	}
}
