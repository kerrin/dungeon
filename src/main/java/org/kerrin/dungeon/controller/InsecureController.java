package org.kerrin.dungeon.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.HiscoresForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountConfig;
import org.kerrin.dungeon.model.AccountCurrency;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.Hiscore;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.EmailService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.utils.Facebook;
import org.kerrin.dungeon.utils.StringTools;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class InsecureController extends SuperPublic {
	
	private static final Logger logger = LoggerFactory.getLogger(InsecureController.class);
	
	private final AccountService accountService;
	private final AchievementService achievementService;
	private final HiscoreService hiscoreService;
	private final Facebook facebook;
	private final EmailService emailService;
	private final String hostUrl;
	protected boolean hardcore;
	protected boolean ironborn;
	
	@Autowired
	public InsecureController(AccountService accountService, 
			AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService,
			AchievementService achievementService, 
			Facebook facebook, HiscoreService hiscoreService, EmailService emailService, String hostUrl) {
		super(accountConfigService, accountCurrencyService);
		this.accountService = accountService;
		this.achievementService = achievementService;
		this.facebook = facebook;
		this.hiscoreService = hiscoreService;
		this.emailService = emailService;
		this.hostUrl = hostUrl;
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult,
			@RequestParam(value="error", required=false) String error, 
			@RequestParam(value="message", required=false) String message) {
		if(!viewTypeForm.isNoBreakout()) {
			return "breakoutLogin";
		}
		
		Account account = accountService.findByPrinciple(principle);
		
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		model.addAttribute("hardcore", viewTypeForm.isHardcore());
		model.addAttribute("ironborn", viewTypeForm.isIronborn());
		
		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
		if(error != null && !error.isEmpty()) model.addAttribute("error", error);
		if(message != null && !message.isEmpty()) model.addAttribute("message", message);
		
		if(account == null) {
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			
			return "index";
		}

		setUpPlayIndex(model, account, viewTypeForm.isHardcore(), viewTypeForm.isIronborn());
		
		return "play/index";
	}
	
	@RequestMapping(value = "/newPlayers", method = RequestMethod.GET)
	public String newPlayers(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, 
			BindingResult bindingResult) {
		Account account = accountService.findByPrinciple(principle);
		model.addAttribute("hardcore", viewTypeForm.isHardcore());
		model.addAttribute("ironborn", viewTypeForm.isIronborn());
		setUpPlayIndex(model, account, viewTypeForm.isHardcore(), viewTypeForm.isIronborn());

		return "newPlayers";
	}

	@RequestMapping(value = "/createAccount", method = RequestMethod.POST)
	public String createNewAccount(HttpServletRequest request, Locale locale, Model model,
			@ModelAttribute("accountCreateForm") @Valid AccountCreateForm accountCreateForm, 
			BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			model.addAttribute("error", "Please enter all the fields as outlined.");

			return "index";
		}
		Account account = accountService.findByUsername(accountCreateForm.getUsername());
		if(account != null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			model.addAttribute("error", "Email address already registered.");
			return "index";
		}
		account = validateAccountForm(accountCreateForm);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			model.addAttribute("error", "Details invalid, your email address is probably incorrect.");
			return "index";
		}
		account = accountService.create(account);
		accountService.processAccount(account);
		emailService.sendNewAccountEmail(request, hostUrl, account, accountCreateForm.getPassword(), false);
		model.addAttribute("error", "Please login");
		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
		return "index";
	}

	/**
	 * Validate the account form and return a new account object
	 * 
	 * @param accountCreateForm
	 * @return
	 */
	private Account validateAccountForm(AccountCreateForm accountCreateForm) {
		if(!StringTools.isValidEmail(accountCreateForm.getUsername())) return null;
		accountCreateForm.setDisplayName(StringTools.dbTidy(accountCreateForm.getDisplayName()));
		return new Account(accountCreateForm);
	}

	@RequestMapping(value = "/profile/{accountId}", method = RequestMethod.GET)
	public String createNewAccount(Locale locale, Model model, Principal principle, @PathVariable long accountId,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		Account account = accountService.findByPrinciple(principle);
		if(account != null) {
			// Re-create the attribute, incase it got updated
			model.addAttribute("account", account);
			
			setupCurrencies(model, account);
		}
		Account viewAccount = accountService.findById(accountId);
		if(viewAccount == null) {
			model.addAttribute("error", "User not found.");
			if(account == null) {
				LoginForm loginForm = new LoginForm();
				model.addAttribute("loginForm", loginForm);
				AccountCreateForm accountCreateForm = new AccountCreateForm();
				model.addAttribute("accountCreateForm", accountCreateForm);
				
				return "index";
			}
		}
		model.addAttribute("hiscores", hiscoreService.findAllByAccount(viewAccount).toArray(new Hiscore[0]));
		model.addAttribute("hardcore", viewTypeForm.isHardcore());
		model.addAttribute("ironborn", viewTypeForm.isIronborn());
		model.addAttribute("viewAccount", viewAccount);
		setUpPlayIndex(model, account, viewTypeForm.isHardcore(), viewTypeForm.isIronborn());
		return "profile";
	}

	@RequestMapping(value = "/message/{message}", method = RequestMethod.GET)
	public String showMessage(Locale locale, Model model, Principal principle, @PathVariable String message) {
		List<AccountMessage> messages = new ArrayList<AccountMessage>();
		messages.add(new AccountMessage(-1, message, null, null, null));
		model.addAttribute("messages", messages);
		return "message";
	}
	
	@RequestMapping(value = "/logout", method=RequestMethod.GET)
	public String logout(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("Logout");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
		AccountCreateForm accountCreateForm = new AccountCreateForm();
		model.addAttribute("accountCreateForm", accountCreateForm);
		
		return "index";
	}

	// Helppers

	@ModelAttribute("facebook")
    public Facebook getFacebook(Principal principle) {		
		return facebook;
	}
	
	/**
	 * Set the account and account currency model attributes, if possible
	 * 
	 * @param model
	 * @param account
	 */
	private void setUpPlayIndex(Model model, Account account, boolean hardcore, boolean ironborn) {
		model.addAttribute("charId", 0);
		model.addAttribute("dungeonId", 0);
		if(account == null) return;
		model.addAttribute("account", account);
		AccountConfig accountConfigEquipmentCompare = accountConfigService.findByAccount(account, hardcore, ironborn, AccountConfigType.EQUIPMENT_COMPARE);
		if(accountConfigEquipmentCompare != null) {
			model.addAttribute("accountConfigEquipmentCompare", accountConfigEquipmentCompare);
		}
		AccountConfig accountConfigToolTips = accountConfigService.findByAccount(account, hardcore, ironborn, AccountConfigType.TOOL_TIPS);
		if(accountConfigToolTips != null) {
			model.addAttribute("accountConfigToolTips", accountConfigToolTips);
		}
		AccountCurrency accountCurrency = accountCurrencyService.findByAccount(account, hardcore, ironborn);
		if(accountCurrency != null) {
			model.addAttribute("accountCurrency", accountCurrency);
		}
	}

	@RequestMapping(value="achievements/{compareAccountId}", method = RequestMethod.GET)
	public String getAccountAchievementsCompare(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult, 
			@PathVariable long compareAccountId) {
		logger.debug("Compare Account Achievements");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account != null) {
			// Re-create the attribute, incase it got updated
			model.addAttribute("account", account);
			
			model.addAttribute("charId", viewTypeForm.getCharId());
			model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
			setupCurrencies(model, account);

			model.addAttribute("myAchievementsGeneral", achievementService.getAchievements(account, null, null));
			model.addAttribute("myAchievementsThisMode", achievementService.getAchievements(account, hardcore, ironborn));
			model.addAttribute("myAchievementPointsGeneral", achievementService.getAchievementPoints(account, null, null));
			model.addAttribute("myAchievementPointsThisMode", achievementService.getAchievementPoints(account, hardcore, ironborn));
			model.addAttribute("myAchievementPointsAll", achievementService.getAchievementPoints(account));			
		}
		

		Account compareAccount = accountService.findById(compareAccountId);
		if(compareAccount == null) {
			return null;
		}
		
		model.addAttribute("compareAccount", compareAccount);
		model.addAttribute("achievementsGeneral", achievementService.getAchievements(compareAccount, null, null));
		model.addAttribute("achievementsThisMode", achievementService.getAchievements(compareAccount, hardcore, ironborn));
		model.addAttribute("achievementPointsGeneral", achievementService.getAchievementPoints(compareAccount, null, null));
		model.addAttribute("achievementPointsThisMode", achievementService.getAchievementPoints(compareAccount, hardcore, ironborn));
		model.addAttribute("achievementPointsAll", achievementService.getAchievementPoints(compareAccount));
		
		return "accountAchievements";
	}

	@RequestMapping(value="/hiscores", method = RequestMethod.GET)
	public String getHiscores(HttpServletRequest request, Locale locale, Model model, Principal principle,
			@ModelAttribute("hiscoresForm") @Valid HiscoresForm hiscoresForm, BindingResult bindingResult) {
		logger.debug("View Hiscores");
		Account account = accountService.findByPrinciple(principle);
		if(account != null) {
			// Re-create the attribute, incase it got updated
			model.addAttribute("account", account);
			
			setupCurrencies(model, account);
		}
		int offset = hiscoresForm.getOffset() - (hiscoresForm.getOffset() % hiscoresForm.getPageSize());
		if(offset < 0) offset = 0;
		hiscoresForm.setOffset(offset);
		List<Hiscore> hiscores = hiscoreService.findAllByTypeAndHardcoreAndIronborn(
				hiscoresForm.getType(), hiscoresForm.isViewHardcore(), hiscoresForm.isViewIronborn(), 
				hiscoresForm.getSinceDate(),
				offset,
				hiscoresForm.getPageSize()+1);
		
		if(hiscores.size() > hiscoresForm.getPageSize()) {
			model.addAttribute("hasMore", true);
			hiscores.remove(hiscoresForm.getPageSize());
		}
		setUpViewTypeModel(model);
		model.addAttribute("hiscoresForm", hiscoresForm);
		model.addAttribute("hiscores", hiscores.toArray(new Hiscore[0]));
		
		return "hiscores";
	}
}
