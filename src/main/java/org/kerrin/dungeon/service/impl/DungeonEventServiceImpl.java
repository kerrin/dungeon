package org.kerrin.dungeon.service.impl;

import java.util.List;

import org.kerrin.dungeon.enums.DungeonEventType;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonEvent;
import org.kerrin.dungeon.repository.DungeonEventRepo;
import org.kerrin.dungeon.service.DungeonEventService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DungeonEventServiceImpl extends ServiceHelppers implements DungeonEventService {
	private static final Logger logger = LoggerFactory.getLogger(DungeonEventServiceImpl.class);
	private final DungeonEventRepo dungeonEventRepo;

	@Autowired
    public DungeonEventServiceImpl(DungeonEventRepo dungeonEventRepo) {
		super();
		this.dungeonEventRepo = dungeonEventRepo;
	}

	@Override
    @Transactional
    public DungeonEvent create(DungeonEvent dungeonEvent) {
    	DungeonEvent createdDungeonEvent = dungeonEvent;
    	    	
		logger.debug("Saving dungeonEvent: "+dungeonEvent.toString());
        return dungeonEventRepo.save(createdDungeonEvent);
    }
     
    @Override
    @Transactional
    public DungeonEvent findById(long id) {
        DungeonEvent dungeonEvent = dungeonEventRepo.findOne(id);
        
        if(dungeonEvent != null) {
			loadDungeonEventsLinkedTables(dungeonEvent);
        }
        
        return dungeonEvent;
    }
 
    @Override
    public List<DungeonEvent> delete(Dungeon dungeon) {
        List<DungeonEvent> deletedDungeonEvents = dungeonEventRepo.findAllByDungeonOrderByEventOrder(dungeon);
         
        dungeonEventRepo.deleteByDungeon(dungeon);
        return deletedDungeonEvents;
    }
 
	@Override
	@Transactional
	public List<DungeonEvent> findAllByDungeon(Dungeon dungeon) {
		List<DungeonEvent> dungeonEvents = dungeonEventRepo.findAllByDungeonOrderByEventOrder(dungeon);
		for(DungeonEvent dungeonEvent:dungeonEvents) {
			loadDungeonEventsLinkedTables(dungeonEvent);
		}
		
		return dungeonEvents;
	}

	@Override
	@Transactional
	public List<DungeonEvent> findAllByEventType(DungeonEventType type) {
		List<DungeonEvent> dungeonEvents = dungeonEventRepo.findAllByEventTypeOrderByEventOrder(type);
		for(DungeonEvent dungeonEvent:dungeonEvents) {
			loadDungeonEventsLinkedTables(dungeonEvent);
		}
		
		return dungeonEvents;
	}

	@Override
	@Transactional
	public List<DungeonEvent> findAllByCharacter(Character character) {
		List<DungeonEvent> dungeonEvents = dungeonEventRepo.findAllByEffectedCharacterOrderByEventOrder(character);
		for(DungeonEvent dungeonEvent:dungeonEvents) {
			loadDungeonEventsLinkedTables(dungeonEvent);
		}
		
		return dungeonEvents;
	}

	@Override
	@Transactional
	public List<DungeonEvent> findAllByMonsterIdAndMonsterTypeId(int monsterId, int monsterTypeId) {
		List<DungeonEvent> dungeonEvents = dungeonEventRepo.findAllByMonsterIdAndMonsterTypeIdOrderByEventOrder(monsterId, monsterTypeId);
		for(DungeonEvent dungeonEvent:dungeonEvents) {
			loadDungeonEventsLinkedTables(dungeonEvent);
		}
		
		return dungeonEvents;
	}
}
