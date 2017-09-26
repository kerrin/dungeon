package org.kerrin.dungeon.controller.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.BooleanOptions;
import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.BoostItemForm;
import org.kerrin.dungeon.forms.BoostItemSearchForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.BoostItemService;
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
@RequestMapping(value="/admin/boostitem")
public class AdminBoostItemSearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminBoostItemSearchController.class);
	
	@Autowired
	private BoostItemService boostItemService;
	@Autowired
	private AccountService accountService;
	
	/**
	 * List the BoostItems. Allow searches for BoostItems
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(HttpServletRequest request, Model model, @ModelAttribute("boostItemSearchForm") @Valid BoostItemSearchForm searchForm, BindingResult bindingResult) {
		boolean restricted = false;
		logger.trace("BoostItem Search: Get");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		List<BoostItem> boostItems = new ArrayList<BoostItem>();
		if (bindingResult.hasErrors()) {
			model.addAttribute("boostItems", boostItems);
			model.addAttribute("boostItemForm", new BoostItemForm());
            return "admin/searchBoostItems";
        }
		if(searchForm.getId() > 0) {
			restricted = true;
			BoostItem boostItem = boostItemService.findById(searchForm.getId());
			if(boostItem != null) {
				boostItems.add(boostItem);
				bindingResult.reject("id", "SearchForm.NotFound.boostItemId");
			}
		}
		if(searchForm.getAccountId() != -1) {
			Account account = accountService.findById(searchForm.getAccountId());
			if(account != null) {
				List<BoostItem> foundBoostItems = boostItemService.findAllByAccount(account);
				if(!restricted && boostItems.isEmpty()) {
					boostItems = foundBoostItems;
				} else {
					boostItems.retainAll(foundBoostItems);
				}
				restricted = true;
			}
		}
		if(searchForm.getGreaterThanLevel() >= 1 || searchForm.getLessThanLevel() >= 1) {
			List<BoostItem> foundBoostItems = boostItemService.findByLevelGreaterThanAndLevelLessThan(
					searchForm.getGreaterThanLevel()-1, searchForm.getLessThanLevel()+1);
			if(!restricted && boostItems.isEmpty()) {
				boostItems = foundBoostItems;
			} else {
				boostItems.retainAll(foundBoostItems);
			}
			restricted = true;
		}
		
		if(searchForm.getHardcore() != BooleanOptions.BOTH) {			
			Iterator<BoostItem> iter = boostItems.iterator();
			while(iter.hasNext()) {
				BoostItem thisBoostItem = iter.next();
				if(thisBoostItem.isHardcore() != searchForm.getHardcore().getBooleanValue()) {
					iter.remove();
				}
			}
		}
		
		if(searchForm.getIronborn() != BooleanOptions.BOTH) {			
			Iterator<BoostItem> iter = boostItems.iterator();
			while(iter.hasNext()) {
				BoostItem thisBoostItem = iter.next();
				if(thisBoostItem.isIronborn() != searchForm.getIronborn().getBooleanValue()) {
					iter.remove();
				}
			}
		}
		
		if(searchForm.getBoostItemType() != null) {			
			Iterator<BoostItem> iter = boostItems.iterator();
			while(iter.hasNext()) {
				BoostItem thisBoostItem = iter.next();
				if(thisBoostItem.getBoostItemType() != searchForm.getBoostItemType()) {
					iter.remove();
				}
			}
		}
		// TODO: Paging
		
		model.addAttribute("boostItems", boostItems);
		model.addAttribute("boostItemForm", new BoostItemForm());
		
		return "admin/searchBoostItems";
	}

	@ModelAttribute("boostItemType")
    public BoostItemType[] getBoostItemType() {
		BoostItemType[] boostItemType = BoostItemType.values();
		BoostItemType[] boostItemTypesNoUnknown = new BoostItemType[boostItemType.length-1];
		for(int i=1; i < boostItemType.length; i++) boostItemTypesNoUnknown[i-1] = boostItemType[i];
		return boostItemTypesNoUnknown;
    }

	@ModelAttribute("booleanOptions")
    public BooleanOptions[] getBooleanOptions() {
		return BooleanOptions.values();
    }
}
