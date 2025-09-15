package com.codewithdurgesh.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithdurgesh.entities.Role;

public interface RoleRepo  extends JpaRepository<Role,Integer>{
	
	Optional<Role> findByName(String name); // ✅ Needed to fetch ROLE_ADMIN or ROLE_USER by name
}
