package org.kerrin.dungeon.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.EquipmentAttribute;

/**
 * Database record for a characters equipment
 * Lists the items in each slot
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="characters_equipment")
public class CharacterEquipment {
	/** The character identifier this equipment is for */
	@Id
	private long characterId;
	
	/** Map of all the characters equipment */
	@ElementCollection
	@MapKeyColumn(name="slot")
    @Column(name="equipment")
	private Map<CharSlot,Equipment> characterSlots;

	protected CharacterEquipment() {};
	
	public CharacterEquipment(Character character, Map<CharSlot,Equipment> characterSlots) {
		this.characterId = character.getId();
		this.characterSlots = characterSlots;
	}

	public long getCharacterId() {
		return characterId;
	}

	public void setCharacter(Character character) {
		this.characterId = character.getId();
	}

	public Map<CharSlot,Equipment> getCharacterSlots() {
		return characterSlots;
	}

	public void setCharacterSlots(Map<CharSlot,Equipment> characterSlots) {
		this.characterSlots = characterSlots;
	}
	
	/**
	 * Get the equipment id of the item in the requested slot, or -1 if no item
	 * 
	 * @param slot
	 * 
	 * @return	Equipment Id of item in slot, or -1 if empty
	 */
	public Equipment getCharacterSlot(CharSlot slot) {
		return characterSlots.get(slot);
	}
	
	/**
	 * Set the character slot to the new id, and return the current value in the slot
	 * 
	 * @param charSlot
	 * @param newEquipmentId
	 * @return Equipment Id of item in slot before, or -1 if empty
	 */
	public Equipment setCharacterSlot(CharSlot charSlot, Equipment newEquipment) {
		Equipment currentEquipment = getCharacterSlot(charSlot);
		if(newEquipment != null) {
			this.characterSlots.put(charSlot, newEquipment);
		} else {
			this.characterSlots.remove(charSlot);
		}
		return currentEquipment;
	}

	/**
	 * Looks through the equipment a character is wearing to calculate the total value of an attribute
	 * 
	 * @param attribute
	 * 
	 * @return	Total value from equipment
	 */
	public int getTotalAttributeValue(EquipmentAttribute attribute) {
		int value = 0;
		for(CharSlot slot:characterSlots.keySet()) {
			Equipment equipment = characterSlots.get(slot);
			value += attribute.calculateRealValue(equipment.getAttributeValue(attribute), equipment.getLevel());
		}
		return value;
	}
}
