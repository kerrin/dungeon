package org.kerrin.dungeon.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.kerrin.dungeon.model.StashSlotItem.TYPE;

/**
 * An item that can be put in a stash slot (Equipment or Boost Item)
 * Note, they are also valid in dungeons
 * @author Kerrin
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class StashSlotItemSuper extends StashSlotItem {
	protected StashSlotItemSuper() {}
	
	public StashSlotItemSuper(long id, TYPE type) {
		super(id, type);
	}
	public abstract boolean isEquipment();
	
	public abstract boolean isHardcore();

	public abstract void setHardcore(boolean hardcore);

	public abstract boolean isIronborn();

	public abstract void setIronborn(boolean ironborn);
}
