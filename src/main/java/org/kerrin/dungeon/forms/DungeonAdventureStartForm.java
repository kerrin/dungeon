package org.kerrin.dungeon.forms;

import java.util.List;

import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;

/**
 * Form data for a character
 * 
 * @author Kerrin
 *
 */
public class DungeonAdventureStartForm {
	/** The character identifier */
	private long[] characterIds = null;
	
	private long panelCharacterId;
	
	/** Is this record for hardcore characters */
	private boolean hardcore = false;
	
	/** Is this record for ironborn characters */
	private boolean ironborn = false;

	public DungeonAdventureStartForm() {};
	
	public DungeonAdventureStartForm(Dungeon dungeon) {
		super();
		List<Character> characters = dungeon.getCharacters();
		long[] characterIds = new long[dungeon.getCharacters().size()];
		for(int i=0; i < characters.size(); i++) {
			characterIds[i] = characters.get(i).getId();
		}
		this.characterIds = characterIds;
	}
	
	public DungeonAdventureStartForm(long[] characterIds) {
		super();
		this.characterIds = characterIds;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("DungeonStartForm [");
		sb.append("characters=");
		if(characterIds == null) {
			sb.append("null");
		} else {
			for(Long characterId:characterIds) {
				sb.append(characterId);
				sb.append(",");
			}
		}
		sb.append("]");
		
		return sb.toString();
	}

	/**
	 * Returns a ViewTypeForm from this DungeonAdventureStartForm
	 * @return
	 */
	public ViewTypeForm asViewTypeForm() {
		ViewTypeForm viewTypeForm = new ViewTypeForm(hardcore, ironborn, false);
		viewTypeForm.setCharId(panelCharacterId);
		viewTypeForm.setDungeonId(0);
		return viewTypeForm;
	}
}
