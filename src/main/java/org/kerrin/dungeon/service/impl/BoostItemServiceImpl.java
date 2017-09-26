package org.kerrin.dungeon.service.impl;

import java.util.List;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.repository.AccountMessageRepo;
import org.kerrin.dungeon.repository.BoostItemRepo;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.InventoryService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoostItemServiceImpl extends ServiceHelppers implements BoostItemService {
	private static final Logger logger = LoggerFactory.getLogger(BoostItemServiceImpl.class);

	private final BoostItemRepo boostItemRepo;
	private final InventoryService inventoryService;
	private final DungeonService dungeonService;
	private final AccountMessageRepo accountMessageRepo;
	
	@Autowired
    public BoostItemServiceImpl(BoostItemRepo boostItemRepo, AccountMessageRepo accountMessageRepo, 
    		InventoryService inventoryService, DungeonService dungeonService) {
		super();
		this.boostItemRepo = boostItemRepo;
		this.accountMessageRepo = accountMessageRepo;
		this.inventoryService = inventoryService;
		this.dungeonService = dungeonService;
	}



	@Override
	@Transactional
	public BoostItem findById(long boostItemId) {
		BoostItem foundBoostItem = boostItemRepo.findOne(boostItemId);
		loadBoostItemLinkedTables(boostItemRepo, foundBoostItem);
        return foundBoostItem;
	}
	
	@Override
	@Transactional
	public List<BoostItem> findAllByAccount(Account account) {
		List<BoostItem> foundBoostItems = boostItemRepo.findAllByAccount(account);
		for(BoostItem boostItem:foundBoostItems) {
			loadBoostItemLinkedTables(boostItemRepo, boostItem);
		}
        return foundBoostItems;
	}
	
	@Override
	@Transactional
	public List<BoostItem> findByLevelGreaterThanAndLevelLessThan(int greaterThan, int lessThan) {
		List<BoostItem> foundBoostItems = boostItemRepo.findAllByLevelGreaterThanAndLevelLessThan(greaterThan, lessThan);
		for(BoostItem boostItem:foundBoostItems) {
			loadBoostItemLinkedTables(boostItemRepo, boostItem);
		}
        return foundBoostItems;
	}

	@Override
    @Transactional
    public BoostItem create(BoostItem BoostItem) {
    	return boostItemRepo.save(BoostItem);
    }

	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={BoostItemNotFound.class, DungeonNotFound.class, AccountIdMismatch.class, InventoryNotFound.class})
    public BoostItem delete(Account account, BoostItem boostItem) 
    		throws BoostItemNotFound, EquipmentNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound {
        BoostItem deletedBoostItem = boostItemRepo.findOne(boostItem.getId());
         
        if (deletedBoostItem == null) throw new BoostItemNotFound();
        
        if(deletedBoostItem.getStashSlotId() >= 0) {
        	StashSlotItemSuper checkItem = inventoryService.getItemInSlot(account, 
        			deletedBoostItem.isHardcore(),deletedBoostItem.isIronborn(), (int)deletedBoostItem.getStashSlotId());
        	if(checkItem.equals(deletedBoostItem)) {
        		inventoryService.removeFromInventory(account, deletedBoostItem);
        	} else {
        		return null;
        	}        	
        } else if(deletedBoostItem.getDungeonId() >= 0) {
        	Dungeon dungeon = dungeonService.findById(deletedBoostItem.getDungeonId());
        	if(dungeon == null) {
        		throw new DungeonNotFound();
        	}        	
        	dungeon.removeBoostItemReward(deletedBoostItem);
        	dungeonService.update(dungeon);
        } else if(deletedBoostItem.getMessageId() >= 0) {
			AccountMessage message = accountMessageRepo.findOne(deletedBoostItem.getMessageId());
        	if(message.getAttachedItemId() == boostItem.getId()) {
				message.setAttachedItemId(-1);
				message.setAttachedItemType(null);
				accountMessageRepo.save(message);
        	}
        }
        
        boostItemRepo.delete(deletedBoostItem);
        return deletedBoostItem;
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=BoostItemNotFound.class)
    public BoostItem update(BoostItem boostItem) throws BoostItemNotFound {
        BoostItem updatedBoostItem = boostItemRepo.findOne(boostItem.getId());
         
        if (updatedBoostItem == null) {
            throw new BoostItemNotFound();
        }
         
        // Set the base, defence, ancient attributes before setting the general ones, as the general ones may change them
        // if the version has changed
        updatedBoostItem.setLevel(boostItem.getLevel());
        updatedBoostItem.setHardcore(boostItem.isHardcore());
        updatedBoostItem.setIronborn(boostItem.isIronborn());
        updatedBoostItem.setBoostItemType(boostItem.getBoostItemType());
        updatedBoostItem.setStashSlotId(boostItem.getStashSlotId());
        updatedBoostItem.setDungeonId(boostItem.getDungeonId());
        updatedBoostItem.setMessageId(boostItem.getMessageId());

        updatedBoostItem = boostItemRepo.save(updatedBoostItem);
        loadBoostItemLinkedTables(boostItemRepo, updatedBoostItem);
        return updatedBoostItem;
    }

	@Override
	@Transactional
	public void clearLocations(BoostItem boostItem) throws DungeonNotFound, InventoryNotFound, AccountIdMismatch, EquipmentNotFound, BoostItemNotFound {
		long dungeonId = boostItem.getDungeonId();
		if(dungeonId >= 0) {
			boostItem.setDungeonId(-1);
			Dungeon dungeon = dungeonService.findById(dungeonId);
        	if(dungeon == null) {
        		throw new DungeonNotFound();
        	}        	
        	dungeon.removeBoostItemReward(boostItem);
        	dungeonService.update(dungeon);
		}
		int stashSlotId = boostItem.getStashSlotId();
		if(stashSlotId >= 0) {
			boostItem.setStashSlotId(-1);
			StashSlotItemSuper checkItem = inventoryService.getItemInSlot(boostItem.getAccount(), 
					boostItem.isHardcore(),boostItem.isIronborn(), stashSlotId);
        	if(boostItem.equals(checkItem)) {
        		inventoryService.removeFromInventory(boostItem.getAccount(), boostItem);
        	}
		}
		long messageId = boostItem.getMessageId();
		if(messageId >= 0) {
			boostItem.setMessageId(-1);
			AccountMessage message = accountMessageRepo.findOne(messageId);
        	if(message.getAttachedItemId() == boostItem.getId()) {
				message.setAttachedItemId(-1);
				message.setAttachedItemType(null);
				accountMessageRepo.save(message);
        	}
		}
		boostItemRepo.save(boostItem);
	}
	
	@Override
	@Transactional
	public List<BoostItem> getBoostItems(Account account, boolean hardcore, boolean ironborn) {
		List<BoostItem> foundBoostItems = boostItemRepo.findAllByAccountAndStashSlotIdAndDungeonIdAndMessageIdAndHardcoreAndIronborn(
				account, -1, -1, -1, hardcore, ironborn);
		for(BoostItem boostItem:foundBoostItems) {
			loadBoostItemLinkedTables(boostItemRepo, boostItem);
		}
		return foundBoostItems;
	}

	@Override
	public BoostItem getValidBoostItemType(Account account, boolean hardcore, boolean ironborn, 
			BoostItemType boostItemType, int level, boolean minimum) {
		BoostItem foundBoostItem = null;
		if(minimum) {
			foundBoostItem = boostItemRepo.findFirstByAccountAndStashSlotIdAndDungeonIdAndMessageIdAndHardcoreAndIronbornAndBoostItemTypeAndLevelGreaterThanOrderByLevel(
					account, -1, -1, -1, hardcore, ironborn, boostItemType, level-1); // -1 as we really want >=
		} else {
			foundBoostItem = boostItemRepo.findFirstByAccountAndStashSlotIdAndDungeonIdAndMessageIdAndHardcoreAndIronbornAndBoostItemTypeAndLevelLessThanOrderByLevelDesc(
					account, -1, -1, -1, hardcore, ironborn, boostItemType, level+1); // +1 as we really want <=
		}
		
		return foundBoostItem;
	}
}
