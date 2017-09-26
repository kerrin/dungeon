package org.kerrin.dungeon.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stubVoid;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountConfig;
import org.kerrin.dungeon.model.AccountCurrency;
import org.kerrin.dungeon.model.AccountRole;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.repository.AccountMessageRepo;
import org.kerrin.dungeon.repository.AccountRepo;
import org.kerrin.dungeon.service.impl.AccountServiceImpl;
import org.springframework.core.task.TaskExecutor;

/**
 * This unit test isn't really testing much
 * 
 * @author Kerrin
 *
 */
public class AccountCurrencyServiceTest {
	private static final long ACCOUNT_ID = 1;
	private static final String USERNAME = "testuser";
	private static final String DISPLAYNAME = "Test User";
	private static final long CURRENCY_ID = 2;
	private static final int EQUIPMENT_COMPARE_VALUE = 0;
	private static final int TOOL_TIPS_VALUE = 0;
	
	AccountServiceImpl accountService;
	private Date now;
	private Date lastLogin;
	private Date previousLogin;
	private Account testAccount;
	private Set<AccountRole> roles = new HashSet<AccountRole>();
	
	private AccountRepo accountRepo;
	private AccountConfigService accountConfigService;
	private AccountCurrencyService accountCurrencyService;
	private AccountRoleService accountRoleService;
	private CharacterService characterService;
	private CharacterEquipmentService characterEquipmentService;
	private InventoryService inventoryService;
	private DungeonService dungeonService;
	private EquipmentService equipmentService;
	private BoostItemService boostItemService;
	private AccountBoostService accountBoostService;
	private AccountMessageRepo accountMessageRepo;
	private HiscoreService hiscoreService;
	private TaskExecutor taskExecuter;
	private ServletContext servletContext;
	private AccountConfig testAccountConfigEquipmentCompare;
	private AccountConfig testAccountConfigToolTips;
	private AccountCurrency testAccountCurrency;
	private Inventory testInventory;
	
	@Before
	public void setup() {
		accountRepo = mock(AccountRepo.class);
		accountMessageRepo = mock(AccountMessageRepo.class);
		accountConfigService = mock(AccountConfigService.class);
		accountCurrencyService = mock(AccountCurrencyService.class);
		accountRoleService = mock(AccountRoleService.class);
		characterService = mock(CharacterService.class);
		characterEquipmentService = mock(CharacterEquipmentService.class);
		inventoryService = mock(InventoryService.class);
		dungeonService = mock(DungeonService.class);
		equipmentService = mock(EquipmentService.class);
		boostItemService = mock(BoostItemService.class);
		accountBoostService = mock(AccountBoostService.class);
		hiscoreService = mock(HiscoreService.class);
		taskExecuter = mock(TaskExecutor.class);
		servletContext = mock(ServletContext.class);
		
		accountService = new AccountServiceImpl(accountRepo, accountMessageRepo, 
				accountConfigService, accountCurrencyService, accountRoleService, 
				characterService, characterEquipmentService, inventoryService, dungeonService, 
				equipmentService, boostItemService, accountBoostService, 
				hiscoreService,
				taskExecuter, servletContext);
		now = new Date();
		lastLogin = new Date(now.getTime()-123456789L);
		previousLogin = new Date(lastLogin.getTime()-123456789L);
		
		testAccount = new Account(ACCOUNT_ID, USERNAME, "", DISPLAYNAME, previousLogin, lastLogin, roles, false, false, 1);
		testAccountConfigEquipmentCompare = new AccountConfig(ACCOUNT_ID, false, false, AccountConfigType.EQUIPMENT_COMPARE, EQUIPMENT_COMPARE_VALUE);
		testAccountConfigToolTips = new AccountConfig(ACCOUNT_ID, false, false, AccountConfigType.TOOL_TIPS, TOOL_TIPS_VALUE);
		testAccountCurrency = new AccountCurrency(ACCOUNT_ID, false, false, CURRENCY_ID);
		testInventory = new Inventory(testAccount, false, false);
		
		
	}
	
	@Test
	public void testCreate() {
		when(accountRepo.save(any(Account.class))).thenReturn(testAccount);
		when(accountConfigService.create(eq(testAccountConfigEquipmentCompare))).thenReturn(testAccountConfigEquipmentCompare);
		when(accountConfigService.create(eq(testAccountConfigToolTips))).thenReturn(testAccountConfigToolTips);
		when(accountCurrencyService.create(any(AccountCurrency.class), anyString())).thenReturn(testAccountCurrency);
		when(inventoryService.create(any(Inventory.class))).thenReturn(testInventory);
		Account resultAccount = accountService.create(testAccount);
		
		// Not much to test due the mocking, if we get here we are probably done really, but lets do this test anyway
		assertEquals(testAccount.getId(), resultAccount.getId());
	}

	@Test
	public void testDelete() {
		when(accountRepo.findOne(anyLong())).thenReturn(testAccount);
		stubVoid(accountRepo).toReturn().on().delete(anyLong());
		Account resultAccount = null;
		try {
			resultAccount = accountService.delete(ACCOUNT_ID);
		} catch (AccountNotFound e) {
			fail(e.getMessage());
		}
		
		// Not much to test due the mocking, if we get here we are probably done really, but lets do this test anyway
		assertEquals(testAccount.getId(), resultAccount.getId());
	}
}
