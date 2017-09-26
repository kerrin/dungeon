package org.kerrin.dungeon.enums;

import java.util.ArrayList;
import java.util.List;

import org.kerrin.dungeon.exception.InvalidSpell;
import org.kerrin.dungeon.model.AttackResult;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Damage;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonEvent;
import org.kerrin.dungeon.model.MonsterInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All the possible character classes
 * 
 * @author Kerrin
 *
 */
public enum Spell {
	UNKNOWN(	0, 	"Unknown!"),
	// 			id, niceName, minimumLevel, damageType, [effectType,] 	castCost, damage, castCoolDown
	Firebolt(	1, 	"Firebolt", 1, 	DamageType.FIRE, 					10, 		5, 		5 ), 
	Fireball(	2, 	"Fireball", 10, DamageType.FIRE, 
			10, 		18, 	20), 
	Meteor(		3, 	"Meteor", 	20, DamageType.FIRE, 					20, 		25, 	20), 
	Volcano(	4, 	"Volcano", 	50, DamageType.FIRE, 					20, 		45, 	30), 
	Hydra(		5, 	"Hydra", 	30, DamageType.FIRE, 					25, 		20, 	10),
	Firewall(	6, 	"Firewall", 40, DamageType.FIRE, 					25, 		30, 	15),
	Icebolt(	7, 	"Icebolt", 	1, 	DamageType.COLD, 					15, 		8, 		25),
	Snowball(	8, 	"Snowball", 10, DamageType.COLD, 					20, 		12, 	20),
	Blizzard(	9, 	"Blizzard", 30, DamageType.COLD, 					30, 		25, 	30),
	Glacia(		10, "Glacia", 	50, DamageType.COLD, 					40, 		30, 	25),
	Snowman(	11, "Snowman", 	20, DamageType.COLD, 					40, 		20, 	30), 
	PoisonDart(	12, "Poison Dart",1,DamageType.POISON, 					20, 		20,		16), 
	Fart(		13, "Fart", 	10, DamageType.POISON, 					30, 		17, 	20), 
	PoisonGas(	14, "Poison Gas",50,DamageType.POISON, 					40, 		45, 	27), 
	Sting(		15, "Sting", 	20, DamageType.POISON, 					30, 		40, 	4),
	Smack(		16, "Smack", 	1, 	DamageType.MELEE, 					10, 		7, 		10),
	Smash(		17, "Smash", 	10, DamageType.MELEE, 					20, 		30, 	20),
	Boulder(	18, "Boulder", 	20, DamageType.MELEE, 					25, 		30, 	30),
	Slash(		19, "Slash", 	30, DamageType.MELEE, 					30, 		22, 	10),
	Hack(		20, "Hack", 	40, DamageType.MELEE, 					10, 		60, 	20),
	Stab(		21, "Stab", 	50, DamageType.PIERCING, 				20, 		52, 	25), 
	Stun(		22, "Stun", 	15, DamageType.MELEE, 					10, 		10, 	20), 
	Slice(		23, "Slice", 	25, DamageType.MELEE, 					40, 		110, 	20), 
	Swing(		24, "Swing", 	30, DamageType.MELEE, 					20, 		30, 	20), 
	Sever(		25, "Sever", 	45, DamageType.MELEE, 					50, 		180, 	30),
	Shoot(		26, "Shoot", 	30, DamageType.PIERCING, 				25, 		32, 	15),
	Dart(		27, "Dart", 	1, 	DamageType.PIERCING, 				11, 		7, 		20),
	Electricute(28, "Electricute",10,DamageType.ELECTRIC, 				12, 		12, 	15),
	Sizzle(		29, "Sizzle", 	1, 	DamageType.ELECTRIC, 				14, 		4, 		15),
	Tazer(		30, "Tazer", 	30, DamageType.ELECTRIC, 				40, 		30, 	20),
	Flood(		31, "Flood", 	30, DamageType.WATER, 					40, 		22, 	10), 
	Drown(		32, "Drown", 	35, DamageType.WATER, 					25, 		160, 	30), 
	Soak(		33, "Soak", 	10, DamageType.WATER, 					16, 		22, 	10), 
	Rain(		34, "Rain", 	20, DamageType.WATER, 					22, 		22, 	15), 
	AcidRain(	35, "AcidRain", 30, DamageType.ACID, 					25, 		30, 	20),
	Tsunami(	36, "Tsunami", 	50, DamageType.WATER, 					40, 		50, 	30),
	Wave(		37, "Wave", 	30, DamageType.WATER, 					25, 		37, 	28),
	Heal(		38, "Heal", 	10, DamageType.HOLY, 					50, 		37, 	28),
	Confuse(	39, "Confuse", 	40, DamageType.STUN, 					50, 		10, 	15),
	Daze(		40, "Daze", 	20, DamageType.MELEE, 					20, 		11, 	10),
	Teleport(	41, "Teleport", 50, (DamageType)null,  					20, 		10, 	15), 
	Dislocate(	42, "Dislocate",30, DamageType.MELEE, 					40, 		100, 	15), 
	Relocate(	43, "Relocate", 40, (DamageType)null, 					18, 		10, 	10), 
	Dash(		44, "Dash", 	20, (DamageType)null,  					12, 		5, 		5), 
	Swarm(		45, "Swarm", 	40, DamageType.POISON, 					50, 		38, 	25),
	Buff(		46, "Buff", 	10, (DamageType)null,   				80, 		38, 	30),
	Invisibility(47,"Invisibility",50,(DamageType)null,   				60, 		30, 	15),
	Throw(		48, "Throw", 	10, DamageType.MELEE, 					28, 		15, 	5),
	Wet(		49, "Wet", 		1, DamageType.WATER, 					12,			15, 	10),
	Burn(		50, "Burn", 	15, DamageType.FIRE, 					30,			95, 	25),
	Freeze(		51, "Freeze", 	15, DamageType.COLD, 					30,			30, 	30),
	Blind(		52, "Blind", 	35, DamageType.HOLY, 					30, 		25, 	25),
	Sing(		53, "Sing", 	1, 	(DamageType)null,   				6, 			7, 		10),
	Poem(		54, "Poem", 	10, (DamageType)null,   				12, 		20, 	8),
	Story(		55, "Story", 	20, (DamageType)null,   				18, 		50, 	25),
	Encourage(	56, "Encourage",40, (DamageType)null,   				20, 		70, 	25),
	Threaten(	57, "Threaten", 30, (DamageType)null,   				20, 		45, 	15),
	VileAcid(	58, "Vile Acid",20, DamageType.ACID, 					40, 		25, 	20),
	Renew(		59, "Renew", 	20, DamageType.HOLY,    				24, 		25, 	7),
	Condem(		60, "Condem", 	40, DamageType.HOLY, 					25, 		60, 	20),
	Arrow(		61, "Arrow", 	1, 	DamageType.PIERCING, 				20, 		10, 	20),
	Lightning(	62, "Lightning",20, DamageType.ELECTRIC, 				40, 		15, 	30),
	Slow(		63, "Slow", 	20, (DamageType)null,  					40, 		-10, 	20),
	Fast(		64, "Fast", 	50, (DamageType)null,  					40, 		10, 	20),
	Disorientate(65,"Disorientate",30, (DamageType)null,  				40, 		-5, 	20),
	ManaRegen(	66, "Mana Renew",30,(DamageType)null,    				40, 		5, 		20),
	//		id, niceName, minimumLevel, damageType, [effectType,] 		castCost, damage, castCoolDown
	;
	
