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
public class ResetPasswordForm {
	/** The key */
	@NotNull
	private String key;
	
	/** The password */
	@NotNull
	@Size(min=2, max=30)
	private String password1;
	
	/** The password again */
	@NotNull
	@Size(min=2, max=30)
	private String password2;

	public ResetPasswordForm() {}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	@Override
	public String toString() {
		return "LoginForm [key=" + key + ", password=NOT SHOWN]";
	};
}
