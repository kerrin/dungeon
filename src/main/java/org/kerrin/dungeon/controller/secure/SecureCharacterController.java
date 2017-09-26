package org.kerrin.dungeon.controller.secure;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.CharacterForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.forms.RenameCharacterForm;
import org.kerrin.dungeon.forms.ViewTypeForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.PowerValues;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.utils.CommonTools;
import org.kerrin.dungeon.utils.Facebook;
import org.kerrin.dungeon.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/play/character")
public class SecureCharacterController extends SuperSecurePublic {
	
	private static final Logger logger = LoggerFactory.getLogger(SecureCharacterController.class);
	
	private final CharacterService characterService;
	private final EquipmentService equipmentService;
	private final CharacterEquipmentService characterEquipmentService;
	private final AchievementService achievementService;
	private final HiscoreService hiscoreService;
	private final BoostItemService boostItemService;
	private final boolean localMode;
	
	@Autowired
	public SecureCharacterController(AccountService accountService, CharacterService characterService, 
			Facebook facebook, EquipmentService equipmentService, CharacterEquipmentService characterEquipmentService, 
			AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService, 
			AchievementService achievementService,
			AccountMessageService accountMessageService, HiscoreService hiscoreService,
			BoostItemService boostItemService,
			boolean localMode) {
		super(accountService, accountConfigService, accountCurrencyService, accountMessageService, facebook);
		this.characterService = characterService;
		this.equipmentService = equipmentService;
		this.characterEquipmentService = characterEquipmentService;
		this.achievementService = achievementService;
		this.hiscoreService = hiscoreService;
		this.boostItemService = boostItemService;
		this.localMode = localMode;
	}
	
	/**
	 * Display the characters list page
	 * 
	 * @param request
	 * @param locale
	 * @param model
	 * @param principle
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getCharacters(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Get Characters");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		List<Character> characters = characterService.findAllByAccountOrderByLevel(account, hardcore, ironborn);
		model.addAttribute("characters", characters);
		Map<Long,CharacterEquipment> characterEquipment = new HashMap<Long,CharacterEquipment>();
		for(Character character:characters) {
			characterEquipment.put(character.getId(), characterEquipmentService.findById(character.getId()));
		}
		model.addAttribute("characterEquipment", characterEquipment);
				
		int resurrectionCost = 0;
		if(!hardcore) {			
			for(Character character:characters) {
				if(character.isCurrentlyDead() && character.getDungeon() == null) {
					resurrectionCost += character.getLevel();
				}
			}
		}
		model.addAttribute("resurrectionCost", resurrectionCost);
		model.addAttribute("localMode", localMode);
		
		return "play/characters";
	}
	
	/**
	 * Look at the details of a specific character
	 */
	@RequestMapping(value = "/{charId}", method = RequestMethod.GET)
	public String getCharacterDetails(Model model, Principal principle, @PathVariable long charId) {
		logger.debug("Character Details");
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Character character = characterService.findById(charId);
		
		if(character == null || !character.getAccount().equals(account)) {
			prepareCreateCharacterModelAttributes(model, account);
			model.addAttribute("error", "Character not found");
			return "play/createCharacter";
		}
		hardcore = character.isHardcore();
		ironborn = character.isIronborn();
		setUpViewTypeModel(model);
		if(!prepareCharacterDetailsModelAttributes(model, character)) {
			prepareCreateCharacterModelAttributes(model, account);
			model.addAttribute("error", "Character not found");
			return "play/createCharacter";
		}
		
		BoostItem usableLevelBoost = boostItemService.getValidBoostItemType(
				account, hardcore, ironborn, BoostItemType.LEVEL_UP, character.getLevel(), true);
		if(usableLevelBoost != null) {
			model.addAttribute("usableLevelBoost", usableLevelBoost);
		}
		
		BoostItem usableResurrectBoost = boostItemService.getValidBoostItemType(
				account, hardcore, ironborn, BoostItemType.RESURRECTION, character.getLevel(), true);
		if(usableResurrectBoost != null) {
			model.addAttribute("usableResurrectBoost", usableResurrectBoost);
		}
		BoostItem usableRenameBoost = boostItemService.getValidBoostItemType(
				account, hardcore, ironborn, BoostItemType.CHANGE_NAME, character.getLevel(), true);
		if(usableRenameBoost != null) {
			model.addAttribute("usableRenameBoost", usableRenameBoost);
		}
		
		return "play/characterDetails";
	}
	
