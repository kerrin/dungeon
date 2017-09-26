package org.kerrin.dungeon.utils;

import java.util.Map;

import org.kerrin.dungeon.enums.AttributePowerType;
import org.kerrin.dungeon.enums.AttributeValueEquationType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.PowerValues;

public class CommonTools {
	/**  */
	public static final long SECONDS_INMILLIS = 1000;
	/**  */
	public static final long MINUTES_INMILLIS = 60 * SECONDS_INMILLIS;
	/**  */
	public static final long HOURS_INMILLIS = 60 * MINUTES_INMILLIS;
	/**  */
	public static final long DAYS_INMILLIS = 24 * HOURS_INMILLIS;

	/**
	 * Calculate the power values for equipment and attribute totals, and 
	 * modify the passed power values and attribute totals appropriately
	 * 
	 * @param powerValues			Current Power values before applying this equipment
	 * @param equipment				Equipment to apply power values of
	 * @param attributeSummary		Attribute totals per attribute
	 * @param character				Character wearing the equipment
	 */
	public static void calculatePowerValues(PowerValues powerValues, 
			Equipment equipment, Map<EquipmentAttribute, Integer> attributeSummary, Character character) {
		Map<Integer, Integer> equipmentAttributes = equipment.getAttributes(true);
		for(EquipmentAttribute attribute:EquipmentAttribute.values()) {
			if(attribute.getPowerType() == AttributePowerType.NONE) continue;
			if(equipmentAttributes.get(attribute.getId()) != null) {
				int newValue = attribute.calculateRealValue(equipmentAttributes.get(attribute.getId()),equipment.getLevel());
				if(attributeSummary.containsKey(attribute)) {
					int value = attributeSummary.get(attribute);
					value += newValue;
					attributeSummary.put(attribute, value);
				} else {
					attributeSummary.put(attribute, newValue);
				}
				if(attribute.getAttributeValueEquationType().equals(AttributeValueEquationType.VALUE)) {
					if(character != null) {
						// This attribute doesn't reduce with level increases, so fix the value to counter that
						newValue *= character.getLevel();
					}
				}
				switch(attribute.getPowerType()) {
				case ATTACK_CLASS:
					if(character == null) {
						// Item not on a character so only use the highest class specific
						if(newValue > powerValues.classSpecificAttackValue) {
							powerValues.classSpecificAttackValue = newValue;
						}
					} else {
						if(character.getCharClass().getMainAttribute().equals(attribute)) {
							// Correct class
							powerValues.classSpecificAttackValue += newValue;
						}
					}
					break;
				case ATTACK:
					powerValues.attackValue += newValue;
					break;
				case DEFENCE_CLASS:
					if(character == null) {
						// Item not on a character so only use the highest class specific
						if(newValue > powerValues.classSpecificDefenceValue) {
							powerValues.classSpecificDefenceValue = newValue;
						}
					} else {
						if(character.getCharClass().getDefenceAttribute().equals(attribute)) {
							// Correct class
							powerValues.classSpecificDefenceValue += newValue;
						}
					}
					break;
				case DEFENCE:
					powerValues.defenceValue += newValue;
					break;
				case RECOVERY:
					powerValues.recoveryValue += newValue;
					break;
				default:
					break;
				}
			}
		}
	}
}
