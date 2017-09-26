package org.kerrin.dungeon.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.exception.AccountIdMismatch;
import org.kerrin.dungeon.exception.BoostItemNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.DifferentGameStates;
import org.kerrin.dungeon.exception.DungeonNotFound;
import org.kerrin.dungeon.exception.DungeonNotRunable;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.springframework.stereotype.Service;

@Service
public interface DungeonService {
	public Dungeon create(Dungeon dungeon);
    public Dungeon delete(Dungeon dungeon) throws DungeonNotFound, EquipmentNotFound;
    public List<Dungeon> findAll();
    public Dungeon update(Dungeon dungeon) 
    		throws DungeonNotFound, AccountIdMismatch, EquipmentNotFound, BoostItemNotFound;
    public Dungeon findById(long dungeonId);
    public Dungeon findByEquipment(Equipment equipment);
	public Dungeon findByBoostItem(BoostItem boostItem);
    
    public List<Dungeon> findAllByAccount(Account account);
	public List<Dungeon> findAllByAccount(Account account, boolean hardcore, boolean ironborn);
	public List<Dungeon> findAllByAccountAndFinished(Account account);
	public List<Dungeon> findAllByAccountAndFinished(Account account, boolean successfull);
	public List<Dungeon> findAllByAccountAndExpired(Account account);
	public List<Dungeon> findAllByLevelBetween(int minLevel, int maxLevel);
	public List<Dungeon> findAllByAccountAndActive(Account account);
	public List<Dungeon> findAllByDungeonType(DungeonType type);
	public List<Dungeon> findAllByXpRewardBetween(long greaterThanXpReward, long lessThanXpReward);
	public List<Dungeon> findAllByPartySizeBetween(int greaterThanPartySize, int lessThanPartySize);
	public List<Dungeon> findAllByStartedBetween(Date startedAfter, Date startedBefore);
	
	public void deleteAllByAccountAndHardcoreAndIronborn(Account account, boolean hardcore, boolean ironborn);
	
	/**
	 * Generate monsters for a dungeon
	 * 
	 * @param level							Level of dungeon
	 * @param dungeonType					Type of dungeon
	 * @param reduceMaxNumberOfMonsters		Reduce the maximum number of monsters by this number (never goes below minimum)
	 * @return
	 */
	public Map<Monster, MonsterType> generateMonsters(int level, DungeonType dungeonType, int reduceMaxNumberOfMonsters);
	/**
	 * Start the dungeon and run its events
	 * 
	 * @param dungeon
	 * @param characterIds
	 * @param levelAdjustment
	 * @param equipmentService
	 * @param characterEquipmentService
	 * @return
	 * @throws CharacterNotFound
	 * @throws AccountIdMismatch
	 * @throws DungeonNotFound
	 * @throws DungeonNotRunable
	 * @throws EquipmentNotFound
	 * @throws DifferentGameStates
	 */
	public Dungeon startDungeon(Dungeon dungeon, long[] characterIds, int levelAdjustment, 
			EquipmentService equipmentService, CharacterEquipmentService characterEquipmentService)
					throws CharacterNotFound, AccountIdMismatch, DungeonNotFound, DungeonNotRunable,
					EquipmentNotFound, BoostItemNotFound, DifferentGameStates;
}
