package org.kerrin.dungeon.enums;

/**
 * All the possible character classes
 * 
 * @author Kerrin
 *
 */
public enum DamageType {
	NONE(0,"None!", EquipmentAttribute.SPARKLES, "white"),
	MELEE(1, "Melee", EquipmentAttribute.MELEE_DAMAGE, "#848484"), 
	/** Ignores armour */
	PIERCING(2, "Piercing", EquipmentAttribute.PIERCING_DAMAGE, "#DF7401"),
	/** Slows */
	COLD(3, "Cold", EquipmentAttribute.COLD_DAMAGE, "#0040FF"), 
	FIRE(4, "Fire", EquipmentAttribute.FIRE_DAMAGE, "#7C474D"),
	/** Increased by armour */
	ELECTRIC(5, "Electric", EquipmentAttribute.ELECTRIC_DAMAGE, "#A4AA08"),
	/** Damage over time */
	POISON(6, "Poison", EquipmentAttribute.POISON_DAMAGE, "#40B040"),
	WATER(7, "Water", EquipmentAttribute.WATER_DAMAGE, "#01A9DB"),
	/** Reduces armour */
	ACID(8, "Acid", EquipmentAttribute.ACID_DAMAGE, "#69A009"),
	HOLY(9, "Holy", EquipmentAttribute.HOLY_DAMAGE, "#AEAE60"), 
	STUN(10, "Stun", EquipmentAttribute.MELEE_DAMAGE, "black"),
	;
	
	private int id;
	private String niceName;
	private String htmlColour;
	private EquipmentAttribute equipmentAttribute;
	private DamageType realDamageType;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	private DamageType (int id, String niceName, EquipmentAttribute equipmentAttribute, DamageType realDamageType, String htmlColour) {
		this.id = id;
		this.niceName = niceName;
		this.equipmentAttribute = equipmentAttribute;
		this.realDamageType = realDamageType;
		this.htmlColour = htmlColour;
	}
	
	private DamageType (int id, String niceName, EquipmentAttribute equipmentAttribute, String htmlColour) {
		this(id, niceName, equipmentAttribute, null, htmlColour);
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
	 * Get the equivalent equipment attribute
	 * @return
	 */
	public EquipmentAttribute getEquipmentAttribute() {
		return equipmentAttribute;
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
	 * Get the real damage type of special damage types
	 * 
	 * @return
	 */
	public DamageType getRealDamageType() {
		return realDamageType==null?this:realDamageType;
	}

	/**
	 * Get the html colour to display this as
	 * 
	 * @return
	 */
	public String getHtmlColour() {
		return htmlColour;
	}

	/**
	 * Get a character class from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static DamageType fromId(int id) {
		for(DamageType thisDamageType:values()) {
			if(thisDamageType.id == id) return thisDamageType;
		}
		return NONE;
	}
}
