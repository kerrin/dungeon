package org.kerrin.dungeon.forms;

import javax.validation.constraints.Min;

import org.kerrin.dungeon.enums.BooleanOptions;
import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.model.BoostItem;

/**
 * Form data for a BoostItem
 * 
 * @author Kerrin
 *
 */
public class BoostItemSearchForm {
	/** The BoostItem identifier */
	@Min(-1)
	private long id = -1;

	/** The account identifier */
	@Min(-1)
	private long accountId = -1;
	
	@Min(-1)
	private int greaterThanLevel;
	
	@Min(-1)
	private int lessThanLevel;
	
	private BooleanOptions hardcore;
	
	private BooleanOptions ironborn;
	
	private BoostItemType boostItemType;

	/** Stash slot it is in, if it is in the stash */
	@Min(-1)
	private long stashSlotId;
	/** Dungeon it is in, if it is in a dungeon */
	@Min(-1)
	private long dungeonId;
	/** Message it is attached to, if it is in a message */
	@Min(-1)
	private long messageId;
	
	public BoostItemSearchForm() {}

	public BoostItemSearchForm(BoostItem boostItem) {
		this(boostItem.getId(), 
				boostItem.getAccount().getId(), 
				boostItem.getLevel()-1,
				boostItem.getLevel()+1,
				BooleanOptions.fromBoolean(boostItem.isHardcore()),
				BooleanOptions.fromBoolean(boostItem.isIronborn()),
				boostItem.getBoostItemType(),
				boostItem.getStashSlotId(),
				boostItem.getDungeonId(),
				boostItem.getMessageId()
				);
	}
	
	public BoostItemSearchForm(long id, long accountId, int greaterThanLevel, int lessThanLevel,  
			BooleanOptions hardcore, BooleanOptions ironborn,
			BoostItemType boostItemType,
			long stashSlotId, long dungeonId, long messageId) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.greaterThanLevel = greaterThanLevel;
		this.lessThanLevel = lessThanLevel;
		this.boostItemType = boostItemType;
		this.stashSlotId = stashSlotId;
		this.dungeonId = dungeonId;
		this.messageId = messageId;
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

	public BoostItemType getBoostItemType() {
		return boostItemType;
	}

	public void setBoostItemType(BoostItemType boostItemType) {
		this.boostItemType = boostItemType;
	}

	public long getStashSlotId() {
		return stashSlotId;
	}

	public void setStashSlotId(long stashSlotId) {
		this.stashSlotId = stashSlotId;
	}

	public long getDungeonId() {
		return dungeonId;
	}

	public void setDungeonId(long dungeonId) {
		this.dungeonId = dungeonId;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	@Override
	public String toString() {
		return "BoostItemForm [" +
				"id=" + id + ", " +
				"accountId=" + accountId + ", " +
				"graeterThanLevel=" + greaterThanLevel + ", " +
				"lessThanLevel=" + lessThanLevel + ", " +
				"boostItemType=" + boostItemType + ", " +
				"stashSlotId=" + stashSlotId + ", " +
				"dungeonId=" + dungeonId + ", " +
				"messageId=" + messageId +
				"]";
	}
}
