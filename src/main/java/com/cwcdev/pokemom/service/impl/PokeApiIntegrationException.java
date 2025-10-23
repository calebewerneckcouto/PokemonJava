// service/impl/PokeApiIntegrationException.java
package com.cwcdev.pokemom.service.impl;

public class PokeApiIntegrationException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PokeApiIntegrationException(String message) {
        super(message);
    }
}