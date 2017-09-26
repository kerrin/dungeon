package org.kerrin.dungeon.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.SuperTest;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.DifferentGameStates;
import org.kerrin.dungeon.exception.DungeonNotRunable;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DungeonTest extends SuperTest {

	private static final int LEVEL_100 = 100;

	@Test
	public void testConstructor() {
		assertEquals(DUNGEON_ID, testDungeon.getId());
		assertEquals(DUNGEON_TYPE, testDungeon.getType());
		assertEquals(LEVEL, testDungeon.getLevel());
		assertEquals(XP, testDungeon.getXpReward());
		assertTrue(testDungeon.getItemRewards().isEmpty());
		assertTrue(testDungeon.getMonsters().isEmpty());
		assertEquals(PARTY_SIZE, testDungeon.getPartySize());
	}

	@Test
	public void testSetMethods() {
		Map<Equipment, Boolean> itemRewards = new HashMap<Equipment, Boolean>();
		Map<Monster, MonsterType> monsters = new HashMap<Monster, MonsterType>();
		testDungeon.setId(DUNGEON_ID2);
		testDungeon.setLevel(LEVEL2);
		testDungeon.setXpReward(XP2);
		testDungeon.setPartySize(PARTY_SIZE2);
		for(int i=0; i < 10; i++) {
			HashMap<Integer, Integer> attributes = new HashMap<Integer, Integer>();
			Equipment equipment = new Equipment(i+1, 
					EquipmentType.FEET, EquipmentQuality.BROKEN, i+2, false, false, 
					null/*baseAttribute*/, 0/*baseAttributeValue*/, 
					null/*defenceAttribute*/, 0/*defenceAttributeValue*/, 
					attributes, 
					null/*ancientAttribute*/, 0/*ancientAttributeValue*/,  
					EquipmentLocation.DUNGEON, DUNGEON_ID2);
			itemRewards.put(equipment, false);
		}
		testDungeon.setItemRewards(itemRewards);
		for(int i=0; i < 10; i++) {
			monsters.put(Monster.ACID_BALL, MonsterType.ELITE);
		}
		testDungeon.setMonsters(monsters);
		
		assertEquals(DUNGEON_ID2, testDungeon.getId());
		assertEquals(LEVEL2, testDungeon.getLevel());
		assertEquals(XP2, testDungeon.getXpReward());
		assertEquals(PARTY_SIZE2, testDungeon.getPartySize());
		
		Map<Equipment, Boolean> checkReward = testDungeon.getItemRewards();
		for(int i=0; i < 10; i++) {
			assertEquals(itemRewards.get(i), checkReward.get(i));
		}
		Map<Monster,MonsterType> checkMonsters = testDungeon.getMonsters();
		for(int i=0; i < 10; i++) {
			assertEquals(monsters.get(i), checkMonsters.get(i));
		}

		for(DungeonType dungeonType:DungeonType.values()) {
			testDungeon.setType(dungeonType);
			assertEquals("Dungeon failed on type "+dungeonType.getNiceName(), dungeonType, testDungeon.getType());
		}
	}
	
	@Test
	public void testGetDoneDate() {
		assertNull(testDungeon.getDoneDate());
		/*
		Date now = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, LEVEL);
		Date finished = cal.getTime();
		assertEquals(finished, testDungeon.getDoneDate());
		
		int level = (int)(Math.random()*100);
		testDungeon.setLevel(level);
		cal.setTime(now);
		cal.add(Calendar.MINUTE, level);
		finished = cal.getTime();
		assertEquals(finished, testDungeon.getDoneDate());
		*/
	}
	
	@Test
	public void testRunNoCharacters() {
		try {
			testDungeon.start(characterEquipmentServiceMock, new Date());
			fail("Was able to run dungeon that hasn't been started");
		} catch (DungeonNotRunable e) {
			assertEquals(e.getMessage(), "Attempt to run dungeon "+DUNGEON_ID+" that had no characters");
		}
	}
	
	@Test
	public void testRunOneTrashOneChar() throws AccountIdMismatch, DifferentGameStates, DungeonNotRunable {		
		Map<Monster, MonsterType> monsters = new HashMap<Monster, MonsterType>();
		monsters.put(Monster.ACID_BALL, MonsterType.TRASH);
		testDungeon.setMonsters(monsters);
		List<Character> characters = new ArrayList<Character>();
		characters.add(testCharacterWithEquipment);

		testDungeon.setCharacters(characters);
		List<DungeonEvent> dungeonEvents = testDungeon.start(characterEquipmentServiceMock, new Date());
		
		// TODO: Check dungeon events
		
		verify(characterEquipmentServiceMock, times(1)).findById(eq(testCharacterWithEquipment.getId()));
		
		restrictMockAccess();
		
		assertFalse(testDungeon.hasGeneratedFoundItemRewards());
	}
	
	@Test
	public void testRunOneEliteTwoChar() {		
		Map<Monster, MonsterType> monsters = new HashMap<Monster, MonsterType>();
		monsters.put(Monster.DEMON, MonsterType.ELITE);
		testDungeon.setMonsters(monsters);
		List<Character> characters = new ArrayList<Character>();
		characters.add(testCharacter);
		CharacterEquipment characterEquipment = createRandomCharacterEquipment(CHAR_ID, LEVEL2);
		when(characterEquipmentServiceMock.findById(eq(CHAR_ID))).thenReturn(characterEquipment);
		characters.add(testCharacter2);
		characterEquipment = createRandomCharacterEquipment(CHAR_ID2, LEVEL2);
		when(characterEquipmentServiceMock.findById(eq(CHAR_ID2))).thenReturn(characterEquipment);
		List<DungeonEvent> dungeonEvents;
		try {
			testDungeon.setCharacters(characters);
			dungeonEvents = testDungeon.start(characterEquipmentServiceMock, new Date());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass()+": "+e.getMessage());
		}
		
		// TODO: Check dungeon events
		
		verify(characterEquipmentServiceMock, atLeast(0)).findById(eq(CHAR_ID));
		verify(characterEquipmentServiceMock, atLeast(0)).findById(eq(CHAR_ID2));
		
		restrictMockAccess();
		
		assertFalse(testDungeon.hasGeneratedFoundItemRewards());
	}
	
	@Test
	public void testRunOneTrashOneEliteOneBossFourChars() {		
		Map<Monster, MonsterType> monsters = new HashMap<Monster, MonsterType>();
		Monster monster = Monster.getRandom();
		monsters.put(monster, MonsterType.TRASH);
		while(monsters.containsKey(monster)) {
			monster = Monster.getRandom();
		}
		monsters.put(monster, MonsterType.ELITE);
		while(monsters.containsKey(monster)) {
			monster = Monster.getRandom();
		}
		monsters.put(monster, MonsterType.BOSS);
		testDungeon.setMonsters(monsters);
		testDungeon.setLevel(LEVEL_100);
		List<Character> characters = new ArrayList<Character>();
		for(long i=0; i < 4; i++) {
			Character character = createRandomCharacter(i+1, LEVEL_100);
			characters.add(character);
			createRandomCharacterEquipment(i+1, LEVEL_100);
		}
		List<DungeonEvent> dungeonEvents;
		try {
			testDungeon.setCharacters(characters);
			dungeonEvents = testDungeon.start(characterEquipmentServiceMock, new Date());
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
			e.printStackTrace();
		}
		
		// TODO: Check dungeon events
		
		for(long i=0; i < 4; i++) {
			verify(characterEquipmentServiceMock, atLeast(0)).findById(eq(i+1));
		}
		
		restrictMockAccess();
		
		assertFalse(testDungeon.hasGeneratedFoundItemRewards());
	}
	
	/**
	 * Run a dungeon for each combination of:
	 * * Number of monsters (1 to 10)
	 * * Number characters (1 to 10)
	 * * Dungeon Type
	 */
	@Test
	public void testRunRandomDungeonsForPermitations() {
		Map<Equipment, Boolean> itemRewards = new HashMap<Equipment, Boolean>();
		Date started = new Date();
		for(int dungeonCount=0; dungeonCount < 10; dungeonCount++) {
			System.out.println("Dungeon "+(dungeonCount+1));
			Map<Monster, MonsterType> monsters = new HashMap<Monster, MonsterType>();
			for(int monsterCount=0; monsterCount < dungeonCount+1; monsterCount++) {
				Monster monster = Monster.getRandom();				
				while(monsters.containsKey(monster)) {
					monster = Monster.getRandom();
				}
				monsters.put(monster, MonsterType.getRandomType());
			}
			for(int charCount=0; charCount < 10; charCount++) {
				List<Character> characters = new ArrayList<Character>();
				for(long i=0; i < charCount+1; i++) {			
					Character character = createRandomCharacter(i+1, LEVEL_100);
					characters.add(character);
					createRandomCharacterEquipment(i+1, LEVEL_100);
					for(DungeonType dungeonType:DungeonType.values()) {
						List<DungeonEvent> dungeonEvents;
						try {
							Dungeon dungeon = new Dungeon(dungeonCount+1, dungeonType, testAccount, false, false, LEVEL_100, XP2, monsters, charCount+1, 0);
							dungeon.setItemRewards(itemRewards);
							dungeon.setCharacters(characters);
							dungeonEvents = dungeon.start(characterEquipmentServiceMock, new Date());
						} catch (Exception e) {
							e.printStackTrace();
							fail("D:"+(dungeonCount+1)+
									"M:"+monsters.size()+
									"C:"+(charCount+1)+
									"T:"+dungeonType.getNiceName()+". "+ 
									e.getClass()+": "+e.getMessage());
						}
						// TODO: Check dungeon events
					}
				}

			}
		}

		
		for(long i=0; i < 10; i++) {
			verify(characterEquipmentServiceMock, atLeast(0)).findById(eq(i+1));
		}
		
		restrictMockAccess();
		
		assertFalse(testDungeon.hasGeneratedFoundItemRewards());
		
		Date ended = new Date();
		long took = ended.getTime() - started.getTime();
		
		System.out.println("300 dungeons took "+ (took/1000)+" seconds");
	}
	
	/**
	 * Run a dungeon for each combination of:
	 * * Number of monsters (1 to 10)
	 * * Number characters (1 to 10)
	 * * Dungeon Type
	 */
	@Test
	public void testRunRandomDungeonsForLevels1To100() {
		Map<Equipment, Boolean> itemRewards = new HashMap<Equipment, Boolean>();
		Date started = new Date();
		
		for(int levelCount=0; levelCount < 100; levelCount++) {
			if(levelCount % 10 == 0) System.out.println("Level "+(levelCount+1));
			Map<Monster, MonsterType> monsters = new HashMap<Monster, MonsterType>();
			int monsterCount=(int)(Math.random()*10)+1;
			for(; monsterCount > 0; monsterCount--) {
				Monster monster = Monster.getRandom();				
				while(monsters.containsKey(monster)) {
					monster = Monster.getRandom();
				}
				monsters.put(monster, MonsterType.getRandomType());
			}
			List<Character> characters = new ArrayList<Character>();
			int charCount=(int)(Math.random()*10);
			for(; charCount >= 0; charCount--) {
				Character character = createRandomCharacter(charCount+1, levelCount+1);
				characters.add(character);
				createRandomCharacterEquipment(charCount+1, levelCount+1);
			}
			DungeonType dungeonType = DungeonType.getRandom(levelCount+1);
			List<DungeonEvent> dungeonEvents;
			try {
				Dungeon dungeon = new Dungeon(levelCount+1, dungeonType, testAccount, false, false, levelCount+1, XP2, monsters, characters.size(), 0);
				dungeon.setItemRewards(itemRewards);
				dungeon.setCharacters(characters);
				dungeonEvents = dungeon.start(characterEquipmentServiceMock, new Date());
			} catch (Exception e) {
				e.printStackTrace();
				fail("D:"+(levelCount+1)+
						"M:"+monsters.size()+
						"C:"+characters.size()+
						"T:"+dungeonType.getNiceName()+". "+ 
						e.getClass()+": "+e.getMessage());
			}
			// TODO: Check dungeon events
		}

		
		for(long i=0; i < 10; i++) {
			verify(characterEquipmentServiceMock, atLeast(0)).findById(eq(i+1));
		}
		
		restrictMockAccess();
		
		assertFalse(testDungeon.hasGeneratedFoundItemRewards());
		
		Date ended = new Date();
		long took = ended.getTime() - started.getTime();
		
		System.out.println("100 dungeon levels took "+ (took/1000)+" seconds");
	}
}
