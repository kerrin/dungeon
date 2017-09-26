package org.kerrin.dungeon.enums;

/**
 * All the possible character classes
 * 
 * @author Kerrin
 *
 */
public enum AttributeType {
	UNKNOWN(0, 	"Unknown"),
	STAT(	1, 	"Base Stat"), 
	DAMAGE(	2, 	"Damage"), 
	BUFF(	3, 	"Buff"), 
	SPEED(	4, 	"Speed"), 
	RESIST(	5, 	"Resist"),
	SPECIAL(6,	"Special");
	
	private int id;
	private String niceName;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	AttributeType (int id, String name) {
		this.id = id;
		this.niceName = name;;
	}
	
	/**
	 * Get the identifier
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the nice name
	 * 
	 * @return
	 */
	public String getNiceName() {
		return niceName;
	}

	/**
	 * Get a character class from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static AttributeType fromId(int id) {
		for(AttributeType thisCharClass:values()) {
			if(thisCharClass.id == id) return thisCharClass;
		}
		return UNKNOWN;
	}
}
