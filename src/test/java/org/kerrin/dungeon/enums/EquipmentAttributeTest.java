package org.kerrin.dungeon.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EquipmentAttributeTest {

	@Test
	public void testFromId() {
		for(EquipmentAttribute attribute:EquipmentAttribute.values()) {
			assertEquals(attribute, EquipmentAttribute.fromId(attribute.getId()));
		}
	}

	@Test
	public void testGetRandomForEquipmentType() {
		for(EquipmentType equipmentType:EquipmentType.values()) {
			EquipmentAttribute attribute = EquipmentAttribute.getRandom(equipmentType);
			if(equipmentType == EquipmentType.UNKNOWN) {
				assertEquals("Attribute was set for UNKNOWN equipment type " + equipmentType, EquipmentAttribute.SPARKLES, attribute);
			} else {
				assertNotEquals("Attribute was not set for equipment type " + equipmentType, EquipmentAttribute.SPARKLES, attribute);
				assertTrue("Did not return a valid attribute for equipment type "+equipmentType, equipmentType.getBaseStatType().getValidAttributes().contains(attribute));
			}
		}
	}

	@Test
	public void testGetRandom() {
			EquipmentAttribute attribute = EquipmentAttribute.getRandom(null);
			assertNotEquals(attribute, EquipmentAttribute.SPARKLES);
	}
}
