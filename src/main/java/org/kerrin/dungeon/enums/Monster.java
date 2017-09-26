package org.kerrin.dungeon.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All the possible monster types
 * 
 * @author Kerrin
 *
 */
@SuppressWarnings("serial")
public enum Monster {
	// Note: id must match equivalent char class ids (0 to 10)
	// 			id, niceName, 	health, damage, damageType, Pack Size
	UNKNOWN(	0,	"Unknown!", 	0, 	0, 	DamageType.NONE),
	BARBARIAN(	1,	"Barbarian", 	100,10, DamageType.MELEE), 
	WIZARD(		2, 	"Wizard", 		50, 10, DamageType.FIRE), 
	HEALER(		3, 	"Cleric", 		75, 5, 	DamageType.HOLY), 
	ARCHER(		4, 	"Archer", 		50, 10, DamageType.PIERCING), 
	ROGUE(		5, 	"Rogue", 		60, 15, DamageType.POISON),
	PALADIN(	6, 	"Paladin", 		75, 9, 	DamageType.HOLY),
	NECROMANCER(7, 	"Necromancer", 	50, 6, 	DamageType.ACID),
	MONK(		8, 	"Monk", 		70, 8,	DamageType.WATER),
	BARD(		9, 	"Bard", 		40, 4, 	DamageType.ELECTRIC),
	SNOWMAN(	10, "Snowman", 		50, 12, DamageType.COLD),
	CULTIST(	11, "Cultist", 		25, 12, DamageType.HOLY, 4),
	ZOMBIE(		12, "Zombie", 		20, 5, 	DamageType.POISON, 5),
	WARLOCK(	13, "Warlock", 		40, 17, DamageType.WATER),
	SKELETON(	14, "Skeleton", 	20, 6, 	DamageType.MELEE, 4),
	YETI(		15, "Yeti", 		100,20, DamageType.COLD),
	IMP(		16, "Imp", 			20, 3, 	DamageType.FIRE, 5),
	ROCK_GOLEM(	17, "Rock Golem", 	90, 20, DamageType.MELEE),
	IRON_GOLEM(	18, "Iron Golem", 	90, 20, DamageType.ELECTRIC),
	ICE_GOLEM(	19, "Ice Golem", 	90, 15, DamageType.COLD),
	FIRE_ELEMENTAL(20,"Fire Elemental",30,25,DamageType.FIRE),
	ICE_ELEMENTAL(21,"Ice Elemental",30,20, DamageType.COLD),
	WASP(		22, "Wasp", 		10, 40, DamageType.POISON, 3),
	SPIDER(		23, "Spider", 		20, 40, DamageType.POISON),
	BEATLE(		24, "Beatle", 		40, 20, DamageType.PIERCING, 2),
	BAT(		25, "Bat", 			20, 20, DamageType.PIERCING, 2),
	EAGLE(		26, "Eagle", 		30, 25, DamageType.PIERCING),
	MAGGOT(		27, "Maggot", 		30, 20, DamageType.ACID, 3),
	SNAKE(		28, "Snake", 		30, 50, DamageType.POISON),
	SAND_SHARK(	29, "Sand Shark", 	30, 20, DamageType.WATER),
	INCUBUS(	30, "Incubus", 		40, 20, DamageType.ELECTRIC),
	SUCUBUS(	31, "Sucubus", 		30, 20, DamageType.ACID),
	DRAGON(		32, "Dragon", 		75, 40, DamageType.FIRE),
	DEMON(		33, "Demon",		40, 20, DamageType.FIRE, 2),
	ELF(		34, "Elf", 			20, 15, DamageType.PIERCING),
	DWARF(		35, "Dwarf", 		40, 15, DamageType.MELEE),
	SHAMAN(		36, "Shaman", 		50, 10, DamageType.HOLY),
	BEAR(		37, "Bear", 		70, 20, DamageType.MELEE),
	OWL(		38, "Owl", 			20, 15, DamageType.PIERCING),
	ROBOT(		39, "Robot", 		70, 15, DamageType.ELECTRIC),
	RAT(		40, "Rat", 			10, 10, DamageType.POISON, 5),
	ATROCITY(	41, "Atrocity", 	80, 30, DamageType.ACID),
	VAMPIRE(	42, "Vampire", 		30, 15, DamageType.PIERCING),
	GUNMAN(		43, "Gunman", 		30, 20, DamageType.PIERCING),
	LUNATIC(	44, "Lunatic", 		10, 50, DamageType.ACID),
	TEMPTRESS(	45, "Temptress", 	20, 40, DamageType.POISON),
	MADUSA(		46, "Medusa", 		50, 45, DamageType.POISON),
	SLIME(		47, "Slime", 		20, 25, DamageType.POISON),
	ACID_BALL(	48, "Acid Ball", 	20, 30, DamageType.ACID, 2),
	SHADE(		49, "Shade", 		15, 30, DamageType.WATER),
	DEATH(		50, "Death", 		20, 65,	DamageType.ELECTRIC),
	BUNNY_OF_DEATH(51,"Bunny of Death!",5,120,DamageType.PIERCING),
	MONKEY(		52, "Monkey", 		25, 25, DamageType.MELEE),
	MAGE(		53, "Mage", 		75, 15, DamageType.WATER), 
	CHUCKER(	54, "Chucker", 		75, 30, DamageType.MELEE)
	//			id, niceName, 	health, damage, damageType
	;
	
