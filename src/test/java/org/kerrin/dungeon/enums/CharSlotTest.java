package org.kerrin.dungeon.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

public class CharSlotTest {

	@Test
	public void testFromId() {
		for(CharSlot type:CharSlot.values()) {
			assertEquals(type, CharSlot.fromId(type.getId()));
		}
	}
	
	@Test
	public void testValidSlots() {
		for(CharSlot charSlot: CharSlot.values()) {
			List<CharSlot> validSlots = charSlot.getValidEquipment().getValidSlots();
			if(validSlots == null) {
				fail("Type has null valid slots: "+charSlot);
			} else {
				for(CharSlot slot:validSlots) {
					if(slot == null) {
						fail("Type has null slot in list: "+charSlot);
					} else {
						System.out.println(charSlot.getName()+":"+slot.getNiceName());
					}
				}
			}
		}
	}
}
