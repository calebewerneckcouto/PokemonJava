// service/PokeApiService.java
package com.cwcdev.pokemom.service;

import com.cwcdev.pokemom.model.dto.PokeApiResponse;

public interface PokeApiService {
    PokeApiResponse getPokemonFromApi(String nameOrId);
}