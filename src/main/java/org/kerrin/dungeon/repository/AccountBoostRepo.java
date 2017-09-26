package org.kerrin.dungeon.repository;

import java.util.List;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.model.AccountBoost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBoostRepo extends JpaRepository<AccountBoost, Long>{
	List<AccountBoost> findAllByAccountId(long accountId);

	AccountBoost findOneByAccountIdAndHardcoreAndIronborn(long accountId, boolean hardcore, boolean ironborn);

	AccountBoost findOneByAccountIdAndHardcoreAndIronbornAndType(
			long accountId, boolean hardcore, boolean ironborn, BoostItemType type);
}
