package com.cwcdev.pokemom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cwcdev.pokemom.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	
	Role findByAuthority(String authority);

}
