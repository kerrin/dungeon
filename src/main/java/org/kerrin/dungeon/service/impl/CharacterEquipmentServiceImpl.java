package org.kerrin.dungeon.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.repository.CharacterEquipmentRepo;
import org.kerrin.dungeon.repository.CharacterRepo;
import org.kerrin.dungeon.repository.EquipmentRepo;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CharacterEquipmentServiceImpl extends ServiceHelppers implements CharacterEquipmentService {
	private static final Logger logger = LoggerFactory.getLogger(CharacterEquipmentServiceImpl.class);

	private final CharacterEquipmentRepo characterEquipmentRepo;
	private final CharacterRepo characterRepo;
	private final EquipmentRepo equipmentRepo;
	
	@Autowired
    public CharacterEquipmentServiceImpl(CharacterEquipmentRepo characterEquipmentRepo, 
    		CharacterRepo characterRepo, EquipmentRepo equipmentRepo) {
		super();
		this.characterEquipmentRepo = characterEquipmentRepo;
		this.characterRepo = characterRepo;
		this.equipmentRepo = equipmentRepo;
	}

	@Override
    @Transactional
    public CharacterEquipment create(CharacterEquipment characterEquipment) {
    	CharacterEquipment createdCharacterEquipment = characterEquipment;
        return characterEquipmentRepo.save(createdCharacterEquipment);
    }
     
    @Override
    @Transactional
    public CharacterEquipment findById(long characterId) {
        CharacterEquipment characterEquipment = characterEquipmentRepo.findOne(characterId);
        
        if(characterEquipment != null) {
        	loadCharSlotsLinkedTables(equipmentRepo, characterEquipment);
        } else {
        	logger.error("No character slots found for character {}", characterId);
        }
        
        return characterEquipment;
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={CharacterEquipmentNotFound.class, CharacterSlotNotFound.class})
    public CharacterEquipment delete(Character character) 
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
			CharacterEquipment characterEquipment = findById(character.getId());
			if(characterEquipment == null) {
        		throw new CharacterEquipmentNotFound();
        	}        	
        	characterEquipment.setCharacterSlot(charSlot, null);
        	characterEquipmentRepo.save(characterEquipment);
		}
        characterEquipmentRepo.delete(deletedCharacterEquipment);
        return deletedCharacterEquipment;
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={CharacterEquipmentNotFound.class, CharacterNotFound.class})
    public CharacterEquipment update(CharacterEquipment characterEquipment) 
    		throws CharacterEquipmentNotFound, CharacterNotFound {
        Character character = characterRepo.getOne(characterEquipment.getCharacterId());
        if(character == null) {
        	throw new CharacterNotFound();
        }
        CharacterEquipment updatedCharacterEquipment = characterEquipmentRepo.findOne(characterEquipment.getCharacterId());
         
        if (updatedCharacterEquipment == null) {
            throw new CharacterEquipmentNotFound();
        }
        updatedCharacterEquipment.setCharacter(character);
        updatedCharacterEquipment.setCharacterSlots(characterEquipment.getCharacterSlots());
        
        characterEquipmentRepo.save(updatedCharacterEquipment);
        return updatedCharacterEquipment;
    }

	@Override
	@Transactional
	public Map<CharSlot, Equipment> findAllByCharacter(Character character) {
		CharacterEquipment characterEquipment = characterEquipmentRepo.findOne(character.getId());
		if(characterEquipment == null) return null;
		
		loadCharSlotsLinkedTables(equipmentRepo, characterEquipment);
        
		return characterEquipment.getCharacterSlots();
	}

	@Override
	@Transactional
	public Equipment findEquipmentForCharacterAndCharSlot(Character character, CharSlot charSlot) {
		CharacterEquipment characterEquipment = characterEquipmentRepo.findOne(character.getId());
		if(characterEquipment == null) return null;
		
		loadCharSlotsLinkedTables(equipmentRepo, characterEquipment);
        
		return characterEquipment.getCharacterSlots().get(charSlot);
	}

	@Override
	@Transactional
	public Equipment equipmentItem(Character character, CharSlot charSlot, Equipment equipment) {
		CharacterEquipment characterEquipment = characterEquipmentRepo.findOne(character.getId());
		if(characterEquipment == null) return null;
		Equipment oldEquipment = characterEquipment.getCharacterSlot(charSlot);
		characterEquipment.setCharacterSlot(charSlot, equipment);
		characterEquipmentRepo.save(characterEquipment);
		return oldEquipment;
	}

	@Override
	@Transactional
	public CharSlot unequipmentItem(Character character, Equipment equipment) {
		CharSlot inSlot = null;
		CharacterEquipment characterEquipment = characterEquipmentRepo.findOne(character.getId());
		if(characterEquipment == null) {
			return null;
		}
		List<CharSlot> validSlots = equipment.getEquipmentType().getValidSlots();
		for(CharSlot thisSlot:validSlots) {
			Equipment checkEquipment = characterEquipment.getCharacterSlot(thisSlot);
			if(equipment.equals(checkEquipment)) {
				characterEquipment.setCharacterSlot(thisSlot, null);
				inSlot = thisSlot;
			}
		}
		if(inSlot == null) {
			logger.error("Unable to unequip {} on character {}", equipment, character);
			return null;
		}
		characterEquipmentRepo.save(characterEquipment);
		return inSlot;
	}

	@Override
	@Transactional
	public Equipment unequipmentItem(Character character, CharSlot charSlot) {
		CharacterEquipment characterEquipment = characterEquipmentRepo.findOne(character.getId());
		if(characterEquipment == null) return null;
		Equipment oldEquipment = characterEquipment.getCharacterSlot(charSlot);
		characterEquipment.setCharacterSlot(charSlot, null);
		characterEquipmentRepo.save(characterEquipment);
		return oldEquipment;
	}

	@Override
	@Transactional
	public CharSlot findCharSlotForEquipment(Character character, Equipment equipment) {
		CharacterEquipment characterEquipment = characterEquipmentRepo.findOne(character.getId());
		if(characterEquipment == null) {
			return null;
		}
		List<CharSlot> validSlots = equipment.getEquipmentType().getValidSlots();
		for(CharSlot thisSlot:validSlots) {
			Equipment checkEquipment = characterEquipment.getCharacterSlot(thisSlot);
			if(equipment.equals(checkEquipment)) {
				return thisSlot;
			}
		}
		
		return null;
	}
}
