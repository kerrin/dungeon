package org.kerrin.dungeon.exception;

public class CharacterNotFound extends Exception {
	private static final long serialVersionUID = 1330999454222815955L;

	public CharacterNotFound() {
		super();
	}
	
	/**
	 * Couldn't find the character id 
	 * @param characterId
	 */
	public CharacterNotFound(Long characterId) {
		super("Character id "+characterId);
	}
	
	public CharacterNotFound(String message) {
		super(message);
	}
}
