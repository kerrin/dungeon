package org.kerrin.dungeon.repository;

import java.util.List;

import org.kerrin.dungeon.model.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterEquipmentRepo extends JpaRepository<CharacterEquipment, Long>{

	List<CharacterEquipment> findAllByCharacterId(long characterId);
}
