package org.kerrin.dungeon.forms;

import org.kerrin.dungeon.enums.Monster;

/**
 * Empty form
 * 
 * @author Kerrin
 *
 */
public class MonsterForm {
	private Monster monster = Monster.BARBARIAN;
	private int level = 1;
	
	public MonsterForm() {}
	
	public Monster getMonster() {
		return monster;
	}
	
	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	};
}
