# Backend — Apoio Pet

API REST do sistema Apoio Pet, responsável por todas as regras de negócio e persistência de dados.

## Tecnologias

| Dependência                   | Versão  | Função                               |
|-------------------------------|---------|--------------------------------------|
| Java                          | 21      | Linguagem                            |
| Spring Boot                   | 3.3.5   | Framework web e DI                   |
| Spring Data JPA               | —       | Persistência com Hibernate           |
| Spring Validation             | —       | Validação de DTOs com Bean Validation|
| PostgreSQL Driver             | —       | Conexão com o banco                  |
| Lombok                        | —       | Redução de boilerplate (getters etc) |
| Maven                         | 3.9+    | Build e gerenciamento de dependências|

## Pré-requisitos

- Java 21+
- Maven 3.9+
- PostgreSQL 15+ em execução

## Configuração do banco de dados

Crie o banco antes de iniciar a aplicação:

```sql
CREATE DATABASE apoio_pet;
```

As credenciais padrão estão em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/apoio_pet
spring.datasource.username=postgres
spring.datasource.password=admin
```

Para usar credenciais diferentes, edite esse arquivo antes de iniciar.

> As tabelas são criadas automaticamente na primeira execução pelo script `src/main/resources/db/create-tables.sql`.
> O banco também é populado com dados de exemplo (animais, ONGs, protetores, adotantes e solicitações) caso as tabelas estejam vazias — via `DataSeeder`.

## Executar em desenvolvimento

```bash
cd backend
mvn spring-boot:run
```

A API ficará disponível em: **http://localhost:8080**

## Build para produção

```bash
mvn clean package -DskipTests
java -jar target/apoio-pet-0.0.1-SNAPSHOT.jar
```

## Estrutura de pacotes

```
br.senai.apoiopet/
├── animal/           # Entidade Animal com filtros e status
├── usuario/
│   ├── ong/          # Organizações responsáveis por animais
│   ├── protetor/     # Protetores individuais
│   └── adotante/     # Adotantes
├── solicitacao/      # Solicitações de adoção (máquina de estados)
├── adocao/           # Confirmação de entrega física
├── config/
│   ├── CorsConfig    # Libera requisições do frontend (porta 4200)
│   └── DataSeeder    # Popula banco com dados de exemplo
└── exception/        # Exceções de negócio e handler global (ProblemDetail)
```

Cada módulo segue a estrutura: `Entity → Repository → Service → Mapper → Controller → DTOs`.

## Endpoints

### Animais — `/api/animais`

| Método | Rota                | Descrição                          |
|--------|---------------------|------------------------------------|
| GET    | `/api/animais`      | Listar animais (aceita filtros)    |
| GET    | `/api/animais/{id}` | Buscar animal por ID               |
| POST   | `/api/animais`      | Cadastrar novo animal              |
| PUT    | `/api/animais/{id}` | Atualizar animal                   |
| DELETE | `/api/animais/{id}` | Excluir animal                     |

**Filtros disponíveis (query params no GET):**

| Parâmetro     | Valores aceitos                                          |
|---------------|----------------------------------------------------------|
| `especie`     | `CAO`, `GATO`                                            |
| `sexo`        | `MACHO`, `FEMEA`                                         |
| `faixaEtaria` | `FILHOTE`, `JOVEM`, `ADULTO`, `SENIOR`                   |
| `porte`       | `PEQUENO`, `MEDIO`, `GRANDE`                             |
| `status`      | `DISPONIVEL`, `EM_PROCESSO`, `ADOTADO`, `INDISPONIVEL`   |
| `cidade`      | texto livre (busca parcial, case-insensitive)            |

Exemplo: `GET /api/animais?especie=CAO&porte=PEQUENO&cidade=florianopolis`

**Status do animal e suas transições:**
- `DISPONIVEL` → pode receber solicitações
- `EM_PROCESSO` → tem uma solicitação aprovada aguardando confirmação
- `ADOTADO` → adoção concluída
- `INDISPONIVEL` → animal retirado temporariamente (cancela automaticamente solicitações pendentes/aprovadas)

---

### ONGs — `/api/ongs`

| Método | Rota            | Descrição           |
|--------|-----------------|---------------------|
| GET    | `/api/ongs`     | Listar todas as ONGs|
| GET    | `/api/ongs/{id}`| Buscar ONG por ID   |
| POST   | `/api/ongs`     | Cadastrar ONG       |
| PUT    | `/api/ongs/{id}`| Atualizar ONG       |
| DELETE | `/api/ongs/{id}`| Excluir ONG         |

**Campos:** `email`\*, `senha`\*, `telefone`, `cep`, `razaoSocial`\*, `cnpj`, `descricao`

---

### Protetores — `/api/protetores`

| Método | Rota                   | Descrição               |
|--------|------------------------|-------------------------|
| GET    | `/api/protetores`      | Listar todos            |
| GET    | `/api/protetores/{id}` | Buscar por ID           |
| POST   | `/api/protetores`      | Cadastrar protetor      |
| PUT    | `/api/protetores/{id}` | Atualizar protetor      |
| DELETE | `/api/protetores/{id}` | Excluir protetor        |

**Campos:** `email`\*, `senha`\*, `telefone`, `cep`, `nome`\*, `cpf`

---

### Adotantes — `/api/adotantes`

| Método | Rota                  | Descrição              |
|--------|-----------------------|------------------------|
| GET    | `/api/adotantes`      | Listar todos           |
| GET    | `/api/adotantes/{id}` | Buscar por ID          |
| POST   | `/api/adotantes`      | Cadastrar adotante     |
| PUT    | `/api/adotantes/{id}` | Atualizar adotante     |
| DELETE | `/api/adotantes/{id}` | Excluir adotante       |

**Campos:** `email`\*, `senha`\*, `telefone`, `cep`, `nome`\*, `cpf`

---

### Solicitações de Adoção — `/api/solicitacoes`

| Método | Rota                          | Descrição                                   |
|--------|-------------------------------|---------------------------------------------|
| GET    | `/api/solicitacoes`           | Listar (filtros: `animalId`, `adotanteId`)  |
| GET    | `/api/solicitacoes/{id}`      | Buscar por ID                               |
| POST   | `/api/solicitacoes`           | Criar solicitação                           |
| PATCH  | `/api/solicitacoes/{id}/aprovar`  | Aprovar solicitação                     |
| PATCH  | `/api/solicitacoes/{id}/recusar`  | Recusar (body: `{ "justificativa": "..." }`) |
| PATCH  | `/api/solicitacoes/{id}/cancelar` | Cancelar (body: `{ "justificativa": "..." }`) |

**Regras de negócio:**
- Só é possível criar solicitação para animal com status `DISPONIVEL`
- Não é permitido criar duas solicitações `PENDENTE` ou `APROVADA` do mesmo adotante para o mesmo animal
- Ao **aprovar**: animal vai para `EM_PROCESSO` e todas as outras solicitações pendentes do mesmo animal são recusadas automaticamente
- Ao **cancelar** uma solicitação `APROVADA`: animal volta para `DISPONIVEL`
- Status possíveis: `PENDENTE`, `APROVADA`, `RECUSADA`, `CANCELADA`

---

### Adoções Confirmadas — `/api/adocoes`

| Método | Rota              | Descrição                              |
|--------|-------------------|----------------------------------------|
| GET    | `/api/adocoes`    | Listar todas as adoções                |
| GET    | `/api/adocoes/{id}` | Buscar por ID                        |
| POST   | `/api/adocoes`    | Confirmar entrega (body abaixo)        |

**Body do POST:**
```json
{
  "solicitacaoId": 1,
  "confirmadoPorId": 2,
  "dataAdocao": "2025-06-01"
}
```

**Regra:** a solicitação referenciada deve estar com status `APROVADA`. Ao confirmar, o animal vai para `ADOTADO`.

---

## Respostas de erro

Todos os erros seguem o padrão RFC 9457 (`ProblemDetail`):

| HTTP | Situação                                               |
|------|--------------------------------------------------------|
| 400  | Dados inválidos (validação Bean Validation)            |
| 404  | Recurso não encontrado                                 |
| 409  | Conflito de regra de negócio (ex: solicitação duplicada, transição de status inválida) |
