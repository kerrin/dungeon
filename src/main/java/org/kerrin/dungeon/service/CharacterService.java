package org.kerrin.dungeon.service;

import java.util.List;

import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.exception.CharacterEquipmentNotFound;
import org.kerrin.dungeon.exception.CharacterNotFound;
import org.kerrin.dungeon.exception.CharacterSlotNotFound;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.springframework.stereotype.Service;

@Service
public interface CharacterService {
	public Character create(Character character);
    public Character delete(Character character) throws CharacterNotFound, CharacterEquipmentNotFound, CharacterSlotNotFound;
    public List<Character> findAll();
    public void update(Character character) throws CharacterNotFound;
    public Character findById(long id);
    public List<Character> findAllByAccountOrderByLevel(Account account);
	public List<Character> findAllByAccountOrderByLevel(Account account, boolean hardcore, boolean ironborn);
	public List<Character> findAllIdleByAccountOrderByLevel(Account account, boolean hardcore, boolean ironborn);
	public List<Character> findAllByName(String name);
	public List<Character> findAllByCharacterClass(CharClass charClassId);
	public List<Character> findAllByLevelGreaterThanAndLevelLessThan(int greaterThanLevel, int lessThanLevel);
	public List<Character> findAllByXpGreaterThanAndXpLessThan(long greaterThanXp, long lessThanXp);
	public List<Character> findAllByPrestigeLevelGreaterThanAndPrestigeLevelLessThan(int startLevel, int endLevel);
	public List<Character> findAllByDungeon(Dungeon dungeon);
	public boolean allAccountCharactersDead(Account account, boolean hardcore, boolean ironborn);
	public long getTotalLevel(Account account, boolean hardcore, boolean ironborn);
}
