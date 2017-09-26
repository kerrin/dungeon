package org.kerrin.dungeon.controller.secure;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.enums.AccountTask;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.forms.AccountPublicForm;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountConfig;
import org.kerrin.dungeon.model.Hiscore;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.utils.Facebook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/play/account")
public class SecureAccountController extends SuperSecurePublic {
	private static final Logger logger = LoggerFactory.getLogger(SecureAccountController.class);
	
	private final HiscoreService hiscoreService;
	private final BoostItemService boostItemService;
	private final AchievementService achievementService;
		
	@Autowired
	public SecureAccountController(AccountService accountService, 
			AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService,
			Facebook facebook, AchievementService achievementService, AccountMessageService accountMessageService,
			HiscoreService hiscoreService, BoostItemService boostItemService) {
		super(accountService, accountConfigService, accountCurrencyService, accountMessageService, facebook);
		this.achievementService = achievementService;
		this.hiscoreService = hiscoreService;
		this.boostItemService = boostItemService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getAccount(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("View Account");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		// Re-create the attribute, incase it got updated
		model.addAttribute("account", account);
		
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		setupCurrencies(model, account);
		
		model.addAttribute("hiscores", hiscoreService.findAllByAccount(account).toArray(new Hiscore[0]));
		
		return "play/account";
	}

	@RequestMapping(value="history", method = RequestMethod.GET)
	public String getAccountHistory(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("View Account History");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		// Re-create the attribute, incase it got updated
		model.addAttribute("account", account);
		
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		setupCurrencies(model, account);
		
		model.addAttribute("history", accountService.getHistory(account, locale));
		
		return "play/accountHistory";
	}

	@RequestMapping(value="boostitem", method = RequestMethod.GET)
	public String getAccountBoostItem(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("View Account Boost Items");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		// Re-create the attribute, incase it got updated
		model.addAttribute("account", account);
		
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		setupCurrencies(model, account);
		
		model.addAttribute("boostItems", boostItemService.getBoostItems(account, hardcore, ironborn));
		
		return "play/accountBoostItems";
	}

	@RequestMapping(value="achievements", method = RequestMethod.GET)
	public String getAccountAchievements(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("View Account Achievements");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		// Re-create the attribute, incase it got updated
		model.addAttribute("account", account);
		
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		setupCurrencies(model, account);
		model.addAttribute("achievementsGeneral", achievementService.getAchievements(account, null, null));
		model.addAttribute("achievementsThisMode", achievementService.getAchievements(account, hardcore, ironborn));
		model.addAttribute("achievementPointsGeneral", achievementService.getAchievementPoints(account, null, null));
		model.addAttribute("achievementPointsThisMode", achievementService.getAchievementPoints(account, hardcore, ironborn));
		model.addAttribute("achievementPointsAll", achievementService.getAchievementPoints(account));
		
		return "accountAchievements";
	}

	@RequestMapping(value="modify", method = RequestMethod.GET)
	public String getAccountModify(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("View Modify Account");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		// Re-create the attribute, incase it got updated
		model.addAttribute("account", account);
		
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		
		model.addAttribute("accountPublicForm", new AccountPublicForm(account));
		
		return "play/accountChange";
	}

	@RequestMapping(value="modify", method = RequestMethod.POST)
	public String getAccountModify(HttpServletRequest request, Locale locale, Model model, Principal principle,
			HttpServletResponse response,
			@ModelAttribute("accountPublicForm") @Valid AccountPublicForm accountPublicForm, BindingResult bindingResult) {
		logger.debug("View Modify Account");
		setUpViewType(accountPublicForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		// Re-create the attribute, incase it got updated
		model.addAttribute("account", account);

		model.addAttribute("charId", accountPublicForm.getCharId());
		model.addAttribute("dungeonId", accountPublicForm.getDungeonId());
		
		String modified = "";
		boolean logout = false;
		
		String username = accountPublicForm.getUsername();
		if(username != null && !username.isEmpty() && !username.equals(account.getUsername())) {
			account.setUsername(username);
			modified = "Username/Email";
			logout = true;
		}

		String displayName = accountPublicForm.getDisplayName();
		if(displayName != null && !displayName.isEmpty() && !displayName.equals(account.getDisplayName())) {
			account.setDisplayName(displayName);
			if(modified.isEmpty()) {
				modified = "Display Name";
			} else {
				modified += " and Display Name";
			}
		}
		
		// Check the current password before we try to change it
		String currentPassword = accountPublicForm.getCurrentPassword();
		boolean currentPasswordCheckedAndCorrect = 
				(currentPassword != null && !currentPassword.isEmpty() && account.passwordMatch(currentPassword));
		if(currentPasswordCheckedAndCorrect) {
			String newPassword1 = accountPublicForm.getNewPassword1();
			String newPassword2 = accountPublicForm.getNewPassword2();
			if((newPassword1 != null && !newPassword1.isEmpty()) || 
					(newPassword1 != null && !newPassword1.isEmpty())) {
				if(newPassword1.equals(newPassword2)) {
					try {
						account.setPassword(newPassword1);
						if(modified.isEmpty()) {
							modified = "Password";
						} else {
							modified += " and Password";
						}
					} catch (Exception e) {
						logger.error("Error updating account password: " + account.toString());
						logger.error(e.getMessage());
						logger.error(e.getStackTrace().toString());
						model.addAttribute("error", "Sorry, but the password update failed, please try again later.");
					}
				} else {
					model.addAttribute("error", "New Passwords did not match, please try again.");
				}
			}
			if(!modified.isEmpty()) {
				try {
					accountService.update(account, false);
					
					if(logout) {
						model.addAttribute("message", "Updated "+modified+" succesfully. "
								+ "You need to relogin, as your username was updated.");
						// The username was changed, so the user needs to relogin
						Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					    if (auth != null){    
					        new SecurityContextLogoutHandler().logout(request, response, auth);
					    }
					    
						return "breakoutLogin";
					}
					accountMessageService.create(account.getId(), "Updated "+modified+" succesfully.", hardcore, ironborn);
				} catch (AccountNotFound e) {
					model.addAttribute("error", "Sorry, but the update failed. Please try again later.");
					logger.error("Error modifying account: " + account.toString());
					logger.error(e.getMessage());
					logger.error(e.getStackTrace().toString());
				}
			} else {
				model.addAttribute("error", "No changes to be made");			
			}
		} else {
			model.addAttribute("error", "You must enter the current password to make modifications");
		}
		
		model.addAttribute("accountPublicForm", new AccountPublicForm(account));
		
		return "play/accountChange";
	}
	
	@RequestMapping(value="addTokens", method = RequestMethod.GET)
	public String addToken(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Add Tokens");
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}

		setUpViewType(viewTypeForm, model);
		
		model.addAttribute("error", "Purchasing dungeon tokens not currently supported.");
		// TODO: PayPal integration
		
		int purchased = 0;
		hiscoreService.tokensPurchased(account, hardcore, ironborn, purchased);
		
		return "play/addTokens";
	}
	
	@RequestMapping(value="onHoliday", method = RequestMethod.GET)
	public String holiday(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("On Holiday");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		account.setOnHoliday(true);
		try {
			account = accountService.update(account, false);
		} catch (AccountNotFound e) {
			logger.error("Error updating account {}",account);
			e.printStackTrace();
			model.addAttribute("error", "Sorry, an error occured, please try again later");
		}
		
		return getAccount(request, locale, model, principle, viewTypeForm, bindingResult);
	}
	
	@RequestMapping(value="offHoliday", method = RequestMethod.GET)
	public String cancelHoliday(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Off Holiday");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		account.setOnHoliday(false);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date tommorow = cal.getTime();
		account.setLastProcessed(AccountTask.DAILY_CHECKS, tommorow);
		try {
			account = accountService.update(account, false);
		} catch (AccountNotFound e) {
			logger.error("Error updating account {}",account);
			e.printStackTrace();
			model.addAttribute("error", "Sorry, an error occured, please try again later");
		}
		
		return getAccount(request, locale, model, principle, viewTypeForm, bindingResult);
	}
}
