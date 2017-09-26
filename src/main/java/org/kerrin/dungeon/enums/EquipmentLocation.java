package org.kerrin.dungeon.enums;

/**
 * Where the equipment is located
 * @author Kerrin
 *
 */
public enum EquipmentLocation {
	NONE(0),
	CHARACTER(1),
	INVENTORY(2),
	DUNGEON(3),
	MESSAGE(4);
	
	private int id;

	private EquipmentLocation(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
