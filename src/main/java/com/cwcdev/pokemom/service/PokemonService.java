// service/PokemonService.java
package com.cwcdev.pokemom.service;

import com.cwcdev.pokemom.model.dto.FavoriteRequest;
import com.cwcdev.pokemom.model.dto.PokemonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PokemonService {
    PokemonResponse cachePokemon(String nameOrId);
    Page<PokemonResponse> getAllPokemon(Pageable pageable);
    PokemonResponse getPokemonById(Long id);
    PokemonResponse getPokemonByIdPokeApi(Integer idPokeApi);
    PokemonResponse getPokemonByName(String name);
    Page<PokemonResponse> searchPokemonByType(String type, Pageable pageable);
    PokemonResponse updateFavorite(Long id, FavoriteRequest request);
    void deletePokemon(Long id);
}