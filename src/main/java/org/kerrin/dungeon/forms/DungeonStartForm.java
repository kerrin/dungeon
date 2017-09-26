package org.kerrin.dungeon.forms;

import java.util.List;

import javax.validation.constraints.Min;

import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;

/**
 * Form data for a character
 * 
 * @author Kerrin
 *
 */
public class DungeonStartForm {
	/** The dungeon identifier */
	@Min(-1)
	private long id = -1;
	
	private long[] characterIds = null;
	
	private long panelCharacterId;
	
	/** Is this record for hardcore characters */
	private boolean hardcore = false;
	
	/** Is this record for ironborn characters */
	private boolean ironborn = false;
	
	/** The difficulty modifier */
	private int difficultyModifier = 0;

	public DungeonStartForm() {};
	
	public DungeonStartForm(Dungeon dungeon) {
		super();
		this.id = dungeon.getId();
		List<Character> characters = dungeon.getCharacters();
		long[] characterIds = new long[dungeon.getCharacters().size()];
		for(int i=0; i < characters.size(); i++) {
			characterIds[i] = characters.get(i).getId();
		}
		this.characterIds = characterIds;
	}
	
	public DungeonStartForm(long id, long[] characterIds) {
		super();
		this.id = id;
		this.characterIds = characterIds;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCharacterIds(long[] characterIds) {
		this.characterIds = characterIds;
	}
	
	public long[] getCharacterIds() {
		return characterIds;
	}

	public long getPanelCharacterId() {
		return panelCharacterId;
	}

	public void setPanelCharacterId(long panelCharacterId) {
		this.panelCharacterId = panelCharacterId;
	}

	public boolean isHardcore() {
		return hardcore;
	}

	public void setHardcore(boolean hardcore) {
		this.hardcore = hardcore;
	}

	public boolean isIronborn() {
		return ironborn;
	}

	public void setIronborn(boolean ironborn) {
		this.ironborn = ironborn;
	}

	public int getDifficultyModifier() {
		return difficultyModifier;
	}

	public void setDifficultyModifier(int difficultyModifier) {
		this.difficultyModifier = difficultyModifier;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("DungeonStartForm [");
		sb.append("id=");
		sb.append(id);
		sb.append(", characters=");
		if(characterIds == null) {
			sb.append("null");
		} else {
			for(Long characterId:characterIds) {
				sb.append(characterId);
				sb.append(",");
			}
		}
		sb.append("difficultyModifier=");
		sb.append(difficultyModifier);
		sb.append("]");
		
		return sb.toString();
	}
}
