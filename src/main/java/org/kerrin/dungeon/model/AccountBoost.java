package org.kerrin.dungeon.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kerrin.dungeon.enums.BoostItemType;

/**
 * Database record for a users account
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="account_boost")
public class AccountBoost {
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
	private BoostItemType type;
	
	/** The date and time the boost ends */
	private Date endDateTime = null;
	
	protected AccountBoost() {}
	
	public AccountBoost(long accountId, boolean hardcore, boolean ironborn, BoostItemType type, Date endDateTime) {
		super();
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.type = type;
		this.endDateTime = endDateTime;
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

	public BoostItemType getType() {
		return type;
	}

	public void setType(BoostItemType type) {
		this.type = type;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public void increaseExpiry(AccountBoost accountBoost, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.endDateTime);
		cal.add(Calendar.MINUTE, minutes);
		this.endDateTime = cal.getTime();
	}
}
