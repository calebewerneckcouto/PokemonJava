
# Projetos Realizados em aula

https://github.com/calebewerneckcouto/GerenciamentoCEP



# 🎮 Pokémon API

API REST para gerenciamento e cache de dados de Pokémon, integrada com a PokeAPI oficial.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Uso da API](#uso-da-api)
- [Endpoints](#endpoints)
- [Exemplos de Requisições](#exemplos-de-requisições)
- [Modelo de Dados](#modelo-de-dados)
- [Tratamento de Erros](#tratamento-de-erros)
- [Licença](#licença)
- [Contato](#contato)

## 🎯 Sobre o Projeto

Esta API permite cachear informações de Pokémon da PokeAPI oficial em um banco de dados local, possibilitando buscas mais rápidas, adição de notas pessoais e marcação de favoritos. É ideal para aplicações que necessitam de consultas frequentes a dados de Pokémon sem sobrecarregar a API oficial.

## 🚀 Tecnologias

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL/MySQL (ou outro banco relacional)
- OpenAPI 3.0 (Swagger)
- Maven

## ✨ Funcionalidades

- ✅ Cache de Pokémon da PokeAPI
- ✅ Busca por ID ou nome
- ✅ Listagem paginada
- ✅ Filtro por tipo
- ✅ Sistema de favoritos
- ✅ Anotações personalizadas
- ✅ Exclusão de cache

## 📦 Instalação

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Banco de dados (PostgreSQL, MySQL, etc.)

### Passos

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/pokemon-api.git
cd pokemon-api
```

2. Configure o banco de dados no `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pokemon_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

3. Compile o projeto:
```bash
mvn clean install
```

4. Execute a aplicação:
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

## ⚙️ Configuração

### Variáveis de Ambiente

Você pode configurar as seguintes variáveis:

```properties
# Servidor
server.port=8080

# Banco de Dados
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# JPA
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=update

# PokeAPI
pokeapi.base.url=https://pokeapi.co/api/v2
```

## 📖 Uso da API

### Documentação Swagger

Acesse a documentação interativa em:
- **Desenvolvimento**: http://localhost:8080/swagger-ui.html
- **Produção**: https://www.cwc3d.net/swagger-ui.html

## 🔌 Endpoints

### 1. Cachear Pokémon

Busca um Pokémon na PokeAPI e salva no banco local.

```http
POST /api/pokemon/cache/{nameOrId}
```

**Parâmetros:**
- `nameOrId` (path): Nome ou ID do Pokémon (ex: "pikachu" ou "25")

**Respostas:**
- `200`: Pokémon cacheado com sucesso
- `404`: Pokémon não encontrado na PokeAPI
- `503`: Erro na integração com PokeAPI

---

### 2. Listar Todos os Pokémon

Retorna lista paginada de Pokémon cacheados.

```http
GET /api/pokemon?page=0&size=20
```

**Parâmetros de Query:**
- `page` (opcional): Número da página (padrão: 0)
- `size` (opcional): Tamanho da página (padrão: 20)

**Resposta:** `200` com dados paginados

---

### 3. Buscar Pokémon por ID

Retorna um Pokémon específico pelo ID local.

```http
GET /api/pokemon/{id}
```

**Parâmetros:**
- `id` (path): ID local do Pokémon

**Respostas:**
- `200`: Pokémon encontrado
- `404`: Pokémon não encontrado

---

### 4. Buscar por Tipo

Busca Pokémon por tipo com paginação.

```http
GET /api/pokemon/search?type=fire&page=0&size=20
```

**Parâmetros de Query:**
- `type` (obrigatório): Tipo do Pokémon (ex: "fire", "water", "electric")
- `page` (opcional): Número da página (padrão: 0)
- `size` (opcional): Tamanho da página (padrão: 20)

**Respostas:**
- `200`: Busca realizada com sucesso
- `404`: Nenhum Pokémon encontrado

---

### 5. Favoritar/Desfavoritar Pokémon

Marca ou desmarca um Pokémon como favorito e adiciona nota.

```http
PATCH /api/pokemon/{id}/favorite
```

**Parâmetros:**
- `id` (path): ID local do Pokémon

**Body:**
```json
{
  "favorite": true,
  "note": "Meu Pokémon favorito!"
}
```

**Respostas:**
- `200`: Pokémon atualizado com sucesso
- `404`: Pokémon não encontrado

---

### 6. Deletar Pokémon

Remove um Pokémon do cache local.

```http
DELETE /api/pokemon/{id}
```

**Parâmetros:**
- `id` (path): ID local do Pokémon

**Respostas:**
- `204`: Pokémon deletado com sucesso
- `404`: Pokémon não encontrado

## 💻 Exemplos de Requisições

### Cachear Pikachu

```bash
curl -X POST http://localhost:8080/api/pokemon/cache/pikachu
```

### Listar Pokémon (primeira página)

```bash
curl -X GET "http://localhost:8080/api/pokemon?page=0&size=10"
```

### Buscar Pokémon do tipo fogo

```bash
curl -X GET "http://localhost:8080/api/pokemon/search?type=fire&page=0&size=20"
```

### Marcar como favorito

```bash
curl -X PATCH http://localhost:8080/api/pokemon/1/favorite \
  -H "Content-Type: application/json" \
  -d '{
    "favorite": true,
    "note": "Muito forte em batalhas!"
  }'
```

### Deletar Pokémon

```bash
curl -X DELETE http://localhost:8080/api/pokemon/1
```

## 📊 Modelo de Dados

### PokemonResponse

```json
{
  "id": 1,
  "idPokeApi": 25,
  "name": "pikachu",
  "height": 4,
  "weight": 60,
  "firstAbility": "static",
  "types": "electric",
  "cachedAt": "2025-10-22T10:30:00",
  "favorite": true,
  "note": "Meu Pokémon favorito!"
}
```

### PagePokemonResponse

```json
{
  "content": [...],
  "totalPages": 5,
  "totalElements": 100,
  "size": 20,
  "number": 0,
  "first": true,
  "last": false,
  "numberOfElements": 20,
  "empty": false
}
```

## ⚠️ Tratamento de Erros

A API utiliza códigos HTTP padrão:

- `200`: Sucesso
- `204`: Sucesso sem conteúdo
- `404`: Recurso não encontrado
- `503`: Serviço indisponível (erro na PokeAPI)

Exemplo de resposta de erro:

```json
{
  "timestamp": "2025-10-22T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pokémon não encontrado",
  "path": "/api/pokemon/999"
}
```

## 📝 Boas Práticas

- Cache Pokémon antes de realizar operações
- Use paginação para listas grandes
- Implemente rate limiting para proteção
- Valide dados antes de enviar requisições
- Trate erros de conexão com a PokeAPI

## 🧪 Testes

Execute os testes com:

```bash
mvn test
```

Para relatório de cobertura:

```bash
mvn clean test jacoco:report
```

## 🚀 Deploy

### Docker

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build e execute:

```bash
docker build -t pokemon-api .
docker run -p 8080:8080 pokemon-api
```

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](https://choosealicense.com/licenses/mit/) para mais detalhes.

## 👤 Contato

**CWCDEV**

- Website: [https://www.cwc3d.net](https://www.cwc3d.net)
- Email: calebewerneck@hotmail.com

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para:

1. Fazer fork do projeto
2. Criar uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abrir um Pull Request

## 📌 Roadmap

- [ ] Autenticação JWT
- [ ] Cache Redis
- [ ] Busca avançada (nome, habilidades, stats)
- [ ] Export de dados (CSV, JSON)
- [ ] WebSocket para atualizações em tempo real
- [ ] Sistema de tags personalizadas
- [ ] Comparação entre Pokémon

---

⭐ Desenvolvido com ❤️ por [CWCDEV](https://www.cwc3d.net)
