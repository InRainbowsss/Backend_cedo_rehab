package com.upc.cedorehab.repositories;

import com.upc.cedorehab.entities.InventarioItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioItemRepository extends JpaRepository<InventarioItem, Long> {
}
