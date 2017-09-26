package org.kerrin.dungeon.service;

import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountCurrency;
import org.kerrin.dungeon.model.AccountCurrencyAudit;
import org.springframework.stereotype.Service;

@Service
public interface AccountCurrencyService {
	public AccountCurrency create(AccountCurrency accountCurrency, String username);
	public AccountCurrency update(AccountCurrency accountCurrency, ModificationType modificationType, String reference);
    public AccountCurrency findById(long id);
	public AccountCurrency findByAccount(Account account, boolean hardcore, boolean ironborn);
	public List<AccountCurrency> findAllByAccount(Account account);
	
	/**
	 * Reduce the currency by the amount requested, if they have enough currency
	 * 
	 * @param accountId		Account to remove currency from
	 * @param hardcore		Currency is for hardcore characters
	 * @param ironborn		Currency is for ironborn characters
	 * @param dungeonTokens	Currency to add or reduce by (-ve is reduction)
	 * @param modificationType	Type of adjustment
	 * @param reference		Unique reference for adjustment
	 * @return	Action successful, they had enough currency to make the deduction if it was a deduction
	 * @throws AccountNotFound
	 */
	public boolean adjustCurrency(Account account, boolean hardcore, boolean ironborn, long dungeonTokens, ModificationType modificationType, String reference);
	/**
	 * Get all the audit records for the account between the supplied dates
	 * 
	 * @param accountId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<AccountCurrencyAudit> findAuditByAccountIdAndDateBetween(long accountId, Date startDate, Date endDate);
	/**
	 * 
	 * @param account
	 * @param hardcore
	 * @param ironborn
	 * @return
	 */
	public boolean haveAwardedDailyTokensToday(Account account, boolean hardcore, boolean ironborn);
	/**
	 * 
	 * @param account
	 * @param hardcore
	 * @param ironborn
	 * @return
	 */
	public String getDailyTokenReference(Account account, boolean hardcore, boolean ironborn);
}
