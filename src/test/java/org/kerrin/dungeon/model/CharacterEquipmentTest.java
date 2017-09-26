package org.kerrin.dungeon.model;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.SuperTest;
import org.kerrin.dungeon.enums.CharSlot;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharacterEquipmentTest extends SuperTest {

	@Test
	public void testSimple() {		
		CharacterEquipment charEquipment = new CharacterEquipment(testCharacter, testCharacterSlots);
		
		assertEquals(CHAR_ID, charEquipment.getCharacterId());
		assertEquals(testCharacterSlots.keySet().size(), charEquipment.getCharacterSlots().keySet().size());
		restrictMockAccess();
	}

	@Test
	public void testSlots() {
		
		assertEquals(CHAR_ID_WITH_EQUIPMENT, testCharacterEquipment.getCharacterId());
		Map<CharSlot, Equipment> checkCharacterSlots = testCharacterEquipment.getCharacterSlots();
		assertEquals(CharSlot.values().length, checkCharacterSlots.size());
		for(CharSlot slot:CharSlot.values()) {
			assertEquals(testCharacterSlots.get(slot), testCharacterEquipment.getCharacterSlot(slot));
			assertEquals(testCharacterSlots.get(slot), checkCharacterSlots.get(slot));
		}
		
		Map<CharSlot, Equipment> characterSlots2 = new HashMap<CharSlot, Equipment>();
		for(CharSlot slot:CharSlot.values()) {
			Equipment equipment = createRandomEquipment(slot.getValidEquipment(), (int)(Math.random()*Integer.MAX_VALUE));
			characterSlots2.put(slot, equipment);
			assertEquals(testCharacterSlots.get(slot), testCharacterEquipment.setCharacterSlot(slot, characterSlots2.get(slot)));
			assertEquals(characterSlots2.get(slot), testCharacterEquipment.getCharacterSlot(slot));
		}
		restrictMockAccess();
	}
}
