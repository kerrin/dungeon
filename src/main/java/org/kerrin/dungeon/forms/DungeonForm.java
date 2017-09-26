package org.kerrin.dungeon.forms;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Min;

import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Dungeon;

/**
 * Form data for a Equipment
 * 
 * @author Kerrin
 *
 */
public class DungeonForm {
	/** Maximum number of monsters a dungeon could have */
	private static final int MAX_MONSTERS = 10;

	/** The Equipment identifier */
	@Min(-1)
	private long id = -1;

	@Min(1)
	private long accountId = -1;
	
	/** Is this record for hardcore characters */
	private boolean hardcore = false;
	
	/** Is this record for ironborn characters */
	private boolean ironborn = false;
	
	private DungeonType type = DungeonType.NONE;
	
	@Min(1)
	private int level;	
	
	@Min(1)
	private long xpReward;
	
	private Monster[] monsters;
	
	private MonsterType[] monsterTypes;
	
	private Date started;
	
	private int partySize;

	public DungeonForm() {
		super();
	}

	public DungeonForm(Dungeon dungeon) {
		super();
		
		this.id = dungeon.getId();
		this.accountId = dungeon.getAccount().getId();
		this.hardcore = dungeon.isHardcore();
		this.ironborn = dungeon.isIronborn();
		this.type = dungeon.getType();
		this.level = dungeon.getLevel();
		this.partySize = dungeon.getPartySize();
		this.started = dungeon.getStarted();
		this.xpReward = dungeon.getXpReward();
		this.monsters = getMonsters(dungeon.getMonsters());
		this.monsterTypes = getMonsterTypes(dungeon.getMonsters());
	}

