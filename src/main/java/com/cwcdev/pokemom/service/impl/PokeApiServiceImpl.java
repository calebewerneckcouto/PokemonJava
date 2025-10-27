package com.cwcdev.pokemom.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cwcdev.pokemom.model.dto.PokeApiResponse;
import com.cwcdev.pokemom.service.PokeApiService;

@Service
public class PokeApiServiceImpl implements PokeApiService {

    private static final Logger logger = LoggerFactory.getLogger(PokeApiServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${pokeapi.base-url:https://pokeapi.co/api/v2}")
    private String pokeApiBaseUrl;

    @Override
    @Cacheable(value = "pokeApiData", key = "#nameOrId.toLowerCase()", unless = "#result == null")
    public PokeApiResponse getPokemonFromApi(String nameOrId) {
        logger.info("Buscando Pokémon na PokeAPI: {}", nameOrId);
        
        String url = UriComponentsBuilder.fromHttpUrl(pokeApiBaseUrl)
                .path("/pokemon/{nameOrId}")
                .buildAndExpand(nameOrId.toLowerCase())
                .toUriString();

        try {
            PokeApiResponse response = restTemplate.getForObject(url, PokeApiResponse.class);
            if (response == null) {
                logger.warn("Resposta vazia da PokeAPI para: {}", nameOrId);
                throw new RuntimeException("Pokémon não encontrado na PokeAPI: " + nameOrId);
            }
            logger.info("Pokémon encontrado na PokeAPI: {} (ID: {})", response.getName(), response.getId());
            return response;
        } catch (Exception e) {
            logger.error("Erro ao buscar Pokémon na PokeAPI: {} - {}", nameOrId, e.getMessage());
            throw new RuntimeException("Não foi possível buscar o Pokémon: " + nameOrId, e);
        }
    }
}