	private static final Logger logger = LoggerFactory.getLogger(Spell.class);
	
	/** Chance monsters that do splash damage cause splash damage */
	private static final int MONSTER_BASE_SPLASH_CHANCE = 50;
	/** Percentage of original damage the monster splash damage is */
	private static final int MONSTER_SPLASH_DAMAGE = 50;
	/** Chance monsters that do stuns to stun */
	private static final int MONSTER_BASE_STUN_CHANCE = 10;
	/** How long should monster stuns last */
	private static final int MONSTER_STUN_TIME = 10;
	
	private int id;
	private String niceName;
	/** The minimum level this can be used by characters and monsters */
	private int minimumLevel;
	/** The type of damage this does */
	private DamageType damageType;
	/** The type of effect this does */
	private List<EquipmentAttribute> effectTypes;
	/** The damage this does before level multiplier */
	private int damage;
	/** The cost to cast the spell */
	private int castCost;
	/** How often it can be cast */
	public final int castCoolDown;
	
	// We need to statically initialise the effect types, else the Spell is not always initialised correctly 
	static {
		Firebolt.effectTypes = new ArrayList<EquipmentAttribute>();
		Firebolt.effectTypes.add(EquipmentAttribute.RANGE); 

		Fireball.effectTypes = new ArrayList<EquipmentAttribute>();
		Fireball.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Fireball.effectTypes.add(EquipmentAttribute.RANGE); 

		Meteor.effectTypes = new ArrayList<EquipmentAttribute>();
		Meteor.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Meteor.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Volcano.effectTypes = new ArrayList<EquipmentAttribute>();
		Volcano.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Volcano	.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Hydra.effectTypes = new ArrayList<EquipmentAttribute>();
		Hydra.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Hydra.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Firewall.effectTypes = new ArrayList<EquipmentAttribute>();
		Firewall.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Firewall.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Icebolt.effectTypes = new ArrayList<EquipmentAttribute>();
		Icebolt.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Snowball.effectTypes = new ArrayList<EquipmentAttribute>();
		Snowball.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Snowball.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Blizzard.effectTypes = new ArrayList<EquipmentAttribute>();
		Blizzard.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Blizzard.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Glacia.effectTypes = new ArrayList<EquipmentAttribute>();
		Glacia.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Glacia.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Snowman.effectTypes = new ArrayList<EquipmentAttribute>();
		Snowman.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Snowman.effectTypes.add(EquipmentAttribute.RANGE); 
		
		PoisonDart.effectTypes = new ArrayList<EquipmentAttribute>();
		PoisonDart.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Fart.effectTypes = new ArrayList<EquipmentAttribute>();
		Fart.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		
		PoisonGas.effectTypes = new ArrayList<EquipmentAttribute>();
		PoisonGas.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		
		Sting.effectTypes = new ArrayList<EquipmentAttribute>();
		
		Smack.effectTypes = new ArrayList<EquipmentAttribute>();
		Smash.effectTypes = new ArrayList<EquipmentAttribute>();
		
		Boulder.effectTypes = new ArrayList<EquipmentAttribute>();
		Boulder.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Boulder.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Slash.effectTypes = new ArrayList<EquipmentAttribute>();
		Slash.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Slash.effectTypes.add(EquipmentAttribute.BLEED);
		
		Hack.effectTypes = new ArrayList<EquipmentAttribute>();
		Hack.effectTypes.add(EquipmentAttribute.BLEED);
		
		Stab.effectTypes = new ArrayList<EquipmentAttribute>();
		Stab.effectTypes.add(EquipmentAttribute.DAMAGE_OVER_TIME); 
		Stab.effectTypes.add(EquipmentAttribute.BLEED);
		
		Stun.effectTypes = new ArrayList<EquipmentAttribute>();
		Stun.effectTypes.add(EquipmentAttribute.STUN_CHANCE); 
		
		Slice.effectTypes = new ArrayList<EquipmentAttribute>();
		Slice.effectTypes.add(EquipmentAttribute.DAMAGE_OVER_TIME); 
		Slice.effectTypes.add(EquipmentAttribute.BLEED);
		
		Swing.effectTypes = new ArrayList<EquipmentAttribute>();
		Swing.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		
		Sever.effectTypes = new ArrayList<EquipmentAttribute>();
		Sever.effectTypes.add(EquipmentAttribute.DAMAGE_OVER_TIME); 
		Sever.effectTypes.add(EquipmentAttribute.BLEED); 
		
		Shoot.effectTypes = new ArrayList<EquipmentAttribute>();
		Shoot.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Dart.effectTypes = new ArrayList<EquipmentAttribute>();
		Dart.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Electricute.effectTypes = new ArrayList<EquipmentAttribute>();
		
		Sizzle.effectTypes = new ArrayList<EquipmentAttribute>();
		
		Tazer.effectTypes = new ArrayList<EquipmentAttribute>();
		Tazer.effectTypes.add(EquipmentAttribute.STUN_CHANCE); 
		Tazer.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Flood.effectTypes = new ArrayList<EquipmentAttribute>();
		Flood.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Flood.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Drown.effectTypes = new ArrayList<EquipmentAttribute>();
		Drown.effectTypes.add(EquipmentAttribute.DAMAGE_OVER_TIME); 
		
		Soak.effectTypes = new ArrayList<EquipmentAttribute>();
		
		Rain.effectTypes = new ArrayList<EquipmentAttribute>();
		Rain.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Rain.effectTypes.add(EquipmentAttribute.RANGE); 
		
		AcidRain.effectTypes = new ArrayList<EquipmentAttribute>();
		AcidRain.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		AcidRain.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Tsunami.effectTypes = new ArrayList<EquipmentAttribute>();
		Tsunami.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Tsunami.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Wave.effectTypes = new ArrayList<EquipmentAttribute>();
		Wave.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Wave.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Heal.effectTypes = new ArrayList<EquipmentAttribute>();
		Heal.effectTypes.add(EquipmentAttribute.HEAL);
		
		Confuse.effectTypes = new ArrayList<EquipmentAttribute>();
		Confuse.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Confuse.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Daze.effectTypes = new ArrayList<EquipmentAttribute>();
		Daze.effectTypes.add(EquipmentAttribute.STUN_CHANCE); 
		
		Teleport.effectTypes = new ArrayList<EquipmentAttribute>();
		Teleport.effectTypes.add(EquipmentAttribute.SPEED); 
		Teleport.effectTypes.add(EquipmentAttribute.RANGE); 

		Dislocate.effectTypes = new ArrayList<EquipmentAttribute>();
		Dislocate.effectTypes.add(EquipmentAttribute.DAMAGE_OVER_TIME); 
		
		Relocate.effectTypes = new ArrayList<EquipmentAttribute>();
		Relocate.effectTypes.add(EquipmentAttribute.STUN_CHANCE); 
		Relocate.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Dash.effectTypes = new ArrayList<EquipmentAttribute>();
		Dash.effectTypes.add(EquipmentAttribute.SPEED); 
		
		Swarm.effectTypes = new ArrayList<EquipmentAttribute>();
		Swarm.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Swarm.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Buff.effectTypes = new ArrayList<EquipmentAttribute>();
		Buff.effectTypes.add(EquipmentAttribute.ALL_RESIST); 
		
		Invisibility.effectTypes = new ArrayList<EquipmentAttribute>();
		Invisibility.effectTypes.add(EquipmentAttribute.ALL_RESIST); 
		
		Throw.effectTypes = new ArrayList<EquipmentAttribute>();
		Throw.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Wet.effectTypes = new ArrayList<EquipmentAttribute>();
		Wet.effectTypes.add(EquipmentAttribute.DAMAGE_OVER_TIME); 
		
		Burn.effectTypes = new ArrayList<EquipmentAttribute>();
		Burn.effectTypes.add(EquipmentAttribute.DAMAGE_OVER_TIME); 
		
		Freeze.effectTypes = new ArrayList<EquipmentAttribute>();
		Freeze.effectTypes.add(EquipmentAttribute.DAMAGE_OVER_TIME); 
		
		Blind.effectTypes = new ArrayList<EquipmentAttribute>();
		Blind.effectTypes.add(EquipmentAttribute.STUN_CHANCE); 
		
		Sing.effectTypes = new ArrayList<EquipmentAttribute>();
		Sing.effectTypes.add(EquipmentAttribute.ALL_RESIST);
		
		Poem.effectTypes = new ArrayList<EquipmentAttribute>();
		Poem.effectTypes.add(EquipmentAttribute.ALL_RESIST);
		
		Story.effectTypes = new ArrayList<EquipmentAttribute>();
		Story.effectTypes.add(EquipmentAttribute.ALL_RESIST);
		
		Encourage.effectTypes = new ArrayList<EquipmentAttribute>();
		Encourage.effectTypes.add(EquipmentAttribute.ALL_RESIST);
		
		Threaten.effectTypes = new ArrayList<EquipmentAttribute>();
		Threaten.effectTypes.add(EquipmentAttribute.ALL_RESIST); 
		
		VileAcid.effectTypes = new ArrayList<EquipmentAttribute>();
		VileAcid.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		
		Renew.effectTypes = new ArrayList<EquipmentAttribute>();
		Renew.effectTypes.add(EquipmentAttribute.HEALTH_REGEN);
		
		Condem.effectTypes = new ArrayList<EquipmentAttribute>();
		Condem.effectTypes.add(EquipmentAttribute.STUN_CHANCE); 
		Condem.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Arrow.effectTypes = new ArrayList<EquipmentAttribute>();
		Arrow.effectTypes.add(EquipmentAttribute.RANGE);
		
		Lightning.effectTypes = new ArrayList<EquipmentAttribute>();
		Lightning.effectTypes.add(EquipmentAttribute.SPLASH_DAMAGE); 
		Lightning.effectTypes.add(EquipmentAttribute.RANGE); 
		
		Slow.effectTypes = new ArrayList<EquipmentAttribute>();
		Slow.effectTypes.add(EquipmentAttribute.SPEED); 
		
		Fast.effectTypes = new ArrayList<EquipmentAttribute>();
		Fast.effectTypes.add(EquipmentAttribute.SPEED); 
		
		Disorientate.effectTypes = new ArrayList<EquipmentAttribute>();
		Disorientate.effectTypes.add(EquipmentAttribute.SPEED); 
		
		ManaRegen.effectTypes = new ArrayList<EquipmentAttribute>();
		ManaRegen.effectTypes.add(EquipmentAttribute.MANA_REGEN);
	}
	
