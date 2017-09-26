package org.kerrin.dungeon.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.kerrin.dungeon.model.Account;

/**
 * Form data for modifying an Account by the owner
 * 
 * @author Kerrin
 *
 */
public class AccountPublicForm extends ViewTypeForm {
	/** The username */
	@Size(min=5, max=64)
	private String username = "";
	
	/** The un-encrypted password, if changed */
	@NotNull
	@Size(min=5, max=64)
	private String currentPassword;
	
	@Size(min=5, max=64)
	private String newPassword1;
	
	@Size(min=5, max=64)
	private String newPassword2;
		
	/** The name to display to users */
	@Size(min=2, max=30)
	private String displayName = "";
	
	public AccountPublicForm() {}

	public AccountPublicForm(Account account) {
		this(account.getUsername(), 
				null/*Passwords not used for output*/, 
				null/*Passwords not used for output*/, 
				null/*Passwords not used for output*/, 
				account.getDisplayName() 
				);
	}
	
	public AccountPublicForm(String username, String currentPassword, String newPassword1, String newPassword2, String displayName) {
		super();
		this.username = username;
		this.currentPassword = currentPassword;
		this.newPassword1 = newPassword1;
		this.newPassword2 = newPassword2;
		this.displayName = displayName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword1() {
		return newPassword1;
	}

	public void setNewPassword1(String newPassword1) {
		this.newPassword1 = newPassword1;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return "AccountForm ["
					+ ", username=" + username 
					+ ", currentPassword="+(currentPassword != null && !currentPassword.isEmpty()?"Set":"Not Set")
					+ ", newPassword1="+(newPassword1 != null && !newPassword1.isEmpty()?"Set":"Not Set")
					+ ", newPassword2="+(newPassword2 != null && !newPassword2.isEmpty()?"Set":"Not Set")
					+ ", displayName=" + displayName 
				+ "]";
	};
}
