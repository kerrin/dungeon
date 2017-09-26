package org.kerrin.dungeon.model;

import java.util.HashMap;
import java.util.Map;

import org.kerrin.dungeon.enums.ModificationType;

public class AccountHistorySummaries {
	/** The amounts per modification type */
	public Map<ModificationType,Long> amountsPerModificationType = new HashMap<ModificationType, Long>();
	
	/**
	 * Add the audit to the totals
	 * @param audit
	 */
	public void add(AccountCurrencyAudit audit) {
		ModificationType modificationType = audit.getModificationType();
		long total = 0;
		if(amountsPerModificationType.containsKey(modificationType)) {
			total = amountsPerModificationType.get(modificationType);
		} else {
			total = 0;
		}
		total += audit.getCurrency();
		amountsPerModificationType.put(modificationType, total);
	}
	
	public ModificationType[] getModificationTypes() {
		return amountsPerModificationType.keySet().toArray(new ModificationType[0]);
	}
	
	public long getValue(ModificationType type) {
		return amountsPerModificationType.get(type);
	}
}
