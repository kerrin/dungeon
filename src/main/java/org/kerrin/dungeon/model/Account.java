package org.kerrin.dungeon.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.AccountTask;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.AccountForm;
import org.kerrin.dungeon.utils.ShaPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Database record for a users account
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="account")
public class Account {
	@Transient
	@JsonIgnore
	private static final Logger logger = LoggerFactory.getLogger(Account.class);	
			
	/** The account identifier */
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	/** The username */
	@Column(unique=true)
	private String username;
	
	/** The encrypted password */
	@JsonIgnore
	private String hashedPassword;
		
	/** The name to display to users */
	private String displayName;
	
	/** The date and time of the last successful login */
	private Date previousLogin;
	
	/** The date and time of the last successful login */
	private Date lastLogin;
	
	/** The user is on holiday until they disable this */
	private boolean onHoliday;

	/** Last time the account was reset */
	private Date lastAccountResetStandard;
	
	/** Last time the account was reset */
	private Date lastAccountResetHardcore;
	
	/** Last time the account was reset */
	private Date lastAccountResetIronborn;
	
	/** Last time the account was reset */
	private Date lastAccountResetExtreme;
	
	/** 
	 * The number of tokens awarded for login
	 * Increases on consecutive day login, decreases for each day missed down to 0
	 */
	private int loginTokens = 0;

	/**
	 * Is the user using touch screen at the moment
	 */
	private boolean isTouchScreen;
	
	/**
	 * Display debug on page
	 */
	private boolean debugMode = false;
	
	/**
	 * The string used to identify this account in API calls as authorised
	 */
	private String apiKey;
	
	/** The level of the highest character in any mode */
	private int level;
	
	/**
	 * The string used to identify this account in API calls as authorised
	 */
	@JsonIgnore
	private String socialUserId;
	
	/**
	 * The string used to identify this account in API calls as authorised
	 */
	@JsonIgnore
	private String resetPasswordKey = null;
	
