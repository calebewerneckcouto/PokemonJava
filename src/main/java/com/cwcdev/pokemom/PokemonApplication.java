package com.cwcdev.pokemom;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PokemonApplication implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    public PokemonApplication(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(PokemonApplication.class, args);
    }

   
    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== GERANDO SENHAS CODIFICADAS ===");
        
        String[] passwords = {"admin", "123456", "guest", "password", "pokemon"};
        
        for (String rawPassword : passwords) {
            String encodedPassword = passwordEncoder.encode(rawPassword);
            System.out.println("Senha: '" + rawPassword + "' -> Codificada: " + encodedPassword);
            
            boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
            System.out.println("  ✓ Verificação: " + matches);
            System.out.println("---");
        }
    }
}