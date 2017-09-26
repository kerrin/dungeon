package org.kerrin.dungeon.service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.kerrin.dungeon.enums.AccountTask;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountHistory;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Equipment;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
	public Account create(Account account);
    public Account delete(long id) throws AccountNotFound;
    public List<Account> findAll();
    /**
     * 
     * @param account	Account to set
     * @param login		Is a successful login, will change API key
     * @return
     * @throws AccountNotFound
     */
    public Account update(Account account, boolean login) throws AccountNotFound;
    public Account findById(long id);
	public Account findByUsername(String username);
	public Account findBySocialUserId(String socialUserId);
	
	/**
	 * Get an account from it's login details
	 * @param principle
	 * @return
	 */
	public Account findByPrinciple(Principal principle);
	/**
	 * Get an account from it's API key
	 * @param accountApiKey
	 * @return
	 */
	public Account findByApiKey(String accountApiKey);
	public List<Account> findAllByDisplayName(String displayName);

	/**
	 * Get the maximum character level on the account
	 * 
	 * @param accountId
	 * @param hardcore
	 * @param ironborn
	 * 
	 * @return Maximum character level
	 */
	public int getMaxCharacterLevel(Account account, boolean hardcore, boolean ironborn);
	/**
	 * Find count of characters that could run a dungeon at the level and get some XP
	 * Characters that are too low will die, so are discounted
	 * Characters that are too high will get no XP, so are discounted
	 * 
	 * @param account	Account to check
	 * @param hardcore
	 * @param ironborn
	 * @param level		Level of dungeon
	 * @return	Number of characters
	 */
	public int getNumberOfCharactersAroundLevel(Account account, boolean hardcore, boolean ironborn, int level);
	/**
	 * Get all the dungeons, etc up to date
	 * This will start an async task to process the account
	 * @param account	Account to process
	 * @param force		Will run the process even if it was already running
	 * @return started (false means it was running already)
	 */
	public void processAccount(Account account);
	/**
	 * Create any dungeons we need to
	 * This will start an async task to process the account
	 * @param account	Account to process
	 * @return started (false means it was running already)
	 */
	public void createDungeons(Account account);
	/**
	 * Schedule the processing of the dungeons on this account
	 * This will start an async task to process the account at the passed time, or immediately if the date is null
	 * @param account	Account to process
	 * @param scheduleDate	Date to start processing
	 * @return started (false means it was running already)
	 */
	public void processDungeons(Account account, Date scheduleDate);
	/**
	 * Check the equipment is owned by the account (on a character, in inventory, or a won award from dungeon)
	 * 
	 * @param account
	 * @param equipment
	 * @return
	 */
	public boolean accountOwnsEquipment(Account account, Equipment equipment);
	/**
	 * Check the boostItem is owned by the account (in inventory, or a won award from dungeon)
	 * 
	 * @param account
	 * @param equipment
	 * @return
	 */
	public boolean accountOwnsBoostItem(Account account, BoostItem boostItem);
	/**
	 * 
	 * @param limit
	 * @return
	 */
	public List<Account> findLastLogins();
	
	/**
	 * Check if the task is already running, and if not set it as running
	 * 
	 * @param task
	 * @return
	 * @throws AccountNotFound 
	 */
	public boolean setProcessing(Account account, AccountTask task) throws AccountNotFound;
	
	/**
	 * Check if the task is already running, and if not set it as running
	 * 
	 * @param task
	 * @return
	 * @throws AccountNotFound 
	 */
	public void setProcessed(Account account, AccountTask task) throws AccountNotFound;
	
	/**
	 * Get the account history to display
	 * @param account
	 * @param locale 	the locale of the user
	 * @return
	 */
	public AccountHistory getHistory(Account account, Locale locale);
	
}
