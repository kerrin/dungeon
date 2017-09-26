package org.kerrin.dungeon.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * A month of the account history, starting from start date
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="account_message")
public class AccountMessage {
	/** The identifier */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	/** The account identifier */
	private long accountId;
	/** The message to display */
	@Column(length=1024) 
	private String message;
	/** An optional link to another part of the site */
	private String linkUrl;
	/** What panel to load, or reload the whole page if null */
	private String linkPanel;
	/** Is there an attached item Id */
	private long attachedItemId;
	/** Is there an attached item Type */
	private StashSlotItemSuper.TYPE attachedItemType;
	/** Is this message for hardcore mode only */
	private Boolean hardcore;
	/** Is this message for ironborn mode only */
	private Boolean ironborn;
	
	@Transient
	private StashSlotItemSuper displayItem;

	protected AccountMessage() {}
	
	public AccountMessage(long accountId, String message, String linkUrl, String linkPanel, StashSlotItemSuper attachedItem) {
		super();
		this.accountId = accountId;
		this.message = message;
		this.linkUrl = linkUrl;
		this.linkPanel = linkPanel;
		if(attachedItem==null) {
			this.attachedItemId = -1;
			this.attachedItemType = null;
		} else {
			this.attachedItemId = attachedItem.getId();
			this.attachedItemType = attachedItem.getStashSlotType();
			hardcore = attachedItem.isHardcore();
			ironborn = attachedItem.isIronborn();
		}
	}
	
	public AccountMessage(long accountId, String message, String linkUrl, String linkPanel, Boolean hardcore, Boolean ironborn) {
		super();
		this.accountId = accountId;
		this.message = message;
		this.linkUrl = linkUrl;
		this.linkPanel = linkPanel;
		this.attachedItemId = -1;
		this.attachedItemType = null;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkPanel() {
		return linkPanel;
	}

	public void setLinkPanel(String linkPanel) {
		this.linkPanel = linkPanel;
	}

	public StashSlotItemSuper.TYPE getAttachedItemType() {
		return attachedItemType;
	}

	public void setAttachedItemType(StashSlotItemSuper.TYPE attachedItemType) {
		this.attachedItemType = attachedItemType;
	}

	public long getAttachedItemId() {
		return attachedItemId;
	}

	public void setAttachedItemId(long attachedItemId) {
		this.attachedItemId = attachedItemId;
	}
	
	public void setDisplayItem(StashSlotItemSuper displayItem) {
		this.displayItem = displayItem;
	}
	
	public StashSlotItemSuper getDisplayItem() {
		return displayItem;
	}
}
