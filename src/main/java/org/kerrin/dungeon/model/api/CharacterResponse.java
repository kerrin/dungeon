package org.kerrin.dungeon.model.api;

import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.service.ReloadPanels;

public class CharacterResponse extends ApiResponse {
	public Character character;
	
	public CharacterResponse(Character character) {
		this(character, false, false, false, -1, false);
	}
	
	public CharacterResponse(Character character, boolean reloadDungeon, boolean reloadCharacters, 
			boolean reloadCharacterDetails, long reloadStashSlotId, boolean reloadSummary) {
		super(reloadDungeon, reloadCharacters, reloadCharacterDetails, reloadStashSlotId, reloadSummary);
		this.character = character;
	}

	public CharacterResponse(Character character, ReloadPanels reloadPanels) {
		super(reloadPanels);
		this.character = character;
	}
}
