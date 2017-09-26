package org.kerrin.dungeon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.exception.InventoryException;
import org.kerrin.dungeon.exception.InventoryNotFound;
import org.kerrin.dungeon.exception.MessageEquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.StashSlotItem;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.kerrin.dungeon.repository.AccountMessageRepo;
import org.kerrin.dungeon.repository.AccountRepo;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.BoostItemService;
import org.kerrin.dungeon.service.EquipmentService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountMessageServiceImpl extends ServiceHelppers implements AccountMessageService {
	private static final Logger logger = LoggerFactory.getLogger(AccountMessageServiceImpl.class);

	private final AccountMessageRepo accountMessageRepo;
	
	private final AccountRepo accountRepo;
	
	private final EquipmentService equipmentService;
	
	private final BoostItemService boostItemService;
	
	@Autowired	
    public AccountMessageServiceImpl(AccountMessageRepo accountMessageRepo, AccountRepo accountRepo, 
    		EquipmentService equipmentService, BoostItemService boostItemService) {
		super();
		this.accountMessageRepo = accountMessageRepo;
		this.accountRepo = accountRepo;
		this.equipmentService = equipmentService;
		this.boostItemService = boostItemService;
	}

	@Override
    @Transactional
    public AccountMessage create(AccountMessage accountMessage) {
    	AccountMessage createdAccountMessage = accountMessage;
        return accountMessageRepo.save(createdAccountMessage);
    }

	@Override
    @Transactional
    public AccountMessage create(long accountId, String message, Boolean hardcore, Boolean ironborn) {
    	AccountMessage createdAccountMessage = new AccountMessage(accountId, message, null, null, hardcore, ironborn);
        return accountMessageRepo.save(createdAccountMessage);
    }

	@Override
    @Transactional
    public AccountMessage create(long accountId, String message, String link, String panel, Boolean hardcore, Boolean ironborn) {
    	AccountMessage createdAccountMessage = new AccountMessage(accountId, message, link, panel, hardcore, ironborn);
        return accountMessageRepo.save(createdAccountMessage);
    }

	@Override
    @Transactional
	public AccountMessage create(long accountId, String message, StashSlotItemSuper stashSlotItem) {
		AccountMessage createdAccountMessage = new AccountMessage(accountId, message, null, null, stashSlotItem);
        return accountMessageRepo.save(createdAccountMessage);
	}

	@Override
	@Transactional
	public void createAll(String message, Boolean hardcore, Boolean ironborn) {
		createAll(message, null, null, hardcore, ironborn);
	}

	@Override
	@Transactional
	public void createAll(String message, String link, String panel, Boolean hardcore, Boolean ironborn) {
		List<Account> accounts = accountRepo.findAll();
		for(Account account: accounts) {
			String thisMessage = replaceTokens(message, account);
			AccountMessage createdAccountMessage = new AccountMessage(account.getId(), thisMessage, link, panel, hardcore, ironborn);
	        accountMessageRepo.save(createdAccountMessage);
		}
	}

	@Override
    @Transactional()
	public AccountMessage findById(long id) {
		AccountMessage accountMessage = accountMessageRepo.findOne(id);
		populateDisplayItem(accountMessage);
        return accountMessage;
	}
         
    @Override
    @Transactional()
    public List<AccountMessage> findAllByAccount(Account account, boolean hardcore, boolean ironborn) {
    	List<AccountMessage> accountMessages = accountMessageRepo.findAllByAccountIdAndHardcoreIsNullAndIronbornIsNull(account.getId());
    	accountMessages.addAll(accountMessageRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
        
    	populateDisplayItems(accountMessages);
    	
        return accountMessages;
    }

	private void populateDisplayItems(List<AccountMessage> accountMessages) {
		for(AccountMessage message: accountMessages) {
			populateDisplayItem(message);
		}		
	}
	
	public void populateDisplayItem(AccountMessage message) {
		if(message.getAttachedItemId() > 0) {
			if(message.getAttachedItemType() == StashSlotItem.TYPE.EQUIPMENT) {
				message.setDisplayItem(equipmentService.findById(message.getAttachedItemId()));
			} else if(message.getAttachedItemType() == StashSlotItem.TYPE.BOOST_ITEM) {
				message.setDisplayItem(boostItemService.findById(message.getAttachedItemId()));
			} else {
				logger.error("Unknown message item type: "+message.getAttachedItemType()+" for ID "+message.getAttachedItemId());
			}
		}
	}

	@Override
    @Transactional
	public boolean deleteById(long accountMessageId) {
		AccountMessage deletedAccountMessage = accountMessageRepo.findOne(accountMessageId);
        
        if (deletedAccountMessage == null) {
            return false;
        }
        
        accountMessageRepo.delete(deletedAccountMessage);
        deletedAccountMessage.setId(-1);
        return true;
	}

	@Override
    @Transactional
	public List<StashSlotItem> deleteAllByAccount(Account account, boolean hardcore, boolean ironborn) 
			throws CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, EquipmentNotFound, 
			BoostItemNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound, MessageEquipmentNotFound, InventoryException {
		List<AccountMessage> accountMessages = accountMessageRepo.findAllByAccountIdAndHardcoreIsNullAndIronbornIsNull(account.getId());
    	accountMessages.addAll(accountMessageRepo.findAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn));
        
		List<StashSlotItem> deleteList = new ArrayList<StashSlotItem>();
		for(AccountMessage message:accountMessages) {
			deleteList.add(new StashSlotItem(message.getAttachedItemId(), message.getAttachedItemType()));
		}
		accountMessageRepo.deleteAllByAccountIdAndHardcoreIsNullAndIronbornIsNull(account.getId());
		accountMessageRepo.deleteAllByAccountIdAndHardcoreAndIronborn(account.getId(), hardcore, ironborn);
		return deleteList;
	}

	/**
	 * Find the attached item and delete it if there is one
	 * 
	 * @param message
	 * 
	 * @throws BoostItemNotFound 
	 * @throws InventoryException 
	 * @throws InventoryNotFound 
	 * @throws EquipmentNotFound 
	 * @throws AccountIdMismatch 
	 * @throws DungeonNotFound 
	 * @throws CharacterEquipmentNotFound 
	 * @throws CharacterSlotNotFound 
	 * @throws CharacterNotFound 
	 * @throws MessageEquipmentNotFound 
//	 */
//	@Override	
//	public List<StashSlotItem> removeItem(AccountMessage message)
//			throws CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, EquipmentNotFound, 
//				BoostItemNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound, MessageEquipmentNotFound, InventoryException {
//		Account account = accountRepo.getOne(message.getAccountId());
//		StashSlotItem.TYPE itemType = message.getAttachedItemType();
//		if(itemType == TYPE.EQUIPMENT) {
//			Equipment equipment = equipmentService.findById(message.getAttachedItemId());
//			equipmentService.delete(account, equipment);
//		} else if(itemType == TYPE.BOOST_ITEM) {
//			BoostItem boostItem = boostItemService.findById(message.getAttachedItemId());
//			boostItemService.delete(account, boostItem);
//		} else {
//			throw new MessageEquipmentNotFound(message);
//		}
//	}

	@Override
	public AccountMessage messageLevelUp(String contextRoot, long characterId, Account account, Character character) {
		StringBuilder message = new StringBuilder(character.getName());
		message.append(" leveled up to level ");
		message.append(character.getLevel());
		StringBuilder linkUrl = new StringBuilder(contextRoot);
		linkUrl.append("/play/character/");
		linkUrl.append(characterId);
		return create(account.getId(), message.toString(), linkUrl.toString(), "characterDetailsFrame", character.isHardcore(), character.isIronborn());
	}

	public String replaceTokens(String message, Account account) {
		String thisMessage = message;
		thisMessage = thisMessage.replace("<USERNAME>", account.getUsername());
		thisMessage = thisMessage.replace("<EMAIL>", account.getUsername());
		thisMessage = thisMessage.replace("<DISPLAYNAME>", account.getDisplayName());
		return thisMessage;
	}
}
