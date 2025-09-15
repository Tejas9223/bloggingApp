package com.codewithdurgesh.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithdurgesh.entities.Role;
import com.codewithdurgesh.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> { 
	
	Optional<User> findByEmail(String email);
	 boolean existsByRolesContaining(Role role); 
}
