package org.kerrin.dungeon.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.ArraySizeMismatch;
import org.kerrin.dungeon.exception.DifferentGameStates;
import org.kerrin.dungeon.exception.DungeonNotRunable;
import org.kerrin.dungeon.forms.DungeonForm;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Database record for a dungeon instance that a player can run, is running, or has run
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="dungeon")
public class Dungeon {
	@Transient
	@JsonIgnore
	private static final Logger logger = LoggerFactory.getLogger(Dungeon.class);

	/** How many times the monster can be attacked before the characters give up */
	@Transient
	@JsonIgnore
	private static final int MAX_ITERATIONS = 100;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	/** The account this dungeon can run by 
	 *	A foreign key to account.id 
	 */
	@ManyToOne
	@JoinColumn(name="account")
	private Account account;
	
	/** Is this record for a hardcore character */
	private boolean hardcore;
	
	/** Is this record for an ironborn character */
	private boolean ironborn;
	
	/** The type of dungeon */
	@Enumerated(EnumType.ORDINAL)
	private DungeonType type;
	
	/** The level of the character */
	private int level; // Length is 60 seconds per level
	
	/** How much bonus experience each character will get for successful completion */
	private long xpReward;
	/** The bonus items the player will get for successful completion, and if it was found */
	@ElementCollection
	@MapKeyColumn(name="equipment")
    @Column(name="found")
	private Map<Equipment,Boolean> itemRewards;
	/** If the found items have be generated */
	@JsonIgnore
	private boolean generatedFoundItemRewards;
	
	/** Boost items, if any */
	@ElementCollection
	private List<BoostItem> boostItemRewards;
	
	/** The monsters of the dungeon in order */
	@ElementCollection
	@MapKeyColumn(name="monster_id")
    @Column(name="monster_type")
	@JsonIgnore
	private Map<Monster,MonsterType> monsters;
	
	/** The monsters of the dungeon in order */
	@ElementCollection
	@MapKeyColumn(name="monster_id")
    @Column(name="monster_type")
	@JsonIgnore
	private Map<Monster,MonsterType> deadMonsters;
	
	/** When was the dungeon started, or null if not started */
	private Date started = null;
	
	/** When was the dungeon started, or null if not started */
	private Date expires = null;
	
	/** The number of the characters running the dungeon */
	private int partySize = 1;
	
	/** List of size party size if active */
	@OneToMany
	private List<Character> characters = null;

	private long killedXp;
	
	private boolean failed = false;
	
	protected Dungeon() {};
	