	/** 
	 * The date and time of the last successful login
	 * This will be the date of the last process finishing, or null if the process is running or needs running
	 */
	@JsonIgnore
	@ElementCollection
	@MapKeyColumn(name="last_processed")
    @Column(name="processed_date")
	private Map<AccountTask, Date> lastProcessed;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="role_id")
	@JsonIgnore
	private Set<AccountRole> roles;
	
	protected Account() {}

	public Account(long id, String username, String hashedPassword, String displayName, Date previousLogin, 
			Date lastLogin, Set<AccountRole> roles, boolean isTouchScreen, boolean debugMode, int level) {
		super();
		this.id = id;
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.displayName = displayName;
		this.previousLogin = previousLogin;
		this.lastLogin = lastLogin;
		this.roles = roles;
		this.lastProcessed = new HashMap<AccountTask, Date>();
		this.isTouchScreen = isTouchScreen;
		this.debugMode = debugMode;
		this.lastAccountResetStandard = new Date();
		this.lastAccountResetHardcore = new Date();
		this.lastAccountResetIronborn = new Date();
		this.lastAccountResetExtreme = new Date();
		this.level = level;
	}

	public Account(AccountForm accountForm) {
		this(accountForm.getId(), accountForm.getUsername(), null, accountForm.getDisplayName(), null, null, null, false, accountForm.isDebugMode(), 1);
		try {
			if(accountForm.getPassword() != null && !accountForm.getPassword().isEmpty()) {
				setPassword(accountForm.getPassword());
			}
		} catch (Exception e) {
			logger.error("Password not set in Account(AccountForm)");
		}
	}

	/**
	 * Create the account from an account create form
	 * 
	 * @param accountCreateForm
	 */
	public Account(AccountCreateForm accountCreateForm) {
		this(-1, accountCreateForm.getUsername(), null, accountCreateForm.getDisplayName(), null, null, null, false, false, 1);
		try {
			setPassword(accountCreateForm.getPassword());
		} catch (Exception e) {
			logger.error("Password not set in Account(AccountCreateForm)");
		}
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
	 * Check if a plain text password is correct
	 * 
	 * @param checkPassword
	 * 
	 * @return Correct Password?
	 */
	public boolean passwordMatch(String checkPassword) {
		try {
			ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
			return passwordEncoder.matches(checkPassword, hashedPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Set the hashed password from the plain text password
	 * 
	 * @param password
	 * @throws Exception
	 */
	public void setPassword(String password) throws Exception {
		ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
		hashedPassword = passwordEncoder.encode(password);
	}
	
	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getPreviousLogin() {
		return previousLogin;
	}

	public void setPreviousLogin(Date previousLogin) {
		this.previousLogin = previousLogin;
	}

	public Set<AccountRole> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<AccountRole> roles) {
		if(roles == null) {
			logger.debug("Null Roles");
		} else {
			for(AccountRole role: roles) {
				logger.debug(role.getRole().getName());
			}
		}
		this.roles = roles;
	}

	public synchronized Date getLastProcessed(AccountTask task) {
		return lastProcessed.get(task);
	}

	public Map<AccountTask, Date> getLastProcessedMap() {
        return lastProcessed;
	}

	public synchronized void setLastProcessed(AccountTask task, Date lastProcessed) {
		this.lastProcessed.put(task, lastProcessed);
	}

	public void setLastProcessedMap(Map<AccountTask, Date> lastProcessedMap) {
		this.lastProcessed = lastProcessedMap;
	}

	/**
	 * Has the processing of the account finished
	 * @return
	 */
	public synchronized boolean processingDone() {
		for(AccountTask task:AccountTask.values()) {
			Date thisLastProcessed = lastProcessed.get(task);
			if(thisLastProcessed != null && thisLastProcessed.getTime() >= lastLogin.getTime()) {
				return false;
			}
		}
		return true;
	}	
	
	public boolean isOnHoliday() {
		return onHoliday;
	}

	public void setOnHoliday(boolean onHoliday) {
		this.onHoliday = onHoliday;
	}

	public int getLoginTokens() {
		return loginTokens;
	}

	public void setLoginTokens(int loginTokens) {
		this.loginTokens = loginTokens;
	}
	
	public void incrementLoginTokens() {
		this.loginTokens++;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void createApiKey() {
		this.apiKey = ""+(int)(Math.random() * Integer.MAX_VALUE)+"O"+id+"l"+(int)(Math.random() * Integer.MAX_VALUE);
	}
	
	public String getResetPasswordKey() {
		return resetPasswordKey;
	}
	
	public void setResetPasswordKey(String resetPasswordKey) {
		this.resetPasswordKey = resetPasswordKey;
	}

	public void generateNewResetPasswordKey() {
		this.resetPasswordKey = ""+(int)(Math.random() * Integer.MAX_VALUE)+"l"+id+"O"+(int)(Math.random() * Integer.MAX_VALUE);
	}

	/**
	 * Extracts the account id from the reset password key
	 * @param resetPasswordKey
	 * @return
	 */
	public static long getIdFromResetPasswordKey(String resetPasswordKey) {
		int beginIndex = resetPasswordKey.indexOf("l") + 1;
		if(beginIndex < 0) return -1;
		int endIndex = resetPasswordKey.indexOf("O");
		if(endIndex < 1) return -1;
		String accountIdStr = resetPasswordKey.substring(beginIndex, endIndex);
		return Long.parseLong(accountIdStr);
	}

	public String getSocialUserId() {
		return socialUserId;
	}

	public void setSocialUserId(String socialUserId) {
		this.socialUserId = socialUserId;
	}

	/**
	 * Increase the number of tokens the account has when logging in
	 * 
	 * @param number
	 */
	public void increaseLoginTokens(int number) {
		this.loginTokens += number;
	}
	
	/**
	 * Decrease the number of login tokens by the requested number
	 * Cannot cause the number to reduce below 1
	 * @param number
	 */
	public void decreaseLoginTokens(int number) {
		this.loginTokens -= number;
		if(this.loginTokens < 1) this.loginTokens = 1;
	}

	public boolean isTouchScreen() {
		return isTouchScreen;
	}

	public void setTouchScreen(boolean isTouchScreen) {
		this.isTouchScreen = isTouchScreen;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public Date getLastResetDateTime(boolean hardcore, boolean ironborn) {
		if(hardcore) {
			if(ironborn) {
				return lastAccountResetExtreme;
			} else {
				return lastAccountResetHardcore;
			}
		} else {
			if(ironborn) {
				return lastAccountResetIronborn;
			} else {
				return lastAccountResetStandard;				
			}
		}
	}
	
	public void setLastResetDateTime(boolean hardcore, boolean ironborn, Date lastAccountReset) {
		if(hardcore) {
			if(ironborn) {
				this.lastAccountResetExtreme = lastAccountReset;
			} else {
				this.lastAccountResetHardcore = lastAccountReset;
			}
		} else {
			if(ironborn) {
				this.lastAccountResetIronborn = lastAccountReset;
			} else {
				this.lastAccountResetStandard = lastAccountReset;
			}
		}
	}
	
	@Override
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean showNice) {
		StringBuffer sb = new StringBuffer("Account [id=");
		sb.append(id);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		
		sb.append("username=");
		sb.append(username);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		
		sb.append("displayName=");
		sb.append(displayName);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		
		sb.append("previousLogin=");
		sb.append(previousLogin);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		
		sb.append("lastLogin=");
		sb.append(lastLogin);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		
		sb.append("lastProcessed=[");
		if(showNice) {
			sb.append("\n");
		}
		for(AccountTask task:lastProcessed.keySet()) {
			sb.append(task.name());
			sb.append("=>");
			sb.append(lastProcessed.get(task));
			if(showNice) {
				sb.append("\n");
			} else {
				sb.append(", ");
			}
		}
		sb.append("]");
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		
		sb.append("onHoliday=");
		sb.append(onHoliday?"Yes":"No");
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		
		sb.append("touchScreen=");
		sb.append(isTouchScreen?"Yes":"No");
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		
		sb.append("loginTokens=");
		sb.append(loginTokens);
		if(showNice) {
			sb.append("\n");
		} else {
			sb.append(", ");
		}
		
		sb.append("roles=(");
		if(showNice) {
			sb.append("\n");
		}
		if(roles != null) {
			for(AccountRole role: roles) {
				sb.append(role.getRole().getName());
				if(showNice) {
					sb.append("\n");
				} else {
					sb.append(", ");
				}
			}
		}
		if(showNice) {
			sb.append("\n");
		}
		sb.append(")]");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object other) {
	    if (!(other instanceof Account)) {
	        return false;
	    }

	    Account that = (Account) other;

	    // Custom equality check here.
	    return this.id == that.id;
	}
	
	@Override
	public int hashCode() {
	    int hashCode = 1;

	    hashCode = hashCode * 37 + (int)this.id;

	    return hashCode;
	}

	public boolean hasRole(AccountPrivilege priv) {
		logger.debug("Checking for privilege "+priv.getName());
		if(roles == null) return false;
		for(AccountRole role: roles) {
			if(role.getRole() == priv) return true;
		}
		return false;
	}
}
