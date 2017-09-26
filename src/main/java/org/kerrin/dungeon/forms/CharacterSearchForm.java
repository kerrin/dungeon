package org.kerrin.dungeon.forms;

import org.kerrin.dungeon.enums.BooleanOptions;
import org.kerrin.dungeon.enums.CharClass;

/**
 * Form data for a character search request
 * 
 * @author Kerrin
 *
 */
public class CharacterSearchForm {
	/** The character identifier */
	private long id = -1;
	
	/** The account identifier */
	private long accountId = -1;
	
	private BooleanOptions hardcore;
	
	private BooleanOptions ironborn;
	
	/** The character name */
	private String name;
	
	/** The class of the character */
	private CharClass charClass = CharClass.ANY;
	
	/** The level of the character */
	private int greaterThanLevel = -1;
	/** The level of the character */
	private int lessThanLevel = -1;
	
	/** How much experience the character has earnt in this level */
	private long greaterThanXp = -1;
	/** How much experience the character has earnt in this level */
	private long lessThanXp = -1;
	
	/** The prestige level of the character */
	private int greaterThanPrestigeLevel = -1;
	/** The Prestige level of the character */
	private int lessThanPrestigeLevel = -1;
	
	private long dungeonId = -1;
	
	private boolean currentlyDead = false;

	public CharacterSearchForm() {};
	
	public CharacterSearchForm(long id, 
			long accountId, 
			BooleanOptions hardcore,
			BooleanOptions ironborn,
			String name, 
			CharClass charClass, 
			int greaterThanLevel, int lessThanLevel, 
			long greaterThanXp, long lessThanXp, 
			int greaterThanPrestigeLevel, int lessThanPrestigeLevel, 
			long dungeonId,
			boolean currentlyDead) {
		this.id = id;
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.name = name;
		this.charClass = charClass;
		this.greaterThanLevel = greaterThanLevel;
		this.lessThanLevel = lessThanLevel;
		this.greaterThanXp = greaterThanXp;
		this.lessThanXp = lessThanXp;
		this.greaterThanPrestigeLevel = greaterThanPrestigeLevel;
		this.lessThanPrestigeLevel = lessThanPrestigeLevel;
		this.dungeonId = dungeonId;
		this.currentlyDead = currentlyDead;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public BooleanOptions getHardcore() {
		return hardcore==null?BooleanOptions.BOTH:hardcore;
	}

	public void setHardcore(BooleanOptions hardcore) {
		this.hardcore = hardcore;
	}

	public BooleanOptions getIronborn() {
		return ironborn==null?BooleanOptions.BOTH:ironborn;
	}

	public void setIronborn(BooleanOptions ironborn) {
		this.ironborn = ironborn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CharClass getCharClass() {
		return charClass;
	}

	public void setCharClass(CharClass charClass) {
		this.charClass = charClass;
	}
	
	public int getGreaterThanLevel() {
		return greaterThanLevel;
	}

	public void setGreaterThanLevel(int greaterThanLevel) {
		this.greaterThanLevel = greaterThanLevel;
	}

	public int getLessThanLevel() {
		return lessThanLevel;
	}

	public void setLessThanLevel(int lessThanLevel) {
		this.lessThanLevel = lessThanLevel;
	}

	public long getGreaterThanXp() {
		return greaterThanXp;
	}

	public void setGreaterThanXp(long greaterThanXp) {
		this.greaterThanXp = greaterThanXp;
	}

	public long getLessThanXp() {
		return lessThanXp;
	}

	public void setLessThanXp(long lessThanXp) {
		this.lessThanXp = lessThanXp;
	}
	
	public int getGreaterThanPrestigeLevel() {
		return greaterThanPrestigeLevel;
	}

	public void setGreaterThanPrestigeLevel(int greaterThanPrestigeLevel) {
		this.greaterThanPrestigeLevel = greaterThanPrestigeLevel;
	}

	public int getLessThanPrestigeLevel() {
		return lessThanPrestigeLevel;
	}

	public void setLessThanPrestigeLevel(int lessThanPrestigeLevel) {
		this.lessThanPrestigeLevel = lessThanPrestigeLevel;
	}

	public long getDungeonId() {
		return dungeonId;
	}

	public void setDungeonId(long dungeonId) {
		this.dungeonId = dungeonId;
	}

	public boolean isCurrentlyDead() {
		return currentlyDead;
	}

	public void setCurrentlyDead(boolean currentlyDead) {
		this.currentlyDead = currentlyDead;
	}

	@Override
	public String toString() {
		return "Character [id=" + id + 
				", accountId=" + accountId + 
				", hardcore=" + (hardcore.getBooleanValue()==null?"null":hardcore.getBooleanValue()?"Yes":"No") +
				", ironborn=" + (ironborn.getBooleanValue()==null?"null":ironborn.getBooleanValue()?"Yes":"No") +
				", name=" + name + 
				", charClass=" + charClass.getNiceName() + 
				", greaterThanLevel=" + greaterThanLevel +
				", lessThanLevel=" + lessThanLevel +
				", greaterThanXp=" + greaterThanXp +
				", lessThanXp=" + lessThanXp +
				", currentlyDead=" + (currentlyDead?"yes":"no") + 				
				"]";
	}
}
