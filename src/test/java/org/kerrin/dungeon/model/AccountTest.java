package org.kerrin.dungeon.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.SuperTest;
import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.AccountTask;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountTest extends SuperTest {
	@Test
	public void testConstructor() {
		assertEquals(ACCOUNT_ID, testAccount.getId());
		assertEquals(USERNAME, testAccount.getUsername());
		assertEquals(DISPLAYNAME, testAccount.getDisplayName());
		assertEquals(previousLogin, testAccount.getPreviousLogin());
		assertEquals(lastLogin, testAccount.getLastLogin());
		Account testAccount2 = testAccount;
		assertTrue(testAccount.equals(testAccount2));
		restrictMockAccess();
	}

	@Test
	public void testSetMethods() {
		testAccount.setId(ACCOUNT_ID2);
		testAccount.setUsername(USERNAME2);
		try {
			testAccount.setPassword(PASSWORD);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		testAccount.setDisplayName(DISPLAYNAME2);
		lastLogin = new Date(now.getTime()-1234567890L);
		testAccount.setLastLogin(lastLogin);
		previousLogin = new Date(lastLogin.getTime()-123456789L);
		testAccount.setPreviousLogin(previousLogin);
		
		testAccount.setOnHoliday(true);
		testAccount.setLastProcessed(AccountTask.DAILY_CHECKS, now);
		int loginTokens = (int)(Math.random() * 123456789);
		testAccount.setLoginTokens(loginTokens);
		
		try {
			testAccount.setPassword(PASSWORD);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(ACCOUNT_ID2, testAccount.getId());
		assertEquals(USERNAME2, testAccount.getUsername());
		assertTrue("Password check failed, hashed password was "+testAccount.getHashedPassword(), testAccount.passwordMatch(PASSWORD));
		testAccount.setHashedPassword(HASHED_PASSWORD);
		assertEquals(HASHED_PASSWORD, testAccount.getHashedPassword());
		assertEquals(DISPLAYNAME2, testAccount.getDisplayName());
		assertEquals(previousLogin, testAccount.getPreviousLogin());
		assertEquals(lastLogin, testAccount.getLastLogin());
		assertEquals(true, testAccount.isOnHoliday());
		assertEquals(now, testAccount.getLastProcessed(AccountTask.DAILY_CHECKS));
		assertEquals(loginTokens, testAccount.getLoginTokens());
		
		testAccount.increaseLoginTokens(123);
		assertEquals(loginTokens+123, testAccount.getLoginTokens());
		testAccount.incrementLoginTokens();
		assertEquals(loginTokens+124, testAccount.getLoginTokens());
		restrictMockAccess();
	}

	@Test
	public void testEquals() {
		Account testAccount2 = new Account();
		testAccount2.setId(ACCOUNT_ID);
		assertTrue(testAccount.equals(testAccount2));
		
		testAccount2 = new Account();
		testAccount2.setId(ACCOUNT_ID2);
		assertFalse(testAccount.equals(testAccount2));
		restrictMockAccess();
	}
	
	@Test
	public void testRoles() {
		Set<AccountRole> roles = new HashSet<AccountRole>();
		testAccount = new Account(ACCOUNT_ID, USERNAME, "", DISPLAYNAME, previousLogin, lastLogin, roles, false, false, 1);
		assertFalse(testAccount.hasRole(AccountPrivilege.USER));
		assertFalse(testAccount.hasRole(AccountPrivilege.VIEW));
		assertFalse(testAccount.hasRole(AccountPrivilege.MODIFY));
		assertFalse(testAccount.hasRole(AccountPrivilege.DELETE));
		
		AccountRole accountRoleUser = new AccountRole(testAccount, AccountPrivilege.USER);
		AccountRole accountRoleView = new AccountRole(testAccount, AccountPrivilege.VIEW);
		AccountRole accountRoleModify = new AccountRole(testAccount, AccountPrivilege.MODIFY);
		AccountRole accountRoleDelete = new AccountRole(testAccount, AccountPrivilege.DELETE);
		
		roles = new HashSet<AccountRole>();
		roles.add(accountRoleUser);
		testAccount.setRoles(roles);
		assertTrue(testAccount.hasRole(AccountPrivilege.USER));
		assertFalse(testAccount.hasRole(AccountPrivilege.VIEW));
		assertFalse(testAccount.hasRole(AccountPrivilege.MODIFY));
		assertFalse(testAccount.hasRole(AccountPrivilege.DELETE));
		
		roles = new HashSet<AccountRole>();
		roles.add(accountRoleView);
		testAccount.setRoles(roles);
		assertFalse(testAccount.hasRole(AccountPrivilege.USER));
		assertTrue(testAccount.hasRole(AccountPrivilege.VIEW));
		assertFalse(testAccount.hasRole(AccountPrivilege.MODIFY));
		assertFalse(testAccount.hasRole(AccountPrivilege.DELETE));

		roles = new HashSet<AccountRole>();
		roles.add(accountRoleModify);
		testAccount.setRoles(roles);
		assertFalse(testAccount.hasRole(AccountPrivilege.USER));
		assertFalse(testAccount.hasRole(AccountPrivilege.VIEW));
		assertTrue(testAccount.hasRole(AccountPrivilege.MODIFY));
		assertFalse(testAccount.hasRole(AccountPrivilege.DELETE));
		
		roles = new HashSet<AccountRole>();
		roles.add(accountRoleDelete);
		testAccount.setRoles(roles);
		assertFalse(testAccount.hasRole(AccountPrivilege.USER));
		assertFalse(testAccount.hasRole(AccountPrivilege.VIEW));
		assertFalse(testAccount.hasRole(AccountPrivilege.MODIFY));
		assertTrue(testAccount.hasRole(AccountPrivilege.DELETE));
		
		roles = new HashSet<AccountRole>();
		roles.add(accountRoleUser);
		roles.add(accountRoleView);
		roles.add(accountRoleModify);
		roles.add(accountRoleDelete);
		testAccount.setRoles(roles);
		assertTrue(testAccount.hasRole(AccountPrivilege.USER));
		assertTrue(testAccount.hasRole(AccountPrivilege.VIEW));
		assertTrue(testAccount.hasRole(AccountPrivilege.MODIFY));
		assertTrue(testAccount.hasRole(AccountPrivilege.DELETE));
		restrictMockAccess();
	}
}
