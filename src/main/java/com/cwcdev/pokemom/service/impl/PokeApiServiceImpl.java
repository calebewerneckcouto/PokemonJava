// service/impl/PokeApiServiceImpl.java
package com.cwcdev.pokemom.service.impl;

import com.cwcdev.pokemom.model.dto.PokeApiResponse;
import com.cwcdev.pokemom.service.PokeApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PokeApiServiceImpl implements PokeApiService {
    
    private final RestTemplate restTemplate;
    
    @Value("${pokeapi.base-url}")
    private String pokeApiBaseUrl;
    
    public PokeApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    public PokeApiResponse getPokemonFromApi(String nameOrId) {
        try {
            String url = pokeApiBaseUrl + "/pokemon/" + nameOrId.toLowerCase();
            return restTemplate.getForObject(url, PokeApiResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new PokemonNotFoundException("Pokémon não encontrado na PokeAPI: " + nameOrId);
        } catch (Exception e) {
            throw new PokeApiIntegrationException("Erro ao consultar PokeAPI: " + e.getMessage());
        }
    }
}