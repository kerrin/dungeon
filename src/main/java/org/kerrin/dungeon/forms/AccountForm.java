package org.kerrin.dungeon.forms;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.model.Account;

/**
 * Form data for an Account (Admin)
 * 
 * @author Kerrin
 *
 */
public class AccountForm {
	/** The Account identifier */
	@Min(-1)
	private long id = -1;

	/** The username */
	@NotNull
	@Size(min=2, max=64)
	private String username = "";
	
	/** The un-encrypted password, if changed */
	private String password;
		
	/** The name to display to users */
	@NotNull
	@Size(min=2, max=30)
	private String displayName = "";
	
	/** Number of tokens to add (negative means reduced) */
	private int modifyTokens = 0;
	
	/** Number of currency to add (negative means reduced) */
	private int modifyCurrencyStandard = 0;
	private int modifyCurrencyHardcore = 0;
	private int modifyCurrencyIronborn = 0;
	private int modifyCurrencyExtreme = 0;
	
	private boolean onHoliday = false;
	private boolean debugMode = false;
	
	private boolean privView = false;
	private boolean privModify = false;
	private boolean privDelete = false;


	public AccountForm() {}

	public AccountForm(Account account) {
		this(account.getId(), 
				account.getUsername(), 
				null/*Password not used for output*/, 
				account.getDisplayName(), 
				0/*Modify token value*/,
				0,0,0,0,/*Modify currency values*/
				account.isOnHoliday(),
				account.hasRole(AccountPrivilege.VIEW),
				account.hasRole(AccountPrivilege.MODIFY),
				account.hasRole(AccountPrivilege.DELETE),
				account.isDebugMode()
				);
	}
	
	public AccountForm(long id, String username, String password, String displayName, int modifyTokens, 
			int modifyCurrencyStandard, int modifyCurrencyHardcore, int modifyCurrencyIronborn, int modifyCurrencyExtreme,  
			boolean onHoliday, boolean privView, boolean privModify, boolean privDelete, boolean debugMode) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.displayName = displayName;
		this.modifyTokens = modifyTokens;
		this.modifyCurrencyStandard = modifyCurrencyStandard;
		this.modifyCurrencyHardcore = modifyCurrencyHardcore;
		this.modifyCurrencyIronborn = modifyCurrencyIronborn;
		this.modifyCurrencyExtreme = modifyCurrencyExtreme;
		this.onHoliday = onHoliday;
		this.privView = privView;
		this.privModify = privModify;
		this.privDelete = privDelete;
		this.debugMode = debugMode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Null if account from database
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public int getModifyTokens() {
		return modifyTokens;
	}

	public void setModifyTokens(int modifyTokens) {
		this.modifyTokens = modifyTokens;
	}
	
	public int getModifyCurrency(boolean hardcore, boolean ironborn) {
		if(hardcore) {
			if(ironborn) return modifyCurrencyExtreme;
			return modifyCurrencyHardcore;
		} else  {
			if(ironborn) return modifyCurrencyIronborn;
			return modifyCurrencyStandard;
		}
	}

	public int getModifyCurrencyStandard() {
		return modifyCurrencyStandard;
	}

	public void setModifyCurrencyStandard(int modifyCurrencyStandard) {
		this.modifyCurrencyStandard = modifyCurrencyStandard;
	}

	public int getModifyCurrencyHardcore() {
		return modifyCurrencyHardcore;
	}

	public void setModifyCurrencyHardcore(int modifyCurrencyHardcore) {
		this.modifyCurrencyHardcore = modifyCurrencyHardcore;
	}

	public int getModifyCurrencyIronborn() {
		return modifyCurrencyIronborn;
	}

	public void setModifyCurrencyIronborn(int modifyCurrencyIronborn) {
		this.modifyCurrencyIronborn = modifyCurrencyIronborn;
	}

	public int getModifyCurrencyExtreme() {
		return modifyCurrencyExtreme;
	}

	public void setModifyCurrencyExtreme(int modifyCurrencyExtreme) {
		this.modifyCurrencyExtreme = modifyCurrencyExtreme;
	}

	public boolean isOnHoliday() {
		return onHoliday;
	}

	public void setOnHoliday(boolean onHoliday) {
		this.onHoliday = onHoliday;
	}

	public boolean isPrivView() {
		return privView;
	}

	public void setPrivView(boolean privView) {
		this.privView = privView;
	}

	public boolean isPrivModify() {
		return privModify;
	}

	public void setPrivModify(boolean privModify) {
		this.privModify = privModify;
	}

	public boolean isPrivDelete() {
		return privDelete;
	}

	public void setPrivDelete(boolean privDelete) {
		this.privDelete = privDelete;
	}
	
	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	@Override
	public String toString() {
		return "AccountForm ["
					+ "id=" + id 
					+ ", username=" + username 
					+ ", password=NOT SHOWN" 
					+ ", displayName=" + displayName 
					+ ", privView=" + privView
					+ ", privModify=" + privModify
					+ ", privDelete=" + privDelete
					+ ", debugMode=" + debugMode
				+ "]";
	};
}
