// model/Pokemon.java
package com.cwcdev.pokemom.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pokemon")
public class Pokemon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_poke_api", unique = true, nullable = false)
    private Integer idPokeApi;
    
    @Column(nullable = false)
    private String name;
    
    private Integer height;
    private Integer weight;
    
    @Column(name = "first_ability")
    private String firstAbility;
    
    @Column(name = "types")
    private String types; // CSV format: "fire,flying"
    
    @Column(name = "cached_at")
    private LocalDateTime cachedAt;
    
    @Column(name = "favorite")
    private Boolean favorite = false;
    
    @Column(name = "note")
    private String note;

    // Constructors
    public Pokemon() {
        this.cachedAt = LocalDateTime.now();
        this.favorite = false;
    }
    
    public Pokemon(Integer idPokeApi, String name, Integer height, Integer weight, 
                  String firstAbility, String types) {
        this();
        this.idPokeApi = idPokeApi;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.firstAbility = firstAbility;
        this.types = types;
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

    // toString method for debugging
    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", idPokeApi=" + idPokeApi +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", firstAbility='" + firstAbility + '\'' +
                ", types='" + types + '\'' +
                ", cachedAt=" + cachedAt +
                ", favorite=" + favorite +
                ", note='" + note + '\'' +
                '}';
    }
}