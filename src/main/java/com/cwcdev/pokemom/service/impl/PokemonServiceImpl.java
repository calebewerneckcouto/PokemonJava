// service/impl/PokemonServiceImpl.java
package com.cwcdev.pokemom.service.impl;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cwcdev.pokemom.model.Pokemon;
import com.cwcdev.pokemom.model.dto.FavoriteRequest;
import com.cwcdev.pokemom.model.dto.PokeApiResponse;
import com.cwcdev.pokemom.model.dto.PokemonResponse;
import com.cwcdev.pokemom.repository.PokemonRepository;
import com.cwcdev.pokemom.service.PokeApiService;
import com.cwcdev.pokemom.service.PokemonService;

@Service
public class PokemonServiceImpl implements PokemonService {
    
    private final PokemonRepository pokemonRepository;
    private final PokeApiService pokeApiService;
    
    public PokemonServiceImpl(PokemonRepository pokemonRepository, 
                            PokeApiService pokeApiService) {
        this.pokemonRepository = pokemonRepository;
        this.pokeApiService = pokeApiService;
    }
    
    @Override
    @Transactional
    public PokemonResponse cachePokemon(String nameOrId) {
        // Get data from PokeAPI
        PokeApiResponse apiResponse = pokeApiService.getPokemonFromApi(nameOrId);
        
        // Check if already exists in cache
        Pokemon pokemon = pokemonRepository.findByIdPokeApi(apiResponse.getId())
                .orElse(new Pokemon());
        
        // Update or create entity
        updatePokemonFromApiResponse(pokemon, apiResponse);
        
        // Save to database
        Pokemon savedPokemon = pokemonRepository.save(pokemon);
        
        return convertToResponse(savedPokemon);
    }
    
    @Override
    public Page<PokemonResponse> getAllPokemon(Pageable pageable) {
        return pokemonRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    @Override
    public PokemonResponse getPokemonById(Long id) {
        Pokemon pokemon = pokemonRepository.findById(id)
                .orElseThrow(() -> new PokemonNotFoundException("Pokémon não encontrado com ID: " + id));
        return convertToResponse(pokemon);
    }
    
    @Override
    public PokemonResponse getPokemonByIdPokeApi(Integer idPokeApi) {
        Pokemon pokemon = pokemonRepository.findByIdPokeApi(idPokeApi)
                .orElseThrow(() -> new PokemonNotFoundException("Pokémon não encontrado com ID PokeAPI: " + idPokeApi));
        return convertToResponse(pokemon);
    }
    
    @Override
    public PokemonResponse getPokemonByName(String name) {
        Pokemon pokemon = pokemonRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new PokemonNotFoundException("Pokémon não encontrado com nome: " + name));
        return convertToResponse(pokemon);
    }
    
    @Override
    public Page<PokemonResponse> searchPokemonByType(String type, Pageable pageable) {
        return pokemonRepository.findByTypeContaining(type.toLowerCase(), pageable)
                .map(this::convertToResponse);
    }
    
    @Override
    @Transactional
    public PokemonResponse updateFavorite(Long id, FavoriteRequest request) {
        Pokemon pokemon = pokemonRepository.findById(id)
                .orElseThrow(() -> new PokemonNotFoundException("Pokémon não encontrado com ID: " + id));
        
        if (request.getFavorite() != null) {
            pokemon.setFavorite(request.getFavorite());
        }
        if (request.getNote() != null) {
            pokemon.setNote(request.getNote());
        }
        
        Pokemon updatedPokemon = pokemonRepository.save(pokemon);
        return convertToResponse(updatedPokemon);
    }
    
    @Override
    @Transactional
    public void deletePokemon(Long id) {
        if (!pokemonRepository.existsById(id)) {
            throw new PokemonNotFoundException("Pokémon não encontrado com ID: " + id);
        }
        pokemonRepository.deleteById(id);
    }
    
    private void updatePokemonFromApiResponse(Pokemon pokemon, PokeApiResponse apiResponse) {
        pokemon.setIdPokeApi(apiResponse.getId());
        pokemon.setName(apiResponse.getName());
        pokemon.setHeight(apiResponse.getHeight());
        pokemon.setWeight(apiResponse.getWeight());
        
        // Get first ability
        if (apiResponse.getAbilities() != null && !apiResponse.getAbilities().isEmpty()) {
            String firstAbility = apiResponse.getAbilities().get(0).getAbility().getName();
            pokemon.setFirstAbility(firstAbility);
        }
        
        // Convert types to CSV string
        if (apiResponse.getTypes() != null && !apiResponse.getTypes().isEmpty()) {
            String types = apiResponse.getTypes().stream()
                    .map(typeWrapper -> typeWrapper.getType().getName())
                    .collect(Collectors.joining(","));
            pokemon.setTypes(types);
        }
    }
    
    private PokemonResponse convertToResponse(Pokemon pokemon) {
        PokemonResponse response = new PokemonResponse();
        response.setId(pokemon.getId());
        response.setIdPokeApi(pokemon.getIdPokeApi());
        response.setName(pokemon.getName());
        response.setHeight(pokemon.getHeight());
        response.setWeight(pokemon.getWeight());
        response.setFirstAbility(pokemon.getFirstAbility());
        response.setTypes(pokemon.getTypes());
        response.setCachedAt(pokemon.getCachedAt());
        response.setFavorite(pokemon.getFavorite());
        response.setNote(pokemon.getNote());
        return response;
    }
}