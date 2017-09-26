package org.kerrin.dungeon.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.DungeonNotRunable;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.forms.DungeonStartForm;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonDifficulty;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.service.AccountBoostService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping(value="/api/account/{accountApiKey}/dungeon")
public class ApiDungeonController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiDungeonController.class);
	
	private final AccountService accountService;
	private final CharacterService characterService;
	private final EquipmentService equipmentService;
	private final DungeonService dungeonService;
	private final CharacterEquipmentService characterEquipmentService;
	private final AccountCurrencyService accountCurrencyService;
	private final AccountBoostService accountBoostService;
	
	@Autowired
	public ApiDungeonController(AccountService accountService, CharacterService characterService, 
			EquipmentService equipmentService, CharacterEquipmentService characterEquipmentService, 
			DungeonService dungeonService, AccountCurrencyService accountCurrencyService,
			AccountBoostService accountBoostService) {
		super();
		this.accountService = accountService;
		this.characterService = characterService;
		this.equipmentService = equipmentService;
		this.characterEquipmentService = characterEquipmentService;
		this.dungeonService = dungeonService;
		this.accountCurrencyService = accountCurrencyService;
		this.accountBoostService = accountBoostService;
	}

	/**
	 * Look at the details of a specific dungeon
	 */
	@RequestMapping(value = "/{dungeonId}", method = RequestMethod.GET)
	public Dungeon getDungeon(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("dungeonId") long dungeonId) {
		logger.debug("API: Get Dungeon Details");
		Account account = accountService.findByApiKey(accountApiKey);
		Dungeon dungeon = dungeonService.findById(dungeonId);
		
		if(account == null || dungeon == null || !dungeon.getAccount().equals(account)) {
			return null;
		}
		
		return dungeon;
	}
	
	/**
	 * Get all the dungeons an account can run
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<Dungeon> getAvailableDungeons(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("API: Get Dungeon Details");
		Account account = accountService.findByApiKey(accountApiKey);
		if(account == null) {
			return null;
		}
		
		List<Dungeon> dungeons = dungeonService.findAllByAccount(account, viewTypeForm.isHardcore(), viewTypeForm.isIronborn());
		
		return dungeons;
	}
	
	/**
	 * Get all the active dungeons
	 */
	@RequestMapping(value = "/active", method = RequestMethod.GET)
	public List<Dungeon> getActiveDungeons(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey) {
		logger.debug("API: Get Active Dungeons");
		Account account = accountService.findByApiKey(accountApiKey);
		if(account == null) {
			return null;
		}
		
		List<Dungeon> dungeons = dungeonService.findAllByAccountAndActive(account);
		
		return dungeons;
	}
	
	/**
	 * Look at the details of a specific dungeon
	 * @throws CharacterNotFound 
	 */
	@RequestMapping(value = "/{dungeonId}/start", method = RequestMethod.POST)
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={
			CharacterNotFound.class, AccountIdMismatch.class, DungeonNotFound.class, DungeonNotRunable.class, 
			EquipmentNotFound.class, BoostItemNotFound.class})
	public Dungeon getStartDungeon(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("dungeonId") long dungeonId,
			@ModelAttribute("dungeonStartForm") @Valid DungeonStartForm dungeonStartForm, BindingResult bindingResult) {
		logger.debug("API: Start Dungeon {}", dungeonId);
		Account account = accountService.findByApiKey(accountApiKey);
		Dungeon dungeon = dungeonService.findById(dungeonId);
		
		if(account == null || dungeon == null || !dungeon.getAccount().equals(account)) {
			return null;
		}
		if (bindingResult.hasErrors()) {
			return null;
        }
		
		try {
			DungeonDifficulty difficulty = dungeon.getDifficulty(dungeonStartForm.getDifficultyModifier());
			if(difficulty.getCost() > 0) {
				String reference = "dungeon-upgrade-"+account.getUsername()+"-"+dungeon.getId();			
				accountCurrencyService.adjustCurrency(account, dungeon.isHardcore(), dungeon.isIronborn(), 
						-difficulty.getCost(), ModificationType.SPEND_BOOST, reference);
			}
			dungeon = dungeonService.startDungeon(dungeon, dungeonStartForm.getCharacterIds(), 
					difficulty.getLevelAdjustment(), 
					equipmentService, characterEquipmentService);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		
		return dungeon;
	}
	
	/**
	 * Look at the details of a specific dungeon
	 * @throws CharacterNotFound 
	 */
	@RequestMapping(value = "/startAdventure", method = RequestMethod.POST)
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={
			CharacterNotFound.class, AccountIdMismatch.class, DungeonNotFound.class, DungeonNotRunable.class, 
			EquipmentNotFound.class, BoostItemNotFound.class})
	public Dungeon getStartDungeonAdventure(HttpServletRequest request, Model model, 
			@PathVariable("accountApiKey") String accountApiKey, 
			@ModelAttribute("dungeonStartForm") @Valid DungeonStartForm dungeonStartForm, BindingResult bindingResult) {
		logger.debug("API: Start Dungeon Adventure");
		Account account = accountService.findByApiKey(accountApiKey);
		if(account == null) {
			return null;
		}
		if (bindingResult.hasErrors()) {
			return null;
        }
		Dungeon dungeon;
		try {
			List<Character> characters = getCharacters(dungeonStartForm.getCharacterIds());
			if(characters == null) {
				throw new CharacterNotFound();
			}
			int lowestCharLevel = getLowestCharacterLevel(characters);
			
			Map<Monster, MonsterType> monsters = Dungeon.createMonstersForAdventure();
			long xpReward = DungeonType.ADVENTURE.getXpBase() * lowestCharLevel;

			if(accountBoostService.getXpBoostExpires(account, dungeonStartForm.isHardcore(), dungeonStartForm.isIronborn()) != null) {
				logger.debug("Applying XP boost");
				xpReward *= 2;
			}
			
			dungeon = new Dungeon(-1, DungeonType.ADVENTURE, account, 
					dungeonStartForm.isHardcore(), dungeonStartForm.isIronborn(), lowestCharLevel, 
					xpReward, monsters, dungeonStartForm.getCharacterIds().length, 0);
			
			dungeon = dungeonService.create(dungeon);
			List<Equipment> rewardItems = dungeon.generateItemRewards(false, dungeonStartForm.isHardcore(), dungeonStartForm.isIronborn());
	        Map<Equipment, Boolean> rewardItemsMap = new HashMap<Equipment, Boolean>();
	        for(Equipment equipment:rewardItems) {
				equipment = equipmentService.create(equipment);
				rewardItemsMap.put(equipment, false);
			}
	        
			dungeon.setItemRewards(rewardItemsMap);
			dungeonService.update(dungeon);
			dungeon = dungeonService.startDungeon(dungeon, dungeonStartForm.getCharacterIds(), 0, 
					equipmentService, characterEquipmentService);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		
		return dungeon;
	}

	private List<Character> getCharacters(long[] characterIds) {
		List<Character> characters = new ArrayList<Character>();
		for(long characterId: characterIds) {
			Character character = characterService.findById(characterId);
			if(character == null) return null;
			characters.add(character);
		}
		return characters;
	}
	
	private int getLowestCharacterLevel(List<Character> characters) {
		int lowestLevel = 100;
		for(Character character: characters) {
			if(lowestLevel > character.getLevel()) {
				lowestLevel = character.getLevel();
			}
		}
		return lowestLevel;
	}
}
