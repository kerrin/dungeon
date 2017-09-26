package org.kerrin.dungeon.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Form data for an Account (Admin)
 * 
 * @author Kerrin
 *
 */
public class MessageForm {
	/** The name to display to users */
	@NotNull
	@Size(min=1, max=1024)
	private String message = "";
	
	public MessageForm() {}
	
	public MessageForm(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
