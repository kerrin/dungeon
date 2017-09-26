package org.kerrin.dungeon.service.impl;

import java.util.Map;

import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.repository.AccountRepo;
import org.kerrin.dungeon.repository.EquipmentRepo;
import org.kerrin.dungeon.repository.InventoryRepo;
import org.kerrin.dungeon.service.InventoryService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryServiceImpl extends ServiceHelppers implements InventoryService {
	private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

	private final InventoryRepo inventoryRepo;
	private final AccountRepo accountRepo;
	private final EquipmentRepo equipmentRepo;
	
	@Autowired
    public InventoryServiceImpl(InventoryRepo inventoryRepo, AccountRepo accountRepo, EquipmentRepo equipmentRepo) {
		super();
		this.inventoryRepo = inventoryRepo;
		this.accountRepo = accountRepo;
		this.equipmentRepo = equipmentRepo;
	}

	@Override
    @Transactional
    public Inventory create(Inventory inventory) {
		Inventory createInventory = inventory;
        return inventoryRepo.save(createInventory);
    }

	@Override
	@Transactional
    public Inventory findByAccount(Account account, boolean hardcore, boolean ironborn) {
        Inventory inventory = inventoryRepo.findOneByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
        if(inventory != null) {
        	// Initialise the slots and equipment in them
        	Map<Integer, StashSlotItemSuper> slots = inventory.getInventorySlots();
        	for(Integer slotIndex:slots.keySet()) {
        		StashSlotItemSuper stashSlotItem = slots.get(slotIndex);
        		if(stashSlotItem instanceof Equipment) {
    				loadEquipmentLinkedTables(equipmentRepo, (Equipment)stashSlotItem);
    			} else if(stashSlotItem instanceof BoostItem) {
    				
    			}
        	}
        }
        
        return inventory;
    }

	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=AccountNotFound.class)
    public Inventory delete(Account account, boolean hardcore, boolean ironborn) throws InventoryNotFound {
        Inventory deletedInventory = inventoryRepo.findOneByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
         
        if (deletedInventory == null) throw new InventoryNotFound();
         
        inventoryRepo.delete(deletedInventory);
        return deletedInventory;
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={InventoryNotFound.class, AccountNotFound.class})
    public Inventory update(Inventory inventory, boolean populateEquipment) throws InventoryNotFound, AccountNotFound {
    	Account account = accountRepo.getOne(inventory.getAccountId());
    	if(account == null) {
    		throw new AccountNotFound();
    	}
        Inventory updatedInventory = inventoryRepo.findOneByAccountIdAndHardcoreAndIronborn(
        		account.getId(), inventory.isHardcore(), inventory.isIronborn());
         
        if (updatedInventory == null) {
            throw new InventoryNotFound();
        }
         
        updatedInventory.setSize(inventory.getSize());
        updatedInventory.setInventorySlots(inventory.getInventorySlots());

        updatedInventory = inventoryRepo.save(updatedInventory);
        
        if(updatedInventory != null) {
        	// Initialise the slots and equipment in them
        	Map<Integer, StashSlotItemSuper> slots = updatedInventory.getInventorySlots();
        	for(Integer slotIndex:slots.keySet()) {
        		StashSlotItemSuper stashSlotItem = slots.get(slotIndex);
        		if(populateEquipment) {
        			if(stashSlotItem instanceof Equipment) {
        				loadEquipmentLinkedTables(equipmentRepo, (Equipment)stashSlotItem);
        			} else if(stashSlotItem instanceof BoostItem) {
        				
        			}
        		}
        	}
        }
        
        return updatedInventory;
    }

	@Override
	@Transactional
	public boolean removeFromInventory(Account account, StashSlotItemSuper stashSlotItem) throws InventoryNotFound {
		Inventory updateInventory = inventoryRepo.findOneByAccountIdAndHardcoreAndIronborn(
				account.getId(), stashSlotItem.isHardcore(), stashSlotItem.isIronborn());
		if (updateInventory == null) {
            throw new InventoryNotFound();
		}
		
		Map<Integer, StashSlotItemSuper> invSlots = updateInventory.getInventorySlots();
		for(Integer index:invSlots.keySet()) {
			if(invSlots.get(index).equals(stashSlotItem)) {
				invSlots.remove(index);
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public StashSlotItemSuper getItemInSlot(Account account, boolean hardcore, boolean ironborn, Integer slotIndex) throws InventoryNotFound {
		Inventory inventory = inventoryRepo.findOneByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		if (inventory == null) {
            throw new InventoryNotFound();
		}
		StashSlotItemSuper stashSlotItem = inventory.getInventorySlots().get(slotIndex);
		if(stashSlotItem != null) {
			if(stashSlotItem instanceof Equipment) {
				loadEquipmentLinkedTables(equipmentRepo, (Equipment)stashSlotItem);
			} else if(stashSlotItem instanceof BoostItem) {
				
			}
		}
		return stashSlotItem;
	}

	@Override
	@Transactional
	public boolean putItemInSlot(Account account, StashSlotItemSuper stashSlotItem, Integer slotIndex) throws InventoryNotFound {
		Inventory inventory = inventoryRepo.findOneByAccountIdAndHardcoreAndIronborn(
				account.getId(), stashSlotItem.isHardcore(), stashSlotItem.isIronborn());
		if (inventory == null) {
            throw new InventoryNotFound();
		}
		
		Map<Integer, StashSlotItemSuper> slots = inventory.getInventorySlots();
		if(slots.get(slotIndex) != null) {
			logger.debug("Inventory slot {} should have been empty, but was {}", slotIndex, slots.get(slotIndex));
			return false;
		}
		slots.put(slotIndex, stashSlotItem);
		inventoryRepo.save(inventory);
		
		return true;
	}
}
