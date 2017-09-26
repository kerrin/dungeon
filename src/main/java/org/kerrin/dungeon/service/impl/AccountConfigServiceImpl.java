package org.kerrin.dungeon.service.impl;

import java.util.List;

import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountConfig;
import org.kerrin.dungeon.repository.AccountConfigRepo;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountConfigServiceImpl extends ServiceHelppers implements AccountConfigService {
	private static final Logger logger = LoggerFactory.getLogger(AccountConfigServiceImpl.class);

	private final AccountConfigRepo accountConfigRepo;
	
	@Autowired	
    public AccountConfigServiceImpl(AccountConfigRepo accountConfigRepo) {
		super();
		this.accountConfigRepo = accountConfigRepo;;
	}

	@Override
    @Transactional
    public AccountConfig create(AccountConfig accountConfig) {
    	AccountConfig createdAccountConfig = accountConfig;
        return accountConfigRepo.save(createdAccountConfig);
    }

	@Override
	public AccountConfig findById(long id) {
		AccountConfig accountConfig = accountConfigRepo.findOne(id);
        
        return accountConfig;
	}
     
    @Override
    public AccountConfig findByAccount(Account account, boolean hardcore, boolean ironborn, AccountConfigType accountConfigType) {
        AccountConfig accountConfig = accountConfigRepo.findOneByAccountIdAndHardcoreAndIronbornAndConfigType(
        		account.getId(), hardcore, ironborn, accountConfigType);
        if(accountConfig == null) {
        	accountConfig = new AccountConfig(account.getId(), hardcore, ironborn, accountConfigType, 0);
        	accountConfig = create(accountConfig);
        }
        return accountConfig;
    }
    
    @Override
    public List<AccountConfig> findAllByAccount(Account account) {
    	List<AccountConfig> accountCurrencies = accountConfigRepo.findAllByAccountId(account.getId());
        
        return accountCurrencies;
    }

    @Override
    @Transactional()
    public AccountConfig update(AccountConfig accountConfig) {
        AccountConfig updatedAccountConfig = accountConfigRepo.findOneByAccountIdAndHardcoreAndIronbornAndConfigType(
        		accountConfig.getAccountId(), accountConfig.isHardcore(), accountConfig.isIronborn(), accountConfig.getType());
         
        if (updatedAccountConfig == null) {
        	updatedAccountConfig = new AccountConfig(
        			accountConfig.getAccountId(), accountConfig.isHardcore(), accountConfig.isIronborn(), 
        			accountConfig.getType(), 0);
        	updatedAccountConfig = accountConfigRepo.save(updatedAccountConfig);
        }
                
        updatedAccountConfig.setValue(accountConfig.getValue());

        updatedAccountConfig = accountConfigRepo.save(updatedAccountConfig);
        return updatedAccountConfig;
    }
}
