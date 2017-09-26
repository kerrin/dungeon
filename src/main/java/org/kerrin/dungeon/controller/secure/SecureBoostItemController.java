package org.kerrin.dungeon.controller.secure;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountBoost;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.service.AccountBoostService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/play/boostitem")
public class SecureBoostItemController extends SuperSecurePublic {
	
	private static final Logger logger = LoggerFactory.getLogger(SecureBoostItemController.class);
	
	private final InventoryService inventoryService;
	
	private final EquipmentService equipmentService;
	
	private final BoostItemService boostItemService;
	
	private final AccountBoostService accountBoostService;
	
	private final AchievementService achievementService;
	
	private final HiscoreService hiscoreService;
	
	@Autowired
	public SecureBoostItemController(AccountService accountService, 
			AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService, 
			Facebook facebook, InventoryService inventoryService, 
			EquipmentService equipmentService, BoostItemService boostItemService, AccountBoostService accountBoostService,
			AchievementService achievementService, HiscoreService hiscoreService, 
			AccountMessageService accountMessageService) {
		super(accountService, accountConfigService, accountCurrencyService, accountMessageService, facebook);
		this.inventoryService = inventoryService;
		this.equipmentService = equipmentService;
		this.boostItemService = boostItemService;
		this.accountBoostService = accountBoostService;
		this.achievementService = achievementService;
		this.hiscoreService = hiscoreService;
	}
	
	@RequestMapping(value="/{boostItemId}/redeem", method = RequestMethod.GET)
	public String redeemItem(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, @PathVariable long boostItemId, 
			BindingResult bindingResult) {
		logger.debug("Redeem Boost Item");

		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		BoostItem boostItem = boostItemService.findById(boostItemId);
		if(!accountService.accountOwnsBoostItem(account, boostItem)) {
			logger.debug("Not owned");
			return null;
		}

		boolean clearLocations = false;
		switch(boostItem.getBoostItemType()) {
		case DUNGEON_TOKENS:
			redeemTokens(request, account, boostItem);
			break;
		case MAGIC_FIND: case XP_BOOST:
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, boostItem.getLevel() * 10);
			Date endDateTime = cal.getTime();
			accountBoostService.redeemBoost(new AccountBoost(account.getId(), boostItem.isHardcore(), boostItem.isIronborn(), boostItem.getBoostItemType(), endDateTime));
			break;
		case CHANGE_NAME:
		case DUNGEON_SPEED:
		case ENCHANT_IMPROVE_RANGE:
		case ENCHANT_RANGE:
		case ENCHANT_REMOVE_CURSE:
		case ENCHANT_TYPE:
		case IMPROVE_QUALITY:
		case LEVEL_UP:
		case RESURRECTION:
			// No action needed
			logger.debug("No additional action");
			clearLocations = true;
			break;
		case UNKNOWN:
			logger.error("Boost Item Type Unknown for item: "+boostItem);
			break;
		default:
			logger.error("Unknown Boost Item Type "+boostItem.getBoostItemType()+" for item: "+boostItem);
		}

		List<Achievement> newAchievements = achievementService.redeemBoostItem(request.getContextPath(), account, boostItem);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			logger.debug("New Achievements");
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			logger.debug("New Points");
			hiscoreService.achievement(account, newPoints);
		}
		
		try {
			if(clearLocations) {
				boostItemService.clearLocations(boostItem);
				logger.debug("Clear Locations");
			} else {
				boostItemService.delete(account, boostItem);
			}
		} catch (DungeonNotFound e) {
			logger.error("Unknown Boost Item "+boostItem+" unknown dungeon");
			e.printStackTrace();
		} catch (InventoryNotFound e) {
			logger.error("Unknown Boost Item "+boostItem+" unknown inventory");
			e.printStackTrace();
		} catch (AccountIdMismatch e) {
			logger.error("Unknown Boost Item "+boostItem+" unknown account id");
			e.printStackTrace();
		} catch (EquipmentNotFound e) {
			logger.error("Unknown Boost Item "+boostItem+" unknown equipment!!!");
			e.printStackTrace();
		} catch (BoostItemNotFound e) {
			logger.error("Unknown Boost Item "+boostItem+" unknown boost item!");
			e.printStackTrace();
		}
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		setUpViewTypeModel(model);
		return "play/index";
	}
	
	/**
	 * 
	 * @param request
	 * @param account
	 * @param boostItem
	 */
	private void redeemTokens(HttpServletRequest request, Account account, BoostItem boostItem) {
		try {
			boostItemService.delete(account, boostItem);
			String reference = "redeem-boost-tokens-"+account.getUsername()+"-"+boostItem.getId();			
			accountCurrencyService.adjustCurrency(account, boostItem.isHardcore(),boostItem.isIronborn(), 
					boostItem.getSalvageValue(), ModificationType.GAIN_SALVAGE, reference);
			hiscoreService.tokensEarnt(account, boostItem.isHardcore(),boostItem.isIronborn(), boostItem.getLevel());
		} catch (EquipmentNotFound e) {
			logger.error("Reedem boostItem tokens failed: {}", e.getMessage());
		} catch (DungeonNotFound e) {
			logger.error("Reedem boostItem tokens failed: {}", e.getMessage());
		} catch (AccountIdMismatch e) {
			logger.error("Reedem boostItem tokens failed: {}", e.getMessage());
		} catch (InventoryNotFound e) {
			logger.error("Reedem boostItem tokens failed: {}", e.getMessage());
		} catch (BoostItemNotFound e) {
			logger.error("Reedem boostItem tokens failed: {}", e.getMessage());
		}
	}
}
