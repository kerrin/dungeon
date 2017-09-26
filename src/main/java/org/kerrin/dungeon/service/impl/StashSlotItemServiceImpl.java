package org.kerrin.dungeon.service.impl;

import java.util.Map;

import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.ModificationType;
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
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.repository.AccountMessageRepo;
import org.kerrin.dungeon.repository.BoostItemRepo;
import org.kerrin.dungeon.repository.EquipmentRepo;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.HiscoreService;
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
public class StashSlotItemServiceImpl extends ServiceHelppers implements StashSlotItemService {
	private static final Logger logger = LoggerFactory.getLogger(StashSlotItemServiceImpl.class);

	private final EquipmentRepo equipmentRepo;
	private final BoostItemRepo boostItemRepo;
	private final CharacterService characterService;
	private final CharacterEquipmentService characterEquipmentService;
	private final InventoryService inventoryService;
	private final DungeonService dungeonService;
	private final AccountMessageRepo accountMessageRepo;
	private final AccountCurrencyService accountCurrencyService;
	private final HiscoreService hiscoreService;
	
	@Autowired
    public StashSlotItemServiceImpl(EquipmentRepo equipmentRepo, BoostItemRepo boostItemRepo,
    		CharacterService characterService, 
    		CharacterEquipmentService characterEquipmentService, 
    		InventoryService inventoryService, DungeonService dungeonService, AccountMessageRepo accountMessageRepo,
    		AccountCurrencyService accountCurrencyService, 
			HiscoreService hiscoreService) {
		super();
		this.equipmentRepo = equipmentRepo;
		this.boostItemRepo = boostItemRepo;
		this.characterService = characterService;
		this.characterEquipmentService = characterEquipmentService;
		this.inventoryService = inventoryService;
		this.dungeonService = dungeonService;
		this.accountMessageRepo = accountMessageRepo;
		this.accountCurrencyService = accountCurrencyService;
		this.hiscoreService = hiscoreService;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={DungeonNotFound.class, AccountIdMismatch.class, EquipmentNotFound.class, InventoryNotFound.class, InventoryException.class, CantEquipToDungeon.class})
	public ReloadPanels swapItem(Account account, StashSlotItemSuper stashSlotItem1, StashSlotItemSuper stashSlotItem2) 
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, 
				CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound {
		if(stashSlotItem2 != null) {
			// Need to check if the current equipment has somewhere to go and is valid for location
			if(!checkSwapValid(stashSlotItem1, stashSlotItem2) || !checkSwapValid(stashSlotItem2, stashSlotItem1)) {
				if(stashSlotItem1 instanceof Equipment && stashSlotItem2 instanceof Equipment) {
					return tryDungeonSwapEquipment(account, (Equipment)stashSlotItem1, (Equipment)stashSlotItem2);
				}
			}
			return doItemSwap(account, stashSlotItem1, stashSlotItem2);
		}
		
		// Items not swappable
		return null;
	}

	@Override
	// Can't be Transactional or we get duplicate id exceptions when swapping inventory items
	//@Transactional(rollbackFor={DungeonNotFound.class, AccountIdMismatch.class, EquipmentNotFound.class, InventoryNotFound.class, InventoryException.class, CantEquipToDungeon.class})
	public ReloadPanels swapItemWithInventory(Account account, StashSlotItemSuper newItem, int slotIndex)
			throws InventoryNotFound, DungeonNotFound, AccountIdMismatch, EquipmentNotFound, 
					InventoryException, CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage, CharacterNotFound, 
					CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound {
		if(newItem.isIronborn()) {
			if(newItem instanceof Equipment) {
				Equipment newEquipment = (Equipment) newItem;
				if(newEquipment.getEquipmentLocation() == EquipmentLocation.CHARACTER) {
					// Can't unequip items
					return null;
				}
			}
		}
		
		StashSlotItemSuper currentStashSlotItem = inventoryService.getItemInSlot(account, 
				newItem.isHardcore(), newItem.isIronborn(), slotIndex);
		if(currentStashSlotItem != null) {
			StashSlotItemSuper checkStashSlotItem = inventoryService.getItemInSlot(account, 
					newItem.isHardcore(), newItem.isIronborn(), slotIndex);
			// Check the character and slot ids match those provided
			if(!currentStashSlotItem.equals(checkStashSlotItem)) {
				logger.debug("Expected {} and {} to be the same Stash Slot Item", currentStashSlotItem, checkStashSlotItem);
				return null;
			}
			if(currentStashSlotItem.equals(newItem)) {
				logger.debug("Expected {} and {} NOT to be the same equipment", currentStashSlotItem, newItem);
				return null;
			}
			return swapItem(account, newItem, currentStashSlotItem);
		}
		
		// Then we are just putting equipment in the inventory slot
		ReloadPanels reloadPanels = moveItem(newItem, EquipmentLocation.INVENTORY, slotIndex, account);
		inventoryService.putItemInSlot(account, newItem, slotIndex);
		if(newItem instanceof Equipment) {
			equipmentRepo.save((Equipment)newItem);
		} else if(newItem instanceof BoostItem) {
			boostItemRepo.save((BoostItem)newItem);
		}
		
		return reloadPanels;
	}
	
