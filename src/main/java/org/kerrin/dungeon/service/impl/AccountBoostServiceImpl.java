package org.kerrin.dungeon.service.impl;

import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountBoost;
import org.kerrin.dungeon.repository.AccountBoostRepo;
import org.kerrin.dungeon.service.AccountBoostService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountBoostServiceImpl extends ServiceHelppers implements AccountBoostService {
	private static final Logger logger = LoggerFactory.getLogger(AccountBoostServiceImpl.class);

	private final AccountBoostRepo accountBoostRepo;
	
	@Autowired	
    public AccountBoostServiceImpl(AccountBoostRepo accountBoostRepo) {
		super();
		this.accountBoostRepo = accountBoostRepo;
	}

	@Override
    @Transactional
    public AccountBoost redeemBoost(AccountBoost accountBoost) {
    	AccountBoost redeemAccountBoost = accountBoost;
    	
    	AccountBoost foundAccountBoost = accountBoostRepo.findOneByAccountIdAndHardcoreAndIronbornAndType(
    			accountBoost.getAccountId(), accountBoost.isHardcore(), accountBoost.isIronborn(), accountBoost.getType());
    	
    	if(foundAccountBoost != null) {
    		redeemAccountBoost = foundAccountBoost;
    		Date now = new Date();
    		long additionalMilliseconds = accountBoost.getEndDateTime().getTime() - now.getTime();
			redeemAccountBoost.increaseExpiry(accountBoost, (int)additionalMilliseconds / 60000);
    	}
    	
        return accountBoostRepo.save(redeemAccountBoost);
    }

	@Override
	public AccountBoost findById(long id) {
		AccountBoost accountBoost = accountBoostRepo.findOne(id);
        
        return accountBoost;
	}
     
    @Override
    public AccountBoost findByAccount(Account account, boolean hardcore, boolean ironborn) {
        AccountBoost accountBoost = accountBoostRepo.findOneByAccountIdAndHardcoreAndIronborn(
        		account.getId(), hardcore, ironborn);
        
        return accountBoost;
    }
    
    @Override
    public List<AccountBoost> findAllByAccount(Account account) {
    	List<AccountBoost> accountBoosts = accountBoostRepo.findAllByAccountId(account.getId());
        
        return accountBoosts;
    }

    @Override
    @Transactional()
    public AccountBoost update(AccountBoost accountBoost) {
        AccountBoost updatedAccountBoost = accountBoostRepo.findOneByAccountIdAndHardcoreAndIronborn(
        		accountBoost.getAccountId(), accountBoost.isHardcore(), accountBoost.isIronborn());
         
        if (updatedAccountBoost == null) {
        	updatedAccountBoost = new AccountBoost(
        			accountBoost.getAccountId(), accountBoost.isHardcore(), accountBoost.isIronborn(), 
        			accountBoost.getType(), accountBoost.getEndDateTime());
        	updatedAccountBoost = accountBoostRepo.save(updatedAccountBoost);
        }
        
        updatedAccountBoost.setType(accountBoost.getType());
        updatedAccountBoost.setEndDateTime(accountBoost.getEndDateTime());
        
        updatedAccountBoost = accountBoostRepo.save(updatedAccountBoost);
        return updatedAccountBoost;
    }

	@Override
	public boolean delete(AccountBoost accountBoost) {
		accountBoostRepo.delete(accountBoost);
		return true;
	}

	@Override
	public Date getMagicFindBoostExpires(Account account, boolean hardcore, boolean ironborn) {
		return getBoostExpires(account, hardcore, ironborn, BoostItemType.MAGIC_FIND);
	}

	@Override
	public Date getXpBoostExpires(Account account, boolean hardcore, boolean ironborn) {
		return getBoostExpires(account, hardcore, ironborn, BoostItemType.XP_BOOST);
	}
	
	/**
	 * Find an account boost expiry or null if no expiry
	 * 
	 * @param account
	 * @param hardcore
	 * @param ironborn
	 * @param type
	 * @return
	 */
	private Date getBoostExpires(Account account, boolean hardcore, boolean ironborn, BoostItemType type) {
		AccountBoost boost = accountBoostRepo.findOneByAccountIdAndHardcoreAndIronbornAndType(account.getId(), hardcore, ironborn, type);
		if(boost == null) {
			return null;
		} else {
			Date now = new Date();
			if(now.before(boost.getEndDateTime())) {
				return boost.getEndDateTime();
			} else {
				accountBoostRepo.delete(boost);
				return null;
			}
		}
	}
}
