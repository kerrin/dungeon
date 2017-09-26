package org.kerrin.dungeon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kerrin.dungeon.enums.DungeonEventType;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.enums.Spell;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.DifferentGameStates;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.DungeonNotRunable;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonEvent;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.MonsterInstance;
import org.kerrin.dungeon.repository.CharacterRepo;
import org.kerrin.dungeon.repository.DungeonEventRepo;
import org.kerrin.dungeon.repository.DungeonRepo;
import org.kerrin.dungeon.repository.EquipmentRepo;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DungeonServiceImpl extends ServiceHelppers implements DungeonService {
	private static final Logger logger = LoggerFactory.getLogger(DungeonServiceImpl.class);
	private final DungeonRepo dungeonRepo;
	private final DungeonEventRepo dungeonEventRepo;
	private final EquipmentRepo equipmentRepo;
	private final CharacterRepo characterRepo;

	@Autowired
    public DungeonServiceImpl(DungeonRepo dungeonRepo, DungeonEventRepo dungeonEventRepo, EquipmentRepo equipmentRepo,
    		CharacterRepo characterRepo) {
		super();
		this.dungeonRepo = dungeonRepo;
		this.dungeonEventRepo = dungeonEventRepo;
		this.equipmentRepo = equipmentRepo;
		this.characterRepo = characterRepo;
	}

	@Override
    @Transactional
    public Dungeon create(Dungeon dungeon) {
		logger.debug("Saving Dungeon: "+dungeon.toString());
        dungeon =  dungeonRepo.save(dungeon);
        loadDungeonLinkedTables(equipmentRepo, dungeon);
        return dungeon;
    }
     
    @Override
    @Transactional
    public Dungeon findById(long dungeonId) {
        Dungeon dungeon = dungeonRepo.findOne(dungeonId);
        
        if(dungeon != null) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
        }
        
        return dungeon;
    }
    
   @Override
   @Transactional
   public Dungeon findByEquipment(Equipment equipment) {
       Dungeon dungeon = dungeonRepo.findByItemRewardContains(equipment);
       
       if(dungeon != null) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
       }
       
       return dungeon;
   }
   
  @Override
  @Transactional
  public Dungeon findByBoostItem(BoostItem boostItem) {
      Dungeon dungeon = dungeonRepo.findByBoostItemRewardContains(boostItem);
      
      if(dungeon != null) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
      }
      
      return dungeon;
  }
 
	/**
	 * Delete the dungeon and anything associated with it (dungeon events, unclaimed items)
	 * This also deleted the associated dungeon events
	 * NOTE: Make sure items that are claimed are removed from item rewards, or they will get deleted in the database.
	 * 
	 * @param id	Dungeon id
	 * @return	Dungeon no longer in database
	 * @throws DungeonNotFound
	 * @throws EquipmentNotFound 
	 */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={DungeonNotFound.class, EquipmentNotFound.class})
    public Dungeon delete(Dungeon dungeon) throws DungeonNotFound, EquipmentNotFound {
        Dungeon deletedDungeon = dungeonRepo.findOne(dungeon.getId());
         
        if (deletedDungeon == null) {
            throw new DungeonNotFound(dungeon.getId());
        }
        
        dungeonEventRepo.deleteByDungeon(deletedDungeon);
        Map<Equipment, Boolean> unclaimedRewards = deletedDungeon.getItemRewards();
        for(Equipment equipment:unclaimedRewards.keySet()) {
        	equipmentRepo.delete(equipment);
        }
        
        dungeonRepo.delete(deletedDungeon);
        deletedDungeon.setId(-1);
        return deletedDungeon;
    }
 
    @Override
    @Transactional
    public List<Dungeon> findAll() {
        return dungeonRepo.findAll();
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={DungeonNotFound.class, AccountIdMismatch.class, EquipmentNotFound.class, BoostItemNotFound.class})
    public Dungeon update(Dungeon dungeon) throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, BoostItemNotFound {
        Dungeon updatedDungeon = dungeonRepo.findOne(dungeon.getId());
         
        if (updatedDungeon == null) {
            throw new DungeonNotFound(dungeon.getId());
        }

        Map<Equipment, Boolean> newItemRewards = dungeon.getItemRewards();
        // Remove any items no longer a reward
        Set<Equipment> previousItemRewards = updatedDungeon.getItemRewardsAsSet();
        Set<Equipment> removeEquipmentList = new HashSet<Equipment>();
        for(Equipment equipment: previousItemRewards) {
        	if(!newItemRewards.containsKey(equipment)) {
        		removeEquipmentList.add(equipment);
        	}
        }
        
        List<BoostItem> newBoostItemRewards = dungeon.getBoostItemRewards();
        // Remove any items no longer a reward
        List<BoostItem> previousBoostItemRewards = updatedDungeon.getBoostItemRewards();
        if(previousBoostItemRewards == null) previousBoostItemRewards = new ArrayList<BoostItem>();
        List<BoostItem> removeBoostItemList = new ArrayList<BoostItem>();
        for(BoostItem boostItem: previousBoostItemRewards) {
        	if(!newBoostItemRewards.contains(boostItem)) {
        		removeBoostItemList.add(boostItem);
        	}
        }
        
        updatedDungeon = new Dungeon(updatedDungeon, dungeon);
        updatedDungeon.setItemRewards(newItemRewards);
        updatedDungeon.setMonsters(dungeon.getMonsters());
        updatedDungeon.setDeadMonsters(dungeon.getDeadMonsters());
        
        dungeonRepo.save(updatedDungeon);
        return updatedDungeon;
    }

	@Override
	@Transactional
	public List<Dungeon> findAllByAccount(Account account) {
		List<Dungeon> dungeons = dungeonRepo.findAllByAccountOrderByLevelDescStartedDescXpRewardDesc(account);
		for(Dungeon dungeon:dungeons) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
		}
		
		return dungeons;
	}
	
	@Override
	@Transactional
	public List<Dungeon> findAllByAccount(Account account, boolean hardcore, boolean ironborn) {
		List<Dungeon> dungeons = dungeonRepo.findAllByAccountAndHardcoreAndIronbornOrderByLevelDescStartedDescXpRewardDesc(
				account, hardcore, ironborn);
		for(Dungeon dungeon:dungeons) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
		}
		
		return dungeons;
	}
	
	@Override
	@Transactional
	public List<Dungeon> findAllByLevelBetween(int greaterLevel, int lessThanLevel) {
		List<Dungeon> dungeons = dungeonRepo.findAllByLevelGreaterThanAndLevelLessThan(greaterLevel, lessThanLevel);
		for(Dungeon dungeon:dungeons) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
		}
		
		return dungeons;
	}

	@Override
	@Transactional
	public List<Dungeon> findAllByDungeonType(DungeonType type) {
		List<Dungeon> dungeons = dungeonRepo.findAllByType(type);
		for(Dungeon dungeon:dungeons) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
		}
		
		return dungeons;
	}

	@Override
	@Transactional
	public List<Dungeon> findAllByAccountAndActive(Account account) {
		List<Dungeon> dungeons = dungeonRepo.findAllByAccountAndStartedNotNull(account);
		for(Dungeon dungeon:dungeons) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
		}
		
		return dungeons;
	}

	@Override
	@Transactional
	public List<Dungeon> findAllByXpRewardBetween(long greaterThanXpReward, long lessThanXpReward) {
		List<Dungeon> dungeons = dungeonRepo.findAllByXpRewardGreaterThanAndXpRewardLessThan(greaterThanXpReward, lessThanXpReward);
		for(Dungeon dungeon:dungeons) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
		}
		
		return dungeons;
	}

	@Override
	@Transactional
	public List<Dungeon> findAllByPartySizeBetween(int greaterThanPartySize, int lessThanPartySize) {
		List<Dungeon> dungeons = dungeonRepo.findAllByPartySizeGreaterThanAndPartySizeLessThan(greaterThanPartySize, lessThanPartySize);
		for(Dungeon dungeon:dungeons) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
		}
		
		return dungeons;
	}

	@Override
	@Transactional
	public List<Dungeon> findAllByStartedBetween(Date startedAfter, Date startedBefore) {
		List<Dungeon> dungeons = dungeonRepo.findAllByStartedGreaterThanAndStartedLessThan(startedAfter, startedBefore);
		for(Dungeon dungeon:dungeons) {
			loadDungeonLinkedTables(equipmentRepo, dungeon);
		}
		
		return dungeons;
	}

	@Override
	@Transactional
	public List<Dungeon> findAllByAccountAndFinished(Account account) {
		List<Dungeon> dungeons = dungeonRepo.findAllByAccountOrderByLevelDescStartedDescXpRewardDesc(account);
		List<Dungeon> finishedDungeons = new ArrayList<Dungeon>();
		// Process dungeons 5 seconds before they finish
		Date processDate = new Date(System.currentTimeMillis() + 5000);
		for(Dungeon dungeon:dungeons) {
			Date doneDate = dungeon.getDoneDate();
			if(doneDate != null && doneDate.before(processDate)) {
				loadDungeonLinkedTables(equipmentRepo, dungeon);
				finishedDungeons.add(dungeon);
			}
		}
		return finishedDungeons;
	}

	@Override
	@Transactional
	public List<Dungeon> findAllByAccountAndFinished(Account account, boolean successfull) {
		List<Dungeon> dungeons = dungeonRepo.findAllByAccountOrderByLevelDescStartedDescXpRewardDesc(account);
		List<Dungeon> finishedDungeons = new ArrayList<Dungeon>();
		for(Dungeon dungeon:dungeons) {
			Date doneDate = dungeon.getDoneDate();
			if(doneDate != null && doneDate.before(new Date()) && dungeon.isFailed() != successfull) {
				loadDungeonLinkedTables(equipmentRepo, dungeon);
				finishedDungeons.add(dungeon);
			}
		}
		return finishedDungeons;
	}

	@Override
	@Transactional
	public List<Dungeon> findAllByAccountAndExpired(Account account) {
		List<Dungeon> dungeons = dungeonRepo.findAllByAccountOrderByLevelDescStartedDescXpRewardDesc(account);
		List<Dungeon> expiredDungeons = new ArrayList<Dungeon>();
		for(Dungeon dungeon:dungeons) {			
			Date expires = dungeon.getExpires();
			if(expires != null && expires.before(new Date())) {
				loadDungeonLinkedTables(equipmentRepo, dungeon);
				expiredDungeons.add(dungeon);
			}
		}
		return expiredDungeons;
	}
	
	@Override
	public Map<Monster, MonsterType> generateMonsters(int level, DungeonType dungeonType, int overrideNumberOfMonsters) {
		Map<Monster, MonsterType> monsters = new HashMap<Monster, MonsterType>();
		
		int monsterMin = dungeonType.getMinMonsters();
		int monsterDeviation = dungeonType.getMaxMonsters() - dungeonType.getMinMonsters();
		if(monsterDeviation < 0) {
			monsterDeviation = 0;
		}
		int monsterCount = (int)(Math.random()*monsterDeviation)+monsterMin;
		if(overrideNumberOfMonsters > 0) monsterCount = overrideNumberOfMonsters;
		for(MonsterType monsterType:dungeonType.getMonsterTypes()) {
			switch(monsterType) {
			case TRASH: case ELITE: case BOSS:
				monsters.put(Monster.getRandom(), monsterType);
				break;
			case RANDOM_ALL:
				for(;monsterCount > 0; monsterCount--) {
					monsters.put(Monster.getRandom(), MonsterType.getRandomType());
				}
				break;
			case RANDOM_NOT_BOSS:
				for(;monsterCount > 0; monsterCount--) {
					MonsterType tempMonsterType = MonsterType.getRandomType();
					if(tempMonsterType.equals(MonsterType.BOSS)) continue;
					monsters.put(Monster.getRandom(), tempMonsterType);
				}
				break;
			case NONE: 
				break;
			}
		}
		
		return monsters;
	}
	
	/**
	 * Run the dungeon.
	 * Assigns all the characters to the dungeon and runs the dungeon getting all the dungeon events and storing them
	 * marking characters that died as dead
	 * 
	 * @param dungeonId
	 * @param dungeonStartForm
	 * @param dungeon
	 * @throws CharacterNotFound
	 * @throws AccountIdMismatch
	 * @throws DungeonNotFound
	 * @throws DungeonNotRunable
	 * @throws EquipmentNotFound 
	 * @throws DifferentGameStates 
	 * @throws BoostItemNotFound 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={
			CharacterNotFound.class, AccountIdMismatch.class, DungeonNotFound.class, DungeonNotRunable.class, 
			EquipmentNotFound.class, BoostItemNotFound.class})
	@Override
	public Dungeon startDungeon(Dungeon dungeon, long[] characterIds, int levelAdjustment,
			EquipmentService equipmentService, CharacterEquipmentService characterEquipmentService)
			throws CharacterNotFound, AccountIdMismatch, DungeonNotFound, DungeonNotRunable, EquipmentNotFound, DifferentGameStates, BoostItemNotFound {
		List<Character> characters = new ArrayList<Character>();
		for(long characterId:characterIds) {
			Character character = characterRepo.findOne(characterId);
			if(characters.contains(character)) {
				throw new DungeonNotRunable("Tried to send character Id "+characterId+" twice");
			}
			if(character == null) {
				if(characterId > 0) {
					throw new CharacterNotFound(characterId);
				}
			} else {
				if(character.getDungeon() != null) {
					throw new DungeonNotRunable("Character "+characterId+" already running dungeon "+character.getDungeon());
				}
				character.setDungeon(dungeon);
				characterRepo.save(character);
				characters.add(character);
			}
		}
		if(characters.size() < dungeon.getType().getMinCharacters()) {
			throw new DungeonNotRunable("You need more party members");
		} else if(characters.size() > dungeon.getType().getMaxCharacters()) {
			throw new DungeonNotRunable("You have too many party members");
		}
		dungeon.setPartySize(characters.size());
		dungeon.setCharacters(characters);
		if(levelAdjustment != 0) {
			int currentLevel = dungeon.getLevel();
			if(currentLevel <= -levelAdjustment) {
				throw new DungeonNotRunable("Attempt to change level below level 1. "
						+ "Current level "+ currentLevel + ", level adjustment requested " + levelAdjustment);
			}
			modifyDungeonEquipmentLevel(equipmentService, dungeon, currentLevel + levelAdjustment);
			dungeon.setLevel(currentLevel + levelAdjustment);
			dungeon.setXpReward(dungeon.getType().getXpBase() * dungeon.getLevel());
		}
		
		update(dungeon);

		List<DungeonEvent> dungeonEvents = dungeon.start(characterEquipmentService, new Date());
		dungeon = dungeonRepo.save(dungeon);
		
		int order = 1;			
		for(DungeonEvent event:dungeonEvents) {
			event.setEventOrder(order++);
			dungeonEventRepo.save(event);
			if(event.getEventType() == DungeonEventType.CHARACTER_DIED && dungeon.getType() != DungeonType.ADVENTURE) {
				Character character = event.getCharacter();
				
				StringBuilder deathDescription = new StringBuilder("");
				Integer monsterId = event.getMonsterId();
				MonsterInstance monsterInstance = null;
				if(monsterId != null) {
					Monster monster = Monster.fromId(monsterId);
					if(monster != null && !monster.equals(Monster.UNKNOWN)) {
						MonsterType monsterType = MonsterType.fromId(event.getMonsterTypeId());
						monsterInstance = new MonsterInstance(
							monster, monsterType, dungeon.getLevel(), dungeon.getPartySize());						
					}
				}
				Integer spellId = event.getSpellId();
				Spell spell = null;
				if(spellId != null) {
					spell = Spell.fromId(spellId);
				}
				if(deathDescription.equals("")) {
					// No specific monster or spell
					deathDescription.append("Damage over time");
				}
				character.setDiedTime(dungeon.getDoneDate());
				character.setDiedTo(monsterInstance, spell);
				characterRepo.save(character);
			}
		}
		
		return dungeon;
	}

	/**
	 * Change all the equipment on a dungeon to a specified level
	 * Note: Call this BEFORE adjusting the level of the dungeon
	 * 
	 * @param equipmentService
	 * @param dungeon
	 * @param newLevel
	 * @throws EquipmentNotFound
	 */
	private void modifyDungeonEquipmentLevel(EquipmentService equipmentService, Dungeon dungeon, int newLevel) 
			throws EquipmentNotFound {
		Map<Equipment, Boolean> itemRewards = dungeon.getItemRewards();
		int realEquipmentNewLevel;
		if(newLevel > 60) {
			realEquipmentNewLevel = 60;
		} else {
			realEquipmentNewLevel = newLevel;
		}
		for(Equipment equipment:itemRewards.keySet()) {
			chanceToChangeQuality(equipment, dungeon.getLevel(), newLevel, dungeon.getType());
			equipment.setLevel(realEquipmentNewLevel);
			equipmentService.update(equipment, false);
		}
	}

	/**
	 * 
	 * @param equipment	equipment to modify
	 * @param oldLevel	dungeon level before change
	 * @param newLevel	dungeon level after change
	 */
	private void chanceToChangeQuality(Equipment equipment, int oldLevel, int newLevel, DungeonType dungeonType) {
		if(newLevel < Character.MAX_LEVEL) return; 
		EquipmentQuality realQuality = equipment.getQuality();
		int newLevelsAboveMax = newLevel - Character.MAX_LEVEL;
		if(newLevelsAboveMax < 0) newLevelsAboveMax = 0;
		int oldLevelsAboveMax = oldLevel - Character.MAX_LEVEL;
		if(oldLevelsAboveMax < 0) oldLevelsAboveMax = 0;
		int levelsChangedAboveMax = newLevelsAboveMax - oldLevelsAboveMax;
		if(levelsChangedAboveMax < 0) {
			EquipmentQuality previousQuality = equipment.getQuality();
			previousQuality = previousQuality.decrease();
			int qualityDecrease = (int)(Math.random()*(Math.abs(levelsChangedAboveMax)));
			int chance = 1000;
			while(qualityDecrease > 0) {
				int random = (int) (Math.random()*chance);
				if(random < previousQuality.getRewardRollChance(dungeonType)) {
					logger.debug("Decreasing quality from {}", realQuality);
					realQuality = previousQuality;
					previousQuality = previousQuality.decrease();
				}
				qualityDecrease--;
			}
		} else if(levelsChangedAboveMax > 0) {
			EquipmentQuality nextQuality = equipment.getQuality();
			nextQuality = nextQuality.increase();
			int qualityIncrease = (int)(Math.random()*(levelsChangedAboveMax));
			int chance = 1000;
			while(qualityIncrease > 0) {
				int random = (int) (Math.random()*chance);
				if(random < nextQuality.getRewardRollChance(dungeonType)) {
					logger.debug("Increasing quality from {}", realQuality);
					realQuality = nextQuality;
					nextQuality = nextQuality.increase();
				}
				qualityIncrease--;
			}
		}
	}

	@Override
	@Transactional
	public void deleteAllByAccountAndHardcoreAndIronborn(Account account, boolean hardcore, boolean ironborn) {
		List<Dungeon> deletedDungeons = dungeonRepo.findAllByAccountAndHardcoreAndIronbornAndStartedNull(account, hardcore, ironborn);
        
		for(Dungeon deleteDungeon:deletedDungeons) {
	        dungeonEventRepo.deleteByDungeon(deleteDungeon);
	        Map<Equipment, Boolean> unclaimedRewards = deleteDungeon.getItemRewards();
	        for(Equipment equipment:unclaimedRewards.keySet()) {
	        	equipmentRepo.delete(equipment);
	        }
	        
	        dungeonRepo.delete(deleteDungeon);
		}
	}
}
