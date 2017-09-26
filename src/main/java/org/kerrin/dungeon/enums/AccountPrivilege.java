package org.kerrin.dungeon.enums;

import javax.persistence.Id;

/**
 * If the account is an administrator, they may have access to admin tools
 * 
 * @author Kerrin
 *
 */
public enum AccountPrivilege {
	NONE(	0, 	"Unknown!"),
	USER(	1, 	"ROLE_User"),
	VIEW(	2, 	"ROLE_View"), 
	MODIFY(	3, 	"ROLE_Modify"), 
	DELETE(	4, 	"ROLE_Delete");
	
	@Id
	private int id;
	private String name;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	AccountPrivilege (int id, String name) {
		this.id = id;
		this.name = name;
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
	public String getName() {
		return name;
	}

	/**
	 * Get a character class from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static AccountPrivilege fromId(int id) {
		for(AccountPrivilege thisPriv:values()) {
			if(thisPriv.id == id) return thisPriv;
		}
		return NONE;
	}
}
