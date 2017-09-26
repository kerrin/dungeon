package org.kerrin.dungeon.enums;

/**
 * All the possible character classes
 * 
 * @author Kerrin
 *
 */
public enum CharClass {
	// Note: id must match equivalent monster ids
	// 		id, name, 		health, mana, 	damage, main attribute, 			defence attribute
	ANY(	0, "Any", 		0, 		0, 		0,	EquipmentAttribute.SPARKLES, 	EquipmentAttribute.SPARKLES), // Used for searches only
	MELEE(	1, "Barbarian", 500, 	100,	20,	EquipmentAttribute.STRENGTH, 	EquipmentAttribute.DODGE), 
	MAGIC(	2, "Wizard", 	200, 	100,	20,	EquipmentAttribute.INTELLIGENCE,EquipmentAttribute.RANGE), 
	HEALER(	3, "Cleric", 	300, 	100,	10,	EquipmentAttribute.INTELLIGENCE,EquipmentAttribute.DODGE), 
	RANGE(	4, "Archer", 	200, 	100,	20,	EquipmentAttribute.DEXTERITY, 	EquipmentAttribute.RANGE), 
	SNEAKY(	5, "Rogue", 	250, 	100,	30,	EquipmentAttribute.DEXTERITY, 	EquipmentAttribute.GUILE),
	BUFF(	6, "Paladin", 	400, 	100,	18,	EquipmentAttribute.STRENGTH, 	EquipmentAttribute.GUILE),
	PETS(	7, "Necromancer",200,	100,	12,	EquipmentAttribute.INTELLIGENCE,EquipmentAttribute.GUILE),
	MOBILE(	8, "Monk",		350, 	100,	16,	EquipmentAttribute.DEXTERITY,	EquipmentAttribute.DODGE),
	BARD(	9, "Hard Bard",	250, 	100,	8,	EquipmentAttribute.STRENGTH,	EquipmentAttribute.RANGE)
	;
	
	private int id;
	private String name;
	private EquipmentAttribute mainAttribute;
	private EquipmentAttribute defenceAttribute;
	private int baseHealth;
	private int baseMana;
	private int baseDamage;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	private CharClass (int id, String name, int baseHealth, int baseMana, int baseDamage, EquipmentAttribute mainAttribute, 
			EquipmentAttribute defenceAttribute) {
		this.id = id;
		this.name = name;
		this.baseHealth = baseHealth;
		this.baseMana = baseMana;
		this.baseDamage = baseDamage;
		this.mainAttribute = mainAttribute;
		this.defenceAttribute = defenceAttribute;
	}
	
	/**
	 * Get the identifier
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	public String getName() {
		return super.name();
	}
	
	/**
	 * Get the nice name
	 * 
	 * @return
	 */
	public String getNiceName() {
		return name;
	}
	
	public EquipmentAttribute getMainAttribute() {
		return mainAttribute;
	}

	public EquipmentAttribute getDefenceAttribute() {
		return defenceAttribute;
	}

	public int getBaseHealth() {
		return baseHealth;
	}

	public int getBaseMana() {
		return baseMana;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	/**
	 * Get a character class from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static CharClass fromId(int id) {
		for(CharClass thisCharClass:values()) {
			if(thisCharClass.id == id) return thisCharClass;
		}
		return ANY;
	}

	public static CharClass getRandom() {
		int typeCount = values().length - 1;
		int index = (int)(Math.random()*typeCount)+1;
		return fromId(index);
	}
}
