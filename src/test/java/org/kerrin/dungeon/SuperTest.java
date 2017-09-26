package org.kerrin.dungeon;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.enums.HiscoreType;
import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CantEquipToDungeon;
import org.kerrin.dungeon.exception.CantEquipToMessage;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.exception.DifferentGameStates;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryException;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.exception.MessageEquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountRole;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonEvent;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Hiscore;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.repository.AccountMessageRepo;
import org.kerrin.dungeon.service.AccountBoostService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.DungeonEventService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.service.InventoryService;
import org.kerrin.dungeon.service.ReloadPanels;
import org.kerrin.dungeon.service.StashSlotItemService;
import org.mockito.Mock;
import org.springframework.http.MediaType;

public class SuperTest {
	protected static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),                        
            Charset.forName("utf8")                     
            );
	
	protected static final long ACCOUNT_ID = 1;
	protected static final long ACCOUNT_ID2 = 2;
	protected static final String USERNAME = "admin";
	protected static final String PASSWORD = "password";
	protected static final String DISPLAYNAME = "Test Admin";
	protected static final String DISPLAYNAME2 = "Admin Test";
	protected static final String USERNAME2 = "testuser2";
	protected static final String HASHED_PASSWORD = "blah$testhashedpassword";
	protected static final String PASSWORD2 = "password2";

	protected static final long DUNGEON_ID = 3;
	protected static final DungeonType DUNGEON_TYPE = DungeonType.RAID;
	protected static final int LEVEL = 4;
	protected static final long XP = 5;
	protected static final int PARTY_SIZE = 6;
	
	protected static final long DUNGEON_ID2 = 7;
	protected static final DungeonType DUNGEON_TYPE2 = DungeonType.DUNGEON;
	protected static final int LEVEL2 = 8;
	protected static final long XP2 = 9;
	protected static final int PARTY_SIZE2 = 10;
	
	protected static final long DUNGEON_STARTED_ID = 4;
	protected static final DungeonType DUNGEON_TYPE_STARTED = DungeonType.ADVENTURE;
	
	protected static final long CHAR_ID = 11;
	protected static final long CHAR_ID_WITH_EQUIPMENT = 12;
	protected static final String CHAR_NAME = "TestChar";
	protected static final CharClass CHAR_CLASS = CharClass.MAGIC;
	protected static final int CHAR_LEVEL = 12;
	protected static final long CHAR_XP = 13;
	protected static final int CHAR_PRESTIGE_LEVEL = 14;
	protected static final Date DEAD = new Date();
		
	protected static final long CHAR_ID2 = 14;
	protected static final String CHAR_NAME2 = "testname2";
	protected static final CharClass CHAR_CLASS2 = CharClass.PETS;
	protected static final int ADD_LEVELS = 123;
	
	protected static final long EQUIPMENT_ID = 15;
	protected static final EquipmentType EQUIPMENT_TYPE = EquipmentType.BROACH;
	protected static final EquipmentQuality EQUIPMENT_QUALITY = EquipmentQuality.ARTIFACT;
	
	protected static final long EQUIPMENT_ID2 = 16;
	protected static final EquipmentType EQUIPMENT_TYPE2 = EquipmentType.MAIN_WEAPON;
	protected static final EquipmentQuality EQUIPMENT_QUALITY2 = EquipmentQuality.EPIC;
	
	protected static final AccountConfigType ACCOUNT_CONFIG_TYPE = AccountConfigType.EQUIPMENT_COMPARE;
	protected static final int ACCOUNT_CONFIG_VALUE = 1234;
	
	protected static final long CURRENCY = 12345678890L; 
	
	@Mock
	protected AccountService accountServiceMock;
	@Mock
	protected CharacterService characterServiceMock;
	@Mock
	protected DungeonService dungeonServiceMock;
	@Mock
	protected DungeonEventService dungeonEventServiceMock;
	@Mock
	protected AccountCurrencyService accountCurrencyServiceMock;	
	@Mock
	protected AccountBoostService accountBoostServiceMock;
	@Mock
	protected StashSlotItemService stashSlotItemServiceMock;
	@Mock
	protected EquipmentService equipmentServiceMock;
	@Mock
	protected BoostItemService boostItemServiceMock;
	@Mock
	protected CharacterEquipmentService characterEquipmentServiceMock;
	@Mock
	protected InventoryService inventoryServiceMock;
	@Mock
	protected HiscoreService hiscoreServiceMock;
	@Mock
	protected AchievementService achievementServiceMock;
	@Mock
	protected AccountMessageRepo accountMessageRepoMock;
	@Mock
	protected AccountMessageService accountMessageServiceMock;
	@Mock
	protected ServletContext servletContextMock;
	
	protected Account testAccount;
	protected Account testAccount2;
	protected Date now;
	protected Date lastLogin;
	protected Date previousLogin;
	
	protected Character testCharacter;
	protected Character testCharacterWithEquipment;
	protected Character testCharacter2;
	
	protected Dungeon testDungeon;
	protected Dungeon testDungeon2;
	protected Dungeon testDungeonStarted;

	protected static final int SLOT_NUMBER = 10;
	
	protected Inventory testInventory;
	
	protected Equipment testEquipment;
	protected Equipment testEquipmentNoId;
	protected Equipment testEquipment2;
	protected Equipment testEquipmentNoId2;
	
	protected HashMap<Integer, Integer> attributes;
	protected HashMap<Integer, Integer> attributes2;
	protected EquipmentAttribute testBaseAttribute;
	protected int testBaseAttributeValue;
	protected EquipmentAttribute testDefenceAttribute;
	protected int testDefenceAttributeValue;
	protected EquipmentAttribute testAncientAttribute;
	protected int testAncientAttributeValue;
	
	protected List<Equipment> equipmentList = new ArrayList<Equipment>();
	protected List<Equipment> equipmentList2 = new ArrayList<Equipment>();
	
	protected Map<CharSlot, Equipment> testCharacterSlots = new HashMap<CharSlot, Equipment>();
	protected CharacterEquipment testCharacterEquipment;
	
	protected List<DungeonEvent> testDungeonEvents = new ArrayList<DungeonEvent>();
	
	protected Hiscore testHiscore;
	
	protected ReloadPanels reloadPanelsMock;
	
	public SuperTest() {
		super();
	}
	
	@Before
    public void setUp() {
        // Set up the account
        now = new Date();
		lastLogin = new Date(now.getTime()-123456789L);
		previousLogin = new Date(lastLogin.getTime()-123456789L);
		Set<AccountRole> roles = new HashSet<AccountRole>();
		testAccount = new Account(ACCOUNT_ID, USERNAME, "", DISPLAYNAME, previousLogin, lastLogin, roles, false, false, 1);
		try {
			testAccount.setPassword(PASSWORD);
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
		roles.add(new AccountRole(testAccount, AccountPrivilege.VIEW));
		roles.add(new AccountRole(testAccount, AccountPrivilege.MODIFY));
		roles.add(new AccountRole(testAccount, AccountPrivilege.DELETE));
		testAccount.setRoles(roles);
		testAccount.createApiKey();
        when(accountServiceMock.findByPrinciple(any(Principal.class))).thenReturn(testAccount);
        when(accountServiceMock.findByApiKey(eq(testAccount.getApiKey()))).thenReturn(testAccount);
        when(accountCurrencyServiceMock.adjustCurrency(eq(testAccount), anyBoolean(), anyBoolean(), anyLong(), 
				any(ModificationType.class), anyString())).thenReturn(true);
		when(servletContextMock.getContextPath()).thenReturn("/dungeon");
		
        testAccount2 = new Account(ACCOUNT_ID2, USERNAME2, "", DISPLAYNAME2, previousLogin, lastLogin, roles, false, false, 1);
		try {
			testAccount2.setPassword(PASSWORD);
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
		roles.add(new AccountRole(testAccount2, AccountPrivilege.VIEW));
		roles.add(new AccountRole(testAccount2, AccountPrivilege.MODIFY));
		roles.add(new AccountRole(testAccount2, AccountPrivilege.DELETE));
		testAccount2.setRoles(roles);
		testAccount2.createApiKey();
        when(accountServiceMock.findByPrinciple(any(Principal.class))).thenReturn(testAccount2);
        when(accountServiceMock.findByApiKey(eq(testAccount2.getApiKey()))).thenReturn(testAccount2);
        when(accountCurrencyServiceMock.adjustCurrency(eq(testAccount2), anyBoolean(), anyBoolean(), anyLong(), 
				any(ModificationType.class), anyString())).thenReturn(true);
		
	    // Set up the character
        testCharacter = new Character(CHAR_ID, testAccount, false, false, CHAR_NAME, CHAR_CLASS, CHAR_LEVEL, CHAR_XP, CHAR_PRESTIGE_LEVEL, null /* Alive */, null /* Dungeon */);
		when(characterServiceMock.findById(eq(CHAR_ID))).thenReturn(testCharacter);
        testCharacter2 = new Character(CHAR_ID2, testAccount, false, false, CHAR_NAME2, CHAR_CLASS2, LEVEL2, XP2, CHAR_PRESTIGE_LEVEL, DEAD, null /* Dungeon */);
        when(characterServiceMock.findById(eq(CHAR_ID2))).thenReturn(testCharacter2);
        when(characterServiceMock.create(any(Character.class))).thenReturn(testCharacter2);
    
        Map<Equipment,Boolean> itemRewards = new HashMap<Equipment, Boolean>();
		Map<Monster,MonsterType> monsters = new HashMap<Monster,MonsterType>();
		// Set up the dungeon
        testDungeon = new Dungeon(DUNGEON_ID, DUNGEON_TYPE, testAccount, false, false, LEVEL, XP, monsters, PARTY_SIZE, 0);
        testDungeon.setItemRewards(itemRewards);
        testDungeonStarted = new Dungeon(DUNGEON_STARTED_ID, DUNGEON_TYPE_STARTED, testAccount, false, false, LEVEL, XP, monsters, PARTY_SIZE, 0);
        testDungeonStarted.setItemRewards(itemRewards);
        testDungeonStarted.setStarted(new Date());
        List<Character> characters = new ArrayList<Character>();
        characters.add(testCharacter);
		try {
			testDungeonStarted.setCharacters(characters);
		} catch (AccountIdMismatch e) {
			e.printStackTrace();
		} catch (DifferentGameStates e) {
			e.printStackTrace();
		}
        when(dungeonServiceMock.findById(eq(DUNGEON_ID))).thenReturn(testDungeon);
        when(dungeonServiceMock.findById(eq(DUNGEON_STARTED_ID))).thenReturn(testDungeonStarted);
        try {
			when(dungeonServiceMock.startDungeon(eq(testDungeon), any(long[].class), anyInt(), 
					any(EquipmentService.class), any(CharacterEquipmentService.class))).thenReturn(testDungeonStarted);
		} catch (Exception e) {
			e.printStackTrace();
		}
        testDungeon2 = new Dungeon(DUNGEON_ID2, DUNGEON_TYPE2, testAccount, false, false, LEVEL2, XP2, monsters, PARTY_SIZE2, 0);
        testDungeon2.setItemRewards(itemRewards);
        when(dungeonServiceMock.findById(eq(DUNGEON_ID2))).thenReturn(testDungeon2);
        when(dungeonServiceMock.create(any(Dungeon.class))).thenReturn(testDungeon2);
        
        HashMap<Integer,StashSlotItemSuper> inventorySlots = new HashMap<Integer, StashSlotItemSuper>(SLOT_NUMBER);
		for(int i=0; i < SLOT_NUMBER; i++) {
			Equipment equipment = createRandomEquipment();
			equipmentList.add(i, equipment);
			inventorySlots.put(i, equipmentList.get(i));
		}
		// Set up the inventory
        testInventory = new Inventory(ACCOUNT_ID, false, false, SLOT_NUMBER, inventorySlots);
        
        when(inventoryServiceMock.findByAccount(eq(testAccount), anyBoolean(), anyBoolean())).thenReturn(testInventory);
        
        attributes = new HashMap<Integer, Integer>();
        testBaseAttribute = EquipmentAttribute.getRandomBase();
        testBaseAttributeValue = Equipment.getRandomAttributeValue(LEVEL, testBaseAttribute, false);
        testDefenceAttribute = EquipmentAttribute.getRandomDefence();
        testDefenceAttributeValue = Equipment.getRandomAttributeValue(LEVEL, testDefenceAttribute, false);
        // Generate the other attributes, they must be unique
        for(int i=2; i < EQUIPMENT_QUALITY.getId(); i++) {
        	EquipmentAttribute attribute = EquipmentAttribute.getRandom(null);
        	while(attributes.containsKey(attribute.getId())) attribute = EquipmentAttribute.getRandom(null);
        	attributes.put(attribute.getId(), Equipment.getRandomAttributeValue(LEVEL, attribute, false));
        }
        testAncientAttribute = EquipmentAttribute.getRandom(null);
        testAncientAttributeValue = Equipment.getRandomAttributeValue(LEVEL, testAncientAttribute, false);
        
		// Set up the dungeon
        testEquipment = new Equipment(EQUIPMENT_ID, EQUIPMENT_TYPE, EQUIPMENT_QUALITY, LEVEL, 
        		false, false, 
        		testBaseAttribute, testBaseAttributeValue, testDefenceAttribute, testDefenceAttributeValue, 
        		attributes, 
        		testAncientAttribute, testAncientAttributeValue, 
        		EquipmentLocation.CHARACTER, CHAR_ID);
        testEquipmentNoId = new Equipment(Equipment.CURRENT_VERSION, -1, EQUIPMENT_TYPE, EQUIPMENT_QUALITY, LEVEL, 
        		false, false,  
        		testBaseAttribute, testBaseAttributeValue, testDefenceAttribute, testDefenceAttributeValue, 
        		attributes, 
        		testAncientAttribute, testAncientAttributeValue, 
        		EquipmentLocation.CHARACTER, CHAR_ID);
        when(accountServiceMock.accountOwnsEquipment(eq(testAccount), eq(testEquipment))).thenReturn(true);
        when(equipmentServiceMock.findById(eq(EQUIPMENT_ID))).thenReturn(testEquipment);
	    when(equipmentServiceMock.create(eq(testEquipmentNoId))).thenReturn(testEquipment);
	    reloadPanelsMock = new ReloadPanels();
	    try {
			when(stashSlotItemServiceMock.swapItemWithInventory(any(Account.class), any(StashSlotItemSuper.class), anyInt())).thenReturn(reloadPanelsMock);
		} catch (InventoryNotFound e) {
			e.printStackTrace();
		} catch (DungeonNotFound e) {
			e.printStackTrace();
		} catch (AccountIdMismatch e) {
			e.printStackTrace();
		} catch (EquipmentNotFound e) {
			e.printStackTrace();
		} catch (InventoryException e) {
			e.printStackTrace();
		} catch (CantEquipToDungeon e) {
			e.printStackTrace();
		} catch (BoostItemNotFound e) {
			e.printStackTrace();
		} catch (CantEquipToMessage e) {
			e.printStackTrace();
		} catch (CharacterNotFound e) {
			e.printStackTrace();
		} catch (CharacterSlotNotFound e) {
			e.printStackTrace();
		} catch (CharacterEquipmentNotFound e) {
			e.printStackTrace();
		} catch (MessageEquipmentNotFound e) {
			e.printStackTrace();
		}
		
		attributes2 = new HashMap<Integer, Integer>();
		testEquipment2 = new Equipment(EQUIPMENT_ID2, EQUIPMENT_TYPE2, EQUIPMENT_QUALITY2, LEVEL2, 
				false, false, null, 0, null, 0, attributes2, null, 0, EquipmentLocation.CHARACTER, CHAR_ID2);
		testEquipmentNoId2 = new Equipment(-1, EQUIPMENT_TYPE2, EQUIPMENT_QUALITY2, LEVEL2, 
				false, false, null, 0, null, 0, attributes2, null, 0, EquipmentLocation.CHARACTER, CHAR_ID2);
        when(equipmentServiceMock.findById(eq(EQUIPMENT_ID2))).thenReturn(testEquipment2);
        when(equipmentServiceMock.create(eq(testEquipmentNoId2))).thenReturn(testEquipment2);        
        
        when(equipmentServiceMock.create(any(Equipment.class))).thenReturn(testEquipment);		
		
        for(CharSlot slot:CharSlot.values()) {
			Equipment equipment = createRandomEquipment();
			testCharacterSlots.put(slot, equipment);
		}

        testCharacterWithEquipment = new Character(CHAR_ID_WITH_EQUIPMENT, testAccount, false, false, CHAR_NAME, CHAR_CLASS, CHAR_LEVEL, CHAR_XP, CHAR_PRESTIGE_LEVEL, null /* Alive */, null /* Dungeon */);
		when(characterServiceMock.findById(eq(CHAR_ID_WITH_EQUIPMENT))).thenReturn(testCharacterWithEquipment);
        testCharacterEquipment = new CharacterEquipment(testCharacterWithEquipment, testCharacterSlots);
		when(characterEquipmentServiceMock.findById(eq(CHAR_ID_WITH_EQUIPMENT))).thenReturn(testCharacterEquipment);
		
		testHiscore = new Hiscore(1, testAccount, HiscoreType.TOTAL_LEVEL, false, false, new Date(), 2);
    }

	// Helper methods
	/**
	 * 
	 * @return
	 */
	protected Equipment createRandomEquipment() {
		int level = (int)(Math.random()*Integer.MAX_VALUE);
		int equipmentTypeId = (int)(Math.random()*EquipmentType.values().length-1)+1; // Don't use 0
		EquipmentType equipmentType = EquipmentType.fromId(equipmentTypeId);
		return createRandomEquipment(equipmentType, level);
	}
	
	protected Equipment createRandomEquipment(EquipmentType equipmentType, int level) {
		HashMap<Integer, Integer> attributes = new HashMap<Integer, Integer>();
		long equipmentId = (long)(Math.random()*Long.MAX_VALUE);
		int qualityId = (int)(Math.random()*EquipmentQuality.values().length-1)+1; // Don't use 0
		EquipmentQuality quality = EquipmentQuality.fromId(qualityId);
		Equipment equipment = new Equipment(equipmentId, equipmentType, quality, level, false, false, 
				EquipmentAttribute.STRENGTH, level, EquipmentAttribute.DODGE, level, 
				attributes, null, 0, EquipmentLocation.CHARACTER, CHAR_ID);
		when(equipmentServiceMock.findById(eq(equipmentId))).thenReturn(equipment);
		when(accountServiceMock.accountOwnsEquipment(eq(testAccount), eq(equipment))).thenReturn(true);
		return equipment;
	}
	
	protected Character createRandomCharacter(long charId, int level) {
		return new Character(charId, testAccount, false, false, "Test Char "+charId, CharClass.getRandom(), level, XP2, CHAR_PRESTIGE_LEVEL, null /* Alive */, null /* Dungeon */);
	}
	
	protected CharacterEquipment createRandomCharacterEquipment(long charId, int level) {
		Map<CharSlot, Equipment> characterSlots = new HashMap<CharSlot, Equipment>();
		for(CharSlot slot:CharSlot.values()) {
			if(slot == CharSlot.UNKNOWN) continue;
			characterSlots.put(slot, createRandomEquipment(slot.getValidEquipment(), level));
		}
		
		CharacterEquipment characterEquipment = new CharacterEquipment(testCharacter, characterSlots);
		when(characterEquipmentServiceMock.findById(eq(charId))).thenReturn(characterEquipment);
		
		return characterEquipment;
	}
	
	protected void restrictMockAccess() {
		verifyNoMoreInteractions(accountServiceMock);
		verifyNoMoreInteractions(accountCurrencyServiceMock);
		verifyNoMoreInteractions(characterServiceMock);
		verifyNoMoreInteractions(dungeonServiceMock);
		verifyNoMoreInteractions(characterEquipmentServiceMock);
		verifyNoMoreInteractions(equipmentServiceMock);
		verifyNoMoreInteractions(inventoryServiceMock);
	}
}