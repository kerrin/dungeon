package org.kerrin.dungeon.model;

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

import org.kerrin.dungeon.enums.DungeonEventType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.enums.Spell;
import org.kerrin.dungeon.exception.InvalidSpell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="dungeon_event")
public class DungeonEvent {
	@Transient
	@JsonIgnore
	private static final Logger logger = LoggerFactory.getLogger(DungeonEvent.class);
	
	/** The account identifier */
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	/** The dungeon */
	@ManyToOne
	@JoinColumn(name="dungeon")
	private Dungeon dungeon;
	/** The type of event that occurred */
	@Column(name = "event_type")
	@Enumerated(EnumType.ORDINAL)
	private DungeonEventType eventType;
	/** An increasing number for the order the event happened */
	private Integer eventOrder;
	/** The character involved in the event */
	@ManyToOne
	@JoinColumn(name="effectedCharacter")
	private Character effectedCharacter;
	/** The maximum health this character can have */
	private Integer characterMaxHealth;
	/** The characters health after the event */
	private Integer characterHealth;
	/** The characters mana after the event */
	private Integer characterMana;
	/** The monster involved in the event */
	private Integer monsterId;
	/** The monster type involved in the event */
	private Integer monsterTypeId;
	/** The characters health after the event */
	private Integer monsterHealth;
	/** The characters mana after the event */
	private Integer monsterMana;
	/** The spell cast in the event */
	private Integer spellId;
	/** 
	 * A description of the event
	 * Contains additional info, like what monster was summoned, or which character was healed and by how much 
	 */
	private String description;
	
	protected DungeonEvent() {};
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param dungeon		The dungeon this event occurred in
	 * @param eventType
	 * @param effectedCharacter
	 * @param monster
	 * @param spell
	 * @param description
	 */
	private DungeonEvent(int id, Dungeon dungeon, DungeonEventType eventType, 
			Character effectedCharacter, MonsterInstance monster, 
			Spell spell, String description) {
		super();
		this.id = -1;
		this.dungeon = dungeon;
		this.eventType = eventType;
		if(effectedCharacter != null) {
			this.effectedCharacter = effectedCharacter;
			this.characterMaxHealth = effectedCharacter.getMaxHealth();
			this.characterHealth = effectedCharacter.getHealth();
			this.characterMana = effectedCharacter.getMana();
		}
		if(monster != null) {
			this.monsterId = monster.monster.getId();
			this.monsterTypeId = monster.monsterType.getId();
			this.monsterHealth = monster.getHealth();
			this.monsterMana = monster.getMana();
		}
		if(spell != null) {
			this.spellId = spell.getId();
		}
		this.description = description;
	}
	
