package org.kerrin.dungeon.forms;

import org.kerrin.dungeon.enums.EquipmentAttribute;

/**
 * Empty form
 * 
 * @author Kerrin
 *
 */
public class AttributeForm {
	private EquipmentAttribute attribute = EquipmentAttribute.INTELLIGENCE;
	
	public AttributeForm() {}
	
	public EquipmentAttribute getAttribute() {
		return attribute;
	}
	
	public void setAttribute(EquipmentAttribute attribute) {
		this.attribute = attribute;
	}
}
