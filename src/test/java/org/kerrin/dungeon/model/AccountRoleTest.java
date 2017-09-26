package org.kerrin.dungeon.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.SuperTest;
import org.kerrin.dungeon.enums.AccountPrivilege;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountRoleTest extends SuperTest {
	
	@Test
	public void testRoles() {		
		AccountRole accountRole = new AccountRole(testAccount, AccountPrivilege.USER);
		
		assertEquals(ACCOUNT_ID, accountRole.getAccount().getId());
		assertEquals(USERNAME, accountRole.getAccount().getUsername());
		assertEquals(DISPLAYNAME, accountRole.getAccount().getDisplayName());
		assertEquals(previousLogin, accountRole.getAccount().getPreviousLogin());
		assertEquals(lastLogin, accountRole.getAccount().getLastLogin());
		assertEquals(AccountPrivilege.USER, accountRole.getRole());
		
		accountRole = new AccountRole(testAccount, AccountPrivilege.VIEW);
		assertEquals(AccountPrivilege.VIEW, accountRole.getRole());
		
		accountRole = new AccountRole(testAccount, AccountPrivilege.MODIFY);
		assertEquals(AccountPrivilege.MODIFY, accountRole.getRole());
		
		accountRole = new AccountRole(testAccount, AccountPrivilege.DELETE);
		assertEquals(AccountPrivilege.DELETE, accountRole.getRole());
		restrictMockAccess();
	}

}
