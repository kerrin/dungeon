package org.kerrin.dungeon.exception;

public class AccountIdMismatch extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1859129146196436037L;

	public AccountIdMismatch() {
		super();
	}
	
	/**
	 * Couldn't find the dungeon id 
	 * @param characterId
	 */
	public AccountIdMismatch(long accountId1, long accountId2) {
		super("Account id "+accountId1+" and "+accountId2 + " should match, but don't");
	}
}
