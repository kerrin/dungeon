package org.kerrin.dungeon.exception;

public class DungeonNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -338322867027814928L;

	public DungeonNotFound() {
		super();
	}
	
	/**
	 * Couldn't find the dungeon id 
	 * @param characterId
	 */
	public DungeonNotFound(Long dungeonId) {
		super("Dungeon id "+dungeonId);
	}
}
