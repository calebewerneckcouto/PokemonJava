package com.cwcdev.pokemom.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cwcdev.pokemom.model.Pokemon;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long>, JpaSpecificationExecutor<Pokemon> {
    
    Optional<Pokemon> findByIdPokeApi(Integer idPokeApi);
    
    Optional<Pokemon> findByNameIgnoreCase(String name);
    
    Page<Pokemon> findByFavorite(Boolean favorite, Pageable pageable);
    
    @Query("SELECT p FROM Pokemon p WHERE LOWER(p.types) LIKE LOWER(CONCAT('%', :type, '%'))")
    Page<Pokemon> findByTypeContainingIgnoreCase(@Param("type") String type, Pageable pageable);
    
    boolean existsByIdPokeApi(Integer idPokeApi);
    
    boolean existsByNameIgnoreCase(String name);
}