	private static final Logger logger = LoggerFactory.getLogger(Monster.class);
	
	private int id;
	private String niceName;
	private int health;
	private int mana = 0;
	private int weaponDamage = 0;
	private DamageType damageType;
	private int baseXp;
	/** How many instances spawn */
	private int packSize;
	private HashMap<EquipmentAttribute, Integer> strongAttributes;
	private HashMap<EquipmentAttribute, Integer> weakAttributes;
	private List<Monster> summons;
	private List<Spell> spells;
	
	static {
		for(Monster monster:values()) {
			monster.strongAttributes = new HashMap<EquipmentAttribute, Integer>();
			monster.weakAttributes = new HashMap<EquipmentAttribute, Integer>();
		}
		
		BARBARIAN.mana = 50;
		BARBARIAN.strongAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 100); // Increase Damage
		BARBARIAN.strongAttributes.put(EquipmentAttribute.MELEE_RESIST, 25); // Reduce incoming melee damage
		BARBARIAN.strongAttributes.put(EquipmentAttribute.ALL_RESIST, 25);
		BARBARIAN.strongAttributes.put(EquipmentAttribute.DODGE, 25);
		BARBARIAN.weakAttributes.put(EquipmentAttribute.RANGE, 25);
		BARBARIAN.spells = new ArrayList<Spell>(){
				{
					add(Spell.Smack); add(Spell.Smash); add(Spell.Boulder);
					add(Spell.Slash); add(Spell.Hack); add(Spell.Stun);
					add(Spell.Slice); add(Spell.Swing); add(Spell.Sever);
					add(Spell.Dislocate);
				}
			};
		
