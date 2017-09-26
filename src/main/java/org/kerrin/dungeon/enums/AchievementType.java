package org.kerrin.dungeon.enums;

/**
 * The types of hiscore
 * 
 * @author Kerrin
 *
 */
public enum AchievementType {
	/** Character to Level 2 */
	LEVEL2			(0, 	"Level Up (Level 2)", "Got a character to level up to level 2", 2, 1),
	/** Character to Level 2 */
	LEVEL4			(2, 	"Dungeon Unlock (Level 4)", "Got a character to level up to level 4 and unlock Dungeons", 4, 2),
	/** Character to Level 7 */
	LEVEL7			(4, 	"Modes Unlock (Level 7)", "Got a character to level 7 and unlocked Hardcore and Ironborn modes", 7, 3),
	/** Character to Level 10 */
	LEVEL10			(6, 	"Raids Unlock (Level 10)", "Got a character to level 10 and unlocked Raid dungeons", 10, 4),
	/** Character to Level 15 */
	LEVEL15			(8, 	"Teenage Agnst (Level 15)", "Got a character to level 15", 15, 5),
	/** Character to Level 20 */
	LEVEL20			(10, 	"Now I'm an adult (L21)", "Got a character to level 21", 21, 5),
	/** Character to Level 30 */
	LEVEL30			(12, 	"I still feel young (L30)", "Got a character to level 30", 30, 10),
	/** Character to Level 40 */
	LEVEL40			(14, 	"I wish I was young (L40)", "Got a character to level 40", 40, 25),
	/** Character to Level 50 */
	LEVEL50			(16, 	"Life beings at Level 50", "Got a character to level 50", 50, 50),
	/** Character to Level 60 */
	LEVEL60			(18, 	"Mature (Level 60)", "Got a character to level 60", 60, 100),
	
	/** Character to Level 5 only in dungeons */
	LEVEL5_DUNGEON	(20, "How hard can this be? (L5)", "Got a character to level 5 only in dungeons", 5, 5),
	/** Character to Level 10 only in dungeons */
	LEVEL10_DUNGEON	(22, "I keep almost levelling him (L10)", "Got a character to level 10 only in dungeons", 10, 10),
	/** Character to Level 20 only in dungeons */
	LEVEL20_DUNGEON	(24, "This is harder than it looks (L20)", "Got a character to level 20 only in dungeons", 20, 15),
	/** Character to Level 30 only in dungeons */
	LEVEL30_DUNGEON(26, "Halfway the hard way (L30)", "Got a character to level 30 only in dungeons", 30, 20),
	/** Character to Level 40 only in dungeons */
	LEVEL40_DUNGEON(28, "Still a way go to (Level 40)", "Got a character to level 40 only in dungeons", 40, 25),
	/** Character to Level 50 only in dungeons */
	LEVEL50_DUNGEON(30, "Won't be long now (Level 50)", "Got a character to level 50 only in dungeons", 50, 30),
	/** Character to Level 60 only in dungeons */
	LEVEL60_DUNGEON(32, "Doing it the hard way (L60)", "Got a character to level 60 only in dungeons", 60, 35),
	
	/** Character to Prestige Level 1 */
	LEVEL1_PRESTIGE(34, "Prestigous (Prestige 1)", "Got a character to prestige level 1", 1, 5),
	/** Character to Prestige Level 10 */
	LEVEL10_PRESTIGE(36, "Once I was 60 (P10)", "Got a character to prestige level 10", 10, 10),
	/** Character to Prestige Level 25 */
	LEVEL20_PRESTIGE(38, "Twice I was 60 (P20)", "Got a character to prestige level 20", 20, 15),
	/** Character to Prestige Level 50 */
	LEVEL50_PRESTIGE(40, "So experienced! (P50)", "Got a character to prestige level 50", 50, 20),
	/** Character to Prestige Level 100 */
	LEVEL100_PRESTIGE(42, "Century of experience (P100)", "Got a character to prestige level 100", 100, 25),
	/** Character to Prestige Level 150 */
	LEVEL150_PRESTIGE(44, "Master (Prestige 150)", "Got a character to prestige level 150", 150, 30),
	/** Character to Prestige Level 200 */
	LEVEL200_PRESTIGE(46, "Grand Master (P200)", "Got a character to prestige level 200", 200, 35),
	/** Character to Prestige Level 250 */
	LEVEL250_PRESTIGE(48, "Guru (Prestige 250)", "Got a character to prestige level 250", 250, 40),
	

