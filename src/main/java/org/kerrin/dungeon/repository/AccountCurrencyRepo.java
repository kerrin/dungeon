package org.kerrin.dungeon.repository;

import java.util.List;

import org.kerrin.dungeon.model.AccountCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountCurrencyRepo extends JpaRepository<AccountCurrency, Long>{
	List<AccountCurrency> findAllByAccountId(long accountId);

	AccountCurrency findOneByAccountIdAndHardcoreAndIronborn(long accountId, boolean hardcore, boolean ironborn);
}
