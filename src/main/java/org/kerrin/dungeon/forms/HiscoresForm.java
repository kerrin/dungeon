package org.kerrin.dungeon.forms;

import java.util.Calendar;
import java.util.Date;

import org.kerrin.dungeon.enums.HiscoreType;
import org.kerrin.dungeon.model.AccountCurrency;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Inventory;

/**
 * Form data for a character
 * 
 * @author Kerrin
 *
 */
public class HiscoresForm {
	
	
	/** Is this record for hardcore characters */
	private boolean viewHardcore = false;
	
	/** Is this record for ironborn characters */
	private boolean viewIronborn = false;

	/** Hiscore */
	private HiscoreType type = HiscoreType.HIGHEST_LEVEL;

	/** The date to get scores since */
	private Date sinceDate;

	/** How many to display */
	private int pageSize = 50;
	
	/** Which rank to show */
	private int offset = 1;
	
	public HiscoresForm() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -28);
		sinceDate = cal.getTime();
	};
	
	public HiscoresForm(Character character) {
		this(character.isHardcore(), character.isIronborn());
	}
	
	public HiscoresForm(Equipment equipment) {
		this(equipment.isHardcore(), equipment.isIronborn());
	}
	
	public HiscoresForm(Dungeon dungeon) {
		this(dungeon.isHardcore(), dungeon.isIronborn());
	}
	
	public HiscoresForm(AccountCurrency accountCurrency) {
		this(accountCurrency.isHardcore(), accountCurrency.isIronborn());
	}
	
	public HiscoresForm(Inventory inventory) {
		this(inventory.isHardcore(), inventory.isIronborn());
	}
	
	public HiscoresForm(boolean hardcore, boolean ironborn) {
		this.viewHardcore = hardcore;
		this.viewIronborn = ironborn;
	}

	public boolean isViewHardcore() {
		return viewHardcore;
	}

	public void setViewHardcore(boolean hardcore) {
		this.viewHardcore = hardcore;
	}

	public boolean isViewIronborn() {
		return viewIronborn;
	}

	public void setViewIronborn(boolean ironborn) {
		this.viewIronborn = ironborn;
	}

	public void setType(HiscoreType type) {
		this.type = type;
	}

	public HiscoreType getType() {
		return type;
	}

	public void setSinceDate(Date sinceDate) {
		this.sinceDate = sinceDate;
	}

	public Date getSinceDate() {
		return sinceDate;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "HiscoreForm [viewHardcore=" + viewHardcore + 
					", viewIronborn=" + viewIronborn + 
					", type=" + type + 
					", sinceDate=" + sinceDate + 
					", pageSize=" + pageSize + 
					", offset=" + offset + 
				"]";
	}
	
	
}
