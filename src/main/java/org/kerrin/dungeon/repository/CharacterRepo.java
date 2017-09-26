package org.kerrin.dungeon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;

public interface CharacterRepo extends JpaRepository<Character, Long>{

	List<Character> findAllByAccountOrderByLevelDesc(Account account);

	List<Character> findAllByAccountAndHardcoreAndIronbornOrderByLevelDesc(
			Account account, boolean hardcore, boolean ironborn);	

	List<Character> findAllByName(String name);

	List<Character> findAllByCharClass(CharClass charClassId);

	List<Character> findAllByLevelGreaterThanAndLevelLessThan(int greaterThanLevel, int lessThanLevel);

	List<Character> findAllByXpGreaterThanAndXpLessThan(long greaterThanXp, long lessThanXp);

	List<Character> findAllByPrestigeLevelGreaterThanAndPrestigeLevelLessThan(int greaterThanLevel, int lessThanLevel);

	List<Character> findAllByDungeon(Dungeon dungeon);

	List<Character> findAllByAccountAndHardcoreAndIronbornAndDeathClockIsNull(Account account, boolean hardcore, boolean ironborn);

	List<Character> findAllByAccountAndHardcoreAndIronbornAndDungeonIsNullAndDeathClockIsNullOrderByLevelDesc(
			Account account, boolean hardcore, boolean ironborn);
}