	/**
	 * Checks that equipment1 can be put where equipment2 is (slot and level are ok)
	 * Both equipment1 and equipment2 must be not null
	 * 
	 * @param equipment1
	 * @param equipment2
	 * @return	Ok to put equipment1 where equipment2 is
	 */
	private boolean checkSwapValid(StashSlotItemSuper item1, StashSlotItemSuper item2) {
		EquipmentLocation itemLocation1;
		long itemLocationId1 = -1;
		EquipmentLocation itemLocation2;
		long itemLocationId2 = -1;
		
		if(item1 instanceof Equipment) {
			Equipment equipment1 = (Equipment)item1;
			itemLocation1 = equipment1.getEquipmentLocation();
			itemLocationId1 = equipment1.getEquipmentLocationId();
		} else if(item1 instanceof BoostItem) {
			BoostItem boostItem1 = (BoostItem)item1;
			if(boostItem1.getDungeonId() > 0) {
				itemLocation1 = EquipmentLocation.DUNGEON;
				itemLocationId1 = boostItem1.getDungeonId();
			} else if(boostItem1.getStashSlotId() >= 0) {
				itemLocation1 = EquipmentLocation.INVENTORY;
				itemLocationId1 = boostItem1.getStashSlotId();
			} else if(boostItem1.getMessageId() >= 0) {
				itemLocation1 = EquipmentLocation.MESSAGE;
				itemLocationId1 = boostItem1.getMessageId();
			} else {
				itemLocation1 = EquipmentLocation.NONE;
			}
		} else {
			itemLocation1 = EquipmentLocation.NONE;
		}
		if(item2 instanceof Equipment) {
			Equipment equipment2 = (Equipment)item2;
			itemLocation2 = equipment2.getEquipmentLocation();
			itemLocationId2 = equipment2.getEquipmentLocationId();
		} else if(item2 instanceof BoostItem) {
			BoostItem boostItem2 = (BoostItem)item2;
			if(boostItem2.getDungeonId() > 0) {
				itemLocation2 = EquipmentLocation.DUNGEON;
			} else if(boostItem2.getStashSlotId() >= 0) {
				itemLocation2 = EquipmentLocation.INVENTORY;
			} else if(boostItem2.getMessageId() >= 0) {
				itemLocation2 = EquipmentLocation.MESSAGE;
			} else {
				itemLocation2 = EquipmentLocation.NONE;
			}
		} else {
			itemLocation2 = EquipmentLocation.NONE;
		}
		switch(itemLocation1) {
		case NONE: case DUNGEON: case MESSAGE: return false;
		case CHARACTER:
			// Can only swap if both are equipment
			if(!(item2 instanceof Equipment)) {
				return false;
			}

			Equipment equipment1 = (Equipment)item1;
			Equipment equipment2 = (Equipment)item2;
			// Check the equipment slots are valid for the equipment to swap
			Character character = characterService.findById(itemLocationId1);
			CharSlot equipmentSlot = characterEquipmentService.findCharSlotForEquipment(character, equipment1);
			if(!equipment2.getEquipmentType().getValidSlots().contains(equipmentSlot)) {
				logger.debug("Equipment {} not valid swap with {}", item1, item2);
				return false;
			}
			
			// Check the equipment is low enough level for the character
			if(equipment2.getRequiredLevel() > character.getLevel()) {
				logger.debug("Equipment {} too high level for character {}", equipment2, character);
				return false;
			}
			if(item1.isHardcore() != item2.isHardcore() || item1.isIronborn() != item2.isIronborn()) {
				logger.debug("Equipment {} and {} from different game states (hardcore or ironborn)", item1, item2);
				return false;
			}
			
			if(item1.isIronborn()) { // Only really need to check one of them is ironborn, as that means they both are
				if(		(itemLocation1 == EquipmentLocation.CHARACTER && 
							itemLocation2 != EquipmentLocation.CHARACTER) ||
						(itemLocation2 == EquipmentLocation.CHARACTER && 
							itemLocation1 != EquipmentLocation.CHARACTER) ) {
					// One item is not on the character and one is
					logger.debug(
							"Can't swap equipment {} and {} as they are ironborn, but one is not on a characterand one is on a character", 
							item1, item2);
					return false;
				}
				if(itemLocation1 == EquipmentLocation.CHARACTER &&
						itemLocationId1 != itemLocationId2) {
					// They are on different characters
					logger.debug("Can't swap equipment {} and {} as they are ironborn but on different characters", 
							item1, item2);
					return false;
				}
			}
			break;
		case INVENTORY:
			if(item1.isHardcore() != item2.isHardcore() || item1.isIronborn() != item2.isIronborn()) {
				logger.debug("Equipment {} and {} from differnt game states (hardcore or ironborn)", item1, item2);
				return false;
			}
			if(item2.isIronborn() && itemLocation2 == EquipmentLocation.CHARACTER) {
				// Can't unequip current item to stash
				logger.debug("Can't unequip equipment {} to stash as it is ironborn", item2);
				return false;
			}
			break;
		}
		return true;
	}
	
