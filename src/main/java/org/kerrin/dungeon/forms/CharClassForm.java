package org.kerrin.dungeon.forms;

import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.Monster;

/**
 * Empty form
 * 
 * @author Kerrin
 *
 */
public class CharClassForm {
	private CharClass charClass = CharClass.MELEE;
	
	public CharClassForm() {}
	
	public CharClass getCharClass() {
		return charClass;
	}
	
	public void setCharClass(CharClass charClass) {
		this.charClass = charClass;
	}
	
	public Monster getMonster() {
		return Monster.fromCharClass(charClass);
	}
}
