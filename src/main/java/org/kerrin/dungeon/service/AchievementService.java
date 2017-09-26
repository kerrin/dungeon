package org.kerrin.dungeon.service;

import java.util.List;

import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Hiscore;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.springframework.stereotype.Service;

@Service
public interface AchievementService {
	/** 
	 * Check for achievements after a character levels up
	 * Call after adjusting level 
	 */
	public List<Achievement> levelUp(String contextRoot, Character character);

	/** 
	 * Check for achievements after a character gets prestige levels 
	 * Call after adjusting level 
	 */
	public List<Achievement> prestigeLevel(String contextRoot, Character character);

	/** 
	 * Check for achievements after a character gets an item equiped 
	 * Call after adding to character 
	 */
	public List<Achievement> equipItem(String contextRoot, Equipment equipment);

	/** 
	 * Check for achievements after a dungeon gets closed 
	 * Call before closing dungeon
	 * @param contextRoot
	 * @param dungeon		The dungeon being closed
	 * @param rushed		If this was closed due to a rush
	 */
	public List<Achievement> dungeonClose(String contextRoot, Dungeon dungeon);

	/** 
	 * Check for achievements after creating a new character
	 * Call after adding character 
	 */
	public List<Achievement> newCharacter(String contextRoot, Character character);

	/** 
	 * Check for achievements after increasing stash size
	 * Call after increasing stash size
	 */
	public List<Achievement> stashIncrease(String contextRoot, Account account, boolean hardcore, boolean ironborn);

	/** 
	 * Check for achievements after character is resurrected 
	 * Call after resurrecting character
	 */
	public List<Achievement> resurrected(String contextRoot, Character character);

	/** 
	 * Check for achievements after a login 
	 * Call after login successful
	 */
	public List<Achievement> login(String contextRoot, Account account);

	/** 
	 * Check for achievements after hiscore position change
	 * Call after new ranking
	 * Only need to call on top 100 people in each hiscore
	 */
	public List<Achievement> hiscore(String contextRoot, Hiscore hiscore);
	
	/** 
	 * Check for achievements after salvaging an item
	 * Call before salvaging
	 */
	public List<Achievement> salvage(String contextRoot, Account account, StashSlotItemSuper item);
	
	/** 
	 * Check for achievements after enchanting an item
	 * Call after enchanting
	 */
	public List<Achievement> enchant(String contextRoot, Account account, Equipment equipment);

	/**
	 * 
	 * @param contextPath
	 * @param account
	 * @param boostItem
	 * @return
	 */
	public List<Achievement> redeemBoostItem(String contextPath, Account account, BoostItem boostItem);

	/**
	 * 
	 * @param contextPath
	 * @param dungeon
	 * @return
	 */
	public List<Achievement> rushDungeon(String contextPath, Dungeon dungeon);

	public Achievement create(Achievement achievement);
	public Achievement update(Achievement achievement);
    public Achievement findById(long achievementId);
	public List<Achievement> findAllByAccount(Account account);
	public List<Achievement> findAllByAccount(Account account, Boolean hardcore, Boolean ironborn);
	
	/**
	 * Get the achievements
	 * @param account
	 * @param hardcore
	 * @param ironborn
	 * 
	 * @return
	 */
	public List<Achievement> getAchievements(Account account, Boolean hardcore, Boolean ironborn);
	/**
	 * 
	 * @param account
	 * @return
	 */
	public long getAchievementPoints(Account account);
	
	/**
	 * 
	 * @param account
	 * @param hardcore
	 * @param ironborn
	 * @return
	 */
	public long getAchievementPoints(Account account, Boolean hardcore, Boolean ironborn);
}
