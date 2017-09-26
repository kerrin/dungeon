package org.kerrin.dungeon.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.BooleanOptions;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.AccountCurrencyAuditSearchForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.AccountCurrencyAudit;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/admin/account/currencyaudit")
public class AdminAccountCurrencyAuditSearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminAccountCurrencyAuditSearchController.class);
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
	     binder.registerCustomEditor(Date.class,     
	                         new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));   
	}
	
	@Autowired
	private AccountCurrencyService accountCurrencyService;
	
	
	@RequestMapping(value="/{accountId}", method = RequestMethod.GET)
	public String getAccount(HttpServletRequest request, Model model, @PathVariable Long accountId,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		List<AccountCurrencyAudit> audits = new ArrayList<AccountCurrencyAudit>();
		if (accountId == null) {
			model.addAttribute("error", "Account Id null error");			
			model.addAttribute("accountCurrencyAuditSearchForm", new AccountCurrencyAuditSearchForm());
			model.addAttribute("audits", audits);
			return "admin/searchAccountCurrencyAudits";
        }
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date startDate = cal.getTime();
		Date endDate = new Date();

		audits = accountCurrencyService.findAuditByAccountIdAndDateBetween(accountId, startDate, endDate);
		// Paging
		
		model.addAttribute("accountCurrencyAuditSearchForm", new AccountCurrencyAuditSearchForm(accountId, 
				BooleanOptions.BOTH, 
				BooleanOptions.BOTH, 
				startDate, endDate));
		model.addAttribute("audits", audits);
		
		return "admin/searchAccountCurrencyAudits";
	}
	
	/**
	 * List the Accounts. Allow searches for Accounts
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(HttpServletRequest request, Model model, 
			@ModelAttribute("accountCurrencyAuditSearchForm") @Valid AccountCurrencyAuditSearchForm searchForm, BindingResult bindingResult) {
		logger.trace("Account Currency Audit Search: Get");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		List<AccountCurrencyAudit> audits = new ArrayList<AccountCurrencyAudit>();
		// Binding result is null if we chained from another function
		if (bindingResult != null && bindingResult.hasErrors()) {
			model.addAttribute("error", "Error");
			model.addAttribute("audits", audits);
			return "admin/searchAccountCurrencyAudits";
        }
		audits = accountCurrencyService.findAuditByAccountIdAndDateBetween(
				searchForm.getAccountId(), searchForm.getStartDate(), searchForm.getEndDate());

		
		if(searchForm.getHardcore() != BooleanOptions.BOTH) {			
			Iterator<AccountCurrencyAudit> iter = audits.iterator();
			while(iter.hasNext()) {
				AccountCurrencyAudit thisAudit = iter.next();
				if(thisAudit.isHardcore() != searchForm.getHardcore().getBooleanValue()) {
					iter.remove();
				}
			}
		}
		
		if(searchForm.getIronborn() != BooleanOptions.BOTH) {			
			Iterator<AccountCurrencyAudit> iter = audits.iterator();
			while(iter.hasNext()) {
				AccountCurrencyAudit thisAudit = iter.next();
				if(thisAudit.isIronborn() != searchForm.getIronborn().getBooleanValue()) {
					iter.remove();
				}
			}
		}
		// Paging
		
		model.addAttribute("audits", audits);
		
		return "admin/searchAccountCurrencyAudits";
	}

	@ModelAttribute("booleanOptions")
    public BooleanOptions[] getBooleanOptions() {
		return BooleanOptions.values();
    }
}