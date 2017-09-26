package org.kerrin.dungeon.model;

import org.kerrin.dungeon.enums.Buff;
import org.kerrin.dungeon.enums.Spell;

public class AttackResult {
	private Character character;
	private MonsterInstance monster;
	private Spell castSpell = null; 
	private MonsterInstance summonedMonster;
	/** Splash Damage to all characters */
	private Damage characterSplashDamage;
	/** Splash Damage to all monsters */
	private Damage monsterSplashDamage;
	/** Amount to heal monster (monsters always self heal) */
	private Integer monsterHeal;
	/** Amount to heal character that needs it the most in the party */
	private Integer characterHeal;
	/**  */
	private Integer characterSpeed;
	/**  */
	private Integer monsterSpeed;
	
	public AttackResult(Character character, MonsterInstance monster, MonsterInstance summonedMonster) {
		super();
		this.character = character;
		this.monster = monster;
		this.summonedMonster = summonedMonster;
	}
	
	public AttackResult(Character character, MonsterInstance monster) {
		this(character, monster, null);
	}
	
	public AttackResult(Character character) {
		this(character, null);
	}
	
	public AttackResult(MonsterInstance monster) {
		this(null, monster);
	}

	public Character getCharacter() {
		return character;
	}
	
	public void setCharacter(Character character) {
		this.character = character;
	}
	
	public MonsterInstance getMonster() {
		return monster;
	}
	
	public void setMonster(MonsterInstance monster) {
		this.monster = monster;
	}
	
	public Spell getCastSpell() {
		return castSpell;
	}

	public void setCastSpell(Spell castSpell) {
		this.castSpell = castSpell;
	}

	public MonsterInstance getSummonedMonster() {
		return summonedMonster;
	}
	
	public void setSummonedMonster(MonsterInstance summonedMonster) {
		this.summonedMonster = summonedMonster;
	}

	public Damage getCharacterSplashDamage() {
		return characterSplashDamage;
	}

	public void setCharacterSplashDamage(Damage splashDamage) {
		this.characterSplashDamage = splashDamage;
	}

	public Damage getMonsterSplashDamage() {
		return monsterSplashDamage;
	}

	public void setMonsterSplashDamage(Damage splashDamage) {
		this.monsterSplashDamage = splashDamage;
	}

	public Integer getMonsterHeal() {
		return monsterHeal;
	}

	public void setMonsterHeal(Integer healAmount) {
		this.monsterHeal = healAmount;
	}

	public Integer getCharacterHeal() {
		return characterHeal;
	}

	public void setCharacterHeal(Integer healAmount) {
		this.characterHeal = healAmount;
	}

	public Integer getCharacterSpeed() {
		return characterSpeed;
	}

	public void setCharacterSpeed(Integer speed) {
		this.characterSpeed = speed;
	}

	public Integer getMonsterSpeed() {
		return monsterSpeed;
	}
	
	public void setMonsterSpeed(Integer speed) {
		this.monsterSpeed = speed;
	}

	/**
	 * Set the buff for the number of ticks, unless the character was already buffed for longer
	 * 
	 * @param buff
	 * @param value
	 */
	public void setCharacterBuff(Buff buff, int value) {
		Integer currentBuff = character.buffs.get(buff);
		if(currentBuff != null && currentBuff > value) {
			value = currentBuff;
		}
		character.buffs.put(buff, value);
	}
	
	/**
	 * Set the buff for the number of ticks, unless the monster was already buffed for longer
	 * 
	 * @param buff
	 * @param value
	 */
	public void setMonsterBuff(Buff buff, int value) {
		Integer currentBuff = monster.buffs.get(buff);
		if(currentBuff != null && currentBuff > value) {
			value = currentBuff;
		}
		monster.buffs.put(buff, value);
	}
}
