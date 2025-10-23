// controller/PokemonController.java
package com.cwcdev.pokemom.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cwcdev.pokemom.model.dto.FavoriteRequest;
import com.cwcdev.pokemom.model.dto.PokemonResponse;
import com.cwcdev.pokemom.service.PokemonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pokemon")
@Tag(name = "Pokémon", description = "API para gerenciamento de Pokémon")
public class PokemonController {
    
    private final PokemonService pokemonService;
    
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }
    
    @Operation(
        summary = "Cachear Pokémon", 
        description = "Busca um Pokémon na PokeAPI e salva/atualiza no banco local"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon cacheado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pokémon não encontrado na PokeAPI"),
        @ApiResponse(responseCode = "503", description = "Erro na integração com PokeAPI")
    })
    @PostMapping("/cache/{nameOrId}")
    public ResponseEntity<PokemonResponse> cachePokemon(
            @Parameter(description = "Nome ou ID do Pokémon", example = "pikachu ou 25")
            @PathVariable String nameOrId) {
        PokemonResponse response = pokemonService.cachePokemon(nameOrId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Listar Pokémon", 
        description = "Retorna lista paginada de todos os Pokémon cacheados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<PokemonResponse>> getAllPokemon(
            @Parameter(description = "Número da página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamanho da página", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PokemonResponse> pokemonPage = pokemonService.getAllPokemon(pageable);
        return ResponseEntity.ok(pokemonPage);
    }
    
    @Operation(
        summary = "Buscar Pokémon por ID", 
        description = "Retorna um Pokémon específico pelo ID local"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon encontrado"),
        @ApiResponse(responseCode = "404", description = "Pokémon não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PokemonResponse> getPokemonById(
            @Parameter(description = "ID local do Pokémon", example = "1")
            @PathVariable Long id) {
        PokemonResponse response = pokemonService.getPokemonById(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Buscar Pokémon por tipo", 
        description = "Busca Pokémon por tipo com paginação"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum Pokémon encontrado")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<PokemonResponse>> searchPokemonByType(
            @Parameter(description = "Tipo do Pokémon", example = "fire")
            @RequestParam String type,
            
            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamanho da página", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PokemonResponse> results = pokemonService.searchPokemonByType(type, pageable);
        return ResponseEntity.ok(results);
    }
    
    @Operation(
        summary = "Favoritar Pokémon", 
        description = "Marca/desmarca um Pokémon como favorito e adiciona uma nota"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pokémon não encontrado")
    })
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<PokemonResponse> updateFavorite(
            @Parameter(description = "ID local do Pokémon", example = "1")
            @PathVariable Long id,
            
            @Parameter(description = "Dados para favoritar", 
                       content = @Content(schema = @Schema(implementation = FavoriteRequest.class)))
            @Valid @RequestBody FavoriteRequest request) {
        PokemonResponse response = pokemonService.updateFavorite(id, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Deletar Pokémon", 
        description = "Remove um Pokémon do cache local"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pokémon deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pokémon não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePokemon(
            @Parameter(description = "ID local do Pokémon", example = "1")
            @PathVariable Long id) {
        pokemonService.deletePokemon(id);
        return ResponseEntity.noContent().build();
    }
}