	/**
	 * Create event for a character attacking a monster
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param character		Attacking Character
	 * @param monster		Attacked Monster
	 * @param description 	Contains how much damage was done, what type
	 * @return
	 */
	public static DungeonEvent characterAttack(Dungeon dungeon, Character character, MonsterInstance monster, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.CHARACTER_ATTACK, character, monster, null, description);	
	}
	
	/**
	 * Create an event for a character casting a spell on a character (including self)
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param character		Casting character
	 * @param spell			Positive Spell cast
	 * @param description	Details of spell, starting with the character name the spell was cast on. 
	 * 						Also contain amount healed, length of buff etc.
	 * @return
	 * @throws InvalidSpell		If not a valid spell to cast on a character by a character
	 */
	public static DungeonEvent characterCastSpell(Dungeon dungeon, Character character, Spell spell, String description) throws InvalidSpell {
		if(!validSelfSpell(spell)) throw new InvalidSpell("Attempt character to cast spell "+spell.getNiceName()+" on character");
		return new DungeonEvent(-1, dungeon, DungeonEventType.CHARACTER_SPELL, character, null, spell, description);
	}
	
	/**
	 * Create an event for a character casting a spell on a monster
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param character		Casting character
	 * @param spell			Negative spell cast
	 * @param monster		Monster spell cast on
	 * @param description	Details of spell cast
	 * @return
	 * @throws InvalidSpell 
	 */
	public static DungeonEvent characterCastSpell(Dungeon dungeon, Character character, Spell spell, MonsterInstance monster, String description) throws InvalidSpell {
		if(validSelfSpell(spell)) throw new InvalidSpell("Attempt character to cast spell "+spell.getNiceName()+" on monster");
		return new DungeonEvent(-1, dungeon, DungeonEventType.CHARACTER_SPELL, character, monster, spell, description);		
	}
	
	// Characters can't currently summon
	/*public static DungeonEvent characterSummon(Dungeon dungeon, Character character, MonsterInstance monster, String description) {
		return new DungeonEvent(-1, DungeonEventType.CHARACTER_SUMMON, character, monster, spell, description);		
	}*/

	/**
	 * Character got healed
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param character		The character getting healed
	 * @param description	Description of heal (amount healed)
	 * @return
	 */
	public static DungeonEvent characterHeal(Dungeon dungeon, Character character, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.CHARACTER_HEAL, character, null, null, description);
	}
	
	/**
	 * A character died
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param character		Character dying
	 * @param description	Description of death
	 * @return
	 */
	public static DungeonEvent characterDied(Dungeon dungeon, Character character, MonsterInstance monster, 
			Spell spell, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.CHARACTER_DIED, character, monster, spell, description);
	}

	/**
	 * Character buff applied
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param character		Character being buffed
	 * @param spell			The spell that buffed
	 * @param description	Description of buff
	 * @return
	 */
	public static DungeonEvent characterBuff(Dungeon dungeon, Character character, Spell spell, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.CHARACTER_BUFF, character, null, spell, description);
	}

	/**
	 * Character debuff applied
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param character		Character being debuffed
	 * @param spell			The spell that debuffed
	 * @param description	Description of debuff
	 * @return
	 */
	public static DungeonEvent characterDebuff(Dungeon dungeon, Character character, MonsterInstance monster, Spell spell, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.CHARACTER_DEBUFF, character, monster, spell, description);
	}

	/**
	 * A character took damage
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param character		Character taking damage
	 * @param description	Description of damage
	 * @return
	 */
	public static DungeonEvent characterTakeDamage(Dungeon dungeon, Character character, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.CHARACTER_TAKE_DAMAGE, character, null, null, description);
	}
	
	/**
	 * Create an event for a monster attacking a character
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param monster		Monster attacking
	 * @param character		Character being attacked
	 * @param description	Contains how much damage was done, what type
	 * @return
	 */
	public static DungeonEvent monsterAttack(Dungeon dungeon, MonsterInstance monster, Character character, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.MONSTER_ATTACK, character, monster, null, description);		
	}
	
	/**
	 * Create an event for a monster casting a spell on a monster (including self)
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param monster		Monster casting spell
	 * @param spell			Positive Spell cast
	 * @param description	Details of spell, starting with the monster name the spell was cast on. 
	 * 						Also contain amount healed, length of buff etc.
	 * @return
	 * @throws InvalidSpell
	 */
	public static DungeonEvent monsterCastSpell(Dungeon dungeon, MonsterInstance monster, Spell spell, String description) throws InvalidSpell {
		if(!validSelfSpell(spell)) throw new InvalidSpell("Attempt monster to cast spell "+spell.getNiceName()+" on monster");
		return new DungeonEvent(-1, dungeon, DungeonEventType.MONSTER_SPELL, null, monster, spell, description);
	}
	
	/**
	 * Create an event for a monster casting a spell on a character
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param monster		Monster casting spell
	 * @param spell			Spell cast
	 * @param character		Character spell cast on
	 * @param description	Details of spell cast
	 * @return
	 * @throws InvalidSpell
	 */
	public static DungeonEvent monsterCastSpell(Dungeon dungeon, MonsterInstance monster, Spell spell, Character character, String description) throws InvalidSpell {
		if(validSelfSpell(spell)) throw new InvalidSpell("Attempt monster to cast spell "+spell.getNiceName()+" on character");
		return new DungeonEvent(-1, dungeon, DungeonEventType.MONSTER_SPELL, character, monster, spell, description);		
	}
	
	/**
	 * Create an event for a monster summoning another monster
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param monster		Monster doing the summoning	
	 * @param description	Details of the summon, starting with the summoned monsters name
	 * @return
	 */
	public static DungeonEvent monsterSummon(Dungeon dungeon, MonsterInstance monster, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.MONSTER_SUMMON, null, monster, null, description);		
	}

	/**
	 * Monster got healed
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param monster		Monster getting healed
	 * @param description	Description of heal (amount healed)
	 * @return
	 */
	public static DungeonEvent monsterHeal(Dungeon dungeon, MonsterInstance monster, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.MONSTER_HEAL, null, monster, null, description);
	}
	
	/**
	 * A monster died
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param monster		Monster dying
	 * @param description	Description of death
	 * @return
	 */
	public static DungeonEvent monsterDied(Dungeon dungeon, MonsterInstance monster, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.MONSTER_DIED, null, monster, null, description);
	}

	/**
	 * Character buff applied
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param monster		Monster being buffed
	 * @param spell			The spell that buffed
	 * @param description	Description of buff
	 * 
	 * @return
	 */
	public static DungeonEvent monsterBuff(Dungeon dungeon, MonsterInstance monster, Spell spell, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.MONSTER_BUFF, null, monster, spell, description);
	}

	/**
	 * Character debuff applied
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param monster		Monster being debuffed
	 * @param spell			The spell that debuffed
	 * @param description	Description of debuff
	 * 
	 * @return
	 */
	public static DungeonEvent monsterDebuff(Dungeon dungeon, MonsterInstance monster, Spell spell, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.MONSTER_DEBUFF, null, monster, spell, description);
	}

	/**
	 * 
	 * 
	 * @param dungeon		The dungeon this event occurred in
	 * @param monster		Monster dying
	 * @param description	Description of damage
	 * @return
	 */
	public static DungeonEvent monsterTakeDamage(Dungeon dungeon, MonsterInstance monster, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.MONSTER_TAKE_DAMAGE, null, monster, null, description);
	}

	/**
	 * Just info
	 * 
	 * @param dungeon
	 * @param description
	 * @return
	 */
	public static DungeonEvent info(Dungeon dungeon, String description) {
		return new DungeonEvent(-1, dungeon, DungeonEventType.INFO, null, null, null, description);
	}

	public Integer getId() {
		return id;
	}

	public Character getCharacter() {
		return effectedCharacter;
	}
	
	public Integer getMonsterId() {
		return monsterId;
	}

	public Integer getCharacterHealth() {
		return characterHealth;
	}

	public Integer getCharacterMana() {
		return characterMana;
	}

	public Integer getMonsterTypeId() {
		return monsterTypeId;
	}

	public Integer getMonsterHealth() {
		return monsterHealth;
	}

	public Integer getMonsterMana() {
		return monsterMana;
	}

	public DungeonEventType getEventType() {
		return eventType;
	}

	public Integer getSpellId() {
		return spellId;
	}

	public int getEventOrder() {
		return eventOrder;
	}

	public void setEventOrder(int eventOrder) {
		this.eventOrder = eventOrder;
	}

	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		switch(eventType) {
		case CHARACTER_ATTACK:
			sb.append(effectedCharacter.getName());
			sb.append(" attacked ");
			sb.append(Monster.fromId(monsterId).getNiceName());
			sb.append(". ");
			break;
		case CHARACTER_HEAL:
			sb.append(effectedCharacter.getName());
			sb.append(" got healed ");
			break;
		case CHARACTER_DIED:
			sb.append(effectedCharacter.getName());
			sb.append(" died. ");
			break;
		case CHARACTER_SPELL:
			sb.append(effectedCharacter.getName());
			sb.append(" cast ");
			sb.append(Spell.fromId(spellId).getNiceName());
			sb.append(" on ");
			if(monsterId != null) {
				sb.append(Monster.fromId(monsterId).getNiceName());
			} else {
				sb.append("self");
			}
			break;
		case CHARACTER_TAKE_DAMAGE:
			sb.append(effectedCharacter.getName());
			sb.append(" took ");
			break;
		case CHARACTER_BUFF:
			sb.append(effectedCharacter.getName());
			sb.append(" was buffed with ");
			sb.append(Spell.fromId(spellId).getNiceName());
			sb.append(" ");
			break;
		case CHARACTER_DEBUFF:
			sb.append(effectedCharacter.getName());
			sb.append(" was debuffed with ");
			sb.append(Spell.fromId(spellId).getNiceName());
			sb.append(" ");
			break;
			
		case MONSTER_ATTACK:
			sb.append(Monster.fromId(monsterId).getNiceName());
			sb.append(" attacked ");
			sb.append(effectedCharacter.getName());
			sb.append(" ");
			break;
		case MONSTER_HEAL:
			sb.append(Monster.fromId(monsterId).getNiceName());
			sb.append(" got healed ");
			break;
		case MONSTER_DIED:
			sb.append(Monster.fromId(monsterId).getNiceName());
			sb.append(" died. ");
			break;
		case MONSTER_TAKE_DAMAGE:
			sb.append(Monster.fromId(monsterId).getNiceName());
			sb.append(" took ");
			break;
		case MONSTER_SPELL:
			sb.append(Monster.fromId(monsterId).getNiceName());
			sb.append(" cast ");
			sb.append(Spell.fromId(spellId).getNiceName());
			sb.append(" on ");
			if(effectedCharacter != null) {
				sb.append(effectedCharacter.getName());
			} else {
				sb.append("self");
			}
			break;
		case MONSTER_SUMMON:
			sb.append(Monster.fromId(monsterId).getNiceName());
			sb.append(" summoned ");
			break;
		case MONSTER_BUFF:
			sb.append(Monster.fromId(monsterId).getNiceName());
			sb.append(" was buffed with ");
			sb.append(Spell.fromId(spellId).getNiceName());
			sb.append(" ");
			break;
		case MONSTER_DEBUFF:
			sb.append(Monster.fromId(monsterId).getNiceName());
			sb.append(" was debuffed with ");
			sb.append(Spell.fromId(spellId).getNiceName());
			sb.append(" ");
			break;
		case INFO: case NONE:
			break;
		}
		
		sb.append(description);
		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String getAffectedMobsDetails(int partySize) {
		StringBuffer sb = new StringBuffer();
		switch(eventType) {
		case CHARACTER_ATTACK:
			getMonsterHealthString(sb, partySize);
			break;
		case CHARACTER_HEAL:
			getCharacterHealthString(sb);
			break;
		case CHARACTER_DIED:
			break;
		case CHARACTER_SPELL:
			getCharacterManaString(sb);
			break;
		case CHARACTER_TAKE_DAMAGE:
			getCharacterHealthString(sb);
			break;
		case CHARACTER_BUFF:
			getCharacterManaString(sb);
			break;
		case CHARACTER_DEBUFF:
			getMonsterManaString(sb);
			break;			
		case MONSTER_ATTACK:
			getCharacterHealthString(sb);
			break;
		case MONSTER_HEAL:
			getMonsterHealthString(sb, partySize);
			break;
		case MONSTER_DIED:
			break;
		case MONSTER_TAKE_DAMAGE:
			getMonsterHealthString(sb, partySize);
			break;
		case MONSTER_SPELL:
			getMonsterManaString(sb);
			break;
		case MONSTER_SUMMON:
			getMonsterManaString(sb);
			break;
		case MONSTER_BUFF:
			getMonsterManaString(sb);
			break;
		case MONSTER_DEBUFF:
			getCharacterManaString(sb);
			break;
		case INFO: case NONE:
			break;
		}
		
		return sb.toString();
	}

	// Helper functions
	private void getCharacterHealthString(StringBuffer sb) {
		if(effectedCharacter == null) {
			sb.append("They have");
		} else {
			sb.append(effectedCharacter.getName());
			sb.append(" has ");
		}
		sb.append(characterHealth<0?0:characterHealth);
		sb.append(" of ");
		sb.append(characterMaxHealth);
		sb.append(" health left.");
	}

	private void getCharacterManaString(StringBuffer sb) {
		if(effectedCharacter == null) {
			sb.append("They have");
		} else {
			sb.append(effectedCharacter.getName());
			sb.append(" has ");
		}
		sb.append(characterMana);
		sb.append(" mana left.");
	}

	private void getMonsterHealthString(StringBuffer sb, int partySize) {
		Monster monster = Monster.fromId(monsterId);
		if(monsterTypeId == null) monsterTypeId = 0;
		MonsterType monsterType = MonsterType.fromId(monsterTypeId);
		sb.append(monster.getNiceName());
		sb.append(" has ");
		sb.append(monsterHealth<0?0:monsterHealth);
		sb.append(" of ");
		sb.append(MonsterInstance.calculateMaxHealth(monster, monsterType, dungeon.getLevel(), partySize));
		sb.append(" health left.");
	}

	private void getMonsterManaString(StringBuffer sb) {
		if(monsterId == null) {
			logger.error("Monster id NULL in getMonsterManaString dungeon event {}", id);
			return;
		}
		Monster thisMonster = Monster.fromId(monsterId);
		if(thisMonster == null) {
			logger.error("Unknown monster id {}, dungeon event {}", monsterId, id);
			return;
		}
		sb.append(Monster.fromId(monsterId).getNiceName());
		sb.append(" has ");
		sb.append(monsterMana);
		sb.append(" mana left.");
	}

	private static boolean validSelfSpell(Spell spell) throws InvalidSpell {
		switch (spell) {
		case Buff: case Fast: case Encourage: case Heal: case Poem: case Renew: case Sing: case Story:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Add the dungeon event to the list, if it is not null
	 * 
	 * @param dungeonEvents
	 * @param dungeonEvent
	 */
	public static void addDungeonEvent(List<DungeonEvent> dungeonEvents, DungeonEvent newDungeonEvent) {
		if(newDungeonEvent != null) {
			dungeonEvents.add(newDungeonEvent);
		}
	}
	
	/**
	 * Add the dungeon events to the list, if it is not null and not empty
	 * 
	 * @param dungeonEvents
	 * @param dungeonEvent
	 */
	public static void addDungeonEvents(List<DungeonEvent> dungeonEvents, List<DungeonEvent> newDungeonEvents) {
		if(newDungeonEvents != null && !newDungeonEvents.isEmpty()) {
			dungeonEvents.addAll(newDungeonEvents);
		}
	}

	@Override
	public String toString() {
		return toString(true, false);
	}
	
	public String toString(boolean inFull, boolean showNice) {
		StringBuilder sb = new StringBuilder("DungeonEvent [");
		if(inFull) {
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
			sb.append("eventType=");
			sb.append(eventType);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("eventOrder=");
			sb.append(eventOrder);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("effectedCharacter=");
			sb.append(effectedCharacter==null?"null":effectedCharacter.getName());
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("characterMaxHealth=");
			sb.append(characterMaxHealth);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("characterHealth=");
			sb.append(characterHealth);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("characterMana=");
			sb.append(characterMana);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("monsterId="
					+ monsterId);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("monsterTypeId=");
			sb.append(monsterTypeId);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("monsterHealth=");
			sb.append(monsterHealth);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("monsterMana="
					+ monsterMana);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("spellId=");
			sb.append(spellId);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}		
			sb.append("description=");
			sb.append(description);
		} else {
			sb.append(getDescription());
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}
			sb.append(getAffectedMobsDetails(dungeon.getPartySize()));
		}
		if(showNice) {
			sb.append("\n");
		}
		sb.append("]");
		
		return sb.toString();
	}
}
