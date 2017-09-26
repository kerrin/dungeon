package org.kerrin.dungeon.controller.admin;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.AccountForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.forms.MessageForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountCurrency;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.AccountRole;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
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
@RequestMapping(value="/admin/account")
public class AdminAccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminAccountController.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountMessageService accountMessageService;

	@Autowired
	private AccountCurrencyService accountCurrencyService;
	
	/**
	 * Look at the details of a specific account
	 */
	@RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
	public String getAccount(HttpServletRequest request, Model model, @PathVariable long accountId) {
		logger.debug("Get Account Details");
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
			AccountForm accountForm = new AccountForm();
			model.addAttribute("accountForm", accountForm);
			account = new Account(accountForm);
			model.addAttribute("account", account);
			setupCurrencies(model, account);
			
			return "admin/modifyAccount";
		}
		
		model.addAttribute("accountForm", new AccountForm(account));
		model.addAttribute("account", account);
		setupCurrencies(model, account);
		
		return "admin/modifyAccount";
	}
	
	/**
	 * Update an account details
	 */
	@RequestMapping(value = "/{accountId}", method = RequestMethod.POST)
	public String updateAccount(HttpServletRequest request, Model model, @PathVariable long accountId, 
			@ModelAttribute("accountForm") @Valid AccountForm accountForm, BindingResult bindingResult) {		
		logger.debug("Update Form: "+accountForm.toString());
		if(!request.isUserInRole(AccountPrivilege.MODIFY.getName())) {
			model.addAttribute("error", "You do not have access to modify data");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("accountForm", accountForm);
			Account account = new Account(accountForm);
			model.addAttribute("account", account);
			setupCurrencies(model, account);
            return "admin/modifyAccount";
        }
		Set<AccountRole> roles = new HashSet<AccountRole>();
		Account account = accountService.findById(accountForm.getId());
		account.setUsername(accountForm.getUsername());
		account.setDisplayName(accountForm.getDisplayName());
		account.setOnHoliday(accountForm.isOnHoliday());
		account.setDebugMode(accountForm.isDebugMode());
		
		// Set the roles
		if(accountForm.isPrivView()) roles.add(new AccountRole(-1, account, AccountPrivilege.VIEW));
		if(accountForm.isPrivModify()) roles.add(new AccountRole(-1, account, AccountPrivilege.MODIFY));
		if(accountForm.isPrivDelete()) roles.add(new AccountRole(-1, account, AccountPrivilege.DELETE));
		if(!roles.isEmpty()) account.setRoles(roles);
		
		try {
			if(accountForm.getPassword() != null && accountForm.getPassword().length() >= 6) {
				account.setPassword(accountForm.getPassword());
			}
		} catch (Exception e) {
			bindingResult.reject("id", "AccountSearchForm.passwordError");
			model.addAttribute("error", "Password update error!");
			model.addAttribute("accountForm", accountForm);
			model.addAttribute("account", account);
			setupCurrencies(model, account);
			return "admin/modifyAccount";
		}
		
		if(accountForm.getModifyTokens() > 0) {
			account.increaseLoginTokens(accountForm.getModifyTokens());
		} else if(accountForm.getModifyTokens() < 0) {
			account.decreaseLoginTokens(Math.abs(accountForm.getModifyTokens()));
		}
		
		AccountCurrency[] accountCurrencies = accountCurrencyService.findAllByAccount(account).toArray(new AccountCurrency[0]);
		if(account.getId() == accountId) {
			try {
				accountService.update(account, false);				
				for(AccountCurrency accountCurrency:accountCurrencies) {
					if(accountCurrency == null) {
						logger.error("Couldn't find account currency for account id "+accountId);
						/*
						accountCurrency = new AccountCurrency(account.getId(), hardcore, ironborn, 0);
						if(accountCurrency.modifyCurrency(accountForm.getModifyCurrency(accountCurrency.isHardcore(), accountCurrency.isIronborn()))) {
							accountCurrencyService.create(accountCurrency);
						}
						*/
					} else {
						if(accountForm.getModifyCurrency(accountCurrency.isHardcore(), accountCurrency.isIronborn()) != 0) {
							if(accountCurrency.modifyCurrency(accountForm.getModifyCurrency(accountCurrency.isHardcore(), accountCurrency.isIronborn()))) {
								String reference = "admin-"+request.getUserPrincipal().getName()+"-"+System.currentTimeMillis();
								accountCurrencyService.update(accountCurrency, ModificationType.ADMIN, reference);
							}
						}
					}
				}
			} catch (AccountNotFound e) {
				model.addAttribute("error", "Error updating account: "+e.getMessage());
			}		
		} else {
			model.addAttribute("error", "Account ID changed from " + accountId + " to " + account.getId() + " update cancelled!");
		}
		
		
		model.addAttribute("accountForm", new AccountForm(account));
		model.addAttribute("account", account);
		setupCurrencies(model, account);
		
		return "admin/modifyAccount";
	}
	
	/**
	 * Create an account
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String createAccount(HttpServletRequest request, Model model, 
			@ModelAttribute("accountForm") @Valid AccountForm accountForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			logger.debug("Has errors");
			model.addAttribute("accountForm", accountForm);
			Account account = new Account(accountForm);
			model.addAttribute("account", account);
			setupCurrencies(model, account);
            return "admin/addAccount";
        }
		logger.debug("Create Form: "+accountForm.toString());
		if(!request.isUserInRole(AccountPrivilege.MODIFY.getName())) {
			model.addAttribute("error", "You do not have access to modify data");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Account account = new Account(0, accountForm.getUsername(), null,
				accountForm.getDisplayName(), null/*Previous Login*/, null/*Last Login*/,
				null /*Roles*/, false /*Touch Screen*/, false /*Debug Mode*/, 1 /*level of account*/);
		try {
			if(accountForm.getPassword() != null && accountForm.getPassword().length() >= 6) {
				account.setPassword(accountForm.getPassword());
			} else {
				throw new Exception("Password Required to add");
			}
		} catch (Exception e) {
			bindingResult.reject("id", "AccountSearchForm.passwordError");
			model.addAttribute("error", "Password update error!");
			model.addAttribute("accountForm", accountForm);
			account = new Account(accountForm);
			model.addAttribute("account", new Account(accountForm));
			setupCurrencies(model, account);
			return "admin/modifyAccount";
		}
		logger.debug("Account: "+account.toString());
		account = accountService.create(account);
		logger.debug("Account Id: "+account.getId());
		setupCurrencies(model, account);
		model.addAttribute("accountForm", new AccountForm(account));
		model.addAttribute("account", account);
		
		return "admin/modifyAccount";
	}
	
	/**
	 * Send an account a message
	 */
	@RequestMapping(value="/{accountId}/message", method = RequestMethod.POST)
	public String sendAccountMessage(HttpServletRequest request, Model model, 
			@ModelAttribute("messageForm") @Valid MessageForm messageForm, BindingResult bindingResult, 
			@PathVariable long accountId) {
		if (bindingResult.hasErrors()) {
			logger.debug("Has errors");
			model.addAttribute("error", "Form has errors");
			return getAccount(request, model, accountId);
        }
		logger.debug("Send Message Form: "+messageForm.toString());
		if(!request.isUserInRole(AccountPrivilege.MODIFY.getName())) {
			model.addAttribute("error", "You do not have access to modify data (which is required to send messages)");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		
		Account account = accountService.findById(accountId);
		
		if(account == null) {
			AccountForm accountForm = new AccountForm();
			model.addAttribute("accountForm", accountForm);
			account = new Account(accountForm);
			model.addAttribute("account", account);
			setupCurrencies(model, account);
			
			return "admin/modifyAccount";
		}
		
		String message = accountMessageService.replaceTokens(messageForm.getMessage(), account);
		AccountMessage accountMessage = accountMessageService.create(accountId, message, null, null);
		
		logger.debug("Account: "+account.toString());
		setupCurrencies(model, account);
		model.addAttribute("accountForm", new AccountForm(account));
		model.addAttribute("account", account);
		
		model.addAttribute("message", "Message sent, message id " + accountMessage.getId());
		
		return "admin/modifyAccount";
	}
	
	protected void setupCurrencies(Model model, Account account) {
		AccountCurrency accountCurrencyStandard = accountCurrencyService.findByAccount(account, false, false);
		model.addAttribute("accountCurrencyStandard", accountCurrencyStandard);
		AccountCurrency accountCurrencyHardcore = accountCurrencyService.findByAccount(account, true, false);
		model.addAttribute("accountCurrencyHardcore", accountCurrencyHardcore);
		AccountCurrency accountCurrencyIronborn = accountCurrencyService.findByAccount(account, false, true);
		model.addAttribute("accountCurrencyIronborn", accountCurrencyIronborn);
		AccountCurrency accountCurrencyExtreme = accountCurrencyService.findByAccount(account, true, true);
		model.addAttribute("accountCurrencyExtreme", accountCurrencyExtreme);
	}
}
