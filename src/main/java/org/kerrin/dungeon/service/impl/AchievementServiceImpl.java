package org.kerrin.dungeon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kerrin.dungeon.enums.AchievementType;
import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Hiscore;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.model.PowerValues;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.repository.AchievementRepo;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.InventoryService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.kerrin.dungeon.utils.CommonTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AchievementServiceImpl extends ServiceHelppers implements AchievementService {
	private static final Logger logger = LoggerFactory.getLogger(AchievementServiceImpl.class);

	private final AchievementRepo achievementRepo;
	private final CharacterService characterService;
	private final CharacterEquipmentService characterEquipmentService;
	private final InventoryService inventoryService;
	private final AccountMessageService accountMessageService;
	
	@Autowired	
    public AchievementServiceImpl(AchievementRepo achievementRepo, 
    		CharacterService characterService,
    		CharacterEquipmentService characterEquipmentService,
    		InventoryService inventoryService,
    		AccountMessageService accountMessageService) {
		super();
		this.achievementRepo = achievementRepo;
		this.characterService = characterService;
		this.characterEquipmentService = characterEquipmentService;
		this.inventoryService = inventoryService;
		this.accountMessageService = accountMessageService;
	}

	@Override
	@Transactional
	public List<Achievement> levelUp(String contextRoot, Character character) {
		Account account = character.getAccount();
		boolean hardcore = character.isHardcore();
		boolean ironborn = character.isIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		// Check for level achievements
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.LEVEL2)) {
			checkAchievementTypes.add(AchievementType.LEVEL2);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL4)) {
			checkAchievementTypes.add(AchievementType.LEVEL4);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL7)) {
			checkAchievementTypes.add(AchievementType.LEVEL7);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL10)) {
			checkAchievementTypes.add(AchievementType.LEVEL10);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL15)) {
			checkAchievementTypes.add(AchievementType.LEVEL15);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL20)) {
			checkAchievementTypes.add(AchievementType.LEVEL20);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL30)) {
			checkAchievementTypes.add(AchievementType.LEVEL30);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL40)) {
			checkAchievementTypes.add(AchievementType.LEVEL40);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL50)) {
			checkAchievementTypes.add(AchievementType.LEVEL50);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL60)) {
			checkAchievementTypes.add(AchievementType.LEVEL60);
		}
		
		if(!character.isUsedLevelUp()) {
			// Also eligible for dungeon only achievements
			if(!achievementTypes.contains(AchievementType.LEVEL5_DUNGEON)) {
				checkAchievementTypes.add(AchievementType.LEVEL5_DUNGEON);
			}
			if(!achievementTypes.contains(AchievementType.LEVEL10_DUNGEON)) {
				checkAchievementTypes.add(AchievementType.LEVEL10_DUNGEON);
			}
			if(!achievementTypes.contains(AchievementType.LEVEL20_DUNGEON)) {
				checkAchievementTypes.add(AchievementType.LEVEL20_DUNGEON);
			}
			if(!achievementTypes.contains(AchievementType.LEVEL30_DUNGEON)) {
				checkAchievementTypes.add(AchievementType.LEVEL30_DUNGEON);
			}
			if(!achievementTypes.contains(AchievementType.LEVEL40_DUNGEON)) {
				checkAchievementTypes.add(AchievementType.LEVEL40_DUNGEON);
			}
			if(!achievementTypes.contains(AchievementType.LEVEL50_DUNGEON)) {
				checkAchievementTypes.add(AchievementType.LEVEL50_DUNGEON);
			}
			if(!achievementTypes.contains(AchievementType.LEVEL60_DUNGEON)) {
				checkAchievementTypes.add(AchievementType.LEVEL60_DUNGEON);
			}
		}
		if(!checkAchievementTypes.isEmpty()) {
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, character.getLevel(),
					account, hardcore, ironborn));
		}
		
		// Check for total level achievements
		checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.LEVEL10_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL10_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL25_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL25_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL50_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL50_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL100_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL100_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL150_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL150_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL200_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL200_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL300_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL300_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL400_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL400_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL500_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL500_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL750_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL750_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL1000_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL1000_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL2500_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL2500_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL5000_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL5000_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL10000_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL10000_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL100000_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL100000_TOTAL);
		}
		
		if(!checkAchievementTypes.isEmpty()) {
			long totalLevel = characterService.getTotalLevel(account, hardcore, ironborn);
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, totalLevel,
					account, hardcore, ironborn));
		}
		
		// TODO: Check Speed Runs
		// TODO: Needs to be less than 1 hour since account reset
		 
		checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.SPEED_RUN_LEVEL_7_HOUR)) {
			// TODO: Need to check only in dungeons
			checkAchievementTypes.add(AchievementType.SPEED_RUN_LEVEL_7_HOUR);
		}
		if(!achievementTypes.contains(AchievementType.SPEED_RUN_LEVEL_8_HOUR_NO_BOOST)) {
			// TODO: Need to check only in dungeons
			// TODO: Need to check only with same level or lower characters
			checkAchievementTypes.add(AchievementType.SPEED_RUN_LEVEL_8_HOUR_NO_BOOST);
		}
		if(!achievementTypes.contains(AchievementType.SPEED_RUN_LEVEL_10_HOUR_BOOST)) {
			// TODO: Need to check only in dungeons			
			checkAchievementTypes.add(AchievementType.SPEED_RUN_LEVEL_10_HOUR_BOOST);
		}
		// TODO: Award speed run achievements
		
		return newAchievements;
	}

	@Override
	@Transactional
	public List<Achievement> prestigeLevel(String contextRoot, Character character) {
		Account account = character.getAccount();
		boolean hardcore = character.isHardcore();
		boolean ironborn = character.isIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		
		// Check for prestige level achievements
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.LEVEL1_PRESTIGE)) {
			checkAchievementTypes.add(AchievementType.LEVEL1_PRESTIGE);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL10_PRESTIGE)) {
			checkAchievementTypes.add(AchievementType.LEVEL10_PRESTIGE);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL20_PRESTIGE)) {
			checkAchievementTypes.add(AchievementType.LEVEL20_PRESTIGE);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL50_PRESTIGE)) {
			checkAchievementTypes.add(AchievementType.LEVEL50_PRESTIGE);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL100_PRESTIGE)) {
			checkAchievementTypes.add(AchievementType.LEVEL100_PRESTIGE);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL150_PRESTIGE)) {
			checkAchievementTypes.add(AchievementType.LEVEL150_PRESTIGE);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL200_PRESTIGE)) {
			checkAchievementTypes.add(AchievementType.LEVEL200_PRESTIGE);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL250_PRESTIGE)) {
			checkAchievementTypes.add(AchievementType.LEVEL250_PRESTIGE);
		}
		if(!checkAchievementTypes.isEmpty()) {
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, character.getPrestigeLevel(),
					account, hardcore, ironborn));
		}
				
		// Check for total level achievements
		checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.LEVEL10_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL10_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL25_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL25_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL50_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL50_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL100_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL100_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL150_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL150_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL200_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL200_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL300_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL300_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL400_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL400_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL500_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL500_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL750_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL750_TOTAL);
		}
		if(!achievementTypes.contains(AchievementType.LEVEL1000_TOTAL)) {
			checkAchievementTypes.add(AchievementType.LEVEL1000_TOTAL);
		}
		
		if(!checkAchievementTypes.isEmpty()) {
			long totalLevel = characterService.getTotalLevel(account, hardcore, ironborn);
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, totalLevel,
					account, hardcore, ironborn));
		}
		
		return newAchievements;
	}

	@Override
	@Transactional
	public List<Achievement> equipItem(String contextRoot, Equipment equipment) {
		if(equipment.getEquipmentLocation() != EquipmentLocation.CHARACTER) return null;
		Character character = characterService.findById(equipment.getEquipmentLocationId());
		Account account = character.getAccount();
		boolean hardcore = equipment.isHardcore();
		boolean ironborn = equipment.isIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		PowerValues powerValues = new PowerValues();
		Map<EquipmentAttribute, Integer> characterSummary = new HashMap<EquipmentAttribute, Integer>();
		CommonTools.calculatePowerValues(powerValues, equipment, characterSummary, character);
		// Check for attack values achievements
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.ATTACK_VALUE_50)) {
			checkAchievementTypes.add(AchievementType.ATTACK_VALUE_50);
		}
		if(!achievementTypes.contains(AchievementType.ATTACK_VALUE_100)) {
			checkAchievementTypes.add(AchievementType.ATTACK_VALUE_100);
		}
		if(!achievementTypes.contains(AchievementType.ATTACK_VALUE_150)) {
			checkAchievementTypes.add(AchievementType.ATTACK_VALUE_150);
		}
		if(!achievementTypes.contains(AchievementType.ATTACK_VALUE_200)) {
			checkAchievementTypes.add(AchievementType.ATTACK_VALUE_200);
		}
		if(!checkAchievementTypes.isEmpty()) {
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
					(powerValues.attackValue + powerValues.classSpecificAttackValue) / equipment.getLevel(),
					account, hardcore, ironborn));
		}
		checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.DEFENCE_VALUE_25)) {
			checkAchievementTypes.add(AchievementType.DEFENCE_VALUE_25);
		}
		if(!achievementTypes.contains(AchievementType.DEFENCE_VALUE_50)) {
			checkAchievementTypes.add(AchievementType.DEFENCE_VALUE_50);
		}
		if(!achievementTypes.contains(AchievementType.DEFENCE_VALUE_75)) {
			checkAchievementTypes.add(AchievementType.DEFENCE_VALUE_75);
		}
		if(!achievementTypes.contains(AchievementType.DEFENCE_VALUE_100)) {
			checkAchievementTypes.add(AchievementType.DEFENCE_VALUE_100);
		}
		if(!checkAchievementTypes.isEmpty()) {
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
					(powerValues.defenceValue + powerValues.classSpecificDefenceValue) / equipment.getLevel(),
					account, hardcore, ironborn));
		}
		
		// Artifact achievements
		if(equipment.getQuality() == EquipmentQuality.ARTIFACT) {
			if(!achievementTypes.contains(AchievementType.EQUIP_ARTIFACT)) {
				checkAchievementTypes = new ArrayList<AchievementType>();
				checkAchievementTypes.add(AchievementType.EQUIP_ARTIFACT);
				newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
						1,
						account, hardcore, ironborn));
			}

			long artifactCount = 0;
			long artifactMaxLevelCount = 0;
			if(!achievementTypes.contains(AchievementType.EQUIP_ALL_ARTIFACTS) ||
					!achievementTypes.contains(AchievementType.EQUIP_ALL_ARTIFACTS_MAX_LEVEL)) {
				CharacterEquipment characterEquipment = characterEquipmentService.findById(character.getId());
				for(Equipment checkEquipment:characterEquipment.getCharacterSlots().values()) {
					if(checkEquipment.getQuality() == EquipmentQuality.ARTIFACT) {
						artifactCount++;
						if(checkEquipment.getLevel() == Character.MAX_LEVEL) {
							artifactMaxLevelCount++;
						}
					}
				}
			}
			if(!achievementTypes.contains(AchievementType.EQUIP_ALL_ARTIFACTS)) {
				checkAchievementTypes = new ArrayList<AchievementType>();
				checkAchievementTypes.add(AchievementType.EQUIP_ALL_ARTIFACTS);
				newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
						artifactCount,
						account, hardcore, ironborn));
			}
			if(!achievementTypes.contains(AchievementType.EQUIP_ALL_ARTIFACTS_MAX_LEVEL)) {
				checkAchievementTypes = new ArrayList<AchievementType>();
				checkAchievementTypes.add(AchievementType.EQUIP_ALL_ARTIFACTS_MAX_LEVEL);
				newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
						artifactMaxLevelCount,
						account, hardcore, ironborn));
			}
		}

		return newAchievements;
	}

	@Override
	@Transactional
	public List<Achievement> dungeonClose(String contextRoot, Dungeon dungeon) {
		Account account = dungeon.getAccount();
		boolean hardcore = dungeon.isHardcore();
		boolean ironborn = dungeon.isIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(dungeon.isFinished() && !dungeon.isFailed()) {
			if(!achievementTypes.contains(AchievementType.FINISH_ADVENTURE) && dungeon.getType() == DungeonType.ADVENTURE) {
				checkAchievementTypes.add(AchievementType.FINISH_ADVENTURE);
				newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
						1,
						account, hardcore, ironborn));
			} else if(!achievementTypes.contains(AchievementType.FINISH_DUNGEON)) {
				checkAchievementTypes.add(AchievementType.FINISH_DUNGEON);
				newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
						1,
						account, hardcore, ironborn));
			} else if(!achievementTypes.contains(AchievementType.FINISH_RAID)) {
				checkAchievementTypes.add(AchievementType.FINISH_RAID);
				newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
						1,
						account, hardcore, ironborn));
			}
		}
		return newAchievements;
	}
	
	@Override
	@Transactional
	public List<Achievement> rushDungeon(String contextRoot, Dungeon dungeon) {
		Account account = dungeon.getAccount();
		boolean hardcore = dungeon.isHardcore();
		boolean ironborn = dungeon.isIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.RUSH_DUNGEON)) {
			checkAchievementTypes.add(AchievementType.RUSH_DUNGEON);
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
				1,
				account, hardcore, ironborn));
		}
		
		return newAchievements;
	}

	@Override
	@Transactional
	public List<Achievement> newCharacter(String contextRoot, Character character) {
		Account account = character.getAccount();
		boolean hardcore = character.isHardcore();
		boolean ironborn = character.isIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		List<Character> allAccountCharacters = characterService.findAllByAccountOrderByLevel(account, hardcore, ironborn);
		if(!achievementTypes.contains(AchievementType.ALL_CLASSES)) {
			checkAchievementTypes.add(AchievementType.ALL_CLASSES);
			Map<CharClass,Boolean> uniqueClasses = new HashMap<CharClass,Boolean>();
			for(Character checkCharacter:allAccountCharacters) {
				uniqueClasses.put(checkCharacter.getCharClass(), true);
			}
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
					uniqueClasses.keySet().size(),
					account, hardcore, ironborn));
		}
		if(!achievementTypes.contains(AchievementType.TEN_CHARACTERS)) {
			checkAchievementTypes = new ArrayList<AchievementType>();
			checkAchievementTypes.add(AchievementType.TEN_CHARACTERS);
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
					allAccountCharacters.size(),
					account, hardcore, ironborn));
		}
		return newAchievements;
	}

	@Override
	@Transactional
	public List<Achievement> stashIncrease(String contextRoot, Account account, boolean hardcore, boolean ironborn) {
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreIsNullAndIronbornIsNull(account.getId()));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.STASH_20)) {
			checkAchievementTypes.add(AchievementType.STASH_20);
			Inventory inventory = inventoryService.findByAccount(account, hardcore, ironborn);
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
					inventory.getSize(),
					account, hardcore, ironborn));
		}
		return newAchievements;
	}

	@Override
	@Transactional
	public List<Achievement> resurrected(String contextRoot, Character character) {
		Account account = character.getAccount();
		boolean hardcore = character.isHardcore();
		boolean ironborn = character.isIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.RESURRECT)) {
			checkAchievementTypes.add(AchievementType.RESURRECT);
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
					1,
					account, hardcore, ironborn));
		}
		return newAchievements;
	}

	@Override
	@Transactional
	public List<Achievement> login(String contextRoot, Account account) {
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreIsNullAndIronbornIsNull(account.getId());
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.LOGIN_TOKENS_5)) {
			checkAchievementTypes.add(AchievementType.LOGIN_TOKENS_5);
		}
		if(!achievementTypes.contains(AchievementType.LOGIN_TOKENS_10)) {
			checkAchievementTypes.add(AchievementType.LOGIN_TOKENS_10);
		}
		if(!achievementTypes.contains(AchievementType.LOGIN_TOKENS_25)) {
			checkAchievementTypes.add(AchievementType.LOGIN_TOKENS_25);
		}
		if(!achievementTypes.contains(AchievementType.LOGIN_TOKENS_50)) {
			checkAchievementTypes.add(AchievementType.LOGIN_TOKENS_50);
		}
		if(!achievementTypes.contains(AchievementType.LOGIN_TOKENS_75)) {
			checkAchievementTypes.add(AchievementType.LOGIN_TOKENS_75);
		}
		if(!achievementTypes.contains(AchievementType.LOGIN_TOKENS_100)) {
			checkAchievementTypes.add(AchievementType.LOGIN_TOKENS_100);
		}
		if(!checkAchievementTypes.isEmpty()) {
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
					account.getLoginTokens(),
					account, null, null));
		}
		return newAchievements;
	}

	@Override
	@Transactional
	public List<Achievement> hiscore(String contextRoot, Hiscore hiscore) {
		Account account = hiscore.getAccount();
		boolean hardcore = hiscore.getHardcore();
		boolean ironborn = hiscore.getIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.TOP)) {
			checkAchievementTypes.add(AchievementType.TOP);
		}
		if(!achievementTypes.contains(AchievementType.TOP_5)) {
			checkAchievementTypes.add(AchievementType.TOP_5);
		}
		if(!achievementTypes.contains(AchievementType.TOP_10)) {
			checkAchievementTypes.add(AchievementType.TOP_10);
		}
		if(!achievementTypes.contains(AchievementType.TOP_25)) {
			checkAchievementTypes.add(AchievementType.TOP_25);
		}
		if(!achievementTypes.contains(AchievementType.TOP_50)) {
			checkAchievementTypes.add(AchievementType.TOP_50);
		}
		if(!achievementTypes.contains(AchievementType.TOP_100)) {
			checkAchievementTypes.add(AchievementType.TOP_100);
		}
		if(!achievementTypes.contains(AchievementType.TOP_1000)) {
			checkAchievementTypes.add(AchievementType.TOP_1000);
		}
		if(!checkAchievementTypes.isEmpty()) {
			newAchievements.addAll(awardAchievementsLower(contextRoot, checkAchievementTypes, 
					hiscore.getRank(),
					account, hardcore, ironborn));
		}
		return newAchievements;
	}

	@Override
	public List<Achievement> salvage(String contextRoot, Account account, StashSlotItemSuper item) {
		boolean hardcore = item.isHardcore();
		boolean ironborn = item.isIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.SALVAGE_ITEM)) {
			checkAchievementTypes.add(AchievementType.SALVAGE_ITEM);
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
					1,
					account, hardcore, ironborn));
		}
		return newAchievements;
	}

	@Override
	public List<Achievement> enchant(String contextRoot, Account account, Equipment equipment) {
		boolean hardcore = equipment.isHardcore();
		boolean ironborn = equipment.isIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		if(!achievementTypes.contains(AchievementType.ENCHANT_ITEM)) {
			checkAchievementTypes.add(AchievementType.ENCHANT_ITEM);
			newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
					1,
					account, hardcore, ironborn));
		}
		return newAchievements;
	}

	/**
	 * Check for achievements on redemption of a boost item
	 */
	@Override
	public List<Achievement> redeemBoostItem(String contextRoot, Account account, BoostItem boostItem) {
		boolean hardcore = boostItem.isHardcore();
		boolean ironborn = boostItem.isIronborn();
		List<Achievement> currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		currentAchievements.addAll(achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		List<AchievementType> checkAchievementTypes = new ArrayList<AchievementType>();
		List<AchievementType> achievementTypes = getAchievementTypes(currentAchievements);
		switch(boostItem.getBoostItemType()) {
		case CHANGE_NAME:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_CHANGE_NAME)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_CHANGE_NAME);
			}
			break;
		case DUNGEON_SPEED:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_DUNGEON_SPEED)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_DUNGEON_SPEED);
			}
			break;
		case DUNGEON_TOKENS:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_DUNGEON_TOKENS)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_DUNGEON_TOKENS);
			}
			break;
		case ENCHANT_IMPROVE_RANGE:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_ENCHANT_IMPROVE_RANGE)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_ENCHANT_IMPROVE_RANGE);
			}
			break;
		case ENCHANT_RANGE:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_ENCHANT_RANGE)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_ENCHANT_RANGE);
			}
			break;
		case ENCHANT_REMOVE_CURSE:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_ENCHANT_REMOVE_CURSE)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_ENCHANT_REMOVE_CURSE);
			}
			break;
		case ENCHANT_TYPE:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_ENCHANT_TYPE)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_ENCHANT_TYPE);
			}
			break;
		case IMPROVE_QUALITY:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_IMPROVE_QUALITY)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_IMPROVE_QUALITY);
			}
			break;
		case LEVEL_UP:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_LEVEL_UP)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_LEVEL_UP);
			}
			break;
		case MAGIC_FIND:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_MAGIC_FIND)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_MAGIC_FIND);
			}
			break;
		case RESURRECTION:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_RESURRECTION)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_RESURRECTION);
			}
			break;
		case XP_BOOST:
			if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_CHANGE_NAME)) {
				checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_XP_BOOST);
			}
			break;
		case UNKNOWN:
			logger.error("Boost Item Type Unknown for item: "+boostItem);
			break;
		default:
			logger.error("Unknown Boost Item Type "+boostItem.getBoostItemType()+" for item: "+boostItem);
		}
		if(!achievementTypes.contains(AchievementType.BOOST_ITEM_REDEEM_ANY)) {
			checkAchievementTypes.add(AchievementType.BOOST_ITEM_REDEEM_ANY);
		}
		newAchievements.addAll(awardAchievementsHigher(contextRoot, checkAchievementTypes, 
				1,
				account, hardcore, ironborn));
		return newAchievements;
	}

	@Override
	@Transactional
	public Achievement create(Achievement achievement) {
		Achievement createdAchievement = achievement;
    	
        return achievementRepo.save(createdAchievement);
	}

	@Override
	@Transactional
	public Achievement update(Achievement achievement) {
		Achievement updatedAchievement = achievementRepo.findById(achievement.getId());
        
        if (updatedAchievement == null) {
        	logger.info("Achievement {} was missing during update, creating", achievement);
        	updatedAchievement = new Achievement(-1,
        			achievement.getAccount(), achievement.getType(), 
        			achievement.isHardcore(), achievement.isIronborn(),
        			new Date());
        	updatedAchievement = achievementRepo.save(updatedAchievement);
        }
        
        updatedAchievement.setTimestamp(new Date());

		loadAchievementLinkedTables(updatedAchievement);
        updatedAchievement = achievementRepo.save(updatedAchievement);
        return updatedAchievement;
	}

	@Override
	@Transactional
	public Achievement findById(long achievementId) {
		Achievement achievements = achievementRepo.findOne(achievementId);
		return achievements;
	}

	@Override
	@Transactional
	public List<Achievement> findAllByAccount(Account account) {
		return achievementRepo.findAllByAccountId(account.getId());
	}

	@Override
	@Transactional
	public List<Achievement> findAllByAccount(Account account, Boolean hardcore, Boolean ironborn) {
		if((hardcore==null) != (ironborn == null)) return null;
		List<Achievement> currentAchievements;
		if(hardcore == null) {
			currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreIsNullAndIronbornIsNull(account.getId());
		} else {
			currentAchievements = achievementRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		}
		return currentAchievements;
	}

	/**
	 * Get the achievement types from the achievements
	 * @param currentAchievements
	 * @return
	 */
	private List<AchievementType> getAchievementTypes(List<Achievement> currentAchievements) {
		List<AchievementType> currentAchievementTypes = new ArrayList<AchievementType>();
		for(Achievement achievement:currentAchievements) {
			currentAchievementTypes.add(achievement.getType());
		}
		return currentAchievementTypes;
	}

	/**
	 * Check if the threshold was met or higher for the achievements
	 * This will add the achievement to the account
	 * 
	 * @param contextRoot
	 * @param checkAchievementTypes		List of achievements to check
	 * @param checkValue				Value to check
	 * @param account					The account to check
	 * @param hardcore					Hardcore mode?
	 * @param ironborn					Ironborn mode?
	 * 
	 * @return	Achievements awarded
	 */
	private List<Achievement> awardAchievementsHigher(String contextRoot, List<AchievementType> checkAchievementTypes, 
			long checkValue, Account account, Boolean hardcore, Boolean ironborn) {
		List<Achievement> awardedAchievements = new ArrayList<Achievement>();
		for(AchievementType checkAchievementType:checkAchievementTypes) {
			if(checkAchievementType.getThreshold() <= checkValue) {
				Achievement newAchievement = new Achievement(-1, account, checkAchievementType, hardcore, ironborn, new Date());
				newAchievement = achievementRepo.save(newAchievement);
				awardedAchievements.add(newAchievement);
				AccountMessage accountMessage = generateAccountMessage(contextRoot, newAchievement);
				accountMessageService.create(accountMessage);
			}
		}
		return awardedAchievements;
	}
	
	/**
	 * Check if the threshold was met or lower for the achievements
	 * This will add the achievement to the account
	 * 
	 * @param checkAchievementTypes
	 * @param threshold
	 * @return	Achievements awarded
	 */
	private List<Achievement> awardAchievementsLower(String contextRoot, List<AchievementType> checkAchievementTypes, long threshold, 
			Account account, Boolean hardcore, Boolean ironborn) {
		List<Achievement> awardedAchievements = new ArrayList<Achievement>();
		for(AchievementType checkAchievementType:checkAchievementTypes) {
			if(checkAchievementType.getThreshold() >= threshold) {
				Achievement newAchievement = new Achievement(-1, account, checkAchievementType, hardcore, ironborn, new Date());
				newAchievement = achievementRepo.save(newAchievement);
				awardedAchievements.add(newAchievement);
				AccountMessage accountMessage = generateAccountMessage(contextRoot, newAchievement);
				accountMessageService.create(accountMessage);
			}
		}
		return awardedAchievements;
	}

	/**
	 * 
	 * @param achievement
	 * @return
	 */
	private AccountMessage generateAccountMessage(String contextRoot, Achievement achievement) {
		StringBuilder message = new StringBuilder("New ");
		if(achievement.isHardcore() == null || achievement.isIronborn()) {
			message.append("Account Wide ");
		} else {
			if(achievement.isHardcore() != null && achievement.isHardcore()) {
				if(achievement.isIronborn() != null && achievement.isIronborn()) {
					message.append("Extreme ");
				} else {
					message.append("Hardcore ");
				}	
			} else {
				if(achievement.isIronborn() != null && achievement.isIronborn()) {
					message.append("Ironborn");
				} else {
					message.append("Normal ");
				}	
			}
		}
		message.append("Achievement!<br/>");
		message.append(achievement.getType().getNiceName());
		message.append("<br/>");
		message.append(achievement.getType().getDescription());
		message.append("<br/>Points: ");
		message.append(achievement.getType().getPoints());
		message.append("<br/>");
		StringBuilder linkUrl = new StringBuilder();
		if(contextRoot != null) {
			linkUrl.append(contextRoot);
			linkUrl.append("/play/account/achievements?hardcore=");
			linkUrl.append(achievement.isHardcore()!= null && achievement.isHardcore()?"true":"false");
			linkUrl.append("&ironborn=");
			linkUrl.append(achievement.isIronborn() != null && achievement.isIronborn()?"true":"false");
		}
		return new AccountMessage(achievement.getAccount().getId(), message.toString(), linkUrl.toString(), null, achievement.isHardcore(), achievement.isIronborn());
	}

	@Override
	public List<Achievement> getAchievements(Account account, Boolean hardcore, Boolean ironborn) {
		if((hardcore == null) != (ironborn == null)) return null;
		List<Achievement> allAchievements = new ArrayList<Achievement>(AchievementType.values().length);
		for(AchievementType achievmentType:AchievementType.values()) {
			while(achievmentType.getOrder() >= allAchievements.size()) {
				allAchievements.add(null);
			}
			if(hardcore == null) {
				if(!achievmentType.isHardcorePossible()  && !achievmentType.isIronbornPossible()) {
					allAchievements.set(achievmentType.getOrder(), new Achievement(achievmentType));
				}
			} else {
				if(hardcore && !achievmentType.isHardcorePossible()) {
					continue;
				} else if(ironborn && !achievmentType.isIronbornPossible()) {
					continue;
				}
				allAchievements.add(achievmentType.getOrder(), new Achievement(achievmentType));
			}
		}
		List<Achievement> gotAchievements = findAllByAccount(account, hardcore, ironborn);
		for(Achievement achievement:gotAchievements) {
			allAchievements.set(achievement.getType().getOrder(), achievement);
		}
		return allAchievements;
	}


	@Override
	public long getAchievementPoints(Account account) {
		long points = 0;
		List<Achievement> gotAchievements = findAllByAccount(account);
		for(Achievement achievement:gotAchievements) {
			points += achievement.getPoints();
		}
		
		return points;
	}
	
	@Override
	public long getAchievementPoints(Account account, Boolean hardcore, Boolean ironborn) {
		long points = 0;
		List<Achievement> gotAchievements = findAllByAccount(account, hardcore, ironborn);
		for(Achievement achievement:gotAchievements) {
			points += achievement.getPoints();
		}
		
		return points;
	}
}