	/**
	 * Move the item to the new location, no swapping needed
	 * 
	 * @param equipment				Equipment to move
	 * @param location				Move to location type
	 * @param locationId			Id for location at location type
	 * @param account				Account that owns equipment
	 * 
	 * @return ReloadPanels			The panels that are effected
	 * 
	 * @throws DungeonNotFound
	 * @throws AccountIdMismatch
	 * @throws EquipmentNotFound
	 * @throws InventoryNotFound
	 * @throws InventoryException
	 * @throws BoostItemNotFound 
	 * @throws MessageEquipmentNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={DungeonNotFound.class, AccountIdMismatch.class, EquipmentNotFound.class, InventoryNotFound.class, InventoryException.class})
	@Override
	public ReloadPanels moveItem(StashSlotItemSuper stashSlotItem, EquipmentLocation location, long locationId, Account account) 
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, BoostItemNotFound, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound {
		ReloadPanels reloadPanels = new ReloadPanels();
		reloadPanels.setPanelReload(location, locationId);
		
		if(stashSlotItem instanceof Equipment) {
			Equipment equipment = (Equipment)stashSlotItem;
			reloadPanels.setPanelReload(equipment.getEquipmentLocation(), equipment.getEquipmentLocationId());
			
			doRemove(stashSlotItem, account);
			
			equipment.setEquipmentLocation(location);
			equipment.setEquipmentLocationId(locationId);
		} else if(stashSlotItem instanceof BoostItem) {	
			BoostItem boostItem = (BoostItem)stashSlotItem;
			EquipmentLocation stashSlotItemLocation;
			long stashSlotItemLocationId;
			if(boostItem.getDungeonId() > 0) {
				stashSlotItemLocation = EquipmentLocation.DUNGEON;
				stashSlotItemLocationId = boostItem.getDungeonId();
			} else if(boostItem.getStashSlotId() >= 0) {
				stashSlotItemLocation = EquipmentLocation.INVENTORY;
				stashSlotItemLocationId = boostItem.getStashSlotId();
			} else if(boostItem.getMessageId() > 0) {
				stashSlotItemLocation = EquipmentLocation.MESSAGE;
				stashSlotItemLocationId = boostItem.getMessageId();
			} else {
				logger.error("Unknown stashSlotItem {} ", stashSlotItem);
				stashSlotItemLocation = EquipmentLocation.NONE;
				stashSlotItemLocationId = -1;
			}
			reloadPanels.setPanelReload(stashSlotItemLocation, stashSlotItemLocationId);
			
			doRemove(stashSlotItem, account);
			
			switch (location) {
			case DUNGEON:
				boostItem.setDungeonId(locationId);
				boostItem.setStashSlotId(-1);
				boostItem.setMessageId(-1);
				break;
			case INVENTORY:
				boostItem.setDungeonId(-1);
				boostItem.setStashSlotId((int)locationId);
				boostItem.setMessageId(-1);
				break;
			case MESSAGE:
				boostItem.setDungeonId(-1);
				boostItem.setMessageId(locationId);
				boostItem.setStashSlotId(-1);
				break;
			default:
				logger.error("Attempt to set Boost item location to {}: {}", location, locationId);
				break;
			}
		} else {
			logger.error("Unknown stash slot item {}", stashSlotItem);
		}
		
		return reloadPanels;
	}
	
	/**
	 * Swaps location of equipment, both equipment must be not null and be in swappable locations
	 *  
	 * @param equipment1			Equipment to swap
	 * @param equipment2			Equipment to swap
	 * 
	 * @return ReloadPanels			The panels that are effected
	 * 
	 * @throws InventoryException 
	 * @throws InventoryNotFound 
	 * @throws EquipmentNotFound 
	 * @throws AccountIdMismatch 
	 * @throws DungeonNotFound 
	 * @throws CantEquipToDungeon 
	 * @throws BoostItemNotFound 
	 * @throws CantEquipToMessage 
	 * @throws MessageEquipmentNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={DungeonNotFound.class, AccountIdMismatch.class, EquipmentNotFound.class, InventoryNotFound.class, InventoryException.class, CantEquipToDungeon.class})
	private ReloadPanels doItemSwap(Account account, StashSlotItemSuper item1, StashSlotItemSuper item2) 
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound {
		EquipmentLocation itemLocation1;
		long itemLocationId1 = -1;
		EquipmentLocation itemLocation2;
		long itemLocationId2 = -1;
		
		if(item1 instanceof Equipment) {
			Equipment equipment1 = (Equipment)item1;
			itemLocation1 = equipment1.getEquipmentLocation();
			itemLocationId1 = equipment1.getEquipmentLocationId();
		} else if(item1 instanceof BoostItem) {
			BoostItem boostItem1 = (BoostItem)item1;
			if(boostItem1.getDungeonId() > 0) {
				itemLocation1 = EquipmentLocation.DUNGEON;
				itemLocationId1 = boostItem1.getDungeonId();
			} else if(boostItem1.getStashSlotId() >= 0) {
				itemLocation1 = EquipmentLocation.INVENTORY;
				itemLocationId1 = boostItem1.getStashSlotId();
			} else if(boostItem1.getMessageId() >= 0) {
				itemLocation1 = EquipmentLocation.MESSAGE;
				itemLocationId1 = boostItem1.getMessageId();
			} else {
				itemLocation1 = EquipmentLocation.NONE;
			}
		} else {
			itemLocation1 = EquipmentLocation.NONE;
		}
		if(item2 instanceof Equipment) {
			Equipment equipment2 = (Equipment)item2;
			itemLocation2 = equipment2.getEquipmentLocation();
			itemLocationId2 = equipment2.getEquipmentLocationId();
		} else if(item2 instanceof BoostItem) {
			BoostItem boostItem2 = (BoostItem)item2;
			if(boostItem2.getDungeonId() > 0) {
				itemLocation2 = EquipmentLocation.DUNGEON;
			} else if(boostItem2.getStashSlotId() >= 0) {
				itemLocation2 = EquipmentLocation.INVENTORY;
			} else if(boostItem2.getMessageId() >= 0) {
				itemLocation2 = EquipmentLocation.MESSAGE;
			} else {
				itemLocation2 = EquipmentLocation.NONE;
			}
		} else {
			itemLocation2 = EquipmentLocation.NONE;
		}
		Character character1 = null;
		CharSlot charSlot1 = CharSlot.UNKNOWN;
		if(itemLocation1 == EquipmentLocation.CHARACTER) {
			if(item1 instanceof Equipment) {
				Equipment equipment1 = (Equipment)item1;
				
				character1 = characterService.findById(itemLocationId1);
				charSlot1 = characterEquipmentService.findCharSlotForEquipment(character1, equipment1);
			} else {
				logger.error("Expected stash slot item to be equipment as it is on a character: {}", item1);
			}
		}
		Character character2 = null;
		CharSlot charSlot2 = CharSlot.UNKNOWN;
		if(itemLocation2 == EquipmentLocation.CHARACTER) {
			if(item2 instanceof Equipment) {
				Equipment equipment2 = (Equipment)item2;
				character2 = characterService.findById(itemLocationId2);
				charSlot2 = characterEquipmentService.findCharSlotForEquipment(character2, equipment2);
			} else {
				logger.error("Expected stash slot item to be equipment as it is on a character: {}", item2);
			}
		}
		
		// Remove equipment from existing locations first
		doRemove(item1, account);
		doRemove(item2, account);
		// Then update equipment1 to it's new location
		if(item1 instanceof Equipment) {
			Equipment equipment1 = (Equipment)item1;
			equipment1.setEquipmentLocation(itemLocation2);
			equipment1.setEquipmentLocationId(itemLocationId2);
		} else if(item1 instanceof BoostItem) {
			BoostItem boostItem1 = (BoostItem)item1;
			if(itemLocation2 == EquipmentLocation.DUNGEON) {
				boostItem1.setDungeonId(itemLocationId2);
				boostItem1.setStashSlotId(-1);
				boostItem1.setMessageId(-1);
			} else if(itemLocation2 == EquipmentLocation.INVENTORY) {
				boostItem1.setDungeonId(-1);
				boostItem1.setStashSlotId((int)itemLocationId2);
				boostItem1.setMessageId(-1);
			} else if(itemLocation2 == EquipmentLocation.MESSAGE) {
				boostItem1.setMessageId(itemLocationId2);
				boostItem1.setDungeonId(-1);
				boostItem1.setStashSlotId(-1);
			} else {
				logger.error("Expected itemLocation2, {}", itemLocation2);
			}
		}
		// Then add it in the new location
		doAdd(item1, account, character2, charSlot2, (int)itemLocationId2);
		
		// Then update equipment2 to it's new location
		if(item2 instanceof Equipment) {
			Equipment equipment2 = (Equipment)item2;
			equipment2.setEquipmentLocation(itemLocation1);
			equipment2.setEquipmentLocationId(itemLocationId1);
		} else if(item2 instanceof BoostItem) {
			BoostItem boostItem2 = (BoostItem)item2;
			if(itemLocation1 == EquipmentLocation.DUNGEON) {
				boostItem2.setDungeonId(itemLocationId1);
				boostItem2.setStashSlotId(-1);
				boostItem2.setMessageId(-1);
			} else if(itemLocation1 == EquipmentLocation.INVENTORY) {
				boostItem2.setDungeonId(-1);
				boostItem2.setStashSlotId((int)itemLocationId1);
				boostItem2.setMessageId(-1);
			} else if(itemLocation2 == EquipmentLocation.MESSAGE) {
				boostItem2.setMessageId(itemLocationId2);
				boostItem2.setDungeonId(-1);
				boostItem2.setStashSlotId(-1);
			} else {
				logger.error("Expected itemLocation1, {}", itemLocation1);
			}
		}
		// Then add it in the new location
		doAdd(item2, account, character1, charSlot1, (int)itemLocationId1);
		
		if(item1 instanceof Equipment) {
			Equipment equipment1 = (Equipment)item1;
			equipmentRepo.save(equipment1);
		} else if(item1 instanceof BoostItem) {
			BoostItem boostItem1 = (BoostItem)item1;
			boostItemRepo.save(boostItem1);
		}
		if(item2 instanceof Equipment) {
			Equipment equipment2 = (Equipment)item2;
			equipmentRepo.save(equipment2);
		} else if(item2 instanceof BoostItem) {
			BoostItem boostItem2 = (BoostItem)item2;
			boostItemRepo.save(boostItem2);
		}
		
		ReloadPanels reloadPanels = new ReloadPanels();
		reloadPanels.setPanelReload(itemLocation1, itemLocationId1);
		reloadPanels.setPanelReload(itemLocation2, itemLocationId2);
		return reloadPanels;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={DungeonNotFound.class, AccountIdMismatch.class, EquipmentNotFound.class, InventoryNotFound.class, InventoryException.class, CantEquipToDungeon.class})
	@Override
	public void doAdd(StashSlotItemSuper item, Account account, Character character, CharSlot charSlot, int inventorySlotId)
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage {
		if(item instanceof Equipment) {
			Equipment equipment = (Equipment)item;
			EquipmentLocation itemLocation = equipment.getEquipmentLocation();
			long itemLocationId = equipment.getEquipmentLocationId();
			switch(itemLocation) {
				case CHARACTER:
					characterEquipmentService.equipmentItem(character, charSlot, equipment);
					break;
				case DUNGEON:
					throw new CantEquipToDungeon(itemLocationId);
				case MESSAGE:
					throw new CantEquipToMessage(itemLocationId);
				case INVENTORY:
					if(!inventoryService.putItemInSlot(account, equipment, inventorySlotId)) {
						throw new InventoryException("Could not remove item from old inventory location");
					}
					break;
				case NONE:
					break;
			}
		} else if(item instanceof BoostItem) {
			BoostItem boostItem1 = (BoostItem)item;
			if(boostItem1.getDungeonId() > 0) {
				throw new CantEquipToDungeon(boostItem1.getDungeonId());
			} else if(boostItem1.getMessageId() > 0) {
				throw new CantEquipToMessage(boostItem1.getDungeonId());
			} else if(boostItem1.getStashSlotId() >= 0) {
				if(!inventoryService.putItemInSlot(account, boostItem1, inventorySlotId)) {
					throw new InventoryException("Could not remove item from old inventory location");
				}
			} else {
				throw new CantEquipToDungeon();
			}
		} else {
			throw new CantEquipToDungeon();
		}
		
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={DungeonNotFound.class, AccountIdMismatch.class, EquipmentNotFound.class, InventoryNotFound.class, InventoryException.class})
	@Override
	public void doRemove(StashSlotItemSuper stashSlotItem, Account account)
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, 
			BoostItemNotFound, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound {
		if(stashSlotItem instanceof Equipment) {
			Equipment equipment = (Equipment)stashSlotItem;
			switch(equipment.getEquipmentLocation()) {
			case CHARACTER:
				Character character = characterService.findById(equipment.getEquipmentLocationId());
				if(characterEquipmentService.unequipmentItem(character, equipment) == null) {
					throw new EquipmentNotFound();
				}
				removeEquipmentFromDungeon(equipment);
				break;
			case DUNGEON:
				Dungeon dungeon = dungeonService.findById(equipment.getEquipmentLocationId());
				dungeon.removeItemReward(equipment);
				dungeonService.update(dungeon);
				break;
			case INVENTORY:
				if(!inventoryService.removeFromInventory(account, equipment)) {
					throw new InventoryException("Could not remove item from old inventory location");
				}
				removeEquipmentFromDungeon(equipment);
				break;
			case MESSAGE:
				AccountMessage message = accountMessageRepo.findOne(equipment.getEquipmentLocationId());
				message.setAttachedItemId(-1);
				message.setAttachedItemType(null);
				accountMessageRepo.save(message);
				break;
			case NONE:
				break;
			} 
		} else if(stashSlotItem instanceof BoostItem) {
			BoostItem boostItem = (BoostItem)stashSlotItem;
			if(boostItem.getDungeonId() > 0) {
				Dungeon dungeon = dungeonService.findById(boostItem.getDungeonId());
				dungeon.removeBoostItemReward(boostItem);
				dungeonService.update(dungeon);
			} else if(boostItem.getStashSlotId() >= 0) {
				if(!inventoryService.removeFromInventory(account, boostItem)) {
					throw new InventoryException("Could not remove item from old inventory location");
				}
				removeBoostItemFromDungeon(boostItem);
			} else if(boostItem.getMessageId() > 0) {
				AccountMessage message = accountMessageRepo.findOne(boostItem.getMessageId());
				message.setAttachedItemId(-1);
				message.setAttachedItemType(null);
				accountMessageRepo.save(message);
			} else {
				logger.error("Unknown stashSlotItem {} ", stashSlotItem);
			}
		} else {
			
		}
	}

	private void removeEquipmentFromDungeon(Equipment equipment) throws DungeonNotFound, AccountIdMismatch, BoostItemNotFound, EquipmentNotFound {
		Dungeon dungeon = dungeonService.findByEquipment(equipment);
		if(dungeon != null) {
			logger.error("Dungeon {} still thought it had Equipment {} in it", dungeon, equipment);
			dungeon.removeItemReward(equipment);
			dungeonService.update(dungeon);
		} else {
			logger.trace("Equipment {} correctly not in dungeon", equipment);
		}
	}

	private void removeBoostItemFromDungeon(BoostItem boostItem) throws DungeonNotFound, AccountIdMismatch, BoostItemNotFound, EquipmentNotFound {
		Dungeon dungeon = dungeonService.findByBoostItem(boostItem);
		if(dungeon != null) {
			logger.error("Dungeon {} still thought it had Equipment {} in it", dungeon, boostItem);
			dungeon.removeBoostItemReward(boostItem);
			dungeonService.update(dungeon);
		} else {
			logger.trace("Equipment {} correctly not in dungeon", boostItem);
		}
	}
	
	@Override
	public void removeItemFromDungeon(StashSlotItemSuper item) 
			throws DungeonNotFound, AccountIdMismatch, BoostItemNotFound, EquipmentNotFound {
		if(item instanceof Equipment) {
			removeEquipmentFromDungeon((Equipment)item);
		} else if(item instanceof BoostItem) {
			removeBoostItemFromDungeon((BoostItem)item);
		} else {
			logger.error("Unknown item type {}", item);
		}
	}
	
	// ------------- Equipment specific functions ----------------
	
	/**
	 * Try a dungeon swap of equipment (not stash slot items!)
	 * 
	 * @param account
	 * @param equipment1
	 * @param equipment2
	 * @return
	 * @throws DungeonNotFound
	 * @throws AccountIdMismatch
	 * @throws EquipmentNotFound
	 * @throws InventoryNotFound
	 * @throws InventoryException
	 * @throws CantEquipToDungeon
	 * @throws BoostItemNotFound 
	 * @throws CantEquipToMessage 
	 * @throws MessageEquipmentNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 */
	@Override
	public ReloadPanels tryDungeonSwapEquipment(Account account, Equipment equipment1, Equipment equipment2) 
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound {
		if(checkSwapDungeonValid(equipment1, equipment2)) {
			return doItemSwapDungeon(account, equipment1, equipment2);
		}
		
		// Items not swappable
		return null;
	}
	
