package org.kerrin.dungeon.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Database record for a users account
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="account_currency")
public class AccountCurrency {
	/** The identifier */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	/** The account identifier */
	private long accountId;
	
	/** Is this record for hardcore characters */
	private boolean hardcore;
	
	/** Is this record for ironborn characters */
	private boolean ironborn;
	
	/** The amount of currency the user has */
	private long currency;
	
	protected AccountCurrency() {}
	
	public AccountCurrency(long accountId, boolean hardcore, boolean ironborn, long currency) {
		super();
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.currency = currency;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public boolean isHardcore() {
		return hardcore;
	}

	public void setHardcore(boolean hardcore) {
		this.hardcore = hardcore;
	}

	public boolean isIronborn() {
		return ironborn;
	}

	public void setIronborn(boolean ironborn) {
		this.ironborn = ironborn;
	}

	public long getCurrency() {
		return currency;
	}

	/**
	 * Add the modification amount to the currency
	 * If negative, will reduce the currency amount.
	 * If the resulting currency is less than 0, the currency final value is 0
	 * 
	 * @param modifyCurrencyAmount
	 */
	public boolean modifyCurrency(long currencyAdjustment) {
		long tempCurrency = currency + currencyAdjustment;
        if(tempCurrency < 0) {
        	return false;
        }
        currency = tempCurrency;
        
        return true;
	}

	/**
	 * Set the currency amount directly. Only for use by account currency service
	 * 
	 * @param currency
	 */
	public void setCurrency(long currency) {
		this.currency = currency;
	}
}
