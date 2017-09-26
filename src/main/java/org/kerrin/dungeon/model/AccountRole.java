package org.kerrin.dungeon.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kerrin.dungeon.enums.AccountPrivilege;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Database record for a users account
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="account_role")
public class AccountRole {
	/** The account identifier */
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	/** The account */
	@ManyToOne
	@JoinColumn(name="account")
	@JsonIgnore
	private Account account;
	
	/** The role this user has */
	@Enumerated
	private AccountPrivilege priv;
	
	protected AccountRole() {}

	public AccountRole(Account account, AccountPrivilege priv) {
		super();
		this.account = account;
		this.priv = priv;
	}
	
	public AccountRole(long id, Account account, AccountPrivilege priv) {
		super();
		this.id = id;
		this.account = account;
		this.priv = priv;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccountId(Account account) {
		this.account = account;
	}

	public AccountPrivilege getRole() {
		return priv;
	}

	public void setRole(AccountPrivilege role) {
		this.priv = role;
	}
}
