package org.kerrin.dungeon.controller.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.BooleanOptions;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.DungeonForm;
import org.kerrin.dungeon.forms.DungeonSearchForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.DungeonService;
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
@RequestMapping(value="/admin/dungeon")
public class AdminDungeonSearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminDungeonSearchController.class);
	
	@Autowired
	private DungeonService dungeonService;
	@Autowired
	private AccountService accountService;
	
	/**
	 * List the Dungeons. Allow searches for Dungeons
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(HttpServletRequest request, Model model, 
			@ModelAttribute("dungeonSearchForm") @Valid DungeonSearchForm searchForm, BindingResult bindingResult) {
		boolean restricted = false;
		logger.trace("Dungeon Search: Get");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		List<Dungeon> dungeons = new ArrayList<Dungeon>();
		if (bindingResult.hasErrors()) {
			model.addAttribute("dungeons", dungeons);
			model.addAttribute("dungeonForm", new DungeonForm());
            return "admin/searchDungeons";
        }
		if(searchForm.getId() > 0) {
			restricted = true;
			Dungeon dungeon = dungeonService.findById(searchForm.getId());
			if(dungeon != null) {
				dungeons.add(dungeon);
				bindingResult.reject("id", "SearchForm.NotFound.dungeonId");
			}
		}
		
		if(searchForm.getAccountId() >= 1) {
			Account account = accountService.findById(searchForm.getAccountId());
			if(account != null) {
				List<Dungeon> foundDungeons = dungeonService.findAllByAccount(account);
				if(!restricted && dungeons.isEmpty()) {
					dungeons = foundDungeons;
				} else {
					dungeons.retainAll(foundDungeons);
				}
				restricted = true;
			}
		}

		if(searchForm.getType() != null && !searchForm.getType().equals(DungeonType.NONE)) {
			List<Dungeon> foundDungeons = dungeonService.findAllByDungeonType(searchForm.getType());
			if(!restricted && dungeons.isEmpty()) {
				dungeons = foundDungeons;
			} else {
				dungeons.retainAll(foundDungeons);
			}
			restricted = true;
		}
		if(searchForm.getGreaterThanLevel() >= 0 && searchForm.getLessThanLevel() >= 1) {
			List<Dungeon> foundDungeons = dungeonService.findAllByLevelBetween(searchForm.getGreaterThanLevel(), searchForm.getLessThanLevel());
			if(!restricted && dungeons.isEmpty()) {
				dungeons = foundDungeons;
			} else {
				dungeons.retainAll(foundDungeons);
			}
			restricted = true;
		}
		if(searchForm.getGreaterThanXpReward() >= 0 && searchForm.getLessThanXpReward() >= 1) {
			List<Dungeon> foundDungeons = dungeonService.findAllByXpRewardBetween(searchForm.getGreaterThanXpReward(), searchForm.getLessThanXpReward());
			if(!restricted && dungeons.isEmpty()) {
				dungeons = foundDungeons;
			} else {
				dungeons.retainAll(foundDungeons);
			}
			restricted = true;
		}
		if(searchForm.getGreaterThanPartySize() >= 0 && searchForm.getLessThanPartySize() >= 1) {
			List<Dungeon> foundDungeons = dungeonService.findAllByPartySizeBetween(searchForm.getGreaterThanPartySize(), searchForm.getLessThanPartySize());
			if(!restricted && dungeons.isEmpty()) {
				dungeons = foundDungeons;
			} else {
				dungeons.retainAll(foundDungeons);
			}
			restricted = true;
		}
		
		if(searchForm.getHardcore() != BooleanOptions.BOTH) {			
			Iterator<Dungeon> iter = dungeons.iterator();
			while(iter.hasNext()) {
				Dungeon thisDungeon = iter.next();
				if(thisDungeon.isHardcore() != searchForm.getHardcore().getBooleanValue()) {
					iter.remove();
				}
			}
		}
		
		if(searchForm.getIronborn() != BooleanOptions.BOTH) {			
			Iterator<Dungeon> iter = dungeons.iterator();
			while(iter.hasNext()) {
				Dungeon thisDungeon = iter.next();
				if(thisDungeon.isIronborn() != searchForm.getIronborn().getBooleanValue()) {
					iter.remove();
				}
			}
		}
		// Paging
		
		model.addAttribute("dungeons", dungeons);
		model.addAttribute("dungeonForm", new DungeonForm());
		
		return "admin/searchDungeons";
	}

	@ModelAttribute("booleanOptions")
    public BooleanOptions[] getBooleanOptions() {
		return BooleanOptions.values();
    }
}
