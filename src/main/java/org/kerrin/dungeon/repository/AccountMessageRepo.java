package org.kerrin.dungeon.repository;

import java.util.List;

import org.kerrin.dungeon.model.AccountMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AccountMessageRepo extends JpaRepository<AccountMessage, Long>{
	List<AccountMessage> findAllByAccountIdAndHardcoreIsNullAndIronbornIsNull(long accountId);
	List<AccountMessage> findAllByAccountIdAndHardcoreAndIronborn(long accountId, boolean hardcore, boolean ironborn);

	@Modifying
    @Transactional
    @Query("delete from AccountMessage am where am.accountId = ?1 AND hardcore IS NULL AND ironborn IS NULL")
	void deleteAllByAccountIdAndHardcoreIsNullAndIronbornIsNull(long accountId);
	
	@Modifying
    @Transactional
    @Query("delete from AccountMessage am where am.accountId = ?1 AND hardcore = ?2 AND ironborn = ?3")
	void deleteAllByAccountIdAndHardcoreAndIronborn(long id, boolean hardcore, boolean ironborn);
}
