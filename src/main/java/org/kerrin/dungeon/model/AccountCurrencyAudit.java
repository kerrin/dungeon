package org.kerrin.dungeon.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kerrin.dungeon.enums.ModificationType;

/**
 * Database record for a users account
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="account_currency_audit")
public class AccountCurrencyAudit {
	/** The identifier */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id = -1;
	
	private long accountId;
	
	/** Is this record for hardcore characters */
	private boolean hardcore;
	
	/** Is this record for ironborn characters */
	private boolean ironborn;
	
	/** When this transaction occurred */
	private Date timestamp;
	
	/** The amount of currency the user was awarded (+ve) or deducted (-ve) */
	private long currency;
	
	/** The type of modification */
	private ModificationType modificationType;
	
	/** The unique reference */
	private String reference;

	protected AccountCurrencyAudit() {}

	public AccountCurrencyAudit(long accountId, boolean hardcore, boolean ironborn, Date timestamp, long currency, ModificationType modificationType, String reference) {
		super();
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.timestamp = timestamp;
		this.currency = currency;
		this.modificationType = modificationType;
		this.reference = reference;
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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	public ModificationType getModificationType() {
		return modificationType;
	}

	public void setModificationType(ModificationType modificationType) {
		this.modificationType = modificationType;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}
