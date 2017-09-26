package org.kerrin.dungeon.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kerrin.dungeon.enums.AchievementType;

/**
 * Database record for a users account
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="achievement")
public class Achievement {

	/** The identifier */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id = -1;
	
	@ManyToOne
	@JoinColumn(name="account")
	private Account account;
	
	@Enumerated(EnumType.ORDINAL)
	private AchievementType type;
	
	/** Is this record for hardcore characters */
	private Boolean hardcore;
	
	/** Is this record for ironborn characters */
	private Boolean ironborn;
	
	/** Date achievement accomplished, or null if not achieved */
	private Date timestamp;
		
	protected Achievement() {}
	
	public Achievement(AchievementType type) {
		super();
		this.id = -1;
		this.account = null;
		this.type = type;
		this.hardcore = null;
		this.ironborn = null;
		this.timestamp = null;
	}

	public Achievement(int id, Account account, AchievementType type, Boolean hardcore, Boolean ironborn, Date timestamp) {
		super();
		this.id = id;
		this.account = account;
		this.type = type;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.timestamp = timestamp;
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public AchievementType getType() {
		return type;
	}

	public void setType(AchievementType type) {
		this.type = type;
	}

	public Boolean isHardcore() {
		return hardcore;
	}

	public void setHardcore(Boolean hardcore) {
		this.hardcore = hardcore;
	}

	public Boolean isIronborn() {
		return ironborn;
	}

	public void setIronborn(Boolean ironborn) {
		this.ironborn = ironborn;
	}

	public long getPoints() {
		return type.getPoints();
	}
}
