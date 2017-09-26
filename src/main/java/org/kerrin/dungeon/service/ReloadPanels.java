package org.kerrin.dungeon.service;

import org.kerrin.dungeon.enums.EquipmentLocation;

public class ReloadPanels {
	public boolean reloadDungeon = false;
	
	public boolean reloadCharacters = false;
	
	public boolean reloadCharacterDetails = false;
	
	public long reloadStashSlotId = -1;

	public boolean reloadSummary = false;
	
	public boolean reloadMessages = false;
	
	public ReloadPanels() {
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param reloadDungeon
	 * @param reloadCharacters
	 * @param reloadCharacterDetails
	 * @param reloadStash
	 */
	public ReloadPanels(boolean reloadDungeon, boolean reloadCharacters, boolean reloadCharacterDetails,
			long reloadStashSlotId, boolean reloadSummary, boolean reloadMessages) {
		super();
		this.reloadDungeon = reloadDungeon;
		this.reloadCharacters = reloadCharacters;
		this.reloadCharacterDetails = reloadCharacterDetails;
		this.reloadStashSlotId = reloadStashSlotId;
		this.reloadSummary = reloadSummary;
		this.reloadMessages = reloadMessages;
	}
	
	public void setPanelReload(EquipmentLocation location, long locationId) {
		switch (location) {
		case CHARACTER:
			reloadCharacterDetails = true;
			break;
		case DUNGEON:
			reloadDungeon = true;
			break;
		case INVENTORY:
			reloadStashSlotId = locationId;
			break;
		case MESSAGE:
			reloadMessages = true;
			break;
		case NONE:
			break;
		default:
			break;
		}
	}
}
