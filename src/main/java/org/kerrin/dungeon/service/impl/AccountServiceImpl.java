package org.kerrin.dungeon.service.impl;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.kerrin.dungeon.enums.AccountConfigType;
import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.AccountTask;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.exception.AccountRoleNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountConfig;
import org.kerrin.dungeon.model.AccountCurrency;
import org.kerrin.dungeon.model.AccountCurrencyAudit;
import org.kerrin.dungeon.model.AccountHistory;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.AccountRole;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.Inventory;
import org.kerrin.dungeon.model.StashSlotItem.TYPE;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.repository.AccountMessageRepo;
import org.kerrin.dungeon.repository.AccountRepo;
import org.kerrin.dungeon.service.AccountBoostService;
import org.kerrin.dungeon.service.AccountConfigService;
import org.kerrin.dungeon.service.AccountCurrencyService;
import org.kerrin.dungeon.service.AccountRoleService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.DungeonService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.service.InventoryService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.kerrin.dungeon.task.ClearExpiredDungeonsTask;
import org.kerrin.dungeon.task.CreateDungeonsTask;
import org.kerrin.dungeon.task.DailyProcessorTask;
import org.kerrin.dungeon.task.DungeonProcessorTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl extends ServiceHelppers implements AccountService {
	private static final Logger logger = LoggerFactory.getLogger(AccountRoleServiceImpl.class);

	private static final int EQUIPMENT_COMPARE_VALUE = 1;
	private static final int TOOL_TIPS_VALUE = 2;
	
	public static final long STARTING_CURRENCY = 5;

	private static final Date PROCESSING = new Date(1); // Can't be null, so we cope with missing entries
	private static final Date PROCESSING_CHECK = new Date(14400000); // Allow for timezones messing up the equals check by adding a day

	private static final long HOUR_IN_MILLIS = 3600000;

	private final AccountRepo accountRepo;

	private final AccountConfigService accountConfigService;
	private final AccountCurrencyService accountCurrencyService;
	private final AccountRoleService accountRoleService;
	private final CharacterService characterService;
	private final CharacterEquipmentService characterEquipmentService;
	private final InventoryService inventoryService;
	private final DungeonService dungeonService;
	private final EquipmentService equipmentService;
	private final BoostItemService boostItemService;
	private final AccountBoostService accountBoostService;
	private final HiscoreService hiscoreService;
	private final AccountMessageRepo accountMessageRepo;
	private final ServletContext servletContext;

	private final TaskExecutor taskExecuter;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
	
	@Autowired
    public AccountServiceImpl(AccountRepo accountRepo, AccountMessageRepo accountMessageRepo,
    		AccountConfigService accountConfigService, AccountCurrencyService accountCurrencyService,
			AccountRoleService accountRoleService, CharacterService characterService,
			CharacterEquipmentService characterEquipmentService, InventoryService inventoryService, 
			DungeonService dungeonService, EquipmentService equipmentService, 
			BoostItemService boostItemService, AccountBoostService accountBoostService,
			HiscoreService hiscoreService, 
			TaskExecutor taskExecuter, ServletContext servletContext) {
		super();
		this.accountRepo = accountRepo;
		this.accountMessageRepo = accountMessageRepo;
		this.accountConfigService = accountConfigService;
		this.accountCurrencyService = accountCurrencyService;
		this.accountRoleService = accountRoleService;
		this.characterService = characterService;
		this.characterEquipmentService = characterEquipmentService;
		this.inventoryService = inventoryService;
		this.dungeonService = dungeonService;
		this.equipmentService = equipmentService;
		this.boostItemService = boostItemService;
		this.accountBoostService = accountBoostService;
		this.hiscoreService = hiscoreService;
		this.taskExecuter = taskExecuter;
		this.servletContext = servletContext;
	}
	
	
    @Override
    @Transactional
    public Account create(Account account) {
    	Account createdAccount = account;
    	createdAccount = accountRepo.save(createdAccount);
    	
    	AccountConfig accountConfig = new AccountConfig(createdAccount.getId(), false, false, AccountConfigType.EQUIPMENT_COMPARE, EQUIPMENT_COMPARE_VALUE);
    	accountConfigService.create(accountConfig);
    	accountConfig = new AccountConfig(createdAccount.getId(), false, false, AccountConfigType.TOOL_TIPS, TOOL_TIPS_VALUE);
    	accountConfigService.create(accountConfig);
    	accountConfig = new AccountConfig(createdAccount.getId(), true, false, AccountConfigType.EQUIPMENT_COMPARE, EQUIPMENT_COMPARE_VALUE);
    	accountConfigService.create(accountConfig);
    	accountConfig = new AccountConfig(createdAccount.getId(), true, false, AccountConfigType.TOOL_TIPS, TOOL_TIPS_VALUE);
    	accountConfigService.create(accountConfig);
    	accountConfig = new AccountConfig(createdAccount.getId(), false, true, AccountConfigType.EQUIPMENT_COMPARE, EQUIPMENT_COMPARE_VALUE);
    	accountConfigService.create(accountConfig);
    	accountConfig = new AccountConfig(createdAccount.getId(), false, true, AccountConfigType.TOOL_TIPS, TOOL_TIPS_VALUE);
    	accountConfigService.create(accountConfig);
    	accountConfig = new AccountConfig(createdAccount.getId(), true, true, AccountConfigType.EQUIPMENT_COMPARE, EQUIPMENT_COMPARE_VALUE);
    	accountConfigService.create(accountConfig);
    	accountConfig = new AccountConfig(createdAccount.getId(), true, true, AccountConfigType.TOOL_TIPS, TOOL_TIPS_VALUE);
    	accountConfigService.create(accountConfig);

    	AccountCurrency accountCurrency = new AccountCurrency(createdAccount.getId(), false, false, STARTING_CURRENCY);
    	accountCurrencyService.create(accountCurrency, account.getUsername());
    	accountCurrency = new AccountCurrency(createdAccount.getId(), true, false, STARTING_CURRENCY);
    	accountCurrencyService.create(accountCurrency, account.getUsername());
    	accountCurrency = new AccountCurrency(createdAccount.getId(), false, true, STARTING_CURRENCY);
    	accountCurrencyService.create(accountCurrency, account.getUsername());
    	accountCurrency = new AccountCurrency(createdAccount.getId(), true, true, STARTING_CURRENCY);
    	accountCurrencyService.create(accountCurrency, account.getUsername());

    	Inventory inventory = new Inventory(createdAccount, false, false);
    	inventoryService.create(inventory);
    	inventory = new Inventory(createdAccount, true, false);
    	inventoryService.create(inventory);
    	inventory = new Inventory(createdAccount, false, true);
    	inventoryService.create(inventory);
    	inventory = new Inventory(createdAccount, true, true);
    	inventoryService.create(inventory);
    	
    	return createdAccount;
    }

	@Override
    @Transactional
    public Account findById(long id) {
        Account account = accountRepo.findOne(id);
        loadAccountLinkedTables(account);
        return getRoles(account);
    }

	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=AccountNotFound.class)
    public Account delete(long id) throws AccountNotFound {
        Account deletedAccount = accountRepo.findOne(id);
         
        if (deletedAccount == null)
        {
            throw new AccountNotFound(id);
        }
         
        accountRepo.delete(deletedAccount);
        return deletedAccount;
    }
 
    @Override
    @Transactional
    public List<Account> findAll() {
    	List<Account> accounts = accountRepo.findAll();
    	for(int index=0; index < accounts.size(); index++) {
    		Account thisAccount = accounts.get(index);
    		loadAccountLinkedTables(thisAccount);
        	accounts.set(index, 
        			getRoles(thisAccount)
        		);
        }
        return accounts;
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={AccountNotFound.class})
    public Account update(Account account, boolean login) throws AccountNotFound {
        Account updatedAccount = accountRepo.findOne(account.getId());
         
        if (updatedAccount == null) {
            throw new AccountNotFound(account.getId());
        }
         
        updatedAccount.setDisplayName(account.getDisplayName());
        if(account.getHashedPassword() != null) {
        	updatedAccount.setHashedPassword(account.getHashedPassword());
        }
        updatedAccount.setUsername(account.getUsername());
        
        Set<AccountRole> roles = new HashSet<AccountRole>(); 
        for(AccountPrivilege priv:AccountPrivilege.values()) {
        	if(account.hasRole(priv)) {
        		roles.add(insertOrFindAccountRole(account, priv));
        	} else {
        		deleteIfExist(account, priv);
        	}
        }
        
		updatedAccount.setRoles(roles);
        updatedAccount.setPreviousLogin(account.getPreviousLogin());
        updatedAccount.setLastLogin(account.getLastLogin());
        updatedAccount.setLastProcessedMap(account.getLastProcessedMap());
        timeoutProtectionLastProcessedMap(account);		
        updatedAccount.setLoginTokens(account.getLoginTokens());
        updatedAccount.setOnHoliday(account.isOnHoliday());
        updatedAccount.setTouchScreen(account.isTouchScreen());
        updatedAccount.setDebugMode(account.isDebugMode());
        updatedAccount.setLevel(account.getLevel());
        if(login) {
        	updatedAccount.createApiKey();
            updatedAccount.setSocialUserId(account.getSocialUserId());
        }
        updatedAccount.setResetPasswordKey(account.getResetPasswordKey());
        updatedAccount.setLastResetDateTime(false, false, account.getLastResetDateTime(false, false));
        updatedAccount.setLastResetDateTime(true, false, account.getLastResetDateTime(true, false));
        updatedAccount.setLastResetDateTime(false, true, account.getLastResetDateTime(false, true));
        updatedAccount.setLastResetDateTime(true, true, account.getLastResetDateTime(true, true));
        
        updatedAccount = accountRepo.save(updatedAccount);
        return updatedAccount;
    }

    /**
     * Delete an AccountRole record if it exists for this account
     * 
     * @param accountId
     * @param priv
     * @throws AccountRoleNotFound 
     */
	@Transactional
	private void deleteIfExist(Account account, AccountPrivilege priv) {
		try {
			accountRoleService.delete(account, priv);
		} catch (AccountRoleNotFound e) {
			// Ignore
		}
	}

	/**
	 * Insert an AccountRole record if it doesn't exists for this account, or find it if it does
	 * @param accountId
	 * @param priv
	 * @return	The role (either existing or new)
	 */
	@Transactional
	private AccountRole insertOrFindAccountRole(Account account, AccountPrivilege priv) {
		List<AccountRole> roles = accountRoleService.findAllByAccountAndPriv(account, priv);
		AccountRole role;
		if(roles.isEmpty()) {
			role = accountRoleService.create(account, priv);
		} else {
			role = roles.get(0);
		}
		return role;
	}

	@Override
	@Transactional
	public Account findByUsername(String username) {
		Account account = accountRepo.findByUsername(username);
		loadAccountLinkedTables(account);
        return getRoles(account);
	}
	
	@Override
	@Transactional
	public Account findBySocialUserId(String socialUserId) {
		Account account = accountRepo.findBySocialUserId(socialUserId);
		loadAccountLinkedTables(account);
        return getRoles(account);
	}
	
	@Override
	@Transactional
	public Account findByPrinciple(Principal principle) {
		if(principle == null) return null;
		String username = principle.getName();
		if(username == null || username.isEmpty()) return null;
		Account account = findByUsername(username);
		loadAccountLinkedTables(account);
		return account;
	}
	
	@Override
	@Transactional
	public Account findByApiKey(String accountApiKey) {
		if(accountApiKey == null) return null;
		Account account = accountRepo.findByApiKey(accountApiKey);
		loadAccountLinkedTables(account);
        return getRoles(account);
	}

	@Override
	@Transactional
	public List<Account> findAllByDisplayName(String displayName) {
		List<Account> accounts = accountRepo.findAllByDisplayName(displayName);
		for(int index=0; index < accounts.size(); index++) {
			Account thisAccount = accounts.get(index);
			loadAccountLinkedTables(thisAccount);
        	accounts.set(index, getRoles(thisAccount)
        		);
        }
		return accounts;
	}
 
	/**
	 * Populate the roles for the account
	 * 
	 * @param account
	 * @return
	 */
	@Transactional
    private Account getRoles(Account account) {
    	if(account == null) return null;
    	Set<AccountRole> roles = accountRoleService.findAllByAccount(account);
		account.setRoles(roles);
		logger.debug("Found "+(roles==null?"NULL":roles.size())+" roles for account id "+account.getId());
		return account;
	}

	@Override
	@Transactional
	public void processAccount(Account account) {
		logger.debug("Processing Account "+account.getId());
		
		DailyProcessorTask dailyProcessorTask = 
				new DailyProcessorTask(this, accountMessageRepo, 
						accountCurrencyService, characterService, hiscoreService, boostItemService, equipmentService, 
						servletContext, account);
		dailyProcessorTask.run();
		
		ClearExpiredDungeonsTask clearExpiredDungeonsTask = new ClearExpiredDungeonsTask(this, dungeonService, account);
		clearExpiredDungeonsTask.run();
		
		DungeonProcessorTask dungeonProcessorTask = 
				new DungeonProcessorTask(this, dungeonService, equipmentService, boostItemService, accountBoostService,
						characterEquipmentService, account);
		dungeonProcessorTask.run();
		
		CreateDungeonsTask createDungeonsTask = 
				new CreateDungeonsTask(this, dungeonService, equipmentService, accountBoostService, account);
		createDungeonsTask.run();
		
		logger.debug("Done processing Account "+account.getId());
	}
	
	@Override
	@Transactional
	public void createDungeons(Account account) {
		CreateDungeonsTask task = new CreateDungeonsTask(this, 
					dungeonService, equipmentService, accountBoostService,
					account);
		taskExecuter.execute(task);
	}

	@Override
	@Transactional
	public void processDungeons(Account account, Date scheduleDate) {
		long delay = 0;
		if(scheduleDate != null) {
			// Set the delay, if the date is in the past just run it now anyway
			delay = System.currentTimeMillis() - scheduleDate.getTime();
			if(delay < 0) {
				delay = 0;
			}
		}
		CreateDungeonsTask task = new CreateDungeonsTask(this, 
				dungeonService, equipmentService, accountBoostService, 
				account);
		scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);	
	}

	@Override
	@Transactional
	public boolean accountOwnsEquipment(Account account, Equipment equipment) {
		switch(equipment.getEquipmentLocation()) {
		case NONE: return false;
		case CHARACTER:
			Character character = characterService.findById(equipment.getEquipmentLocationId());
			if(character == null) {
				logger.error("Can't find character for id {}", equipment.getEquipmentLocationId());
				return false;
			}
			if(!character.getAccount().equals(account)) {
				logger.debug("Character {} not on account {}", character, account);
				return false;
			}
			List<CharSlot> validSlots = equipment.getEquipmentType().getValidSlots();
			Iterator<CharSlot> charSlotIter = validSlots.iterator();
			while(charSlotIter.hasNext()) {
				CharSlot thisSlot = charSlotIter.next();
				Equipment checkEquipment = characterEquipmentService.findEquipmentForCharacterAndCharSlot(character, thisSlot);
				if(checkEquipment != null && checkEquipment.equals(equipment)) {
					return true;
				}
			}
			break;
		case DUNGEON:
			Dungeon dungeon = dungeonService.findById(equipment.getEquipmentLocationId());
			if(dungeon == null) {
				logger.error("Can't find dungeon for id {}", equipment.getEquipmentLocationId());
				return false;
			}
			if(!dungeon.getAccount().equals(account)) {
				logger.debug("Dungeon {} not on account {}", dungeon, account);
				return false;
			}
			Map<Equipment, Boolean> rewards = dungeon.getItemRewards();
			for(Equipment checkEquipment:rewards.keySet()) {
				if(checkEquipment != null && checkEquipment.equals(equipment)) {
					return true;
				}
			}
			break;
		case INVENTORY:
			Inventory inventory = inventoryService.findByAccount(account, equipment.isHardcore(), equipment.isIronborn());
			StashSlotItemSuper checkStashSlot = inventory.getInventorySlots().get((int)equipment.getEquipmentLocationId());
			if(checkStashSlot != null) {
				if(checkStashSlot.equals(equipment)) {
					return true;
				} else {
					if(checkStashSlot instanceof Equipment) {
						logger.error("Item in inventory slot {}, but equipment also thinks it is there {}", equipment.toStringNoAttributes(), ((Equipment)checkStashSlot).toStringNoAttributes());
					} else if(checkStashSlot instanceof BoostItem) {
						logger.error("Item in inventory slot {}, but boost item also thinks it is there {}", equipment.toStringNoAttributes(), ((BoostItem)checkStashSlot).toString());
					} else {
						logger.error("Item in inventory slot {}, but **Stash Slot** Item also thinks it is there {}", equipment.toStringNoAttributes(), checkStashSlot.toString());
					}
					return false;
				}
			}
			break;
		case MESSAGE:
			AccountMessage message = accountMessageRepo.findOne(equipment.getEquipmentLocationId());
			populateMessageItem(message);
			return (message != null && message.getAccountId() == account.getId());
		}
		
		// Not found where the equipment reported it to be
		return false;
	}
	
	public void populateMessageItem(AccountMessage message) {
		if(message.getAttachedItemId() > 0) {
			if(message.getAttachedItemType() == TYPE.EQUIPMENT) {
				message.setDisplayItem(equipmentService.findById(message.getAttachedItemId()));
			} else if(message.getAttachedItemType() == TYPE.BOOST_ITEM) {
				message.setDisplayItem(boostItemService.findById(message.getAttachedItemId()));
			} else {
				logger.error("Unknown message item type: "+message.getAttachedItemType()+" for ID "+message.getAttachedItemId());
			}
		}
	}

	@Override
	@Transactional
	public boolean accountOwnsBoostItem(Account account, BoostItem boostItem) {
		if(boostItem.getDungeonId() > 0) {
			Dungeon dungeon = dungeonService.findById(boostItem.getDungeonId());
			if(dungeon == null) {
				logger.error("Can't find dungeon for id {}", boostItem.getDungeonId());
				return false;
			}
			if(!dungeon.getAccount().equals(account)) {
				logger.debug("Dungeon {} not on account {}", dungeon, account);
				return false;
			}
			List<BoostItem> rewards = dungeon.getBoostItemRewards();
			for(BoostItem checkBoostItem:rewards) {
				if(checkBoostItem != null && checkBoostItem.equals(boostItem)) {
					return true;
				}
			}
		} else if(boostItem.getStashSlotId() >= 0) {
			Inventory inventory = inventoryService.findByAccount(account, boostItem.isHardcore(), boostItem.isIronborn());
			StashSlotItemSuper checkStashSlot = inventory.getInventorySlots().get((int)boostItem.getStashSlotId());
			if(checkStashSlot != null) {
				if(checkStashSlot.equals(boostItem)) {
					return true;
				} else {
					if(checkStashSlot instanceof Equipment) {
						logger.error("Item in inventory slot {}, but equipment also thinks it is there {}", boostItem.toString(), ((Equipment)checkStashSlot).toStringNoAttributes());
					} else if(checkStashSlot instanceof BoostItem) {
						logger.error("Item in inventory slot {}, but boost item also thinks it is there {}", boostItem.toString(), ((BoostItem)checkStashSlot).toString());
					} else {
						logger.error("Item in inventory slot {}, but **Stash Slot** Item also thinks it is there {}", boostItem.toString(), checkStashSlot.toString());
					}
					return false;
				}
			}
		} else if(boostItem.getMessageId() >= 0) {
			AccountMessage message = accountMessageRepo.findOne(boostItem.getMessageId());
			populateMessageItem(message);
			return (message != null && message.getAccountId() == account.getId());
		}
		// Not found where the equipment reported it to be
		return false;
	}

	@Override
	public int getMaxCharacterLevel(Account account, boolean hardcore, boolean ironborn) {
		List<Character> characters = characterService.findAllByAccountOrderByLevel(account, hardcore, ironborn);
		int maxLevel = 0;
		for(Character character:characters) {
			if(character.getLevel() > maxLevel) maxLevel = character.getLevel();
		}
		return maxLevel;
	}

	@Override
	public int getNumberOfCharactersAroundLevel(Account account, boolean hardcore, boolean ironborn, int level) {
		List<Character> characters = characterService.findAllByAccountOrderByLevel(account, hardcore, ironborn);
		int count = 0;
		int allowedRange = level / 10;
		for(Character character:characters) {
			if(character.getLevel() > level+allowedRange) continue;
			if(character.getLevel() < level-allowedRange) continue;
			count++;
		}
		return count;
	}

	/**
	 * Get the last accounts to login
	 */
	@Override
	@Transactional
	public List<Account> findLastLogins() {
		Pageable pagable = new PageRequest(0, 10);
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.WEEK_OF_YEAR, -1);
		Date lastWeek = cal.getTime();
		List<Account> accounts = accountRepo.findAllByLastLoginGreaterThanOrderByLastLogin(lastWeek, pagable);
		for(int index=0; index < accounts.size(); index++) {
    		Account thisAccount = accounts.get(index);
    		loadAccountLinkedTables(thisAccount);
        	accounts.set(index, 
        			getRoles(thisAccount)
        		);
        }
		return accounts;
	}

	/**
	 * Check if the task is already running, and if not set it as running
	 * 
	 * @param task
	 * @return already processing
	 * @throws AccountNotFound 
	 */
	@Transactional
	public boolean setProcessing(Account account, AccountTask task) throws AccountNotFound {
		// Check processing this task
		Date thisLastProcessed = account.getLastProcessed(task);
		if(thisLastProcessed != null && thisLastProcessed.before(PROCESSING_CHECK)) {
			logger.trace("Task {} account {} was already running when lock requested", task.name(), account.getId());
			return false;
		} else {
			logger.trace("Task "+task.name()+" account {} was last processed {}", account.getId(), thisLastProcessed);
		}
		account.setLastProcessed(task, PROCESSING);
		update(account, false);
		return true;
	}

	/**
	 * Check if the task is already running, and if not set it as running
	 * 
	 * @param task
	 * @return
	 * @throws AccountNotFound 
	 */
	@Transactional
	public void setProcessed(Account account, AccountTask task) throws AccountNotFound {
		Date thisLastProcessed = account.getLastProcessed(task);
		if(thisLastProcessed != null && thisLastProcessed.before(PROCESSING_CHECK)) {
			logger.trace("Freeing lock on account {} task {}", account.getId(), task.name());
			account.setLastProcessed(task, new Date());
			update(account, false);
			return;
		}
	}

	/**
	 * For all locked processes, increase the time so it times out after about 24 updates
	 * @param account
	 */
	private void timeoutProtectionLastProcessedMap(Account account) {
		for(AccountTask task:AccountTask.values()) {
			Date thisLastProcessed = account.getLastProcessed(task);
			if(thisLastProcessed != null && thisLastProcessed.before(PROCESSING_CHECK)) {
				Date newLastProcessed = new Date(thisLastProcessed.getTime()+HOUR_IN_MILLIS);
				account.setLastProcessed(task, newLastProcessed);
			}
		}
	}


	@Override
	public AccountHistory getHistory(Account account, Locale locale) {
		Calendar cal = Calendar.getInstance();
		Date startDate = cal.getTime();
		cal.add(Calendar.MONTH, -1);
		Date endDate = cal.getTime();
		List<AccountCurrencyAudit> audits = accountCurrencyService.findAuditByAccountIdAndDateBetween(account.getId(), startDate, endDate);
		AccountHistory accountHistory = new AccountHistory(startDate, locale);
		for(AccountCurrencyAudit audit:audits) {
			accountHistory.add(audit);
		}
		return accountHistory;
	}
}
