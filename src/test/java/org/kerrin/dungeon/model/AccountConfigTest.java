package org.kerrin.dungeon.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.SuperTest;
import org.kerrin.dungeon.enums.AccountConfigType;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountConfigTest extends SuperTest {

	@Test
	public void test() {		
		AccountConfig accountConfig = new AccountConfig(ACCOUNT_ID, false, false, ACCOUNT_CONFIG_TYPE, ACCOUNT_CONFIG_VALUE);
		
		assertEquals(ACCOUNT_ID, accountConfig.getAccountId());
		assertEquals(ACCOUNT_CONFIG_TYPE, accountConfig.getType());
		assertEquals(ACCOUNT_CONFIG_VALUE, accountConfig.getValue());
		restrictMockAccess();
	}

}
