package org.kerrin.dungeon.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * An item that can be put in a stash slot (Equipment or Boost Item)
 * Note, they are also valid in dungeons
 * @author Kerrin
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class StashSlotItem {
	public enum TYPE {EQUIPMENT, BOOST_ITEM}
	/** The account identifier */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	protected long id;
	
	protected TYPE stashSlotType;
	
	protected StashSlotItem() {}
	
	public StashSlotItem(long id, TYPE stashSlotType) {
		this.id = id;
		this.stashSlotType = stashSlotType;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public TYPE getStashSlotType() {
		return stashSlotType;
	}

	public void setStashSlotType(TYPE stashSlotType) {
		this.stashSlotType = stashSlotType;
	}
}
