package org.kerrin.dungeon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.kerrin.dungeon.enums.AttackType;
import org.kerrin.dungeon.enums.Buff;
import org.kerrin.dungeon.enums.CommonConstants;
import org.kerrin.dungeon.enums.DamageType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.enums.Spell;

public class MonsterInstance extends MobData {
	private static final int SUMMON_MANA_COST = 40;
	/** Monster Type */
	public MonsterType monsterType;
	/** The monster level */
	public int level;
	/** The number of characters in the party */
	public int partySize;
	
	public MonsterInstance(Monster monster, MonsterType monsterType, int level, int partySize) {
		super();
		this.monster = monster;
		this.monsterType = monsterType;
		this.level = level;
		this.partySize = partySize;
		// Health is:
		// Base health x Monster Type x Level x Party Size (which is +0.75 for each additional party member)
		this.remainingHealth = calculateMaxHealth(monster, monsterType, level, partySize);
		this.maxHealth = remainingHealth;
		this.mana = monster.getMana();	
	}

	/**
	 * Max Health is health x Monster Type x Level Modifier x Party Size (which is +0.75 for each additional party member)
	 * Where level modifier is 120% level - 1
	 * Special case, level 1s level modifier is 1 
	 * 
	 * @param monster
	 * @param monsterType
	 * @param level
	 * @param partySize
	 * @return
	 */
	public static int calculateMaxHealth(Monster monster, MonsterType monsterType, int level, int partySize) {
		int levelModifier = level + ((level / 5) - 1);
		if(levelModifier < 1) levelModifier = 1;
		return monster.getHealth() * monsterType.getTypeMultiplier() * levelModifier * (25 + (partySize * 75)) / 100;
	}
	
	public AttackResult attack(Dungeon dungeon, Character character, List<DungeonEvent> dungeonEvents) {
		AttackType attackType = AttackType.getRandom();
		AttackResult attackResult = new AttackResult(character, this);
		switch(attackType) {
		case WEAPON:
			characterTakeDamage(dungeon, character, dungeonEvents, attackResult);
			break;
		case SPELL:
			Spell spell = monster.getRandomSpell(level);
			if(spell == null) {
				// No spell, so hit with weapon instead
				characterTakeDamage(dungeon, character, dungeonEvents, attackResult);
			} else {
				if(spell.getCastCost() < mana) {
					if(coolDown(spell)) {
						mana -= spell.getCastCost();
						attackResult = spell.castOn(dungeon, character, this, dungeonEvents);
					} else {
						characterTakeDamage(dungeon, character, dungeonEvents, attackResult);
					}
				} else {
					characterTakeDamage(dungeon, character, dungeonEvents, attackResult);
				}
			}
			break;
		case SUMMON:
			Monster summonedMonster = monster.getRandomSummon();
			if(summonedMonster == null) {
				// No summon, so hit with weapon instead
				characterTakeDamage(dungeon, character, dungeonEvents, attackResult);
				
			} else {
				if(SUMMON_MANA_COST < mana) {
					if(coolDown(summonedMonster)) {
						mana -= SUMMON_MANA_COST;
						attackResult.setSummonedMonster(new MonsterInstance(summonedMonster, MonsterType.TRASH, level, partySize));
					} else {
						characterTakeDamage(dungeon, character, dungeonEvents, attackResult);
					}
				} else {
					characterTakeDamage(dungeon, character, dungeonEvents, attackResult);
				}				
			}
			break;
		}
		character.copy(attackResult.getCharacter());
		return attackResult;
	}
	
