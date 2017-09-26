package org.kerrin.dungeon.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.forms.BoostItemForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Database record for a users account
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="boost_item")
public class BoostItem extends StashSlotItemSuper implements Comparable<BoostItem> {
	@Transient
	@JsonIgnore
	private static final Logger logger = LoggerFactory.getLogger(BoostItem.class);

	/** 
	 * The account this boost item belongs to 
	 *	A foreign key to account.id 
	 */
	@ManyToOne
	@JoinColumn(name="account")
	private Account account;
	
	/** Is this record for a hardcore character */
	private boolean hardcore;
	
	/** Is this record for an ironborn character */
	private boolean ironborn;

	/**
	 * What item boost type is this
	 */
	private BoostItemType boostItemType;
	
	/**
	 * The boost item level (if appropriate for type)
	 */
	private int level;
	
	/**
	 * If in stash, which slot
	 */
	private int stashSlotId;
	
	/**
	 * If in dungeon, which one
	 */
	private long dungeonId;
	
	/**
	 * If in a message, which one
	 */
	private long messageId;
	
	protected BoostItem() {}

	public BoostItem(long id, 
			Account account, boolean hardcore, boolean ironborn,
			BoostItemType boostItemType, 
			int stashSlotId, long dungeonId, long messageId, 
			int level) {
		super(id, TYPE.BOOST_ITEM);
		logger.trace("Boost Item");
		this.account = account;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.boostItemType = boostItemType;
		this.stashSlotId = stashSlotId;
		this.dungeonId = dungeonId;
		this.messageId = messageId;
		this.level = level;
	}
	
	/**
	 * Copy constructor
	 * @param equipment
	 */
	public BoostItem(BoostItem boostItem) {
		super(boostItem.getId(), TYPE.BOOST_ITEM);
		this.account = boostItem.getAccount();
		this.level = boostItem.getLevel();
		this.boostItemType = boostItem.getBoostItemType();
		this.hardcore = boostItem.isHardcore();
		this.ironborn = boostItem.isIronborn();
		this.stashSlotId = boostItem.getStashSlotId();
		this.dungeonId = boostItem.getDungeonId();
		this.messageId = boostItem.getMessageId();
	}
	
	public BoostItem(BoostItemForm boostItemForm) {
		super(boostItemForm.getId(), TYPE.BOOST_ITEM);
		this.account = boostItemForm.getAccount();
		this.level = boostItemForm.getLevel();
		this.boostItemType = boostItemForm.getBoostItemType();
		this.hardcore = boostItemForm.isHardcore();
		this.ironborn = boostItemForm.isIronborn();
		this.stashSlotId = boostItemForm.getStashSlotId();
		this.dungeonId = boostItemForm.getDungeonId();
		this.messageId = boostItemForm.getMessageId();
	}

	@Override
	public boolean isEquipment() {
		return false;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	public int getLevel() {
		return level;
	}
	
	public int getSalvageValue() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	/**
	 * 
	 * @param account
	 * @param level
	 * @param hardcore
	 * @param ironborn
	 * @param stashSlotId 	The stash slot the item is in. NOTE: 0 is a valid stash slot
	 * @param dungeonId		The dungeon the item is in
	 * @param messageId		The message the item is attached to
	 * @return
	 */
	public static BoostItem createRandom(Account account, int level, boolean hardcore, boolean ironborn,
			int stashSlotId, long dungeonId, long messageId) {
		BoostItemType type = BoostItemType.getRandomType(level, hardcore);
		if(type != BoostItemType.UNKNOWN) {
			return new BoostItem(-1, account, hardcore, ironborn, 
				type, 
				stashSlotId, dungeonId, messageId, level);
		} else {
			return null;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoostItem other = (BoostItem) obj;
		if (id != other.id)
			return false;
		if(id == -1) {
			// New equipment, so check the other fields instead
			if(!boostItemType.equals(other.boostItemType) ||
					hardcore != other.hardcore ||
					ironborn != other.ironborn ||
					level != other.level) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int compareTo(BoostItem compareTo) {
		// Level 1 to 60
		// Equipment Type cost (20 to 2000 / 20)
	    return ((level - compareTo.level)*100) +
	    		((boostItemType.getCost()-compareTo.boostItemType.getCost()) / 20);
	}

	@Override
	public String toString() {
		return "BoostItem [account=" + account + 
				", hardcore=" + hardcore + 
				", ironborn=" + ironborn +
				", boostItemType=" + boostItemType + 
				", level=" + level + 
				", stashSlotId=" + stashSlotId +
				", dungeonId=" + dungeonId + 
				", messageId=" + messageId + "]";
	}
}