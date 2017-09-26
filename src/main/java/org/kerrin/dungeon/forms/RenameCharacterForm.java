package org.kerrin.dungeon.forms;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Form data for a character
 * 
 * @author Kerrin
 *
 */
public class RenameCharacterForm {
	/** The character identifier */
	@Min(-1)
	private long id = -1;
	
	/** The account identifier */
	@Min(-1)
	private long accountId = -1;
	
	/** Is this record for hardcore characters */
	private boolean hardcore = false;
	
	/** Is this record for ironborn characters */
	private boolean ironborn = false;
	
	/** The character name */
	@NotNull
	@Size(min=2, max=14)
	private String name;

	public RenameCharacterForm() {};
		
	public RenameCharacterForm(long id, long accountId, boolean hardcore, boolean ironborn, String name) {
		this.id = id;
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Character [id=" + id + 
				", accountId=" + accountId + 
				", hardcore=" + (hardcore?"Yes":"No") +
				", ironborn=" + (ironborn?"Yes":"No") +
				", name=" + name + 
				"]";
	}
}
