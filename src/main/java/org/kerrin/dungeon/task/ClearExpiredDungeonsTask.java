package org.kerrin.dungeon.task;

import java.util.List;

import org.kerrin.dungeon.enums.AccountTask;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.DungeonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
public class ClearExpiredDungeonsTask extends SuperAccountTask {
	private static final Logger logger = LoggerFactory.getLogger(ClearExpiredDungeonsTask.class);
	
	private DungeonService dungeonService;
	
	protected ClearExpiredDungeonsTask() {}
	
	public ClearExpiredDungeonsTask(
			AccountService accountService,
			DungeonService dungeonService, 
			Account account) {
		super(accountService, account, AccountTask.CLEAR_EXPIRED_DUNGEONS);
		this.dungeonService = dungeonService;
	}
	
	@Transactional
	@Override
	protected void realRun() {
		// Clear out expired dungeons (started dungeons have no expiry)
		List<Dungeon> dungeons = dungeonService.findAllByAccountAndExpired(account);
		for(Dungeon dungeon:dungeons) {
			try {
				dungeonService.delete(dungeon);
			} catch (DungeonNotFound e) {
				logger.error("Failed to delete expired dungeon "+dungeon.toString());
			} catch (EquipmentNotFound e) {
				logger.error("Failed to delete expired dungeon due to equipment "+dungeon.toString());
			}
		}
	}

}
