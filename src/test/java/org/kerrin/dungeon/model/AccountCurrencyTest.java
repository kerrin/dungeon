package org.kerrin.dungeon.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.SuperTest;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountCurrencyTest extends SuperTest {

	@Test
	public void test() {		
		AccountCurrency accountCurrency = new AccountCurrency(ACCOUNT_ID, false, false, CURRENCY);
		
		assertEquals(ACCOUNT_ID, accountCurrency.getAccountId());
		assertEquals(CURRENCY, accountCurrency.getCurrency());
		restrictMockAccess();
	}

}
