package org.kerrin.dungeon.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class EquipmentQualityTest {

	@Test
	public void testFromId() {
		for(EquipmentQuality attribute:EquipmentQuality.values()) {
			assertEquals(attribute, EquipmentQuality.fromId(attribute.getId()));
		}
	}

	@Test
	public void testGetRandomDrop() {
			EquipmentQuality attribute = EquipmentQuality.getRandomDrop();
			assertNotEquals(attribute, EquipmentQuality.USELESS);
	}

	@Test
	public void testGetRandomReward() {
			EquipmentQuality attribute = EquipmentQuality.getRandomReward(DungeonType.DUNGEON);
			assertNotEquals(attribute, EquipmentQuality.USELESS);
	}
}
