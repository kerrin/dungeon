package org.kerrin.dungeon.service;

import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.enums.HiscoreType;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Hiscore;
import org.springframework.stereotype.Service;

@Service
public interface HiscoreService {
	/** Highest character level (base level + prestige level) */
	public Hiscore highestLevel(Account account, boolean hardcore, boolean ironborn);
	/** Highest character level (base level + prestige level), sets score to level */
	public Hiscore highestLevel(Account account, boolean hardcore, boolean ironborn, int level);
	/** Highest total level of all characters including prestige */
	public Hiscore totalLevel(Account account, boolean hardcore, boolean ironborn);
	/** Highest total level of all characters including prestige, sets score to level */
	public Hiscore totalLevel(Account account, boolean hardcore, boolean ironborn, int level);
	/** Fastest character from 1 to max level only using dungeons */
	public Hiscore fastestMaxLevel(Account account, boolean hardcore, boolean ironborn);
	/** Fastest character from 1 to max level only using dungeons, sets score to duration */
	public Hiscore fastestMaxLevel(Account account, boolean hardcore, boolean ironborn, long duration);
	/** Fastest character from 1 to max level only using dungeons after an account reset */
	public Hiscore fastestMaxLevelAfterReset(Account account, boolean hardcore, boolean ironborn);
	/** Fastest character from 1 to max level only using dungeons after an account reset, sets score to duration */
	public Hiscore fastestMaxLevelAfterReset(Account account, boolean hardcore, boolean ironborn, long duration);
	/** Most tokens earnt */
	public Hiscore tokensEarnt(Account account, boolean hardcore, boolean ironborn);
	/** Most tokens earnt, adds earnt to existing total */
	public Hiscore tokensEarnt(Account account, boolean hardcore, boolean ironborn, int earnt);
	/** Most tokens purchased for real money */
	public Hiscore tokensPurchased(Account account, boolean hardcore, boolean ironborn);
	/** Most tokens purchased for real money, adds purchased to existing total */
	public Hiscore tokensPurchased(Account account, boolean hardcore, boolean ironborn, int purchased);
	
	/** Most achievement points */
	public Hiscore achievement(Account account);
	/** Most achievement points, adds points to existing score */
	public Hiscore achievement(Account account, int points);
	
	public Hiscore create(Hiscore hiscore);
	public Hiscore update(Hiscore hiscore);
    public Hiscore findById(long hiscoreId);
	public List<Hiscore> findAllByAccount(Account account);
	
	/**
	 * Find the top scores for the type since the date
	 * @param type
	 * @param hardcore
	 * @param ironborn
	 * @param sinceDate
	 * @param limit
	 * @return
	 */
	public List<Hiscore> findAllByTypeAndHardcoreAndIronborn(
			HiscoreType type, boolean hardcore, boolean ironborn, Date sinceDate, int offset, int limit);
	/**
	 * Set all scores before passed date to be unranked, as they are too old
	 * @param hardcore
	 * @param ironborn
	 * @param sinceDate
	 */
	public void unrankOutdatedScores(boolean hardcore, boolean ironborn, Date sinceDate);
}
