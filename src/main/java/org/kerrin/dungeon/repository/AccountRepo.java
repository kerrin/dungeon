package org.kerrin.dungeon.repository;

import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.model.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long>{
	Account findOneByUsername(String userame);
	
	List<Account> findAllByDisplayName(String displayName);

	Account findByUsername(String username);

	Account findBySocialUserId(String socialUserId);

	Account findByApiKey(String accountApiKey);

	List<Account> findAllByLastLoginGreaterThanOrderByLastLogin(Date oldestLogin, Pageable pagable);
}
