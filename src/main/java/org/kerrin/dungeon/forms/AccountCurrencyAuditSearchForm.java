package org.kerrin.dungeon.forms;

import java.util.Calendar;
import java.util.Date;

import org.kerrin.dungeon.enums.BooleanOptions;

/**
 * Form data for a Account search request
 * 
 * @author Kerrin
 *
 */
public class AccountCurrencyAuditSearchForm {
	/** The Account identifier */
	private long accountId = -1;
	
	/** Is this record for hardcore characters */
	private BooleanOptions hardcore;
	
	/** Is this record for ironborn characters */
	private BooleanOptions ironborn;
	
	/** Date of first audit we want to see */
	private Date startDate;
	
	/** Date of last audit we want to see */
	private Date endDate;
	
	public AccountCurrencyAuditSearchForm() {};	
	
	public AccountCurrencyAuditSearchForm(long accountId, BooleanOptions hardcore, BooleanOptions ironborn, Date startDate, Date endDate) {
		super();
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public BooleanOptions getHardcore() {
		return hardcore==null?BooleanOptions.BOTH:hardcore;
	}

	public void setHardcore(BooleanOptions hardcore) {
		this.hardcore = hardcore;
	}

	public BooleanOptions getIronborn() {
		return ironborn==null?BooleanOptions.BOTH:ironborn;
	}

	public void setIronborn(BooleanOptions ironborn) {
		this.ironborn = ironborn;
	}

	public Date getStartDate() {
		if(startDate == null) {
			Calendar cal = Calendar.getInstance();
			cal.set(2016, 1/*Feb*/, 1);
			startDate = cal.getTime();
		}
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		if(endDate == null) endDate = new Date();
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "AccountCurrencyAuditSearchForm [" +
					"accountId=" + accountId + ", " +
					"hardcore=" + (hardcore.getBooleanValue()==null?"null":hardcore.getBooleanValue()?"Yes":"No") + ", " +
					"ironborn=" + (ironborn.getBooleanValue()==null?"null":ironborn.getBooleanValue()?"Yes":"No") + ", " +
					"startDate=" + startDate + ", " +
					"endDate=" + endDate + 
				"]";
	}
}