	/** Total Character Levels 10 */
	LEVEL10_TOTAL(50, "Party at 10", "Got characters to a total level of 10", 10, 1),
	/** Total Character Levels 25 */
	LEVEL25_TOTAL(52, "Party of 25", "Got characters to a total level of 25", 25, 5),
	/** Total Character Levels 50 */
	LEVEL50_TOTAL(54, "Party for 50", "Got characters to a total level of 50", 50, 10),
	/** Total Character Levels 100 */
	LEVEL100_TOTAL(56, "Flash mob! (100)", "Got characters to a total level of 100", 100, 15),
	/** Total Character Levels 150 */
	LEVEL150_TOTAL(58, "Totally 150", "Got characters to a total level of 150", 150, 20),
	/** Total Character Levels 200 */
	LEVEL200_TOTAL(60, "Totally Too Many Levels (200)", "Got characters to a total level of 200", 200, 25),
	/** Total Character Levels 300 */
	LEVEL300_TOTAL(62, "Totally 300", "Got characters to a total level of 300", 300, 30),
	/** Total Character Levels 400 */
	LEVEL400_TOTAL(64, "Totally 400", "Got characters to a total level of 400", 400, 35),
	/** Total Character Levels 500 */
	LEVEL500_TOTAL(66, "Half K (500)", "Got characters to a total level of 500", 500, 40),
	/** Total Character Levels 750 */
	LEVEL750_TOTAL(68, "What a lot of levels (750)", "Got characters to a total level of 750", 750, 45),
	/** Total Character Levels 1000 */
	LEVEL1000_TOTAL(70, "Totally 1k", "Got characters to a total level of 1000", 1000, 50),
	/** Total Character Levels 2500 */
	LEVEL2500_TOTAL(72, "Totally 2.5k", "Got characters to a total level of 2500", 2500, 100),
	/** Total Character Levels 5000 */
	LEVEL5000_TOTAL(74, "Totally 5k", "Got characters to a total level of 5000", 5000, 150),
	/** Total Character Levels 10000 */
	LEVEL10000_TOTAL(76, "Totally 10k", "Got character to a total level of 10,000", 10000, 250),
	/** Total Character Levels 100000 */
	LEVEL100000_TOTAL(78, "Totally 100k", "Got character to a total level of 100,000", 100000, 500),
	
	/** Equip Item with an Attack Value of 50 or better */
	ATTACK_VALUE_50(80, "Feeble Attack (50)", "Equip an item with an Attack Value of 50 or better", 50, 5),
	/** Equip Item with an Attack Value of 100 or better */
	ATTACK_VALUE_100(82, "Standard Attack (100)", "Equip an item with an Attack Value of 100 or better", 100, 10),
	/** Equip Item with an Attack Value of 150 or better */
	ATTACK_VALUE_150(84, "Good Attack (150)", "Equip an item with an Attack Value of 150 or better", 150, 15),
	/** Equip Item with an Attack Value of 200 or better */
	ATTACK_VALUE_200(86, "Strong Attack (200)", "Equip an item with an Attack Value of 200 or better", 200, 20),
	
	/** Equip Item with a Defence Value of 25 or better */
	DEFENCE_VALUE_25(88, "Feeble Defence (25)", "Equip Item with a Defence Value of 25 or better", 25, 5),
	/** Equip Item with a Defence Value of 50 or better */
	DEFENCE_VALUE_50(90, "Standard Defence (50)", "Equip Item with a Defence Value of 50 or better", 50, 10),
	/** Equip Item with a Defence Value of 75 or better */
	DEFENCE_VALUE_75(92, "Good Defence (75)", "Equip Item with a Defence Value of 75 or better", 75, 15),
	/** Equip Item with an Defence Value of 100 or better */
	DEFENCE_VALUE_100(94, "Strong Defence (100)", "Equip Item with a Defence Value of 100 or better", 100, 20),

