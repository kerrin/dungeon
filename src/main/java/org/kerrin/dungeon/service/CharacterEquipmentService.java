package org.kerrin.dungeon.service;

import java.util.Map;

import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Equipment;
import org.springframework.stereotype.Service;

@Service
public interface CharacterEquipmentService {
	public CharacterEquipment create(CharacterEquipment character);
    public CharacterEquipment delete(Character character) 
    		throws CharacterEquipmentNotFound, CharacterSlotNotFound;
    public CharacterEquipment update(CharacterEquipment character) throws CharacterEquipmentNotFound, CharacterNotFound;
    public CharacterEquipment findById(long id);
	public Map<CharSlot, Equipment> findAllByCharacter(Character character);
	public Equipment findEquipmentForCharacterAndCharSlot(Character character, CharSlot charSlot);
	/**
	 * 
	 * @param character
	 * @param charSlot
	 * @param equipmentId
	 * @return Equipment in slot before, -1 if slot was empty before, or null if error
	 */
	public Equipment equipmentItem(Character character, CharSlot charSlot, Equipment equipment);
	/**
	 * 
	 * @param character
	 * @param equipmentId
	 * @return Equipment removed slot, -1 if slot was empty before, or null if error
	 */
	public Equipment unequipmentItem(Character character, CharSlot charSlot);
	/**
	 * 
	 * @param character
	 * @param equipmentId
	 * @return Char Slot the item was in, or null if not found
	 */
	public CharSlot unequipmentItem(Character character, Equipment equipment);
	/**
	 * Get the char slot the equipment is in on this character
	 * 
	 * @param character
	 * @param equipment
	 * @return
	 */
	public CharSlot findCharSlotForEquipment(Character character, Equipment equipment);
}
