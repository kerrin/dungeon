package org.kerrin.dungeon.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.SuperTest;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentTest extends SuperTest {
	
	@Test
	public void testConstructor() {
		assertEquals(EQUIPMENT_ID, testEquipment.getId());
		assertEquals(EQUIPMENT_QUALITY, testEquipment.getQuality());
		assertEquals(LEVEL, testEquipment.getLevel());
		assertEquals(EQUIPMENT_QUALITY.getId() - 2, testEquipment.getAttributes(false).size());		
		restrictMockAccess();
	}

	@Test
	public void testSetMethods() {
		testEquipment.setId(EQUIPMENT_ID2);
		testEquipment.setLevel(LEVEL2);
		for(EquipmentAttribute attribute: EquipmentAttribute.values()) {
			attributes.put(attribute.getId(), (int) (Math.random() * Integer.MAX_VALUE));
		}
		testEquipment.setAttributes(Equipment.CURRENT_VERSION, attributes);
		
		assertEquals(EQUIPMENT_ID2, testEquipment.getId());
		assertEquals(LEVEL2, testEquipment.getLevel());
		for(EquipmentQuality quality:EquipmentQuality.values()) {
			testEquipment.setQuality(quality);
			assertEquals(quality, testEquipment.getQuality());
		}
		
		Map<Integer,Integer> checkAttributes = testEquipment.getAttributes(true);
		for(EquipmentAttribute attribute: EquipmentAttribute.values()) {
			assertEquals(attributes.get(attribute), checkAttributes.get(attribute));
		}
		
		assertEquals(testBaseAttribute, testEquipment.getBaseAttribute());
		assertEquals(testBaseAttributeValue, testEquipment.getBaseAttributeValue());
		assertEquals(testDefenceAttribute, testEquipment.getDefenceAttribute());
		assertEquals(testDefenceAttributeValue, testEquipment.getDefenceAttributeValue());
		assertEquals(testAncientAttribute, testEquipment.getAncientAttribute());
		assertEquals(testAncientAttributeValue, testEquipment.getAncientAttributeValue());
		
		restrictMockAccess();
	}
	
	@Test
	public void testCreateRandomEquipment() {
		// Gets uncursed random equipment
		Equipment equipment = Equipment.createRandom(DungeonType.DUNGEON, LEVEL, false, false, 
				EquipmentLocation.DUNGEON, DUNGEON_ID);
		assertNotNull(equipment);
		assertEquals(LEVEL, equipment.getLevel());
		
		// Change the id before checking the create
		equipment.setId(-1);
		verifyNoMoreInteractions(equipmentServiceMock);
		
		Map<Integer, Integer> testAttributes = equipment.getAttributes(true);
		for(Integer attributeId:testAttributes.keySet()) {
			assertTrue("Attribute Id "+attributeId+ " was cursed: "+testAttributes.get(attributeId), 
					testAttributes.get(attributeId) > 0);
			assertEquals("Attribute Id "+attributeId+ " not equals", 
					testAttributes.get(attributeId).intValue(), 
					equipment.getAttributeValue(EquipmentAttribute.fromId(attributeId)));
		}
		EquipmentAttribute baseAttribute = equipment.getBaseAttribute();
		int baseAttributeValue = equipment.getBaseAttributeValue();
		assertTrue("Base Attribute Id "+baseAttribute.getId()+ " was cursed: "+testBaseAttribute, 
				baseAttributeValue > 0);
		//assertEquals("Base Attribute Id "+baseAttribute.getId()+ " not equals", 
		//		baseAttributeValue, 
		//		testBaseAttributeValue);
		EquipmentAttribute defenceAttribute = equipment.getBaseAttribute();
		int defenceAttributeValue = equipment.getDefenceAttributeValue();
		assertTrue("Defence Attribute Id "+defenceAttribute.getId()+ " was cursed: "+testDefenceAttribute, 
				defenceAttributeValue > 0);
		//assertEquals("Defence Attribute Id "+defenceAttribute.getId()+ " not equals", 
		//		defenceAttributeValue, 
		//		testDefenceAttributeValue);
		if(equipment.getQuality().getId() >= EquipmentQuality.ARTIFACT.getId()) {
			EquipmentAttribute ancientAttribute = equipment.getAncientAttribute();
			int ancientAttributeValue = equipment.getAncientAttributeValue();
			assertTrue("Ancient Attribute Id "+ancientAttribute.getId()+ " was cursed: "+testAncientAttribute, 
					ancientAttributeValue > 0);
			//assertEquals("Ancient Attribute Id "+ancientAttribute.getId()+ " not equals", 
			//		ancientAttributeValue, 
			//		testAncientAttributeValue);
		}
		
		restrictMockAccess();
	}
	
	@Test
	public void testGetRequiredLevel() {
		testEquipment.setLevel(LEVEL2);
		attributes.put(EquipmentAttribute.REDUCE_LEVEL.getId(), LEVEL);
		testEquipment.setAttributes(Equipment.CURRENT_VERSION, attributes);
		
		assertEquals("Level not reduced from "+ LEVEL2 + " by " + LEVEL, 
					LEVEL2 - LEVEL, 
					testEquipment.getRequiredLevel());
		restrictMockAccess();
	}
}
