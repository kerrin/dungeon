package org.kerrin.dungeon.repository;

import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DungeonRepo extends JpaRepository<Dungeon, Long>{
	List<Dungeon> findAllByAccountOrderByLevelDescStartedDescXpRewardDesc(Account account);

	List<Dungeon> findAllByAccountAndHardcoreAndIronbornOrderByLevelDescStartedDescXpRewardDesc(
			Account account, boolean hardcore, boolean ironborn);

	List<Dungeon> findAllByType(DungeonType type);

	List<Dungeon> findAllByLevelGreaterThanAndLevelLessThan(int greaterLevel, int lessThanLevel);

	List<Dungeon> findAllByAccountAndStartedNotNull(Account account);

	/** Find all the pending dungeons for the mode on the account */
	List<Dungeon> findAllByAccountAndHardcoreAndIronbornAndStartedNull(
			Account account, boolean hardcore, boolean ironborn);

	List<Dungeon> findAllByXpRewardGreaterThanAndXpRewardLessThan(long greaterThanXpReward, long lessThanXpReward);

	List<Dungeon> findAllByStartedGreaterThanAndStartedLessThan(Date greaterThanStarted, Date lessThanStarted);

	List<Dungeon> findAllByPartySizeGreaterThanAndPartySizeLessThan(int greaterThanPartySize, int lessThanPartySize);

	@Query("select d from Dungeon d where KEY(d.itemRewards) = ?1")
	Dungeon findByItemRewardContains(Equipment equipment);

	@Query("select d from Dungeon d where ?1 MEMBER OF d.boostItemRewards")
	Dungeon findByBoostItemRewardContains(BoostItem boostItem);
}
