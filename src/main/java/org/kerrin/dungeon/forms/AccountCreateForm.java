package org.kerrin.dungeon.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.kerrin.dungeon.model.Account;

/**
 * Form data for a Account
 * 
 * @author Kerrin
 *
 */
public class AccountCreateForm {
	/** The username */
	@NotNull
	@Email
	@Size(min=5, max=64)
	private String username = "";
	
	/** The un-encrypted password, if changed */
	@NotNull
	@Size(min=6, max=30)
	private String password;
		
	/** The name to display to users */
	@NotNull
	@Size(min=2, max=30)
	private String displayName = "";
	
	public AccountCreateForm() {}

	public AccountCreateForm(Account account) {
		this(account.getUsername(), 
				null, // Password not used for output 
				account.getDisplayName()
				);
	}
	
	public AccountCreateForm(String username, String password, String displayName) {
		super();
		this.username = username;
		this.password = password;
		this.displayName = displayName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Null if account from database
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return "AccountCreateForm ["
					+ ", username=" + username 
					+ ", password=NOT SHOWN" 
					+ ", displayName=" + displayName 
				+ "]";
	};
}