	private Spell(int id, String niceName) {
		this(id, niceName, 0, DamageType.NONE, 0, 0, 0);
	}
	/**
	 * Constructor for spell with no restrictions
	 * 
	 * @param id
	 * @param niceName
	 * @param minimumLevel
	 * @param damageType
	 * @param damage
	 * @param castCost
	 * @param castCoolDown
	 */
	private Spell(int id, String niceName, int minimumLevel, 
			DamageType damageType, int castCost, int damage, int castCoolDown) {
		this.id = id;
		this.niceName = niceName;
		this.minimumLevel = minimumLevel;
		this.damageType = damageType;
		this.damage = damage;
		this.castCost = castCost;
		this.castCoolDown = castCoolDown;
	}

	/**
	 * Get the identifier
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the nice name
	 * 
	 * @return
	 */
	public String getName() {
		return niceName;
	}

	public String getNiceName() {
		return niceName;
	}
	
	public int getMinimumLevel() {
		return minimumLevel;
	}
	
	public DamageType getDamageType() {
		return damageType;
	}
	
	public List<EquipmentAttribute> getEffectTypes() {
		return effectTypes;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getCastCost() {
		return castCost;
	}
	
	/**
	 * Get the cool down length
	 * @return
	 */
	public int getCastCoolDown() {
		return castCoolDown;
	}
	
	/**
	 * Get a character class from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static Spell fromId(int id) {
		for(Spell thisCharClass:values()) {
			if(thisCharClass.id == id) return thisCharClass;
		}
		return UNKNOWN;
	}
	
	/**
	 * Cast spell on character (if aggressive) or monster is positive
	 * 
	 * @param dungeon	The dungeon the spell is cast in
	 * @param character	Character being cast on (if aggressive)
	 * @param monster	Monster casting spell
	 * @param dungeonEvents	All the dungeon events so far
	 * 
	 * @return
	 */
	public AttackResult castOn(Dungeon dungeon, Character character, MonsterInstance monster,
			List<DungeonEvent> dungeonEvents) {
		try {
			String description = "";
			int levelDamage = damage * monster.level;
			// One spell special case
			if(damageType != null) {
				if(damageType == DamageType.STUN) {
					if(stun()) {
						if(character.stun(MONSTER_STUN_TIME, monster.level, dungeon, dungeonEvents)) {
							description = " for "+MONSTER_STUN_TIME+" rounds.";
							dungeonEvents.add(DungeonEvent.characterDebuff(dungeon, character, monster, this, description));
						} else {
							description = character.getName()+" resisted stun for "+MONSTER_STUN_TIME+" rounds from "+
									monster.getMonster().getNiceName()+" using spell "+this.getNiceName()+".";
							dungeonEvents.add(DungeonEvent.info(dungeon, description));							
						}
					}
				}
				damageType = damageType.getRealDamageType();
			}
			AttackResult attackResult = new AttackResult(character, monster);
			if(effectTypes != null) {
				for(EquipmentAttribute effectType:effectTypes) {
					switch(effectType) {
					case SPLASH_DAMAGE:
						if(splash()) {
							int splashDamage = (levelDamage * MONSTER_SPLASH_DAMAGE) / 100;
							if(splashDamage < 1) splashDamage = 1;
							description = " with "+splashDamage+" splash damage.";
							dungeonEvents.add(DungeonEvent.monsterCastSpell(dungeon, monster, this, character, description));
							attackResult.setCharacterSplashDamage(new Damage(splashDamage, damageType));
						}
						break;
					case DAMAGE_OVER_TIME: 
						description = " for damage over time.";
						dungeonEvents.add(DungeonEvent.characterTakeDamage(dungeon, character, description));
						DungeonEvent.addDungeonEvent(dungeonEvents, 
								character.takeDamageOverTime(levelDamage, damageType, effectTypes, monster.level, dungeon));
						break;
					case BLEED: 
						description = " for bleed damage.";
						dungeonEvents.add(DungeonEvent.characterTakeDamage(dungeon, character, description));
						DungeonEvent.addDungeonEvent(dungeonEvents, 
								character.takeDamageOverTime(levelDamage, damageType, 
									new ArrayList<EquipmentAttribute>() {{ add(EquipmentAttribute.BLEED);}}, 
									monster.level, dungeon));
						break;
					case HEAL: 
						description = " to heal.";
						dungeonEvents.add(DungeonEvent.monsterCastSpell(dungeon, monster, this, description));
						attackResult.setMonsterHeal(levelDamage);
						break;
					case HEALTH_REGEN: 
						description = " to regenerate health.";
						dungeonEvents.add(DungeonEvent.monsterCastSpell(dungeon, monster, this, description));
						attackResult.setMonsterBuff(org.kerrin.dungeon.enums.Buff.HEALTH_REGEN, damage);
						break;
					case MANA_REGEN:
						description = " to regenerate mana.";
						dungeonEvents.add(DungeonEvent.monsterCastSpell(dungeon, monster, this, description));
						attackResult.setMonsterBuff(org.kerrin.dungeon.enums.Buff.MANA_REGEN, damage);
						break;
					case STUN_CHANCE:
						if(stun()) {
							if(character.stun(MONSTER_STUN_TIME, monster.level, dungeon, dungeonEvents)) {
								description = " for "+MONSTER_STUN_TIME+" rounds.";
								dungeonEvents.add(DungeonEvent.monsterDebuff(dungeon, monster, this, description));
							} else {
								description = character.getName()+" resisted stun for "+MONSTER_STUN_TIME+" rounds from "+
										monster.getMonster().getNiceName()+" using spell "+this.getNiceName()+".";
								dungeonEvents.add(DungeonEvent.info(dungeon, description));							
							}
						}
						break;
					case RANGE:
						description = " range damage.";
						dungeonEvents.add(DungeonEvent.monsterCastSpell(dungeon, monster, this, character, description));
						// Reduces incoming damage
						attackResult.setMonsterBuff(org.kerrin.dungeon.enums.Buff.ALL_RESIST, 1);
						break;
					case SPEED:
						if(damage > 0) {
							description = " for "+damage+" speed boost.";
							dungeonEvents.add(DungeonEvent.monsterBuff(dungeon, monster, this, description));
							attackResult.setMonsterSpeed(damage);
						} else {
							description = " for "+damage+" slow.";
							dungeonEvents.add(DungeonEvent.characterDebuff(dungeon, character, monster, this, description));
							attackResult.setCharacterSpeed(damage);
						}
						break;
					case ALL_RESIST:
						description = " for "+damage+" all resist.";
						dungeonEvents.add(DungeonEvent.monsterBuff(dungeon, monster, this, description));
						attackResult.setMonsterBuff(org.kerrin.dungeon.enums.Buff.ALL_RESIST, damage);
						break;
					default:
						break;
					}
				}
			} else {
				dungeonEvents.add(DungeonEvent.monsterCastSpell(dungeon, monster, this, character, description));			
			}
			
			if(damage != 0) {
				if(effectTypes != null) {
					boolean effectDone = false;
					for(EquipmentAttribute effectType:effectTypes) {
						if(effectType == EquipmentAttribute.DAMAGE_OVER_TIME || effectType == EquipmentAttribute.HEAL || 
								effectType == EquipmentAttribute.MANA_REGEN || effectType == EquipmentAttribute.HEALTH_REGEN || 
								effectType == EquipmentAttribute.SPEED || effectType == EquipmentAttribute.ALL_RESIST) {
							// Do nothing here, dealt with elsewhere.
							effectDone = true;
						}
					}
					if(effectDone) {
						// Good, nothing to worry about
					} else if (damageType == null) {
						logger.error("No damage type for spell {}", this);
					} else {
						List<DungeonEvent> damageEvents = 
								character.takeDamage(levelDamage, damageType, effectTypes, monster.level, dungeon, attackResult);
						description = levelDamage+" of "+damageType.getNiceName()+".";
						dungeonEvents.add(DungeonEvent.characterTakeDamage(dungeon, character, description));
						DungeonEvent.addDungeonEvents(dungeonEvents, damageEvents);
					}
				} else {
					List<DungeonEvent> damageEvents = character.takeDamage(levelDamage, damageType, effectTypes, monster.level, dungeon, attackResult);
					description = levelDamage+" of "+damageType.getNiceName()+".";
					dungeonEvents.add(DungeonEvent.characterTakeDamage(dungeon, character, description));
					DungeonEvent.addDungeonEvents(dungeonEvents, damageEvents);
				}
			} else {
				logger.debug("No damage for {} on {}", this, character);
			}			
			attackResult.setMonster(monster);
			attackResult.setCharacter(character);
			attackResult.setCastSpell(this);
			return attackResult;
		} catch (NullPointerException e) {
			throw e;
		} catch (InvalidSpell e) {
			logger.error("Spell type error: {}", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Cast spell on monster
	 * If a friendly spell, actually cast on character
	 * 
	 * @param dungeon				The dungeon the spell was cast in
	 * @param monster				The monster the spell is cast on (if aggressive spell)
	 * @param castingCharacter		The character casting the spell
	 * @param casterLevel			The level of the caster (in case it is a buff spell)
	 * @param characterEquipment	The equipment the character is wearing
	 * @param dungeonEvents			The dungeon events so far
	 * @return
	 */
	public AttackResult castOn(Dungeon dungeon, MonsterInstance monster, Character castingCharacter,
			CharacterEquipment characterEquipment, List<DungeonEvent> dungeonEvents) {
		try {
			String description = "";
			AttackResult attackResult = new AttackResult(castingCharacter, monster);
			int levelDamage = damage * castingCharacter.getLevel();
			if(effectTypes != null) {
				for(EquipmentAttribute effectType:effectTypes) {
					switch(effectType) {
					case SPLASH_DAMAGE:
						if(splash(characterEquipment)) {
							int splashDamage = (levelDamage * MONSTER_SPLASH_DAMAGE) / 100;
							if(splashDamage < 1) splashDamage = 1;
							description = " has "+splashDamage+" splash damage.";
							dungeonEvents.add(DungeonEvent.characterCastSpell(dungeon, castingCharacter, this, monster, description));
							attackResult.setMonsterSplashDamage(new Damage(splashDamage, damageType));
						}
						break;
					case DAMAGE_OVER_TIME: 
						description = " for damage over time.";
						dungeonEvents.add(DungeonEvent.characterCastSpell(dungeon, castingCharacter, this, monster, description));					
						DungeonEvent.addDungeonEvent(dungeonEvents, 
								monster.takeDamageOverTime(levelDamage, damageType, effectTypes, castingCharacter.getLevel(), dungeon));
						break;
					case BLEED:
						description = " for "+damage+" bleed.";
						dungeonEvents.add(DungeonEvent.characterCastSpell(dungeon, castingCharacter, this, monster, description));					
						DungeonEvent.addDungeonEvent(dungeonEvents, 
								monster.takeDamageOverTime(levelDamage, damageType, 
										new ArrayList<EquipmentAttribute>() {{ add(EquipmentAttribute.BLEED);}}, 
										castingCharacter.getLevel(), dungeon));
						break;
					case HEAL: 
						description = " to heal.";
						dungeonEvents.add(DungeonEvent.characterCastSpell(dungeon, castingCharacter, this, description));											
						attackResult.setCharacterHeal(levelDamage);
						break;
					case HEALTH_REGEN: 
						description = " to regenerate health.";
						dungeonEvents.add(DungeonEvent.characterCastSpell(dungeon, castingCharacter, this, description));								
						attackResult.setCharacterBuff(org.kerrin.dungeon.enums.Buff.HEALTH_REGEN, damage);
						break;
					case MANA_REGEN:
						description = " to regenerate mana.";
						dungeonEvents.add(DungeonEvent.characterCastSpell(dungeon, castingCharacter, this, description));											
						attackResult.setCharacterBuff(org.kerrin.dungeon.enums.Buff.MANA_REGEN, damage);
						break;
					case STUN_CHANCE:
						if(stun()) {
							int stunLength = characterEquipment.getTotalAttributeValue(EquipmentAttribute.STUN_LENGTH);
							description = " for "+stunLength+" rounds.";
							dungeonEvents.add(DungeonEvent.monsterDebuff(dungeon, monster, this, description));
							monster.stun(stunLength, castingCharacter.getLevel(), dungeon, dungeonEvents);
						}
						break;
					case RANGE:
						description = " range damage.";
						dungeonEvents.add(DungeonEvent.characterCastSpell(dungeon, castingCharacter, this, monster, description));
						// Reduces incoming damage this round
						attackResult.setCharacterBuff(org.kerrin.dungeon.enums.Buff.ALL_RESIST, 1);
						break;
					case SPEED:
						if(damage > 0) {
							description = " for "+damage+" speed boost.";
							dungeonEvents.add(DungeonEvent.characterBuff(dungeon, castingCharacter, this, description));
							attackResult.setCharacterSpeed(damage);
						} else {
							description = " for "+damage+" slow.";
							dungeonEvents.add(DungeonEvent.monsterDebuff(dungeon, monster, this, description));
							attackResult.setMonsterSpeed(damage);
						}
						break;
					case ALL_RESIST:
						description = " for "+damage+" all resist.";
						dungeonEvents.add(DungeonEvent.characterBuff(dungeon, castingCharacter, this, description));
						attackResult.setCharacterBuff(org.kerrin.dungeon.enums.Buff.ALL_RESIST, damage);
						break;
					default:
						logger.info("Unexpected effect type: {}", effectType.getNiceName());
						break;
					}
				}
			} else {
				description = ".";
				dungeonEvents.add(DungeonEvent.characterCastSpell(dungeon, castingCharacter, this, monster, description));
			}
			
			if(damage != 0) {
				if(effectTypes != null) {
					boolean effectDone = false;
					for(EquipmentAttribute effectType:effectTypes) {						
						if(effectType == EquipmentAttribute.DAMAGE_OVER_TIME || effectType == EquipmentAttribute.HEAL ||
							effectType == EquipmentAttribute.MANA_REGEN || effectType == EquipmentAttribute.HEALTH_REGEN || 
							effectType == EquipmentAttribute.SPEED || effectType == EquipmentAttribute.ALL_RESIST) {
							// Nothing here, dealt with elsewhere
							effectDone = true;
						}
					}
					if(effectDone) {
						// Good, nothing to worry about
					} else if (damageType == null) {
						logger.error("No damage type for spell {}", this);
					} else {
						List<DungeonEvent> damageEvents = 
								monster.takeDamage(levelDamage, damageType, effectTypes, castingCharacter.getLevel(), dungeon, attackResult);
						description = levelDamage+" of "+damageType.getNiceName()+".";
						dungeonEvents.add(DungeonEvent.monsterTakeDamage(dungeon,monster, description));
						DungeonEvent.addDungeonEvents(dungeonEvents, damageEvents);
					}
				} else {
					List<DungeonEvent> damageEvents = 
							monster.takeDamage(levelDamage, damageType, effectTypes, castingCharacter.getLevel(), dungeon, attackResult);
					description = levelDamage+" of "+damageType.getNiceName()+".";
					dungeonEvents.add(DungeonEvent.monsterTakeDamage(dungeon,monster, description));
					DungeonEvent.addDungeonEvents(dungeonEvents, damageEvents);
				}
			} else {
				logger.debug("No damage for {} on {}", this, monster);
			}
			attackResult.setMonster(monster);
			attackResult.setCharacter(castingCharacter);
			return attackResult;
		} catch (NullPointerException e) {
			throw e;
		} catch (InvalidSpell e) {
			logger.error("Spell type error: {}", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean stun() {
		return (int)(Math.random()*100)<=MONSTER_BASE_STUN_CHANCE;
	}
	/**
	 * Does this monster spell splash
	 * @return
	 */
	private boolean splash() {
		return (int)(Math.random()*100)<=MONSTER_BASE_SPLASH_CHANCE;
	}
	
	/**
	 * Does this character spell splash
	 * @param equipment
	 * @return
	 */
	private boolean splash(CharacterEquipment equipment) {
		int splashChance = 5 + equipment.getTotalAttributeValue(EquipmentAttribute.SPLASH_CHANCE);
		return (int)(Math.random()*100)<=splashChance;
	}
	
	public static List<Spell> getSpells(CharClass charClass, int level) {
		return Monster.fromCharClass(charClass).getSpells(level);
	}
	
	/**
	 * Get the cast cool down time
	 * @param attackSpeed
	 * @return
	 */
	public Integer getCastCoolDown(int attackSpeed) {
		int coolDown = castCoolDown - attackSpeed;
		return coolDown > 1?coolDown:1;
	}
}
