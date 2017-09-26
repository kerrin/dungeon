package org.kerrin.dungeon.controller;

import java.security.Principal;
import java.util.Locale;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.Spell;
import org.kerrin.dungeon.forms.AttributeForm;
import org.kerrin.dungeon.forms.CharClassForm;
import org.kerrin.dungeon.forms.MonsterForm;
import org.kerrin.dungeon.forms.SpellForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.task.DungeonProcessorTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/dungeonpedia")
public class DungeonPediaController {
	
	private static final Logger logger = LoggerFactory.getLogger(DungeonPediaController.class);
	private AccountService accountService;
	
	@Autowired
	public DungeonPediaController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/index";
	}
	
	@RequestMapping(value="/account", method = RequestMethod.GET)
	public String account(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/account";
	}
	
	@RequestMapping(value="/character", method = RequestMethod.GET)
	public String character(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/character";
	}
	
	@RequestMapping(value="/character/characters", method = RequestMethod.GET)
	public String characterCharacters(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/character/characters";
	}
	
	@RequestMapping(value="/character/createCharacter", method = RequestMethod.GET)
	public String characterCreateCharacter(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/character/createCharacter";
	}
	
	@RequestMapping(value="/character/characterDetails", method = RequestMethod.GET)
	public String characterCharacterDetails(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/character/characterDetails";
	}
	
	@RequestMapping(value="/character/characterClasses", method = RequestMethod.GET)
	public String characterCharacterClasses(Locale locale, Model model, Principal principle) {
		CharClassForm charClassForm = new CharClassForm();
		model.addAttribute("charClassForm", charClassForm);
		
		return "dungeonpedia/character/characterClasses";
	}
	
	@RequestMapping(value="/character/characterClasses/{charClassId}", method = RequestMethod.GET)
	public String characterCharacterClassesSelectClass(Locale locale, Model model, Principal principle, @PathVariable int charClassId) {
		CharClass charClass = CharClass.fromId(charClassId);
		model.addAttribute("charClass", charClass);
		model.addAttribute("monster", Monster.fromCharClass(charClass));
		
		return "dungeonpedia/character/characterClassesDetails";
	}
	
	@RequestMapping(value="/dungeon", method = RequestMethod.GET)
	public String dungeonFrame(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/dungeon";
	}
	
	@RequestMapping(value="/dungeon/dungeon", method = RequestMethod.GET)
	public String dungeon(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/dungeon/dungeon";
	}
	
	@RequestMapping(value="/dungeon/dungeon/general", method = RequestMethod.GET)
	public String dungeonGeneral(Locale locale, Model model, Principal principle) {
		model.addAttribute("dungeonAdventure", DungeonType.ADVENTURE);
		model.addAttribute("dungeonDungeon", DungeonType.DUNGEON);
		model.addAttribute("dungeonRaid", DungeonType.RAID);
		model.addAttribute("baseMagicFind", DungeonProcessorTask.BASE_MAGIC_FIND);
		model.addAttribute("foundItemChances", DungeonProcessorTask.FOUND_EQUIPMENT_CHANCES);
		model.addAttribute("boostItemDropChance", DungeonProcessorTask.FIND_BOOST_ITEM_CHANCE);
		
		return "dungeonpedia/dungeon/dungeonGeneral";
	}
	
	@RequestMapping(value="/dungeon/dungeon/adventures", method = RequestMethod.GET)
	public String dungeonAdventures(Locale locale, Model model, Principal principle) {
		model.addAttribute("dungeonAdventure", DungeonType.ADVENTURE);
				
		return "dungeonpedia/dungeon/dungeonAdventures";
	}
	
	@RequestMapping(value="/dungeon/dungeon/dungeons", method = RequestMethod.GET)
	public String dungeonDungeons(Locale locale, Model model, Principal principle) {
		model.addAttribute("dungeonDungeon", DungeonType.DUNGEON);
		
		return "dungeonpedia/dungeon/dungeonDungeons";
	}
	
	@RequestMapping(value="/dungeon/dungeon/raids", method = RequestMethod.GET)
	public String dungeonRaids(Locale locale, Model model, Principal principle) {
		model.addAttribute("dungeonRaid", DungeonType.RAID);
		
		return "dungeonpedia/dungeon/dungeonRaids";
	}
	
	@RequestMapping(value="/dungeon/monsters", method = RequestMethod.GET)
	public String monsters(Locale locale, Model model, Principal principle) {
		model.addAttribute("monsterForm", new MonsterForm());
		model.addAttribute("maxLevel", getAllowedLevel(principle));
		
		return "dungeonpedia/dungeon/monsters";
	}
	
	@RequestMapping(value="/dungeon/monsters/{monsterId}", method = RequestMethod.GET)
	public String monstersSelectMonster(Locale locale, Model model, Principal principle, @PathVariable int monsterId) {
		MonsterForm monsterForm = new MonsterForm();
		monsterForm.setMonster(Monster.fromId(monsterId));
		model.addAttribute("monsterForm", monsterForm);
		model.addAttribute("maxLevel", getAllowedLevel(principle));
		
		return "dungeonpedia/dungeon/monsters";
	}

	/**
	 * Calculate the maximum level a user would see as a dungeon
	 * If the user is not logged on, just return the maximum level for a max level user
	 * @param principle
	 * @return
	 */
	private int getAllowedLevel(Principal principle) {
		Account account = accountService.findByPrinciple(principle);
		int accountLevel = 60;
		if(account != null) {
			accountLevel = Math.max(
					Math.max(
						accountService.getMaxCharacterLevel(account, false, false),
						accountService.getMaxCharacterLevel(account, true, false)
						),
					Math.max(
						accountService.getMaxCharacterLevel(account, true, false),
						accountService.getMaxCharacterLevel(account, true, true)
						)
				);
		}
		int allowLevel = accountLevel + (accountLevel / 5);
		return allowLevel;
	}
	
	@RequestMapping(value="/dungeon/monsters/{monsterId}/monsterDetails/{level}", method = RequestMethod.GET)
	public String monstersShowDetails(Locale locale, Model model, Principal principle, 
			@PathVariable int monsterId, @PathVariable int level) {
		model.addAttribute("monster", Monster.fromId(monsterId));
		model.addAttribute("level", level);
		
		return "dungeonpedia/dungeon/monsterDetails";
	}
	
	@RequestMapping(value="/equipment", method = RequestMethod.GET)
	public String equipmentFrame(Locale locale, Model model, Principal principle) {
		
		return "dungeonpedia/equipment";
	}
	
	@RequestMapping(value="/equipment/equipment", method = RequestMethod.GET)
	public String equipment(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/equipment/equipment";
	}
	
	@RequestMapping(value="/equipment/enchanting", method = RequestMethod.GET)
	public String enchanting(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/equipment/enchanting";
	}
	
	@RequestMapping(value="/equipment/attributes", method = RequestMethod.GET)
	public String attributes(Locale locale, Model model, Principal principle) {
		model.addAttribute("attributeForm", new AttributeForm());
		
		return "dungeonpedia/equipment/attributes";
	}
	
	@RequestMapping(value="/equipment/attributes/{attributeId}", method = RequestMethod.GET)
	public String atributeDetails(Locale locale, Model model, Principal principle, @PathVariable int attributeId) {
		EquipmentAttribute attribute = EquipmentAttribute.fromId(attributeId);
		model.addAttribute("attribute", attribute);
		
		return "dungeonpedia/equipment/attributeDetails";
	}
	
	@RequestMapping(value="/equipment/stash", method = RequestMethod.GET)
	public String stash(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/equipment/stash";
	}
	
	@RequestMapping(value="/equipment/salvaging", method = RequestMethod.GET)
	public String salvaging(Locale locale, Model model, Principal principle) {
		
		
		return "dungeonpedia/equipment/salvaging";
	}
	
	@RequestMapping(value="/equipment/boostItem", method = RequestMethod.GET)
	public String boostItems(Locale locale, Model model, Principal principle) {
		model.addAttribute("boostItemDropChance", DungeonProcessorTask.FIND_BOOST_ITEM_CHANCE);
		
		return "dungeonpedia/equipment/boostItem";
	}
	
	@RequestMapping(value="/spells", method = RequestMethod.GET)
	public String spells(Locale locale, Model model, Principal principle) {
		SpellForm spellForm = new SpellForm();
		model.addAttribute("spellForm", spellForm);
		
		return "dungeonpedia/spells";
	}
	
	@RequestMapping(value="/spells/{spellId}", method = RequestMethod.GET)
	public String spellsShowDetails(Locale locale, Model model, Principal principle, @PathVariable int spellId) {
		Spell spell = Spell.fromId(spellId);
		model.addAttribute("spell", spell);
		
		return "dungeonpedia/spellsDetails";
	}

	@ModelAttribute("monsters")
    public Monster[] getMonsters() {
		Monster[] monsters = Monster.values();
		Monster[] monstersNoUnknown = new Monster[monsters.length-1];
		for(int i=1; i < monsters.length; i++) monstersNoUnknown[i-1] = monsters[i];
		return monstersNoUnknown;
    }

	@ModelAttribute("charClasses")
    public CharClass[] getCharClasses() {
		CharClass[] charClasses = CharClass.values();
		CharClass[] charClassesNoAny = new CharClass[charClasses.length-1];
		for(int i=1; i < charClasses.length; i++) charClassesNoAny[i-1] = charClasses[i];
		return charClassesNoAny;
    }

	@ModelAttribute("attributes")
    public EquipmentAttribute[] getAttributes() {
		EquipmentAttribute[] attributes = EquipmentAttribute.values();
		EquipmentAttribute[] atttributesNoSparkles = new EquipmentAttribute[attributes.length-1];
		for(int i=1; i < attributes.length; i++) atttributesNoSparkles[i-1] = attributes[i];
		return atttributesNoSparkles;
    }

	@ModelAttribute("spells")
    public Spell[] getSpells() {
		Spell[] spells = Spell.values();
		Spell[] spellsNoSparkles = new Spell[spells.length-1];
		for(int i=1; i < spells.length; i++) spellsNoSparkles[i-1] = spells[i];
		return spellsNoSparkles;
    }

	@ModelAttribute("equipmentQualities")
    public EquipmentQuality[] getEquipmentQualities() {
		EquipmentQuality[] qualities = EquipmentQuality.values();
		EquipmentQuality[] qualitiesNoBroken = new EquipmentQuality[qualities.length-1];
		for(int i=1; i < qualities.length; i++) qualitiesNoBroken[i-1] = qualities[i];
		return qualitiesNoBroken;
    }

	@ModelAttribute("boostItemTypes")
    public BoostItemType[] getBoostItemTypes() {
		BoostItemType[] boostItemTypes = BoostItemType.values();
		BoostItemType[] boostItemTypesNoUnknown = new BoostItemType[boostItemTypes.length-1];
		for(int i=1; i < boostItemTypes.length; i++) boostItemTypesNoUnknown[i-1] = boostItemTypes[i];
		return boostItemTypesNoUnknown;
    }
}
