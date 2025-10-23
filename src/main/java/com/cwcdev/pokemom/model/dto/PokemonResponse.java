// model/dto/PokemonResponse.java
package com.cwcdev.pokemom.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Resposta com dados do Pokémon")
public class PokemonResponse {
    
    @Schema(description = "ID local do Pokémon", example = "1")
    private Long id;
    
    @Schema(description = "ID da PokeAPI", example = "25")
    private Integer idPokeApi;
    
    @Schema(description = "Nome do Pokémon", example = "pikachu")
    private String name;
    
    @Schema(description = "Altura do Pokémon", example = "4")
    private Integer height;
    
    @Schema(description = "Peso do Pokémon", example = "60")
    private Integer weight;
    
    @Schema(description = "Primeira habilidade", example = "static")
    private String firstAbility;
    
    @Schema(description = "Tipos do Pokémon (CSV)", example = "electric")
    private String types;
    
    @Schema(description = "Data/hora do cache", example = "2024-01-15T10:30:00")
    private LocalDateTime cachedAt;
    
    @Schema(description = "Indica se é favorito", example = "true")
    private Boolean favorite;
    
    @Schema(description = "Nota pessoal", example = "Meu Pokémon favorito!")
    private String note;

    // Constructors
    public PokemonResponse() {}

    public PokemonResponse(Long id, Integer idPokeApi, String name, Integer height, Integer weight,
                         String firstAbility, String types, LocalDateTime cachedAt, 
                         Boolean favorite, String note) {
        this.id = id;
        this.idPokeApi = idPokeApi;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.firstAbility = firstAbility;
        this.types = types;
        this.cachedAt = cachedAt;
        this.favorite = favorite;
        this.note = note;
    }

    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public Integer getIdPokeApi() { 
        return idPokeApi; 
    }
    
    public void setIdPokeApi(Integer idPokeApi) { 
        this.idPokeApi = idPokeApi; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public Integer getHeight() { 
        return height; 
    }
    
    public void setHeight(Integer height) { 
        this.height = height; 
    }
    
    public Integer getWeight() { 
        return weight; 
    }
    
    public void setWeight(Integer weight) { 
        this.weight = weight; 
    }
    
    public String getFirstAbility() { 
        return firstAbility; 
    }
    
    public void setFirstAbility(String firstAbility) { 
        this.firstAbility = firstAbility; 
    }
    
    public String getTypes() { 
        return types; 
    }
    
    public void setTypes(String types) { 
        this.types = types; 
    }
    
    public LocalDateTime getCachedAt() { 
        return cachedAt; 
    }
    
    public void setCachedAt(LocalDateTime cachedAt) { 
        this.cachedAt = cachedAt; 
    }
    
    public Boolean getFavorite() { 
        return favorite; 
    }
    
    public void setFavorite(Boolean favorite) { 
        this.favorite = favorite; 
    }
    
    public String getNote() { 
        return note; 
    }
    
    public void setNote(String note) { 
        this.note = note; 
    }
}