package org.kerrin.dungeon.model;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.SuperTest;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InventoryTest extends SuperTest {

	private static final int SLOT_NUMBER2 = 4;
	private static final CharSlot VALID_SLOT = CharSlot.OFF_HAND;
	
	private Map<Integer, StashSlotItemSuper> inventorySlots;
	@Before
	public void setup() {
		super.setUp();
		inventorySlots = new HashMap<Integer, StashSlotItemSuper>();
	}
	
	@Test
	public void testConstructor() {
		HashMap<Integer,StashSlotItemSuper> inventorySlots = new HashMap<Integer, StashSlotItemSuper>(SLOT_NUMBER);
		for(int i=0; i < SLOT_NUMBER; i++) {
			Equipment equipment = createRandomEquipment();
			equipmentList.add(i, equipment);
			inventorySlots.put(i, equipmentList.get(i));
		}
		// Set up the inventory
        testInventory = new Inventory(ACCOUNT_ID, false, false, SLOT_NUMBER, inventorySlots);
		assertEquals(ACCOUNT_ID, testInventory.getAccountId());
		assertEquals(SLOT_NUMBER, testInventory.getSize());
		assertEquals(SLOT_NUMBER, testInventory.getInventorySlots().keySet().size());
		restrictMockAccess();
	}

	@Test
	public void testSetMethods() {
		testInventory.setAccount(testAccount2);
		testInventory.setSize(SLOT_NUMBER2);
		Map<Integer, Integer> attributes = new HashMap<Integer, Integer>();
		for(int i=0; i < SLOT_NUMBER2; i++) {
			Set<CharSlot> validSlots = new HashSet<CharSlot>();
			validSlots.add(VALID_SLOT);
			Equipment equipment = new Equipment(i+1, 
					EquipmentType.SHOULDERS, EquipmentQuality.COMMON, i+2, false, false, 
					null/*baseAttribute*/, 0/*baseAttributeValue*/, 
					null/*defenceAttribute*/, 0/*defenceAttributeValue*/, 
					attributes, 
					null/*ancientAttribute*/, 0/*ancientAttributeValue*/, 
					EquipmentLocation.INVENTORY, i);
			inventorySlots.put(i, equipment );
		}
		testInventory.setInventorySlots(inventorySlots);
		
		assertEquals(ACCOUNT_ID2, testInventory.getAccountId());
		assertEquals(SLOT_NUMBER2, testInventory.getSize());
		Map<Integer, StashSlotItemSuper> checkSlots = testInventory.getInventorySlots();
		assertEquals(SLOT_NUMBER2, checkSlots.size());
		
		for(Integer slot:checkSlots.keySet()) {
			assertEquals(checkSlots.get(slot), inventorySlots.get(slot));
		}
		restrictMockAccess();
	}
}
