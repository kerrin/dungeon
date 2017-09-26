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

import org.kerrin.dungeon.enums.HiscoreType;
import org.kerrin.dungeon.utils.CommonTools;

/**
 * Database record for a users account
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="hiscore")
public class Hiscore {

	/** The identifier */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id = -1;
	
	@ManyToOne
	@JoinColumn(name="account")
	private Account account;
	
	@Enumerated(EnumType.ORDINAL)
	private HiscoreType type;
	
	/** Is this record for hardcore characters */
	private Boolean hardcore;
	
	/** Is this record for ironborn characters */
	private Boolean ironborn;
	
	/** Date of highscore */
	private Date timestamp;
	
	private long score;
	
	private int rank = -1;
	
	protected Hiscore() {}

	public Hiscore(int id, Account account, HiscoreType type, Boolean hardcore, Boolean ironborn, Date timestamp, long score) {
		super();
		this.id = id;
		this.account = account;
		this.type = type;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.timestamp = timestamp;
		this.score = score;
		this.rank = -1;
		
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

	public HiscoreType getType() {
		return type;
	}

	public void setType(HiscoreType type) {
		this.type = type;
	}

	public Boolean getHardcore() {
		return hardcore;
	}

	public void setHardcore(Boolean hardcore) {
		this.hardcore = hardcore;
	}

	public Boolean getIronborn() {
		return ironborn;
	}

	public void setIronborn(Boolean ironborn) {
		this.ironborn = ironborn;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getDisplayScore() {
		switch(type) {
		case FASTEST_MAX_LEVEL: case FASTEST_MAX_LEVEL_AFTER_RESET:
			int days = (int)(score % CommonTools.DAYS_INMILLIS);
			score /= CommonTools.DAYS_INMILLIS;
			int hours = (int)(score % CommonTools.HOURS_INMILLIS);
			score /= CommonTools.HOURS_INMILLIS;
			int minutes = (int)(score % CommonTools.MINUTES_INMILLIS);
			score /= CommonTools.MINUTES_INMILLIS;
			int seconds = (int)(score % CommonTools.SECONDS_INMILLIS);
			score /= CommonTools.SECONDS_INMILLIS;
			return days+"d:"+hours+"h:"+minutes+"m:"+seconds+ "s"; 
		case HIGHEST_LEVEL: case TOTAL_LEVEL: case TOKENS_EARNT: case TOKENS_PURCHASED:
			return ""+score;
		}
		return null;
	}
}
