package org.kerrin.dungeon.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.kerrin.dungeon.enums.Buff;
import org.kerrin.dungeon.enums.CommonConstants;
import org.kerrin.dungeon.enums.DamageType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.Spell;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class MobData {
	@Transient
	@JsonIgnore
	protected static final int HOT_BUFF_REGEN = 10;
	@Transient
	@JsonIgnore
	protected static final int MANA_BUFF_REGEN = 10;
	@Transient
	@JsonIgnore
	protected static final int BASE_MANA_REGEN = 1;
	@Transient
	@JsonIgnore
	private static final Integer COLD_SLOW_LENGTH = 2;
	/** Monster type (characters have a monster type too, to know what spells they have, and damage types, etc) */
	@Transient
	@JsonIgnore
	protected Monster monster;
	@Transient
	@JsonIgnore
	protected int remainingHealth;
	@Transient
	@JsonIgnore
	protected int maxHealth;
	@Transient
	@JsonIgnore
	protected int mana;
	@Transient
	@JsonIgnore
	protected int stunLeft = 0;
	@Transient
	@JsonIgnore
	protected int[] pendingDamage;
	@Transient
	@JsonIgnore
	/** Buffs on Mob and how many ticks it has left */
	protected Map<Buff,Integer> buffs;
	@Transient
	@JsonIgnore
	protected Map<Spell,Integer> spellCoolDowns;
	@Transient
	@JsonIgnore
	protected Map<Monster,Integer> summonCoolDowns;
	@Transient
	@JsonIgnore
	protected int attackSpeed = 1;
	
	public MobData() {
		this.pendingDamage = new int[CommonConstants.DAMAGE_OVER_TIME_TICKS];
		for(int i=0; i < CommonConstants.DAMAGE_OVER_TIME_TICKS; i ++) {
			this.pendingDamage[i] = 0;
		}
		buffs = new HashMap<Buff,Integer>();
		spellCoolDowns = new HashMap<Spell, Integer>();
		summonCoolDowns = new HashMap<Monster, Integer>();
	}

	public void processTick(Dungeon dungeon, List<DungeonEvent> dungeonEvents) {
		processDamageOverTime(dungeon, dungeonEvents);
		processRegen(dungeon, dungeonEvents);
		if(stunLeft > 0) stunLeft--;
		if(attackSpeed > 1) attackSpeed--;
		if(attackSpeed < 1) attackSpeed++;
		reduceCoolDowns();
		reduceBuffs();
	}

	private void reduceCoolDowns() {
		List<Spell> addSpellList = new ArrayList<Spell>();
		List<Spell> removeSpellList = new ArrayList<Spell>();
		for(Spell spell:spellCoolDowns.keySet()) {
			int remainingTime = spellCoolDowns.get(spell) - 1;
			if(remainingTime < 1) {
				removeSpellList.add(spell);
			} else {
				addSpellList.add(spell);
			}
		}
		for(Spell spell:addSpellList) {
			int remainingTime = spellCoolDowns.get(spell) - 1;
			spellCoolDowns.put(spell, remainingTime);
		}
		for(Spell spell:removeSpellList) {
			spellCoolDowns.remove(spell);
		}
		
		List<Monster> addSummonList = new ArrayList<Monster>();
		List<Monster> removeSummonList = new ArrayList<Monster>();
		for(Monster summon:summonCoolDowns.keySet()) {
			int remainingTime = summonCoolDowns.get(summon) - 1;
			if(remainingTime < 1) {
				removeSummonList.add(summon);
			} else {
				addSummonList.add(summon);
			}
		}
		for(Monster summon:addSummonList) {
			int remainingTime = summonCoolDowns.get(summon) - 1;
			summonCoolDowns.put(summon, remainingTime);
		}
		for(Monster summon:removeSummonList) {
			summonCoolDowns.remove(summon);
		}
	}

	private void reduceBuffs() {
		List<Buff> addList = new ArrayList<Buff>();
		List<Buff> removeList = new ArrayList<Buff>();
		for(Buff buff:buffs.keySet()) {
			int remainingTime = buffs.get(buff) - 1;
			if(remainingTime < 1) {
				removeList.add(buff);
			} else {
				addList.add(buff);
			}
		}
		for(Buff buff:addList) {
			int remainingTime = buffs.get(buff) - 1;
			buffs.put(buff, remainingTime);
		}
		for(Buff buff:removeList) {
			buffs.remove(buff);
		}
	}
	
	protected abstract void processDamageOverTime(Dungeon dungeon, List<DungeonEvent> dungeonEvents);

	protected abstract void processRegen(Dungeon dungeon, List<DungeonEvent> dungeonEvents) ;

	/**
	 * Get how much remaining health this mob has
	 * @return
	 */
	public int getHealth() {
		return remainingHealth;
	}

	/**
	 * Get how much mana this mob has
	 * 
	 * @return
	 */
	public int getMana() {
		return mana;
	}
	
	public Monster getMonster() {
		return monster;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getStunLeft() {
		return stunLeft;
	}

	public Map<Buff, Integer> getBuffs() {
		return buffs;
	}

	public Map<Spell, Integer> getCoolDowns() {
		return spellCoolDowns;
	}

	public int[] getPendingDamage() {
		return pendingDamage;
	}

	/**
	 * 
	 * @param speedChange	-ve increases, -ve decreases
	 */
	public abstract void modifySpeed(Integer speedChange, Dungeon dungeon, List<DungeonEvent> dungeonEvents);
	
	public int getAttackSpeed() {
		return attackSpeed ;
	}

	public boolean hasBuff(Buff buff) {
		return buffs.containsKey(buff);
	}
	
	/**
	 * Check for cooldown of spell and add it
	 * 
	 * @param spell
	 * @return
	 */
	protected boolean coolDown(Spell spell) {
		if(spellCoolDowns.containsKey(spell)) {
			return false;
		}
		spellCoolDowns.put(spell, spell.getCastCoolDown(getAttackSpeed()));
		return true;
	}
	
	/**
	 * Check for cooldown of spell and add it
	 * 
	 * @param spell
	 * @return
	 */
	protected boolean coolDown(Monster summon) {
		if(summonCoolDowns.containsKey(summon)) {
			return false;
		}
		summonCoolDowns.put(summon, monster.getBaseXp());
		return true;
	}
	
	protected DungeonEvent doDamageTypeEffect(int damage, DamageType damageType, List<EquipmentAttribute> effectTypes, AttackResult attackResult, 
			boolean characterAffected, int levelOfDamager, 
			Dungeon dungeon) {
		DungeonEvent dungeonEvent = null;
		switch(damageType) {
		case FIRE: case HOLY: case MELEE: case NONE: case STUN: case WATER: break;
		case ACID: // Reduce armour	
			if(characterAffected) {
				attackResult.setCharacterBuff(Buff.REDUCE_ARMOUR, 2);
			} else {
				attackResult.setMonsterBuff(Buff.REDUCE_ARMOUR, 2);
			}
			break;
		case COLD: // Slow
			if(characterAffected) {
				attackResult.setCharacterSpeed(-COLD_SLOW_LENGTH);
			} else {
				attackResult.setMonsterSpeed(-COLD_SLOW_LENGTH);
			}
			break;
		case ELECTRIC: // Amour increases damage
			break;
		case PIERCING: // Ignore armour
			break;
		case POISON: // Damage over time
			dungeonEvent = takeDamageOverTime(damage, damageType, effectTypes, levelOfDamager, dungeon);
			damage = 0; // Poison is DoT only
			break;
		}
		
		return dungeonEvent;
	}
	
	/**
	 * 
	 * @param damage			Damage to do, this should already be adjusted for level, but not resists
	 * @param damageType		Type of damage
	 * @param levelOfDamager	Level of damager
	 * @param dungeon			Dungeon the damage occurred in
	 * @return	The resist dungeon event
	 */
	protected abstract List<DungeonEvent> takeDamage(int damage, DamageType damageType, List<EquipmentAttribute> effectTypes, int levelOfDamager, 
			Dungeon dungeon, AttackResult attackResult);
	
	/**
	 * 
	 * 
	 * @param damage			Damage to do, this should already be adjusted for level, but not resists
	 * @param damageType		Type of damage
	 * @param levelOfDamager	Level of damager
	 * @param dungeon			Dungeon the damage occurred in
	 * @return	The resist dungeon event
	 */
	public abstract DungeonEvent takeDamageOverTime(int damage, DamageType damageType, List<EquipmentAttribute> effectTypes, int levelOfDamager, 
			Dungeon dungeon);
	
	protected abstract int calculateDamage(int damage, DamageType damageType, List<EquipmentAttribute> effectTypes, int levelOfDamager);

	/**
	 * Has the mob died in the dungeon attempt they are associated with
	 * @return
	 */
	public boolean isDead() {
		return remainingHealth <= 0;
	}

	/**
	 * Set the mob to stunned unless they resist
	 * 
	 * @param stunTime		Number of ticks to stun for
	 * @param level			Level of mob stunning
	 * @param dungeon		The dungeon the stun occurred in
	 * @param dungeonEvents	The dungeon events to date
	 * 
	 * @return resisted stun
	 */
	public abstract boolean stun(int stunTime, int level, Dungeon dungeon, List<DungeonEvent> dungeonEvents);
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("MobData [");
		sb.append("monster=");
		sb.append(monster);
		sb.append(", ");
		sb.append("remainingHealth=");
		sb.append(remainingHealth);
		sb.append(", ");
		sb.append("maxHealth=");
		sb.append(maxHealth);
		sb.append(", ");
		sb.append("mana=");
		sb.append(mana);
		sb.append(", ");
		sb.append("stunLeft=");
		sb.append(stunLeft);
		sb.append(", ");
		sb.append("pendingDamage=");
		sb.append(Arrays.toString(pendingDamage));
		sb.append("buffs=[");
		for(Buff buff :buffs.keySet()) {
			sb.append(buff);
			sb.append("=>");
			sb.append(buffs.get(buff));
		}
		sb.append("]]");
		return sb.toString();
	}

	public void copy(MobData copyMobData) {
		this.monster = copyMobData.monster;
		this.buffs = copyMobData.buffs;
		this.mana = copyMobData.mana;
		this.maxHealth = copyMobData.maxHealth;
		this.pendingDamage = copyMobData.pendingDamage;
		this.remainingHealth = copyMobData.remainingHealth;
		this.stunLeft = copyMobData.stunLeft;
	}
}