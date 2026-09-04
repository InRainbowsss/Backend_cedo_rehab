package com.upc.cedorehab.security.repositories;

import com.upc.cedorehab.security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