	public Dungeon(long id, DungeonType type, Account account, boolean hardcore, boolean ironborn, int level, long xpReward,
			Map<Monster,MonsterType> monsters, int partySize, long killedXp) {
		super();
		this.id = id;
		this.type = type;
		this.account = account;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.level = level;
		this.xpReward = xpReward;
		this.killedXp = killedXp;
		this.generatedFoundItemRewards = false;
		this.monsters = monsters;
		this.deadMonsters = new HashMap<Monster, MonsterType>();
		this.partySize = partySize;
		// Default to expiring tomorrow
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH) + 1;
		cal.set(Calendar.DAY_OF_MONTH, day);
		this.expires = cal.getTime();
	}

	/**
	 * Update dungeon details
	 * 
	 * @param trustedDungeon
	 * @param newDungeon
	 */
	public Dungeon(Dungeon trustedDungeon, Dungeon newDungeon) {
		this(trustedDungeon.getId(), newDungeon.type, newDungeon.account, newDungeon.hardcore, newDungeon.ironborn, 
				newDungeon.level, newDungeon.xpReward, newDungeon.monsters, newDungeon.partySize, newDungeon.killedXp);
		this.itemRewards = newDungeon.itemRewards;
		this.boostItemRewards = newDungeon.boostItemRewards;
		this.started = newDungeon.started;
		this.expires = newDungeon.expires;
		this.generatedFoundItemRewards = newDungeon.generatedFoundItemRewards;
		this.failed = newDungeon.failed;
		this.characters = newDungeon.characters;
	}

	/**
	 * You must save the reward items
	 * 
	 * @param equipmentRepo
	 * @param dungeonForm
	 * @throws ArraySizeMismatch
	 */
	public Dungeon(DungeonForm dungeonForm) throws ArraySizeMismatch {
		this(dungeonForm.getId(), dungeonForm.getType(), dungeonForm.getAccount(), 
				dungeonForm.isHardcore(), dungeonForm.isIronborn(), dungeonForm.getLevel(),
				dungeonForm.getXpReward(),
				createMonsterMap(dungeonForm.getMonsters(),dungeonForm.getMonsterTypes()),
				dungeonForm.getPartySize(), 0);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public DungeonType getType() {
		return type;
	}

	public void setType(DungeonType type) {
		this.type = type;
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

	public long getXpReward() {
		if(type == DungeonType.ADVENTURE) {
			return xpReward * characters.size();
		} else {
			return xpReward;
		}
	}

	public void setXpReward(long xpReward) {
		this.xpReward = xpReward;
	}

	public long getKilledXp() {
		return killedXp;
	}

	/**
	 * Get all the equipment that are rewards
	 * Results are a Map of equipment to a boolean determining if it was a found item
	 * @return
	 */
	public Map<Equipment,Boolean> getItemRewards() {
		return itemRewards;
	}
	
	public Set<Equipment> getItemRewardsAsSet() {
		return itemRewards.keySet();
	}

	public void setItemRewards(Map<Equipment,Boolean> itemRewards) {
		this.itemRewards = itemRewards;
	}

	public boolean hasGeneratedFoundItemRewards() {
		return generatedFoundItemRewards;
	}

	public List<BoostItem> getBoostItemRewards() {
		return boostItemRewards;
	}

	public void setBoostItemRewards(List<BoostItem> boostItemRewards) {
		this.boostItemRewards = boostItemRewards;
	}

	public Map<Monster,MonsterType> getMonsters() {
		monsters.remove(Monster.UNKNOWN);
		return monsters;
	}
	
	public List<Monster> getMonstersAsList() {
		monsters.remove(Monster.UNKNOWN);
		
		Set<Monster> monsterSet = monsters.keySet();
		List<Monster> monsterOrderedList = new Vector<Monster>();
		for(Monster monster:monsterSet) {
			MonsterType monsterType = monsters.get(monster);
			int monsterCount = monsterOrderedList.size();
			int index = 0;
			MonsterType itemMonsterType = MonsterType.NONE;
			if(!monsterOrderedList.isEmpty()) {
				itemMonsterType = monsters.get(monsterOrderedList.get(index));
			}
			while(monsterType.after(itemMonsterType) && index < monsterCount) {
				Monster itemMonster = monsterOrderedList.get(index);				
				itemMonsterType = monsters.get(itemMonster);
				index++;
			}
			monsterOrderedList.add(index, monster);
		}
		return monsterOrderedList;
	}

	public Map<Monster,MonsterType> getDeadMonsters() {
		deadMonsters.remove(Monster.UNKNOWN);
		return deadMonsters;
	}
	
	public List<Monster> getDeadMonstersAsList() {
		deadMonsters.remove(Monster.UNKNOWN);
		
		Set<Monster> monsterSet = deadMonsters.keySet();
		List<Monster> monsterOrderedList = new Vector<Monster>();
		for(Monster monster:monsterSet) {
			MonsterType monsterType = deadMonsters.get(monster);
			int monsterCount = monsterOrderedList.size();
			int index = 0;
			MonsterType itemMonsterType = MonsterType.NONE;
			if(!monsterOrderedList.isEmpty()) {
				itemMonsterType = deadMonsters.get(monsterOrderedList.get(index));
			}
			while(monsterType.after(itemMonsterType) && index < monsterCount) {
				Monster itemMonster = monsterOrderedList.get(index);				
				itemMonsterType = deadMonsters.get(itemMonster);
				index++;
			}
			monsterOrderedList.add(index, monster);
		}
		return monsterOrderedList;
	}
	
	public void setMonsters(Map<Monster,MonsterType> monsters) {
		monsters.remove(Monster.UNKNOWN);
		this.monsters = monsters;
	}

	public void setDeadMonsters(Map<Monster, MonsterType> deadMonsters) {
		deadMonsters.remove(Monster.UNKNOWN);
		this.deadMonsters = deadMonsters;
	}
	
	public void killMonster(MonsterInstance monsterInstance) {
		MonsterType monsterType = monsters.get(monsterInstance.monster);
		if(monsterType != null && monsterInstance.monsterType.equals(monsterType)) {
			monsters.remove(monsterInstance.monster);
			deadMonsters.put(monsterInstance.monster, monsterType);
		}
	}

	public Date getStarted() {
		return started;
	}

	/**
	 * For use in test rigs only!
	 * @param started
	 */
	public void setStarted(Date started) {
		this.started = started;
	}
	
	/**
	 * Get the date and time the dungeon will be done, or null if not started
	 * 
	 * @return	Length of dungeon in seconds
	 */
	public Date getDoneDate() {
		if(started == null) return null;
		Calendar finishes = Calendar.getInstance();
		finishes.setTime(started);
		finishes.add(Calendar.MINUTE, level);
		return finishes.getTime();
	}
	
	/**
	 * How much would it cost to rush this?
	 * Always minutes (or part there of) left of dungeon
	 * 
	 * @return
	 */
	public int getRushCost() {
		Date now = new Date();
		Date doneDate = getDoneDate();
		if(doneDate == null) return -1;
		return 1 + (int)((doneDate.getTime() - now.getTime()) / 1000) / 60;
	}

	/**
	 * The cost in boost item rush item level
	 * @return
	 */
	public int getRushCostBoostItemLevel() {
		return getRushCost() * 2;
	}
	
	/**
	 * Has the dungeon finished?
	 * You should also check if it was failed
	 * 
	 * @return
	 */
	public boolean isFinished() {
		Date doneDate = getDoneDate();
		Date now = new Date();
		if(doneDate == null || doneDate.after(now)) {
			return false;
		}
		return true;
	}
	
	public List<DungeonEvent> start(CharacterEquipmentService characterEquipmentService, Date started) throws DungeonNotRunable {
		this.started = started;
		this.expires = null;
		return run(characterEquipmentService);
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public int getPartySize() {
		return partySize;
	}

	public void setPartySize(int partySize) {
		this.partySize = partySize;
	}

	public int getDeadCount() {
		if(!isFinished()) {
			return 0;
		} else {
			int deadCount = 0;
			for(Character character:characters) {
				if(character.isCurrentlyDead()) deadCount++;
			}
			return deadCount;
		}
	}
	
	public List<Character> getCharacters() {
		return characters;
	}

	public boolean isFailed() {
		return failed;
	}

	public void removeItemReward(Equipment equipment) {
		itemRewards.remove(equipment);
	}

	public void removeBoostItemReward(BoostItem removeBoostItem) {
		Iterator<BoostItem> iter = boostItemRewards.iterator();
		while(iter.hasNext()) {
			BoostItem thisBoostItem = iter.next();
			if(thisBoostItem.equals(removeBoostItem)) {
				iter.remove();
				break;
			}
		}
	}

	/**
	 * Get the characters that ran this dungeon
	 * If aliveOnly is true, it will only return characters that aren't dead 
	 * You should only call this after dungeon.run(), or it will just return the same as getCharacters()
	 * 
	 * @param aliveOnly
	 * @return
	 */
	public List<Character> getCharacters(boolean aliveOnly) {
		if(!aliveOnly) return characters;
		List<Character> tempCharacters = new ArrayList<Character>();
		for(Character character:characters) {
			if(!character.isCurrentlyDead()) {
				tempCharacters.add(character);
			}
		}
		return tempCharacters;
	}

	public void setCharacters(List<Character> characters) throws AccountIdMismatch, DifferentGameStates {
		for(Character character:characters) {
			if(!character.getAccount().equals(account)) {
				throw new AccountIdMismatch(character.getAccount().getId(), account.getId());
			}
			if(character.isHardcore() != hardcore || character.isIronborn() != ironborn) {
				throw new DifferentGameStates(character.toString(), this.toString());
			}
		}
		this.characters = characters;
	}

	/**
	 * Returns a difficulty number, as a rough guide to how hard the dungeon will likely be
	 * 
	 * @param difficultyModifier	How to adjust the difficulty based on user modification
	 * 								0 	= No change
	 * 								+ve = 1 -> 5% harder
	 * 								-ve = 1 -> 5% easier
	 * 								Note: the level will change at least 1 level per difficulty modifier
	 * 
	 * @return
	 */
	public DungeonDifficulty getDifficulty(int difficultyModifier) {
		DungeonDifficulty difficulty = new DungeonDifficulty();
		
		difficulty.difficultyModifier = difficultyModifier;
		if(difficulty.difficultyModifier > 0) difficulty.cost = difficultyModifier;
		difficulty.levelAdjustment = 0;
		
		if(difficultyModifier != 0) {
			int levelChange = (int)(level * 0.05 * difficultyModifier);
			if(Math.abs(levelChange) < Math.abs(difficultyModifier)) {
				levelChange = Math.abs(difficultyModifier);
				if(difficultyModifier < 0) {
					levelChange = -levelChange;
				}
			}
			difficulty.levelAdjustment += levelChange;
		}
		if(level + difficulty.levelAdjustment < 1) {
			// Can't make dungeons lower level than 1
			return null;
		}
		
		for(Monster monster:monsters.keySet()) {
			MonsterType monsterType = monsters.get(monster);
			int monsterDifficulty = monster.getBaseXp() * monsterType.getTypeMultiplier() * monster.getPackSize();
			difficulty.difficultyScore += monsterDifficulty;
		}
		
		// Add the dead ones for dungeons that have started
		for(Monster monster:deadMonsters.keySet()) {
			MonsterType monsterType = deadMonsters.get(monster);
			int monsterDifficulty = monster.getBaseXp() * monsterType.getTypeMultiplier() * monster.getPackSize();
			difficulty.difficultyScore += monsterDifficulty;
		}
		
		if(level + difficulty.levelAdjustment > Character.MAX_LEVEL) {
			difficulty.difficultyScore *= (1 + ((level + difficulty.levelAdjustment - Character.MAX_LEVEL) * 0.2));
		}
		
		return difficulty;
	}

	/**
	 * Get how many tokens this dungeon rewards for successful completion
	 * @return
	 */
	public long getRewardTokens() {
		return 1 + (level/2);
	}
	
	/**
	 * Get a Map of the monsters from the arrays
	 * The two arrays must be the same size and the entries at the same index are for the same monster
	 * 
	 * @param monstersArray		Monsters
	 * @param monsterTypeArray	Monster types
	 * 
	 * @return Map of monsters
	 * 
	 * @throws ArraySizeMismatch
	 */
	public static Map<Monster,MonsterType> createMonsterMap(
			Monster[] monstersArray, MonsterType[] monsterTypeArray) throws ArraySizeMismatch {
		if(monstersArray == null || monsterTypeArray == null) {
			logger.debug("No monster instances");
			return new HashMap<Monster,MonsterType>();
		}
		Map<Monster,MonsterType> instances = new HashMap<Monster,MonsterType>();
		if(monstersArray.length != monsterTypeArray.length) {
			throw new ArraySizeMismatch("Monster and Type arrays different sizes. "+monstersArray.length +","+ monsterTypeArray.length);
		}
		for(int i=0; i < monstersArray.length; i++) {
			if(monstersArray[i] != Monster.UNKNOWN) {
				instances.put(monstersArray[i], monsterTypeArray[i]);
			}
		}
		return instances;
	}

	/**
	 * Create a random set of monsters for an adventure
	 * 
	 * @return
	 */
	public static Map<Monster, MonsterType> createMonstersForAdventure() {
		Map<Monster, MonsterType> monsters = new HashMap<Monster, MonsterType>();
		int numberMonsters = (int)(Math.random()*10) + 1;
		for(int i=0; i < numberMonsters; i++) {
			Monster monster = Monster.getRandom();
			while(monsters.containsKey(monster)) {
				monster = Monster.getRandom();
			}
			monsters.put(monster, MonsterType.getRandomType());
		}
		return monsters;
	}
	
	/**
	 * 
	 * @param canCurse
	 * @param hardcore
	 * @param ironborn
	 * @return
	 */
	public List<Equipment> generateItemRewards(boolean canCurse, boolean hardcore, boolean ironborn) {
		Map<Equipment,Boolean> equipmentList = new HashMap<Equipment,Boolean>();
		
		int rewardCount = 1;
		switch(type) {
		case NONE: case ADVENTURE: break;
		case DUNGEON:
			rewardCount = 2;
			break;
		case RAID:
			rewardCount = 4;
			break;
		}
		List<Equipment> newItems = new ArrayList<Equipment>();
		for(int rewardIndex = 0; rewardIndex < rewardCount; rewardIndex++) {
			Equipment equipment = Equipment.createRandom(type, level, hardcore, ironborn,
					EquipmentLocation.DUNGEON, id, canCurse);
			newItems.add(equipment);
			logger.debug("Saved equipment: {}", equipment.toString());
			equipmentList.put(equipment, false);
		}
		
		itemRewards = equipmentList;
		
		return newItems;
	}

	/**
	 * Generate the found items, you need to add these to the itemRewards manually after storing in the database
	 * 
	 * @param rewardCount	Number of items to create
	 */
	public List<Equipment> generateFoundItemRewards(int rewardCount) {
		if(this.generatedFoundItemRewards) {
			logger.error("Attempt to regenerate found rewards for dungeon {}", id);
			return new ArrayList<Equipment>();
		}
		List<Equipment> newItems = new ArrayList<Equipment>();
		for(int rewardIndex = 0; rewardIndex < rewardCount; rewardIndex++) {
			Equipment newItem = Equipment.createRandom(type, level, hardcore, ironborn, 
					EquipmentLocation.DUNGEON, id, true);
			newItems.add(newItem);			
		}
		generatedFoundItemRewards = true;
		return newItems;
	}

	public List<BoostItem> generateBoostItemRewards(int rewardCount) {
		if(!this.boostItemRewards.isEmpty()) {
			logger.error("Attempt to regenerate boost item rewards for dungeon {}", id);
			return new ArrayList<BoostItem>();
		}
		List<BoostItem> newItems = new ArrayList<BoostItem>();
		for(int rewardIndex = 0; rewardIndex < rewardCount; rewardIndex++) {
			BoostItem newBoostItem = BoostItem.createRandom(account, level, hardcore, ironborn, -1, id, -1);
			if(newBoostItem != null) {
				newItems.add(newBoostItem);
			}
		}
		return newItems;
	}

	/**
	 * Run the dungeon when it is finished
	 */
	private List<DungeonEvent> run(CharacterEquipmentService characterEquipmentService) throws DungeonNotRunable {
		List<DungeonEvent> dungeonEvents = new ArrayList<DungeonEvent>();
		if(started == null) {
			throw new DungeonNotRunable("Attempt to run dungeon "+id+" that hasn't been started yet");
		}
		if(characters == null || characters.isEmpty()) {
			throw new DungeonNotRunable("Attempt to run dungeon "+id+" that had no characters");
		}
		List<Character> aliveCharacters = new Vector<Character>();
		int aliveCharacterCount = 0;
		for(Character character:characters) {
			CharacterEquipment equipment = characterEquipmentService.findById(character.getId());
			character.startDungeon(this, equipment);
			aliveCharacters.add(character);
			aliveCharacterCount++;
			int runSpeedValue = equipment.getTotalAttributeValue(EquipmentAttribute.SPEED);
			if(runSpeedValue > 0) {
				// Check for max run speed
				int maxRunSpeed = level * 100;
				if(runSpeedValue > maxRunSpeed) runSpeedValue = maxRunSpeed;
				// Only proportional effect
				runSpeedValue /= characters.size();
				int secondsReduced = (int)((level*60)*(1.0*runSpeedValue/200/level));
				logger.debug("Reducing length of dungeon by %d seconds"+secondsReduced);
				Calendar cal = Calendar.getInstance();
				cal.setTime(started);
				cal.add(Calendar.SECOND, -secondsReduced);
				started = cal.getTime();
			}
		}
		
		// Process the monsters against the characters
		List<MonsterInstance> monsterInstances = new ArrayList<MonsterInstance>();
		for(Monster monster:monsters.keySet()) {
			int packSize = monster.getPackSize();
			for(int i = 0; i < packSize; i++) {
				MonsterInstance monsterInstance = new MonsterInstance(monster, monsters.get(monster), level, partySize);
				monsterInstances.add(monsterInstance);
			}
		}
		ListIterator<MonsterInstance> iter = monsterInstances.listIterator();
		List<MonsterInstance> summonedMonsters = new ArrayList<MonsterInstance>();
		while(iter.hasNext()) {
			while(iter.hasNext()) {
				MonsterInstance monster = iter.next();
				boolean allMonstersDead = false;
				int iterations = 0;
				while(!allMonstersDead && aliveCharacterCount > 0 && iterations++ < MAX_ITERATIONS) {
					for(Character activeCharacter:aliveCharacters) {
						if(!allMonstersDead){
							// Monster attacks				
							AttackResult attackResult = monster.attack(this, activeCharacter, dungeonEvents);
							activeCharacter.copy(processCharacterAttackResult(attackResult, aliveCharacters, dungeonEvents));
							monster.copy(processMonsterAttackResult(attackResult, dungeonEvents));
							if(attackResult.getCharacterSplashDamage() != null) {
								Damage splashDamage = attackResult.getCharacterSplashDamage();
								for(Character splashCharacter:aliveCharacters) {
									// Splash damage doesn't hit original character again
									if(splashCharacter.equals(activeCharacter)) continue;
									String description = splashDamage.getDamageAmount() + " " + 
											splashDamage.getDamageType().getNiceName() + " splash damage.";
									dungeonEvents.add(DungeonEvent.characterTakeDamage(this, splashCharacter, description));
									DungeonEvent.addDungeonEvents(dungeonEvents, 
											splashCharacter.takeDamage(splashDamage.getDamageAmount(), splashDamage.getDamageType(), 
											new ArrayList<EquipmentAttribute>(){{add(EquipmentAttribute.SPLASH_DAMAGE);}},
											monster.level, this, attackResult));
								}
							}
							if(attackResult.getSummonedMonster() != null) {
								String description = attackResult.getSummonedMonster().monster.getNiceName();
								dungeonEvents.add(DungeonEvent.monsterSummon(this, monster, description));
								// Add the summoned monster to the monsters and now make it the active monster
								summonedMonsters.add(attackResult.getSummonedMonster());
							}
							
							if(activeCharacter.isStunned()) {
								String description = activeCharacter.getName() + " is stunned.";
								dungeonEvents.add(DungeonEvent.info(this, description));
							} else if(activeCharacter.isDead()) {
								dungeonEvents.add(DungeonEvent.characterDied(this, activeCharacter, 
										monster, attackResult.getCastSpell(), ""));
								// Used later so we know it wasm't a DOT that killed them, not really saved here
								activeCharacter.setDiedTo(monster, attackResult.getCastSpell());
							} else {
								attackResult = activeCharacter.attack(this, monster, dungeonEvents);
								monster.copy(processMonsterAttackResult(attackResult, dungeonEvents));
								activeCharacter.copy(processCharacterAttackResult(attackResult, aliveCharacters, dungeonEvents));
								if(attackResult.getMonsterSplashDamage() != null) {
									Damage splashDamage = attackResult.getMonsterSplashDamage();
									for(MonsterInstance splashMonster:monsterInstances) {
										// Splash damage doesn't hit original character again
										if(splashMonster.equals(monster)) continue;
										String description = splashDamage.getDamageAmount() + " " + 
												splashDamage.getDamageType().getNiceName() + " splash damage.";
										dungeonEvents.add(DungeonEvent.monsterTakeDamage(this, splashMonster, description));
										DungeonEvent.addDungeonEvents(dungeonEvents, 
												splashMonster.takeDamage(
														splashDamage.getDamageAmount(), splashDamage.getDamageType(),
														new ArrayList<EquipmentAttribute>(){{add(EquipmentAttribute.SPLASH_DAMAGE);}},
														activeCharacter.getLevel(), this, attackResult));
									}
								}
							}
							if(monster.isDead()) {
								killedXp += monster.getXp();
								String description = "";
								dungeonEvents.add(DungeonEvent.monsterDied(this, monster, description));
								description = "Characters share "+monster.getXp()+" XP";
								dungeonEvents.add(DungeonEvent.info(this, description));
								killMonster(monster);
								if(iter.hasNext()) {
									monster = iter.next();
								} else {
									allMonstersDead = true;								
								}
							}
						} // Not all monsters dead
					} // Loop all the characters
					aliveCharacterCount -= processCharacters(aliveCharacters, dungeonEvents);
					// Process the tick for the monster
					monster.processTick(this, dungeonEvents);
				} // Loop until either all monsters or all characters are dead
				if(aliveCharacterCount < 1) {
					StringBuilder description = new StringBuilder();
					description.append(monster.getMonster().getNiceName());
					description.append(" survived.");
					dungeonEvents.add(DungeonEvent.info(this, description.toString()));
				} else if (iterations >= MAX_ITERATIONS) {
					StringBuilder description = new StringBuilder();
					description.append(monster.getMonster().getNiceName());
					description.append(" was too tough, so they decided to skip it.");
					dungeonEvents.add(DungeonEvent.info(this, description.toString()));
				}
			}
			
			if(summonedMonsters.isEmpty()) {
				if(aliveCharacterCount < 1 && type != DungeonType.ADVENTURE) {
					// If all the characters are dead, fail the dungeon
					failed = true;
					dungeonEvents.add(DungeonEvent.info(this, type.getNiceName()+" Failed"));
				} else {
					for(Character character:characters) {
						StringBuilder description = new StringBuilder();
						description.append(character.getName());
						if(character.isDead()) {
							description.append(" was dead");
							if(type == DungeonType.ADVENTURE) {
								description.append(" and got a free resurrection.");
							} else {
								description.append("!");
							}
						} else {
							description.append(" ended with ");
							description.append(character.remainingHealth);
							description.append(" of ");
							description.append(character.maxHealth);
							description.append(" and ");
							description.append(character.mana);
							description.append(" of ");
							description.append(character.getCharClass().getBaseMana());
							description.append(" mana.");
							if(type == DungeonType.ADVENTURE) {
								// Normalise health xp on dungeon level
								long healthXp = Math.round(
										(type.getXpBase() * level) * 
										(1.0 * character.remainingHealth / character.maxHealth) * 
										monsters.size() / partySize);
								description.append(" Health converted to ");
								description.append(healthXp);
								description.append(" bonus XP.");
								killedXp += healthXp;
							} else {
								killedXp *= (monsters.size() / partySize);
							}
						}
						dungeonEvents.add(DungeonEvent.info(this, description.toString()));
					}
				}
			}
			monsterInstances = summonedMonsters;
			summonedMonsters = new ArrayList<MonsterInstance>(); 
			iter = monsterInstances.listIterator();
		}
		
		return dungeonEvents;
	}

	/**
	 * Process all the characters damage over time, healing, etc
	 * 
	 * @param aliveCharacters
	 * 
	 * @return number of characters that just died
	 */
	protected int processCharacters(List<Character> aliveCharacters, List<DungeonEvent> dungeonEvents) {
		int diedCount = 0;
		ListIterator<Character> iter = aliveCharacters.listIterator();
		while(iter.hasNext()) {
			Character character = iter.next();
			character.processTick(this, dungeonEvents);
			if(character.isDead()) {
				diedCount++;
				iter.remove();
				if(character.getDiedTo() == null) {
					// Earlier we set a died to if it was a monster that did it, so if it's not set here, it must be a DOT
					dungeonEvents.add(DungeonEvent.characterDied(this, character, 
						null, null, "The fatality was due to damage over time."));
				}
			}
		}
		return diedCount;
	}

	protected MonsterInstance processMonsterAttackResult(AttackResult attackResult, List<DungeonEvent> dungeonEvents) {
		MonsterInstance monster = attackResult.getMonster();
		Integer heal = attackResult.getMonsterHeal();
		if(heal != null) {
			String description = " for "+heal+ " health.";
			dungeonEvents.add(DungeonEvent.monsterHeal(this, monster, description));
			monster.heal(heal, this, dungeonEvents);
		}
		Integer speedChange = attackResult.getMonsterSpeed();
		if(speedChange != null) {
			monster.modifySpeed(speedChange, this, dungeonEvents);
		}
		
		return monster;
	}

	protected Character processCharacterAttackResult(AttackResult attackResult, List<Character> aliveCharacters, List<DungeonEvent> dungeonEvents) {
		Character character = attackResult.getCharacter();
		Integer heal = attackResult.getCharacterHeal();
		if(heal != null) {
			Character healCharacter = character;
			for(Character thisCharacter:aliveCharacters) {
				if(thisCharacter.getHealth() < healCharacter.getHealth()) {
					healCharacter = thisCharacter;
				}
			}
			String description = " for "+heal+ " health.";
			dungeonEvents.add(DungeonEvent.characterHeal(this, character, description));
			healCharacter.heal(heal);
		}
		Integer speedChange = attackResult.getCharacterSpeed();
		if(speedChange != null) {
			character.modifySpeed(speedChange, this, dungeonEvents);
		}
		
		return character;
	}

	@Override
	public String toString() {
		return toString(false, false);
	}
	
	public String toString(boolean showSimple, boolean showNice) {
		StringBuilder sb = new StringBuilder("Dungeon [");
		sb.append("id=");
		sb.append(id);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		sb.append("type=");
		sb.append(type);
		if(showSimple) {
			sb.append("accountId=");
			sb.append(account.getId());
		} else {
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}
			sb.append("account=");
			sb.append(account.toString(showNice));
		}
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}

		sb.append("level=");
		sb.append(level);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}

		sb.append("xpReward=");
		sb.append(xpReward);
		if(!showSimple) {
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}

			sb.append("itemRewards=");
			sb.append(itemRewards);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}
			
			sb.append("boostItemRewards=");
			sb.append(boostItemRewards);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}

			sb.append("monsters=");
			sb.append(monsters);
		}
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}

		sb.append("started=");
		sb.append(started);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}

		sb.append("expires=");
		sb.append(expires);
		if(!showSimple) {
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}

			sb.append("partySize=");
			sb.append(partySize);
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}

			sb.append("characters=");
			if(characters == null) {
				sb.append("null");
			} else {
				for(Character character:characters) {
					if(character != null) {
						sb.append(character.toString(false, showNice));
						if(showNice) {
							sb.append("\n\t");
						} else {
							sb.append(", ");
						}
					}
				}
			}
		}
		sb.append("]");
		
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dungeon other = (Dungeon) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
