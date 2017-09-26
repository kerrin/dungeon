package org.kerrin.dungeon.repository;

import java.util.List;

import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.model.AccountConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountConfigRepo extends JpaRepository<AccountConfig, Long>{
	List<AccountConfig> findAllByAccountId(long accountId);

	AccountConfig findAllByAccountIdAndHardcoreAndIronborn(long accountId, boolean hardcore, boolean ironborn);

	AccountConfig findOneByAccountIdAndHardcoreAndIronbornAndConfigType(long accountId, boolean hardcore,
			boolean ironborn, AccountConfigType type);
}
