package org.kerrin.dungeon.controller.secure;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryException;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.exception.MessageEquipmentNotFound;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountConfig;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.utils.Facebook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/play")
public class SecureController extends SuperSecurePublic {
	
	private static final Logger logger = LoggerFactory.getLogger(SecureController.class);

	@Autowired
	public SecureController(AccountService accountService,
			AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService,
			AccountMessageService accountMessageService, Facebook facebook) {
		super(accountService, accountConfigService, accountCurrencyService, accountMessageService, facebook);
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		// Make sure we're processed
		accountService.processAccount(account);
		
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		setUpViewTypeModel(model);
		
		if(viewTypeForm.isNoBreakout()) {
			return "play/index";
		} else {
			return "play/breakout";
		}
	}

	/**
	 * Simply displays the salvage panel
	 */
	@RequestMapping(value="/salvage", method = RequestMethod.GET)
	public String salvageFrame(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		return "play/salvage";
	}
	
	/**
	 * Simply displays the summary section of the menu bar.
	 */
	@RequestMapping(value="/summary", method = RequestMethod.GET)
	public String summaryFrame(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		return "play/summary";
	}
	
	/**
	 * Change the view type between hardcore and ironborn settings
	 */
	@RequestMapping(value="/viewTypeChange", method = RequestMethod.GET)
	public String changeViewType(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		setUpViewType(viewTypeForm, model);
		
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		// Make sure we're processed
		accountService.processAccount(account);
		// Reset character and dungeon ids as the type changed
		model.addAttribute("charId", -1);
		model.addAttribute("dungeonId", 0);
		
		if(viewTypeForm.isNoBreakout()) {
			return "play/index";
		} else {
			return "play/breakout";
		}
	}
	
	@RequestMapping(value="/init", method = RequestMethod.GET)
	@Transactional
	public String initTouchScreen(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("touchScreen") String isTouchScreenString) {
		logger.debug("Initialise Touch Screen Status");
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		setUpViewTypeModel(model);
		
		boolean isTouchScreen = true;
		if(isTouchScreenString == null || isTouchScreenString.equalsIgnoreCase("false")) isTouchScreen  = false;
		
		account.setTouchScreen(isTouchScreen);
		try {
			accountService.update(account, false);
		} catch (AccountNotFound e) {
			e.printStackTrace();
		}
		
		return "blank";
	}

	@RequestMapping(value = "/displayMessages", method = RequestMethod.GET)
	public String showMessages(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("View Messages");
		List<AccountMessage> messages = new ArrayList<AccountMessage>();
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account != null) {
			messages = accountMessageService.findAllByAccount(account, hardcore, ironborn);
		}
		model.addAttribute("messages", messages);
		return "messages";
	}

	@RequestMapping(value = "/closeMessages", method = RequestMethod.GET)
	public String closeMessages(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Close Messages");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account != null) {
			try {
				accountMessageService.deleteAllByAccount(account, hardcore, ironborn);
			} catch (DungeonNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
			} catch (AccountIdMismatch e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
			} catch (EquipmentNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
			} catch (InventoryNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
			} catch (BoostItemNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
			} catch (CharacterNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
			} catch (CharacterSlotNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
			} catch (CharacterEquipmentNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
			} catch (MessageEquipmentNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
			} catch (InventoryException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
			}
		}
		return "blank";
	}
	
	@RequestMapping(value="config/toggleEquipmentCompare", method = RequestMethod.GET)
	public String toggleEquipmentCompare(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("toggleEquipmentCompare");
		return toggleConfig(request, locale, model, principle, viewTypeForm, bindingResult, AccountConfigType.EQUIPMENT_COMPARE);
	}
	
	@RequestMapping(value="config/toggleToolTips", method = RequestMethod.GET)
	public String toggleToolTips(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("toggleEquipmentCompare");
		return toggleConfig(request, locale, model, principle, viewTypeForm, bindingResult, AccountConfigType.TOOL_TIPS);
	}
	
	public String toggleConfig(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult, 
			AccountConfigType accountConfigType) {
		logger.debug("toggleEquipmentCompare");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		AccountConfig config = accountConfigService.findByAccount(account, hardcore, ironborn, accountConfigType);
		config.setValue(config.getValue() > 0?0:1);
		config = accountConfigService.update(config);
		
		return home(locale, model, principle, viewTypeForm, bindingResult);
	}
}