	/**
	 * Checks that equipment1 can be put where equipment2 is (slot and level are ok)
	 * Both equipment1 and equipment2 must be not null
	 * 
	 * @param equipment1
	 * @param equipment2
	 * @return	Ok to put equipment1 where equipment2 is
	 */
	private boolean checkSwapDungeonValid(Equipment equipment1, Equipment equipment2) {
		if(equipment1 != null && equipment2 != null && 
				equipment1.getEquipmentLocation() == EquipmentLocation.DUNGEON &&
						equipment2.getEquipmentLocation() == EquipmentLocation.CHARACTER) { 
			// Check the equipment slots are valid for the equipment to swap
			Character character = characterService.findById(equipment2.getEquipmentLocationId());
			CharSlot equipmentSlot = characterEquipmentService.findCharSlotForEquipment(character, equipment2);
			if(!equipment1.getEquipmentType().getValidSlots().contains(equipmentSlot)) {
				logger.debug("Equipment {} not valid swap with {}", equipment1, equipment2);
				return false;
			}
			
			// Check the equipment is low enough level for the character
			if(equipment1.getRequiredLevel() > character.getLevel()) {
				logger.debug("Equipment {} too high level for character {}", equipment2, character);
				return false;
			}
			if(equipment1.isHardcore() != equipment2.isHardcore() || equipment1.isIronborn() != equipment2.isIronborn()) {
				logger.debug("Equipment {} and {} from differnt game states (hardcore or ironborn)", equipment1, equipment2);
				return false;
			}
			
			return true;
		}
		return false;
	}
	
