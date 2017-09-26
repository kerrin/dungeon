package org.kerrin.dungeon.exception;

public class InventoryException extends Exception {
	private static final long serialVersionUID = -3240919283230871977L;

	public InventoryException() {
		super();
	}
	
	/**
	 * Couldn't find the dungeon id 
	 * @param characterId
	 */
	public InventoryException(String message) {
		super(message);
	}
}