	public DungeonForm(long id, long accountId, boolean hardcore, boolean ironborn, DungeonType type, int level, long xpReward, 
			Monster[] monsters, MonsterType[] monsterTypes,
			long length, Date started, int partySize) {
		this();
		
		this.id = id;
		this.accountId = accountId;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.type = type;
		this.level = level;
		this.partySize = partySize;
		this.started = started;
		this.xpReward = xpReward;
		this.monsters = monsters;
		this.monsterTypes = monsterTypes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	
	/**
	 * Get an account with JUST the account id set
	 * @return
	 */
	public Account getAccount() {
		return new Account(accountId,null,null,null,null,null,null, false, false, 1);
	}

	public boolean isHardcore() {
		return hardcore;
	}

	public void setHardcore(boolean hardcore) {
		this.hardcore = hardcore;
	}

	public boolean isIronborn() {
		return ironborn;
	}

	public void setIronborn(boolean ironborn) {
		this.ironborn = ironborn;
	}

	public DungeonType getType() {
		return type;
	}

	public void setType(DungeonType type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getXpReward() {
		return xpReward;
	}

	public void setXpReward(long xpReward) {
		this.xpReward = xpReward;
	}

	public Monster[] getMonsters() {
		return monsters;
	}

	public void setMonsters(Monster[] monsters) {
		this.monsters = monsters;
	}

	public MonsterType[] getMonsterTypes() {
		return monsterTypes;
	}

	public void setMonsterTypes(MonsterType[] monsterTypes) {
		this.monsterTypes = monsterTypes;
	}

	public int getPartySize() {
		return partySize;
	}

	public void setPartySize(int partySize) {
		this.partySize = partySize;
	}

	/**
	 * Get instances of the monsters
	 * NOTE: The instances returned are not database backed
	 * @return
	 */
	public Map<Monster,MonsterType> getMonstersMap() {
		Map<Monster,MonsterType> monstersMap = new HashMap<Monster,MonsterType>();
		for(int i=0; i < MAX_MONSTERS; i++) {
			if(monsters[i] != null && monsterTypes[i] != null) {				
				monstersMap.put(monsters[i], monsterTypes[i]);
			}
		}
		return monstersMap;
	}

	public void setMonsters(Map<Monster,MonsterType> monstersMap) {
		this.monsterTypes = new MonsterType[MAX_MONSTERS];
		this.monsters = new Monster[MAX_MONSTERS];
		int i=0;
		for(Monster thisMonster:monstersMap.keySet()) {
			this.monsters[i] = thisMonster;
			this.monsterTypes[i] = monstersMap.get(thisMonster);
			i++;
		}
	}

	public void setMonsterType0(MonsterType monsterType) {
		makeMonsterTypesArrayAtLeastSize(1);
		this.monsterTypes[0] = monsterType;
	}
	
	public MonsterType getMonsterType0() {
		return monsterTypes==null||monsterTypes.length<1||monsterTypes[0]==null?
				MonsterType.NONE:
				monsterTypes[0];
	}
	
	public void setMonsterType1(MonsterType monsterType) {
		makeMonsterTypesArrayAtLeastSize(2);
		this.monsterTypes[1] = monsterType;
	}

	public MonsterType getMonsterType1() {
		return monsterTypes==null||monsterTypes.length<2||monsterTypes[1]==null?MonsterType.NONE:monsterTypes[1];
	}
	
	public void setMonsterType2(MonsterType monsterType) {
		makeMonsterTypesArrayAtLeastSize(3);
		this.monsterTypes[2] = monsterType;
	}
	
	public MonsterType getMonsterType2() {
		return monsterTypes==null||monsterTypes.length<3||monsterTypes[2]==null?MonsterType.NONE:monsterTypes[2];
	}
	
	public void setMonsterType3(MonsterType monsterType) {
		makeMonsterTypesArrayAtLeastSize(4);
		this.monsterTypes[3] = monsterType;
	}
	
	public MonsterType getMonsterType3() {
		return monsterTypes==null||monsterTypes.length<4||monsterTypes[3]==null?MonsterType.NONE:monsterTypes[3];
	}
	
	public void setMonsterType4(MonsterType monsterType) {
		makeMonsterTypesArrayAtLeastSize(5);
		this.monsterTypes[4] = monsterType;
	}
	
	public MonsterType getMonsterType4() {
		return monsterTypes==null||monsterTypes.length<5||monsterTypes[4]==null?MonsterType.NONE:monsterTypes[4];
	}
	
	public void setMonsterType5(MonsterType attribute) {
		makeMonsterTypesArrayAtLeastSize(6);
		this.monsterTypes[5] = attribute;
	}
	
	public MonsterType getMonsterType5() {
		return monsterTypes==null||monsterTypes.length<6||monsterTypes[5]==null?MonsterType.NONE:monsterTypes[5];
	}
	
	public void setMonsterType6(MonsterType monsterType) {
		makeMonsterTypesArrayAtLeastSize(7);
		this.monsterTypes[6] = monsterType;
	}
	
	public MonsterType getMonsterType6() {
		return monsterTypes==null||monsterTypes.length<7||monsterTypes[6]==null?MonsterType.NONE:monsterTypes[6];
	}
	
	public void setMonsterType7(MonsterType monsterType) {
		makeMonsterTypesArrayAtLeastSize(8);
		this.monsterTypes[7] = monsterType;
	}
	
	public MonsterType getMonsterType7() {
		return monsterTypes==null||monsterTypes.length<8||monsterTypes[7]==null?MonsterType.NONE:monsterTypes[7];
	}
	
	public void setMonsterType8(MonsterType monsterType) {
		makeMonsterTypesArrayAtLeastSize(9);
		this.monsterTypes[8] = monsterType;
	}
	
	public MonsterType getMonsterType8() {
		return monsterTypes==null||monsterTypes.length<9||monsterTypes[8]==null?MonsterType.NONE:monsterTypes[8];
	}
	
	public void setMonsterType9(MonsterType monsterType) {
		makeMonsterTypesArrayAtLeastSize(10);
		this.monsterTypes[9] = monsterType;
	}
	
	public MonsterType getMonsterType9() {
		return monsterTypes==null||monsterTypes.length<10||monsterTypes[9]==null?MonsterType.NONE:monsterTypes[9];
	}
	
	public void setMonster0(Monster value) {
		makeMonstersArrayAtLeastSize(1);
		this.monsters[0] = value;
	}
	
	public Monster getMonster0() {
		return monsters==null||monsters.length<=0?null:monsters[0];
	}
	
	public void setMonster1(Monster value) {
		makeMonstersArrayAtLeastSize(2);
		this.monsters[1] = value;
	}
	
	public Monster getMonster1() {
		return monsters==null||monsters.length<=1?null:monsters[1];
	}
	
	public void setMonster2(Monster value) {
		makeMonstersArrayAtLeastSize(3);
		this.monsters[2] = value;
	}
	
	public Monster getMonster2() {
		return monsters==null||monsters.length<=2?null:monsters[2];
	}
	
	public void setMonster3(Monster value) {
		makeMonstersArrayAtLeastSize(4);
		this.monsters[3] = value;
	}
	
	public Monster getMonster3() {
		return monsters==null||monsters.length<=3?null:monsters[3];
	}
	
	public void setMonster4(Monster value) {
		makeMonstersArrayAtLeastSize(5);
		this.monsters[4] = value;
	}
	
	public Monster getMonster4() {
		return monsters==null||monsters.length<=4?null:monsters[4];
	}
	
	public void setMonster5(Monster value) {
		makeMonstersArrayAtLeastSize(6);
		this.monsters[5] = value;
	}
	
	public Monster getMonster5() {
		return monsters==null||monsters.length<=5?null:monsters[5];
	}
	
	public void setMonster6(Monster value) {
		makeMonstersArrayAtLeastSize(7);
		this.monsters[6] = value;
	}
	
	public Monster getMonster6() {
		return monsters==null||monsters.length<=6?null:monsters[6];
	}
	
	public void setMonster7(Monster value) {
		makeMonstersArrayAtLeastSize(8);
		this.monsters[7] = value;
	}
	
	public Monster getMonster7() {
		return monsters==null||monsters.length<=7?null:monsters[7];
	}
	
	public void setMonster8(Monster value) {
		makeMonstersArrayAtLeastSize(9);
		this.monsters[8] = value;
	}
	
	public Monster getMonster8() {
		return monsters==null||monsters.length<=8?null:monsters[8];
	}
	
	public void setMonster9(Monster value) {
		makeMonstersArrayAtLeastSize(10);
		this.monsters[9] = value;
	}
	
	public Monster getMonster9() {
		return monsters==null||monsters.length<=9?null:monsters[9];
	}
	
	/**
	 * Get the monsters that the list of monster instances are
	 * 
	 * @param monstersMap
	 * 
	 * @return
	 */
	private Monster[] getMonsters(Map<Monster,MonsterType> monstersMap) {		
		return monstersMap.keySet().toArray(new Monster[0]);
	}

	/**
	 * Get the monster types that the list of monster instances are
	 * 
	 * @param monstersMap
	 * 
	 * @return
	 */
	private MonsterType[] getMonsterTypes(Map<Monster,MonsterType> monstersMap) {
		MonsterType[] monsterTypeArray = new MonsterType[monstersMap.size()];
		int i=0;
		for(Monster monsterKey:monstersMap.keySet()) {
			monsterTypeArray[i] = monstersMap.get(monsterKey);
			i++;
		}
		return monsterTypeArray;
	}

	@Override
	public String toString() {
		return "DungeonForm [" +
				"id=" + id + ", " +
				"accountId=" + accountId + ", " + 
				"hardcore=" + (hardcore?"Yes":"No") + ", " + 
				"ironborn=" + (ironborn?"Yes":"No") + ", " + 
				"type=" + type + ", " +
				"level=" + level + ", " +
				"xpReward=" + xpReward + ", " +
				"monsters=" + Arrays.toString(monsters) + ", " +
				"monsterTypes=" + Arrays.toString(monsterTypes) + ", " +
				"started=" + started + ", " +
				"partySize=" + partySize + 
				"]";
	}

	private void makeMonsterTypesArrayAtLeastSize(int minSize) {
		if(monsterTypes == null) {
			monsterTypes = new MonsterType[minSize];
			return;
		}
		if(minSize <= monsterTypes.length) return;
		MonsterType[] tempMonsterTypes = new MonsterType[minSize];
		for(int i=0; i < monsterTypes.length; i++) {
			tempMonsterTypes[i] = monsterTypes[i];
		}
		this.monsterTypes = tempMonsterTypes ;
	}

	private void makeMonstersArrayAtLeastSize(int minSize) {
		if(monsters == null) {
			monsters = new Monster[minSize];
			return;
		}

		if(minSize <= monsters.length) return;
		Monster[] tempMonsters = new Monster[minSize];
		for(int i=0; i < monsters.length; i++) {
			tempMonsters[i] = monsters[i];
		}
		this.monsters = tempMonsters ;
	}
}
