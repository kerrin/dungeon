package org.kerrin.dungeon.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.SuperTest;
import org.kerrin.dungeon.enums.Buff;
import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.CommonConstants;
import org.kerrin.dungeon.enums.DamageType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.enums.Spell;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharacterTest extends SuperTest {

	@Test
	public void testConstructor() {
		assertEquals(CHAR_ID, testCharacter.getId());
		assertEquals(ACCOUNT_ID, testCharacter.getAccount().getId());
		assertEquals(CHAR_CLASS, testCharacter.getCharClass());
		assertEquals(CHAR_NAME, testCharacter.getName());
		assertEquals(CHAR_LEVEL, testCharacter.getLevel());
		assertEquals(CHAR_XP, testCharacter.getXp());
		assertEquals(false, testCharacter.isCurrentlyDead());
		
		Character testChar2 = testCharacter;
		assertTrue(testCharacter.equals(testChar2));
		restrictMockAccess();
	}

	@Test
	public void testSetMethods() {
		testCharacter.setId(CHAR_ID2);
		testCharacter.setAccount(testAccount2);
		testCharacter.setName(CHAR_NAME2);
		testCharacter.setLevel(LEVEL2);
		testCharacter.setXp(XP2);
		testCharacter.setDiedTime(new Date(1));
		testCharacter.setDiedTo("Test Death");
		
		assertEquals(CHAR_ID2, testCharacter.getId());
		assertEquals(ACCOUNT_ID2, testCharacter.getAccount().getId());
		assertEquals(CHAR_NAME2, testCharacter.getName());
		assertEquals(LEVEL2, testCharacter.getLevel());
		assertEquals(XP2, testCharacter.getXp());
		assertEquals(true, testCharacter.isCurrentlyDead());
		testCharacter.setAlive();
		assertEquals(false, testCharacter.isCurrentlyDead());
		
		for(CharClass charClass:CharClass.values()) {
			testCharacter.setCharClass(charClass);
			assertEquals("Char Class failed on class "+charClass.getNiceName(), charClass, testCharacter.getCharClass());
		}
		restrictMockAccess();
	}
	
	@Test
	public void testAttackMonster() {
		int level = (int)(Math.random()*CommonConstants.MAX_LEVEL)+1;
		int partySize = (int)(Math.random()*10)+1;
		MonsterInstance monster = new MonsterInstance(Monster.getRandom(), MonsterType.getRandomType(), level, partySize);
		testCharacter.currentEquipment = testCharacterEquipment;
		AttackResult attackResult = testCharacter.attack(testDungeon, monster, testDungeonEvents);
		
		Character actualCharacter = attackResult.getCharacter();
		assertEquals(testCharacter.getAccount(), actualCharacter.getAccount());
		assertTrue(testCharacter.getAttackSpeed() <= actualCharacter.getAttackSpeed());
		assertEquals(testCharacter.getCharClass(), actualCharacter.getCharClass());
		assertEquals(testCharacter.getCurrentEquipmentWhileInDungeon(), actualCharacter.getCurrentEquipmentWhileInDungeon());
		assertEquals(testCharacter.getDungeon(), actualCharacter.getDungeon());
		assertEquals(testCharacter.getHealth(), actualCharacter.getHealth());
		assertEquals(testCharacter.getId(), actualCharacter.getId());
		assertEquals(testCharacter.getLevel(), actualCharacter.getLevel());
		assertEquals(testCharacter.getName(), actualCharacter.getName());
		assertEquals(testCharacter.getXp(), actualCharacter.getXp());
		MonsterInstance actualMonster = attackResult.getMonster();
		assertTrue(monster.attackSpeed >= actualMonster.attackSpeed);
		assertTrue(monster.buffs.size() <= actualMonster.buffs.size());
		assertTrue(monster.spellCoolDowns.size() <= actualMonster.spellCoolDowns.size());
		assertEquals(monster.level, actualMonster.level);
		assertEquals(monster.mana, actualMonster.mana);
		assertEquals(monster.maxHealth, actualMonster.maxHealth);
		assertEquals(monster.monster, actualMonster.monster);
		assertTrue(monster.pendingDamage.length <= actualMonster.pendingDamage.length);
		assertTrue(monster.remainingHealth >= actualMonster.remainingHealth);
		assertTrue(monster.stunLeft <= actualMonster.stunLeft);
		
		assertTrue(attackResult.getCharacterHeal()==null || attackResult.getCharacterHeal() >= 0);
		assertTrue(attackResult.getCharacterSpeed() == null || attackResult.getCharacterSpeed() >= 0);
		//assertTrue(attackResult.getCharacterSplashDamage());
		assertNull(attackResult.getMonsterHeal());
		assertTrue(attackResult.getMonsterSpeed() == null || attackResult.getMonsterSpeed() <= 0);
		//assertTrue(attackResult.getMonsterSplashDamage());
		//assertTrue(attackResult.getSummonedMonster());

		restrictMockAccess();
	}

	@Test
	public void testCalculateDamage() {
		int baseDamage = (int)(Math.random()*CommonConstants.MAX_LEVEL);
		int damage = baseDamage*100;
		int damageTypeId = (int)(Math.random()*DamageType.values().length-1)+1;
		DamageType damageType = DamageType.fromId(damageTypeId);
		List<EquipmentAttribute> effectTypes = new ArrayList<EquipmentAttribute>();
		int level = testCharacter.getLevel();
		Equipment newEquipment = createRandomEquipment(EquipmentType.AMULET, level);
		
		// Test damage attribute (No effect)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.AMULET, 
				new int[]{
						damageType.getEquipmentAttribute().getId(),
						}, 
				13 /* 13 slots */
		);
		int calculatedDamage = testCharacter.calculateDamage(damage, damageType, effectTypes, level);
		assertEquals(damage, calculatedDamage);
		
		// Test resist attribute (reduce by 1% = baseDamage)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.AMULET, 
				new int[]{
						damageType.getEquipmentAttribute().getResistAttribute().getId(),
						}, 
				13 /* 13 slots */);
		calculatedDamage = testCharacter.calculateDamage(damage, damageType, effectTypes, level);
		assertEquals(damage - baseDamage, calculatedDamage);
		
		// Test main attribute (reduce by 1% = baseDamage)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.AMULET, 
				new int[]{
						}, 
				13 /* 13 slots */);
		// Make the base attribute correct for class
		newEquipment.setBaseAttribute(testCharacter.getCharClass().getMainAttribute());
		newEquipment.setBaseAttributeValue(13);
		testCharacterEquipment.setCharacterSlot(CharSlot.AMULET, newEquipment);
		// Run test
		calculatedDamage = testCharacter.calculateDamage(damage, damageType, effectTypes, level);
		assertEquals(damage - baseDamage, calculatedDamage);
		
		// Test damage AND main resist attribute (reduce by 2% = baseDamage X 2)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.AMULET, 
				new int[]{
						damageType.getEquipmentAttribute().getResistAttribute().getId(),
						}, 
				13 /* 13 slots */);
		// Make the base attribute correct for class
		newEquipment.setBaseAttribute(testCharacter.getCharClass().getMainAttribute());
		newEquipment.setBaseAttributeValue(13);		
		testCharacterEquipment.setCharacterSlot(CharSlot.AMULET, newEquipment);
		// Run test
		calculatedDamage = testCharacter.calculateDamage(damage, damageType, effectTypes, level);
		assertEquals(damage - (baseDamage*2), calculatedDamage);
		
		restrictMockAccess();
	}

	@Test
	public void testClassDamageReductionValue() {
		int level = testCharacter.getLevel();
		int damageTypeId = (int)(Math.random()*DamageType.values().length-1)+1;
		DamageType damageType = DamageType.fromId(damageTypeId);
		List<EquipmentAttribute> effectTypes = new ArrayList<EquipmentAttribute>();
		Equipment newEquipment = createRandomEquipment(EquipmentType.AMULET, level);
		
		// Test damage attribute (No effect)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.AMULET, 
				new int[]{}, 
				13 /* 13 slots */);
		int reductionValue = testCharacter.classDamageReductionValue(damageType, effectTypes);
		assertEquals(0, reductionValue);		
		
		// Test resist attribute (reduce by 1% = baseDamage)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.AMULET, 
				new int[]{
						damageType.getEquipmentAttribute().getResistAttribute().getId(),
						}, 
				13 /* 13 slots */);
		reductionValue = testCharacter.classDamageReductionValue(damageType, effectTypes);
		assertEquals(level, reductionValue);
		
		// Test main attribute (reduce by 1% = baseDamage)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.AMULET, 
				new int[]{
						testCharacter.getCharClass().getMainAttribute().getId(),
						}, 
				13 /* 13 slots */);
		reductionValue = testCharacter.classDamageReductionValue(damageType, effectTypes);
		assertEquals(level, reductionValue);
		
		// Test damage AND main resist attribute (reduce by 2% = baseDamage X 2)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.AMULET, 
				new int[]{
						damageType.getEquipmentAttribute().getResistAttribute().getId(),
						}, 
				13 /* 13 slots */);
		// Make the base attribute correct for class
		newEquipment.setBaseAttribute(testCharacter.getCharClass().getMainAttribute());
		newEquipment.setBaseAttributeValue(13);
		testCharacterEquipment.setCharacterSlot(CharSlot.AMULET, newEquipment);
		// Run test
		reductionValue = testCharacter.classDamageReductionValue(damageType, effectTypes);
		assertEquals(level*2, reductionValue);
		
		restrictMockAccess();
	}
	
	@Test
	public void testCopy() {
		testCharacter.copy(testCharacter2 );
		deepCharacterComparision(testCharacter, testCharacter2); 
	
		restrictMockAccess();
	}
	
	@Test
	public void testGetRandomSpell() {
		for(int level=1; level <= 100; level++) {
			Spell spell = testCharacter.getRandomSpell(level);
			assertTrue(spell.getMinimumLevel() <= level);
		}

		restrictMockAccess();
	}
	
	@Test
	public void testHasBuff() {
		for(Buff buff:Buff.values()) {
			assertFalse(testCharacter.hasBuff(buff));
			testCharacter.buffs.put(buff, 1);
			assertTrue(testCharacter.hasBuff(buff));
		}

		restrictMockAccess();
	}
	
	@Test
	public void testHeal() {
		int heal = 123;
		testCharacter.maxHealth = 1000;
		testCharacter.remainingHealth = testCharacter.maxHealth;
		testCharacter.heal(heal );
		assertEquals(testCharacter.maxHealth, testCharacter.remainingHealth);
		
		testCharacter.remainingHealth = testCharacter.maxHealth - 1;
		testCharacter.heal(heal );
		assertEquals(testCharacter.maxHealth, testCharacter.remainingHealth);
		
		testCharacter.remainingHealth = testCharacter.maxHealth - heal;
		testCharacter.heal(heal );
		assertEquals(testCharacter.maxHealth, testCharacter.remainingHealth);
		
		testCharacter.remainingHealth = testCharacter.maxHealth - (heal * 2);
		testCharacter.heal(heal );
		assertEquals(testCharacter.maxHealth - heal, testCharacter.remainingHealth);
		
		testCharacter.remainingHealth = 0;
		testCharacter.heal(heal );
		assertEquals(heal, testCharacter.remainingHealth);
				
		restrictMockAccess();
	}
	
	@Test
	public void testModifySpeed() {
		int speedChange = 123;
		testCharacter.attackSpeed = 1;
		testCharacter.modifySpeed(speedChange, testDungeon, testDungeonEvents );
		assertEquals(speedChange + 1, testCharacter.attackSpeed);
		
		testCharacter.attackSpeed = 0;
		testCharacter.modifySpeed(speedChange, testDungeon, testDungeonEvents );
		assertEquals(speedChange, testCharacter.attackSpeed);

		testCharacter.attackSpeed = speedChange;
		testCharacter.modifySpeed(speedChange, testDungeon, testDungeonEvents );
		assertEquals(speedChange*2, testCharacter.attackSpeed);

		speedChange = -123;
		testCharacter.attackSpeed = 1;
		testCharacter.modifySpeed(speedChange, testDungeon, testDungeonEvents );
		assertEquals(speedChange + 1, testCharacter.attackSpeed);
		
		testCharacter.attackSpeed = 0;
		testCharacter.modifySpeed(speedChange, testDungeon, testDungeonEvents );
		assertEquals(speedChange, testCharacter.attackSpeed);

		testCharacter.attackSpeed = speedChange;
		testCharacter.modifySpeed(speedChange, testDungeon, testDungeonEvents );
		assertEquals(speedChange + speedChange, testCharacter.attackSpeed);

		restrictMockAccess();
	}
	
	@Test
	public void testProcessTick() {
		testCharacter.currentEquipment = testCharacterEquipment;
		testCharacter.setLevel(4);
		testCharacter.stunLeft = 100;
		testCharacter.attackSpeed = 90;
		testCharacter.spellCoolDowns = new HashMap<Spell, Integer>();
		testCharacter.spellCoolDowns.put(Spell.Buff, 80);
		testCharacter.spellCoolDowns.put(Spell.VileAcid, 70);
		testCharacter.spellCoolDowns.put(Spell.Blind, 1);
		testCharacter.buffs = new HashMap<Buff, Integer>();
		testCharacter.buffs.put(Buff.ALL_RESIST, 1);
		testCharacter.buffs.put(Buff.SPEED, 60);
		testCharacter.maxHealth = 100;
		testCharacter.remainingHealth = 100;
		testCharacter.pendingDamage = new int[] {5,4,6,3,7};
		testCharacter.processTick(testDungeon, testDungeonEvents);
		assertEquals(99, testCharacter.stunLeft);
		assertEquals(89, testCharacter.attackSpeed);
		assertEquals(79, testCharacter.spellCoolDowns.get(Spell.Buff).intValue());
		assertEquals(69, testCharacter.spellCoolDowns.get(Spell.VileAcid).intValue());
		assertFalse(testCharacter.spellCoolDowns.containsKey(Spell.Blind));
		assertEquals(59, testCharacter.buffs.get(Buff.SPEED).intValue());
		assertFalse(testCharacter.buffs.containsKey(Buff.ALL_RESIST));
		assertEquals(95, testCharacter.remainingHealth);
		assertEquals(4, testCharacter.pendingDamage[0]);
		assertEquals(6, testCharacter.pendingDamage[1]);
		assertEquals(3, testCharacter.pendingDamage[2]);
		assertEquals(7, testCharacter.pendingDamage[3]);
		assertEquals(0, testCharacter.pendingDamage[4]);
		
		// Round 2
		testCharacter.attackSpeed = -90;
		testCharacter.spellCoolDowns.put(Spell.Poem, 2);
		testCharacter.buffs.put(Buff.ALL_RESIST, 2);
		testCharacter.processTick(testDungeon, testDungeonEvents);
		assertEquals(98, testCharacter.stunLeft);
		assertEquals(-89, testCharacter.attackSpeed);
		assertEquals(78, testCharacter.spellCoolDowns.get(Spell.Buff).intValue());
		assertEquals(68, testCharacter.spellCoolDowns.get(Spell.VileAcid).intValue());
		assertEquals(1, testCharacter.spellCoolDowns.get(Spell.Poem).intValue());
		assertEquals(58, testCharacter.buffs.get(Buff.SPEED).intValue());
		assertEquals(1, testCharacter.buffs.get(Buff.ALL_RESIST).intValue());
		assertEquals(91, testCharacter.remainingHealth);
		assertEquals(6, testCharacter.pendingDamage[0]);
		assertEquals(3, testCharacter.pendingDamage[1]);
		assertEquals(7, testCharacter.pendingDamage[2]);
		assertEquals(0, testCharacter.pendingDamage[3]);
		assertEquals(0, testCharacter.pendingDamage[4]);
		
		// Regen from buff
		testCharacter.pendingDamage = new int[] {0,0,0,0,0};
		testCharacter.buffs.put(Buff.HEALTH_REGEN, 50);
		testCharacter.remainingHealth = 10;
		testCharacter.processTick(testDungeon, testDungeonEvents);
		// 10 + (level * 10)
		assertEquals(50, testCharacter.remainingHealth);
		
		// Regen from buff
		testCharacter.pendingDamage = new int[] {0,0,0,0,0};
		testCharacter.buffs.put(Buff.HEALTH_REGEN, 1);
		testCharacter.remainingHealth = 10;
		testCharacter.processTick(testDungeon, testDungeonEvents);
		// 10 + (level * 10)
		assertEquals(50, testCharacter.remainingHealth);
		assertFalse(testCharacter.buffs.containsKey(Buff.HEALTH_REGEN));
		
		// Regen from equipment
		int level = testCharacter.getLevel();
		int damageTypeId = (int)(Math.random()*DamageType.values().length-1)+1;
		DamageType damageType = DamageType.fromId(damageTypeId);
		Equipment newEquipment = createRandomEquipment(EquipmentType.BROACH, level);
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.BROACH, 
				new int[]{
						EquipmentAttribute.HEALTH_REGEN.getId(),
						}, 
				1 /* 13 slots */);
		testCharacter.remainingHealth = 10;
		testCharacter.processTick(testDungeon, testDungeonEvents);
		// 10 + level
		assertEquals(14, testCharacter.remainingHealth);
		
		restrictMockAccess();
	}
	
	@Test
	public void testStartDungeon() {
		testCharacter.setLevel(23);
		CharClass charClass = CharClass.MELEE;
		testCharacter.setCharClass(charClass);
		int damageTypeId = (int)(Math.random()*DamageType.values().length-1)+1;
		DamageType damageType = DamageType.fromId(damageTypeId);
		Equipment newEquipment = createRandomEquipment(EquipmentType.BRACERS, testCharacter.getLevel());
		// Set the base and defence attributes to not affect the result
		newEquipment.setBaseAttribute(EquipmentAttribute.INTELLIGENCE);
		newEquipment.setBaseAttributeValue(testCharacter.getLevel() * 13);
		newEquipment.setDefenceAttribute(EquipmentAttribute.GUILE);
		newEquipment.setDefenceAttributeValue(testCharacter.getLevel() * 13);
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.BRACERS, 
				new int[]{}, 
				testCharacter.getLevel() * 13 /* 13 slots */
				);
		testCharacter.maxHealth = 1;
		testCharacter.remainingHealth = 2;
		testCharacter.mana = 3;
		testCharacter.stunLeft = 4;
		testCharacter.currentEquipment = null;
		testCharacter.startDungeon(testDungeon, testCharacterEquipment);
		
		assertEquals((testCharacter.getLevel() * charClass.getBaseHealth()), 
						testCharacter.maxHealth);
		assertEquals(testCharacter.maxHealth, testCharacter.remainingHealth);
		assertEquals(charClass.getBaseMana(), testCharacter.mana);
		assertEquals(0, testCharacter.stunLeft);
		assertEquals(testCharacterEquipment, testCharacter.currentEquipment);

		restrictMockAccess();
	}
	
	@Test
	public void testStun() {
		int stunTime = 123;
		int level = 12;
		
		Map<CharSlot, Equipment> characterSlots = new HashMap<CharSlot, Equipment>();
		// Test no resist
		testCharacter.currentEquipment = new CharacterEquipment(testCharacter, characterSlots);
		testCharacter.stunLeft = 1;
		testCharacter.stun(stunTime, level, testDungeon, testDungeonEvents);
		assertEquals(stunTime, testCharacter.stunLeft);
		
		// Test 100% resist
		int damageTypeId = (int)(Math.random()*DamageType.values().length-1)+1;
		DamageType damageType = DamageType.fromId(damageTypeId);
		Equipment newEquipment = createRandomEquipment(EquipmentType.CHEST, level);
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.BROACH, 
				new int[]{
						EquipmentAttribute.STUN_RESIST.getId()
						},
				100 * 13 /* 13 slots */
				);
		testCharacter.stunLeft = 2;
		testCharacter.stun(stunTime, level, testDungeon, testDungeonEvents);
		assertEquals(2, testCharacter.stunLeft);

		// Random chance
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.BROACH, 
				new int[]{
						EquipmentAttribute.STUN_RESIST.getId()
						},
				50 * 13 /* 13 slots */
				);
		testCharacter.stunLeft = 3;
		testCharacter.stun(stunTime, level, testDungeon, testDungeonEvents);
		assertTrue(testCharacter.stunLeft == stunTime || testCharacter.stunLeft == 3);
		
		restrictMockAccess();
	}
	
	@Test
	public void testTakeDamage() {
		int baseDamage = (int)(Math.random()*CommonConstants.MAX_LEVEL);
		int damage = baseDamage*100;
		int startHealth = 100 * damage;
		int level = 14;
		int expectedBaseDamage = baseDamage * level;
		int expectedDamage = damage * level;
		DamageType damageType = DamageType.FIRE;		
		List<EquipmentAttribute> effectTypes = new ArrayList<EquipmentAttribute>();
		Equipment newEquipment = createRandomEquipment(EquipmentType.FEET, level);
		
		// Test damage attribute (No effect)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.FEET, 
				new int[]{
						damageType.getEquipmentAttribute().getId(),
						}, 
				13 /* 13 slots */
		);
		testCharacter.remainingHealth = startHealth;
		AttackResult attackResults = new AttackResult(testCharacter);
		List<DungeonEvent> dungeonEvents = testCharacter.takeDamage(
				expectedDamage, damageType, effectTypes, level, testDungeon, attackResults);
		assertEquals(startHealth - expectedDamage, testCharacter.remainingHealth);
		
		// Test resist attribute (reduce by 1% = baseDamage)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.FEET, 
				new int[]{
						damageType.getEquipmentAttribute().getResistAttribute().getId()
				}, 
				13 /* 13 slots */);
		testCharacter.remainingHealth = startHealth;
		dungeonEvents = testCharacter.takeDamage(expectedDamage, damageType, effectTypes, level, testDungeon, attackResults);
		assertEquals(startHealth - (expectedDamage - expectedBaseDamage), testCharacter.remainingHealth);
		
		// Test main attribute (reduce by 1% = baseDamage)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.FEET, 
				new int[]{},
				13 /* 13 slots */);
		// Make the base attribute correct for class
		newEquipment.setBaseAttribute(testCharacter.getCharClass().getMainAttribute());
		newEquipment.setBaseAttributeValue(13);
		testCharacterEquipment.setCharacterSlot(CharSlot.FEET, newEquipment);
		// Run test
		testCharacter.remainingHealth = startHealth;
		dungeonEvents = testCharacter.takeDamage(expectedDamage, damageType, effectTypes, level, testDungeon, attackResults);
		assertEquals(startHealth - (expectedDamage - expectedBaseDamage), testCharacter.remainingHealth);
		
		// Test damage AND main resist attribute (reduce by 2% = baseDamage X 2)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.FEET, 
				new int[]{
						damageType.getEquipmentAttribute().getResistAttribute().getId()
						}, 
				13 /* 13 slots */);
		// Make the base attribute correct for class
		newEquipment.setBaseAttribute(testCharacter.getCharClass().getMainAttribute());
		newEquipment.setBaseAttributeValue(13);
		testCharacterEquipment.setCharacterSlot(CharSlot.FEET, newEquipment);
		// Run test
		testCharacter.remainingHealth = startHealth;
		dungeonEvents = testCharacter.takeDamage(expectedDamage, damageType, effectTypes, level, testDungeon, attackResults);
		assertEquals(startHealth - (expectedDamage - (expectedBaseDamage*2)), testCharacter.remainingHealth);
		
		restrictMockAccess();
	}
	
	@Test
	public void testTakeDamageOverTime() {
		int baseDamage = (int)(Math.random()*CommonConstants.MAX_LEVEL);
		int damage = baseDamage*100;
		int level = 14;
		int expectedBaseDamage = baseDamage * level;
		int expectedDamage = damage * level;
		DamageType damageType = DamageType.HOLY;		
		List<EquipmentAttribute> effectTypes = new ArrayList<EquipmentAttribute>();
		Equipment newEquipment = createRandomEquipment(EquipmentType.FEET, level);
		
		// Test damage attribute (No effect)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.FEET, 
				new int[]{
						damageType.getEquipmentAttribute().getId(),
						}, 
				13 /* 13 slots */
		);
		testCharacter.pendingDamage = new int[]{0,0,0,0,0};
		DungeonEvent dungeonEvent = testCharacter.takeDamageOverTime(expectedDamage, damageType, effectTypes, level, testDungeon);
		int expectedDamageTicks = expectedDamage / CommonConstants.DAMAGE_OVER_TIME_TICKS;
		for(int i=0; i < CommonConstants.DAMAGE_OVER_TIME_TICKS; i++) {
			assertEquals(expectedDamageTicks, testCharacter.pendingDamage[i]);
		}
		
		// Test resist attribute (reduce by 1% = baseDamage)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.FEET, 
				new int[]{
						damageType.getEquipmentAttribute().getResistAttribute().getId()}, 
				13 /* 13 slots */);
		testCharacter.pendingDamage = new int[]{0,0,0,0,0};
		dungeonEvent = testCharacter.takeDamageOverTime(expectedDamage, damageType, effectTypes, level, testDungeon);
		expectedDamageTicks = (expectedDamage - expectedBaseDamage) / CommonConstants.DAMAGE_OVER_TIME_TICKS;
		for(int i=0; i < CommonConstants.DAMAGE_OVER_TIME_TICKS; i++) {
			assertEquals(expectedDamageTicks, testCharacter.pendingDamage[i]);
		}
		
		// Test main attribute (reduce by 1% = baseDamage)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.FEET, 
				new int[]{testCharacter.getCharClass().getMainAttribute().getId()}, 
				13 /* 13 slots */);
		testCharacter.pendingDamage = new int[]{0,0,0,0,0};
		dungeonEvent = testCharacter.takeDamageOverTime(expectedDamage, damageType, effectTypes, level, testDungeon);
		expectedDamageTicks = (expectedDamage - expectedBaseDamage) / CommonConstants.DAMAGE_OVER_TIME_TICKS;
		for(int i=0; i < CommonConstants.DAMAGE_OVER_TIME_TICKS; i++) {
			assertEquals(expectedDamageTicks, testCharacter.pendingDamage[i]);
		}
		
		// Test damage AND main resist attribute (reduce by 2% = baseDamage X 2)
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.FEET, 
				new int[]{
						testCharacter.getCharClass().getMainAttribute().getId(),
						damageType.getEquipmentAttribute().getResistAttribute().getId()
						}, 
				13 /* 13 slots */);
		testCharacter.pendingDamage = new int[]{0,0,0,0,0};
		dungeonEvent = testCharacter.takeDamageOverTime(expectedDamage, damageType, effectTypes, level, testDungeon);
		expectedDamageTicks = (expectedDamage - (expectedBaseDamage*2)) / CommonConstants.DAMAGE_OVER_TIME_TICKS;
		for(int i=0; i < CommonConstants.DAMAGE_OVER_TIME_TICKS; i++) {
			assertEquals(expectedDamageTicks, testCharacter.pendingDamage[i]);
		}
		
		// Test with damage already pending
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.FEET, 
				new int[]{
						testCharacter.getCharClass().getMainAttribute().getId(),
						damageType.getEquipmentAttribute().getResistAttribute().getId()
						}, 
				13 /* 13 slots */);
		testCharacter.pendingDamage = new int[]{10,20,30,40,50};
		dungeonEvent = testCharacter.takeDamageOverTime(expectedDamage, damageType, effectTypes, level, testDungeon);
		expectedDamageTicks = (expectedDamage - (expectedBaseDamage*2)) / CommonConstants.DAMAGE_OVER_TIME_TICKS;
		for(int i=0; i < CommonConstants.DAMAGE_OVER_TIME_TICKS; i++) {
			assertEquals(expectedDamageTicks+((i+1)*10), testCharacter.pendingDamage[i]);
		}
		
		restrictMockAccess();
	}
	
	@Test
	public void testProcessRegen() {
		testCharacter.setLevel(35);
		testCharacter.currentEquipment = testCharacterEquipment;
		testCharacter.setCharClass(CharClass.MAGIC);
		testCharacter.buffs = new HashMap<Buff, Integer>();
		testCharacter.buffs.put(Buff.ALL_RESIST, 1);
		testCharacter.buffs.put(Buff.SPEED, 60);
		testCharacter.maxHealth = 1000;
		testCharacter.remainingHealth = 1000;
		testCharacter.mana = 20;
		testCharacter.processRegen(testDungeon, testDungeonEvents);
		assertEquals(1000, testCharacter.remainingHealth);
		/*
		assertEquals(20+Character.BASE_MANA_REGEN, testCharacter.mana);
		
		testCharacter.mana = 100;
		testCharacter.processRegen(testDungeon, testDungeonEvents);
		assertEquals(100, testCharacter.mana);
				
		// Regen from buff
		testCharacter.buffs.put(Buff.HEALTH_REGEN, 50);
		testCharacter.buffs.put(Buff.MANA_REGEN, 20);
		testCharacter.remainingHealth = 11;
		testCharacter.mana = 21;
		testCharacter.processRegen(testDungeon, testDungeonEvents);
		assertEquals(11 + (testCharacter.getLevel() * Character.HOT_BUFF_REGEN), testCharacter.remainingHealth);
		assertEquals(21+Character.BASE_MANA_REGEN+Character.MANA_BUFF_REGEN, testCharacter.mana);
		
		// Regen from buff
		testCharacter.buffs.put(Buff.HEALTH_REGEN, 1);
		testCharacter.buffs.put(Buff.MANA_REGEN, 1);
		testCharacter.remainingHealth = 12;
		testCharacter.mana = 22;
		testCharacter.processRegen(testDungeon, testDungeonEvents);
		assertEquals(12 + (testCharacter.getLevel() * Character.HOT_BUFF_REGEN), testCharacter.remainingHealth);
		assertEquals(22+Character.BASE_MANA_REGEN+Character.MANA_BUFF_REGEN, testCharacter.mana);
		
		// Regen from equipment
		int level = testCharacter.getLevel();
		testCharacter.buffs = new HashMap<Buff, Integer>();
		int damageTypeId = (int)(Math.random()*DamageType.values().length-1)+1;
		DamageType damageType = DamageType.fromId(damageTypeId);
		Equipment newEquipment = createRandomEquipment(EquipmentType.BROACH, level);
		testCharacter.currentEquipment = generateEquipment(damageType, newEquipment, CharSlot.BROACH, 
				new int[]{
						EquipmentAttribute.HEALTH_REGEN.getId(),
						EquipmentAttribute.MANA_REGEN.getId(),
					}, 
				13
				);
		testCharacter.remainingHealth = 13;
		testCharacter.mana = 23;
		testCharacter.processRegen(testDungeon, testDungeonEvents);
		assertEquals(13 + (level*13), testCharacter.remainingHealth);
		assertEquals(23 + Character.BASE_MANA_REGEN+13, testCharacter.mana);
		*/
		restrictMockAccess();
	}

	@Test
	public void testEquals() {
		Character testChar2 = new Character();
		testChar2.setId(CHAR_ID);
		assertTrue(testCharacter.equals(testChar2));
		testChar2.setId(CHAR_ID2);
		assertFalse(testCharacter.equals(testChar2));
		restrictMockAccess();
	}
	
	// Helper methods
	private CharacterEquipment generateEquipment(DamageType damageType, Equipment newEquipment, CharSlot slot, int[] attributeIds) {
		return generateEquipment(damageType, newEquipment, slot, attributeIds, 1);
	} 
	private CharacterEquipment generateEquipment(DamageType damageType, Equipment newEquipment, CharSlot slot, int[] attributeIds, int value) {
		attributes = new HashMap<Integer, Integer>();
		for(int attributeId:attributeIds) {
			attributes.put(attributeId, value);
		}
		newEquipment.setQuality(EquipmentQuality.fromId(2+attributes.size()));
		newEquipment.setAttributes((short)0/*Version 0 so it translates the attributes*/, attributes);
		testCharacterSlots = new HashMap<CharSlot, Equipment>();
		testCharacterEquipment = new CharacterEquipment(testCharacter, testCharacterSlots);
		testCharacterEquipment.setCharacterSlot(slot, newEquipment);
		return testCharacterEquipment;
	}
	
	private void deepCharacterComparision(Character expectCharacter, Character actualCharacter) {
		assertEquals(expectCharacter.getAccount(), actualCharacter.getAccount());
		assertEquals(expectCharacter.getAttackSpeed(), actualCharacter.getAttackSpeed());
		assertEquals(expectCharacter.getCharClass(), actualCharacter.getCharClass());
		assertEquals(expectCharacter.getCurrentEquipmentWhileInDungeon(), actualCharacter.getCurrentEquipmentWhileInDungeon());
		assertEquals(expectCharacter.getDungeon(), actualCharacter.getDungeon());
		assertEquals(expectCharacter.getHealth(), actualCharacter.getHealth());
		assertEquals(expectCharacter.getId(), actualCharacter.getId());
		assertEquals(expectCharacter.getLevel(), actualCharacter.getLevel());
		assertEquals(expectCharacter.getName(), actualCharacter.getName());
		assertEquals(expectCharacter.getXp(), actualCharacter.getXp());
	}
	
	private void deepMonsterComparision(MonsterInstance expectMonster, MonsterInstance actualMonster) {
		assertEquals(expectMonster.attackSpeed, actualMonster.attackSpeed);
		assertEquals(expectMonster.buffs, actualMonster.buffs);
		assertEquals(expectMonster.spellCoolDowns, actualMonster.spellCoolDowns);
		assertEquals(expectMonster.level, actualMonster.level);
		assertEquals(expectMonster.mana, actualMonster.mana);
		assertEquals(expectMonster.maxHealth, actualMonster.maxHealth);
		assertEquals(expectMonster.monster, actualMonster.monster);
		assertEquals(expectMonster.pendingDamage, actualMonster.pendingDamage);
		assertEquals(expectMonster.remainingHealth, actualMonster.remainingHealth);
		assertEquals(expectMonster.stunLeft, actualMonster.stunLeft);
	}
}
