package org.kerrin.dungeon.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.kerrin.dungeon.enums.AttackType;
import org.kerrin.dungeon.enums.Buff;
import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.CommonConstants;
import org.kerrin.dungeon.enums.DamageType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.enums.Spell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Database record for a character
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="characters") // character is a reserved word
public class Character extends MobData {
	@Transient
	@JsonIgnore
	private static final Logger logger = LoggerFactory.getLogger(Character.class);
	
	/**
	 * XP for a level 1, level 2 is double, 3 is triple, etc
	 */
	@Transient
	@JsonIgnore
	public static final long LEVEL1_XP = 200;
	/**
	 * The maximum level a character can reach
	 */
	@Transient
	@JsonIgnore
	public static final int MAX_LEVEL = 60;
	
	@Transient
	@JsonIgnore
	private static final int PRESTIGE_SCORE_MULTIPLIER = 10;

	/** 10->1, 19->2, 27->3, 34->4, 40->5, 45->6, 50->7, 55->8, 58->9, 60->10 */
	@Transient
	@JsonIgnore
	private static final List<Integer> prestigeLevels = new ArrayList<Integer>() {{ add(10); add(19); add(27); add(34); add(40); add(45); add(50); add(55); add(58); add(60); }};

	/** The character identifier */
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	/** The account this character belongs to 
	 *	A foreign key to account.id 
	 */
	@ManyToOne
	@JoinColumn(name="account")
	private Account account;
	
	/** Is this record for a hardcore character */
	private boolean hardcore;
	
	/** Is this record for an ironborn character */
	private boolean ironborn;
	
	/** The character name */
	private String name;
	
	/** The character level */
	private int level;
	
	/** The character prestige level */
	private int prestigeLevel;
	
	private boolean usedLevelUp;
	
	/** The class of the character */
	@Column(name = "char_class")
	@Enumerated(EnumType.ORDINAL)
	private CharClass charClass;
	
	/** The level of the character */
	//private int level;
	
	/** How much experience the character has earnt in this level */
	private long xp;
	
	/** Date and time this character dies next, or null if they are alive */
	private Date deathClock = null;
	
	private String diedTo = null;
	
	/** The date and time the character was created, used for speed levelling hiscore */
	private Date createdDateTime = null;
	
	/** If they are running a dungeon, this is the dungeon id */
	@ManyToOne
	@JoinColumn(name="dungeon")
	@JsonIgnore // Else we get circular references, as this character is link in the dungeon
	private Dungeon dungeon;

	// ------------ Variables used during dungeon processing
	/**
	 * The equipment the character was wearing when they started the dungeon they are running (to reduce DB access)
	 * Important: This is only public for unit testing
	 */
	@Transient
	@JsonIgnore
	public CharacterEquipment currentEquipment;	

	protected Character() {};
	
