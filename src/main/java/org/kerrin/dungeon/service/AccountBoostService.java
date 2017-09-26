package org.kerrin.dungeon.service;

import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountBoost;
import org.springframework.stereotype.Service;

@Service
public interface AccountBoostService {
	/**
	 * Create or update the end date and time when this is redeemed
	 * 
	 * @param accountBoost
	 * @return
	 */
	public AccountBoost redeemBoost(AccountBoost accountBoost);
	/**
	 * Update the account boost
	 * 
	 * @param accountBoost
	 * @return
	 */
	public AccountBoost update(AccountBoost accountBoost);
    public AccountBoost findById(long id);
	public AccountBoost findByAccount(Account account, boolean hardcore, boolean ironborn);
	public List<AccountBoost> findAllByAccount(Account account);
	
	public boolean delete(AccountBoost accountBoost);
	
	/**
	 * 
	 * @param account
	 * @param hardcore
	 * @param ironborn
	 * @return
	 */
	
	public Date getMagicFindBoostExpires(Account account, boolean hardcore, boolean ironborn);
	/**
	 * 
	 * @param account
	 * @param hardcore
	 * @param ironborn
	 * @return
	 */
	public Date getXpBoostExpires(Account account, boolean hardcore, boolean ironborn);
}