		WIZARD.mana = 100;
		WIZARD.strongAttributes.put(EquipmentAttribute.RANGE, 75);
		WIZARD.weakAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 25);
		WIZARD.spells = new ArrayList<Spell>(){
				{
					add(Spell.Firebolt); add(Spell.Fireball); add(Spell.Meteor);
					add(Spell.Volcano); add(Spell.Hydra); add(Spell.Firewall);
					add(Spell.Burn);add(Spell.Invisibility); add(Spell.Blind); 
					add(Spell.Teleport);
				}
			};
			
		HEALER.mana = 100;
		HEALER.strongAttributes.put(EquipmentAttribute.HEAL, 75);
		HEALER.strongAttributes.put(EquipmentAttribute.HEALTH_REGEN, 75);
		HEALER.strongAttributes.put(EquipmentAttribute.HOLY_RESIST, 75);
		HEALER.weakAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 25);
		HEALER.spells = new ArrayList<Spell>(){
				{
					add(Spell.Heal); add(Spell.Renew); add(Spell.Buff);
					add(Spell.Condem); add(Spell.Blind); add(Spell.Confuse);
					add(Spell.Daze);
				}
			};
			
			
		ARCHER.mana = 60;
		ARCHER.strongAttributes.put(EquipmentAttribute.RANGE, 100);
		ARCHER.weakAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 75);
		ARCHER.spells = new ArrayList<Spell>(){
				{
					add(Spell.Shoot); add(Spell.Arrow); add(Spell.Dash); add(Spell.Disorientate);
				}
			};
		
		ROGUE.mana = 50;
		ROGUE.strongAttributes.put(EquipmentAttribute.MELEE_RESIST, 85);
		ROGUE.strongAttributes.put(EquipmentAttribute.GUILE, 85);
		ROGUE.weakAttributes.put(EquipmentAttribute.RANGE, 25);
		ROGUE.spells = new ArrayList<Spell>(){
				{
					add(Spell.Invisibility); add(Spell.Stun); add(Spell.Dislocate);
					add(Spell.Daze); add(Spell.VileAcid); add(Spell.Stab);
					add(Spell.Slash); add(Spell.Slice); add(Spell.PoisonDart);  
					add(Spell.PoisonGas);
				}
			};
		
		PALADIN.mana = 75;
		PALADIN.strongAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 25);
		PALADIN.strongAttributes.put(EquipmentAttribute.HOLY_DAMAGE, 25);
		PALADIN.strongAttributes.put(EquipmentAttribute.BLEED, 25);
		PALADIN.spells = new ArrayList<Spell>(){
				{
					add(Spell.Heal); add(Spell.Renew); add(Spell.Buff);
					add(Spell.Condem); add(Spell.Smash); add(Spell.Smack);
				}
			};
		
		NECROMANCER.mana = 75;
		NECROMANCER.strongAttributes.put(EquipmentAttribute.POISON_DAMAGE, 25);
		NECROMANCER.weakAttributes.put(EquipmentAttribute.HOLY_DAMAGE, 25);
		NECROMANCER.spells = new ArrayList<Spell>(){
				{
					add(Spell.AcidRain); add(Spell.VileAcid); add(Spell.Confuse);
					add(Spell.Fart); add(Spell.Sting); add(Spell.Swarm);
					add(Spell.Slow);
				}
			};
		
		MONK.mana = 70;
		MONK.strongAttributes.put(EquipmentAttribute.ATTACK_SPEED, 25);
		MONK.strongAttributes.put(EquipmentAttribute.DODGE, 25);
		MONK.strongAttributes.put(EquipmentAttribute.ALL_RESIST, 25);
		MONK.strongAttributes.put(EquipmentAttribute.HOLY_DAMAGE, 25);
		MONK.strongAttributes.put(EquipmentAttribute.BLEED, 25);
		MONK.weakAttributes.put(EquipmentAttribute.RANGE, 25);
		MONK.spells = new ArrayList<Spell>(){
				{
					add(Spell.Heal); add(Spell.Renew); add(Spell.Condem);
					add(Spell.Dislocate); add(Spell.Blind); add(Spell.Slice);
				}
			};
		
		BARD.mana = 100;
		BARD.strongAttributes.put(EquipmentAttribute.STUN_RESIST, 25);
		BARD.strongAttributes.put(EquipmentAttribute.MAGIC_FIND, 25);
		BARD.weakAttributes.put(EquipmentAttribute.DODGE, 25);
		BARD.spells = new ArrayList<Spell>(){
				{
					add(Spell.Buff); add(Spell.Sing); add(Spell.Poem);
					add(Spell.Story); add(Spell.Encourage); add(Spell.Threaten);
					add(Spell.Fast); add(Spell.Disorientate);
				}
			};
			
		MAGE.mana = 75; 
		MAGE.strongAttributes.put(EquipmentAttribute.RANGE, 25);
		MAGE.weakAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 25);
		MAGE.spells = new ArrayList<Spell>(){
				{
					add(Spell.Flood); add(Spell.Drown); add(Spell.Soak);
					add(Spell.Rain); add(Spell.Tsunami); add(Spell.Wave);
					add(Spell.Wet);  add(Spell.Icebolt); add(Spell.Snowball); 
					add(Spell.Glacia); add(Spell.Blizzard); add(Spell.Freeze);
				}
			};
		
		SNOWMAN.strongAttributes.put(EquipmentAttribute.COLD_RESIST, 100);
		SNOWMAN.weakAttributes.put(EquipmentAttribute.FIRE_RESIST, 100);
		SNOWMAN.spells = new ArrayList<Spell>(){
			{
				add(Spell.Slow);
			}
		};
		
		CULTIST.mana = 100;
		CULTIST.spells = new ArrayList<Spell>(){
				{
					add(Spell.Icebolt); add(Spell.Snowball); add(Spell.Glacia);
					add(Spell.Blizzard); add(Spell.Freeze); add(Spell.Disorientate);
				}
			};
		CULTIST.summons = new ArrayList<Monster>(){
				{
					add(SNOWMAN);
				}
			};
			
		ZOMBIE.weakAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 75);
		ZOMBIE.weakAttributes.put(EquipmentAttribute.SPEED, 50);
		
		WARLOCK.mana = 90;
		WARLOCK.summons = new ArrayList<Monster>(){
				{
					add(IMP); add(DEMON); add(ZOMBIE);
				}
			};
			
		SKELETON.weakAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 95);
		
		YETI.strongAttributes.put(EquipmentAttribute.COLD_RESIST, 100);
		YETI.spells = new ArrayList<Spell>(){
			{
				add(Spell.Slow);
			}
		};
		
		IMP.strongAttributes.put(EquipmentAttribute.FIRE_DAMAGE, 100);
		IMP.strongAttributes.put(EquipmentAttribute.FIRE_RESIST, 100);
		
		ROCK_GOLEM.strongAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 75);
		ROCK_GOLEM.strongAttributes.put(EquipmentAttribute.STUN_RESIST, 50);
		ROCK_GOLEM.strongAttributes.put(EquipmentAttribute.FIRE_DAMAGE, 75);
		
		IRON_GOLEM.strongAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 75);
		IRON_GOLEM.strongAttributes.put(EquipmentAttribute.STUN_RESIST, 50);
		IRON_GOLEM.weakAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 75);
		
		ICE_GOLEM.weakAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 75);
		ICE_GOLEM.strongAttributes.put(EquipmentAttribute.STUN_RESIST, 50);
		ICE_GOLEM.weakAttributes.put(EquipmentAttribute.FIRE_DAMAGE, 100);
		ICE_GOLEM.spells = new ArrayList<Spell>(){
			{
				add(Spell.Slow);
			}
		};
		
		FIRE_ELEMENTAL.weakAttributes.put(EquipmentAttribute.COLD_RESIST, 100);
		ICE_ELEMENTAL.weakAttributes.put(EquipmentAttribute.FIRE_RESIST, 100);
		ICE_ELEMENTAL.spells = new ArrayList<Spell>(){
			{
				add(Spell.Slow);
			}
		};
		
		WASP.strongAttributes.put(EquipmentAttribute.POISON_DAMAGE, 75);
		WASP.strongAttributes.put(EquipmentAttribute.DAMAGE_OVER_TIME, 75);
		
		SPIDER.strongAttributes.put(EquipmentAttribute.POISON_DAMAGE, 75);
		BEATLE.strongAttributes.put(EquipmentAttribute.ARMOUR, 75);
		BAT.strongAttributes.put(EquipmentAttribute.ATTACK_SPEED, 50);
		EAGLE.weakAttributes.put(EquipmentAttribute.RANGE, 50);
		MAGGOT.weakAttributes.put(EquipmentAttribute.ACID_DAMAGE, 50);
		SNAKE.strongAttributes.put(EquipmentAttribute.POISON_DAMAGE, 50);
		SAND_SHARK.strongAttributes.put(EquipmentAttribute.ALL_RESIST, 50);
		INCUBUS.strongAttributes.put(EquipmentAttribute.POISON_DAMAGE, 50);
		SUCUBUS.strongAttributes.put(EquipmentAttribute.FIRE_DAMAGE, 50);
		DRAGON.strongAttributes.put(EquipmentAttribute.FIRE_DAMAGE, 150);
		DRAGON.strongAttributes.put(EquipmentAttribute.FIRE_RESIST, 100);
		DEMON.strongAttributes.put(EquipmentAttribute.FIRE_RESIST, 95);
		ELF.strongAttributes.put(EquipmentAttribute.CRIT_CHANCE, 100);
		DWARF.strongAttributes.put(EquipmentAttribute.DODGE, 70);
		SHAMAN.mana=90;
		SHAMAN.spells = new ArrayList<Spell>(){
			{
				add(Spell.AcidRain); add(Spell.VileAcid); add(Spell.Confuse);
				add(Spell.Fart); add(Spell.Sting); add(Spell.Swarm);
			}
		};
		SHAMAN.summons = new ArrayList<Monster>(){
			{
				add(IMP); add(DEMON); add(ZOMBIE);
			}
		};
		BEAR.strongAttributes.put(EquipmentAttribute.BLEED, 100);
		ROBOT.spells = new ArrayList<Spell>(){
			{
				add(Spell.Slash); add(Spell.Slice);
			}
		};
		OWL.strongAttributes.put(EquipmentAttribute.ATTACK_SPEED, 50);
		ROBOT.mana=50;
		ROBOT.spells = new ArrayList<Spell>(){
			{
				add(Spell.Electricute); add(Spell.Sizzle); add(Spell.Tazer);
				add(Spell.Lightning);
			}
		};
		RAT.strongAttributes.put(EquipmentAttribute.POISON_DAMAGE, 70);
		RAT.strongAttributes.put(EquipmentAttribute.POISON_RESIST, 70);
		ATROCITY.strongAttributes.put(EquipmentAttribute.CRIT_CHANCE, 50);
		VAMPIRE.strongAttributes.put(EquipmentAttribute.BLEED, 100);
		GUNMAN.strongAttributes.put(EquipmentAttribute.RANGE, 50);
		GUNMAN.weakAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 50);
		LUNATIC.strongAttributes.put(EquipmentAttribute.CRIT_DAMAGE, 100);
		LUNATIC.weakAttributes.put(EquipmentAttribute.ATTACK_SPEED, 70);
		TEMPTRESS.strongAttributes.put(EquipmentAttribute.STUN_RESIST, 50);
		MADUSA.strongAttributes.put(EquipmentAttribute.STUN_CHANCE, 70);
		MADUSA.strongAttributes.put(EquipmentAttribute.POISON_DAMAGE, 100);
		MADUSA.strongAttributes.put(EquipmentAttribute.STUN_RESIST, 100);
		SLIME.strongAttributes.put(EquipmentAttribute.POISON_DAMAGE, 100);
		SLIME.strongAttributes.put(EquipmentAttribute.POISON_RESIST, 100);
		SLIME.strongAttributes.put(EquipmentAttribute.ACID_RESIST, 100);
		SLIME.spells = new ArrayList<Spell>(){
			{
				add(Spell.Slow);
			}
		};
		ACID_BALL.strongAttributes.put(EquipmentAttribute.ACID_DAMAGE, 100);
		ACID_BALL.strongAttributes.put(EquipmentAttribute.ACID_RESIST, 100);
		SHADE.strongAttributes.put(EquipmentAttribute.MELEE_RESIST, 100);
		DEATH.strongAttributes.put(EquipmentAttribute.MELEE_RESIST, 90);
		DEATH.strongAttributes.put(EquipmentAttribute.ACID_RESIST, 90);
		DEATH.strongAttributes.put(EquipmentAttribute.COLD_RESIST, 90);
		DEATH.strongAttributes.put(EquipmentAttribute.ELECTRIC_RESIST, 90);
		DEATH.strongAttributes.put(EquipmentAttribute.FIRE_RESIST, 90);
		DEATH.strongAttributes.put(EquipmentAttribute.POISON_RESIST, 90);
		DEATH.strongAttributes.put(EquipmentAttribute.WATER_RESIST, 90);
		DEATH.weakAttributes.put(EquipmentAttribute.HOLY_RESIST, 90);
		BUNNY_OF_DEATH.strongAttributes.put(EquipmentAttribute.MELEE_DAMAGE, 300);
		MONKEY.strongAttributes.put(EquipmentAttribute.GUILE, 70);
		
		CHUCKER.strongAttributes.put(EquipmentAttribute.RANGE, 50);
		CHUCKER.spells = new ArrayList<Spell>(){
			{ 
				add(Spell.Shoot); add(Spell.Throw); add(Spell.Disorientate);
			}
		};
	}
	
	
	/**
	 * 
	 * @param id
	 * @param niceName
	 * @param health
	 * @param baseDamage
	 * @param damageType
	 */
	Monster (int id, String niceName, int health, int baseDamage, DamageType damageType) {
		this(id, niceName, health, baseDamage, damageType, 1);
	}
	
	/**
	 * Constuctor
	 * 
	 * @param id
	 * @param niceName
	 * @param health
	 * @param baseDamage
	 * @param damageType
	 * @param packSize
	 */
	Monster (int id, String niceName, int health, int baseDamage, DamageType damageType, int packSize) {
		this.id = id;
		this.niceName = niceName;
		this.health = health;
		this.mana = 0;
		this.weaponDamage = baseDamage;
		this.damageType = damageType;
		this.baseXp = 1+((health * baseDamage) / 200);
		this.packSize = packSize;
	}
	
	/**
	 * Get the identifier
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	public String getName() {
		return name();
	}
	
	/**
	 * Get the nice name
	 * 
	 * @return
	 */
	public String getNiceName() {
		return niceName;
	}	
	
	public int getHealth() {
		return health;
	}

	public int getMana() {
		return mana;
	}

	public int getBaseWeaponDamage() {
		return weaponDamage;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public HashMap<EquipmentAttribute, Integer> getStrongAttributes() {
		return strongAttributes;
	}

	public HashMap<EquipmentAttribute, Integer> getWeakAttributes() {
		return weakAttributes;
	}

	public List<Monster> getSummons() {
		return summons;
	}

	public List<Spell> getSpells() {
		return spells;
	}
	
	public List<Spell> getSpells(int level) {
		List<Spell> spellsAtLevel = new ArrayList<Spell>();
		if(spells != null) {
			for(Spell spell:spells) {
				if(level >= spell.getMinimumLevel()) {
					spellsAtLevel.add(spell);
				}
			}
		}
		return spellsAtLevel;
	}

	public int getPackSize() {
		return packSize;
	}

	/**
	 * Get a character class from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static Monster fromId(int id) {
		for(Monster thisCharClass:values()) {
			if(thisCharClass.id == id) return thisCharClass;
		}
		return UNKNOWN;
	}
	
	public static Monster getRandom() {
		int chance = values().length - 1;
		for(Monster thisMonster:values()) {
			if(thisMonster == Monster.UNKNOWN) continue;
			int random = (int) (Math.random()*chance);
			if(random < 1) return thisMonster;
			chance--;
		}
		logger.error("No monster rolled randomly, chance is still " + chance);
		return UNKNOWN;
	}
	
	/**
	 * Get a random spell this monster can cast at this level
	 * 
	 * @return Spell or null if no spells castable
	 */
	public Spell getRandomSpell(int level) {
		if(spells == null) {
			return null;
		}
		int chance = spells.size();
		List<Spell> spellsAtLevel = new ArrayList<Spell>();
		for(Spell spell:spells) {
			if(spell.getMinimumLevel() <= level) {
				spellsAtLevel.add(spell);
			} else {
				chance--;
			}
		}
		
		for(Spell spell:spellsAtLevel) {
			int random = (int) (Math.random()*chance);
			if(random < 1) return spell;
			chance--;
		}
		if(chance > 0) {
			logger.error("No spell rolled randomly, chance is still " + chance +" of " + spells.size());
		} else {
			logger.debug("No spell rolled randomly, must have been none low enough level");
		}
		return null;
	}

	public Monster getRandomSummon() {
		if(summons == null) {
			return null;
		}
		int chance = summons.size();
		for(Monster summon:summons) {			
			int random = (int) (Math.random()*chance);
			if(random < 1) return summon;
			chance--;
		}
		if(chance > 0) {
			logger.error("No summon rolled randomly, chance is still " + chance +" of " + summons.size());
		} else {
			logger.debug("No summon rolled randomly");
		}
		return null;
	}

	public int getBaseXp() {
		return this.baseXp;
	}

	public static Monster fromCharClass(CharClass charClass) {
		return Monster.fromId(charClass.getId());
	}
}
