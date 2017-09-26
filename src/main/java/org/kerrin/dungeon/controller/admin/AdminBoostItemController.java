package org.kerrin.dungeon.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.enums.Messages;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.BoostItemForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.BoostItemService;
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
@RequestMapping(value="/admin/boostitem")
public class AdminBoostItemController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminBoostItemController.class);
	
	@Autowired
	private BoostItemService boostItemService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountMessageService accountMessageService;
	/**
	 * Look at the details of a specific boostItem
	 */
	@RequestMapping(value = "/{boostItemId}", method = RequestMethod.GET)
	public String getBoostItem(HttpServletRequest request, Model model, @PathVariable long boostItemId) {
		logger.debug("Get BoostItem Details");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		BoostItem boostItem = boostItemService.findById(boostItemId);
		
		if(boostItem == null) {
			BoostItemForm boostItemForm = new BoostItemForm();
			model.addAttribute("boostItemForm", boostItemForm);
			model.addAttribute("boostItem", new BoostItem(boostItemForm));
			
			return "admin/modifyBoostItem";
		}
		
		model.addAttribute("boostItemForm", new BoostItemForm(boostItem));
		model.addAttribute("boostItem", boostItem);
		
		return "admin/modifyBoostItem";
	}
	
	/**
	 * Update a boostItem details
	 */
	@RequestMapping(value = "/{boostItemId}", method = RequestMethod.POST)
	public String updateBoostItem(HttpServletRequest request, Model model, @PathVariable long boostItemId, 
			@ModelAttribute("boostItemForm") @Valid BoostItemForm boostItemForm, BindingResult bindingResult) {		
		logger.debug("Update Form: "+boostItemForm.toString());
		if(!request.isUserInRole(AccountPrivilege.MODIFY.getName())) {
			model.addAttribute("error", "You do not have access to modify data");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";

		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("boostItemForm", boostItemForm);
			model.addAttribute("boostItem", new BoostItem(boostItemForm));
            return "admin/modifyBoostItem";
        }
		BoostItem boostItem = boostItemService.findById(boostItemForm.getId());
		Account account = accountService.findById(boostItemForm.getAccountId());
		boostItem.setAccount(account);
		boostItem.setLevel(boostItemForm.getLevel());
		boostItem.setBoostItemType(boostItemForm.getBoostItemType());
		boostItem.setHardcore(boostItemForm.isHardcore());
		boostItem.setIronborn(boostItemForm.isIronborn());
		boolean sendToAccount = boostItemForm.isSendToAccount();
		if(sendToAccount) {
			if(account == null) {
				model.addAttribute("error", "Error sending boostItem to account "+ boostItemForm.getAccountId() + " can't find account");
			} else {			
				String message = accountMessageService.replaceTokens(Messages.ADMIN_ITEM.getMessage(), account);
				AccountMessage accountMessage = accountMessageService.create(boostItemForm.getAccountId(), message, boostItem);
				boostItem.setMessageId(accountMessage.getId());
				boostItem.setDungeonId(-1);
				boostItem.setStashSlotId(-1);
			}
		}
		
		if(boostItem.getId() == boostItemId) {
			try {
				boostItem = boostItemService.update(boostItem);
			} catch (BoostItemNotFound e) {
				model.addAttribute("error", "Error updating boostItem: "+e.getMessage());
			}		
		} else {
			model.addAttribute("error", "BoostItem ID changed from " + boostItemId + " to " + boostItem.getId() + " update cancelled!");
		}
		
		
		model.addAttribute("boostItemForm", new BoostItemForm(boostItem));
		model.addAttribute("boostItem", boostItem);
		
		return "admin/modifyBoostItem";
	}
	
	/**
	 * Create a boostItem
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String createBoostItem(HttpServletRequest request, Model model, @ModelAttribute("boostItemForm") @Valid BoostItemForm boostItemForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			logger.debug("Has errors");
			model.addAttribute("boostItemForm", boostItemForm);
			model.addAttribute("boostItem", new BoostItem(boostItemForm));
            return "admin/addBoostItem";
        }
		logger.debug("Create Form: "+boostItemForm.toString());
		if(!request.isUserInRole(AccountPrivilege.MODIFY.getName())) {
			model.addAttribute("error", "You do not have access to modify data");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		BoostItem boostItem = new BoostItem(0, boostItemForm.getAccount(), 
				boostItemForm.isHardcore(), boostItemForm.isIronborn(), 
				boostItemForm.getBoostItemType(), 
				boostItemForm.getStashSlotId(), boostItemForm.getDungeonId(), boostItemForm.getMessageId(),
				boostItemForm.getLevel());
		logger.debug("BoostItem: "+boostItem.toString());
		boostItem = boostItemService.create(boostItem);
		logger.debug("BoostItem Id: "+boostItem.getId());
		model.addAttribute("boostItemForm", new BoostItemForm(boostItem));
		model.addAttribute("boostItem", boostItem);
		
		return "admin/modifyBoostItem";
	}

	@ModelAttribute("boostItemType")
    public BoostItemType[] getBoostItemType() {
		BoostItemType[] boostItemType = BoostItemType.values();
		BoostItemType[] boostItemTypesNoUnknown = new BoostItemType[boostItemType.length-1];
		for(int i=1; i < boostItemType.length; i++) boostItemTypesNoUnknown[i-1] = boostItemType[i];
		return boostItemTypesNoUnknown;
    }
}
