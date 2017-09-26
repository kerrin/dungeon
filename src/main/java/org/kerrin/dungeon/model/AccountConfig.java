package org.kerrin.dungeon.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kerrin.dungeon.enums.AccountConfigType;

/**
 * Database record for a users account
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="account_config")
public class AccountConfig {
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
	
	/** The config type */
	private AccountConfigType configType;

	/** The config value */
	private int configValue;

	protected AccountConfig() {}
	
	public AccountConfig(long accountId, boolean hardcore, boolean ironborn, AccountConfigType configType, int configValue) {
		super();
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.configType = configType;
		this.configValue = configValue;
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

	public AccountConfigType getType() {
		return configType;
	}
	
	public int getValue() {
		return configValue;
	}

	/**
	 * Set the config value
	 * 
	 * @param currency
	 */
	public void setValue(int configValue) {
		this.configValue = configValue;
	}
}
