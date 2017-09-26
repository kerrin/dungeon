package org.kerrin.dungeon.forms;

import org.kerrin.dungeon.enums.Spell;

/**
 * Empty form
 * 
 * @author Kerrin
 *
 */
public class SpellForm {
	private Spell spell = Spell.Firebolt;
	
	public SpellForm() {}
	
	public Spell getSpell() {
		return spell;
	}
	
	public void setSpell(Spell spell) {
		this.spell = spell;
	}
}
