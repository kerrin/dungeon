package org.kerrin.dungeon.exception;

public class CantEquipToMessage extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -338322867027814928L;

	public CantEquipToMessage() {
		super();
	}
	
	/**
	 * Couldn't find the dungeon id 
	 * @param characterId
	 */
	public CantEquipToMessage(Long messageId) {
		super("Message id "+messageId);
	}
}
