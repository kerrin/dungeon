package org.kerrin.dungeon.forms;

import javax.persistence.Transient;
import javax.validation.constraints.Min;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Form data for a BoostItem
 * 
 * @author Kerrin
 *
 */
public class BoostItemForm {
	@Transient
	@JsonIgnore
	private static final Logger logger = LoggerFactory.getLogger(BoostItemForm.class);
	
	/** The BoostItem identifier */
	@Min(-1)
	private long id = -1;
	
	/** The account identifier */
	private long accountId = -1;

	@Min(1)
	private int level;
	
	/** Is this record for hardcore characters */
	private boolean hardcore = false;
	
	/** Is this record for ironborn characters */
	private boolean ironborn = false;
		
	private BoostItemType boostItemType;

	/** Stash slot it is in, if it is in the stash */
	private int stashSlotId = -1;
	/** Dungeon it is in, if it is in a dungeon */
	private long dungeonId = -1;
	/** Message it is attached to, if it is in a message */
	private long messageId = -1;
	
	private boolean sendToAccount = false;
	
	public BoostItemForm() {
		super();
	}

	public BoostItemForm(BoostItem boostItem) {
		super();
		
		this.id = boostItem.getId();
		this.accountId = boostItem.getAccount().getId();
		this.level = boostItem.getLevel();
		this.hardcore = boostItem.isHardcore();
		this.ironborn = boostItem.isIronborn();
		this.boostItemType = boostItem.getBoostItemType();
		this.stashSlotId = boostItem.getStashSlotId();
		this.dungeonId = boostItem.getDungeonId();
		this.messageId = boostItem.getMessageId();
	}
	
	public BoostItemForm(long id, long accountId, BoostItemType boostItemType, int level, 
			boolean hardcore, boolean ironborn, int stashSlotId, long dungeonId, long messageId) {
		this();
		
		this.id = id;
		this.accountId = accountId;
		this.boostItemType = boostItemType;
		this.level = level;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
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
	
	/**
	 * Get an account with JUST the account id set
	 * @return
	 */
	public Account getAccount() {
		return new Account(accountId,null,null,null,null,null,null, false, false, 1);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public BoostItemType getBoostItemType() {
		return boostItemType;
	}

	public void setBoostItemType(BoostItemType boostItemType) {
		this.boostItemType = boostItemType;
	}

	public int getStashSlotId() {
		return stashSlotId;
	}

	public void setStashSlotId(int stashSlotId) {
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

	public boolean isSendToAccount() {
		return sendToAccount;
	}

	public void setSendToAccount(boolean sendToAccount) {
		this.sendToAccount = sendToAccount;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("BoostItemForm [");
		sb.append("id=");
		sb.append(id);
		sb.append(", boostItemType=");
		sb.append(boostItemType.getNiceName());
		sb.append(", level=");
		sb.append(level);
		sb.append(", hardcore=");
		sb.append(hardcore?"Yes":"No");
		sb.append(", ironborn=");
		sb.append(ironborn?"Yes":"No");
		sb.append(", stashSlotId=");
		sb.append(stashSlotId);
		sb.append(", dungeonId=");
		sb.append(dungeonId);
		sb.append(", messageId=");
		sb.append(messageId);
		sb.append(", sendToAccount=");
		sb.append(sendToAccount?"true":"false");
		sb.append("]");
		
		return sb.toString();
	}
}
