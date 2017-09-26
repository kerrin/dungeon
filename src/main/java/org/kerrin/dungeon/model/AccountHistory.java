package org.kerrin.dungeon.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A month of the account history, starting from start date
 * 
 * @author Kerrin
 *
 */
public class AccountHistory {
	private Date startDate;
	
	/** Maps day of month to account summary */
	public Map<Integer,AccountHistorySummary> accountHistorySummaries = new HashMap<Integer,AccountHistorySummary>();
	private String startMonth;
	private String endMonth;

	private int startDay;

	public AccountHistory(Date startDate, Locale locale) {
		this.startDate = startDate;
		Calendar cal = Calendar.getInstance();
		startDay = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(startDate);
		endMonth = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
		cal.add(Calendar.MONTH, -1);
		startMonth = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
	}
	
	public void add(AccountCurrencyAudit audit) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(audit.getTimestamp());
		int day = cal.get(Calendar.DAY_OF_MONTH);
		AccountHistorySummary daySummary;
		if(accountHistorySummaries.containsKey(day)) {
			daySummary = accountHistorySummaries.get(day);
		} else {
			daySummary = new AccountHistorySummary();
		}
		daySummary.add(audit);
		accountHistorySummaries.put(day, daySummary);
	}
	
	public Integer[] getDays() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.MONTH, 1);
		List<Integer> days = new ArrayList<Integer>();
		int day = startDay;
		days.add(day);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		day = cal.get(Calendar.DAY_OF_MONTH);
		while(day != startDay) {
			days.add(day);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			day = cal.get(Calendar.DAY_OF_MONTH);	
		}
		
		return days.toArray(new Integer[0]);
	}
	
	public int getStartDay() {
		return startDay;
	}
	
	public String getStartMonth() {
		return startMonth;
	}
	
	public String getEndMonth() {
		return endMonth;
	}
	
	public AccountHistorySummary getDaySummary(int day) {
		return accountHistorySummaries.get(day);
	}
}
