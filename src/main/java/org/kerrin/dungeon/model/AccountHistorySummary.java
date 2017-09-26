package org.kerrin.dungeon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kerrin.dungeon.enums.AccountMode;

public class AccountHistorySummary {
	public Map<AccountMode,AccountHistorySummaries> accountSummaries = new HashMap<AccountMode,AccountHistorySummaries>();
	/**  */
	public List<AccountCurrencyAudit> accountHistories = new ArrayList<AccountCurrencyAudit>();
	
	public void add(AccountCurrencyAudit audit) {
		AccountMode accountMode = AccountMode.fromModeFlags(audit.isHardcore(), audit.isIronborn());
		AccountHistorySummaries summary;
		if(accountSummaries.containsKey(accountMode)) {
			summary = accountSummaries.get(accountMode);
		} else {
			summary = new AccountHistorySummaries();
		}
		summary.add(audit);
		accountSummaries.put(accountMode, summary);
		
		accountHistories.add(audit);
	}
	
	public AccountHistorySummaries getSummaries(boolean hardcore, boolean ironborn) {
		AccountMode mode = AccountMode.fromModeFlags(hardcore, ironborn);
		return accountSummaries.get(mode);
	}
	
	public AccountCurrencyAudit[] getAccountAudits() {
		return accountHistories.toArray(new AccountCurrencyAudit[0]);
	}
}