	/**
	 * Swaps location of equipment, both equipment must be not null and be in swappable locations
	 *  
	 * @param equipment1			Equipment to swap
	 * @param equipment2			Equipment to swap
	 * 
	 * @return ReloadPanels			The panels that are effected
	 * 
	 * @throws InventoryException 
	 * @throws InventoryNotFound 
	 * @throws EquipmentNotFound 
	 * @throws AccountIdMismatch 
	 * @throws DungeonNotFound 
	 * @throws CantEquipToDungeon 
	 * @throws BoostItemNotFound 
	 * @throws CantEquipToMessage 
	 * @throws MessageEquipmentNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={DungeonNotFound.class, AccountIdMismatch.class, EquipmentNotFound.class, InventoryNotFound.class, InventoryException.class, CantEquipToDungeon.class})
	private ReloadPanels doItemSwapDungeon(Account account, Equipment itemDungeon, Equipment itemCharacter) 
			throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, InventoryNotFound, InventoryException, CantEquipToDungeon, BoostItemNotFound, CantEquipToMessage, CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, MessageEquipmentNotFound {
		EquipmentLocation itemCharacterLocation = itemCharacter.getEquipmentLocation();
		long itemOtherLocationId = itemCharacter.getEquipmentLocationId();
		EquipmentLocation itemDungeonLocation = itemDungeon.getEquipmentLocation();
		long itemDungeonLocationId = itemDungeon.getEquipmentLocationId();
		
		if(itemDungeonLocation != EquipmentLocation.DUNGEON) {
			// This function expects the item to be in a dungeon
			return null;
		}
		if(itemCharacterLocation != EquipmentLocation.CHARACTER) {
			return null;
		}
		// Dungeon item needs to be equipment too then
		if(!(itemDungeon instanceof Equipment)) return null;
		Equipment equipmentDungeon = (Equipment)itemDungeon;
		Character character = characterService.findById(itemOtherLocationId);
		Equipment equipmentCharacter = (Equipment)itemCharacter;
		CharSlot charSlotCharacter = characterEquipmentService.findCharSlotForEquipment(character, equipmentCharacter);			
	
		// Check for valid location to put equipment currently on character
		boolean sendToSalvage = false;
		int stashSlotId = -1;
		if(itemCharacter.isIronborn()) {
			// Ironborn salvages item
			sendToSalvage = true;
		} else {
			// Check for empty stash slot
			Inventory inventory = inventoryService.findByAccount(account, itemDungeon.isHardcore(), itemDungeon.isIronborn());
			int stashSize = inventory.getSize();
			stashSlotId = 0;
			Map<Integer, StashSlotItemSuper> inventorySlots = inventory.getInventorySlots();
			while(stashSlotId < stashSize && inventorySlots.get(stashSlotId) != null) {
				stashSlotId++;
			}
			if(stashSlotId >= stashSize) {
				// No Free Slots
				return null;
			}
		}
		
		// Remove equipment from existing locations first
		removeItemFromDungeon(equipmentDungeon);
		doRemove(equipmentCharacter, account);
		// Then update equipment1 to it's new location
		equipmentDungeon.setEquipmentLocation(itemCharacterLocation);
		equipmentDungeon.setEquipmentLocationId(itemOtherLocationId);
		// Then add it in the new location
		doAdd(equipmentDungeon, account, character, charSlotCharacter, (int)itemOtherLocationId);
		
		ReloadPanels reloadPanels = new ReloadPanels();
		
		reloadPanels.setPanelReload(itemDungeonLocation, itemDungeonLocationId);
		reloadPanels.setPanelReload(itemCharacterLocation, itemOtherLocationId);

		if(sendToSalvage) {
			String reference = "api-salvage-"+account.getUsername()+"-"+equipmentCharacter.getId();			
			accountCurrencyService.adjustCurrency(account, equipmentCharacter.isHardcore(),equipmentCharacter.isIronborn(), 
					equipmentCharacter.getSalvageValue(), ModificationType.GAIN_SALVAGE, reference);
			hiscoreService.tokensEarnt(account, equipmentCharacter.isHardcore(),equipmentCharacter.isIronborn(), equipmentCharacter.getLevel());
			equipmentRepo.save(equipmentDungeon);
			equipmentRepo.delete(equipmentCharacter);
			reloadPanels.reloadSummary = true;
		} else {
			// Then update equipment2 to it's new location
			equipmentCharacter.setEquipmentLocation(EquipmentLocation.INVENTORY);
			equipmentCharacter.setEquipmentLocationId(stashSlotId);
			// Then add it in the new location in the stash
			inventoryService.putItemInSlot(account, equipmentCharacter, stashSlotId);

			reloadPanels.reloadStashSlotId = stashSlotId;
			equipmentRepo.save(equipmentDungeon);
			equipmentRepo.save(equipmentCharacter);
		}
				
		return reloadPanels;
	}
}
