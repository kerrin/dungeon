package org.kerrin.dungeon.model;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DailyItem {
	@Transient
	@JsonIgnore
	protected StashSlotItemSuper item;
	@Transient
	@JsonIgnore
	protected String itemMessage;
	@Transient
	@JsonIgnore
	protected String itemCharacterUrl;
	
	
	public DailyItem(StashSlotItemSuper item, String itemMessage, String itemCharacterUrl) {
		super();
		this.item = item;
		this.itemMessage = itemMessage;
		this.itemCharacterUrl = itemCharacterUrl;
	}
	
	public StashSlotItemSuper getItem() {
		return item;
	}
	public void setItem(StashSlotItemSuper item) {
		this.item = item;
	}
	
	public String getItemMessage() {
		return itemMessage;
	}
	public void setItemMessage(String itemMessage) {
		this.itemMessage = itemMessage;
	}
	
	public String getItemCharacterUrl() {
		return itemCharacterUrl;
	}
	public void setItemCharacterUrl(String itemCharacterUrl) {
		this.itemCharacterUrl = itemCharacterUrl;
	}
	
	
}
