package org.kerrin.dungeon.controller.secure;

import java.security.Principal;

import javax.validation.Valid;

import org.kerrin.dungeon.controller.SuperPublic;
import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.enums.HiscoreType;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountConfig;
import org.kerrin.dungeon.model.AccountCurrency;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.utils.Facebook;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

public class SuperSecurePublic extends SuperPublic {
	protected final AccountService accountService;
	protected final AccountMessageService accountMessageService;
	private Facebook facebook;
		
	public SuperSecurePublic(AccountService accountService, 
			AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService,
			AccountMessageService accountMessageService, Facebook facebook) {
		super(accountConfigService, accountCurrencyService);
		this.accountService = accountService;
		this.accountMessageService = accountMessageService;
		this.facebook = facebook;
	}
	
	@ModelAttribute("account")
    public Account getAccount(Principal principle) {
		return accountService.findByPrinciple(principle);
	}
	
	@ModelAttribute("accountCurrency")
    public AccountCurrency getAccountCurrency(Principal principle) {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		return accountCurrencyService.findByAccount(account, hardcore, ironborn);
	}
	
	@ModelAttribute("accountConfigEquipmentCompare")
    public AccountConfig getAccountConfigEquipmentCompare(Principal principle) {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		return accountConfigService.findByAccount(account, hardcore, ironborn, AccountConfigType.EQUIPMENT_COMPARE);
	}
	
	@ModelAttribute("accountConfigToolTips")
    public AccountConfig getAccountConfigToolTips(Principal principle) {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		return accountConfigService.findByAccount(account, hardcore, ironborn, AccountConfigType.TOOL_TIPS);
	}
	
	@ModelAttribute("hiscoreTypes")
    public HiscoreType[] getHiscoreTypes(Principal principle) {		
		return HiscoreType.values();
	}
	
	@ModelAttribute("facebook")
    public Facebook getFacebook(Principal principle) {		
		return facebook;
	}
	
	@ModelAttribute("gotMessages")
    public boolean getMessages(Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return false;
		}
		setUpViewType(viewTypeForm, model);
		return !accountMessageService.findAllByAccount(account, hardcore, ironborn).isEmpty();
	}
}