	@Override
	public List<DungeonEvent> takeDamage(int damage, DamageType damageType, List<EquipmentAttribute> effectTypes, int levelOfDamager, 
			Dungeon dungeon, AttackResult attackResult) {
		int resultingDamage = calculateDamage(damage, damageType, effectTypes, levelOfDamager);
		List<DungeonEvent> dungeonEvents = new ArrayList<DungeonEvent>();
		DungeonEvent.addDungeonEvent(dungeonEvents, 
				doDamageTypeEffect(resultingDamage, damageType, effectTypes, attackResult, true, levelOfDamager, dungeon));
		if(damageType != DamageType.POISON) {
			this.remainingHealth -= resultingDamage;
			if(resultingDamage < damage) {
				StringBuilder description = new StringBuilder();
				description.append(monster.getNiceName());
				description.append(" resisted ");
				description.append(damage - resultingDamage);
				description.append(" damage.");
				dungeonEvents.add(DungeonEvent.info(dungeon, description.toString()));
			}
		}
		
		return dungeonEvents;
	}

	private void characterTakeDamage(Dungeon dungeon, Character character, List<DungeonEvent> dungeonEvents, 
			AttackResult attackResult) {
		int damageAmount = monster.getBaseWeaponDamage() * dungeon.getLevel();
		DamageType damageType = monster.getDamageType();
		
		List<EquipmentAttribute> effectTypes = new ArrayList<EquipmentAttribute>();
		List<DungeonEvent> newDungeonEvents = character.takeDamage(damageAmount, damageType, effectTypes , level, dungeon, attackResult);
		String description = "("+damageAmount+" "+damageType.getNiceName()+" Damage)";
		dungeonEvents.add(DungeonEvent.monsterAttack(dungeon, this, character, description));
		// Add resist, etc event after damage event
		DungeonEvent.addDungeonEvents(dungeonEvents, newDungeonEvents);
		attackResult.setCharacter(character);
	}

	@Override
	public DungeonEvent takeDamageOverTime(int damage, DamageType damageType, List<EquipmentAttribute> effectTypes, int levelOfDamager, 
			Dungeon dungeon) {
		DungeonEvent dungeonEvent = null;
		int realDamage = damage * levelOfDamager;
		int resultingDamage = calculateDamage(realDamage, damageType, effectTypes, levelOfDamager);
		if(resultingDamage < realDamage) {
			StringBuilder description = new StringBuilder();
			description.append(monster.getNiceName());
			description.append(" resisted ");
			description.append(realDamage - resultingDamage);
			description.append(" damage from damage over time.");
			dungeonEvent = DungeonEvent.info(dungeon, description.toString());
		}
		int damageTick = resultingDamage / CommonConstants.DAMAGE_OVER_TIME_TICKS;
		for(int i=0; i < CommonConstants.DAMAGE_OVER_TIME_TICKS; i ++) {
			pendingDamage[i] += damageTick;
		}
		
		return dungeonEvent;
	}

	/**
	 * Increase the health of this character by the heal amount
	 * 
	 * @param heal
	 */
	public void heal(Integer heal, Dungeon dungeon, List<DungeonEvent> dungeonEvents) {
		remainingHealth += heal;
		if(remainingHealth > maxHealth) remainingHealth = maxHealth;
		String description = "";
		dungeonEvents.add(DungeonEvent.monsterHeal(dungeon, this, description));		
	}
	
	public void copy(MonsterInstance copyMonsterInstance) {
		super.copy(copyMonsterInstance);
		this.level = copyMonsterInstance.level;
	}
	
	@Override
	protected int calculateDamage(int damage, DamageType damageType, List<EquipmentAttribute> effectTypes, int levelOfDamager) {
		int resultingDamage = damage;
		
		int damageReductionValue = monsterDamageReductionValue(damageType, effectTypes);
		if(hasBuff(Buff.REDUCE_ARMOUR)) {
			damageReductionValue *= 0.75;
		}		
		double damageRemainingPercentage = (100.0 - (damageReductionValue / levelOfDamager)) / 100;
		resultingDamage *= damageRemainingPercentage;

		// Can never completely reduce the damage (at least 1%)
		if(resultingDamage < (damage/100)) resultingDamage = (damage/100);
		// At low levels it could still be less than 1
		if(resultingDamage < 1) resultingDamage = 1;
		
		return resultingDamage;
	}

