package org.kerrin.dungeon.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.kerrin.dungeon.forms.InventoryForm;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.InventoryService;
import org.kerrin.dungeon.service.ReloadPanels;
import org.kerrin.dungeon.service.StashSlotItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping(value="/api/account/{accountApiKey}/inventory")
public class ApiInventoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiInventoryController.class);
	
	private final AccountService accountService;
	private final InventoryService inventoryService;
	private final EquipmentService equipmentService;
	private final BoostItemService boostItemService;
	private final StashSlotItemService stashSlotItemService;
	
	@Autowired
	public ApiInventoryController(AccountService accountService, InventoryService inventoryService, 
			EquipmentService equipmentService, BoostItemService boostItemService, 
			StashSlotItemService stashSlotItemService) {
		super();
		this.accountService = accountService;
		this.inventoryService = inventoryService;
		this.equipmentService = equipmentService;
		this.boostItemService = boostItemService;
		this.stashSlotItemService = stashSlotItemService;
	}

	/**
	 * Get the full inventory list
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Inventory getEquipment(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("API: Get Inventory");
		Account account = accountService.findByApiKey(accountApiKey);
		if(account == null) {
			return null;
		}
		
		Inventory inventory = inventoryService.findByAccount(account, viewTypeForm.isHardcore(), viewTypeForm.isIronborn());
		if(inventory == null) {
			return null;
		}
		
		return inventory;
	}
	
	/**
	 * Get the equipment in the inventory slot
	 */
	@RequestMapping(value = "/{slotIndex}", method = RequestMethod.GET)
	public StashSlotItemSuper getInventorySlot(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, @PathVariable("slotIndex") int slotIndex,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("API: Get Equipment in Inventory Slot");
		Account account = accountService.findByApiKey(accountApiKey);
		if(account == null) {
			return null;
		}
		
		Inventory inventory = inventoryService.findByAccount(account, viewTypeForm.isHardcore(), viewTypeForm.isIronborn());
		if(inventory == null) {
			return null;
		}
		
		Map<Integer, StashSlotItemSuper> slots = inventory.getInventorySlots();
		
		if(slots == null) return null;
		
		return slots.get(slotIndex);
	}
	
	/**
	 * Update an inventory slot
	 */
	@RequestMapping(value = "/{slotIndex}", method = RequestMethod.POST)
	public ReloadPanels updateInventorySlot(HttpServletRequest request, Model model,  
			@ModelAttribute("inventoryForm") @Valid InventoryForm inventoryForm, BindingResult bindingResult,
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("slotIndex") int slotIndex) {		
		logger.debug("API: Update Inventory slot "+slotIndex);
		Account account = accountService.findByApiKey(accountApiKey);
		if(account == null) {
			return null;
		}
		Inventory inventory = inventoryService.findByAccount(account, inventoryForm.isHardcore(), inventoryForm.isIronborn());
		
		if(inventory == null) {
			return null;
		}
		
		if (bindingResult.hasErrors()) {
			return null;
        }
		Map<Integer, StashSlotItemSuper> slots = inventory.getInventorySlots();
		if(slots == null) {
			return null;
		}
		
		Long itemId = inventoryForm.getItemId();
		if(itemId != null) {
			try {
				if(inventoryForm.isEquipment()) {
					Equipment equipment = equipmentService.findById(itemId);
					if(!accountService.accountOwnsEquipment(account, equipment)) {
						return null;
					}
					return stashSlotItemService.swapItemWithInventory(account, (StashSlotItemSuper)equipment, slotIndex);
				} else {
					BoostItem boostItem = boostItemService.findById(itemId);
					if(!accountService.accountOwnsBoostItem(account, boostItem)) {
						return null;
					}
					return stashSlotItemService.swapItemWithInventory(account, (StashSlotItemSuper)boostItem, slotIndex);
				}	
			} catch (InventoryNotFound e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (DungeonNotFound e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (AccountIdMismatch e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (EquipmentNotFound e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (InventoryException e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (CantEquipToDungeon e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (BoostItemNotFound e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (CantEquipToMessage e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (CharacterNotFound e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (CharacterSlotNotFound e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (CharacterEquipmentNotFound e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			} catch (MessageEquipmentNotFound e) {
				model.addAttribute("error", "Error updating inventory: "+e.getMessage());
			}
		}
		
		return null;
	}
}
