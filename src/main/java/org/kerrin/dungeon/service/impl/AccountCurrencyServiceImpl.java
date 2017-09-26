package org.kerrin.dungeon.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountCurrency;
import org.kerrin.dungeon.model.AccountCurrencyAudit;
import org.kerrin.dungeon.repository.AccountCurrencyAuditRepo;
import org.kerrin.dungeon.repository.AccountCurrencyRepo;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountCurrencyServiceImpl extends ServiceHelppers implements AccountCurrencyService {
	private static final Logger logger = LoggerFactory.getLogger(AccountCurrencyServiceImpl.class);

	private final AccountCurrencyRepo accountCurrencyRepo;
	private final AccountCurrencyAuditRepo accountCurrencyAuditRepo;
	
	@Autowired	
    public AccountCurrencyServiceImpl(AccountCurrencyRepo accountCurrencyRepo, AccountCurrencyAuditRepo accountCurrencyAuditRepo) {
		super();
		this.accountCurrencyRepo = accountCurrencyRepo;
		this.accountCurrencyAuditRepo = accountCurrencyAuditRepo;
	}

	@Override
    @Transactional
    public AccountCurrency create(AccountCurrency accountCurrency, String username) {
    	AccountCurrency createdAccountCurrency = accountCurrency;
    	String reference = "createaccount-" + username;
    	AccountCurrencyAudit accountCurrencyAudit = 
    			new AccountCurrencyAudit(accountCurrency.getAccountId(), 
    					accountCurrency.isHardcore(), accountCurrency.isIronborn(), 
    					new Date(), accountCurrency.getCurrency(), ModificationType.GAIN_CREATE, reference);
		accountCurrencyAuditRepo.save(accountCurrencyAudit );
        return accountCurrencyRepo.save(createdAccountCurrency);
    }

	@Override
	public AccountCurrency findById(long id) {
		AccountCurrency accountCurrency = accountCurrencyRepo.findOne(id);
        
        return accountCurrency;
	}
     
    @Override
    public AccountCurrency findByAccount(Account account, boolean hardcore, boolean ironborn) {
        AccountCurrency accountCurrency = accountCurrencyRepo.findOneByAccountIdAndHardcoreAndIronborn(
        		account.getId(), hardcore, ironborn);
        
        return accountCurrency;
    }
    
    @Override
    public List<AccountCurrency> findAllByAccount(Account account) {
    	List<AccountCurrency> accountCurrencies = accountCurrencyRepo.findAllByAccountId(account.getId());
        
        return accountCurrencies;
    }

    @Override
    @Transactional()
    public AccountCurrency update(AccountCurrency accountCurrency, ModificationType modificationType, String reference) {
        AccountCurrency updatedAccountCurrency = accountCurrencyRepo.findOneByAccountIdAndHardcoreAndIronborn(
        		accountCurrency.getAccountId(), accountCurrency.isHardcore(), accountCurrency.isIronborn());
         
        if (updatedAccountCurrency == null) {
        	updatedAccountCurrency = new AccountCurrency(
        			accountCurrency.getAccountId(), accountCurrency.isHardcore(), accountCurrency.isIronborn(), 
        			AccountServiceImpl.STARTING_CURRENCY);
        	updatedAccountCurrency = accountCurrencyRepo.save(updatedAccountCurrency);
        }
        
        long currencyChange = accountCurrency.getCurrency() - updatedAccountCurrency.getCurrency();
		AccountCurrencyAudit audit = new AccountCurrencyAudit(
				accountCurrency.getAccountId(), 
				accountCurrency.isHardcore(), accountCurrency.isIronborn(), 
				new Date(), currencyChange, modificationType, reference);
		accountCurrencyAuditRepo.save(audit);
        
        updatedAccountCurrency.setCurrency(accountCurrency.getCurrency());

        updatedAccountCurrency = accountCurrencyRepo.save(updatedAccountCurrency);
        return updatedAccountCurrency;
    }

	@Override
	@Transactional
	public boolean adjustCurrency(Account account, boolean hardcore, boolean ironborn,  
			long dungeonTokensToAdjust, ModificationType modificationType, String reference) {
		AccountCurrency updatedAccountCurrency = accountCurrencyRepo.findOneByAccountIdAndHardcoreAndIronborn(
				account.getId(), hardcore, ironborn);
        
        if (updatedAccountCurrency == null) {
        	updatedAccountCurrency = new AccountCurrency(account.getId(), hardcore, ironborn, AccountServiceImpl.STARTING_CURRENCY);
        	updatedAccountCurrency = accountCurrencyRepo.save(updatedAccountCurrency);
        }
        
        if(!updatedAccountCurrency.modifyCurrency(dungeonTokensToAdjust)) {
        	return false;
        }
        
        AccountCurrencyAudit audit = new AccountCurrencyAudit(
				account.getId(), hardcore, ironborn, 
				new Date(), dungeonTokensToAdjust, modificationType, reference);
		accountCurrencyAuditRepo.save(audit);
		
        accountCurrencyRepo.save(updatedAccountCurrency);
        
		return true;
	}

	@Override
	@Transactional
	public List<AccountCurrencyAudit> findAuditByAccountIdAndDateBetween(long accountId, Date startDate, Date endDate) {
		// Check the dates are in the right order
		if(startDate.after(endDate)) {
			Date temp = startDate;
			startDate = endDate;
			endDate = temp;
		}
		// Set the dates to the start and end of the day
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		startDate = cal.getTime();
		
		cal.setTime(endDate);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		endDate = cal.getTime();
		List<AccountCurrencyAudit> audits = accountCurrencyAuditRepo.findAllByAccountIdAndTimestampBetween(accountId, startDate, endDate);
		return audits;
	}

	@Override
	public boolean haveAwardedDailyTokensToday(Account account, boolean hardcore, boolean ironborn) {
		List<AccountCurrencyAudit> audits = accountCurrencyAuditRepo.findAllByReference(
				getDailyTokenReference(account, hardcore, ironborn));
		return !audits.isEmpty();
	}

	@Override
	public String getDailyTokenReference(Account account, boolean hardcore, boolean ironborn) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");	
		String day = sdf.format(new Date());
		String reference = "daily-"+account.getUsername()+(hardcore?"-hardcore":"")+(ironborn?"-ironborn":"")+"-"+day;			
		return reference;
	}
}
