package com.cwcdev.pokemom.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/pokemon")
@Tag(name = "Pokémon", description = "API para gerenciamento de Pokémon")
@Validated
public class PokemonController {
    
    private final PokemonService pokemonService;
    
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }
    
    @Operation(
        summary = "Cachear Pokémon", 
        description = "Busca um Pokémon na PokeAPI e salva/atualiza no banco local. Use apenas o nome OU o ID, não ambos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon cacheado com sucesso",
                    content = @Content(schema = @Schema(implementation = PokemonResponse.class))),
        @ApiResponse(responseCode = "400", description = "Parâmetro inválido",
                    content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pokémon não encontrado na PokeAPI",
                    content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "503", description = "Erro na integração com PokeAPI",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/cache/{nameOrId}")
    public ResponseEntity<PokemonResponse> cachePokemon(
            @Parameter(description = "Nome OU ID do Pokémon (apenas um)", example = "pikachu")
            @PathVariable 
            @NotBlank(message = "Nome ou ID do Pokémon é obrigatório")
            @Size(min = 1, max = 50, message = "Nome ou ID deve ter entre 1 e 50 caracteres")
            @Pattern(regexp = "^[a-zA-Z0-9\\-\\s]+$", message = "Nome ou ID deve conter apenas letras, números, hífens e espaços")
            String nameOrId) {
        
        PokemonResponse response = pokemonService.cachePokemon(nameOrId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Listar Pokémon", 
        description = "Retorna lista paginada de todos os Pokémon cacheados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ResponseEntity<Page<PokemonResponse>> getAllPokemon(
            @Parameter(description = "Número da página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") 
            @Min(value = 0, message = "O número da página deve ser no mínimo 0")
            @Max(value = 1000, message = "O número da página deve ser no máximo 1000")
            int page,
            
            @Parameter(description = "Tamanho da página", example = "20")
            @RequestParam(defaultValue = "20") 
            @Min(value = 1, message = "O tamanho da página deve ser no mínimo 1")
            @Max(value = 100, message = "O tamanho da página deve ser no máximo 100")
            int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PokemonResponse> pokemonPage = pokemonService.getAllPokemon(pageable);
        return ResponseEntity.ok(pokemonPage);
    }
    
    @Operation(
        summary = "Buscar Pokémon por ID", 
        description = "Retorna um Pokémon específico pelo ID local"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon encontrado",
                    content = @Content(schema = @Schema(implementation = PokemonResponse.class))),
        @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pokémon não encontrado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PokemonResponse> getPokemonById(
            @Parameter(description = "ID local do Pokémon", example = "1")
            @PathVariable 
            @NotNull(message = "ID do Pokémon é obrigatório")
            @Positive(message = "ID do Pokémon deve ser um número positivo")
            @Min(value = 1, message = "ID do Pokémon deve ser no mínimo 1")
            Long id) {
        
        PokemonResponse response = pokemonService.getPokemonById(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Buscar Pokémon por ID da PokeAPI", 
        description = "Retorna um Pokémon específico pelo ID da PokeAPI"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon encontrado",
                    content = @Content(schema = @Schema(implementation = PokemonResponse.class))),
        @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pokémon não encontrado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/pokeapi/{idPokeApi}")
    public ResponseEntity<PokemonResponse> getPokemonByIdPokeApi(
            @Parameter(description = "ID da PokeAPI", example = "25")
            @PathVariable 
            @NotNull(message = "ID da PokeAPI é obrigatório")
            @Positive(message = "ID da PokeAPI deve ser um número positivo")
            @Min(value = 1, message = "ID da PokeAPI deve ser no mínimo 1")
            @Max(value = 2000, message = "ID da PokeAPI deve ser no máximo 2000")
            Integer idPokeApi) {
        
        PokemonResponse response = pokemonService.getPokemonByIdPokeApi(idPokeApi);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Buscar Pokémon por nome", 
        description = "Retorna um Pokémon específico pelo nome"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon encontrado",
                    content = @Content(schema = @Schema(implementation = PokemonResponse.class))),
        @ApiResponse(responseCode = "400", description = "Nome inválido",
                    content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pokémon não encontrado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<PokemonResponse> getPokemonByName(
            @Parameter(description = "Nome do Pokémon", example = "pikachu")
            @PathVariable 
            @NotBlank(message = "Nome do Pokémon é obrigatório")
            @Size(min = 2, max = 50, message = "Nome do Pokémon deve ter entre 2 e 50 caracteres")
            @Pattern(regexp = "^[a-zA-Z\\-\\s]+$", message = "Nome do Pokémon deve conter apenas letras, hífens e espaços")
            String name) {
        
        PokemonResponse response = pokemonService.getPokemonByName(name);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Buscar Pokémon por tipo", 
        description = "Busca Pokémon por tipo com paginação"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Parâmetros de busca inválidos",
                    content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Nenhum Pokémon encontrado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/search")
    public ResponseEntity<Page<PokemonResponse>> searchPokemonByType(
            @Parameter(description = "Tipo do Pokémon", example = "fire")
            @RequestParam 
            @NotBlank(message = "Tipo do Pokémon é obrigatório")
            @Size(min = 2, max = 20, message = "Tipo do Pokémon deve ter entre 2 e 20 caracteres")
            @Pattern(regexp = "^[a-zA-Z]+$", message = "Tipo do Pokémon deve conter apenas letras")
            String type,
            
            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") 
            @Min(value = 0, message = "O número da página deve ser no mínimo 0")
            @Max(value = 1000, message = "O número da página deve ser no máximo 1000")
            int page,
            
            @Parameter(description = "Tamanho da página", example = "20")
            @RequestParam(defaultValue = "20") 
            @Min(value = 1, message = "O tamanho da página deve ser no mínimo 1")
            @Max(value = 100, message = "O tamanho da página deve ser no máximo 100")
            int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PokemonResponse> results = pokemonService.searchPokemonByType(type, pageable);
        return ResponseEntity.ok(results);
    }
    
    @Operation(
        summary = "Listar Pokémon favoritos", 
        description = "Retorna lista paginada de Pokémon favoritos"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/favorites")
    public ResponseEntity<Page<PokemonResponse>> getFavoritePokemon(
            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") 
            @Min(value = 0, message = "O número da página deve ser no mínimo 0")
            @Max(value = 1000, message = "O número da página deve ser no máximo 1000")
            int page,
            
            @Parameter(description = "Tamanho da página", example = "20")
            @RequestParam(defaultValue = "20") 
            @Min(value = 1, message = "O tamanho da página deve ser no mínimo 1")
            @Max(value = 100, message = "O tamanho da página deve ser no máximo 100")
            int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PokemonResponse> favorites = pokemonService.getFavoritePokemon(pageable);
        return ResponseEntity.ok(favorites);
    }
    
    @Operation(
        summary = "Favoritar Pokémon", 
        description = "Marca/desmarca um Pokémon como favorito e adiciona uma nota"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = PokemonResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pokémon não encontrado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<PokemonResponse> updateFavorite(
            @Parameter(description = "ID local do Pokémon", example = "1")
            @PathVariable 
            @NotNull(message = "ID do Pokémon é obrigatório")
            @Positive(message = "ID do Pokémon deve ser um número positivo")
            @Min(value = 1, message = "ID do Pokémon deve ser no mínimo 1")
            Long id,
            
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
        @ApiResponse(responseCode = "204", description = "Pokémon deletado com sucesso",
                    content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pokémon não encontrado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePokemon(
            @Parameter(description = "ID local do Pokémon", example = "1")
            @PathVariable 
            @NotNull(message = "ID do Pokémon é obrigatório")
            @Positive(message = "ID do Pokémon deve ser um número positivo")
            @Min(value = 1, message = "ID do Pokémon deve ser no mínimo 1")
            Long id) {
        
        pokemonService.deletePokemon(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(
        summary = "Limpar cache", 
        description = "Limpa todos os caches da aplicação"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cache limpo com sucesso",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/cache/clear")
    public ResponseEntity<Void> clearCache() {
        pokemonService.evictAllCaches();
        return ResponseEntity.ok().build();
    }
}