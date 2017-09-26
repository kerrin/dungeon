package org.kerrin.dungeon.controller.secure;

import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.EquipmentService;
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
@RequestMapping(value="/play/enchant")
public class SecureEnchantingController extends SuperSecurePublic {
	
	private static final Logger logger = LoggerFactory.getLogger(SecureEnchantingController.class);
	
	private final EquipmentService equipmentService;
	private final BoostItemService boostItemService;
	
	@Autowired
	public SecureEnchantingController(AccountService accountService, 
			AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService,
			AccountMessageService accountMessageService, EquipmentService equipmentService, BoostItemService boostItemService, 
			Facebook facebook) {
		super(accountService, accountConfigService, accountCurrencyService, accountMessageService, facebook);

		this.equipmentService = equipmentService;
		this.boostItemService = boostItemService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String getEnchant(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Get Enchant");
		Account account = accountService.findByPrinciple(principle);
		setUpViewType(viewTypeForm, model);

		if(account == null) {
			model.addAttribute("error", "Could not find that equipment on your account");
			return "play/enchantEquipment";
		}		
		
		return "play/enchant";
	}
	
	/**
	 * Look at the details of a specific equipment
	 */
	@RequestMapping(value = "/{equipmentId}", method = RequestMethod.GET)
	public String getEquipment(HttpServletRequest request, Model model, Principal principle, 
			@PathVariable("equipmentId") long equipmentId) {
		logger.debug("API: Get Equipment Details for enchanting");
		Account account = accountService.findByPrinciple(principle);
		Equipment equipment = equipmentService.findById(equipmentId);

		if(account == null || equipment == null) {
			model.addAttribute("error","Could not find your account or the equipment");
			return "play/enchantEquipment";
		}
		hardcore = equipment.isHardcore();
		ironborn = equipment.isIronborn();
		setUpViewTypeModel(model);
		if(!accountService.accountOwnsEquipment(account, equipment)) {
			model.addAttribute("error","Could not find that equipment on your account");
			return "play/enchantEquipment";
		}
		model.addAttribute("equipment", equipment);
		BoostItem usableTypeBoost = boostItemService.getValidBoostItemType(
				account, hardcore, ironborn, BoostItemType.ENCHANT_TYPE, equipment.getLevel(), true);
		if(usableTypeBoost != null) {
			model.addAttribute("usableTypeBoost", usableTypeBoost);
		}
		BoostItem usableRangeBoost = boostItemService.getValidBoostItemType(
				account, hardcore, ironborn, BoostItemType.ENCHANT_RANGE, equipment.getLevel(), true);
		if(usableRangeBoost != null) {
			model.addAttribute("usableRangeBoost", usableRangeBoost);
		}
		BoostItem usableImproveRangeBoost = boostItemService.getValidBoostItemType(
				account, hardcore, ironborn, BoostItemType.ENCHANT_IMPROVE_RANGE, equipment.getLevel(), true);
		if(usableImproveRangeBoost != null) {
			model.addAttribute("usableImproveRangeBoost", usableImproveRangeBoost);
		}
		BoostItem usableCurseBoost = boostItemService.getValidBoostItemType(
				account, hardcore, ironborn, BoostItemType.ENCHANT_REMOVE_CURSE, equipment.getLevel(), true);
		if(usableCurseBoost != null) {
			model.addAttribute("usableCurseBoost", usableCurseBoost);
		}
		BoostItem usableQualityBoost = boostItemService.getValidBoostItemType(
				account, hardcore, ironborn, BoostItemType.IMPROVE_QUALITY, equipment.getLevel(), true);
		if(usableQualityBoost != null) {
			model.addAttribute("usableQualityBoost", usableQualityBoost);
		}
		
		return "play/enchantEquipment";
	}
}
