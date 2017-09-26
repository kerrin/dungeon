package org.kerrin.dungeon.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.InventoryService;
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
@RequestMapping(value="/admin/account/{accountId}/inventory")
public class AdminInventoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminInventoryController.class);
	
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private AccountService accountService;
	
	/**
	 * Look at the details of a specific inventory
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getEquipment(HttpServletRequest request, Model model, @PathVariable long accountId,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Get Inventory");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Account account = accountService.findById(accountId);
		if(account == null) {
			model.addAttribute("error", "Error finding account id: "+accountId);
			return "admin/modifyDungeon";
		}
		Inventory inventory = inventoryService.findByAccount(account, viewTypeForm.isHardcore(), viewTypeForm.isIronborn());
		
		if(inventory == null) {
			inventory = new Inventory(account, viewTypeForm.isHardcore(), viewTypeForm.isIronborn());
			inventory = inventoryService.create(inventory);
			model.addAttribute("error", "Had to create inventory");
		}
		;
		model.addAttribute("inventory", inventory);
		
		return "admin/viewInventory";
	}
}