	/** Salvage an item */
	SALVAGE_ITEM(96, "Salvaging", "Salvage an item and claim dungeon tokens as a reward", 1, 5),
	/** Enchant an item */
	ENCHANT_ITEM(98, "Enchanting", "Enchant an item to replace an attribute with a better one", 1, 10),
	
	/** Complete an Adventure */
	FINISH_ADVENTURE(100, "Adventurous", "Complete an Adventure", 1, 1),
	/** Complete a Dungeon successfully */
	FINISH_DUNGEON(102, "Dungeonlisous", "Complete a Dungeon successfully, without all the characters dying", 1, 5),
	/** Complete a Dungeon successfully */
	FINISH_RAID(104, "Raid Array", "Complete a Raid successfully, without all the characters dying", 1, 10),
	/** Rush a Dungeon */
	RUSH_DUNGEON(105, "In A Rush", "Finish a dungeon early by paying tokens", 1, 10),
	
	/** Equip an Artifact */
	EQUIP_ARTIFACT(106, "Artifactiod", "Equip an Artifact on a character", 1, 5),
	/** Fully equip a character in Artifact equipment any level */
	EQUIP_ALL_ARTIFACTS(108, "All Artifacted", "Fully equip a character with Artifact equipment", 13, 10),
	/** Fully equip a character in Artifact equipment at level 60 */
	EQUIP_ALL_ARTIFACTS_MAX_LEVEL(110, "Top Artifacted", "Fully equip a character with level 60 Artifact equipment", 13, 50),
	/** Create a character of each of the 9 classes */
	ALL_CLASSES(112, "All Classy", "Create a character of each of the 9 classes", 9, 10),
	/** Create 10 character (in a single mode) */
	TEN_CHARACTERS(114, "One for each toe", "Have 10 characters at once", 10, 10),
	/** Increase stash size to 20 slots */
	STASH_20(116, "Stashed", "Increase stash size to 20 slots", 20, 10),
	/** Pay to resurrect a character */
	RESURRECT(118, "Just A Scratch!", "Pay to resurrect a dead character", 1, false, true, 1),
	/** Pay to level up a character */
	LEVEL_UP(120, "Levelled", "Pay to level up a character", 1, true, false, 1),
		
	/** Get your daily tokens up to 5 */
	LOGIN_TOKENS_5(122, "Login and Get 5 Free", "Get your daily tokens up to 5", 5, false, false, 5),
	/** Get your daily tokens up to 10 */
	LOGIN_TOKENS_10(124, "Decadent (10)", "Get your daily tokens up to 10", 10, false, false, 10),
	/** Get your daily tokens up to 25 */
	LOGIN_TOKENS_25(126, "5 Squared (25)", "Get your daily tokens up to 25", 25, false, false, 15),
	/** Get your daily tokens up to 50 */
	LOGIN_TOKENS_50(128, "Fifty Free (50)", "Get your daily tokens up to 50", 50, false, false, 20),
	/** Get your daily tokens up to 75 */
	LOGIN_TOKENS_75(130, "Seventy Five Free (75)", "Get your daily tokens up to 75", 75, false, false, 25),
	/** Get your daily tokens up to 100 */
	LOGIN_TOKENS_100(132, "Century Free (100)", "Get your daily tokens up to 100", 100, false, false, 30),
	
	/** Character to level 7 on new account in under 1 hour only in dungeons */
	SPEED_RUN_LEVEL_7_HOUR(134, "PENDING: 7 in 60", "Not implemented yet: Character to level 7 on new account in under 1 hour only in dungeons", 7, 100),
	/** Character to level 8 on new account in under 1 hour only in dungeons (only 2 minutes spare if you don't use higher level characters to boost) */
	SPEED_RUN_LEVEL_8_HOUR_NO_BOOST(136, "PENDING: 8 in 60", "Not implemented yet: Character to level 8 on new account in under 1 hour only in dungeons (no higher level characters in party)", 8, 200),
	/** Character to level 10 on new account in under 1 hour only in dungeons (need to user higher level characters to boost that were paid to levelled) */
	SPEED_RUN_LEVEL_10_HOUR_BOOST(138, "PENDING: 10 in 60", "Not implemented yet: Character to level 10 on new account in under 1 hour only in dungeons (higher level characters in parties allowed)", 10, 100),
	
