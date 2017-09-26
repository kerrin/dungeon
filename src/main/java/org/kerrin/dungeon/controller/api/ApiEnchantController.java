package org.kerrin.dungeon.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryNotFound;
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
@RequestMapping(value="/api/account/{accountApiKey}/enchant")
public class ApiEnchantController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiEnchantController.class);

	/** Chance a boost item quality will boost the quality of an item in 1000 */
	private static final int IMPROVE_QUALITY_CHANCE = 1000;
	
	private final AccountService accountService;
	private final AccountCurrencyService accountCurrencyService;
	private final EquipmentService equipmentService;
	private final AchievementService achievementService;
	private final HiscoreService hiscoreService;
	private final BoostItemService boostItemService;
	
	@Autowired
	public ApiEnchantController(AccountService accountService, AccountCurrencyService accountCurrencyService, 
			EquipmentService equipmentService, AchievementService achievementService,
			HiscoreService hiscoreService, BoostItemService boostItemService) {
		super();
		this.accountService = accountService;
		this.accountCurrencyService = accountCurrencyService;
		this.equipmentService = equipmentService;
		this.achievementService = achievementService;
		this.hiscoreService = hiscoreService;
		this.boostItemService = boostItemService;
	}

	/**
	 * Look at the details of a specific equipment
	 */
	@RequestMapping(value = "/{equipmentId}", method = RequestMethod.GET)
	public Equipment getEquipment(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, @PathVariable("equipmentId") long equipmentId) {
		logger.debug("API: Get Equipment Details for enchanting");
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
	 * Reroll an attribute completely, probably changing the attribute
	 */
	@RequestMapping(value = "/{equipmentId}/enchant/{attributeTypeId}", method = RequestMethod.POST)
	public Equipment rerollEquipmentAttribute(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("equipmentId") long equipmentId,
			@PathVariable("attributeTypeId") int attributeTypeId) {
		logger.debug("API: Enchant Equipment Attribute");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		// Deduct token cost
		if(!deductTokens(account, equipment, attributeTypeId, true, false)) {
		    return equipment;
		}
		
		try {
			equipment = rerollAttribute(request, attributeTypeId, account, equipment);
		} catch (EquipmentNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		}
		
		return equipment;
	}
	
	/**
	 * Reroll an attribute completely, probably changing the attribute
	 */
	@RequestMapping(value = "/{equipmentId}/enchant/base/{attributeTypeId}", method = RequestMethod.POST)
	public Equipment rerollEquipmentBaseAttribute(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("equipmentId") long equipmentId,
			@PathVariable("attributeTypeId") int attributeTypeId) {
		logger.debug("API: Enchant Equipment Base Attribute");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		// Deduct token cost
		if(!deductTokens(account, equipment, attributeTypeId, true, true)) {
		    return equipment;
		}
		
		// Reroll Equipment Attribute
		try {
			equipment = rerollBaseAttribute(request, account, equipment);
		} catch (EquipmentNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		}

		
		return equipment;
	}
	
	/**
	 * Reroll an attribute completely, probably changing the attribute
	 */
	@RequestMapping(value = "/{equipmentId}/enchant/defence/{attributeTypeId}", method = RequestMethod.POST)
	public Equipment rerollEquipmentDefenceAttribute(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("equipmentId") long equipmentId,
			@PathVariable("attributeTypeId") int attributeTypeId) {
		logger.debug("API: Enchant Equipment Defence Attribute");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		// Deduct token cost
		if(!deductTokens(account, equipment, attributeTypeId, true, true)) {
		    return equipment;
		}
		
		// Reroll Equipment Attribute
		try {
			equipment = rerollDefenceAttribute(request, account, equipment);
		} catch (EquipmentNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		}
		
		return equipment;
	}

	/**
	 * Reroll an attribute range, attribute type unchanged
	 */
	@RequestMapping(value = "/{equipmentId}/enchant/{attributeTypeId}/range", method = RequestMethod.POST)
	public Equipment rerollEquipmentAttributeRange(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("equipmentId") long equipmentId,
			@PathVariable("attributeTypeId") int attributeTypeId) {
		logger.debug("API: Enchant Equipment Attribute Range");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		// Deduct token cost
		if(!deductTokens(account, equipment, attributeTypeId, false, false)) {
		    return equipment;
		}
		
		// Reroll Equipment Attribute Range
		try {
			equipment = rerollAttributeRange(request, attributeTypeId, account, equipment, false);
		} catch (EquipmentNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		}
		
		return equipment;
	}

	/**
	 * Reroll an attribute range, attribute type unchanged
	 */
	@RequestMapping(value = "/{equipmentId}/enchant/{attributeTypeId}/range/base", method = RequestMethod.POST)
	public Equipment rerollEquipmentBaseAttributeRange(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("equipmentId") long equipmentId,
			@PathVariable("attributeTypeId") int attributeTypeId) {
		logger.debug("API: Enchant Equipment Attribute Range");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		// Deduct token cost
		if(!deductTokens(account, equipment, attributeTypeId, false, true)) {
		    return equipment;
		}
		
		// Reroll Equipment Attribute Range
		try {
			equipment = rerollBaseAttributeRange(request, account, equipment, false);
		} catch (EquipmentNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		}
		
		return equipment;
	}
	

	/**
	 * Reroll an attribute range, attribute type unchanged
	 */
	@RequestMapping(value = "/{equipmentId}/enchant/{attributeTypeId}/range/defence", method = RequestMethod.POST)
	public Equipment rerollEquipmentDefenceAttributeRange(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("equipmentId") long equipmentId,
			@PathVariable("attributeTypeId") int attributeTypeId) {
		logger.debug("API: Enchant Equipment Attribute Range");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		// Deduct token cost
		if(!deductTokens(account, equipment, attributeTypeId, false, true)) {
		    return equipment;
		}
		
		// Reroll Equipment Attribute Range
		try {
			equipment = rerollDefenceAttributeRange(request, account, equipment, false);
		} catch (EquipmentNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		}
		
		return equipment;
	}
	
	/**
	 * Reroll an attribute completely, probably changing the attribute
	 */
	@RequestMapping(value = "/{equipmentId}/enchant/{attributeTypeId}/{boostItemId}", method = RequestMethod.POST)
	public Equipment rerollEquipmentAttributeBoostItem(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("equipmentId") long equipmentId,
			@PathVariable("attributeTypeId") int attributeTypeId,
			@PathVariable("boostItemId") int boostItemId) {
		logger.debug("API: Enchant Equipment Attribute (Boost Item)");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		// Get the boost token
		BoostItem boostItem = boostItemService.findById(boostItemId);
		if(boostItem == null) {
			return equipment;
		}
		try {
			switch(boostItem.getBoostItemType()) {
			case ENCHANT_IMPROVE_RANGE:
				equipment = rerollAttributeRange(request, attributeTypeId, account, equipment, true);
				break;
			case ENCHANT_RANGE:
				equipment = rerollAttributeRange(request, attributeTypeId, account, equipment, false);
				break;
			case ENCHANT_REMOVE_CURSE:
				if(!equipment.removeCurse(attributeTypeId)) {
					return equipment;
				}
				break;
			case ENCHANT_TYPE:
				equipment = rerollAttribute(request, attributeTypeId, account, equipment);
				break;
			default:
				return equipment;
			}
			boostItemService.delete(account, boostItem);
			equipment = updateEquipment(request, account, equipment);
		} catch (EquipmentNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (BoostItemNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (DungeonNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (AccountIdMismatch e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (InventoryNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		}
		
		return equipment;
	}
	
	/**
	 * Reroll an attribute completely, probably changing the attribute
	 */
	@RequestMapping(value = "/{equipmentId}/enchant/base/{attributeTypeId}/{boostItemId}", method = RequestMethod.POST)
	public Equipment rerollEquipmentBaseAttributeBoostItem(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("equipmentId") long equipmentId,
			@PathVariable("attributeTypeId") int attributeTypeId,
			@PathVariable("boostItemId") int boostItemId) {
		logger.debug("API: Enchant Equipment Attribute (Boost Item)");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		// Get the boost token
		BoostItem boostItem = boostItemService.findById(boostItemId);
		if(boostItem == null) {
			return equipment;
		}
		try {
			switch(boostItem.getBoostItemType()) {
			case ENCHANT_IMPROVE_RANGE:
				equipment = rerollBaseAttributeRange(request, account, equipment, true);
				break;
			case ENCHANT_RANGE:
				equipment = rerollBaseAttributeRange(request, account, equipment, false);
				break;
			case ENCHANT_TYPE:
				equipment = rerollBaseAttribute(request, account, equipment);
				break;
			default:
				return equipment;
			}
			boostItemService.delete(account, boostItem);
			equipment = updateEquipment(request, account, equipment);
		} catch (EquipmentNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (BoostItemNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (DungeonNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (AccountIdMismatch e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (InventoryNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		}
		
		return equipment;
	}
	
	/**
	 * Reroll an attribute completely, probably changing the attribute
	 */
	@RequestMapping(value = "/{equipmentId}/enchant/defence/{attributeTypeId}/{boostItemId}", method = RequestMethod.POST)
	public Equipment rerollEquipmentDefenceAttributeBoostItem(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("equipmentId") long equipmentId,
			@PathVariable("attributeTypeId") int attributeTypeId,
			@PathVariable("boostItemId") int boostItemId) {
		logger.debug("API: Enchant Equipment Attribute (Boost Item)");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		// Get the boost token
		BoostItem boostItem = boostItemService.findById(boostItemId);
		if(boostItem == null) {
			return equipment;
		}
		try {
			switch(boostItem.getBoostItemType()) {
			case ENCHANT_IMPROVE_RANGE:
				equipment = rerollDefenceAttributeRange(request, account, equipment, true);
				break;
			case ENCHANT_RANGE:
				equipment = rerollDefenceAttributeRange(request, account, equipment, false);
				break;
			case ENCHANT_TYPE:
				equipment = rerollDefenceAttribute(request, account, equipment);
				break;
			default:
				return equipment;
			}
			boostItemService.delete(account, boostItem);
			equipment = updateEquipment(request, account, equipment);
		} catch (EquipmentNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (BoostItemNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (DungeonNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (AccountIdMismatch e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (InventoryNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		}
		
		return equipment;
	}

	/**
	 * Reroll an attribute completely, probably changing the attribute
	 */
	@RequestMapping(value = "/{equipmentId}/quality/{boostItemId}", method = RequestMethod.POST)
	public Equipment improveEquipmentQualityBoostItem(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("equipmentId") long equipmentId,
			@PathVariable("boostItemId") int boostItemId) {
		logger.debug("API: Enchant Equipment Improve Quality (Boost Item)");
		Account account = accountService.findByApiKey(accountApiKey);
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(account == null || equipment == null) {
			return null;
		}
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			return null;
		}
		
		// Get the boost token
		BoostItem boostItem = boostItemService.findById(boostItemId);
		if(boostItem == null) {
			return equipment;
		}
		try {
			switch(boostItem.getBoostItemType()) {
			case IMPROVE_QUALITY:
				equipment.improveQuality(IMPROVE_QUALITY_CHANCE);
				break;
			default:
				logger.debug("Failed chance at quality upgrade");
				return equipment;
			}
			boostItemService.delete(account, boostItem);
			equipment = updateEquipment(request, account, equipment);
		} catch (EquipmentNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (BoostItemNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (DungeonNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (AccountIdMismatch e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		} catch (InventoryNotFound e) {
			logger.error("Error: {}", e.getMessage());
			e.printStackTrace();
            return equipment;
		}
		
		return equipment;
	}
	
	/**
	 * 
	 * @param account
	 * @param equipment
	 * @param attributeTypeId
	 * @param isType
	 * @return	Successfully deducted tokens
	 */
	protected boolean deductTokens(Account account, Equipment equipment, int attributeTypeId, boolean isType, boolean isBaseOrDefence) {
		// Deduct token cost
		String reference = "api-enchant-"+(isType?"type":"range") + 
				"-"+account.getUsername()+"-"+equipment.getId()+"-"+System.currentTimeMillis();
		int cost = equipment.getLevel();
		if(isType) {
			if(isBaseOrDefence) {
				// Base and defence cost 5 times as much for a type reroll
				cost *= 5;
			}
			if(equipment.isAttributeCursed(attributeTypeId)) {
				// Cursed cost 5 time as much to reroll type
				cost *= 5;
			}
		} else {
			// Range costs 10 times as much to reroll
			cost *= 10;
			if(isBaseOrDefence) {
				// Base and defence cost twice as much for a range
				cost *= 2;
			}
		}
		if(!accountCurrencyService.adjustCurrency(account, equipment.isHardcore(), equipment.isIronborn(), 
				-cost, 
				ModificationType.SPEND_ENCHANT, reference)) {
		    return false;
		}
		return true;
	}
	
	protected Equipment rerollAttribute(HttpServletRequest request, int attributeTypeId, Account account,
			Equipment equipment) throws EquipmentNotFound {
		// Reroll Equipment Attribute
		equipment.rerollAttribute(attributeTypeId);		
		equipment = updateEquipment(request, account, equipment);
		return equipment;
	}
	
	protected Equipment rerollBaseAttribute(HttpServletRequest request, Account account,
			Equipment equipment) throws EquipmentNotFound {
		// Reroll Equipment Attribute
		equipment.rerollBaseAttribute();		
		equipment = updateEquipment(request, account, equipment);
		return equipment;
	}
	
	protected Equipment rerollDefenceAttribute(HttpServletRequest request, Account account,
			Equipment equipment) throws EquipmentNotFound {
		// Reroll Equipment Attribute
		equipment.rerollDefenceAttribute();		
		equipment = updateEquipment(request, account, equipment);
		return equipment;
	}
	
	protected Equipment rerollAttributeRange(HttpServletRequest request, int attributeTypeId, Account account,
			Equipment equipment, boolean improveOnly) throws EquipmentNotFound {
		// Reroll Equipment Attribute
		if(equipment.rerollAttributeRange(attributeTypeId, improveOnly)) { 		
			equipment = updateEquipment(request, account, equipment);
		} else {
			logger.error("Unable to reroll attribute ID "+attributeTypeId + " on " + equipment);
		}
		return equipment;
	}
	
	protected Equipment rerollBaseAttributeRange(HttpServletRequest request, Account account,
			Equipment equipment, boolean improveOnly) throws EquipmentNotFound {
		// Reroll Equipment Attribute
		if(equipment.rerollBaseAttributeRange(improveOnly)) {		
			equipment = updateEquipment(request, account, equipment);
		} else {
			logger.error("Unable to reroll base attribute on " + equipment);
		}
		return equipment;
	}
	
	protected Equipment rerollDefenceAttributeRange(HttpServletRequest request, Account account,
			Equipment equipment, boolean improveOnly) throws EquipmentNotFound {
		// Reroll Equipment Attribute
		if(equipment.rerollDefenceAttributeRange(improveOnly)) {		
			equipment = updateEquipment(request, account, equipment);
		} else {
			logger.error("Unable to reroll defence attribute on " + equipment);
		}
		return equipment;
	}

	protected Equipment updateEquipment(HttpServletRequest request, Account account, Equipment equipment)
			throws EquipmentNotFound {
		equipment = equipmentService.update(equipment, true);
		List<Achievement> newAchievements = achievementService.enchant(request.getContextPath(), account, equipment);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
		return equipment;
	}
}
