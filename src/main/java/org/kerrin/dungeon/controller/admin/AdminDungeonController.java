package org.kerrin.dungeon.controller.admin;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.ArraySizeMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.exception.DifferentGameStates;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.DungeonNotRunable;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.DungeonForm;
import org.kerrin.dungeon.forms.DungeonSearchForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.forms.TestDungeonForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonEvent;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.DungeonEventService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/admin/dungeon")
public class AdminDungeonController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminDungeonController.class);
	
	@Autowired
	private DungeonService dungeonService;
	
	@Autowired
	private DungeonEventService dungeonEventService;
	
	@Autowired
	private CharacterService characterService;
	
	@Autowired
	private CharacterEquipmentService characterEquipmentService;
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * Look at the details of a specific dungeon
	 */
	@RequestMapping(value = "/{dungeonId}", method = RequestMethod.GET)
	public String getDungeon(HttpServletRequest request, Model model, @PathVariable long dungeonId) {
		logger.debug("Get Dungeon Details");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Dungeon dungeon = dungeonService.findById(dungeonId);
		
		if(dungeon == null) {
			model.addAttribute("error", "Dungeon Id "+dungeonId+" not found");
			List<Dungeon> dungeons = new ArrayList<Dungeon>();
			model.addAttribute("dungeons", dungeons);
			DungeonSearchForm dungeonSearchForm = new DungeonSearchForm();
			dungeonSearchForm.setId(dungeonId);
			model.addAttribute("dungeonSearchForm", dungeonSearchForm);
			
			return "admin/searchDungeons";
		}
		
		List<DungeonEvent> dungeonEvents = dungeonEventService.findAllByDungeon(dungeon);
		model.addAttribute("dungeon", dungeon);
		model.addAttribute("dungeonEvents", dungeonEvents);
		model.addAttribute("dungeonForm", new DungeonForm(dungeon));
		
		return "admin/modifyDungeon";
	}
	
	/**
	 * Update a dungeon details
	 */
	@RequestMapping(value = "/{dungeonId}", method = RequestMethod.POST)
	public String updateDungeon(HttpServletRequest request, Model model, @PathVariable long dungeonId, @ModelAttribute("dungeonForm") @Valid DungeonForm dungeonForm, BindingResult bindingResult) {		
		logger.debug("Update Form: "+dungeonForm.toString());
		if(!request.isUserInRole(AccountPrivilege.MODIFY.getName())) {
			model.addAttribute("error", "You do not have access to modify data");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("dungeonForm", dungeonForm);
			try {
				model.addAttribute("dungeon", new Dungeon(dungeonForm));
			} catch (ArraySizeMismatch e) {
				model.addAttribute("error", "Form error with monster instances: "+e.getMessage());
			}
			List<DungeonEvent> dungeonEvents = new ArrayList<DungeonEvent>();
			model.addAttribute("dungeonEvents", dungeonEvents);

            return "admin/modifyDungeon";
        }
		Account account = accountService.findById(dungeonForm.getAccountId());
		if(account == null) {
			model.addAttribute("error", "Error finding account id: "+dungeonForm.getAccountId());
			return "admin/modifyDungeon";
		}
		Dungeon dungeon = dungeonService.findById(dungeonForm.getId());
		dungeon.setAccount(account);
		dungeon.setHardcore(dungeonForm.isHardcore());
		dungeon.setIronborn(dungeonForm.isIronborn());
		dungeon.setLevel(dungeonForm.getLevel());
		dungeon.setPartySize(dungeonForm.getPartySize());
		dungeon.setType(dungeonForm.getType());
		dungeon.setXpReward(dungeonForm.getXpReward());
		dungeon.setMonsters(dungeonForm.getMonstersMap());
		
		if(dungeon.getId() == dungeonId) {
			try {
				dungeonService.update(dungeon);
			} catch (DungeonNotFound e) {
				model.addAttribute("error", "Error updating dungeon, dungeon not found: "+e.getMessage());
			} catch (AccountIdMismatch e) {
				model.addAttribute("error", "Error updating dungeon, account Id missmatch: "+e.getMessage());
			} catch (EquipmentNotFound e) {
				model.addAttribute("error", "Error updating dungeon, equipment Id not found: "+e.getMessage());
			} catch (BoostItemNotFound e) {
				model.addAttribute("error", "Error updating dungeon, boost item Id not found: "+e.getMessage());
			}		
		} else {
			model.addAttribute("error", "Dungeon ID changed from " + dungeonId + " to " + dungeon.getId() + " update cancelled!");
		}
		
		
		model.addAttribute("dungeonForm", new DungeonForm(dungeon));
		model.addAttribute("dungeon", dungeon);
		List<DungeonEvent> dungeonEvents = dungeonEventService.findAllByDungeon(dungeon);
		model.addAttribute("dungeonEvents", dungeonEvents);
		
		return "admin/modifyDungeon";
	}
	
	/**
	 * Create a dungeon
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String createDungeon(HttpServletRequest request, Model model, @ModelAttribute("dungeonForm") @Valid DungeonForm dungeonForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			logger.debug("Has errors");
			model.addAttribute("dungeonForm", dungeonForm);
			try {
				Dungeon dungeon = new Dungeon(dungeonForm);
				logger.debug(dungeon.toString());
				model.addAttribute("dungeon", dungeon);
			} catch (ArraySizeMismatch e) {
				logger.error("Dungeon Form error: "+e.getMessage());
				model.addAttribute("dungeon", new Dungeon(-1, DungeonType.NONE, null, false, false, -1, -1, null, -1, -1));
			}
            return "admin/addDungeon";
        }
		logger.debug("Create Form: "+dungeonForm.toString());
		if(!request.isUserInRole(AccountPrivilege.MODIFY.getName())) {
			model.addAttribute("error", "You do not have access to modify data");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Account account = accountService.findById(dungeonForm.getAccountId());
		if(account == null) {
			model.addAttribute("error", "Error finding account id: "+dungeonForm.getAccountId());
			return "admin/addDungeon";
		}
		Dungeon dungeon;
		try {
			dungeon = new Dungeon(-1, dungeonForm.getType(), account, dungeonForm.isHardcore(),dungeonForm.isIronborn(), 
					dungeonForm.getLevel(), dungeonForm.getXpReward(), 
					Dungeon.createMonsterMap(dungeonForm.getMonsters(), dungeonForm.getMonsterTypes()), 
					dungeonForm.getPartySize(),
					0);
			logger.debug("Dungeon: "+dungeon.toString());
			dungeon = dungeonService.create(dungeon);
			List<Equipment> rewardItems = dungeon.generateItemRewards(false, dungeonForm.isHardcore(),dungeonForm.isIronborn());
	        Map<Equipment, Boolean> rewardItemsMap = new HashMap<Equipment, Boolean>();
	        for(Equipment equipment:rewardItems) {
				equipment = equipmentService.create(equipment);
				rewardItemsMap.put(equipment, false);
			}
	        
			dungeon.setItemRewards(rewardItemsMap);
			try {
				dungeon = dungeonService.update(dungeon);
			} catch (DungeonNotFound e) {
				logger.error("Failed to save back dungeon after creating reward items: {}", dungeon);
			} catch (AccountIdMismatch e) {
				logger.error("Failed to save back dungeon after creating reward items: {}", dungeon);
			} catch (EquipmentNotFound e) {
				logger.error("Failed to save back dungeon after creating reward items: {}", dungeon);
			} catch (BoostItemNotFound e) {
				logger.error("Failed to save back dungeon after creating reward items: {}", dungeon);
			}
			logger.debug("Dungeon Id: "+dungeon.getId());
			model.addAttribute("dungeonForm", new DungeonForm(dungeon));
			model.addAttribute("dungeon", dungeon);
		} catch (ArraySizeMismatch e) {
			model.addAttribute("error", "Error updating dungeon: "+e.getMessage());
			model.addAttribute("dungeonForm", dungeonForm);
			try {
				model.addAttribute("dungeon", new Dungeon(dungeonForm));
			} catch (ArraySizeMismatch e2) {
				model.addAttribute("dungeon", new Dungeon(-1, DungeonType.NONE, null, false, false, -1, -1, null, -1, -1));
			}
		}
		
		return "admin/modifyDungeon";
	}
	
	@RequestMapping(value="/test", method = RequestMethod.GET)
	public String showTestDungeon(HttpServletRequest request, Model model, Principal principle) {
		logger.debug("showTestDungeon");
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		model.addAttribute("account", account);
		TestDungeonForm testForm = new TestDungeonForm();
		testForm.setLevel(1);
		testForm.setPartySize(2);
		testForm.setPartyLevel(1);
		testForm.setType(DungeonType.DUNGEON);
		testForm.setRandomMonsters(true);
		model.addAttribute("testDungeonForm", testForm);
		return "admin/addTestDungeon";
	}
		
	/**
	 * Create and run a test dungeon
	 */
	@RequestMapping(value="/test", method = RequestMethod.POST)
	public String runTestDungeon(HttpServletRequest request, Model model, Principal principle, @ModelAttribute("testDungeonForm") @Valid TestDungeonForm testDungeonForm, BindingResult bindingResult) {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		if (bindingResult.hasErrors()) {
			logger.debug("Has errors");
			model.addAttribute("testDungeonForm", testDungeonForm);
			try {
				Dungeon dungeon = new Dungeon(testDungeonForm);
				logger.debug(dungeon.toString());
				model.addAttribute("dungeon", dungeon);
			} catch (ArraySizeMismatch e) {
				logger.error("Dungeon Form error: "+e.getMessage());
				model.addAttribute("dungeon", new Dungeon(-1, DungeonType.NONE, null, false, false, -1, -1, null, -1, -1));
			}
            return "admin/addTestDungeon";
        }
		logger.debug("Create Form: "+testDungeonForm.toString());
		if(!request.isUserInRole(AccountPrivilege.MODIFY.getName())) {
			model.addAttribute("error", "You do not have access to modify data");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Dungeon dungeon;
		try {
			Map<Monster, MonsterType> monsters;
			if(testDungeonForm.isRandomMonsters()) {
				monsters = dungeonService.generateMonsters(testDungeonForm.getLevel(), testDungeonForm.getType(), 0);
			} else {
				monsters = Dungeon.createMonsterMap(testDungeonForm.getMonsters(), testDungeonForm.getMonsterTypes());
			}
			dungeon = new Dungeon(-1, testDungeonForm.getType(), account, 
					testDungeonForm.isHardcore(), testDungeonForm.isIronborn(), testDungeonForm.getLevel(), 
					testDungeonForm.getXpReward(), 
					monsters, 
					testDungeonForm.getPartySize(),
					0);
			logger.debug("Dungeon: "+dungeon.toString());
			dungeon = dungeonService.create(dungeon);
			logger.debug("Dungeon Id: "+dungeon.getId());
			
			List<Character> tempCharacters = new ArrayList<Character>();
			long[] characterIds = new long[testDungeonForm.getPartySize()];
			for(int i = 0; i < testDungeonForm.getPartySize(); i++) {
				Character tempCharacter = new Character(-1, account, false, false, "TempChar"+i, CharClass.getRandom(), 
						testDungeonForm.getPartyLevel(), 0, 0, null, null);
				tempCharacter = characterService.create(tempCharacter);
				characterIds[i] = tempCharacter.getId();
				tempCharacters.add(tempCharacter);
				
				if(!generateCharacterEquipment(tempCharacter)) {
					model.addAttribute("error", "Error generating equipment");
					return "admin/testDungeonPassRate";
				}
			}
			
			try {
				dungeon = dungeonService.startDungeon(dungeon, characterIds, 0, 
						equipmentService, characterEquipmentService);
			} catch (DungeonNotRunable e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
				
				return "admin/addTestDungeon";
			} catch (CharacterNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
				
				return "admin/addTestDungeon";
			} catch (AccountIdMismatch e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
				
				return "admin/addTestDungeon";
			} catch (DungeonNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
				
				return "admin/addTestDungeon";
			} catch (EquipmentNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
				
				return "admin/addTestDungeon";
			} catch (DifferentGameStates e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
				
				return "admin/addTestDungeon";
			} catch (BoostItemNotFound e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", e.getMessage());
				
				return "admin/addTestDungeon";
			}
			
			List<DungeonEvent> dungeonEvents = dungeonEventService.findAllByDungeon(dungeon);
			model.addAttribute("dungeon", dungeon);
			model.addAttribute("dungeonEvents", dungeonEvents);
			
			// Clean up test dungeons and characters
			
			// First disassociate the characters from the dungeon
			for(Character tempCharacter:tempCharacters) {
				tempCharacter.setDungeon(null);
				try {
					characterService.update(tempCharacter);
				} catch (CharacterNotFound e) {
					logger.error("Unable to remove character {} from dungeon id {}, as the equipment doesn't exist", 
							tempCharacter, dungeon.getId());
					e.printStackTrace();
					return "admin/viewTestDungeon";
				}
			}
			
			// Now delete the dungeon
			try {
				dungeonService.delete(dungeon);
			} catch (DungeonNotFound e) {
				logger.error("Unable to delete dungeon id {}, as it doesn't exist", dungeon.getId());
				e.printStackTrace();
				return "admin/viewTestDungeon";
			} catch (EquipmentNotFound e) {
				logger.error("Unable to delete dungeon id {}, as the equipment doesn't exist", dungeon.getId());
				e.printStackTrace();
				return "admin/viewTestDungeon";
			}
			
			// And then delete the characters
			for(Character tempCharacter:tempCharacters) {
				try {
					characterEquipmentService.delete(tempCharacter);
					characterService.delete(tempCharacter);
				} catch (CharacterNotFound e) {
					logger.error("Unable to delete temp character id {}", tempCharacter.getId());
					e.printStackTrace();
				} catch (CharacterEquipmentNotFound e) {
					logger.error("Unable to delete temp character eqipment for character {}", tempCharacter);
					e.printStackTrace();
				} catch (CharacterSlotNotFound e) {
					logger.error("Unable to delete temp character equipment for character {}", tempCharacter);
					e.printStackTrace();
				}
			}
		} catch (ArraySizeMismatch e) {
			model.addAttribute("error", "Error updating dungeon: "+e.getMessage());
			model.addAttribute("testDungeonForm", testDungeonForm);
			try {
				model.addAttribute("dungeon", new Dungeon(testDungeonForm));
			} catch (ArraySizeMismatch e2) {
				model.addAttribute("dungeon", new Dungeon(-1, DungeonType.NONE, null, false, false, -1, -1, null, -1, -1));
			}
			return "admin/addTestDungeon";
		}
		
		return "admin/viewTestDungeon";
	}
	
	@RequestMapping(value="/testPassRate", method = RequestMethod.GET)
	public String showTestDungeonPassRate(HttpServletRequest request, Model model, Principal principle) {
		logger.debug("showTestDungeonPassRate");
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		model.addAttribute("account", account);
		return "admin/testDungeonPassRate";
	}
		
	/**
	 * Run test dungeons in different permutations
	 */
	@RequestMapping(value="/testPassRate", method = RequestMethod.POST)
	public String runTestDungeonSuccessRate(HttpServletRequest request, Model model, Principal principle) {
		Account account = accountService.findByPrinciple(principle);
		if(account == null) {
			return null;
		}
		
		Dungeon dungeon;
		Map<Monster,Integer> monsterSuccesses = new HashMap<Monster, Integer>();
		Map<Monster,Integer> monsterFailures = new HashMap<Monster, Integer>();
		Map<CharClass,Integer> characterSuccesses = new HashMap<CharClass, Integer>();
		Map<CharClass,Integer> characterFailures = new HashMap<CharClass, Integer>();
		Map<Integer,Integer> levelSuccesses = new HashMap<Integer, Integer>();
		Map<Integer,Integer> levelFailures = new HashMap<Integer, Integer>();
		Map<Integer,Integer> levelStepSuccesses = new HashMap<Integer, Integer>();
		Map<Integer,Integer> levelStepFailures = new HashMap<Integer, Integer>();
		Map<Integer,Integer> monsterCountSuccesses = new HashMap<Integer, Integer>();
		Map<Integer,Integer> monsterCountFailures = new HashMap<Integer, Integer>();
		for(int monstersInDungeon=1; monstersInDungeon <= 20; monstersInDungeon++) {
			Map<Monster, MonsterType> monsterMap = new HashMap<Monster, MonsterType>();
			for(int monsterCount = 0; monsterCount <= monstersInDungeon; monsterCount++) {
				monsterMap.put(Monster.getRandom(), MonsterType.getRandomType());
			}
			for(int level=1; level <= 100; level += 10) {
				int levelStepSize = 1 + (level / 15);
				if(levelStepSize < 1) {
					levelStepSize = 1;
				}
				int partyLevel=level-(3*levelStepSize);
				int maxLevelTest = level+(3*levelStepSize);
				if(partyLevel < 1) {
					partyLevel = 1;
				}
				if(maxLevelTest > 100) {
					maxLevelTest = 100;
				}
				for(; partyLevel <= maxLevelTest; partyLevel += levelStepSize) {
					for(int partySize=1; partySize <= 10; partySize++) {
						dungeon = new Dungeon(-1, DungeonType.DUNGEON, account, false, false, level, 
							1, 
							monsterMap, 
							partySize,
							0);
						logger.debug("Dungeon: "+dungeon.toString());
						dungeon = dungeonService.create(dungeon);
						logger.debug("Dungeon Id: "+dungeon.getId());
						
						List<Character> tempCharacters = new ArrayList<Character>();
						long[] characterIds = new long[partySize];
						for(int i = 0; i < partySize; i++) {
							Character tempCharacter = new Character(-1, account, false, false, 
									"TempChar"+i, CharClass.getRandom(), 
									partyLevel, 0, 0, null, null);
							tempCharacter = characterService.create(tempCharacter);
							characterIds[i] = tempCharacter.getId();
							tempCharacters.add(tempCharacter);
							
							if(!generateCharacterEquipment(tempCharacter)) {
								model.addAttribute("error", "Error generating equipment");
								return "admin/testDungeonPassRate";
							}
						}
						
						try {
							dungeon = dungeonService.startDungeon(dungeon, characterIds, 0, 
									equipmentService, characterEquipmentService);
						} catch (DungeonNotRunable e) {
							logger.error(e.getMessage());
							e.printStackTrace();
							model.addAttribute("error", e.getMessage());
							
							return "admin/testDungeonPassRate";
						} catch (CharacterNotFound e) {
							logger.error(e.getMessage());
							e.printStackTrace();
							model.addAttribute("error", e.getMessage());
							
							return "admin/testDungeonPassRate";
						} catch (AccountIdMismatch e) {
							logger.error(e.getMessage());
							e.printStackTrace();
							model.addAttribute("error", e.getMessage());
							
							return "admin/testDungeonPassRate";
						} catch (DungeonNotFound e) {
							logger.error(e.getMessage());
							e.printStackTrace();
							model.addAttribute("error", e.getMessage());
							
							return "admin/testDungeonPassRate";
						} catch (EquipmentNotFound e) {
							logger.error(e.getMessage());
							e.printStackTrace();
							model.addAttribute("error", e.getMessage());
							
							return "admin/testDungeonPassRate";
						} catch (DifferentGameStates e) {
							logger.error(e.getMessage());
							e.printStackTrace();
							model.addAttribute("error", e.getMessage());
							
							return "admin/testDungeonPassRate";
						} catch (BoostItemNotFound e) {
							logger.error(e.getMessage());
							e.printStackTrace();
							model.addAttribute("error", e.getMessage());
							
							return "admin/testDungeonPassRate";
						}
						
						// Record result
						if(dungeon.isFailed()) {
							for(Character character:tempCharacters) {
								CharClass charClass = character.getCharClass();
								if(characterFailures.containsKey(charClass)) {
									characterFailures.put(charClass, characterFailures.get(charClass)+1);
								} else { 
									characterFailures.put(charClass, 1);
								}
							}
							for(Monster monster:monsterMap.keySet()) {
								if(monsterFailures.containsKey(monster)) {
									monsterFailures.put(monster, monsterFailures.get(monster)+1);
								} else { 
									monsterFailures.put(monster, 1);
								}
							}

							if(levelFailures.containsKey(level)) {
								levelFailures.put(level, levelFailures.get(level)+1);
							} else { 
								levelFailures.put(level, 1);
							}
							
							Integer levelStep = (level-partyLevel) / levelStepSize;
							if(levelStepFailures.containsKey(levelStep)) {
								levelStepFailures.put(levelStep, levelStepFailures.get(levelStep)+1);
							} else { 
								levelStepFailures.put(levelStep, 1);
							}

							if(monsterCountFailures.containsKey(monstersInDungeon)) {
								monsterCountFailures.put(monstersInDungeon, monsterCountFailures.get(monstersInDungeon)+1);
							} else { 
								monsterCountFailures.put(monstersInDungeon, 1);
							}
						} else {
							for(Character character:tempCharacters) {
								CharClass charClass = character.getCharClass();
								if(characterSuccesses.containsKey(charClass)) {
									characterSuccesses.put(charClass, characterSuccesses.get(charClass)+1);
								} else { 
									characterSuccesses.put(charClass, 1);
								}
							}
							for(Monster monster:monsterMap.keySet()) {
								if(monsterFailures.containsKey(monster)) {
									monsterSuccesses.put(monster, monsterFailures.get(monster)+1);
								} else { 
									monsterSuccesses.put(monster, 1);
								}
							}

							if(levelSuccesses.containsKey(level)) {
								levelSuccesses.put(level, levelSuccesses.get(level)+1);
							} else { 
								levelSuccesses.put(level, 1);
							}
							
							Integer levelStep = (level-partyLevel) / levelStepSize;
							if(levelStepSuccesses.containsKey(levelStep)) {
								levelStepSuccesses.put(levelStep, levelStepSuccesses.get(levelStep)+1);
							} else { 
								levelStepSuccesses.put(levelStep, 1);
							}
							
							if(monsterCountSuccesses.containsKey(monstersInDungeon)) {
								monsterCountSuccesses.put(monstersInDungeon, monsterCountSuccesses.get(monstersInDungeon)+1);
							} else { 
								monsterCountSuccesses.put(monstersInDungeon, 1);
							}
						}
						
						// Clean up test dungeons
						try {
							dungeonService.delete(dungeon);
						} catch (DungeonNotFound e) {
							logger.error("Unable to delete dungeon id {}, as it doesn't exist", dungeon.getId());
							e.printStackTrace();
							return "admin/viewTestDungeonPassRate";
						} catch (EquipmentNotFound e) {
							logger.error("Unable to delete dungeon id {}, as the equipment doesn't exist", dungeon.getId());
							e.printStackTrace();
							return "admin/viewTestDungeonPassRate";
						}
						for(Character tempCharacter:tempCharacters) {
							try {
								characterEquipmentService.delete(tempCharacter);
								characterService.delete(tempCharacter);
							} catch (CharacterNotFound e) {
								logger.error("Unable to delete temp character {}", tempCharacter);
								e.printStackTrace();
							} catch (CharacterEquipmentNotFound e) {
								logger.error("Unable to delete temp character equipment for character {}", tempCharacter);
								e.printStackTrace();
							} catch (CharacterSlotNotFound e) {
								logger.error("Unable to delete temp character equipment for character {}", tempCharacter);
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		model.addAttribute("monsterSuccesses", monsterSuccesses);
		model.addAttribute("monsterFailures", monsterFailures);
		model.addAttribute("characterSuccesses", characterSuccesses);
		model.addAttribute("characterFailures", characterFailures);
		model.addAttribute("levelSuccesses", levelSuccesses);
		model.addAttribute("levelFailures", levelFailures);
		model.addAttribute("levelStepSuccesses", levelStepSuccesses);
		model.addAttribute("levelStepFailures", levelStepFailures);
		model.addAttribute("monsterCountSuccesses", monsterCountSuccesses);
		model.addAttribute("monsterCountFailures", monsterCountFailures);
		
		return "admin/viewTestDungeonPassRate";
	}

	private boolean generateCharacterEquipment(Character tempCharacter) {
		// Create equipment
		Map<CharSlot, Equipment> characterSlots = new HashMap<CharSlot, Equipment>();
		try {
			equipmentService.generateStarterEquipment(tempCharacter, characterSlots);
			int tempCharacterLevel = tempCharacter.getLevel();
			if(tempCharacter.getLevel() > 4) {
				int moreItemCount = tempCharacterLevel / 4;
				if(moreItemCount > 9) moreItemCount = 9;
				for(int temp = 0; temp < moreItemCount; temp ++) {
					CharSlot equipmentSlot = null;
					Equipment equipment = null;
					while(equipmentSlot == null) {
						equipment = Equipment.createRandom(DungeonType.DUNGEON, tempCharacterLevel, 
								tempCharacter.isHardcore(), tempCharacter.isIronborn(),
								EquipmentLocation.CHARACTER, tempCharacter.getId(), true);
						List<CharSlot> slots = equipment.getEquipmentType().getValidSlots();
						Iterator<CharSlot> iter = slots.iterator();
						while(equipmentSlot == null && iter.hasNext()) {
							CharSlot slot = iter.next();
							if(!characterSlots.containsKey(slot)) {
								equipmentSlot = slot;
							}
						}
					}
					equipment = equipmentService.create(equipment);
					characterEquipmentService.equipmentItem(tempCharacter, equipmentSlot, equipment);
					// Keep our local cache of what we have upto date
					characterSlots.put(equipmentSlot, equipment);
				}
			}
		} catch (EquipmentNotFound e) {
			logger.error("Error creating starter equipment for character {}: {}", tempCharacter.getId(), e.getMessage());
			e.printStackTrace();
			return false;
		}
		CharacterEquipment characterEquipment = new CharacterEquipment(tempCharacter, characterSlots );
		characterEquipmentService.create(characterEquipment);
		
		return true;
	}
}
