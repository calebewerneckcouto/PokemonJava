package com.cwcdev.pokemom.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableCaching
@EnableScheduling
public class PokemonCacheManager {

    private static final Logger logger = LoggerFactory.getLogger(PokemonCacheManager.class);
    private final AtomicInteger cacheClearCount = new AtomicInteger(0);

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(List.of(
            "pokemon", 
            "pokemonByPokeApiId", 
            "pokemonByName", 
            "allPokemon", 
            "pokemonSearch", 
            "favoritePokemon", 
            "pokeApiData"
        ));
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    /**
     * Limpeza automática de caches a cada hora para garantir dados atualizados
     */
    @Scheduled(fixedRate = 60 * 60 * 1000) // 1 hora
    public void scheduledCacheClear() {
        int count = cacheClearCount.incrementAndGet();
        logger.info("Executando limpeza programada de caches (#{})", count);
        
        // Obtém o CacheManager diretamente do contexto
        CacheManager cacheManager = this.cacheManager();
        clearAllCaches(cacheManager);
        logCacheStatistics(cacheManager);
    }

    /**
     * Log de estatísticas de cache a cada 30 minutos
     */
    @Scheduled(fixedRate = 30 * 60 * 1000) // 30 minutos
    public void logCacheStatistics() {
        CacheManager cacheManager = this.cacheManager();
        logCacheStatistics(cacheManager);
    }

    /**
     * Método público para limpar todos os caches
     */
    public void clearAllCaches(CacheManager cacheManager) {
        logger.info("Limpando todos os caches");
        
        cacheManager.getCacheNames().forEach(cacheName -> {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                logger.debug("Cache limpo: {}", cacheName);
            }
        });
        
        logger.info("Todos os caches foram limpos");
    }

    private void logCacheStatistics(CacheManager cacheManager) {
        logger.info("=== Estatísticas de Cache ===");
        
        cacheManager.getCacheNames().forEach(cacheName -> {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                Object nativeCache = cache.getNativeCache();
                if (nativeCache instanceof org.springframework.cache.concurrent.ConcurrentMapCache) {
                    var concurrentMapCache = (org.springframework.cache.concurrent.ConcurrentMapCache) nativeCache;
                    int size = concurrentMapCache.getNativeCache().size();
                    logger.info("Cache '{}': {} entradas", cacheName, size);
                }
            }
        });
        
        logger.info("=============================");
    }
}