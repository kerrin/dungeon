package org.kerrin.dungeon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Hiscore;
import org.kerrin.dungeon.repository.CharacterEquipmentRepo;
import org.kerrin.dungeon.repository.CharacterRepo;
import org.kerrin.dungeon.repository.EquipmentRepo;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CharacterServiceImpl extends ServiceHelppers implements CharacterService {
	private static final Logger logger = LoggerFactory.getLogger(CharacterServiceImpl.class);
	
	private final CharacterRepo characterRepo;
	private final CharacterEquipmentRepo characterEquipmentRepo;
	private final EquipmentRepo equipmentRepo;
	private final HiscoreService hiscoreService;

	@Autowired
    public CharacterServiceImpl(CharacterRepo characterRepo, 
    		CharacterEquipmentRepo characterEquipmentRepo, EquipmentRepo equipmentRepo,
    		HiscoreService hiscoreService) {
		super();
		this.characterRepo = characterRepo;
		this.characterEquipmentRepo = characterEquipmentRepo;
		this.equipmentRepo = equipmentRepo;
		this.hiscoreService = hiscoreService;
	}

	@Override
    @Transactional
    public Character create(Character character) {
    	Character createdCharacter = character;
        return characterRepo.save(createdCharacter);
    }
     
    @Override
    @Transactional
    public Character findById(long id) {
        Character character = characterRepo.findOne(id);
        loadCharacterLinkedTables(equipmentRepo, character);
        return character;
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=CharacterNotFound.class)
    public Character delete(Character character) 
    		throws CharacterNotFound, CharacterEquipmentNotFound, CharacterSlotNotFound {
        Character deletedCharacter = characterRepo.findOne(character.getId());
         
        if (deletedCharacter == null) {
            throw new CharacterNotFound(character.getId());
        }
        
        deleteCharacterEquipment(deletedCharacter);
        
        characterRepo.delete(deletedCharacter);
        return deletedCharacter;
    }
 
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={CharacterEquipmentNotFound.class, CharacterSlotNotFound.class})
    public CharacterEquipment deleteCharacterEquipment(Character character) 
    		throws CharacterEquipmentNotFound, CharacterSlotNotFound {
        CharacterEquipment deletedCharacterEquipment = characterEquipmentRepo.findOne(character.getId());
         
        if (deletedCharacterEquipment == null) {
            throw new CharacterEquipmentNotFound();
        }
        Map<CharSlot, Equipment> equipmentSlots = deletedCharacterEquipment.getCharacterSlots();
        Set<CharSlot> charSlots = equipmentSlots.keySet();
        List<CharSlot> deleteSlots = new ArrayList<CharSlot>();
		for(CharSlot charSlot:charSlots) {
        	Equipment equipment = equipmentSlots.get(charSlot);
        	equipmentRepo.delete(equipment.getId());
        	deleteSlots.add(charSlot);
        }
		for(CharSlot charSlot:deleteSlots) {
			CharacterEquipment characterEquipment = findCharacterEquipmentById(character.getId());
			if(characterEquipment == null) {
        		throw new CharacterEquipmentNotFound();
        	}        	
        	characterEquipment.setCharacterSlot(charSlot, null);
        	characterEquipmentRepo.save(characterEquipment);
		}
        characterEquipmentRepo.delete(deletedCharacterEquipment);
        return deletedCharacterEquipment;
    }
    
    @Transactional
    public CharacterEquipment findCharacterEquipmentById(long characterId) {
        CharacterEquipment characterEquipment = characterEquipmentRepo.findOne(characterId);
        
        if(characterEquipment != null) {
        	loadCharSlotsLinkedTables(equipmentRepo, characterEquipment);
        } else {
        	logger.error("No character slots found for character {}", characterId);
        }
        
        return characterEquipment;
    }
    
    @Override
    @Transactional
    public List<Character> findAll() {
        return characterRepo.findAll();
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=CharacterNotFound.class)
    public void update(Character character) throws CharacterNotFound {
        Character updatedCharacter = characterRepo.findOne(character.getId());
         
        if (updatedCharacter == null)
            throw new CharacterNotFound(character.getId());
         
        updatedCharacter.setAccount(character.getAccount());
        updatedCharacter.setHardcore(character.isHardcore());
        updatedCharacter.setIronborn(character.isIronborn());
        updatedCharacter.setName(character.getName());
        updatedCharacter.setCharClass(character.getCharClass());
        updatedCharacter.setLevel(character.getLevel());
        updatedCharacter.setXp(character.getXp());
        updatedCharacter.setPrestigeLevel(character.getPrestigeLevel());
        updatedCharacter.setDungeon(character.getDungeon());
        updatedCharacter.setDiedTime(character.getDeathTime());
        updatedCharacter.setDiedTo(character.getDiedTo());
        updatedCharacter.setUsedLevelUp(character.isUsedLevelUp());
        
        updatedCharacter = characterRepo.save(updatedCharacter);
        
        Hiscore hiscore = hiscoreService.highestLevel(
        		updatedCharacter.getAccount(), updatedCharacter.isHardcore(), updatedCharacter.isIronborn());
		long combinedScore = updatedCharacter.getCombinedLevel();
		if(hiscore.getScore() < combinedScore) {
			hiscore.setScore(combinedScore);
			hiscoreService.update(hiscore);
		}
		if(updatedCharacter.getLevel() >= Character.MAX_LEVEL && !updatedCharacter.isUsedLevelUp()) {
			Date now = new Date();
			Date characterCreated = updatedCharacter.getCreatedDateTime();
			if(characterCreated != null) {
				long duration = now.getTime() - characterCreated.getTime();
				hiscore = hiscoreService.fastestMaxLevel(
		        		updatedCharacter.getAccount(), updatedCharacter.isHardcore(), updatedCharacter.isIronborn());
				if(hiscore.getScore() < duration) {
					hiscore.setScore(duration);
				}
			} else {
				logger.error("Character {} need created date", updatedCharacter);
			}
			
			Date accountLastReset = updatedCharacter.getAccount().getLastResetDateTime(
					updatedCharacter.isHardcore(), 
					updatedCharacter.isIronborn());
			if(accountLastReset != null) {
				long duration = now.getTime() - accountLastReset.getTime();
				hiscore = hiscoreService.fastestMaxLevelAfterReset(
		        		updatedCharacter.getAccount(), updatedCharacter.isHardcore(), updatedCharacter.isIronborn());
				if(hiscore.getScore() < duration) {
					hiscore.setScore(duration);
				}
			} else {
				logger.error("Account {} need reset date", updatedCharacter.getAccount());
			}
		}
        //loadLinkedTables(updatedCharacter);
        //return updatedCharacter;
    }

	@Override
	@Transactional
	public List<Character> findAllByAccountOrderByLevel(Account account) {
		List<Character> characters = characterRepo.findAllByAccountOrderByLevelDesc(account);
		for(Character character:characters) {
			loadCharacterLinkedTables(equipmentRepo, character);
		}
		return characters;
	}

	@Override
	@Transactional
	public List<Character> findAllByAccountOrderByLevel(Account account, boolean hardcore, boolean ironborn) {
		List<Character> characters = characterRepo.findAllByAccountAndHardcoreAndIronbornOrderByLevelDesc(
				account, hardcore, ironborn);
		for(Character character:characters) {
			loadCharacterLinkedTables(equipmentRepo, character);
		}
		return characters;
	}

	@Override
	public List<Character> findAllIdleByAccountOrderByLevel(Account account, boolean hardcore, boolean ironborn) {
		List<Character> characters = characterRepo.findAllByAccountAndHardcoreAndIronbornAndDungeonIsNullAndDeathClockIsNullOrderByLevelDesc(
				account, hardcore, ironborn);
		for(Character character:characters) {
			loadCharacterLinkedTables(equipmentRepo, character);
		}
		return characters;
	}

	@Override
	public boolean allAccountCharactersDead(Account account, boolean hardcore, boolean ironborn) {
		List<Character> characters = characterRepo.findAllByAccountAndHardcoreAndIronbornAndDeathClockIsNull(
				account, hardcore, ironborn);
		return characters.isEmpty();
	}
	
	@Override
	@Transactional
	public List<Character> findAllByName(String name) {
		List<Character> characters = characterRepo.findAllByName(name);
		for(Character character:characters) {
			loadCharacterLinkedTables(equipmentRepo, character);
		}
		return characters;
	}

	@Override
	@Transactional
	public List<Character> findAllByCharacterClass(CharClass charClassId) {
		List<Character> characters = characterRepo.findAllByCharClass(charClassId);
		for(Character character:characters) {
			loadCharacterLinkedTables(equipmentRepo, character);
		}
		return characters;
	}

	@Override
	@Transactional
	public List<Character> findAllByLevelGreaterThanAndLevelLessThan(int greaterThanLevel, int lessThanLevel) {
		List<Character> characters = characterRepo.findAllByLevelGreaterThanAndLevelLessThan(greaterThanLevel, lessThanLevel);
		for(Character character:characters) {
			loadCharacterLinkedTables(equipmentRepo, character);
		}
		return characters;
	}

	@Override
	@Transactional
	public List<Character> findAllByXpGreaterThanAndXpLessThan(long greaterThanXp, long lessThanXp) {
		List<Character> characters = characterRepo.findAllByXpGreaterThanAndXpLessThan(greaterThanXp, lessThanXp);
		for(Character character:characters) {
			loadCharacterLinkedTables(equipmentRepo, character);
		}
		return characters;
	}

	@Override
	@Transactional
	public List<Character> findAllByPrestigeLevelGreaterThanAndPrestigeLevelLessThan(int greaterThanLevel, int lessThanLevel) {
		List<Character> characters = characterRepo.findAllByPrestigeLevelGreaterThanAndPrestigeLevelLessThan(greaterThanLevel, lessThanLevel);
		for(Character character:characters) {
			loadCharacterLinkedTables(equipmentRepo, character);
		}
		return characters;
	}

	@Override
	@Transactional
	public List<Character> findAllByDungeon(Dungeon dungeon) {
		List<Character> characters = characterRepo.findAllByDungeon(dungeon);
		for(Character character:characters) {
			loadCharacterLinkedTables(equipmentRepo, character);
		}
		return characters;
	}

	@Override
	public long getTotalLevel(Account account, boolean hardcore, boolean ironborn) {
		List<Character> characters = findAllByAccountOrderByLevel(account, hardcore, ironborn);
		long totalLevel = 0;
		for(Character character : characters) {
			totalLevel += character.getCombinedLevel();
		}
		return totalLevel;
	}
}
