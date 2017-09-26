package org.kerrin.dungeon.repository;

import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.enums.HiscoreType;
import org.kerrin.dungeon.model.Hiscore;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface HiscoreRepo extends JpaRepository<Hiscore, Long>{
	Hiscore findById(long hiscoreId);
	List<Hiscore> findAllByAccountIdOrderByTypeAscIronbornAscHardcoreAsc(long accountId);

	/**
	 * 
	 * @param accountId
	 * @param hardcore
	 * @param ironborn
	 * @param type		Hiscore type that is for a specific mode
	 * @return
	 */
	Hiscore getOneByAccountIdAndHardcoreAndIronbornAndType(
			long accountId, boolean hardcore, boolean ironborn, HiscoreType type);
	/**
	 * 
	 * @param type		Hiscore type that is for a specific mode
	 * @param hardcore
	 * @param ironborn
	 * @param sinceDate
	 * @param pageable
	 * @return
	 */
	List<Hiscore> findAllByTypeAndHardcoreAndIronbornAndTimestampGreaterThanOrderByScoreDesc(
			HiscoreType type, boolean hardcore, boolean ironborn, Date sinceDate, Pageable pageable);

	
	/**
	 * 
	 * @param id
	 * @param type Hiscore type that is not for a specific mode
	 * @return
	 */
	Hiscore getOneByAccountIdAndType(long id, HiscoreType type);

	@Modifying
	@Transactional
	@Query("update Hiscore h set h.rank = -1 where h.hardcore = :hardcore AND h.ironborn = :ironborn AND h.timestamp < :timestamp")
	int unrankOutdatedScores(@Param("hardcore") boolean hardcore, @Param("ironborn") boolean ironborn, @Param("timestamp") Date timestamp);}
