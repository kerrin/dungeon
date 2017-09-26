package org.kerrin.dungeon.service;

import java.util.List;
import java.util.Set;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.exception.AccountRoleNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountRole;
import org.springframework.stereotype.Service;

@Service
public interface AccountRoleService {
	public List<AccountRole> findAllByAccountAndPriv(Account account, AccountPrivilege priv);
	public AccountRole create(Account account, AccountPrivilege priv);
    public AccountRole delete(Account account, AccountPrivilege priv) throws AccountRoleNotFound;
    public Set<AccountRole> findAllByAccount(Account account);
}
