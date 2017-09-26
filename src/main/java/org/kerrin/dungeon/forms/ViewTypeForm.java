package org.kerrin.dungeon.forms;

import org.kerrin.dungeon.model.AccountConfig;
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
public class ViewTypeForm {
	/** Is this record for hardcore characters */
	private boolean hardcore = false;
	
	/** Is this record for ironborn characters */
	private boolean ironborn = false;
	
	/** If we should breakout the frame */
	private boolean noBreakout = false;
	
	/** The selected character */
	private long charId = -1;
	
	/** The selected dungeon */
	private long dungeonId = 0;
	
	public ViewTypeForm() {};
	
	public ViewTypeForm(Character character) {
		this(character.isHardcore(), character.isIronborn(), false);
	}
	
	public ViewTypeForm(Equipment equipment) {
		this(equipment.isHardcore(), equipment.isIronborn(), false);
	}
	
	public ViewTypeForm(Dungeon dungeon) {
		this(dungeon.isHardcore(), dungeon.isIronborn(), false);
	}
	
	public ViewTypeForm(AccountCurrency accountCurrency) {
		this(accountCurrency.isHardcore(), accountCurrency.isIronborn(), false);
	}
	
	public ViewTypeForm(AccountConfig accountConfig) {
		this(accountConfig.isHardcore(), accountConfig.isIronborn(), false);
	}
	
	public ViewTypeForm(Inventory inventory) {
		this(inventory.isHardcore(), inventory.isIronborn(), false);
	}
	
	public ViewTypeForm(boolean hardcore, boolean ironborn, boolean noBreakout) {
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.noBreakout = noBreakout;
	}

	public boolean isHardcore() {
		return hardcore;
	}

	public void setHardcore(boolean hardcore) {
		this.hardcore = hardcore;
	}

	public boolean isIronborn() {
		return ironborn;
	}

	public void setIronborn(boolean ironborn) {
		this.ironborn = ironborn;
	}
	
	public boolean isNoBreakout() {
		return noBreakout;
	}

	public void setNoBreakout(boolean noBreakout) {
		this.noBreakout = noBreakout;
	}
	
	public long getCharId() {
		return charId;
	}

	public void setCharId(long charId) {
		this.charId = charId;
	}

	public long getDungeonId() {
		return dungeonId;
	}

	public void setDungeonId(long dungeonId) {
		this.dungeonId = dungeonId;
	}

	@Override
	public String toString() {
		return "View [" +
				"hardcore=" + (hardcore?"Yes":"No") +
				", ironborn=" + (ironborn?"Yes":"No") +
				", noBreakout=" + (noBreakout?"Yes":"No") +
				", charId=" + charId +
				", dungeonId=" + dungeonId +
				"]";
	}
}
