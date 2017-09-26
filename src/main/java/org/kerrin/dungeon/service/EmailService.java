package org.kerrin.dungeon.service;

import javax.servlet.http.HttpServletRequest;

import org.kerrin.dungeon.model.Account;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
	public final static String ADMIN_EMAIL_ADDRESS = "kerrin.hardy+dungeon_anomoly@gmail.com";

	/**
	 * Send a plain text email
	 * 
	 * @param emailAddress
	 * @param subject
	 * @param body
	 * @return	Sent succesfully
	 */
	public boolean sendPlainTextEmail(String emailAddress, String subject, String body);
	
	/**
	 * Send the new account email
	 * @param request
	 * @param hostUrl
	 * @param account
	 * @param password
	 */
	public void sendNewAccountEmail(HttpServletRequest request, String hostUrl, Account account, String password, boolean generatedPassword);
}
