package org.kerrin.dungeon.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser extends Account implements UserDetails
{
	private static final Logger logger = LoggerFactory.getLogger(SecurityUser.class);
	
	private static final long serialVersionUID = 1L;
	public SecurityUser(Account account) {
		if(account != null)
		{
			this.setId(account.getId());
			this.setUsername(account.getUsername());
			this.setHashedPassword(account.getHashedPassword());
			this.setDisplayName(account.getDisplayName());
			this.setLastLogin(account.getLastLogin());
			this.setRoles(account.getRoles());
			this.setTouchScreen(account.isTouchScreen());
			logger.debug("Set "+(account.getRoles()==null?"NULL":account.getRoles().size())+" roles");
		}		
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		logger.debug("getAuthorities");
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		Set<AccountRole> userRoles = this.getRoles();
		
		if(userRoles != null)
		{
			for (AccountRole role : userRoles) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRole().getName());
				logger.debug("Role: "+authority.toString());
				authorities.add(authority);
			}
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return super.getHashedPassword();
	}

	@Override
	public String getUsername() {
		return super.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}	
}
