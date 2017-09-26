package org.kerrin.dungeon.exception;

import org.kerrin.dungeon.model.AccountMessage;

public class MessageEquipmentNotFound extends Exception {
	private static final long serialVersionUID = 1330999454222815955L;

	public MessageEquipmentNotFound(AccountMessage message) {
		super("Message ID: "+ message.getId() + ", Account ID: "+ message.getAccountId() + 
				", Attachment Type: " + message.getAttachedItemType() + 
				", Attachment ID: " + message.getAttachedItemId());
	}
}
