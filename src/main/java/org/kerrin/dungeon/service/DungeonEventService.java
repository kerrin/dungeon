package org.kerrin.dungeon.service;

import java.util.List;

import org.kerrin.dungeon.enums.DungeonEventType;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonEvent;
import org.springframework.stereotype.Service;

@Service
public interface DungeonEventService {
	public DungeonEvent create(DungeonEvent dungeon);
    public List<DungeonEvent> delete(Dungeon dungeon);
    public DungeonEvent findById(long id);
	public List<DungeonEvent> findAllByDungeon(Dungeon dungeon);
	public List<DungeonEvent> findAllByEventType(DungeonEventType type);
	public List<DungeonEvent> findAllByCharacter(Character character);
	public List<DungeonEvent> findAllByMonsterIdAndMonsterTypeId(int monsterId, int monsterTypeId);
}