	@Override
	public void modifySpeed(Integer speedChange, Dungeon dungeon, List<DungeonEvent> dungeonEvents)  {
		attackSpeed += speedChange;
		StringBuilder description = new StringBuilder();
		description.append(monster.getNiceName());
		description.append(" increased attack speed by ");
		description.append(speedChange);
		description.append(".");
		dungeonEvents.add(DungeonEvent.info(dungeon, description.toString()));
	}
	
	private int monsterDamageReductionValue(DamageType damageType, List<EquipmentAttribute> effectTypes) {
		int reductionValue = (hasBuff(Buff.ALL_RESIST)?25:0);
		
		if(monster != null) {
			HashMap<EquipmentAttribute, Integer> strongAttributes = monster.getStrongAttributes();
			if(strongAttributes != null) {
				Integer strongAttribute = strongAttributes.get(damageType.getEquipmentAttribute());
				if(strongAttribute != null) {
					reductionValue += strongAttribute;
				}
				for(EquipmentAttribute effectType: effectTypes) {
					strongAttribute = strongAttributes.get(effectType);
					if(strongAttribute != null) {
						reductionValue += strongAttribute;
					}
				}
			}
		}
		
		return reductionValue * level;
	}

	@Override
	protected void processRegen(Dungeon dungeon, List<DungeonEvent> dungeonEvents) {
		if(isDead()) return;
		int startHealth = remainingHealth;
		int startMana = mana;
		// Only regen if has HEAL_OVER_TIME buff active
		remainingHealth += (hasBuff(Buff.HEALTH_REGEN)?level*HOT_BUFF_REGEN:0);
		if(remainingHealth > maxHealth) remainingHealth = maxHealth;
		mana += BASE_MANA_REGEN;
		if(mana > monster.getMana()) mana = monster.getMana();
		if(startHealth < remainingHealth || startMana < mana) {
			StringBuilder sb = new StringBuilder();
			sb.append(monster.getNiceName());
			sb.append(" regenerated ");
			if(startHealth < remainingHealth) {
				sb.append(remainingHealth - startHealth);
				sb.append(" health ");
			}
			if(startMana < mana) {
				if(startHealth < remainingHealth) {
					sb.append("and ");
				}
				sb.append(mana - startMana);
				sb.append(" mana.");
			}
			dungeonEvents.add(DungeonEvent.info(dungeon, sb.toString()));
		}
	}

	@Override
	protected void processDamageOverTime(Dungeon dungeon, List<DungeonEvent> dungeonEvents) {
		// Damage the character for the next tick
		remainingHealth -= pendingDamage[0];
		
		if(pendingDamage[0] != 0) {
			StringBuilder description = new StringBuilder();
			description.append(monster.getNiceName());
			description.append(" took ");
			description.append(pendingDamage[0]);
			description.append(" damage over time");
			dungeonEvents.add(DungeonEvent.info(dungeon, description.toString()));
		}
		
		// Shift all the damage up 1 tick
		for(int i=1; i < CommonConstants.DAMAGE_OVER_TIME_TICKS; i ++) {
			pendingDamage[i-1] = pendingDamage[i];
		}
		// zero the last tick
		pendingDamage[CommonConstants.DAMAGE_OVER_TIME_TICKS-1] = 0;
	}
	
	public boolean stun(int stunTime, int level, Dungeon dungeon, List<DungeonEvent> dungeonEvents) {
		if(stunLeft < stunTime) {
			StringBuilder description = new StringBuilder();
			description.append(monster.getNiceName());
			description.append(" was stunned for ");
			description.append(stunTime);
			description.append(" ticks.");
			dungeonEvents.add(DungeonEvent.info(dungeon, description.toString()));
			stunLeft = stunTime;
		}
		return false; // Monsters never resist stuns
	}

	/**
	 * Calculate the XP for the monster
	 * @return
	 */
	public long getXp() {		
		return monster.getBaseXp() * level;
	}
	
	@Override
	public String toString() {
		return "MonsterInstance [level=" + level + " mobData=[" + super.toString() + "]]";
	}
}
