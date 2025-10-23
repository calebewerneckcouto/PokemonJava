// model/dto/FavoriteRequest.java
package com.cwcdev.pokemom.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Requisição para favoritar Pokémon")
public class FavoriteRequest {
    
    @Schema(description = "Indica se é favorito", required = true, example = "true")
    @NotNull(message = "O campo favorite é obrigatório")
    private Boolean favorite;
    
    @Schema(description = "Nota pessoal", example = "Muito forte!")
    private String note;

    // Constructors
    public FavoriteRequest() {}

    public FavoriteRequest(Boolean favorite, String note) {
        this.favorite = favorite;
        this.note = note;
    }

    // Getters and Setters
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