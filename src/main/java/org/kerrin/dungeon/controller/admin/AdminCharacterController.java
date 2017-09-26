package org.kerrin.dungeon.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.CharacterForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.forms.validator.CharacterFormValidator;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping(value="/admin/character")
public class AdminCharacterController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminCharacterController.class);
	
	private final CharacterService characterService;
	private final EquipmentService equipmentService;
	private final AccountService accountService;
	private final CharacterEquipmentService characterEquipmentService;
	private final CharacterFormValidator characterFormValidator;
	private final DungeonService dungeonService;
	
	@Autowired
	public AdminCharacterController(CharacterService characterService, 
			EquipmentService equipmentService, CharacterEquipmentService chararcterEquipmentService, 
			AccountService accountService, CharacterFormValidator characterFormValidator, DungeonService dungeonService) {
		super();
		this.characterService = characterService;
		this.equipmentService = equipmentService;
		this.characterEquipmentService = chararcterEquipmentService;
		this.accountService = accountService;
		this.characterFormValidator = characterFormValidator;
		this.dungeonService = dungeonService;
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
	public String getCharacter(Model model, @PathVariable long charId) {
		logger.debug("Character Details");
		Character character = characterService.findById(charId);
		
		if(character == null) {
			model.addAttribute("characters", new ArrayList<Character>());
			model.addAttribute("characterForm", new CharacterForm());
			model.addAttribute("error", "Character not found");
			return "admin/searchCharacters";
		}
		
		model.addAttribute("characterForm", new CharacterForm(character));
		
		return "admin/modifyCharacter";
	}
	
	/**
	 * Update a character details
	 */
	@RequestMapping(value = "/{charId}", method = RequestMethod.POST)
	public String updateCharacter(Model model, @PathVariable long charId, 
			@ModelAttribute("characterForm") @Validated CharacterForm characterForm, BindingResult bindingResult) {		
		logger.debug("Update Form: "+characterForm.toString());
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "Form Has Errors");
            return "admin/modifyCharacter";
        }
		
		Character character = characterService.findById(charId);
		Date deadDate = character.getDeathTime();
		if((deadDate != null) != characterForm.isCurrentlyDead()) {
			// Change of dead status
			if(deadDate == null) {
				deadDate = new Date();
			} else {
				deadDate = null;
			}
		}
		
		Account account = accountService.findById(characterForm.getAccountId());
		if(account == null) {
			model.addAttribute("error", "Error finding account id: "+characterForm.getAccountId());
			return "admin/modifyCharacter";
		}
		
		Dungeon dungeon = dungeonService.findById(characterForm.getDungeonId());
		
		Character updatedCharacter = new Character(characterForm.getId(), account,
				characterForm.isHardcore(), characterForm.isIronborn(), 
				characterForm.getName(), characterForm.getCharClass(), 
				characterForm.getLevel(), characterForm.getXp(), characterForm.getPrestigeLevel(), 
				deadDate, dungeon);
		
		if(updatedCharacter.getId() == charId) {
			try {
				characterService.update(updatedCharacter);
			} catch (CharacterNotFound e) {
				model.addAttribute("error", "Error updating character: "+e.getMessage());
			}
		} else {
			model.addAttribute("error", "Character ID changed from " + charId + " to " + updatedCharacter.getId() + " update cancelled!");
		}
		
		model.addAttribute("characterForm", new CharacterForm(updatedCharacter));
		
		return "admin/modifyCharacter";
	}
	
	/**
	 * Create a character
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String createCharacter(Locale locale, Model model, 
			@ModelAttribute("characterForm") @Validated CharacterForm characterForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			logger.debug("Has errors");
            return "admin/addCharacter";
        }
		logger.debug("Create Form: "+characterForm.toString());
		Account account = accountService.findById(characterForm.getAccountId());
		if(account == null) {
			model.addAttribute("error", "Error finding account id: "+characterForm.getAccountId());
			return "admin/addCharacter";
		}
		Character character = new Character(0, account,
				characterForm.isHardcore(), characterForm.isIronborn(), 
				characterForm.getName(), characterForm.getCharClass(), 
				characterForm.getLevel(), characterForm.getXp(), characterForm.getPrestigeLevel(), 
				(characterForm.isCurrentlyDead()?new Date():null), null);
		logger.debug("Character: "+character.toString());
		character = characterService.create(character);
		logger.debug("Character Id: "+character.getId());
		model.addAttribute("characterForm", new CharacterForm(character));
		
		// Create equipment
		Map<CharSlot, Equipment> characterSlots = new HashMap<CharSlot, Equipment>();
		try {
			equipmentService.generateStarterEquipment(character, characterSlots);
		} catch (EquipmentNotFound e) {
			logger.error("Error creating starter equipment for character {}: {}", character.getId(), e.getMessage());
			return "admin/addCharacter";
		}
		CharacterEquipment characterEquipment = new CharacterEquipment(character, characterSlots);
		characterEquipmentService.create(characterEquipment );
		
		return "admin/modifyCharacter";
	}
	
	/**
	 * Search Character Equipment
	 * 
	 * @param request
	 * @param model
	 * @param characterId
	 * @return
	 */
	@RequestMapping(value="/{characterId}/equipment", method = RequestMethod.GET)
	public String get(HttpServletRequest request, Model model, @PathVariable long characterId) {
		logger.trace("Character Equipment Search");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		
		Character character = characterService.findById(characterId);
		if(character == null) {
			model.addAttribute("error", "Error finding character id: "+characterId);
			return "admin/modifyCharacter";
		}
		
		Map<CharSlot, Equipment> charEquipment = characterEquipmentService.findAllByCharacter(character);

		if(charEquipment == null) {
			model.addAttribute("error", "Character had no equipment. Created empty character.");
			Map<CharSlot, Equipment> characterSlots = new HashMap<CharSlot, Equipment>();
			CharacterEquipment characterEquipment = new CharacterEquipment(character, characterSlots );
			characterEquipmentService.create(characterEquipment );
			return getCharacter(model, characterId);
		}
		
		Map<String, Equipment> webCharEquipment = new HashMap<String, Equipment>();
		for(CharSlot slot:CharSlot.values()) {
			Equipment equipment = charEquipment.get(slot);
			if(equipment != null) {
				logger.debug(slot.getNiceName() + "=>" + equipment.toString());
				webCharEquipment.put(slot.getName(), equipment);
			} else {
				logger.debug(slot.getNiceName() + "=>EMPTY");
				Map<Integer, Integer> attributes = new HashMap<Integer, Integer>();
				webCharEquipment.put(slot.getName(), new Equipment( 
						-1, EquipmentType.UNKNOWN, EquipmentQuality.USELESS, 
						0, character.isHardcore(), character.isIronborn(), 
						null/*baseAttribute*/, 0/*baseAttributeValue*/, 
						null/*defenceAttribute*/, 0/*defenceAttributeValue*/, 
						attributes, 
						null/*ancientAttribute*/, 0/*ancientAttributeValue*/, 
						EquipmentLocation.NONE, -1));
			}
		}
		model.addAttribute("characterId", character.getId());
		model.addAttribute("hardcore", character.isHardcore());
		model.addAttribute("ironborn", character.isIronborn());
		model.addAttribute("characterEquipment", webCharEquipment);
		
		return "admin/viewCharacterEquipment";
	}

	@ModelAttribute("characterClasses")
    public CharClass[] getCharacterClasses() {
		return CharClass.values();
    }
}
