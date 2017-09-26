package org.kerrin.dungeon.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CantEquipToDungeon;
import org.kerrin.dungeon.exception.CantEquipToMessage;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryException;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.exception.MessageEquipmentNotFound;
import org.kerrin.dungeon.forms.CharacterForm;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.forms.validator.CharacterFormValidator;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.api.CharacterResponse;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.service.ReloadPanels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping(value="/api/account/{accountApiKey}/character")
public class ApiCharacterController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiCharacterController.class);
	
	private final AccountService accountService;
	private final CharacterService characterService;
	private final CharacterEquipmentService characterEquipmentService;
	private final AccountCurrencyService accountCurrencyService;
	private final CharacterFormValidator characterFormValidator;
	private final EquipmentService equipmentService;
	private final AchievementService achievementService;
	private final AccountMessageService accountMessageService;
	private final HiscoreService hiscoreService;
	
	@Autowired
	public ApiCharacterController(AccountService accountService, CharacterService characterService,
			CharacterEquipmentService characterEquipmentService,
			AccountCurrencyService accountCurrencyService, CharacterFormValidator characterFormValidator,
			EquipmentService equipmentService, AchievementService achievementService,
			AccountMessageService accountMessageService, HiscoreService hiscoreService) {
		super();
		this.accountService = accountService;
		this.characterService = characterService;
		this.characterEquipmentService = characterEquipmentService;
		this.accountCurrencyService = accountCurrencyService;
		this.characterFormValidator = characterFormValidator;
		this.equipmentService = equipmentService;
		this.achievementService = achievementService;
		this.hiscoreService = hiscoreService;
		this.accountMessageService = accountMessageService;
	}

	//Set a form validator
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(characterFormValidator);
	}
	
	/**
	 * Look at the details of a specific character
	 */
	@RequestMapping(value = "/{charId}", method = RequestMethod.GET)
	public Character getCharacter(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey, @PathVariable("charId") long charId) {
		logger.debug("API: Get Character Details for character Id "+charId);
		Account account = accountService.findByApiKey(accountApiKey);
		
		if(account == null) {
			return null;
		}
		
		Character character = characterService.findById(charId);
		// Check for invalid character or the character is not for this account
		if(character == null || !character.getAccount().equals(account)) {
			return null;
		}
		
		return character;
	}
	
	/**
	 * Look at the details of all character for an account
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Character> getAllCharacters(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("API: Get All Characters");
		Account account = accountService.findByApiKey(accountApiKey);
		
		if(account == null) {
			return null;
		}
		
		List<Character> characters = characterService.findAllByAccountOrderByLevel(
				account, viewTypeForm.isHardcore(), viewTypeForm.isIronborn());
		
		return characters;
	}
	
	/**
	 * Create a new character
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Character createCharacter(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey, 
			@ModelAttribute("characterForm") @Validated CharacterForm characterForm, BindingResult bindingResult) {		
		logger.debug("API: Create Character Details");
		Account account = accountService.findByApiKey(accountApiKey);
		
		if(account == null) {
			return null;
		}
		
		List<Character> accountCharacters = characterService.findAllByAccountOrderByLevel(
				account, characterForm.isHardcore(), characterForm.isIronborn());
		int numberOfCharacters = 1;
		if(accountCharacters != null) {
			numberOfCharacters = accountCharacters.size();
		}
		String reference = "api-create-"+account.getUsername();
		if(!accountCurrencyService.adjustCurrency(account, characterForm.isHardcore(), characterForm.isIronborn(), 
				-numberOfCharacters, 
				ModificationType.SPEND_CHARACTER, reference)) {
		    return null;
		}
		
		Character character = new Character(-1, account, characterForm.isHardcore(), characterForm.isIronborn(), 
				characterForm.getName(), characterForm.getCharClass(), 1, 0, 0, 
				null /* Death time */, null /* dungeon */);
		// Check for invalid character or the character is not for this account
		if(character == null || !character.getAccount().equals(account)) {
			return null;
		}
		
		if (bindingResult.hasErrors()) {
			return null;
        }
		if(characterForm.getName() != null && !characterForm.getName().isEmpty()) {
			character.setName(characterForm.getName());
		}
				
		character = characterService.create(character);
		
		// Create equipment
		Map<CharSlot, Equipment> characterSlots = new HashMap<CharSlot, Equipment>();
		try {
			equipmentService.generateStarterEquipment(character, characterSlots);
		} catch (EquipmentNotFound e) {
			logger.error("Error creating starter equipment for character {}: {}", character.getId(), e.getMessage());
			return null;
		}
		CharacterEquipment characterEquipment = new CharacterEquipment(character, characterSlots );
		characterEquipmentService.create(characterEquipment);
		List<Achievement> newAchievements = achievementService.newCharacter(request.getContextPath(), character);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
			
		return character;
	}
	
	/**
	 * Update a character details
	 */
	@RequestMapping(value = "/{charId}", method = RequestMethod.POST)
	public Character updateCharacter(HttpServletRequest request, Model model,
			@ModelAttribute("characterForm") @Valid CharacterForm characterForm, BindingResult bindingResult,
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("charId") long charId) {		
		logger.debug("API: Update Character Details for character Id "+charId);
		Account account = accountService.findByApiKey(accountApiKey);
		
		if(account == null) {
			return null;
		}
		
		Character character = characterService.findById(charId);
		// Check for invalid character or the character is not for this account
		if(character == null || !character.getAccount().equals(account)) {
			return null;
		}
		
		if (bindingResult.hasErrors()) {
			return null;
        }
		if(characterForm.getName() != null && !characterForm.getName().isEmpty()) {
			character.setName(characterForm.getName());
		}
				
		if(character.getId() == charId) {
			try {
				characterService.update(character);
			} catch (CharacterNotFound e) {
				model.addAttribute("error", "Error updating character: "+e.getMessage());
			}
		} else {
			model.addAttribute("error", "Character ID changed from " + charId + 
					" to " + character.getId() + " update cancelled!");
			character = null;
		}
		
		return character;
	}
	
	/**
	 * Level up a character by spending dungeon tokens
	 */
	@RequestMapping(value = "/{charId}/level/{levels}", method = RequestMethod.POST)
	@Transactional
	public Character levelUpCharacter(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("charId") long charId, 
			@PathVariable("levels") int levels) {		
		logger.debug("API: Level up Character for character Id "+charId);
		Account account = accountService.findByApiKey(accountApiKey);
		
		if(account == null) {
			return null;
		}
		
		Character character = characterService.findById(charId);
		// Check for invalid character or the character is not for this account
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("error", "Invalid account.");
			return null;
		}

		if(character.isIronborn()) {
			// Can't Level up Ironborn characters
			model.addAttribute("error", "That character is Ironborn, you cannot level it that way.");
			return null;
		}
		if(character.isCurrentlyDead()) {
			model.addAttribute("error", "That character is dead. Only alive characters can be reset for prestige");
			return null;
		}
		
		// Calculate required number of char tokens for upgrade
		int tempLevel = character.getLevel();
		int levelsLeftToAdd = levels;
		int charTokensRequired = 0;
		while(levelsLeftToAdd > 0) {
			// Add current level then increment the level
			charTokensRequired += tempLevel++;
			levelsLeftToAdd--;
		}
		logger.debug("Char Level "+character.getLevel()+" level up " + levels +" times takes "+charTokensRequired+ " tokens");
		String reference = "api-levelup-"+account.getUsername()+"-"+charId+"-"+character.getLevel()+"-"+levels;			
		if(!accountCurrencyService.adjustCurrency(account, character.isHardcore(), character.isIronborn(), 
				-charTokensRequired, 
				ModificationType.SPEND_LEVELUP, reference)) {
			model.addAttribute("error", "Not enough dungeon tokens, you need "+charTokensRequired);
		}
		
		if(character.getId() == charId) {
			try {
				character.setLevel(character.getLevel() + levels);
				character.setUsedLevelUp(true);
				characterService.update(character);
				accountMessageService.messageLevelUp(request.getContextPath(), character.getId(), account, character);
				List<Achievement> newAchievements = achievementService.levelUp(request.getContextPath(), character);
				int newPoints = 0;
				for(Achievement achievement:newAchievements) {
					newPoints += achievement.getPoints();
				}
				if(newPoints > 0) {
					hiscoreService.achievement(account, newPoints);
				}
			} catch (CharacterNotFound e) {
				model.addAttribute("error", "Error updating character: "+e.getMessage());
			}
		} else {
			model.addAttribute("error", "Character ID changed from " + charId + 
					" to " + character.getId() + " update cancelled!");
			character = null;
		}
		
		return character;
	}
	
	/**
	 * Level up a character by spending dungeon tokens
	 */
	@RequestMapping(value = "/{charId}/level/prestige", method = RequestMethod.POST)
	@Transactional
	public Character resetCharacterForPrestige(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("charId") long charId) {		
		logger.debug("API: Reset Character for prestige for character Id "+charId);
		Account account = accountService.findByApiKey(accountApiKey);
		
		if(account == null) {
			model.addAttribute("error", "Invalid account.");
			return null;
		}
		
		Character character = characterService.findById(charId);
		// Check for invalid character or the character is not for this account
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("error", "Invalid account.");
			return null;
		}
		
		if(character.isCurrentlyDead()) {
			model.addAttribute("error", "That character is dead. Only alive characters can be reset for prestige");
			return null;
		}
		
		character.resetPrestige();
		Map<CharSlot, Equipment> equipmentBySlot = characterEquipmentService.findAllByCharacter(character);
		try {
			for(Equipment equipment:equipmentBySlot.values()) {
				equipment.setLevel(1);
				equipment.checkForRerollReduceLevel();
				equipment = equipmentService.update(equipment, false);
			}
		
			characterService.update(character);
		} catch (CharacterNotFound e) {
			model.addAttribute("error", "Character "+charId+" not found");
		} catch (EquipmentNotFound e) {
			model.addAttribute("error", "Character equipment error");
		}
		List<Achievement> newAchievements = achievementService.prestigeLevel(request.getContextPath(), character);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
		
		return character;
	}
	
	/**
	 * Resurrect a dead character
	 */
	@RequestMapping(value = "/{charId}/resurrect", method = RequestMethod.POST)
	@Transactional
	public Character resurrect(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("charId") long charId) {		
		logger.debug("API: Resurrect Character for character Id "+charId);
		Account account = accountService.findByApiKey(accountApiKey);
		
		if(account == null) {
			model.addAttribute("characterDetails", null);
			model.addAttribute("error", "Invalid account.");
			return null;
		}
		
		Character character = characterService.findById(charId);
		// Check for invalid character or the character is not for this account
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("error", "Invalid account.");
			return null;
		}
		
		if(!character.isCurrentlyDead()) {
			return null;
		}

		if(character.isHardcore()) {
			// Can't Resurrect hardcore characters
			model.addAttribute("error", "Can't resurrect hardcore characters.");
			return null;
		}
		
		String reference = "api-resurrect-"+account.getUsername()+"-"+charId+"-"+System.currentTimeMillis();			
		if(!accountCurrencyService.adjustCurrency(account, character.isHardcore(), character.isIronborn(), 
				-character.getLevel(), 
				ModificationType.SPEND_RESURRECT, reference)) {
			model.addAttribute("error", "Not enough dungeon tokens, you need "+character.getLevel());
		}
		
		if(character.getId() == charId) {
			try {
				character.setAlive();
				characterService.update(character);
			} catch (CharacterNotFound e) {
				return null;
			}
		} else {
			return null;
		}

		List<Achievement> newAchievements = achievementService.resurrected(request.getContextPath(), character);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
		
		return character;
	}
	
	/**
	 * Equip item on character
	 */
	@RequestMapping(value = "/{characterId}/equipment/{equipmentId}/slot/{slotId}", method = RequestMethod.POST)
	public CharacterResponse equipmentItemOnCharacter(HttpServletRequest request, Model model,
			@PathVariable("accountApiKey") String accountApiKey, 
			@PathVariable("characterId") long characterId, 
			@PathVariable("equipmentId") long equipmentId, 
			@PathVariable("slotId") int slotId) {
		logger.debug("API: Equipment item in characters slot");
		Account account = accountService.findByApiKey(accountApiKey);
		if(account == null) {
			return null;
		}
		
		Character character = characterService.findById(characterId);
		// Check for invalid character or the character is not for this account
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("characterDetails", null);
			
			return null;
		}
		
		CharSlot charSlot = CharSlot.fromId(slotId);
		if(charSlot == CharSlot.UNKNOWN) {
			model.addAttribute("characterDetails", null);
			
			return null;
		}
		Equipment equipment = equipmentService.findById(equipmentId);
		ReloadPanels reloadPanels;
		try {
			// Make sure the account owns the equipment involved in the swap
			if(!accountService.accountOwnsEquipment(account, equipment)) {
				return null;
			}	
			
			reloadPanels = equipmentService.swapEquipmentInCharacterSlot(account, equipment, character, charSlot);
			if(reloadPanels == null) {
				logger.debug("Equipment in slot not in a swapable location");
				return null;
			}
		} catch (DungeonNotFound e) {
			logger.debug("Dungeon not found");
			return null;
		} catch (AccountIdMismatch e) {
			logger.debug("Account Id mismatch");
			return null;
		} catch (EquipmentNotFound e) {
			logger.debug("Equipment not found");
			return null;
		} catch (InventoryNotFound e) {
			logger.debug("Inventory not found");
			return null;
		} catch (InventoryException e) {
			logger.debug("Inventory exception {}", e.getMessage());
			return null;
		} catch (CantEquipToDungeon e) {
			logger.debug("Attempt to equip to dungeon");
			return null;
		} catch (BoostItemNotFound e) {
			logger.debug("BoostItem not found");
			return null;
		} catch (CantEquipToMessage e) {
			logger.debug("Attempt to equip to message");
			return null;
		} catch (CharacterNotFound e) {
			logger.debug("Character not found");
			return null;
		} catch (CharacterSlotNotFound e) {
			logger.debug("Character slot not found");
			return null;
		} catch (CharacterEquipmentNotFound e) {
			logger.debug("Character equipment not found");
			return null;
		} catch (MessageEquipmentNotFound e) {
			logger.debug("Message equipment not found");
			return null;
		}
		
		List<Achievement> newAchievements = achievementService.equipItem(request.getContextPath(), equipment);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
		
		return new CharacterResponse(character, reloadPanels);
	}
}
