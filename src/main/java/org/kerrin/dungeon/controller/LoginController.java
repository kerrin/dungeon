package org.kerrin.dungeon.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.forms.ResetPasswordForm;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.EmailService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.utils.Facebook;
import org.kerrin.dungeon.utils.SecurityUser;
import org.kerrin.dungeon.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/login")
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	private final Facebook facebook;
	private final AccountService accountService;
	private final EmailService emailService;
	private final AchievementService achievementService;
	private final HiscoreService hiscoreService;
	private final String hostUrl;
	
	@Autowired
	public LoginController(Facebook facebook, AccountService accountService, EmailService emailService, 
			AchievementService achievementService, HiscoreService hiscoreService, String hostUrl) {
		super();
		this.facebook = facebook;
		this.accountService = accountService;
		this.emailService = emailService;
		this.achievementService = achievementService;
		this.hiscoreService = hiscoreService;
		this.hostUrl = hostUrl;
	}
	
	/**
	 * Force a login
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String login(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult,
			@RequestParam(value="error", required=false) String error, 
			@RequestParam(value="message", required=false) String message) {
		if(!viewTypeForm.isNoBreakout()) {
			return "breakoutLogin";
		}
		
		model.addAttribute("charId", viewTypeForm.getCharId());
		model.addAttribute("dungeonId", viewTypeForm.getDungeonId());
		model.addAttribute("hardcore", viewTypeForm.isHardcore());
		model.addAttribute("ironborn", viewTypeForm.isIronborn());
		
		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
		if(error != null && !error.isEmpty()) model.addAttribute("error", error);
		if(message != null && !message.isEmpty()) model.addAttribute("message", message);
		
		AccountCreateForm accountCreateForm = new AccountCreateForm();
		model.addAttribute("accountCreateForm", accountCreateForm);
		
		return "index";
	}
	
	@RequestMapping(value="/facebook", method=RequestMethod.GET)
	public String facebookLogin(Locale locale, Model model, HttpServletRequest request, 
			@RequestParam("code") String fbCode,
			@RequestParam(value="charId", required=false, defaultValue="-1") long charId,
			@RequestParam(value="dungeonId", required=false, defaultValue="0") long dungeonId
			)
	{
		logger.debug("Login Facebook Code {}", fbCode);
		model.addAttribute("charId", charId);
		model.addAttribute("dungeonId", dungeonId);
		if (fbCode == null || fbCode.equals("")) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
        }
		String token = null;
        try {
        	StringBuilder returnUrl = new StringBuilder();
    		returnUrl.append(facebook.getHostUrl());
    		returnUrl.append(request.getContextPath());
    		returnUrl.append("/login/facebook?charId=");
			returnUrl.append(charId);
			returnUrl.append("&dungeonId=");
			returnUrl.append(dungeonId);
    		String urlEncodedReturnUrl = URLEncoder.encode(returnUrl.toString(), "UTF-8");
            StringBuilder callbackUrl = new StringBuilder();
            callbackUrl.append("https://graph.facebook.com/oauth/access_token?client_id=");
            callbackUrl.append(facebook.getApiId());
            callbackUrl.append("&redirect_uri=");
            callbackUrl.append(urlEncodedReturnUrl);
            callbackUrl.append("&client_secret=");
            callbackUrl.append(facebook.getApiSecret());
            callbackUrl.append("&code=");
            callbackUrl.append(fbCode);
            URL u = new URL(callbackUrl.toString());
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                b.append(inputLine + "\n");            
            }
            in.close();
            token = b.toString();
            if (token.startsWith("{")) {
            	logger.error("Error on requesting token: " + token + " with code: " + fbCode);
                throw new Exception("Error on requesting token: " + token + " with code: " + fbCode);
            }
        } catch (Exception e) {
        	logger.error("Error while requesting token: " + token + " with code: " + fbCode + ", Exception " + e.getMessage());
        	e.printStackTrace();
        	LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
        }

        String graph = null;
        try {
            String g = "https://graph.facebook.com/me?fields=id,name,email,first_name,last_name&" + token;
            URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                b.append(inputLine + "\n");            
            }
            in.close();
            graph = b.toString();
        } catch (Exception e) {
        	logger.error("Error during requesting data with token: " + token + ", Exception " + e.getMessage());
        	e.printStackTrace();
        	LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
        }

        try {
            JSONObject json = new JSONObject(graph);
            String facebookId = json.getString("id");
            String name = json.getString("name");
            String firstName = json.getString("first_name");
            String lastName = json.getString("last_name");
            String emailAddress = json.getString("email");
            
            Account account = doAutoLogin(facebookId, name, firstName, lastName, emailAddress, request);
            if(account == null) {
            	logger.error("Account login failed: " + token + ", code: " + fbCode + ", id: "+ facebookId);
            	LoginForm loginForm = new LoginForm();
    			model.addAttribute("loginForm", loginForm);
    			AccountCreateForm accountCreateForm = new AccountCreateForm();
    			model.addAttribute("accountCreateForm", accountCreateForm);
    			return "index";
            }
            
            logger.debug("Account login Succeeded. Token: " + token + ", code: " + fbCode + ", id: "+ facebookId);
            
            // Make sure we're processed
    		accountService.processAccount(account);

    		List<Achievement> newAchievements = achievementService.login(request.getContextPath(), account);
    		int newPoints = 0;
    		for(Achievement achievement:newAchievements) {
    			newPoints += achievement.getPoints();
    		}
    		if(newPoints > 0) {
    			hiscoreService.achievement(account, newPoints);
    		}
    		
    		model.addAttribute("hardcore", false);
    		model.addAttribute("ironborn", false);
    		
    		return "play/breakout";
        } catch (JSONException e) {
        	logger.error("Error during Auto Login token: " + token + ", Exception " + e.getMessage());
        	e.printStackTrace();
        	LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
        }
	}
	
	/**
	 * Set up the user principle
	 * 
	 * @param facebookId
	 * @param name
	 * @param firstName
	 * @param lastName
	 * @param emailAddress
	 * @param request
	 * @return
	 */
	private Account doAutoLogin(String facebookId, String name, String firstName, String lastName, String emailAddress,
			HttpServletRequest request) {
	    try {
	    	// Find account or create if new user
            Account account = accountService.findBySocialUserId(facebookId);
            if(account == null) {
            	account = accountService.findByUsername(emailAddress);
            	if(account == null) {
            		// Create a new account, as this is the first time they logged on using facebook
            		String generatedPassword = StringTools.generateRandomPassword();
            		account = new Account(-1, emailAddress, null, name, null, new Date(), null, false, false, 1);
            		account.setPassword(generatedPassword);
            		account.setSocialUserId(facebookId);

            		account = accountService.create(account);
            		emailService.sendNewAccountEmail(request, hostUrl, account, generatedPassword, true);
            		logger.debug("Account created: " + account);
            	} else {
            		if(account.getSocialUserId() == null) {
            			// Set the facebook id on the account, as it's not set yet
            			account.setSocialUserId(facebookId);
            			account.setPreviousLogin(account.getLastLogin());
                		account.setLastLogin(new Date());
            			account = accountService.update(account, true);
            			logger.debug("Account found: " + account);
            		} else {
    	        		logger.debug("Account not found from facebook id, but found from email address with another facebook id");
    	        		return null;
            		}
            	}
            } else {
            	account.setPreviousLogin(account.getLastLogin());
        		account.setLastLogin(new Date());
    			account = accountService.update(account, true);
    			logger.debug("Account found: " + account);
            }
    		
            // Create the user principle            
            SecurityUser user = new SecurityUser(account);
	        Authentication authentication = new RememberMeAuthenticationToken(facebookId, account.getUsername(), user.getAuthorities());
	        logger.debug("Logging in with [{}]", authentication.getPrincipal());
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        return account;
	    } catch (Exception e) {
	        SecurityContextHolder.getContext().setAuthentication(null);
	        logger.error("Failure in autoLogin", e);
        	e.printStackTrace();
        	
	        return null;
	    }
	}
	
	@RequestMapping(value="/403")
	public String failedLogin(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response, 
			@ModelAttribute("loginForm")LoginForm loginForm)
	{
		logger.debug("403ed!");
		model.addAttribute("loginForm", loginForm);
		AccountCreateForm accountCreateForm = new AccountCreateForm();
		model.addAttribute("accountCreateForm", accountCreateForm);
		model.addAttribute("error", "Invalid credentials!");

		return "index";
	}
	
	@RequestMapping(value="/forgotpassword")
	public String forgotPassword(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response, 
			@RequestParam("username") String username)
	{
		logger.debug("Forgot password");
		if(username == null || username.isEmpty()) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			model.addAttribute("error", "Username required to retreive password.");
			return "index";
		}
		
		Account account = accountService.findByUsername(username);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			model.addAttribute("error", "Username unknown.");
			return "index";
		}
		account.generateNewResetPasswordKey();
		try {
			accountService.update(account, false);
		} catch (AccountNotFound e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			model.addAttribute("error", "Unexpected error occured, please try again later.");
			return "index";
		}

		sendForgotPasswordEmail(request, account);
		
		model.addAttribute("message", "We have sent an email to "+username+" with a link to reset your password in.");
		
		return "forgotPassword";
	}

	protected void sendForgotPasswordEmail(HttpServletRequest request, Account account) {
		StringBuilder emailMessage = new StringBuilder();
		emailMessage.append("Dear ");
		emailMessage.append(account.getDisplayName());
		emailMessage.append(",\n\nA request to reset your password was made.\nTo reset your password visit the following link:\n");
		StringBuilder resetPasswordUrl = new StringBuilder();
		resetPasswordUrl.append(hostUrl);
		resetPasswordUrl.append(request.getContextPath());
		resetPasswordUrl.append("/login/resetpassword/");
		resetPasswordUrl.append(account.getResetPasswordKey());
		logger.debug(resetPasswordUrl.toString());
		emailMessage.append(resetPasswordUrl.toString());
		emailMessage.append("\n\nThis link is only valid once and only the most recent link sent will be valid.\n");
		emailMessage.append("If you did not make this request, be aware that someone maybe attempting to hack your account.\n");
		emailMessage.append("No action is required.\n\n");
		emailMessage.append("Thanks,\nThe Dungeon Game");
		emailService.sendPlainTextEmail(account.getUsername(), "Forgotten Password Requested", emailMessage.toString());
	}
	
	@RequestMapping(value="/resetpassword/{key}", method=RequestMethod.GET)
	public String resetPassword(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response, 
			@PathVariable("key") String key)
	{
		logger.debug("Reset password");
		long accountId = Account.getIdFromResetPasswordKey(key);
		Account account = accountService.findById(accountId);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			model.addAttribute("error", "Invalid Reset Password Link.");
			return "index";
		}
		String pendingKey = account.getResetPasswordKey();
		if(pendingKey == null || !key.equals(pendingKey)) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			model.addAttribute("error", "Reset Password Failed. Check that is the most recent reset request. Or send another email.");
			return "index";
		}
		
		ResetPasswordForm resetPasswordForm = new ResetPasswordForm();
		resetPasswordForm.setKey(key);
		model.addAttribute("username", account.getUsername());
		model.addAttribute("displayName", account.getDisplayName());
		model.addAttribute("resetPasswordForm", resetPasswordForm);
		
		return "resetPassword";
	}
	
	@RequestMapping(value="/resetpassword", method=RequestMethod.POST)
	public String resetPasswordDo(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response, 
			@ModelAttribute("resetPasswordForm") ResetPasswordForm resetPasswordForm, BindingResult bindingResult)
	{
		logger.debug("Reset password do");
		long accountId = Account.getIdFromResetPasswordKey(resetPasswordForm.getKey());
		Account account = accountService.findById(accountId);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			model.addAttribute("error", "Invalid Reset Password Link.");
			return "index";
		}
		String pendingKey = account.getResetPasswordKey();
		if(pendingKey == null || !resetPasswordForm.getKey().equals(pendingKey)) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			model.addAttribute("error", "Reset Password Failed. Check that is the most recent reset request. Or send another email.");
			return "index";
		}
		
		if(!resetPasswordForm.getPassword1().equals(resetPasswordForm.getPassword2())) {
			model.addAttribute("username", account.getUsername());
			model.addAttribute("displayName", account.getDisplayName());
			model.addAttribute("resetPasswordForm", resetPasswordForm);
			model.addAttribute("error", "The passwords did not match. Please try again.");
			return "resetPassword";
		}
		
		try {
			account.setPassword(resetPasswordForm.getPassword1());
			account.setResetPasswordKey(null);
			accountService.update(account, false);
		} catch (Exception e) {
			logger.error("Error updating account on reset password");
			e.printStackTrace();
			model.addAttribute("username", account.getUsername());
			model.addAttribute("displayName", account.getDisplayName());
			model.addAttribute("resetPasswordForm", resetPasswordForm);
			model.addAttribute("error", "There was an unexpected error, please try again later.");
			return "resetPassword";
		}
		
		LoginForm loginForm = new LoginForm();
		loginForm.setUsername(account.getUsername());
		model.addAttribute("loginForm", loginForm);
		AccountCreateForm accountCreateForm = new AccountCreateForm();
		model.addAttribute("accountCreateForm", accountCreateForm);
		model.addAttribute("error", "Password Reset Succesfully. That reset password link is now disabled.");
		
		return "index";
	}
	
	@ModelAttribute("facebook")
    public Facebook getFacebook(Principal principle) {		
		return facebook;
	}
}
