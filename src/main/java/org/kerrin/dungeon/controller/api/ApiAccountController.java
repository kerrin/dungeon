package org.kerrin.dungeon.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.forms.AccountForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.service.AccountService;
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
@RequestMapping(value="/api/account")
public class ApiAccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiAccountController.class);
		
	private final AccountService accountService;
	
	@Autowired
	public ApiAccountController(AccountService accountService) {
		super();
		this.accountService = accountService;
	}

	/**
	 * Look at the details of a specific account
	 */
	@RequestMapping(value = "/{accountApiKey}", method = RequestMethod.GET)
	public Account getAccount(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey) {
		logger.debug("API: Get Account Details");
		Account account = accountService.findByApiKey(accountApiKey);
		
		if(account == null) {
			return null;
		}
		
		return account;
	}
	
	/**
	 * Update a account details
	 */
	@RequestMapping(value = "/{accountApiKey}", method = RequestMethod.POST)
	public Account updateAccount(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey, 
			@ModelAttribute("accountForm") @Valid AccountForm accountForm, BindingResult bindingResult) {		
		logger.debug("Update Account API: "+accountForm.toString());
		Account account = accountService.findByApiKey(accountApiKey);
		
		if(account == null || account.getId() != accountForm.getId()) {
			return null;
		}
		if (bindingResult.hasErrors()) {
			return null;
        }
		if(accountForm.getDisplayName() != null && !accountForm.getDisplayName().isEmpty()) {
			account.setDisplayName(accountForm.getDisplayName());
		}
		
		try {
			if(accountForm.getPassword() != null && accountForm.getPassword().length() >= 6) {
				account.setPassword(accountForm.getPassword());
			}
		} catch (Exception e) {
			bindingResult.reject("id", "AccountSearchForm.passwordError");
			model.addAttribute("error", "Password update error!");
			return null;
		}
		
		try {
			accountService.update(account, false);
		} catch (AccountNotFound e) {
			logger.debug("Account Update for "+account.getId()+" failed with exception "+e.getMessage());
			model.addAttribute("error", "Update Failed");
		}
		
		return account;
	}
	
	@RequestMapping(value = "/{accountApiKey}/process", method = RequestMethod.GET)
	public Account processAccount(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey 
			) {	
		logger.debug("Process Account API");
		Account account = accountService.findByApiKey(accountApiKey);
		
		if(account == null) {
			return null;
		}
		
		accountService.processAccount(account);
		
		return account;
	}
}
