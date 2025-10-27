package com.cwcdev.pokemom.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cwcdev.pokemom.model.Pokemon;
import com.cwcdev.pokemom.model.dto.FavoriteRequest;
import com.cwcdev.pokemom.model.dto.PokeApiResponse;
import com.cwcdev.pokemom.model.dto.PokemonResponse;
import com.cwcdev.pokemom.repository.PokemonRepository;
import com.cwcdev.pokemom.service.PokeApiService;
import com.cwcdev.pokemom.service.PokemonService;

@Service
public class PokemonServiceImpl implements PokemonService {

    private static final Logger logger = LoggerFactory.getLogger(PokemonServiceImpl.class);

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private PokeApiService pokeApiService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "allPokemon", allEntries = true),
            @CacheEvict(value = "pokemonSearch", allEntries = true),
            @CacheEvict(value = "favoritePokemon", allEntries = true)
        }
    )
    public PokemonResponse cachePokemon(String nameOrId) {
        logger.info("Cacheando Pokémon: {}", nameOrId);
        
        validateNameOrId(nameOrId);
        
        // Limpa e normaliza o parâmetro
        String cleanedNameOrId = cleanNameOrIdParameter(nameOrId);
        logger.info("Parâmetro normalizado: '{}' -> '{}'", nameOrId, cleanedNameOrId);
        
        // Verifica se já existe no cache local
        Optional<Pokemon> existingPokemon = findExistingPokemon(cleanedNameOrId);
        if (existingPokemon.isPresent()) {
            logger.info("Pokémon já está em cache: {}", cleanedNameOrId);
            return convertToResponse(existingPokemon.get());
        }

        // Busca na PokeAPI
        PokeApiResponse pokeApiData = pokeApiService.getPokemonFromApi(cleanedNameOrId);
        if (pokeApiData == null) {
            throw new RuntimeException("Pokémon não encontrado na PokeAPI: " + cleanedNameOrId);
        }

        // Verifica se já existe pelo ID da PokeAPI (caso de nome diferente)
        Optional<Pokemon> existingByPokeApiId = pokemonRepository.findByIdPokeApi(pokeApiData.getId());
        if (existingByPokeApiId.isPresent()) {
            logger.info("Pokémon já está em cache pelo ID PokeAPI: {}", pokeApiData.getId());
            return convertToResponse(existingByPokeApiId.get());
        }

        // Converte e salva
        Pokemon pokemon = convertPokeApiToEntity(pokeApiData);
        Pokemon savedPokemon = pokemonRepository.save(pokemon);
        
        logger.info("Pokémon cacheado com sucesso: {} (ID: {})", savedPokemon.getName(), savedPokemon.getId());
        return convertToResponse(savedPokemon);
    }

    @Override
    @Cacheable(value = "allPokemon", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    public Page<PokemonResponse> getAllPokemon(Pageable pageable) {
        logger.info("Buscando todos os Pokémon - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        return pokemonRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Cacheable(value = "pokemon", key = "#id", unless = "#result == null")
    public PokemonResponse getPokemonById(Long id) {
        logger.info("Buscando Pokémon por ID: {}", id);
        
        validateId(id);
        
        Pokemon pokemon = pokemonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pokémon não encontrado com ID: " + id));
        
        return convertToResponse(pokemon);
    }

    @Override
    @Cacheable(value = "pokemonByPokeApiId", key = "#idPokeApi", unless = "#result == null")
    public PokemonResponse getPokemonByIdPokeApi(Integer idPokeApi) {
        logger.info("Buscando Pokémon por ID PokeAPI: {}", idPokeApi);
        
        validatePokeApiId(idPokeApi);
        
        Pokemon pokemon = pokemonRepository.findByIdPokeApi(idPokeApi)
                .orElseThrow(() -> new RuntimeException("Pokémon não encontrado com ID PokeAPI: " + idPokeApi));
        
        return convertToResponse(pokemon);
    }

    @Override
    @Cacheable(value = "pokemonByName", key = "#name.toLowerCase()", unless = "#result == null")
    public PokemonResponse getPokemonByName(String name) {
        logger.info("Buscando Pokémon por nome: {}", name);
        
        validateName(name);
        
        Pokemon pokemon = pokemonRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Pokémon não encontrado com nome: " + name));
        
        return convertToResponse(pokemon);
    }

    @Override
    @Cacheable(value = "pokemonSearch", key = "#type.toLowerCase() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    public Page<PokemonResponse> searchPokemonByType(String type, Pageable pageable) {
        logger.info("Buscando Pokémon por tipo: {} - página: {}", type, pageable.getPageNumber());
        
        validateType(type);
        
        Specification<Pokemon> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("types")), "%" + type.toLowerCase() + "%");
        
        return pokemonRepository.findAll(spec, pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "pokemon", key = "#id"),
            @CacheEvict(value = "pokemonByPokeApiId", allEntries = true),
            @CacheEvict(value = "pokemonByName", allEntries = true),
            @CacheEvict(value = "allPokemon", allEntries = true),
            @CacheEvict(value = "pokemonSearch", allEntries = true),
            @CacheEvict(value = "favoritePokemon", allEntries = true)
        }
    )
    public PokemonResponse updateFavorite(Long id, FavoriteRequest request) {
        logger.info("Atualizando favorito para Pokémon {}: {}", id, request.getFavorite());
        
        validateId(id);
        validateFavoriteRequest(request);
        
        Pokemon pokemon = pokemonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pokémon não encontrado com ID: " + id));
        
        pokemon.setFavorite(request.getFavorite());
        if (request.getNote() != null) {
            pokemon.setNote(request.getNote());
        }
        
        // Atualiza timestamp de cache
        pokemon.setCachedAt(LocalDateTime.now());
        
        Pokemon updatedPokemon = pokemonRepository.save(pokemon);
        logger.info("Pokémon {} atualizado com sucesso - favorito: {}", id, request.getFavorite());
        
        return convertToResponse(updatedPokemon);
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "pokemon", key = "#id"),
            @CacheEvict(value = "pokemonByPokeApiId", allEntries = true),
            @CacheEvict(value = "pokemonByName", allEntries = true),
            @CacheEvict(value = "allPokemon", allEntries = true),
            @CacheEvict(value = "pokemonSearch", allEntries = true),
            @CacheEvict(value = "favoritePokemon", allEntries = true)
        }
    )
    public void deletePokemon(Long id) {
        logger.info("Deletando Pokémon com ID: {}", id);
        
        validateId(id);
        
        Pokemon pokemon = pokemonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pokémon não encontrado com ID: " + id));
        
        pokemonRepository.delete(pokemon);
        logger.info("Pokémon {} deletado com sucesso", id);
    }

    @Override
    @Cacheable(value = "favoritePokemon", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    public Page<PokemonResponse> getFavoritePokemon(Pageable pageable) {
        logger.info("Buscando Pokémon favoritos - página: {}", pageable.getPageNumber());
        
        return pokemonRepository.findByFavorite(true, pageable)
                .map(this::convertToResponse);
    }

    @Override
    public void evictAllCaches() {
        logger.info("Limpando todos os caches manualmente");
        
        try {
            cacheManager.getCacheNames().forEach(cacheName -> {
                var cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    cache.clear();
                    logger.debug("Cache limpo: {}", cacheName);
                }
            });
            logger.info("Todos os caches foram limpos manualmente");
        } catch (Exception e) {
            logger.error("Erro ao limpar caches: {}", e.getMessage());
        }
    }

    // ============ MÉTODOS PRIVADOS AUXILIARES ============

    private Optional<Pokemon> findExistingPokemon(String nameOrId) {
        try {
            // Tenta como ID
            Integer id = Integer.parseInt(nameOrId);
            return pokemonRepository.findByIdPokeApi(id);
        } catch (NumberFormatException e) {
            // Se não for número, busca por nome
            return pokemonRepository.findByNameIgnoreCase(nameOrId);
        }
    }

    /**
     * Limpa e normaliza o parâmetro nameOrId
     * Remove palavras como "ou" e espaços extras
     */
    private String cleanNameOrIdParameter(String nameOrId) {
        if (nameOrId == null) {
            return null;
        }
        
        // Remove espaços extras
        String cleaned = nameOrId.trim();
        
        // Se contém "ou", pega apenas a primeira parte
        if (cleaned.toLowerCase().contains(" ou ")) {
            String[] parts = cleaned.split(" ou ");
            cleaned = parts[0].trim();
        }
        
        // Se contém vírgula, pega apenas a primeira parte
        if (cleaned.contains(",")) {
            String[] parts = cleaned.split(",");
            cleaned = parts[0].trim();
        }
        
        return cleaned;
    }

    private Pokemon convertPokeApiToEntity(PokeApiResponse response) {
        Pokemon pokemon = new Pokemon();
        pokemon.setIdPokeApi(response.getId());
        pokemon.setName(response.getName());
        pokemon.setHeight(response.getHeight());
        pokemon.setWeight(response.getWeight());
        pokemon.setCachedAt(LocalDateTime.now());
        pokemon.setFavorite(false);

        // Extrai primeira habilidade não oculta
        String firstAbility = response.getAbilities().stream()
                .filter(ability -> ability.getIsHidden() != null && !ability.getIsHidden())
                .findFirst()
                .map(ability -> ability.getAbility().getName())
                .orElse("unknown");
        pokemon.setFirstAbility(firstAbility);

        // Extrai tipos como CSV
        String types = response.getTypes().stream()
                .map(type -> type.getType().getName())
                .reduce((a, b) -> a + "," + b)
                .orElse("unknown");
        pokemon.setTypes(types);

        logger.debug("Pokémon convertido: {} (ID: {})", pokemon.getName(), pokemon.getIdPokeApi());
        return pokemon;
    }

    private PokemonResponse convertToResponse(Pokemon pokemon) {
        return new PokemonResponse(
                pokemon.getId(),
                pokemon.getIdPokeApi(),
                pokemon.getName(),
                pokemon.getHeight(),
                pokemon.getWeight(),
                pokemon.getFirstAbility(),
                pokemon.getTypes(),
                pokemon.getCachedAt(),
                pokemon.getFavorite(),
                pokemon.getNote()
        );
    }

    // ============ MÉTODOS DE VALIDAÇÃO ============

    private void validateNameOrId(String nameOrId) {
        if (nameOrId == null || nameOrId.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome ou ID do Pokémon não pode ser vazio");
        }
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do Pokémon deve ser um número positivo");
        }
    }

    private void validatePokeApiId(Integer idPokeApi) {
        if (idPokeApi == null || idPokeApi <= 0) {
            throw new IllegalArgumentException("ID da PokeAPI deve ser um número positivo");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do Pokémon não pode ser vazio");
        }
    }

    private void validateType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo do Pokémon não pode ser vazio");
        }
    }

    private void validateFavoriteRequest(FavoriteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request não pode ser nulo");
        }
        if (request.getFavorite() == null) {
            throw new IllegalArgumentException("Campo 'favorite' é obrigatório");
        }
    }
}