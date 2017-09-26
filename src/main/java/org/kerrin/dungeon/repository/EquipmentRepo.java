package org.kerrin.dungeon.repository;

import java.util.List;

import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepo extends JpaRepository<Equipment, Long>{

	List<Equipment> findAllByQuality(EquipmentQuality fromId);
	
	List<Equipment> findAllByLevelGreaterThanAndLevelLessThan(int greaterThanLevel, int lessThanLevel);
	
}
