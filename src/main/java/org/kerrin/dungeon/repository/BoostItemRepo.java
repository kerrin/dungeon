package org.kerrin.dungeon.repository;

import java.util.List;

import org.kerrin.dungeon.enums.BoostItemType;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoostItemRepo extends JpaRepository<BoostItem, Long>{

	List<BoostItem> findAllByLevelGreaterThanAndLevelLessThan(int greaterThan, int lessThan);

	List<BoostItem> findAllByAccount(Account account);

	List<BoostItem> findAllByAccountAndStashSlotIdAndDungeonIdAndMessageIdAndHardcoreAndIronborn(
			Account account, int stashSlotId, long dungeonId, long messageId, boolean hardcore, boolean ironborn);

	BoostItem findFirstByAccountAndStashSlotIdAndDungeonIdAndMessageIdAndHardcoreAndIronbornAndBoostItemTypeAndLevelLessThanOrderByLevelDesc(
			Account account, int stashSlotId, long dungeonId, long messageId, boolean hardcore, boolean ironborn, 
			BoostItemType type, int level);

	BoostItem findFirstByAccountAndStashSlotIdAndDungeonIdAndMessageIdAndHardcoreAndIronbornAndBoostItemTypeAndLevelGreaterThanOrderByLevel(
			Account account, int stashSlotId, long dungeonId, long messageId, boolean hardcore, boolean ironborn, 
			BoostItemType type, int level);
	
}
