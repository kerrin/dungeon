package org.kerrin.dungeon.exception;

public class CantEquipToDungeon extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -338322867027814928L;

	public CantEquipToDungeon() {
		super();
	}
	
	/**
	 * Couldn't find the dungeon id 
	 * @param characterId
	 */
	public CantEquipToDungeon(Long dungeonId) {
		super("Dungeon id "+dungeonId);
	}
}
