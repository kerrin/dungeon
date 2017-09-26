package org.kerrin.dungeon.repository;

import java.util.List;

import org.kerrin.dungeon.enums.AchievementType;
import org.kerrin.dungeon.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepo extends JpaRepository<Achievement, Long>{
	Achievement findById(long achievementId);
	List<Achievement> findAllByAccountIdOrderByTypeAscIronbornAscHardcoreAsc(long accountId);
	Achievement getOneByAccountIdAndHardcoreAndIronbornAndType(
			long accountId, boolean hardcore, boolean ironborn, AchievementType type);
	List<Achievement> findAllByAccountId(long id);	
	List<Achievement> findAllByAccountIdAndHardcoreAndIronborn(long accountId, boolean hardcore, boolean ironborn);
	List<Achievement> findAllByAccountIdAndHardcoreIsNullAndIronbornIsNull(long accountId);
}
