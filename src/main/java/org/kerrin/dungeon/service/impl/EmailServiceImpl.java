package org.kerrin.dungeon.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {
	private Environment env;
	private JavaMailSender mailSender;
	
	@Autowired
	public EmailServiceImpl(Environment env, JavaMailSender mailSender) {
		super();
		this.env = env;
		this.mailSender = mailSender;
	}

	@Override
	public boolean sendPlainTextEmail(String emailAddress, String subject, String body) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8"); 
		
		try {
			// Prepare message using a Spring helper 
	        message.setSubject(subject); 
	        message.setFrom(env.getProperty("email.from.address")); 
	        message.setTo(emailAddress); 
	 
	        message.setText(body, false /* isHtml */); 
	 
	        // Send email 
	        this.mailSender.send(mimeMessage); 
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public void sendNewAccountEmail(HttpServletRequest request, String hostUrl, Account account, String password, boolean generatedPassword) {
		StringBuilder emailMessage = new StringBuilder();
		emailMessage.append("Dear ");
		emailMessage.append(account.getDisplayName());
		emailMessage.append(",\n\nThank you for registering to play Dungeon (");
		StringBuilder playUrl = new StringBuilder();
		playUrl.append(hostUrl);
		playUrl.append(request.getContextPath());
		playUrl.append("/");
		emailMessage.append(playUrl.toString());
		emailMessage.append(")\n\n");
		emailMessage.append("Your login details are:.\n");
		emailMessage.append("\tUsername: ");
		emailMessage.append(account.getUsername());
		emailMessage.append("\n\tPassword: ");
		emailMessage.append(password);
		if(generatedPassword) {
			emailMessage.append(" Note: If a character in the password could be a number, it is.");
		}
		emailMessage.append("\n\nThanks,\nThe Dungeon Game");
		sendPlainTextEmail(account.getUsername(), "Dungeon Account Registration", emailMessage.toString());
	}
}
