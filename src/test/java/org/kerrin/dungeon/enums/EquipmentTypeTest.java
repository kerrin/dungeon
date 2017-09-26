package org.kerrin.dungeon.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

public class EquipmentTypeTest {

	@Test
	public void testFromId() {
		for(EquipmentType type:EquipmentType.values()) {
			assertEquals(type, EquipmentType.fromId(type.getId()));
		}
	}

	@Test
	public void testGetRandom() {
		EquipmentType type = EquipmentType.getRandomType();
		assertNotEquals(type, EquipmentType.UNKNOWN);
	}
	
	@Test
	public void testValidSlots() {
		for(EquipmentType type: EquipmentType.values()) {
			List<CharSlot> slots = type.getValidSlots();
			if(slots == null) {
				fail("Type has null valid slots: "+type);
			} else {
				for(CharSlot slot:slots) {
					if(slot == null) {
						fail("Type has null slot in list: "+type);
					} else {
						System.out.println(type.getName()+":"+slot.getNiceName());
					}
				}
			}
		}
	}
}
