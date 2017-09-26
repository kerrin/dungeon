package org.kerrin.dungeon.model.api;

import org.kerrin.dungeon.service.ReloadPanels;

public abstract class ApiResponse {
	public boolean reloadDungeon = false;
	
	public boolean reloadCharacters = false;
	
	public boolean reloadCharacterDetails = false;
	
	public long reloadStashSlotId = -1;
	
	public boolean reloadSummary = false;

	/**
	 * Constructor
	 * 
	 * @param reloadDungeon
	 * @param reloadCharacters
	 * @param reloadCharacterDetails
	 * @param reloadStash
	 */
	public ApiResponse(boolean reloadDungeon, boolean reloadCharacters, boolean reloadCharacterDetails,
			long reloadStashSlotId, boolean reloadSummary) {
		super();
		this.reloadDungeon = reloadDungeon;
		this.reloadCharacters = reloadCharacters;
		this.reloadCharacterDetails = reloadCharacterDetails;
		this.reloadStashSlotId = reloadStashSlotId;
		this.reloadSummary = reloadSummary;
	}

	public ApiResponse(ReloadPanels reloadPanels) {
		super();
		this.reloadDungeon = reloadPanels.reloadDungeon;
		this.reloadCharacters = reloadPanels.reloadCharacters;
		this.reloadCharacterDetails = reloadPanels.reloadCharacterDetails;
		this.reloadStashSlotId = reloadPanels.reloadStashSlotId;
		this.reloadSummary = reloadPanels.reloadSummary;
	}	
}
