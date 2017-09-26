package org.kerrin.dungeon.forms;

/**
 * Form data for a Account search request
 * 
 * @author Kerrin
 *
 */
public class AccountSearchForm {
	/** The Account identifier */
	private long id = -1;
	
	/** The Account username */
	private String username;
	
	/** The name to display to users */
	private String displayName;

	public AccountSearchForm() {};	
	
	public AccountSearchForm(long id, String username, String displayName) {
		super();
		this.id = id;
		this.username = username;
		this.displayName = displayName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return "AccountSearchForm [" +
					"id=" + id + 
					", username=" + username + 
					", displayName=" + displayName + 
				"]";
	}
}
