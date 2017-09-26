package org.kerrin.dungeon.forms;

import javax.validation.constraints.Min;

import org.kerrin.dungeon.enums.BooleanOptions;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.model.Dungeon;

/**
 * Form data for a Dungeon
 * 
 * @author Kerrin
 *
 */
public class DungeonSearchForm {
	/** The Dungeon identifier */
	@Min(-1)
	private long id = -1;

	@Min(-1)
	private long accountId = -1;
	
	private BooleanOptions hardcore;
	
	private BooleanOptions ironborn;
	
	private DungeonType type = DungeonType.NONE;
	
	@Min(-1)
	private int greaterThanLevel = -1;
	@Min(-1)
	private int lessThanLevel = -1;	
	
	@Min(-1)
	private long greaterThanXpReward = -1;
	@Min(-1)
	private long lessThanXpReward = -1;
	
	private Monster monster = Monster.UNKNOWN;
	
	@Min(-1)
	private int greaterThanPartySize = -1;
	@Min(-1)
	private int lessThanPartySize = -1;

	public DungeonSearchForm() {}

	public DungeonSearchForm(Dungeon dungeon) {
		this(dungeon.getId(), 
				dungeon.getAccount().getId(),
				BooleanOptions.fromBoolean(dungeon.isHardcore()),
				BooleanOptions.fromBoolean(dungeon.isIronborn()),
				dungeon.getType(),
				dungeon.getLevel()-1,
				dungeon.getLevel()+1,
				dungeon.getXpReward()-1,
				dungeon.getXpReward()+1,
				null, // Monster
				dungeon.getPartySize()-1,
				dungeon.getPartySize()+1
				);
	}
	
	public DungeonSearchForm(long id, long accountId, BooleanOptions hardcore, BooleanOptions ironborn, 
			DungeonType type, int greaterThanLevel, int lessThanLevel, 
			long greaterThanXpReward, long lessThanXpReward, Monster monster,
			int greaterThanPartySize, int lessThanPartySize) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.type = type;
		this.greaterThanLevel = greaterThanLevel;
		this.lessThanLevel = lessThanLevel;
		this.greaterThanXpReward = greaterThanXpReward;
		this.lessThanXpReward = lessThanXpReward;
		this.monster = monster;
		this.greaterThanPartySize = greaterThanPartySize;
		this.lessThanPartySize = lessThanPartySize;
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

	public DungeonType getType() {
		return type;
	}

	public void setType(DungeonType type) {
		this.type = type;
	}

	public int getGreaterThanLevel() {
		return greaterThanLevel;
	}

	public void setGreaterThanLevel(int greaterThanLevel) {
		this.greaterThanLevel = greaterThanLevel;
	}

	public int getLessThanLevel() {
		return lessThanLevel;
	}

	public void setLessThanLevel(int lessThanLevel) {
		this.lessThanLevel = lessThanLevel;
	}

	public long getGreaterThanXpReward() {
		return greaterThanXpReward;
	}

	public void setGreaterThanXpReward(long greaterThanXpReward) {
		this.greaterThanXpReward = greaterThanXpReward;
	}

	public long getLessThanXpReward() {
		return lessThanXpReward;
	}

	public void setLessThanXpReward(long lessThanXpReward) {
		this.lessThanXpReward = lessThanXpReward;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	public int getGreaterThanPartySize() {
		return greaterThanPartySize;
	}

	public void setGreaterThanPartySize(int greaterThanPartySize) {
		this.greaterThanPartySize = greaterThanPartySize;
	}

	public int getLessThanPartySize() {
		return lessThanPartySize;
	}

	public void setLessThanPartySize(int lessThanPartySize) {
		this.lessThanPartySize = lessThanPartySize;
	}

	@Override
	public String toString() {
		return "DungeonSearchForm [" +
				"id=" + id + ", " +
				"accountId=" + accountId + ", " + 
				"hardcore=" + (hardcore.getBooleanValue()==null?"null":hardcore.getBooleanValue()?"Yes":"No") + ", " + 
				"ironborn=" + (ironborn.getBooleanValue()==null?"null":ironborn.getBooleanValue()?"Yes":"No") + ", " + 
				"type=" + type + ", " +
				"greaterThanLevel=" + greaterThanLevel + ", " +
				"lessThanLevel=" + lessThanLevel + ", " +
				"greaterThanXpReward=" + greaterThanXpReward + ", " +
				"lessThanXpReward=" + lessThanXpReward + ", " +
				"monster=" + monster + ", " +
				"greaterThanPartySize=" + greaterThanPartySize + ", " +
				"lessThanPartySize=" + lessThanPartySize + 
				"]";
	}
}
