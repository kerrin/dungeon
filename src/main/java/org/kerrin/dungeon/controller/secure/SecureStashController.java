package org.kerrin.dungeon.controller.secure;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.service.InventoryService;
import org.kerrin.dungeon.utils.Facebook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/play/stash")
public class SecureStashController extends SuperSecurePublic {
	
	private static final Logger logger = LoggerFactory.getLogger(SecureStashController.class);
	
	private final InventoryService inventoryService;
	
	private final EquipmentService equipmentService;
	
	private final BoostItemService boostItemService;
	
	private final AchievementService achievementService;
	
	private final HiscoreService hiscoreService;
	
	@Autowired
	public SecureStashController(AccountService accountService, 
			AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService, 
			Facebook facebook, InventoryService inventoryService, 
			EquipmentService equipmentService, BoostItemService boostItemService,
			AchievementService achievementService, HiscoreService hiscoreService, 
			AccountMessageService accountMessageService) {
		super(accountService, accountConfigService, accountCurrencyService, accountMessageService, facebook);
		this.inventoryService = inventoryService;
		this.equipmentService = equipmentService;
		this.boostItemService = boostItemService;
		this.achievementService = achievementService;
		this.hiscoreService = hiscoreService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String getStash(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Get Stash");

		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		Inventory inventory = inventoryService.findByAccount(account, hardcore, ironborn);
		
		if(inventory == null) {
			inventory = new Inventory(account, hardcore, ironborn);
			logger.error("Had to create empty inventory for account "+account+" Hardcore: {}, Ironborn: {}", hardcore, ironborn);
			inventoryService.create(inventory);
		}

		model.addAttribute("inventory", inventory);
		
		return "play/stash";
	}
	
	@RequestMapping(value="/purchase", method = RequestMethod.GET)
	public String purchaseSlot(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Purchase Stash Slot");

		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}

		Inventory inventory = inventoryService.findByAccount(account, hardcore, ironborn);
		
		if(inventory == null) {
			inventory = new Inventory(account, hardcore, ironborn);
			logger.error("Had to create empty inventory for account "+account+" Hardcore: {}, Ironborn: {}", hardcore, ironborn);
			inventoryService.create(inventory);
		}
		
		int slotCost = inventory.getSize();
		slotCost *= slotCost;
		
		String reference = "stash-purchase-"+account.getUsername()+"-"+slotCost;
		if(hardcore) {
			reference += "-hardcore";
		}
		if(ironborn) {
			reference += "-ironborn";
		}
		if(!accountCurrencyService.adjustCurrency(account, 
				viewTypeForm.isHardcore(), viewTypeForm.isIronborn(), -slotCost, 
				ModificationType.SPEND_UPGRADE, reference)) {
			model.addAttribute("error", 
					"Not enough dungeon tokens to purchase a new inventory slot. You need " + slotCost + " tokens.");
		    return "play/addTokens";
		}
		
		inventory.purchaseSlots(1);
		try {
			inventory = inventoryService.update(inventory, false);
		} catch (Exception e) {
			e.printStackTrace();
			reference = "stash-purchase-fail-"+account.getUsername()+"-"+slotCost;			
			accountCurrencyService.adjustCurrency(account, 
					viewTypeForm.isHardcore(), viewTypeForm.isIronborn(), slotCost, 
					ModificationType.SPEND_UPGRADE, reference);
		}

		List<Achievement> newAchievements = achievementService.stashIncrease(request.getContextPath(), account, hardcore, ironborn);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
		
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		
		return "play/index";
	}
	

	
	@RequestMapping(value="/sort", method = RequestMethod.GET)
	public String sortStash(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Sort Stash");

		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		Inventory inventory = inventoryService.findByAccount(account, hardcore, ironborn);
		
		if(inventory == null) {
			inventory = new Inventory(account, hardcore, ironborn);
			logger.error("Had to create empty inventory for account "+account+" Hardcore: {}, Ironborn: {}", hardcore, ironborn);
			inventoryService.create(inventory);
		} else {
			Map<Integer, StashSlotItemSuper> slots = inventory.getInventorySlots();
			ArrayList<Equipment> equipmentList = new ArrayList<Equipment>();
			ArrayList<BoostItem> boostItemList = new ArrayList<BoostItem>();
			for(StashSlotItemSuper slot:slots.values()) {
				if(slot instanceof Equipment) {
					equipmentList.add((Equipment)slot);
				} else if(slot instanceof BoostItem) {
					boostItemList.add((BoostItem)slot);
				} else {
					logger.error("Unknown Stash Slot Item sub-class for {}", slot);
				}
			}
			Collections.sort(equipmentList);
			Collections.sort(boostItemList);
			slots = new HashMap<Integer, StashSlotItemSuper>();
			int stashOffset = inventory.getSize() - equipmentList.size() - boostItemList.size();
			try {
				for(int i=0; i < boostItemList.size(); i++) {
					BoostItem boostItem = boostItemList.get(i);
					int stashSlotId = i+stashOffset;
					boostItem.setStashSlotId(stashSlotId);
					boostItem = boostItemService.update(boostItem);
					slots.put(stashSlotId, boostItem);
				}
				stashOffset += boostItemList.size();
				for(int i=0; i < equipmentList.size(); i++) {
					Equipment equipment = equipmentList.get(i);
					int stashSlot = i+stashOffset;
					equipment.setEquipmentLocationId(stashSlot);
					equipment = equipmentService.update(equipment, false);
					slots.put(stashSlot, equipment);
				}
				inventory.setInventorySlots(slots);
				inventory = inventoryService.update(inventory, true);
			} catch (InventoryNotFound e) {
				model.addAttribute("error", 
						"Error while sorting inventory, inventory error.");
				e.printStackTrace();
			} catch (AccountNotFound e) {
				model.addAttribute("error", 
						"Error while sorting inventory, account error.");
				e.printStackTrace();
			} catch (EquipmentNotFound e) {
				model.addAttribute("error", 
						"Error while sorting inventory, missing equipment.");
				e.printStackTrace();
			} catch (BoostItemNotFound e) {
				model.addAttribute("error", 
						"Error while sorting inventory, missing boost item.");
				e.printStackTrace();
			}
		}
		
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		model.addAttribute("inventory", inventory);
		
		return "play/stash";
	}
}
