package org.kerrin.dungeon.service;

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
import org.springframework.stereotype.Service;

@Service
public interface AccountMessageService {
	public AccountMessage create(AccountMessage accountMessage);
	public AccountMessage create(long accountId, String message, Boolean hardcore, Boolean ironborn);
	public AccountMessage create(long accountId, String message, String link, String panel, Boolean hardcore, Boolean ironborn);
	public AccountMessage create(long accountId, String message, StashSlotItemSuper stashSlotItem);
	public void createAll(String message, Boolean hardcore, Boolean ironborn);
	public void createAll(String message, String link, String panel, Boolean hardcore, Boolean ironborn);
	public boolean deleteById(long accountMessageId);
    public AccountMessage findById(long id);
	public List<AccountMessage> findAllByAccount(Account account, boolean hardcore, boolean ironborn);
	public List<StashSlotItem> deleteAllByAccount(Account account, boolean hardcore, boolean ironborn)
			throws CharacterNotFound, CharacterSlotNotFound, CharacterEquipmentNotFound, EquipmentNotFound, 
			BoostItemNotFound, DungeonNotFound, AccountIdMismatch, InventoryNotFound, MessageEquipmentNotFound, InventoryException;	
	public AccountMessage messageLevelUp(String contextRoot, long characterId, Account account, Character character);
	public String replaceTokens(String message, Account account);
}
