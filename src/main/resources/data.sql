-- ====================================
-- INSERIR ROLES
-- ====================================
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_USER');

-- ====================================
-- INSERIR USERS
-- ====================================
-- Senha: admin123 (BCrypt)
INSERT INTO tb_user (first_name, last_name, email, password) 
VALUES ('Admin', 'Sistema', 'admin@pokemom.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- Senha: user123 (BCrypt)
INSERT INTO tb_user (first_name, last_name, email, password) 
VALUES ('João', 'Silva', 'joao@pokemom.com', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.');

-- Senha: maria123 (BCrypt)
INSERT INTO tb_user (first_name, last_name, email, password) 
VALUES ('Maria', 'Santos', 'maria@pokemom.com', '$2a$10$HlzMUqYVjLflF5eLCxN8GuLZFtWJELVLXxbmNTzqIjpL5Dv1xr.i6');

-- ====================================
-- ASSOCIAR USERS E ROLES
-- ====================================
-- Admin tem ROLE_ADMIN e ROLE_USER
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 2);

-- João tem apenas ROLE_USER
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);

-- Maria tem apenas ROLE_USER
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 2);

-- ====================================
-- INSERIR POKÉMON (Exemplos)
-- ====================================
INSERT INTO pokemon (id_poke_api, name, height, weight, first_ability, types, cached_at, favorite, note) 
VALUES (25, 'pikachu', 4, 60, 'static', 'electric', CURRENT_TIMESTAMP, true, 'Pokémon icônico e mascote da franquia');

INSERT INTO pokemon (id_poke_api, name, height, weight, first_ability, types, cached_at, favorite, note) 
VALUES (6, 'charizard', 17, 905, 'blaze', 'fire, flying', CURRENT_TIMESTAMP, true, 'Dragão de fogo poderoso');

INSERT INTO pokemon (id_poke_api, name, height, weight, first_ability, types, cached_at, favorite, note) 
VALUES (9, 'blastoise', 16, 855, 'torrent', 'water', CURRENT_TIMESTAMP, false, null);

INSERT INTO pokemon (id_poke_api, name, height, weight, first_ability, types, cached_at, favorite, note) 
VALUES (3, 'venusaur', 20, 1000, 'overgrow', 'grass, poison', CURRENT_TIMESTAMP, false, null);

INSERT INTO pokemon (id_poke_api, name, height, weight, first_ability, types, cached_at, favorite, note) 
VALUES (150, 'mewtwo', 20, 1220, 'pressure', 'psychic', CURRENT_TIMESTAMP, true, 'Pokémon lendário criado geneticamente');

INSERT INTO pokemon (id_poke_api, name, height, weight, first_ability, types, cached_at, favorite, note) 
VALUES (94, 'gengar', 15, 405, 'cursed-body', 'ghost, poison', CURRENT_TIMESTAMP, false, null);

INSERT INTO pokemon (id_poke_api, name, height, weight, first_ability, types, cached_at, favorite, note) 
VALUES (130, 'gyarados', 65, 2350, 'intimidate', 'water, flying', CURRENT_TIMESTAMP, false, null);

INSERT INTO pokemon (id_poke_api, name, height, weight, first_ability, types, cached_at, favorite, note) 
VALUES (143, 'snorlax', 21, 4600, 'immunity', 'normal', CURRENT_TIMESTAMP, false, null);

INSERT INTO pokemon (id_poke_api, name, height, weight, first_ability, types, cached_at, favorite, note) 
VALUES (131, 'lapras', 25, 2200, 'water-absorb', 'water, ice', CURRENT_TIMESTAMP, false, null);

INSERT INTO pokemon (id_poke_api, name, height, weight, first_ability, types, cached_at, favorite, note) 
VALUES (448, 'lucario', 12, 540, 'steadfast', 'fighting, steel', CURRENT_TIMESTAMP, true, 'Pokémon lutador com aura');