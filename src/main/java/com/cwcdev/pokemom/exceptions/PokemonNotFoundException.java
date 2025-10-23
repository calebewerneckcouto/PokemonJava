// service/impl/PokemonNotFoundException.java
package com.cwcdev.pokemom.exceptions;

public class PokemonNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PokemonNotFoundException(String message) {
        super(message);
    }
}