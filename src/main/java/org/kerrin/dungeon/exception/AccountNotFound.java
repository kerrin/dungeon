package org.kerrin.dungeon.exception;

public class AccountNotFound extends Exception {
	private static final long serialVersionUID = -3240919283230871977L;

	public AccountNotFound() {
		super();
	}
	
	/**
	 * Couldn't find the dungeon id 
	 * @param characterId
	 */
	public AccountNotFound(Long accountId) {
		super("Account id "+accountId);
	}
}
