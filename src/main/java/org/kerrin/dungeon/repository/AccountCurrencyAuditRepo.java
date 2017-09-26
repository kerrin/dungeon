package org.kerrin.dungeon.repository;

import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.model.AccountCurrencyAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountCurrencyAuditRepo extends JpaRepository<AccountCurrencyAudit, Long> {
	List<AccountCurrencyAudit> findAllByAccountIdAndTimestampBetween(long accountId, Date startDate, Date endDate);

	List<AccountCurrencyAudit> findAllByReference(String dailyTokenReference);
}
