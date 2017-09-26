package org.kerrin.dungeon.enums;

import java.util.ArrayList;
import java.util.List;

public enum BaseStatType {
	NONE(0),
	ARMOUR(1), // Armour 
	WEAPON(2), // Damage
	JEWLERY(3), // Stats (Int, Dex, Str)
	;
	
	private final int id;
	private List<EquipmentAttribute> validAttributes;
	
	static {
		ARMOUR.validAttributes = new ArrayList<EquipmentAttribute>();
		ARMOUR.validAttributes.add(EquipmentAttribute.ARMOUR);
		
		WEAPON.validAttributes = new ArrayList<EquipmentAttribute>();
		WEAPON.validAttributes.add(EquipmentAttribute.ACID_DAMAGE);
		WEAPON.validAttributes.add(EquipmentAttribute.COLD_DAMAGE);
		WEAPON.validAttributes.add(EquipmentAttribute.ELECTRIC_DAMAGE);
		WEAPON.validAttributes.add(EquipmentAttribute.FIRE_DAMAGE);
		WEAPON.validAttributes.add(EquipmentAttribute.HOLY_DAMAGE);
		WEAPON.validAttributes.add(EquipmentAttribute.MELEE_DAMAGE);
		WEAPON.validAttributes.add(EquipmentAttribute.PIERCING_DAMAGE);
		WEAPON.validAttributes.add(EquipmentAttribute.POISON_DAMAGE);
		WEAPON.validAttributes.add(EquipmentAttribute.WATER_DAMAGE);
	
		JEWLERY.validAttributes = new ArrayList<EquipmentAttribute>();
		JEWLERY.validAttributes.add(EquipmentAttribute.INTELLIGENCE);
		JEWLERY.validAttributes.add(EquipmentAttribute.DEXTERITY);
		JEWLERY.validAttributes.add(EquipmentAttribute.STRENGTH);	
	}

	private BaseStatType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public List<EquipmentAttribute> getValidAttributes() {
		return validAttributes;
	}	
}
