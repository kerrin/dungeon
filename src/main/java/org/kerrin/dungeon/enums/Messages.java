package org.kerrin.dungeon.enums;

/**
 * All the possible character classes
 * 
 * @author Kerrin
 *
 */
public enum Messages {
	NONE(		0, 	"Unexpected Message"),
	ADMIN_ITEM(	1, 	"Dear <DISPLAYNAME>,<br />\n" + 
					"You have been sent an item by an administrator.<br />\n" + 
					"Closing this message will delete the item.<br />\n" + 
					"Drag the item to an empty character or stash slot to keep.");
	
	private int id;
	private String message;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	Messages (int id, String message) {
		this.id = id;
		this.message = message;;
	}
	
	/**
	 * Get the identifier
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the nice name
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Get a character class from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static Messages fromId(int id) {
		for(Messages thisMessage:values()) {
			if(thisMessage.id == id) return thisMessage;
		}
		return NONE;
	}
}
