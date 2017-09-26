package org.kerrin.dungeon.controller;

import java.security.Principal;

import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountConfig;
import org.kerrin.dungeon.model.AccountCurrency;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

public class SuperPublic {
	protected final AccountConfigService accountConfigService;
	protected final AccountCurrencyService accountCurrencyService;
	protected boolean hardcore;
	protected boolean ironborn;
	
	public SuperPublic(AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService) {
		super();
		this.accountConfigService = accountConfigService;
		this.accountCurrencyService = accountCurrencyService;
	}
	protected void setUpViewType(ViewTypeForm viewTypeForm, Model model) {
		hardcore = viewTypeForm.isHardcore();
		ironborn = viewTypeForm.isIronborn();
		
		setUpViewTypeModel(model);
	}

	protected void setUpViewTypeModel(Model model) {
		model.addAttribute("hardcore", hardcore);
		model.addAttribute("ironborn", ironborn);
	}
	
	@ModelAttribute("hardcore")
    public boolean getHardcore(Principal principle) {
		return hardcore;
	}
	
	@ModelAttribute("ironborn")
    public boolean getIronborn(Principal principle) {
		return ironborn;
	}

	protected void setupConfigs(Model model, Account account) {
		AccountConfig accountConfigStandardEquipmentCompare = accountConfigService.findByAccount(account, false, false, AccountConfigType.EQUIPMENT_COMPARE);
		model.addAttribute("accountConfigStandardEquipmentCompare", accountConfigStandardEquipmentCompare);
		AccountConfig accountConfigStandardToolTips = accountConfigService.findByAccount(account, false, false, AccountConfigType.TOOL_TIPS);
		model.addAttribute("accountConfigStandardToolTips", accountConfigStandardToolTips);
		AccountConfig accountConfigHardcoreEquipmentCompare = accountConfigService.findByAccount(account, true, false, AccountConfigType.EQUIPMENT_COMPARE);
		model.addAttribute("accountConfigHardcoreEquipmentCompare", accountConfigHardcoreEquipmentCompare);
		AccountConfig accountConfigHardcoreToolTips = accountConfigService.findByAccount(account, true, false, AccountConfigType.TOOL_TIPS);
		model.addAttribute("accountConfigHardcoreToolTips", accountConfigHardcoreToolTips);
		AccountConfig accountConfigIronbornEquipmentCompare = accountConfigService.findByAccount(account, false, true, AccountConfigType.EQUIPMENT_COMPARE);
		model.addAttribute("accountConfigIronbornEquipmentCompare", accountConfigIronbornEquipmentCompare);
		AccountConfig accountConfigIronbornToolTips = accountConfigService.findByAccount(account, false, true, AccountConfigType.TOOL_TIPS);
		model.addAttribute("accountConfigIronbornToolTips", accountConfigIronbornToolTips);
		AccountConfig accountConfigExtremeEquipmentCompare = accountConfigService.findByAccount(account, true, true, AccountConfigType.EQUIPMENT_COMPARE);
		model.addAttribute("accountConfigExtremeEquipmentCompare", accountConfigExtremeEquipmentCompare);
		AccountConfig accountConfigExtremeToolTips = accountConfigService.findByAccount(account, true, true, AccountConfigType.TOOL_TIPS);
		model.addAttribute("accountConfigExtremeToolTips", accountConfigExtremeToolTips);
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