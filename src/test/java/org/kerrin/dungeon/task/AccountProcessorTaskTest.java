package org.kerrin.dungeon.task;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.SuperTest;
import org.kerrin.dungeon.enums.AccountTask;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountProcessorTaskTest extends SuperTest {

	private static final long DAY_MILLIS = 24*60*60*1000;
	private static final int TEST_ACCOUNT_LEVEL1 = 4;
	private static final int TEST_ACCOUNT_LEVEL2 = 10;	
	
	@Test
	public void testRun() throws Exception {
		DailyProcessorTask dailyTask = new DailyProcessorTask(accountServiceMock, accountMessageRepoMock, 
				accountCurrencyServiceMock, characterServiceMock, hiscoreServiceMock, 
				boostItemServiceMock, equipmentServiceMock, servletContextMock, 
				testAccount);
		ClearExpiredDungeonsTask expiredTask = new ClearExpiredDungeonsTask(accountServiceMock, 
				dungeonServiceMock, testAccount);
		CreateDungeonsTask createTask = new CreateDungeonsTask(accountServiceMock, 
				dungeonServiceMock, equipmentServiceMock, accountBoostServiceMock, testAccount);
		DungeonProcessorTask processTask = new DungeonProcessorTask(accountServiceMock, 
				dungeonServiceMock, equipmentServiceMock, boostItemServiceMock, accountBoostServiceMock, 
				characterEquipmentServiceMock, testAccount);
		
		Calendar cal = Calendar.getInstance();
		Date nowYesterday = new Date(cal.getTimeInMillis()-DAY_MILLIS);
		testAccount.setPreviousLogin(nowYesterday);
		when(accountServiceMock.setProcessing(testAccount, AccountTask.CLEAR_EXPIRED_DUNGEONS)).thenReturn(true);
		when(accountServiceMock.setProcessing(testAccount, AccountTask.CREATE_DUNGEONS)).thenReturn(true);
		when(accountServiceMock.setProcessing(testAccount, AccountTask.DAILY_CHECKS)).thenReturn(true);
		when(accountServiceMock.setProcessing(testAccount, AccountTask.DUNGEON_PROCESSING)).thenReturn(true);
		when(accountServiceMock.getMaxCharacterLevel(eq(testAccount), anyBoolean(), anyBoolean())).thenReturn(TEST_ACCOUNT_LEVEL1);
		when(hiscoreServiceMock.totalLevel(eq(testAccount), anyBoolean(), anyBoolean())).thenReturn(testHiscore);
		
		dailyTask.run();
		expiredTask.run();
		createTask.run();
		processTask.run();
		
		verify(accountServiceMock, times(4)).setProcessing(eq(testAccount), any(AccountTask.class));
		verify(accountServiceMock, times(4)).setProcessed(eq(testAccount), any(AccountTask.class));
		verify(accountServiceMock, times(4)).getMaxCharacterLevel(eq(testAccount), anyBoolean(), anyBoolean());
		verify(accountServiceMock, times(1)).update(eq(testAccount), anyBoolean());
		verifyNoMoreInteractions(accountServiceMock);
		verify(accountCurrencyServiceMock, times(4)).adjustCurrency(eq(testAccount), anyBoolean(), anyBoolean(), eq(1L), 
				any(ModificationType.class), anyString());
		verify(accountCurrencyServiceMock, times(4)).getDailyTokenReference(eq(testAccount), anyBoolean(), anyBoolean());
		verify(accountCurrencyServiceMock, times(1)).haveAwardedDailyTokensToday(eq(testAccount), anyBoolean(), anyBoolean());
		verifyNoMoreInteractions(accountCurrencyServiceMock);
		verify(dungeonServiceMock, times(1)).findAllByAccountAndFinished(eq(testAccount));
		verify(dungeonServiceMock, times(1)).findAllByAccountAndExpired(eq(testAccount));
		verify(dungeonServiceMock, times(4)).findAllByAccount(eq(testAccount), anyBoolean(), anyBoolean());
		verify(dungeonServiceMock, times(TEST_ACCOUNT_LEVEL1*4)).generateMonsters(anyInt(), any(DungeonType.class), eq(0));
		verify(dungeonServiceMock, times(TEST_ACCOUNT_LEVEL1*4)).create(any(Dungeon.class));
		verify(dungeonServiceMock, times(TEST_ACCOUNT_LEVEL1*4)).update(any(Dungeon.class));
		verifyNoMoreInteractions(dungeonServiceMock);
		verify(equipmentServiceMock, atLeastOnce()).create(any(Equipment.class));
		verifyNoMoreInteractions(equipmentServiceMock);
		verifyNoMoreInteractions(characterEquipmentServiceMock);		
	}
	
	@Test
	public void testCreateDungeon() throws Exception {
		CreateDungeonsTask task = new CreateDungeonsTask(accountServiceMock, dungeonServiceMock, 
				equipmentServiceMock, accountBoostServiceMock, testAccount);
		
		Calendar cal = Calendar.getInstance();
		Date nowYesterday = new Date(cal.getTimeInMillis()-DAY_MILLIS);
		testAccount.setPreviousLogin(nowYesterday);		
		when(accountServiceMock.setProcessing(testAccount, AccountTask.CREATE_DUNGEONS)).thenReturn(true);
		when(accountServiceMock.getMaxCharacterLevel(eq(testAccount), anyBoolean(), anyBoolean())).thenReturn(TEST_ACCOUNT_LEVEL2);
		Map<Monster, MonsterType> monstersMap = new HashMap<Monster, MonsterType>();
		monstersMap.put(Monster.ACID_BALL, MonsterType.TRASH);
		when(dungeonServiceMock.generateMonsters(eq(1), any(DungeonType.class), eq(0))).thenReturn(monstersMap);
		
		task.run();
		verify(accountServiceMock, times(1)).setProcessing(eq(testAccount), eq(AccountTask.CREATE_DUNGEONS));
		verify(accountServiceMock, times(1)).setProcessed(eq(testAccount), any(AccountTask.class));
		verify(accountServiceMock, times(4)).getMaxCharacterLevel(eq(testAccount), anyBoolean(), anyBoolean());
		verify(accountServiceMock, times(1)).update(eq(testAccount), anyBoolean());
		verifyNoMoreInteractions(accountServiceMock);
		verify(dungeonServiceMock, times(4)).findAllByAccount(eq(testAccount), anyBoolean(), anyBoolean());
		verify(dungeonServiceMock, times(TEST_ACCOUNT_LEVEL2*4)).generateMonsters(anyInt(), any(DungeonType.class), eq(0));
		verify(dungeonServiceMock, times(TEST_ACCOUNT_LEVEL2*4)).create(any(Dungeon.class));
		verify(dungeonServiceMock, times(TEST_ACCOUNT_LEVEL2*4)).update(any(Dungeon.class));
		verifyNoMoreInteractions(dungeonServiceMock);
		verify(equipmentServiceMock, atLeastOnce()).create(any(Equipment.class));
		verifyNoMoreInteractions(equipmentServiceMock);
		verifyNoMoreInteractions(characterEquipmentServiceMock);		
	}
}
