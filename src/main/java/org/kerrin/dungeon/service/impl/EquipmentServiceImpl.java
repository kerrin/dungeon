package org.kerrin.dungeon.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CantEquipToDungeon;
import org.kerrin.dungeon.exception.CantEquipToMessage;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryException;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.exception.MessageEquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.StashSlotItem;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.repository.AccountMessageRepo;
import org.kerrin.dungeon.repository.AccountRepo;
import org.kerrin.dungeon.repository.BoostItemRepo;
import org.kerrin.dungeon.repository.EquipmentRepo;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.InventoryService;
import org.kerrin.dungeon.service.ReloadPanels;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.kerrin.dungeon.service.StashSlotItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipmentServiceImpl extends ServiceHelppers implements EquipmentService {
	private static final Logger logger = LoggerFactory.getLogger(EquipmentServiceImpl.class);

	private final AccountRepo accountRepo;
	private final EquipmentRepo equipmentRepo;
	private final CharacterService characterService;
	private final CharacterEquipmentService characterEquipmentService;
	private final InventoryService inventoryService;
	private final BoostItemRepo boostItemRepo;
	private final StashSlotItemService stashSlotItemService;
	private final AccountMessageRepo accountMessageRepo;
	private final DungeonService dungeonService;
	
	@Autowired
    public EquipmentServiceImpl(EquipmentRepo equipmentRepo, AccountRepo accountRepo, BoostItemRepo boostItemRepo,
    		CharacterService characterService, 
    		CharacterEquipmentService characterEquipmentService, 
    		InventoryService inventoryService, DungeonService dungeonService,
    		StashSlotItemService stashSlotItemService, AccountMessageRepo accountMessageRepo) {
		super();
		this.equipmentRepo = equipmentRepo;
		this.accountRepo = accountRepo;
		this.boostItemRepo = boostItemRepo;
		this.characterService = characterService;
		this.characterEquipmentService = characterEquipmentService;
		this.inventoryService = inventoryService;
		this.stashSlotItemService = stashSlotItemService;
		this.dungeonService = dungeonService;
		this.accountMessageRepo = accountMessageRepo;
	}

	@Override
    @Transactional
    public Equipment create(Equipment equipment) {
    	return equipmentRepo.save(equipment);
    }

	@Override
    @Transactional(propagation = Propagation.REQUIRED, 
    	rollbackFor={EquipmentNotFound.class, DungeonNotFound.class, AccountIdMismatch.class, InventoryNotFound.class, CharacterSlotNotFound.class, CharacterEquipmentNotFound.class})
    public Equipment delete(Account account, Equipment equipment) 
    		throws EquipmentNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound, 
    				CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, BoostItemNotFound, MessageEquipmentNotFound, InventoryException {
        Equipment deletedEquipment = equipmentRepo.findOne(equipment.getId());
         
        if (deletedEquipment == null) throw new EquipmentNotFound();
        
        switch(deletedEquipment.getEquipmentLocation()) {
        case NONE:
        	break;
        case CHARACTER:
        	long characterId = deletedEquipment.getEquipmentLocationId();
        	Character character = characterService.findById(characterId);
        	if(character == null) {
        		throw new CharacterNotFound(characterId);
        	}
        	CharSlot charSlot = characterEquipmentService.findCharSlotForEquipment(character, deletedEquipment);
        	if(charSlot == null) {
        		throw new CharacterSlotNotFound();
        	}        	
			CharacterEquipment characterEquipment = characterEquipmentService.findById(characterId);
			if(characterEquipment == null) {
        		throw new CharacterEquipmentNotFound();
        	}        	
        	characterEquipment.setCharacterSlot(charSlot, null);
        	characterEquipmentService.update(characterEquipment);
        	break;
        case DUNGEON:
        	Dungeon dungeon = dungeonService.findById(deletedEquipment.getEquipmentLocationId());
        	if(dungeon == null) {
        		throw new DungeonNotFound();
        	}        	
        	dungeon.removeItemReward(deletedEquipment);
        	dungeonService.update(dungeon);
        	break;
        case INVENTORY:
        	StashSlotItemSuper checkItem = inventoryService.getItemInSlot(account, 
        			deletedEquipment.isHardcore(),deletedEquipment.isIronborn(), (int)deletedEquipment.getEquipmentLocationId());
        	if(checkItem.equals(deletedEquipment)) {
        		inventoryService.removeFromInventory(account, deletedEquipment);
        	} else {
        		return null;
        	}
        	
        	break;
        case MESSAGE:
        	AccountMessage message = accountMessageRepo.findOne(deletedEquipment.getEquipmentLocationId());
        	if(message.getAttachedItemId() == deletedEquipment.getId()) {
        		message.setAttachedItemId(-1);
        	} else {
        		return null;
        	}
        	
        	break;
        }
        
        equipmentRepo.delete(deletedEquipment);
        return deletedEquipment;
    }
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={CharacterNotFound.class, CharacterSlotNotFound.class, CharacterEquipmentNotFound.class, 
			EquipmentNotFound.class, BoostItemNotFound.class, DungeonNotFound.class, AccountIdMismatch.class, 
			InventoryNotFound.class, MessageEquipmentNotFound.class, InventoryException.class})
	public void removeItemFromMessage(AccountMessage message)
			throws CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, EquipmentNotFound, 
				BoostItemNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound, MessageEquipmentNotFound, InventoryException {
		Account account = accountRepo.getOne(message.getAccountId());
		StashSlotItemSuper.TYPE itemType = message.getAttachedItemType();
		if(itemType == StashSlotItem.TYPE.EQUIPMENT) {
			Equipment equipment = findById(message.getAttachedItemId());
			delete(account, equipment);
		} else if(itemType == StashSlotItem.TYPE.BOOST_ITEM) {
			BoostItem boostItem = boostItemRepo.findOne(message.getAttachedItemId());
			stashSlotItemService.doRemove(boostItem, account);
		} else {
			throw new MessageEquipmentNotFound(message);
		}
	}
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=EquipmentNotFound.class)
    public Equipment update(Equipment equipment, boolean populateAttributes) throws EquipmentNotFound {
        Equipment updatedEquipment = equipmentRepo.findOne(equipment.getId());
         
        if (updatedEquipment == null) {
            throw new EquipmentNotFound();
        }
         
        // Set the base, defence, ancient attributes before setting the general ones, as the general ones may change them
        // if the version has changed
        updatedEquipment.setBaseAttribute(equipment.getBaseAttribute());
        updatedEquipment.setBaseAttributeValue(equipment.getBaseAttributeValue());
        updatedEquipment.setDefenceAttribute(equipment.getDefenceAttribute());
        updatedEquipment.setDefenceAttributeValue(equipment.getDefenceAttributeValue());
        updatedEquipment.setAncientAttribute(equipment.getAncientAttribute());
        updatedEquipment.setAncientAttributeValue(equipment.getAncientAttributeValue());
        updatedEquipment.setQuality(equipment.getQuality());
        updatedEquipment.setAttributes(equipment.getVersion(), equipment.getAttributes(false));
        updatedEquipment.setLevel(equipment.getLevel());
        updatedEquipment.setHardcore(equipment.isHardcore());
        updatedEquipment.setIronborn(equipment.isIronborn());
        updatedEquipment.setEquipmentType(equipment.getEquipmentType());
        updatedEquipment.setEquipmentLocation(equipment.getEquipmentLocation());
        updatedEquipment.setEquipmentLocationId(equipment.getEquipmentLocationId());
        // We should have updated the version if needed, so save it too
        updatedEquipment.setVersion(Equipment.CURRENT_VERSION);

        updatedEquipment = equipmentRepo.save(updatedEquipment);
        if(populateAttributes) loadEquipmentLinkedTables(equipmentRepo, updatedEquipment);
        return updatedEquipment;
    }

	@Override
	@Transactional
	public Equipment findById(long equipmentId) {
		Equipment foundEquipment = equipmentRepo.findOne(equipmentId);
		if(foundEquipment != null) {
			loadEquipmentLinkedTables(equipmentRepo, foundEquipment);
		}
		
        return foundEquipment;
	}

	@Override
	@Transactional
	public List<Equipment> findAllByQualityId(int qualityId) {
		List<Equipment> foundEquipments = equipmentRepo.findAllByQuality(EquipmentQuality.fromId(qualityId));

		loadEquipmentLinkedTables(equipmentRepo, foundEquipments);
		
		return foundEquipments;
	}

	@Override
	@Transactional
	public List<Equipment> findByLevelGreaterThanAndLevelLessThan(int greaterThanLevel, int lessThanLevel) {
		List<Equipment> foundEquipments = equipmentRepo.findAllByLevelGreaterThanAndLevelLessThan(greaterThanLevel, lessThanLevel);
		
		loadEquipmentLinkedTables(equipmentRepo, foundEquipments);
		
		return foundEquipments;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={EquipmentNotFound.class})
	public void generateStarterEquipment(Character character, Map<CharSlot, Equipment> characterSlots) 
			throws EquipmentNotFound {
		List<EquipmentAttribute> requireAttributes = new ArrayList<EquipmentAttribute>();
		requireAttributes.add(character.getCharClass().getMainAttribute());
		requireAttributes.add(character.getCharClass().getDefenceAttribute());
		Equipment starterWeapon = Equipment.createRandom(
				character.getLevel(), 
				character.isHardcore(), 
				character.isIronborn(), 
				EquipmentLocation.CHARACTER, 
				character.getId(), 
				false,
				EquipmentType.MAIN_WEAPON,
				EquipmentQuality.COMMON,
				requireAttributes,
				DungeonType.ADVENTURE
				);
		starterWeapon = create(starterWeapon);
		characterSlots.put(CharSlot.MAIN_WEAPON, starterWeapon);
		
		requireAttributes = new ArrayList<EquipmentAttribute>();
		requireAttributes.add(character.getCharClass().getMainAttribute());
		requireAttributes.add(character.getCharClass().getDefenceAttribute());
		Equipment starterRing = Equipment.createRandom(
				character.getLevel(), 
				character.isHardcore(),
				character.isIronborn(),
				EquipmentLocation.CHARACTER, 
				character.getId(), 
				false,
				EquipmentType.RING,
				EquipmentQuality.COMMON,
				requireAttributes,
				DungeonType.ADVENTURE
				);
		starterRing = create(starterRing);
		characterSlots.put(CharSlot.RING_LEFT, starterRing);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={DungeonNotFound.class, AccountIdMismatch.class, EquipmentNotFound.class, InventoryNotFound.class, InventoryException.class, CantEquipToDungeon.class})
	public ReloadPanels swapEquipmentInCharacterSlot(Account account, Equipment newEquipment, Character character, CharSlot charSlot) 
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, 
				CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound {
		Equipment currentEquipment = 
				characterEquipmentService.findEquipmentForCharacterAndCharSlot(character, charSlot);
		if(currentEquipment != null) {
			Equipment checkEquipment = 
					characterEquipmentService.findEquipmentForCharacterAndCharSlot(character, charSlot);
			// Check the character and slot ids match those provided
			if(!currentEquipment.equals(checkEquipment)) {
				logger.debug("Expected {} and {} to be the same equipment", currentEquipment, checkEquipment);
				return null;
			}
			return stashSlotItemService.swapItem(account, newEquipment, currentEquipment);
		}
		
		// Then we are just putting equipment in the character slot
		
		// Check the slot is valid for the equipment
		if(!EquipmentType.getValidTypesFromCharSlot(charSlot).contains(newEquipment.getEquipmentType())) {
			logger.debug("Equipment {} not valid in slot {}",newEquipment, charSlot);
			return null;
		}
		
		// Check the equipment is low enough level for the character
		if(newEquipment.getRequiredLevel() > character.getLevel()) {
			logger.debug("Equipment {} too high level for character {}", newEquipment, character);
			return null;
		}
		
		// All good, so move the equipment
		ReloadPanels reloadPanels = stashSlotItemService.moveItem(newEquipment, EquipmentLocation.CHARACTER, character.getId(), account);
		characterEquipmentService.equipmentItem(character, charSlot, newEquipment);
		equipmentRepo.save(newEquipment);

		return reloadPanels;
	}
}
