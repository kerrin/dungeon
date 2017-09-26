package org.kerrin.dungeon.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.exception.AccountRoleNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountRole;
import org.kerrin.dungeon.repository.AccountRoleRepo;
import org.kerrin.dungeon.service.AccountRoleService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountRoleServiceImpl extends ServiceHelppers implements AccountRoleService {

    private final AccountRoleRepo accountRoleRepo;

	@Autowired
	public AccountRoleServiceImpl(AccountRoleRepo accountRoleRepo) {
		super();
		this.accountRoleRepo = accountRoleRepo;
	}

	@Override
	public AccountRole create(Account account, AccountPrivilege priv) {
		AccountRole createdAccountRole = new AccountRole(account, priv);
        return accountRoleRepo.save(createdAccountRole);
	}

	@Override
	@Transactional
	public AccountRole delete(Account account, AccountPrivilege priv) throws AccountRoleNotFound {
		List<AccountRole> deletedAccountRoles = accountRoleRepo.findAllByAccountAndPriv(account, priv);
        
        if (deletedAccountRoles.isEmpty())
            throw new AccountRoleNotFound();
         
        for(AccountRole role : deletedAccountRoles) {
        	accountRoleRepo.delete(role);
        }
        return deletedAccountRoles.get(0);
	}

	@Override
	@Transactional
	public Set<AccountRole> findAllByAccount(Account account) {
		List<AccountRole> roles = accountRoleRepo.findAllByAccount(account);
		Set<AccountRole> rolesSet = new HashSet<AccountRole>();
		for(AccountRole role : roles) {
			rolesSet.add(role);
		}
		return rolesSet;
	}

	@Override
	public List<AccountRole> findAllByAccountAndPriv(Account account, AccountPrivilege priv) {		
		return accountRoleRepo.findAllByAccountAndPriv(account, priv);
	}
}
