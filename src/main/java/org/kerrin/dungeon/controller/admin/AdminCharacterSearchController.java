package org.kerrin.dungeon.controller.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.BooleanOptions;
import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.CharacterForm;
import org.kerrin.dungeon.forms.CharacterSearchForm;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/admin/character")
public class AdminCharacterSearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminCharacterSearchController.class);
	
	@Autowired
	private CharacterService charService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private DungeonService dungeonService;
	
	/**
	 * List the characters. Allow searches for characters
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(HttpServletRequest request, Model model, 
			@ModelAttribute("characterSearchForm") @Valid CharacterSearchForm searchForm, 
			BindingResult bindingResult) {
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		
		List<Character> characters = new ArrayList<Character>();
		if (bindingResult.hasErrors()) {
			model.addAttribute("characters", characters);
			model.addAttribute("characterForm", new CharacterForm());
            return "admin/searchCharacters";
        }
		if(searchForm.getId() > 0) {			
			Character character = charService.findById(searchForm.getId());
			if(character == null) {
				bindingResult.reject("id", "CharacterSearchForm.NotFound.characterId");
				model.addAttribute("error", "Character ID not found!");
			} else {
				characters.add(character);
			}
		}
		if(searchForm.getAccountId() > 0) {			
			Account account = accountService.findById(searchForm.getAccountId());
			if(account != null) {
				List<Character> foundCharacters = charService.findAllByAccountOrderByLevel(account);
				if(characters.isEmpty()) {
					characters = foundCharacters;
				} else {
					characters.retainAll(foundCharacters);
				}
			}
		}
		if(searchForm.getName() != null && !searchForm.getName().isEmpty()) {
			List<Character> foundCharacters = charService.findAllByName(searchForm.getName());
			if(characters.isEmpty()) {
				characters = foundCharacters;
			} else {
				characters.retainAll(foundCharacters);
			}
		}
		if(searchForm.getCharClass() != CharClass.ANY) {
			List<Character> foundCharacters = charService.findAllByCharacterClass(searchForm.getCharClass());
			if(characters.isEmpty()) {
				characters = foundCharacters;
			} else {
				characters.retainAll(foundCharacters);
			}
		}
		if(searchForm.getGreaterThanLevel() >= 0 || searchForm.getLessThanLevel() >= 0) {
			int startLevel = searchForm.getGreaterThanLevel();
			int endLevel = searchForm.getLessThanLevel();
			if(startLevel < 0) startLevel = 0;
			if(endLevel < 0) endLevel = 0;
			if(endLevel < startLevel) {
				int temp = startLevel;
				startLevel = endLevel;
				endLevel = temp;
			}
			// Greater Than or EQUAL, etc
			startLevel--;
			endLevel++;
			logger.debug("Level "+startLevel+" to "+endLevel);
			List<Character> foundCharacters = charService.findAllByLevelGreaterThanAndLevelLessThan(startLevel, endLevel);
			if(characters.isEmpty()) {
				characters = foundCharacters;
			} else {
				characters.retainAll(foundCharacters);
			}
		}
		if(searchForm.getGreaterThanXp() >= 0 || searchForm.getLessThanXp() >= 0) {
			long startXp = searchForm.getGreaterThanXp();
			long endXp = searchForm.getLessThanXp();
			if(startXp < 0) startXp = 0;
			if(endXp < 0) endXp = 0;
			if(endXp < startXp) {
				long temp = startXp;
				startXp = endXp;
				endXp = temp;
			}
			// Greater Than or EQUAL, etc
			startXp--;
			endXp++;
			logger.debug("XP "+startXp+" to "+endXp);
			List<Character> foundCharacters = charService.findAllByXpGreaterThanAndXpLessThan(startXp, endXp);
			if(characters.isEmpty()) {
				characters = foundCharacters;
			} else {
				characters.retainAll(foundCharacters);
			}
		}
		if(searchForm.getGreaterThanPrestigeLevel() >= 0 || searchForm.getLessThanPrestigeLevel() >= 0) {
			int startLevel = searchForm.getGreaterThanPrestigeLevel();
			int endLevel = searchForm.getLessThanPrestigeLevel();
			if(startLevel < 0) startLevel = 0;
			if(endLevel < 0) endLevel = 0;
			if(endLevel < startLevel) {
				int temp = startLevel;
				startLevel = endLevel;
				endLevel = temp;
			}
			// Greater Than or EQUAL, etc
			startLevel--;
			endLevel++;
			logger.debug("Prestige Level "+startLevel+" to "+endLevel);
			List<Character> foundCharacters = charService.findAllByPrestigeLevelGreaterThanAndPrestigeLevelLessThan(startLevel, endLevel);
			if(characters.isEmpty()) {
				characters = foundCharacters;
			} else {
				characters.retainAll(foundCharacters);
			}
		}

		if(searchForm.getDungeonId() > 0) {			
			Dungeon dungeon = dungeonService.findById(searchForm.getDungeonId());
			if(dungeon != null) {
				List<Character> foundCharacters = charService.findAllByDungeon(dungeon);
				if(characters.isEmpty()) {
					characters = foundCharacters;
				} else {
					characters.retainAll(foundCharacters);
				}
			}
		}
		
		if(searchForm.getHardcore() != BooleanOptions.BOTH) {			
			Iterator<Character> iter = characters.iterator();
			while(iter.hasNext()) {
				Character thisChar = iter.next();
				if(thisChar.isHardcore() != searchForm.getHardcore().getBooleanValue()) {
					iter.remove();
				}
			}
		}
		
		if(searchForm.getIronborn() != BooleanOptions.BOTH) {			
			Iterator<Character> iter = characters.iterator();
			while(iter.hasNext()) {
				Character thisChar = iter.next();
				if(thisChar.isIronborn() != searchForm.getIronborn().getBooleanValue()) {
					iter.remove();
				}
			}
		}
		
		// TODO: Paging
		
		model.addAttribute("characters", characters);
		//model.addAttribute("characterSearchForm", searchForm);
		model.addAttribute("characterForm", new CharacterForm());
		
		return "admin/searchCharacters";
	}

	@ModelAttribute("characterClasses")
    public CharClass[] getCharacterClasses() {
		return CharClass.values();
    }

	@ModelAttribute("booleanOptions")
    public BooleanOptions[] getBooleanOptions() {
		return BooleanOptions.values();
    }
}
