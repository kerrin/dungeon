package org.kerrin.dungeon.repository;

import org.kerrin.dungeon.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<Inventory, Long>{
	Inventory findOneByAccountIdAndHardcoreAndIronborn(long accountId, boolean hardcore, boolean ironborn);
}
