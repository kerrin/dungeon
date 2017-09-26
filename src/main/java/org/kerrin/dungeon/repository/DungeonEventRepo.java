package org.kerrin.dungeon.repository;

import java.util.List;

import org.kerrin.dungeon.enums.DungeonEventType;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonEvent;
import org.springframework.data.repository.CrudRepository;

public interface DungeonEventRepo extends CrudRepository<DungeonEvent, Long>{
	List<DungeonEvent> findAllByDungeonOrderByEventOrder(Dungeon dungeon);

	List<DungeonEvent> findAllByEventTypeOrderByEventOrder(DungeonEventType type);

	List<DungeonEvent> findAllByEffectedCharacterOrderByEventOrder(Character character);

	List<DungeonEvent> findAllByMonsterIdAndMonsterTypeIdOrderByEventOrder(int monsterId, int monsterTypeId);

	Long deleteByDungeon(Dungeon dungeon);
}