	public Character(long id, Account account, boolean hardcore, boolean ironborn, String name, CharClass charClass, 
			int level, long xp, int prestigeLevel, Date deathClock, Dungeon dungeon) {
		this.id = id;
		this.account = account;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.usedLevelUp = false;
		this.name = name;
		this.charClass = charClass;
		this.level = level;
		this.xp = xp;
		this.prestigeLevel = prestigeLevel;
		this.deathClock = deathClock;
		this.createdDateTime = new Date();
		this.dungeon = dungeon;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public boolean isHardcore() {
		return hardcore;
	}

	public void setHardcore(boolean hardcore) {
		this.hardcore = hardcore;
	}

	public boolean isIronborn() {
		return ironborn;
	}

	public void setIronborn(boolean ironborn) {
		this.ironborn = ironborn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CharClass getCharClass() {
		return charClass;
	}

	public void setCharClass(CharClass charClass) {
		this.charClass = charClass;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPrestigeLevel() {
		return prestigeLevel;
	}

	public void setPrestigeLevel(int prestigeLevel) {
		this.prestigeLevel = prestigeLevel;
	}

	public long getXp() {
		return xp;
	}

	public void setXp(long xp) {
		this.xp = xp;
	}

	public boolean isUsedLevelUp() {
		return usedLevelUp;
	}

	public void setUsedLevelUp(boolean usedLevelUp) {
		this.usedLevelUp = usedLevelUp;
	}

	/**
	 * Give the character XP
	 * 
	 * @param xp
	 * @return	Character levels gained
	 */
	public int giveXp(long xp) {
		this.xp += xp;
		int levelsGained = 0;
		long xpNextLevel = getXpNextLevel();
		while(this.xp >= xpNextLevel && level < MAX_LEVEL) {
			level++;
			levelsGained++;
			this.xp -= xpNextLevel;
			xpNextLevel = getXpNextLevel();
		}
		if(level >= MAX_LEVEL) xp = 0;
		return levelsGained;
	}

	public long getXpNextLevel() {
		return level * LEVEL1_XP;
	}

	/**
	 * Gets the number of prestige levels this character would gain if it were reset now 
	 * @return
	 */
	public int getResetPrestige() {
		int awardPrestigeLevels = 0;
		while(awardPrestigeLevels < prestigeLevels.size() && level >= prestigeLevels.get(awardPrestigeLevels)) {
			awardPrestigeLevels++;
		}
		return awardPrestigeLevels;
	}

	/**
	 * Reset the level for prestige
	 * Note: Remember to modify the worn equipment too
	 * @return Number of levels added
	 */
	public int resetPrestige() {
		int levelsGained = getResetPrestige();
		prestigeLevel += levelsGained;
		level = 1;
		xp = 0;
		return levelsGained;
	}
	
	/**
	 * Get the percentage of XP to next level to the nearest 10%
	 * 
	 * @return
	 */
	public int getXpClass() {
		if(xp <= 0) return 0;
		double roundXp = xp/(getXpNextLevel()/10.0);
		int xpClass = ((int)Math.round(roundXp)*10) - 10;
		return xpClass>0?xpClass:0;
	}
	
	/**
	 * Has the character died and their dungeon finished or they aren't in one
	 * @return
	 */
	public boolean isCurrentlyDead() {
		return deathClock != null && deathClock.before(new Date());
	}
	
	public Date getDeathTime() {
		return deathClock;
	}
	
	public void setDiedTime(Date timeOfDeath) {
		this.deathClock = timeOfDeath;
	}
	
	public void setAlive() {
		this.deathClock = null;
		this.diedTo = null;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	
	public Dungeon getDungeon() {
		return dungeon;
	}

	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}
	
	public String getDiedTo() {
		return diedTo;
	}

	public void setDiedTo(String diedTo) {
		this.diedTo = diedTo;
	}

	/**
	 * Mark the characters reason for death from the monster and spell
	 * 
	 * @param monster
	 * @param spell
	 * */
	public void setDiedTo(MonsterInstance monster, Spell spell) {
		StringBuilder deathDescription = new StringBuilder();
		if(monster != null && !monster.equals(Monster.UNKNOWN)) {
			if(monster.monsterType.after(MonsterType.TRASH)) {
				deathDescription.append(monster.monsterType.getNiceName());
				deathDescription.append(" ");
			}
			deathDescription.append(monster.monster.getNiceName());
		}
		if(spell != null && !spell.equals(Spell.UNKNOWN)) {
			if(!deathDescription.equals("")) {
				deathDescription.append(" Casting ");
			}
			deathDescription.append(spell.getNiceName());
		}
		if(deathDescription.length() == 0) {
			// No specific monster or spell
			deathDescription.append("Damage over time");
		}
		this.diedTo = deathDescription.toString();
	}

	/**
	 * This should only be called while the character is running a dungeon by the dungeon processor
	 * @return
	 */
	public CharacterEquipment getCurrentEquipmentWhileInDungeon() {
		return currentEquipment;
	}

	/**
	 * This character has just started a dungeon
	 * 
	 * @param equipment
	 */
	public void startDungeon(Dungeon dungeon, CharacterEquipment equipment) {
		this.dungeon = dungeon;
		this.maxHealth = calculateMaxHealth(equipment);
		this.remainingHealth = this.maxHealth;
		this.mana = charClass.getBaseMana();
		this.stunLeft = 0;
		this.currentEquipment = equipment;
	}

	public int calculateMaxHealth(CharacterEquipment equipment) {
		return (level * charClass.getBaseHealth()) + equipment.getTotalAttributeValue(charClass.getMainAttribute());
	}

	@Override
	public List<DungeonEvent> takeDamage(int damage, DamageType damageType, List<EquipmentAttribute> effectTypes, int levelOfDamager, Dungeon dungeon, AttackResult attackResult) {
		int resultingDamage = calculateDamage(damage, damageType, effectTypes, levelOfDamager);
		List<DungeonEvent> dungeonEvents = new ArrayList<DungeonEvent>();
		DungeonEvent.addDungeonEvent(dungeonEvents, 
				doDamageTypeEffect(resultingDamage, damageType, effectTypes, attackResult, true, levelOfDamager, dungeon));
		if(damageType != DamageType.POISON) {
			this.remainingHealth -= resultingDamage;
			if(resultingDamage < damage) {
				StringBuilder description = new StringBuilder();
				description.append(name);
				description.append(" resisted ");
				description.append(damage - resultingDamage);
				description.append(" damage.");
				dungeonEvents.add(DungeonEvent.info(dungeon, description.toString()));
			}
		}
		return dungeonEvents;
	}
	
	/**
	 * Calculate how much damage to really do after equipment reduction
	 */
	public int calculateDamage(int damage, DamageType damageType, List<EquipmentAttribute> effectTypes, int levelOfDamager) {
		int resultingDamage = damage;
		
		int damageReductionValue = classDamageReductionValue(damageType, effectTypes);
		double damageRemainingPercentage = (100.0 - (damageReductionValue / levelOfDamager)) / 100;
		if(damageRemainingPercentage < 0) {
			damageRemainingPercentage = 0.1;
		}
		resultingDamage *= damageRemainingPercentage;

		int armourReductionValue = currentEquipment.getTotalAttributeValue(EquipmentAttribute.ARMOUR) / (CharSlot.values().length-1);
		if(hasBuff(Buff.REDUCE_ARMOUR)) {
			armourReductionValue *= 0.75;
		}
		damageRemainingPercentage = (100.0 - (armourReductionValue / levelOfDamager)) / 100;
		if(damageRemainingPercentage < 0) {
			damageRemainingPercentage = 0.1;
		}
		if(damageType != DamageType.PIERCING) {
			if(damageType == DamageType.ELECTRIC) {
				// Increase damage by  1/4 'damage reduction' from armour
				resultingDamage *= 1+((1-damageRemainingPercentage)/4);
			} else  {
				resultingDamage *= damageRemainingPercentage;
			}
		}

		// Can never completely reduce the damage (at least 1%)
		if(resultingDamage < (damage/100)) resultingDamage = (damage/100);
		// At low levels it could still be less than 1
		if(resultingDamage < 1) resultingDamage = 1;
		
		return resultingDamage;
	}

	/**
	 * 
	 * @param monster
	 * @return
	 */
	public AttackResult attack(Dungeon dungeon, MonsterInstance monster, List<DungeonEvent> dungeonEvents) {
		AttackType attackType = AttackType.getRandom();
		AttackResult attackResult = new AttackResult(this, monster);
		switch(attackType) {
		case WEAPON: case SUMMON: // Characters can't summon
			monsterTakeDamage(dungeon, monster, dungeonEvents, attackResult);
			break;
		case SPELL:
			Spell spell = this.getRandomSpell(level);
			if(spell == null) {
				// No spell, so hit with weapon instead
				monsterTakeDamage(dungeon, monster, dungeonEvents, attackResult);
			} else {
				if(spell.getCastCost() < mana) {
					if(coolDown(spell)) {
						mana -= spell.getCastCost();
						attackResult = spell.castOn(dungeon, monster, this, currentEquipment, dungeonEvents);
					} else {
						monsterTakeDamage(dungeon, monster, dungeonEvents, attackResult);
					}
				} else {
					monsterTakeDamage(dungeon, monster, dungeonEvents, attackResult);
				}
			}
			break;
		}
		monster.copy(attackResult.getMonster());
		return attackResult;
	}

	private void monsterTakeDamage(Dungeon dungeon, MonsterInstance monster, List<DungeonEvent> dungeonEvents,
			AttackResult attackResult) {
		int damageAmount = getLevelWeaponDamage();
		DamageType damageType = Monster.fromCharClass(charClass).getDamageType();
		List<EquipmentAttribute> effectTypes = new ArrayList<EquipmentAttribute>();
		List<DungeonEvent> newDungeonEvents = monster.takeDamage(damageAmount, damageType, effectTypes , level, dungeon, attackResult);
		attackResult.setMonster(monster);
		String description = "("+damageAmount+" "+damageType.getNiceName()+" Damage)";
		dungeonEvents.add(DungeonEvent.characterAttack(dungeon, this, monster, description));
		// Add new events after damage event
		DungeonEvent.addDungeonEvents(dungeonEvents, newDungeonEvents);
	}
	
	/**
	 * Get a random spell this monster can cast at this level
	 * 
	 * @return Spell or null if no spells castable
	 */
	public Spell getRandomSpell(int level) {
		List<Spell> spells = Spell.getSpells(charClass, level);
		int chance = spells.size();
		
		for(Spell spell:spells) {
			int random = (int) (Math.random()*chance);
			if(random < 1) return spell;
			chance--;
		}
		if(chance > 0) {
			logger.error("No spell rolled randomly, chance is still {} of {}", chance, spells.size());
		} else {
			logger.debug("No spell rolled randomly, must have been none low enough level ({}) for class {}", 
					level, charClass.getNiceName());
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private int getLevelWeaponDamage() {
		return charClass.getBaseDamage() * level;
	}

	/**
	 * Get the value adjustment to make to the damage based on the character equipment main stat and resists
	 * If the equipment is all level X, the this is percentage reduction at level X
	 * 
	 * @param damageType	The type of damage
	 * @return
	 */
	public int classDamageReductionValue(DamageType damageType, List<EquipmentAttribute> effectTypes) {
		int reductionValue = currentEquipment.getTotalAttributeValue(charClass.getMainAttribute()) / (CharSlot.values().length-1);
		reductionValue += currentEquipment.getTotalAttributeValue(damageType.getEquipmentAttribute().getResistAttribute()) / (CharSlot.values().length-1);
		reductionValue += currentEquipment.getTotalAttributeValue(EquipmentAttribute.ALL_RESIST) / (CharSlot.values().length-1);
		reductionValue += (hasBuff(Buff.ALL_RESIST)?25:0);
		
		Monster charMonster = Monster.fromCharClass(charClass);
		if(charMonster != null) {
			HashMap<EquipmentAttribute, Integer> strongAttributes = charMonster.getStrongAttributes();
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
		
		return reductionValue;
	}

	@Override
	public boolean stun(int stunTime, int level, Dungeon dungeon, List<DungeonEvent> dungeonEvents) {
		int stunResist = (currentEquipment.getTotalAttributeValue(EquipmentAttribute.STUN_RESIST) / level);
		if((int)(Math.random()*100) >= stunResist) {
			// Stun not resisted
			if(stunLeft  < stunTime) {
				StringBuilder description = new StringBuilder();
				description.append(name);
				description.append(" was stunned for ");
				description.append(stunTime);
				description.append(" ticks.");
				dungeonEvents.add(DungeonEvent.info(dungeon, description.toString()));
				stunLeft = stunTime;
				return true;
			}
		}
		return false;
	}

	@Override
	public DungeonEvent takeDamageOverTime(int damage, DamageType damageType, List<EquipmentAttribute> effectTypes, int levelOfDamager, Dungeon dungeon) {
		DungeonEvent dungeonEvent = null;
		int resultingDamage = calculateDamage(damage, damageType, effectTypes, levelOfDamager);
		if(resultingDamage < damage) {
			StringBuilder description = new StringBuilder();
			description.append(name);
			description.append(" resisted ");
			description.append(damage - resultingDamage);
			description.append(" damage from damage over time.");
			dungeonEvent = DungeonEvent.info(dungeon, description.toString());
		}
		int damageTick = resultingDamage / CommonConstants.DAMAGE_OVER_TIME_TICKS;
		for(int i=0; i < CommonConstants.DAMAGE_OVER_TIME_TICKS; i ++) {
			pendingDamage[i] += damageTick;
		}
		
		return dungeonEvent;
	}
	
	@Override
	protected void processRegen(Dungeon dungeon, List<DungeonEvent> dungeonEvents) {
		if(isDead()) return;
		int startHealth = remainingHealth;
		int startMana = mana;
		remainingHealth += currentEquipment.getTotalAttributeValue(EquipmentAttribute.HEALTH_REGEN);
		remainingHealth += (hasBuff(Buff.HEALTH_REGEN)?level*HOT_BUFF_REGEN:0);
		if(remainingHealth > maxHealth) remainingHealth = maxHealth;
		mana += BASE_MANA_REGEN;
		mana += currentEquipment.getTotalAttributeValue(EquipmentAttribute.MANA_REGEN);
		mana += (hasBuff(Buff.MANA_REGEN)?MANA_BUFF_REGEN:0);
		if(mana > charClass.getBaseMana()) mana = charClass.getBaseMana();
		
		if(startHealth < remainingHealth || startMana < mana) {
			StringBuilder sb = new StringBuilder();
			sb.append(name);
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
	public void modifySpeed(Integer speedChange, Dungeon dungeon, List<DungeonEvent> dungeonEvents)  {
		attackSpeed += speedChange;
		StringBuilder description = new StringBuilder();
		description.append(name);
		description.append(" increased attack speed by ");
		description.append(speedChange);
		description.append(".");
		dungeonEvents.add(DungeonEvent.info(dungeon, description.toString()));
	}
	
	@Override
	protected void processDamageOverTime(Dungeon dungeon, List<DungeonEvent> dungeonEvents) {
		// Damage the character for the next tick
		remainingHealth -= pendingDamage[0];
		
		if(pendingDamage[0] != 0) {
			StringBuilder description = new StringBuilder();
			description.append(name);
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

	/**
	 * Increase the health of this character by the heal amount
	 * 
	 * @param heal
	 */
	public void heal(Integer heal) {
		remainingHealth += heal;
		if(remainingHealth > maxHealth) remainingHealth = maxHealth;
	}

	/**
	 * Is this character currently stunned
	 * @return
	 */
	public boolean isStunned() {
		return stunLeft > 0;
	}

	@Override
	public String toString() {
		return toString(true, false);
	}
	
	
	public String toString(boolean showInFull, boolean showNice) {
		StringBuilder sb = new StringBuilder("Character [");
		if(showNice) {
			sb.append("\n");
		}
		sb.append("id=");
		sb.append(id);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		if(showInFull) {
			sb.append("account=");
			sb.append(account);
		} else {
			sb.append("accountId=");
			sb.append(account.getId());
		}
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}

		sb.append("name=");
		sb.append(name);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		sb.append("charClass=");
		sb.append(charClass);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		sb.append("level=" + level);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		sb.append("prestigeLevel=");
		sb.append(prestigeLevel);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		sb.append("xp=" + xp);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		sb.append("created=");
		sb.append(createdDateTime);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		sb.append("dead=");
		sb.append((isCurrentlyDead()?"yes":"no"));
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		sb.append("diedTo=");
		sb.append(diedTo);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		sb.append("dungeon=");
		sb.append((dungeon==null?"none":dungeon.toString(!showInFull, showNice)));
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		sb.append("mobData=[");
		if(showNice) {
			sb.append("\n");
		}
		sb.append(super.toString());
		if(showNice) {
			sb.append("\n");
		}
		sb.append("]]");
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object other) {
	    if (!(other instanceof Character)) {
	        return false;
	    }

	    Character that = (Character) other;

	    // Custom equality check here.
	    return this.id == that.id;
	}
	
	@Override
	public int hashCode() {
	    int hashCode = 1;

	    hashCode = hashCode * 37 + (int)this.id;

	    return hashCode;
	}

	/**
	 * Copy the character data
	 * @param copyCharacter
	 */
	public void copy(Character copyCharacter) {
		super.copy(copyCharacter);
		this.id = copyCharacter.id;
		this.account = copyCharacter.account;
		this.name = copyCharacter.name;
		this.charClass = copyCharacter.charClass;
		this.level = copyCharacter.level;
		this.prestigeLevel = copyCharacter.prestigeLevel;
		this.xp = copyCharacter.xp;
		this.deathClock = copyCharacter.deathClock;
		this.createdDateTime = copyCharacter.createdDateTime;
	}

	/**
	 * Get the levels combined to get a score
	 * @return
	 */
	public long getCombinedLevel() {
		int combinedLevel = (prestigeLevel * PRESTIGE_SCORE_MULTIPLIER) + level;
		return combinedLevel;
	}
}
