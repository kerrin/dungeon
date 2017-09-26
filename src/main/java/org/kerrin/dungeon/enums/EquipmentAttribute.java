package org.kerrin.dungeon.enums;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All the equipment slots for a character
 * 
 * Note resist and other percentage values are calculated as follows:
 * Resist value = Item level * value 
 * where value is (<= 1000/12) (12 is number is equipment slots)
 * That makes it just possible to be immune to a damage type if all slots have the max roll for it
 * Maybe make it easier?
 * Resist Percent = (Monster Level * 1000) / Resist Value
 * 
 * Attributes can be cursed, which means they have a negative value, items with these on may be compensated some how
 * 
 * @author Kerrin
 *
 */
public enum EquipmentAttribute {
	SPARKLES(		0,	"Sparkles!", "It's sparkly! It makes you look cool!", 
			AttributeType.UNKNOWN, AttributePowerType.NONE, 0, 0, 
			false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.ONE_HUNDRED, true),
	INTELLIGENCE(	1,	"Intelligence", "Is used by caster classes to increase damage.", 
			AttributeType.STAT, AttributePowerType.ATTACK_CLASS, 1000, 10), 
	STRENGTH(		2,	"Strength", "Is used by melee classes to increase damage.", 
			AttributeType.STAT, AttributePowerType.ATTACK_CLASS, 1000, 10), 
	DEXTERITY(		3,	"Dexterity", "Is used by agile classes to increase damage.", 
			AttributeType.STAT, AttributePowerType.ATTACK_CLASS, 1000, 10), 
	ARMOUR(			4,	"Armour", "Is used by all classes to reduce incomming damage.", 
			AttributeType.STAT, AttributePowerType.DEFENCE, 500, 5), 
	DODGE(			5,	"Dodge", "Is used by all melee classes to reduce incomming damage.", 
			AttributeType.STAT, AttributePowerType.DEFENCE_CLASS, 500, 5), 
	GUILE(			6,	"Guile", "Is used by agile classes to reduce incomming damage.", 
			AttributeType.STAT, AttributePowerType.DEFENCE_CLASS, 500, 5),
	RANGE(			7,	"Range", "Is used by range classes to reduce incomming damage.", 
			AttributeType.STAT, AttributePowerType.DEFENCE_CLASS, 500, 5),
	HEAL(			8,	"Heal", "Increases amount healed.", 
			AttributeType.BUFF, AttributePowerType.RECOVERY, 650, 6),
	MELEE_RESIST(	9,	"Melee Resist", "Reduces damage of incomming melee attacks. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 650, 6, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false), 
	PIERCING_RESIST(10,	"Piercing Resist", "Reduces damage of incomming projectile attacks. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 650, 6, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false), 
	COLD_RESIST(	11, "Cold Resist", "Reduces damage of incomming cold attacks. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 650, 6, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false), 
	FIRE_RESIST(	12, "Fire Resist", "Reduces damage of incomming fire attacks. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 650, 6, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false), 
	ELECTRIC_RESIST(13, "Electric Resist", "Reduces damage of incomming electric attacks. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 650, 6, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	POISON_RESIST(	14, "Poison Resist", "Reduces damage of incomming posion attacks. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 650, 6, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	WATER_RESIST(	15, "Water Resist", "Reduces damage of incomming water attacks. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 650, 6, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	ACID_RESIST(	16, "Acid Resist", "Reduces damage of incomming acid attacks. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 650, 6, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	HOLY_RESIST(	17, "Holy Resist", "Reduces damage of incomming holy attacks. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 650, 6, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	MELEE_DAMAGE(	18, "Melee Damage", "Increases damage of melee attacks.", 
			AttributeType.DAMAGE, AttributePowerType.ATTACK, 750, 7, MELEE_RESIST), 
	PIERCING_DAMAGE(19, "Piercing Damage", "Increases damage of projectiles.", 
			AttributeType.DAMAGE, AttributePowerType.ATTACK, 750, 7, PIERCING_RESIST), // Splash Damage
	COLD_DAMAGE(	20, "Cold Damage", "Increases damage of cold attacks.", 
			AttributeType.DAMAGE, AttributePowerType.ATTACK, 750, 7, COLD_RESIST), // Slows target
	FIRE_DAMAGE(	21, "Fire Damage", "Increases damage of fire attacks.", 
			AttributeType.DAMAGE, AttributePowerType.ATTACK, 750, 7, FIRE_RESIST), 
	ELECTRIC_DAMAGE(22, "Electric Damage", "Increases damage of electric.", 
			AttributeType.DAMAGE, AttributePowerType.ATTACK, 750, 7, ELECTRIC_RESIST), // Stun chance
	POISON_DAMAGE(	23, "Poison Damage", "Increases damage of posion attacks.", 
			AttributeType.DAMAGE, AttributePowerType.ATTACK, 750, 7, POISON_RESIST), // Damage over time target
	WATER_DAMAGE(	24, "Water Damage", "Increases damage of water attacks.", 
			AttributeType.DAMAGE, AttributePowerType.ATTACK, 750, 7, WATER_RESIST),
	ACID_DAMAGE(	25, "Acid Damage", "Increases damage of acid attack.", 
			AttributeType.DAMAGE, AttributePowerType.ATTACK, 750, 7, ACID_RESIST), // Damage over time target
	HOLY_DAMAGE(	26, "Holy Damage", "Increases damage of holy attacks.", 
			AttributeType.DAMAGE, AttributePowerType.ATTACK, 750, 7, HOLY_RESIST),
	CRIT_CHANCE(	27, "Critical Chance", "Increase your chance to critical hit. 100% would be 100 times the attackers level.", 
			AttributeType.BUFF, AttributePowerType.ATTACK, 450, 4, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	CRIT_DAMAGE(	28, "Critical Damage", "Increase critical damage percent. Critical damage starts at double damage.", 
			AttributeType.BUFF, AttributePowerType.ATTACK, 450, 4),
	SPLASH_CHANCE(	29, "Splash Chance", "Chance for an attack to hit adjacent enemies. 100% would be 100 times the attackers level.", 
			AttributeType.BUFF, AttributePowerType.ATTACK, 350, 3, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	SPLASH_DAMAGE(	30, "Splash Damage", "Percentage of original damage that adjacent enemies take. 100% would be 100 times the attackers level.", 
			AttributeType.BUFF, AttributePowerType.ATTACK, 350, 3, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	ATTACK_SPEED(	31, "Attack Speed", "Attack speed increase. 100% would be 100 times the attackers level.", 
			AttributeType.BUFF, AttributePowerType.ATTACK, 450, 4, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	BLEED(			32, "Bleed Damage", "Damage the enemy continues to take after being hit until healed.", 
			AttributeType.DAMAGE, AttributePowerType.ATTACK, 275, 2),
	DAMAGE_OVER_TIME(33, "Damage Over Time", "Damage the enemy continues to take after being hit until cleansed.", 
			AttributeType.BUFF, AttributePowerType.ATTACK, 200, 2),
	STUN_CHANCE(	34, "Stun Chance", "Chance to stun the hit enemy. 100% would be 100 times the attackers level.", 
			AttributeType.BUFF, AttributePowerType.DEFENCE, 250, 2, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	STUN_LENGTH(	35, "Stun Length", "How long enemies are stunned for when stunned.", 
			AttributeType.BUFF, AttributePowerType.DEFENCE, 250, 2, false, AttributeValueEquationType.VALUE, AttributeValueMaxType.THREE, false),
	STUN_RESIST(	36, "Stun Resist", "Reduces chance you will be stunned by enimies. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 150, 1, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	HEAL_RESIST_MOB(37, "Heal Reduction", "Reduce the healing on the enemy. 100% would be 100 times the enemies level.", 
			AttributeType.BUFF, AttributePowerType.ATTACK, 250, 6, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),	// Reduces heal on monsters
	ALL_RESIST(		38, "Resist All", "Reduces damage of ALL incomming attacks. 100% would be 100 times the attackers level.", 
			AttributeType.RESIST, AttributePowerType.DEFENCE, 200, 2, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	REDUCE_LEVEL(	39, "Reduced Level", "Items other attributes have values of items this much higher in level.", 
			AttributeType.SPECIAL, AttributePowerType.NONE, 300, 0, false, AttributeValueEquationType.VALUE, AttributeValueMaxType.LEVEL, true),
	SPEED(			40, "Run Speed", "Increases movement speed, allowing quicker clearing of dungeons. Maximum reduction is 50%.", 
			AttributeType.SPEED, AttributePowerType.NONE, 200, 1),
	HEALTH_REGEN(	41, "Health Regen", "How much health you regenerate over time. As you level up, your regen value will need to increase to maintain the same regen rate.", 
			AttributeType.SPECIAL, AttributePowerType.RECOVERY, 200, 2, false, AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.SLOTS, false),
	MANA_REGEN(		42, "Mana Regen", "How much mana you regenerate over time. As you level up, your regen value will need to increase to maintain the same regen rate.", 
			AttributeType.SPECIAL, AttributePowerType.RECOVERY, 200, 2, false, AttributeValueEquationType.VALUE, AttributeValueMaxType.SLOTS, false),
	MAGIC_FIND(		43, "Magic Find", "Increases chance of random equipment dropping.", 
			AttributeType.SPECIAL, AttributePowerType.NONE, 200, 2, false, AttributeValueEquationType.VALUE, AttributeValueMaxType.SLOTS, false),
	;
	
	private static final Logger logger = LoggerFactory.getLogger(EquipmentAttribute.class);
	
	private final int id;
	private final String niceName;
	private final String description;
	private final AttributeType type;
	private final AttributePowerType powerType;
	/** Value for how likely it rolls */
	private final int rollChanceWeighting;
	private final int cursedChanceWeigthing;
	private final boolean badAttribute;
	private final boolean cantBeAncient;
	
	/** If a damage type attribute, this is the equivalent resist attribute */
	private EquipmentAttribute resistAttribute;
	
	private final AttributeValueEquationType attributeValueEquationType;
	
	private final AttributeValueMaxType attributeValueMaxType;
	
	private static final int totalChance;
	
	private static final int totalBaseOrDefenceChance;
	
	// Initialise the total chance of all attributes
	static {
		int tempTotalChance = 0;
		int tempTotalBaseOrDefenceChance = 0;
		for(EquipmentAttribute attribute:values()) {
			tempTotalChance += attribute.rollChanceWeighting;
			if(attribute.isBase() || attribute.isDefence()) {
				tempTotalBaseOrDefenceChance += attribute.rollChanceWeighting;
			}
		}
		totalChance = tempTotalChance;
		totalBaseOrDefenceChance = tempTotalBaseOrDefenceChance;
	}
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	EquipmentAttribute (int id, String niceName, String description, AttributeType type, AttributePowerType powerType, 
			int rollChance, int cursedChance) {
		this(id, niceName, description, type, powerType, rollChance, cursedChance, false, 
				AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.ONE_HUNDRED, false);
	}
	
	EquipmentAttribute (int id, String niceName, String description, AttributeType type, AttributePowerType powerType, 
			int rollChance, int cursedChance, EquipmentAttribute resistAttribute) {
		this(id, niceName, description, type, powerType, rollChance, cursedChance, false, 
				AttributeValueEquationType.LEVEL_MULTI, AttributeValueMaxType.ONE_HUNDRED, false);
		this.resistAttribute = resistAttribute;
	}
	
	EquipmentAttribute (int id, 
			String niceName, String description, 
			AttributeType type, AttributePowerType powerType, 
			int rollChanceWeighting, int cursedChanceWeighting, boolean badAttribute, 
			AttributeValueEquationType attributeValueEquationType, AttributeValueMaxType attributeValueMaxType,
			boolean cantBeAncient) {
		this.id = id;
		this.niceName = niceName;
		this.description = description;
		this.type = type;
		this.powerType = powerType;
		this.rollChanceWeighting = rollChanceWeighting;
		this.cursedChanceWeigthing = cursedChanceWeighting;
		this.badAttribute = badAttribute;
		this.attributeValueEquationType = attributeValueEquationType;
		this.attributeValueMaxType = attributeValueMaxType;
		this.cantBeAncient = cantBeAncient;
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
	 * The detailed description of the attribute
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * For use by JSTL for field names
	 * @return
	 */
	public String getName() {
		return name();
	}
	
	public AttributeType getType() {
		return type;
	}

	public AttributePowerType getPowerType() {
		return powerType;
	}

	public int getRollChance() {
		return rollChanceWeighting;
	}

	public int getCursedChance() {
		return cursedChanceWeigthing;
	}

	public boolean isBadAttribute() {
		return badAttribute;
	}

	public boolean isCantBeAncient() {
		return cantBeAncient;
	}

	public boolean isBase() {
		return this == EquipmentAttribute.STRENGTH || this == EquipmentAttribute.INTELLIGENCE || this == DEXTERITY;
	}

	public boolean isDefence() {
		return this == EquipmentAttribute.ARMOUR || this == EquipmentAttribute.DODGE || this == EquipmentAttribute.GUILE || this == RANGE;
	}

	public AttributeValueMaxType getAttributeValueMaxType() {
		return attributeValueMaxType;
	}

	public AttributeValueEquationType getAttributeValueEquationType() {
		return attributeValueEquationType;
	}

	public EquipmentAttribute getResistAttribute() {
		return resistAttribute;
	}

	/**
	 * Given a base value, return the value it provides at this level
	 * @param baseValue
	 * @return
	 */
	public int calculateRealValue(int baseValue, int level) {
		switch(attributeValueEquationType) {
		case VALUE: return baseValue;
		case LEVEL_MULTI:
			return baseValue * level;
		}
		logger.error("Unknown attributeValueEquationType {}", attributeValueEquationType);
		return baseValue;
	}

	/**
	 * Get an attribute from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static EquipmentAttribute fromId(int id) {
		for(EquipmentAttribute thisSlot:values()) {
			if(thisSlot.id == id) return thisSlot;
		}
		return SPARKLES;
	}
	
	/**
	 * Get a reward quality based on chance
	 * If equipmentType is not null, only base stats will be allowed for the equipment type
	 * 
	 * @param equipmentType type of equipment to roll base stat for, or null if any stat valid
	 * 
	 * @return
	 */
	public static EquipmentAttribute getRandom(EquipmentType equipmentType) {		
		int chance = totalChance;
		EquipmentAttribute[] allowedAttributes = values();
		if(equipmentType != null) {
			if(equipmentType == EquipmentType.UNKNOWN) {
				logger.error("Equipment type was: "+equipmentType.name());
				return EquipmentAttribute.SPARKLES;
			}
			allowedAttributes = equipmentType.getBaseStatType().getValidAttributes().toArray(new EquipmentAttribute[0]);
			chance = 0;
			for(EquipmentAttribute thisAttribute:allowedAttributes) {
				chance += thisAttribute.rollChanceWeighting;
			}
		}
		for(EquipmentAttribute thisAttribute:allowedAttributes) {
			int random = (int) (Math.random()*chance);
			if(random < thisAttribute.rollChanceWeighting) {
				return thisAttribute;
			}
			chance -= thisAttribute.rollChanceWeighting;
		}
		logger.error("No attribute rolled, still had chance "+chance+", total chance is "+totalChance);
		if(equipmentType != null) {
			logger.error("Equipment type was: "+equipmentType.name());
		}
		return EquipmentAttribute.SPARKLES;
	}
	
	public static EquipmentAttribute getRandomBase() {
		return getRandomBase(new ArrayList<EquipmentAttribute>());
	}
	
	public static EquipmentAttribute getRandomBase(List<EquipmentAttribute> requiredGoodAttributes) {
		for(EquipmentAttribute attribute: requiredGoodAttributes) {
			if(attribute.isBase()) {
				requiredGoodAttributes.remove(attribute);
				return attribute;
			}
		}
		EquipmentAttribute attribute = EquipmentAttribute.SPARKLES;
		while(!attribute.isBase()) attribute = getRandomBaseOrDefence();
		return attribute;
	}
	
	public static EquipmentAttribute getRandomDefence() {
		return getRandomDefence(new ArrayList<EquipmentAttribute>());
	}
	
	public static EquipmentAttribute getRandomDefence(List<EquipmentAttribute> requiredGoodAttributes) {
		for(EquipmentAttribute attribute: requiredGoodAttributes) {
			if(attribute.isDefence()) {
				requiredGoodAttributes.remove(attribute);
				return attribute;
			}
		}
		EquipmentAttribute attribute = EquipmentAttribute.SPARKLES;
		while(!attribute.isDefence()) attribute = getRandomBaseOrDefence();
		return attribute;
	}
	
	public static EquipmentAttribute getRandomBaseOrDefence() {		
		int chance = totalBaseOrDefenceChance;
		List<EquipmentAttribute> allowedAttributes = statsValues();
		for(EquipmentAttribute thisAttribute:allowedAttributes) {
			int random = (int) (Math.random()*chance);
			if(random < thisAttribute.rollChanceWeighting) {
				return thisAttribute;
			}
			chance -= thisAttribute.rollChanceWeighting;
		}
		logger.error("No attribute rolled, still had chance "+chance+", total chance is "+totalChance);
		return EquipmentAttribute.STRENGTH;
	}

	private static List<EquipmentAttribute> statsValues() {
		List<EquipmentAttribute> attributesList = new ArrayList<EquipmentAttribute>();
		EquipmentAttribute[] values = values();
		for(EquipmentAttribute thisAttribute:values) {
			if(thisAttribute.type == AttributeType.STAT) {
				attributesList.add(thisAttribute);
			}
		}
		return attributesList;
	}

	/**
	 * Check if the value passed in is already max value
	 * 
	 * @param level
	 * @param currentValue
	 * @return
	 */
	public boolean isMaxValue(int level, int currentValue) {
		return attributeValueMaxType.isMaxValue(level, currentValue);
	}
}
