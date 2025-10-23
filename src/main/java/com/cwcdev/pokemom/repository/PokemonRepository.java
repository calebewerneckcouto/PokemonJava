// repository/PokemonRepository.java
package com.cwcdev.pokemom.repository;

import com.cwcdev.pokemom.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    
    Optional<Pokemon> findByIdPokeApi(Integer idPokeApi);
    
    Optional<Pokemon> findByNameIgnoreCase(String name);
    
    @Query("SELECT p FROM Pokemon p WHERE p.types LIKE %:type%")
    Page<Pokemon> findByTypeContaining(@Param("type") String type, Pageable pageable);
    
    @Query("SELECT p FROM Pokemon p WHERE p.name LIKE %:name%")
    Page<Pokemon> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
    
    boolean existsByIdPokeApi(Integer idPokeApi);
    
    Page<Pokemon> findByFavorite(Boolean favorite, Pageable pageable);
}