package org.kerrin.dungeon.service.impl;

import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.exception.AccountNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.utils.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService, ApplicationListener<AuthenticationSuccessEvent>
{
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AchievementService achievementService;
	
	@Autowired
	private HiscoreService hiscoreService;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Account account = accountService.findByUsername(username);
		if(account == null){
			throw new UsernameNotFoundException("Username "+username+" not found");
		}
		
		logger.debug(account.toString());
		return new SecurityUser(account);
	}

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		String username = ((UserDetails) event.getAuthentication().
                getPrincipal()).getUsername();
		logger.debug("Succesful login of user: " + username);
		Account account = accountService.findByUsername(username);
		if(account == null){
			throw new UsernameNotFoundException("Username "+username+" not found");
		}
		account.setPreviousLogin(account.getLastLogin());
		account.setLastLogin(new Date());

		try {
			account = accountService.update(account, true);
		} catch (AccountNotFound e) {
			logger.error("Attempt to update last login for user "+username+" failed after succsful login: "+e.getMessage());
		}
		accountService.processAccount(account);
		List<Achievement> newAchievements = achievementService.login(null, account);
		int newPoints = 0;
		for(Achievement achievement:newAchievements) {
			newPoints += achievement.getPoints();
		}
		if(newPoints > 0) {
			hiscoreService.achievement(account, newPoints);
		}

	}
}