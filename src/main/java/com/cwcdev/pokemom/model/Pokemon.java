// model/Pokemon.java
package com.cwcdev.pokemom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "pokemon")
public class Pokemon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_poke_api", unique = true, nullable = false)
    @NotNull(message = "ID da PokeAPI não pode ser nulo")
    private Integer idPokeApi;
    
    @Column(nullable = false)
    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String name;
    
    private Integer height;
    private Integer weight;
    
    @Column(name = "first_ability")
    @Size(max = 50, message = "Habilidade deve ter no máximo 50 caracteres")
    private String firstAbility;
    
    @Column(name = "types")
    @Size(max = 100, message = "Tipos devem ter no máximo 100 caracteres")
    private String types;
    
    @Column(name = "cached_at")
    private LocalDateTime cachedAt;
    
    @Column(name = "favorite")
    private Boolean favorite = false;
    
    @Column(name = "note")
    @Size(max = 500, message = "Nota deve ter no máximo 500 caracteres")
    private String note;

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