package org.kerrin.dungeon.service;

import java.util.List;

import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountConfig;
import org.springframework.stereotype.Service;

@Service
public interface AccountConfigService {
	public AccountConfig create(AccountConfig accountConfig);
	public AccountConfig update(AccountConfig accountConfig);
    public AccountConfig findById(long id);
	public AccountConfig findByAccount(Account account, boolean hardcore, boolean ironborn, AccountConfigType accountConfigType);
	public List<AccountConfig> findAllByAccount(Account account);
}