	/** Get in the top 1000 on a high score */
	TOP_1000(140, "Public Profile! (Rank 1000)", "Get in the top 1000 on a high score", 1000, 1),
	/** Get in the top 100 on a high score */
	TOP_100(142, "How good am I (Rank 100)", "Get in the top 100 on a high score", 100, 5),
	/** Get in the top 50 on a high score */
	TOP_50(144, "Beat me if you can (Rank 50)", "Get in the top 50 on a high score", 50, 10),
	/** Get in the top 25 on a high score */
	TOP_25(146, "Elitist (Rank 25)", "Get in the top 25 on a high score", 25, 25),
	/** Get in the top 10 on a high score */
	TOP_10(148, "Now I've made it! (Rank 10)", "Get in the top 10 on a high score", 10, 50),
	/** Get in the top 5 on a high score */
	TOP_5(150, "One of the best! (Rank 5)", "Get in the top 5 on a high score", 5, 100),
	/** Get the top spot on a high score */
	TOP(152, "Simply the best! (Rank 1)", "Get the top spot on a high score", 1, 200),
	
	BOOST_ITEM_REDEEM_ANY(160, "Rare Redeem", "Redeem any of the rare boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_IMPROVE_QUALITY(162, "Quality Control", "Redeem an Improve Quality boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_DUNGEON_TOKENS(164, "A Token Gift", "Redeem a Dungeon Token boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_DUNGEON_SPEED(166, "Caffine Pill", "Redeem a Dungeon Speed Up boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_LEVEL_UP(168, "Page Of Learning", "Redeem a Level Up boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_XP_BOOST(170, "Tome Of Learning", "Redeem a XP Boost boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_RESURRECTION(172, "You are not dead yet", "Redeem a Resurrection boost items", 1, false, true, 10),
	
	BOOST_ITEM_REDEEM_ENCHANT_RANGE(174, "Dodgy Armor Body Shop Coupon", "Redeem a Re-enchant Range boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_ENCHANT_IMPROVE_RANGE(176, "Armor Body Shop Coupon", "Redeem an Improve Enchant Range boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_ENCHANT_REMOVE_CURSE(178, "Cleansing Bath", "Redeem a Remove Curse Enchant boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_ENCHANT_TYPE(180, "Free Spin", "Redeem a Re-enchant Type boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_MAGIC_FIND(182, "Sixth Sense", "Redeem a Magic Find boost items", 1, 10),
	
	BOOST_ITEM_REDEEM_CHANGE_NAME(184, "Now Known As", "Redeem a Change Name boost items", 1, 10),
	;
	
	/** The order this achievement is show on the achievements page */
	private int order;
	private String niceName;
	private String description;
	private boolean hardcorePossible;
	private boolean ironbornPossible;
	/** The threshold to award the achievement */
	private long threshold;
	/** Points awarder for getting the achievement */
	private long points;

	private AchievementType(int order, String niceName, String description, long threshold, int points) {
		this(order, niceName, description, threshold, true, true, points);
	}
	
	private AchievementType(int order, String niceName, String description, long threshold, boolean hardcorePossible, boolean ironbornPossible, int points) {
		this.order = order;
		this.niceName = niceName;
		this.description = description;
		this.threshold = threshold;
		this.hardcorePossible = hardcorePossible;
		this.ironbornPossible = ironbornPossible;
		this.points = points;
	}
	
	public int getOrder() {
		return order;
	}

	public static AchievementType fromId(int id) {
		for(AchievementType thisType:values()) {
			if(thisType.order == id) return thisType;
		}
		return null;
	}
	
	public String getNiceName() {
		return niceName;
	}
	
	public String getDescription() {
		return description;
	}

	public long getThreshold() {
		return threshold;
	}

	public boolean isHardcorePossible() {
		return hardcorePossible;
	}

	public boolean isIronbornPossible() {
		return ironbornPossible;
	}

	public long getPoints() {
		return points;
	}
}
