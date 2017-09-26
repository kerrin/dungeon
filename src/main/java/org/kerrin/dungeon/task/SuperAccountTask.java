package org.kerrin.dungeon.task;

import org.kerrin.dungeon.enums.AccountTask;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
public abstract class SuperAccountTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(SuperAccountTask.class);

	protected AccountService accountService;
	
	protected Account account;
	
	private AccountTask taskType;
	
	protected SuperAccountTask() {}
	
	public SuperAccountTask(
			AccountService accountService,
			Account account,
			AccountTask taskType) {
		this.account = account;
		this.accountService = accountService;
		this.taskType = taskType;
	}
	
	@Override
	public final synchronized void run() {
		logger.debug("Locking account {} for task {}", account.getId(), taskType.name());
		
		try {
			if(!accountService.setProcessing(account, taskType)) {
				logger.debug("Already running {}", taskType.name());
				// Already running, so ignore this
				return;
			}

			realRun();
			accountService.setProcessed(account, taskType);
			logger.debug("Unlocking account {} for task {}", account.getId(), taskType.name());
		} catch (AccountNotFound e) {
			logger.error("Error locking ClearExpiredDungeonsTask");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Actually run the daily process
	 */
	@Transactional
	protected abstract void realRun();
}
