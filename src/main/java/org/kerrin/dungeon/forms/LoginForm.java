package org.kerrin.dungeon.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;


/**
 * Form data for the login form
 * 
 * @author Kerrin
 *
 */
public class LoginForm {
	/** The username */
	@NotNull
	@Size(min=2, max=64)
	@Email
	private String username;

	/** The password */
	@NotNull
	@Size(min=2, max=30)
	private String password;

	public LoginForm() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginForm [username=" + username + ", password=NOT SHOWN]";
	};
}
