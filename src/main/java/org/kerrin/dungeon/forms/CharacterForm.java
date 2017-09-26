package org.kerrin.dungeon.forms;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.model.Character;

/**
 * Form data for a character
 * 
 * @author Kerrin
 *
 */
public class CharacterForm {
	/** The character identifier */
	@Min(-1)
	private long id = -1;
	
	/** The account identifier */
	@Min(-1)
	private long accountId = -1;
	
	/** Is this record for hardcore characters */
	private boolean hardcore = false;
	
	/** Is this record for ironborn characters */
	private boolean ironborn = false;
	
	/** The character name */
	@NotNull
	@Size(min=2, max=14)
	private String name;
	
	// The rest is only used by the admin and create form and is validated in the validation class
	
	/** The class of the character */
	private CharClass charClass = CharClass.ANY;
	
	/** The level of the character */
	private int level = -1;
	
	/** How much experience the character has earnt in this level */
	private long xp = -1;
	
	/** The prestige level of the character */
	private int prestigeLevel = -1;
	
	private boolean currentlyDead = false;
	
	private long dungeonId;

	public CharacterForm() {};
	
	public CharacterForm(Character character) {
		this(character.getId(), character.getAccount().getId(), character.isHardcore(), character.isIronborn(), 
				character.getName(), character.getCharClass(), 
				character.getLevel(), character.getXp(), character.getPrestigeLevel(), character.isCurrentlyDead(), 
				character.getDungeon()==null?-1:character.getDungeon().getId());
	}
	
	public CharacterForm(long id, long accountId, boolean hardcore, boolean ironborn, String name, int charClassId, 
			int level, long xp, int prestigeLevel, boolean currentlyDead, long dungeonId) {
		this(id, accountId, hardcore, ironborn, name, CharClass.fromId(charClassId), level, xp, prestigeLevel, currentlyDead, dungeonId);
	}
	public CharacterForm(long id, long accountId, boolean hardcore, boolean ironborn, String name, CharClass charClass, 
			int level, long xp, int prestigeLevel, boolean currentlyDead, long dungeonId) {
		this.id = id;
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.name = name;
		this.charClass = charClass;
		this.level = level;
		this.xp = xp;
		this.prestigeLevel = prestigeLevel;
		this.currentlyDead = currentlyDead;
		this.dungeonId = dungeonId;
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
	
	public void setCharClassId(int charClassId) {
		this.charClass = CharClass.fromId(charClassId);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPrestigeLevel() {
		return prestigeLevel;
	}

	public void setPrestigeLevel(int prestigeLevel) {
		this.prestigeLevel = prestigeLevel;
	}

	public long getXp() {
		return xp;
	}

	public void setXp(long xp) {
		this.xp = xp;
	}
	
	public boolean isCurrentlyDead() {
		return currentlyDead;
	}

	public void setCurrentlyDead(boolean dead) {
		this.currentlyDead = dead;
	}

	public long getDungeonId() {
		return dungeonId;
	}

	public void setDungeonId(long dungeonId) {
		this.dungeonId = dungeonId;
	}

	@Override
	public String toString() {
		return "Character [id=" + id + 
				", accountId=" + accountId + 
				", hardcore=" + (hardcore?"Yes":"No") +
				", ironborn=" + (ironborn?"Yes":"No") +
				", name=" + name + 
				", charClass=" + charClass.getNiceName() + 
				", level=" + level + 
				", xp=" + xp +
				", prestigeLevel=" + prestigeLevel + 
				", currentlyDead=" + (currentlyDead?"yes":"no") + 	
				", dungeonId=" + dungeonId + 
				"]";
	}
}
