package org.kerrin.dungeon.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class MonsterTest {

	@Test
	public void testFromId() {
		for(Monster type:Monster.values()) {
			assertEquals(type, Monster.fromId(type.getId()));
		}
	}

	@Test
	public void testGetRandom() {
		Monster type = Monster.getRandom();
		assertNotEquals(type, Monster.UNKNOWN);
	}
}
