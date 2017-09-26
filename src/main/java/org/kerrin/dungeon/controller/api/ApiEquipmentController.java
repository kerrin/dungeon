package org.kerrin.dungeon.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryException;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.exception.MessageEquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.HiscoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping(value="/api/account/{accountApiKey}/equipment")
public class ApiEquipmentController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiEquipmentController.class);
	
	private final AccountService accountService;
	private final AccountCurrencyService accountCurrencyService;
	private final EquipmentService equipmentService;
	private final BoostItemService boostItemService;
	private final HiscoreService hiscoreService;
	private final AchievementService achievementService;
	
	@Autowired
	public ApiEquipmentController(AccountService accountService, AccountCurrencyService accountCurrencyService, 
			EquipmentService equipmentService, BoostItemService boostItemService,
			HiscoreService hiscoreService, AchievementService achievementService) {
		super();
		this.accountService = accountService;
		this.accountCurrencyService = accountCurrencyService;
		this.equipmentService = equipmentService;
		this.boostItemService = boostItemService;
		this.hiscoreService = hiscoreService;
		this.achievementService = achievementService;
	}

	/**
	 * Look at the details of a specific equipment
	 */
	@RequestMapping(value = "/{equipmentId}", method = RequestMethod.GET)
	public Equipment getEquipment(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, @PathVariable("equipmentId") long equipmentId) {
		logger.debug("API: Get Equipment Details");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		return equipment;
	}

	/**
	 * Salvage some equipment
	 */
	@RequestMapping(value = "/{equipmentId}/salvage", method = RequestMethod.POST)
	public Equipment salvageEquipment(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, @PathVariable("equipmentId") long equipmentId) {
		logger.debug("API: Salvage Equipment");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		try {
			List<Achievement> newAchievements = achievementService.salvage(request.getContextPath(), account, equipment);
			int newPoints = 0;
			for(Achievement achievement:newAchievements) {
				newPoints += achievement.getPoints();
			}
			if(newPoints > 0) {
				hiscoreService.achievement(account, newPoints);
			}

			equipmentService.delete(account, equipment);
			String reference = "api-salvage-equipment-"+account.getUsername()+"-"+equipmentId;			
			accountCurrencyService.adjustCurrency(account, equipment.isHardcore(),equipment.isIronborn(), 
					equipment.getSalvageValue(), ModificationType.GAIN_SALVAGE, reference);
			hiscoreService.tokensEarnt(account, equipment.isHardcore(),equipment.isIronborn(), equipment.getLevel());
		} catch (EquipmentNotFound e) {
			logger.error("Salvage equipment failed: {}", e.getMessage());
			return null;
		} catch (DungeonNotFound e) {
			logger.error("Salvage equipment failed: {}", e.getMessage());
			return null;
		} catch (AccountIdMismatch e) {
			logger.error("Salvage equipment failed: {}", e.getMessage());
			return null;
		} catch (InventoryNotFound e) {
			logger.error("Salvage equipment failed: {}", e.getMessage());
			return null;
		} catch (CharacterNotFound e) {
			logger.error("Salvage equipment failed: {}", e.getMessage());
			return null;
		} catch (CharacterSlotNotFound e) {
			logger.error("Salvage equipment failed: {}", e.getMessage());
			return null;
		} catch (CharacterEquipmentNotFound e) {
			logger.error("Salvage equipment failed: {}", e.getMessage());
			return null;
		} catch (BoostItemNotFound e) {
			logger.error("Salvage equipment failed: {}", e.getMessage());
			return null;
		} catch (MessageEquipmentNotFound e) {
			logger.error("Salvage equipment failed: {}", e.getMessage());
			return null;
		} catch (InventoryException e) {
			logger.error("Salvage equipment failed: {}", e.getMessage());
			return null;
		}
		
		return equipment;
	}

	/**
	 * Salvage some equipment
	 */
	@RequestMapping(value = "/{boostItemId}/salvageBoostItem", method = RequestMethod.POST)
	public BoostItem salvageBoostItem(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, @PathVariable("boostItemId") long boostItemId) {
		logger.debug("API: Salvage BoostItem");
		Account account = accountService.findByApiKey(accountApiKey);
		BoostItem boostItem = boostItemService.findById(boostItemId);
		
		if(account == null || boostItem == null) {
			return null;
		}
		if(!accountService.accountOwnsBoostItem(account, boostItem)) {
			return null;
		}
		
		try {
			List<Achievement> newAchievements = achievementService.salvage(request.getContextPath(), account, boostItem);
			int newPoints = 0;
			for(Achievement achievement:newAchievements) {
				newPoints += achievement.getPoints();
			}
			if(newPoints > 0) {
				hiscoreService.achievement(account, newPoints);
			}

			boostItemService.delete(account, boostItem);
			String reference = "api-salvage-boost-"+account.getUsername()+"-"+boostItemId;			
			accountCurrencyService.adjustCurrency(account, boostItem.isHardcore(),boostItem.isIronborn(), 
					boostItem.getSalvageValue(), ModificationType.GAIN_SALVAGE, reference);
			hiscoreService.tokensEarnt(account, boostItem.isHardcore(),boostItem.isIronborn(), boostItem.getLevel());
		} catch (EquipmentNotFound e) {
			logger.error("Salvage boostItem failed: {}", e.getMessage());
			return null;
		} catch (DungeonNotFound e) {
			logger.error("Salvage boostItem failed: {}", e.getMessage());
			return null;
		} catch (AccountIdMismatch e) {
			logger.error("Salvage boostItem failed: {}", e.getMessage());
			return null;
		} catch (InventoryNotFound e) {
			logger.error("Salvage boostItem failed: {}", e.getMessage());
			return null;
		} catch (BoostItemNotFound e) {
			logger.error("Salvage boostItem failed: {}", e.getMessage());
			return null;
		}
		
		return boostItem;
	}
}
