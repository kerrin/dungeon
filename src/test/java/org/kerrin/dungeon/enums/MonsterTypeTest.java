package org.kerrin.dungeon.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class MonsterTypeTest {

	@Test
	public void testFromId() {
		for(MonsterType type:MonsterType.values()) {
			assertEquals(type, MonsterType.fromId(type.getId()));
		}
	}

	@Test
	public void testGetRandom() {
		MonsterType type = MonsterType.getRandomType();
		assertNotEquals(type, MonsterType.NONE);
	}
}
