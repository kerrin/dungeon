package org.kerrin.dungeon.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.AccountForm;
import org.kerrin.dungeon.forms.AccountSearchForm;
import org.kerrin.dungeon.forms.CharacterForm;
import org.kerrin.dungeon.forms.CharacterSearchForm;
import org.kerrin.dungeon.forms.DungeonForm;
import org.kerrin.dungeon.forms.DungeonSearchForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.DungeonService;
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
public class AdminAccountSearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminAccountSearchController.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CharacterService charService;
	
	@Autowired
	private DungeonService dungeonService;
	
	/**
	 * List the Accounts. Allow searches for Accounts
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(HttpServletRequest request, Model model, 
			@ModelAttribute("accountSearchForm") @Valid AccountSearchForm searchForm, BindingResult bindingResult) {
		logger.trace("Account Search: Get");
		boolean restricted = false;
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		List<Account> accounts = new ArrayList<Account>();
		if (bindingResult.hasErrors()) {
			model.addAttribute("accounts", accounts);
			model.addAttribute("accountForm", new AccountForm());
            return "admin/searchAccounts";
        }
		if(searchForm.getId() > 0) {
			restricted = true;
			Account account = accountService.findById(searchForm.getId());
			if(account != null) {
				accounts.add(account);
				bindingResult.reject("id", "SearchForm.NotFound.accountId");
			}
		}
		if(searchForm.getDisplayName() != null && !searchForm.getDisplayName().isEmpty()) {
			List<Account> foundAccounts = accountService.findAllByDisplayName(searchForm.getDisplayName());
			if(!restricted && accounts.isEmpty()) {
				accounts = foundAccounts;
			} else {
				accounts.retainAll(foundAccounts);
			}
			restricted = true;
		}
		if(searchForm.getUsername() != null && !searchForm.getUsername().isEmpty()) {
			Account foundAccount = accountService.findByUsername(searchForm.getUsername());
			if(foundAccount == null) {
				accounts = new ArrayList<Account>();
				bindingResult.reject("id", "SearchForm.NotFound.username");
				model.addAttribute("error", "Username not found!");
			} else {
				if(!restricted && accounts.isEmpty()) {
					accounts.add(foundAccount);
				} else {
					if(accounts.contains(foundAccount)) {
						// Only this account should return
						accounts = new ArrayList<Account>();
						accounts.add(foundAccount);
					} else {
						accounts = new ArrayList<Account>();
					}
				}
			}
			restricted = true;
		}
		if(!restricted) {
			List<Account> foundAccounts = accountService.findLastLogins();
			if(foundAccounts == null) {
				accounts = new ArrayList<Account>();
				bindingResult.reject("id", "SearchForm.NotFound.username");
				model.addAttribute("error", "Username not found!");
			} else {
				accounts.addAll(foundAccounts);
			}
		}
		// Paging
		
		model.addAttribute("accounts", accounts);
		model.addAttribute("accountForm", new AccountForm());
		
		return "admin/searchAccounts";
	}
	
	@RequestMapping(value = "/{accountId}/process", method = RequestMethod.GET)
	public String processAccount(HttpServletRequest request, Model model, @PathVariable long accountId) {
		logger.debug("Process Account");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Account account = accountService.findById(accountId);
		List<Account> accounts = new ArrayList<Account>();
		
		if(account == null) {
			model.addAttribute("accounts", accounts);
			model.addAttribute("accountForm", new AccountForm());
			model.addAttribute("accountSearchForm", new AccountSearchForm(-1,null,null));
			
			return "admin/searchAccounts";
		}
		
		// Process the account
		accountService.processAccount(account);
		
		accounts.add(account);
		
		model.addAttribute("accounts", accounts);
		model.addAttribute("accountForm", new AccountForm());
		model.addAttribute("accountSearchForm", new AccountSearchForm(account.getId(),null,null));
		
		return "admin/searchAccounts";
	}
	
	@RequestMapping(value = "/{accountId}/characters", method = RequestMethod.GET)
	public String searchCharactersForAccount(HttpServletRequest request, Model model, @PathVariable long accountId) {
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		List<Character> characters = new ArrayList<Character>();
		if(accountId > 0) {	
			Account account = accountService.findById(accountId);
			if(account != null) {
				List<Character> foundCharacters = charService.findAllByAccountOrderByLevel(account);
				if(characters.isEmpty()) {
					characters = foundCharacters;
				} else {
					characters.retainAll(foundCharacters);
				}
			}
		}
		// TODO: Paging

		model.addAttribute("characters", characters);
		CharacterSearchForm searchForm = new CharacterSearchForm();
		searchForm.setAccountId(accountId);
		model.addAttribute("characterSearchForm", searchForm  );
		model.addAttribute("characterForm", new CharacterForm());
		
		return "admin/searchCharacters";
	}
	
	@RequestMapping(value = "/{accountId}/dungeons", method = RequestMethod.GET)
	public String searchDungeonsForAccount(HttpServletRequest request, Model model, @PathVariable long accountId) {
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		List<Dungeon> dungeons = new ArrayList<Dungeon>();
		Account account = accountService.findById(accountId);
		if(account != null) {			
			List<Dungeon> foundDungeons = dungeonService.findAllByAccount(account);
			if(dungeons.isEmpty()) {
				dungeons = foundDungeons;
			} else {
				dungeons.retainAll(foundDungeons);
			}
		}
		// Paging

		model.addAttribute("dungeons", dungeons);
		DungeonSearchForm searchForm = new DungeonSearchForm();
		searchForm.setAccountId(accountId);
		model.addAttribute("dungeonSearchForm", searchForm  );
		model.addAttribute("dungeonForm", new DungeonForm());
		
		return "admin/searchDungeons";
	}
}