	/**
	 * Look at the attribute summary of a specific character
	 */
	@RequestMapping(value = "/{charId}/summary", method = RequestMethod.GET)
	public String getCharacterDetailsSummary(Model model, Principal principle, @PathVariable long charId) {
		logger.debug("Character Details Summary");
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Character character = characterService.findById(charId);
		
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("error", "Character not found");
			
			return "blank";
		}
		hardcore = character.isHardcore();
		ironborn = character.isIronborn();
		setUpViewTypeModel(model);
		if(!prepareCharacterDetailsSummaryModelAttributes(model, character)) {
			model.addAttribute("error", "Character not found");
			
			return "blank";
		}
		
		return "play/characterDetailsSummary";
	}
	
	/**
	 * Create a character form
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createCharacterForm(Locale locale, Model model, Principal principle,
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Create Character Form");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		
		prepareCreateCharacterModelAttributes(model, account);
		
		return "play/createCharacter";
	}
	
	/**
	 * Create a character
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String createCharacter(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@ModelAttribute("characterForm") @Validated CharacterForm characterForm, BindingResult bindingResult) {
		logger.debug("Create Character Action");
		hardcore = characterForm.isHardcore();
		ironborn = characterForm.isIronborn();
		setUpViewTypeModel(model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		
		if (bindingResult.hasErrors() || characterForm.getCharClass() == CharClass.ANY) {
			logger.debug("Has errors");
			model.addAttribute("error", "Please, enter all the details.");
			model.addAttribute("charId", -1);
			model.addAttribute("dungeonId", 0);		
            return "play/index";
        }
		
		List<Character> accountCharacters = characterService.findAllByAccountOrderByLevel(account, hardcore, ironborn);
		int numberOfCharacters = 1;
		if(accountCharacters != null) {
			numberOfCharacters = accountCharacters.size();
		}
		String reference = "create-char-"+account.getUsername()+"-"+System.currentTimeMillis();			
		if(!accountCurrencyService.adjustCurrency(
					account, characterForm.isHardcore(), characterForm.isIronborn(),
					-numberOfCharacters, ModificationType.SPEND_CHARACTER, reference)
				) {
			model.addAttribute("error", "Not enough dungeon tokens to create a new character.");
		    return "play/addTokens";
		}
		
		logger.debug("Create Form: "+characterForm.toString());
		Character character = new Character(0, account, characterForm.isHardcore(), characterForm.isIronborn(), 
				StringTools.dbTidy(characterForm.getName()), 
				characterForm.getCharClass(), 1/*level*/, 0/*xp*/, 0/*prestige*/, null /* Death time */, null /* Dungeon */);
		logger.debug("Character: "+character.toString());
		character = characterService.create(character);
		logger.debug("Character Id: "+character.getId());
		// Create equipment
		Map<CharSlot, Equipment> characterSlots = new HashMap<CharSlot, Equipment>();
		try {
			equipmentService.generateStarterEquipment(character, characterSlots);
		} catch (EquipmentNotFound e) {
			logger.error("Error creating starter equipment for character {}: {}", character.getId(), e.getMessage());
			model.addAttribute("charId", -1);
			model.addAttribute("dungeonId", 0);
            logger.error("Error: {}", e.getMessage());
			return "play/index";
		}
		CharacterEquipment characterEquipment = new CharacterEquipment(character, characterSlots );
		characterEquipmentService.create(characterEquipment );

		List<Achievement> newAchievements = achievementService.newCharacter(request.getContextPath(), character);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
		
		model.addAttribute("charId", character.getId());
		model.addAttribute("dungeonId", 0);
		
		return "play/index";
	}
	
	/**
	 * Level up a character
	 */
	@RequestMapping(value = "/{charId}/levelUp", method = RequestMethod.GET)
	public String levelUpCharacter(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long charId) {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Character character = characterService.findById(charId);
		
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			// Whatever it was before is as good as we can do
			setUpViewTypeModel(model);
			model.addAttribute("error", "Character not found");
			return "play/index";
		}
		hardcore = character.isHardcore();
		ironborn = character.isIronborn();
		setUpViewTypeModel(model);
		
		if(character.isCurrentlyDead()) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			model.addAttribute("error", "That character is dead.");
			return "play/index";
		}
		
		if(character.isIronborn()) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			model.addAttribute("error", "That character is Ironborn, you can't pay to level Ironborn characters.");
			return "play/index";
		}
		
		int newLevel = character.getLevel() + 1;
		String reference = "levelup-char-"+account.getUsername()+"-"+charId+"-"+newLevel;			
		if(!accountCurrencyService.adjustCurrency(account, character.isHardcore(), character.isIronborn(), 
				-newLevel, ModificationType.SPEND_LEVELUP, reference)) {
			model.addAttribute("error", 
					"Not enough dungeon tokens to level up character. You need " + newLevel + " tokens.");
		    return "play/addTokens";
		}
	
		
		character.setLevel(newLevel);
		character.setUsedLevelUp(true);
		try {
			characterService.update(character);
		} catch (CharacterNotFound e) {
			logger.error("Error updating character: {}", e.getMessage());
			e.printStackTrace();
			reference = "levelup-char-fail-"+account.getUsername()+"-"+charId+"-"+newLevel;			
			accountCurrencyService.adjustCurrency(account, character.isHardcore(), character.isIronborn(), 
					newLevel, ModificationType.SPEND_LEVELUP, reference);			
		}
		accountMessageService.messageLevelUp(request.getContextPath(), character.getId(), account, character);
		List<Achievement> newAchievements = achievementService.levelUp(request.getContextPath(), character);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
		
		model.addAttribute("charId", charId);
		model.addAttribute("dungeonId", 0);
		
		return "play/index";
	}
	
	/**
	 * Level up a character
	 * @throws InventoryNotFound 
	 * @throws AccountIdMismatch 
	 * @throws DungeonNotFound 
	 * @throws EquipmentNotFound 
	 * @throws BoostItemNotFound 
	 */	
	@RequestMapping(value = "/{charId}/levelUp/{boostId}", method = RequestMethod.GET)
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BoostItemNotFound.class, EquipmentNotFound.class, DungeonNotFound.class, AccountIdMismatch.class, InventoryNotFound.class})
	public String levelUpCharacter(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long charId,@PathVariable long boostId) 
					throws BoostItemNotFound, EquipmentNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Character character = characterService.findById(charId);
		
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			// Whatever it was before is as good as we can do
			setUpViewTypeModel(model);
			model.addAttribute("error", "Character not found");
			return "play/index";
		}
		hardcore = character.isHardcore();
		ironborn = character.isIronborn();
		setUpViewTypeModel(model);
		
		if(character.isCurrentlyDead()) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			model.addAttribute("error", "That character is dead.");
			return "play/index";
		}
		
		if(character.isIronborn()) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			model.addAttribute("error", "That character is Ironborn, you can't pay to level Ironborn characters.");
			return "play/index";
		}
		
		BoostItem boostItem = boostItemService.findById(boostId);
		if(boostItem == null || !boostItem.getAccount().equals(account) && boostItem.isHardcore() == hardcore) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Boost item not found");
			return "play/index";
		}
		
		if(boostItem.getBoostItemType() != BoostItemType.LEVEL_UP) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Wrong boost item type");
			return "play/index";
		}
		
		if(boostItem.getLevel() < character.getLevel()) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Boost item not high enough level");
			return "play/index";
		}
		
		if(boostItem.isHardcore() != hardcore || boostItem.isIronborn() != ironborn) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Boost item not for this mode");
			return "play/index";
		}
		
		int newLevel = character.getLevel() + 1;
		
		character.setLevel(newLevel);
		character.setUsedLevelUp(true);
		try {
			characterService.update(character);
			boostItemService.delete(account, boostItem);
		} catch (CharacterNotFound e) {
			logger.error("Error updating character: {}", e.getMessage());
			e.printStackTrace();
		}
		accountMessageService.messageLevelUp(request.getContextPath(), character.getId(), account, character);
		List<Achievement> newAchievements = achievementService.levelUp(request.getContextPath(), character);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
		
		model.addAttribute("charId", charId);
		model.addAttribute("dungeonId", 0);
		
		return "play/index";
	}
	
	/**
	 * Reset Character for Prestige levels
	 */
	@RequestMapping(value = "/{charId}/prestige", method = RequestMethod.GET)
	public String resetCharacterForPrestige(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long charId) {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Character character = characterService.findById(charId);
		
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			// Whatever it was before is as good as we can do
			model.addAttribute("hardcore", hardcore);
			model.addAttribute("ironborn", ironborn);
			model.addAttribute("error", "Character not found");
			return "play/index";
		}
		hardcore = character.isHardcore();
		ironborn = character.isIronborn();
		setUpViewTypeModel(model);
		
		if(character.isCurrentlyDead()) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			model.addAttribute("error", "That character is dead.");
			return "play/index";
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
			model.addAttribute("charId", 0);
			model.addAttribute("dungeonId", 0);
			model.addAttribute("error", "Character not found");
			return "play/index";
		} catch (EquipmentNotFound e) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			model.addAttribute("error", "Equipment not found");
			return "play/index";
		}
		List<Achievement> newAchievements = achievementService.prestigeLevel(request.getContextPath(), character);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}
		
		model.addAttribute("charId", charId);
		model.addAttribute("dungeonId", 0);
		
		return "play/index";
	}
	
	/**
	 * Resurrect a dead character
	 */
	@RequestMapping(value = "/{charId}/resurrect", method = RequestMethod.GET)
	public String resurrectCharacter(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long charId) {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		
		Character character = characterService.findById(charId);
		
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Character not found");
			return "play/index";
		}
		hardcore = character.isHardcore();
		ironborn = character.isIronborn();
		setUpViewTypeModel(model);
		if(character.getDungeon() != null) {
			// Can't Resurrect characters in dungeons, else they incorrectly get XP when it is closed
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", character.getDungeon().getId());
			model.addAttribute("error", "You need to close the dungeon they are in before you can resurrect them");
			return "play/index";
		}
		if(hardcore) {
			// Can't Resurrect hardcore characters
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", character.getDungeon().getId());
			model.addAttribute("error", "This character is hardcore, sorry but you can't resurrect them.");
			return "play/index";
		}
		long timestamp = System.currentTimeMillis();
		String reference = "resurrect-char-"+account.getUsername()+"-"+charId+"-"+timestamp;			
		if(!accountCurrencyService.adjustCurrency(account,  character.isHardcore(), character.isIronborn(), 
				-character.getLevel(), ModificationType.SPEND_RESURRECT, reference)) {
			model.addAttribute("error", 
					"Not enough dungeon tokens to resurrect character. You need 1 token.");
		    return "play/addTokens";
		}
		
		if(!character.isCurrentlyDead()) {
			model.addAttribute("error", "That character is not dead.");
			if(!prepareCharacterDetailsModelAttributes(model, character)) {
				return getCharacterDetails(model, principle, charId);
			}
            return "play/characterDetails";
		}
		character.setAlive();
		try {
			characterService.update(character);
		} catch (CharacterNotFound e) {
			logger.error("Error updating character: {}", e.getMessage());
			e.printStackTrace();
			// Try to return the currency to the user
			reference = "resurrect-char-fail-"+account.getUsername()+"-"+charId+"-"+timestamp;			
			accountCurrencyService.adjustCurrency(account, character.isHardcore(), character.isIronborn(),
					character.getLevel(), ModificationType.SPEND_RESURRECT, reference);
		}
		
		List<Achievement> newAchievements = achievementService.resurrected(request.getContextPath(), character);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}

		model.addAttribute("charId", charId);
		model.addAttribute("dungeonId", 0);
		
		return "play/index";
	}
	
	
	/**
	 * Resurrect a dead character using a boost item
	 * @throws InventoryNotFound 
	 * @throws AccountIdMismatch 
	 * @throws DungeonNotFound 
	 * @throws EquipmentNotFound 
	 * @throws BoostItemNotFound 
	 */
	@RequestMapping(value = "/{charId}/resurrect/{boostId}", method = RequestMethod.GET)
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BoostItemNotFound.class, EquipmentNotFound.class, DungeonNotFound.class, AccountIdMismatch.class, InventoryNotFound.class})
	public String resurrectCharacter(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long charId, @PathVariable long boostId) 
					throws BoostItemNotFound, EquipmentNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		
		Character character = characterService.findById(charId);
		
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Character not found");
			return "play/index";
		}
		hardcore = character.isHardcore();
		ironborn = character.isIronborn();
		setUpViewTypeModel(model);
		if(character.getDungeon() != null) {
			// Can't Resurrect characters in dungeons, else they incorrectly get XP when it is closed
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", character.getDungeon().getId());
			model.addAttribute("error", "You need to close the dungeon they are in before you can resurrect them");
			return "play/index";
		}
		if(hardcore) {
			// Can't Resurrect hardcore characters
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", character.getDungeon().getId());
			model.addAttribute("error", "This character is hardcore, sorry but you can't resurrect them.");
			return "play/index";
		}
		
		BoostItem boostItem = boostItemService.findById(boostId);
		if(boostItem == null || !boostItem.getAccount().equals(account) && boostItem.isHardcore() == hardcore) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Boost item not found");
			return "play/index";
		}
		
		if(boostItem.getBoostItemType() != BoostItemType.RESURRECTION) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Wrong boost item type");
			return "play/index";
		}
		
		if(boostItem.getLevel() < character.getLevel()) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Boost item not high enough level");
			return "play/index";
		}
		
		if(boostItem.isHardcore() != hardcore || boostItem.isIronborn() != ironborn) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Boost item not for this mode");
			return "play/index";
		}
				
		if(!character.isCurrentlyDead()) {
			model.addAttribute("error", "That character is not dead.");
			if(!prepareCharacterDetailsModelAttributes(model, character)) {
				return getCharacterDetails(model, principle, charId);
			}
            return "play/characterDetails";
		}
		character.setAlive();
		try {
			characterService.update(character);
			boostItemService.delete(account, boostItem);
		} catch (CharacterNotFound e) {
			logger.error("Error updating character: {}", e.getMessage());
			e.printStackTrace();
		}
		
		List<Achievement> newAchievements = achievementService.resurrected(request.getContextPath(), character);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}

		model.addAttribute("charId", charId);
		model.addAttribute("dungeonId", 0);
		
		return "play/index";
	}
	/**
	 * Resurrect all dead characters
	 */
	@RequestMapping(value = "/resurrectAll", method = RequestMethod.GET)
	public String resurrectAllCharacters(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@ModelAttribute("viewTypeForm") @Valid ViewTypeForm viewTypeForm, BindingResult bindingResult) {
		logger.debug("Resurrect All Characters");
		setUpViewType(viewTypeForm, model);
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
	
		if(hardcore) {
			// Can't Resurrect hardcore characters
			model.addAttribute("error", "You are in hardcore mode, sorry but you can't resurrect characters.");
			return getCharacters(locale, model, principle, viewTypeForm, bindingResult);
		}
		
		List<Character> characters = characterService.findAllByAccountOrderByLevel(account, hardcore, ironborn);
		
		model.addAttribute("characters", characters);
		
		int resurrectionCost = 0;
		List<Character> resurrectingCharacters = new ArrayList<Character>();
		for(Character character:characters) {
			if(character.isCurrentlyDead() && character.getDungeon() == null) {
				resurrectingCharacters.add(character);
				resurrectionCost += character.getLevel();
			}
		}
		
		long timestamp = System.currentTimeMillis();
		String reference = "resurrect-allchars-"+account.getUsername();
		if(hardcore) {
			reference += "-hardcore";
		}
		if(ironborn) {
			reference += "-ironborn";
		}
		reference += "-"+timestamp;

		if(!accountCurrencyService.adjustCurrency(account,  hardcore, ironborn, -resurrectionCost, 
				ModificationType.SPEND_RESURRECT, reference)) {
			model.addAttribute("error", 
					"Not enough dungeon tokens to resurrect character. You need 1 token.");
		    return "play/addTokens";
		}
		
		int refundTokens = 0;
		for(Character character:resurrectingCharacters) {
			if(!character.isCurrentlyDead()) {
				refundTokens += character.getLevel();
			} else {
				character.setAlive();
				try {
					characterService.update(character);
				} catch (CharacterNotFound e) {
					logger.error("Error updating character: {}", e.getMessage());
					e.printStackTrace();
					refundTokens += character.getLevel();
				}
			}
		}
		
		if(refundTokens > 0) {
			reference = "REFUND-resurrect-allchars-"+account.getUsername();
			if(hardcore) {
				reference += "-hardcore";
			}
			if(ironborn) {
				reference += "-ironborn";
			}
			reference += "-"+timestamp;

			accountCurrencyService.adjustCurrency(account,  hardcore, ironborn, refundTokens, 
					ModificationType.SPEND_RESURRECT, reference);
		}
		if(resurrectingCharacters.size() > 0) {
			List<Achievement> newAchievements = 
					achievementService.resurrected(request.getContextPath(), resurrectingCharacters.get(0));
			int newPoints = 0;
			for(Achievement achievement:newAchievements) {
				newPoints += achievement.getPoints();
			}
			if(newPoints > 0) {
				hiscoreService.achievement(account, newPoints);
			}
		}
		model.addAttribute("charId", -1);
		model.addAttribute("dungeonId", -1);
		return "play/index";
	}

	/**
	 * Delete a dead hardcore character
	 */
	@RequestMapping(value = "/{charId}/delete", method = RequestMethod.GET)
	public String deleteCharacter(Locale locale, Model model, Principal principle, 
			@PathVariable long charId) {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		
		Character character = characterService.findById(charId);
		
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Character not found");
			return "play/index";
		}
		hardcore = character.isHardcore();
		ironborn = character.isIronborn();
		setUpViewTypeModel(model);
		if(character.getDungeon() != null) {
			// Can't delete characters in dungeons
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", character.getDungeon().getId());
			model.addAttribute("error", "You need to close the dungeon they are in before you can delete them");
			return "play/index";
		}
		if(!hardcore) {
			// Can only delete dead hardcore characters
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", character.getDungeon().getId());
			model.addAttribute("error", "This character is hardcore, sorry but you can't resurrect them.");
			return "play/index";
		}
		
		if(!character.isCurrentlyDead()) {
			model.addAttribute("error", "That character is not dead, only dead hardcore characters can be deleted.");
			if(!prepareCharacterDetailsModelAttributes(model, character)) {
				return getCharacterDetails(model, principle, charId);
			}
            return "play/characterDetails";
		}
		try {
			characterService.delete(character);
		} catch (CharacterNotFound e) {
			logger.error("Error updating character: {}", e.getMessage());
			e.printStackTrace();
		} catch (CharacterEquipmentNotFound e) {
			logger.error("Error updating character: {}", e.getMessage());
			e.printStackTrace();
		} catch (CharacterSlotNotFound e) {
			logger.error("Error updating character: {}", e.getMessage());
			e.printStackTrace();
		}
		
		model.addAttribute("charId", charId);
		model.addAttribute("dungeonId", 0);
		
		return "play/index";
	}
	
	/**
	 * Show the rename a character form
	 * @throws InventoryNotFound 
	 * @throws AccountIdMismatch 
	 * @throws DungeonNotFound 
	 * @throws EquipmentNotFound 
	 * @throws BoostItemNotFound 
	 */	
	@RequestMapping(value = "/{charId}/rename/{boostId}", method = RequestMethod.GET)
	public String renameCharacterForm(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long charId, @PathVariable long boostId)  {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Character character = characterService.findById(charId);
		
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("error", "Character not found.");
			return getCharacterDetails(model, principle, charId);
		}
		hardcore = character.isHardcore();
		ironborn = character.isIronborn();
		setUpViewTypeModel(model);
		
		if(character.isCurrentlyDead()) {
			model.addAttribute("error", "That character is dead.");
			return getCharacterDetails(model, principle, charId);
		}
		
		BoostItem boostItem = boostItemService.findById(boostId);
		if(boostItem == null || !boostItem.getAccount().equals(account) && boostItem.isHardcore() == hardcore) {
			model.addAttribute("error", "Boost item not found");
			return getCharacterDetails(model, principle, charId);
		}
		
		if(boostItem.getBoostItemType() != BoostItemType.CHANGE_NAME) {
			model.addAttribute("error", "Wrong boost item type");
			return getCharacterDetails(model, principle, charId);
		}
		
		if(boostItem.getLevel() < character.getLevel()) {
			model.addAttribute("error", "Boost item not high enough level");
			return getCharacterDetails(model, principle, charId);
		}
		
		if(boostItem.isHardcore() != hardcore || boostItem.isIronborn() != ironborn) {
			model.addAttribute("error", "Boost item not for this mode");
			return getCharacterDetails(model, principle, charId);
		}
		model.addAttribute("character", character);
		model.addAttribute("boostId", boostId);
		
		return "play/renameCharacter";
	}
	
	/**
	 * Rename a character do
	 * @throws InventoryNotFound 
	 * @throws AccountIdMismatch 
	 * @throws DungeonNotFound 
	 * @throws EquipmentNotFound 
	 * @throws BoostItemNotFound 
	 */	
	@RequestMapping(value = "/{charId}/rename/{boostId}", method = RequestMethod.POST)
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BoostItemNotFound.class, EquipmentNotFound.class, DungeonNotFound.class, AccountIdMismatch.class, InventoryNotFound.class})
	public String renameCharacterDo(HttpServletRequest request, Locale locale, Model model, Principal principle, 
			@PathVariable long charId, @PathVariable long boostId,
			@ModelAttribute("renameCharacterForm") @Validated RenameCharacterForm renameCharacterForm, BindingResult bindingResult) 
					throws BoostItemNotFound, EquipmentNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Character character = characterService.findById(charId);
		
		if(character == null || !character.getAccount().equals(account)) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			// Whatever it was before is as good as we can do
			setUpViewTypeModel(model);
			model.addAttribute("error", "Character not found");
			return "play/index";
		}
		
		hardcore = character.isHardcore();
		ironborn = character.isIronborn();
		setUpViewTypeModel(model);
		
		if(renameCharacterForm.getId() != charId || renameCharacterForm.getAccountId() != account.getId() || 
				renameCharacterForm.isHardcore() != hardcore || renameCharacterForm.isIronborn() != ironborn) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			// Whatever it was before is as good as we can do
			model.addAttribute("error", "Character details in form do not match real character details");
			return "play/index";
		}
		
		if(character.isCurrentlyDead()) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			model.addAttribute("error", "That character is dead.");
			return "play/index";
		}
		
		BoostItem boostItem = boostItemService.findById(boostId);
		if(boostItem == null || !boostItem.getAccount().equals(account) && boostItem.isHardcore() == hardcore) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Boost item not found");
			return "play/index";
		}
		
		if(boostItem.getBoostItemType() != BoostItemType.CHANGE_NAME) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Wrong boost item type");
			return "play/index";
		}
		
		if(boostItem.getLevel() < character.getLevel()) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Boost item not high enough level");
			return "play/index";
		}
		
		if(boostItem.isHardcore() != hardcore || boostItem.isIronborn() != ironborn) {
			model.addAttribute("charId", charId);
			model.addAttribute("dungeonId", 0);
			setUpViewTypeModel(model);
			model.addAttribute("error", "Boost item not for this mode");
			return "play/index";
		}

		character.setName(renameCharacterForm.getName());
		try {
			characterService.update(character);
			boostItemService.delete(account, boostItem);
		} catch (CharacterNotFound e) {
			logger.error("Error updating character: {}", e.getMessage());
			e.printStackTrace();
		}
		
		model.addAttribute("charId", charId);
		model.addAttribute("dungeonId", 0);
		
		return "play/index";
	}
	
	/**
	 * Set up the model attributes for the play/characterDetails page view
	 * 
	 * @param model
	 * @param character
	 */
	private boolean prepareCharacterDetailsModelAttributes(Model model, Character character) {
		Map<CharSlot, Equipment> charEquipment = characterEquipmentService.findAllByCharacter(character);

		if(charEquipment == null) {
			model.addAttribute("error", "Character had no equipment. Created empty character.");
			Map<CharSlot, Equipment> characterSlots = new HashMap<CharSlot, Equipment>();
			CharacterEquipment characterEquipment = new CharacterEquipment(character, characterSlots );
			characterEquipmentService.create(characterEquipment );
			return false;
		}
		
		model.addAttribute("characterId", character.getId());
		Map<String, Equipment> webCharEquipment = new HashMap<String, Equipment>();
		for(CharSlot slot:CharSlot.values()) {
			Equipment equipment = charEquipment.get(slot);
			if(equipment != null) {
				logger.debug(slot.getNiceName() + "=>" + equipment.toString());
				webCharEquipment.put(slot.getName(), equipment);
			} else {
				logger.debug(slot.getNiceName() + "=>EMPTY");
				Map<Integer, Integer> attributes = new HashMap<Integer, Integer>();
				webCharEquipment.put(slot.getName(), new Equipment(-1, 
						EquipmentType.UNKNOWN, EquipmentQuality.USELESS, 
						0, false, false,
						null/*baseAttribute*/, 0/*baseAttributeValue*/, 
						null/*defenceAttribute*/, 0/*defenceAttributeValue*/, 
						attributes, 
						null/*ancientAttribute*/, 0/*ancientAttributeValue*/, 
						EquipmentLocation.NONE, -1));
			}
		}
		model.addAttribute("character", character);
		model.addAttribute("characterEquipment", webCharEquipment);
		
		return true;
	}

	/**
	 * Set up the model attributes for the play/characterDetailsSummary page view
	 * 
	 * @param model
	 * @param character
	 */
	private boolean prepareCharacterDetailsSummaryModelAttributes(Model model, Character character) {
		Map<CharSlot, Equipment> charEquipment = characterEquipmentService.findAllByCharacter(character);

		if(charEquipment == null) {
			model.addAttribute("error", "Character had no equipment. Created empty character.");
			Map<CharSlot, Equipment> characterSlots = new HashMap<CharSlot, Equipment>();
			CharacterEquipment characterEquipment = new CharacterEquipment(character, characterSlots );
			characterEquipmentService.create(characterEquipment );
			return false;
		}
		
		PowerValues powerValues = new PowerValues();
		Map<EquipmentAttribute, Integer> characterSummary = new TreeMap<EquipmentAttribute, Integer>();
		for(CharSlot slot:CharSlot.values()) {
			Equipment equipment = charEquipment.get(slot);
			if(equipment != null) {
				CommonTools.calculatePowerValues(powerValues, equipment, characterSummary, character);
			}
		}
		model.addAttribute("characterSummary", characterSummary);
		int level = character.getLevel();
		model.addAttribute("attackValue", (powerValues.attackValue+powerValues.classSpecificAttackValue) / level);
		model.addAttribute("defenceValue", (powerValues.defenceValue+powerValues.classSpecificDefenceValue) / level);
		model.addAttribute("recoveryValue", powerValues.recoveryValue / level);
		
		return true;
	}
	
	private void prepareCreateCharacterModelAttributes(Model model, Account account) {
		List<Character> accountCharacters = characterService.findAllByAccountOrderByLevel(account, hardcore, ironborn);
		int numberOfCharacters = 1;
		if(accountCharacters != null) {
			numberOfCharacters = accountCharacters.size();
		}
		model.addAttribute("numberOfCharacters", numberOfCharacters);
		model.addAttribute("characterForm", new CharacterForm());
	}
}
