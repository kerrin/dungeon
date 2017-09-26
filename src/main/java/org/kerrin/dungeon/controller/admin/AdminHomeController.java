package org.kerrin.dungeon.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.forms.MessageForm;
import org.kerrin.dungeon.service.AccountMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value="/admin")
public class AdminHomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminHomeController.class);
	
	@Autowired
	private AccountMessageService accountMessageService;
	
	/**
	 * List the characters. Allow searches for characters
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(HttpServletRequest request, Model model) {
		logger.trace("Admin Home");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		return "admin/home";
	}
	
	/**
	 * Send an account a message
	 */
	@RequestMapping(value="/messageAll", method = RequestMethod.GET)
	public String sendAllMessageForm(HttpServletRequest request, Model model) {
		return "admin/messageAll";
	}
	
	/**
	 * Send an account a message
	 */
	@RequestMapping(value="/messageAll", method = RequestMethod.POST)
	public String sendAccountMessage(HttpServletRequest request, Model model, 
			@ModelAttribute("messageForm") @Valid MessageForm messageForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			logger.debug("Has errors");
			model.addAttribute("error", "Form errors");
			return "admin/messageAll";
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
		
		accountMessageService.createAll(messageForm.getMessage(), null, null);
		
		model.addAttribute("message", "Message sent to all account");
		
		return "admin/messageAll";
	}
	
	@RequestMapping(value = "/snoop", method = RequestMethod.GET)
	public String snoop(Locale locale, Model model) {
		logger.info("Welcome snoop! The client locale is {}.", locale);
		
		return "admin/snoop";
	}
}
