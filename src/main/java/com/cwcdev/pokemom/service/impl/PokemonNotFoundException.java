// service/impl/PokemonNotFoundException.java
package com.cwcdev.pokemom.service.impl;

public class PokemonNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PokemonNotFoundException(String message) {
        super(message);
    }
}