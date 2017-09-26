package org.kerrin.dungeon.forms;

import javax.validation.constraints.Min;

import org.kerrin.dungeon.model.Dungeon;

/**
 * Form data for a Equipment
 * 
 * @author Kerrin
 *
 */
public class TestDungeonForm extends DungeonForm {
	@Min(1)
	private int partyLevel;
	
	private boolean randomMonsters;
	
	public TestDungeonForm() {
		super();
	}

	public TestDungeonForm(Dungeon dungeon) {
		super(dungeon);
	}

	public int getPartyLevel() {
		return partyLevel;
	}

	public void setPartyLevel(int partyLevel) {
		this.partyLevel = partyLevel;
	}

	public boolean isRandomMonsters() {
		return randomMonsters;
	}

	public void setRandomMonsters(boolean randomMonsters) {
		this.randomMonsters = randomMonsters;
	}
}
