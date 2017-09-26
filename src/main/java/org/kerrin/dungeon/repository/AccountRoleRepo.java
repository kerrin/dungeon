package org.kerrin.dungeon.repository;

import java.util.List;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRoleRepo extends JpaRepository<AccountRole, Long>{
	List<AccountRole> findAllByAccount(Account account);
	
	List<AccountRole> findAllByAccountAndPriv(Account account, AccountPrivilege priv);
}
