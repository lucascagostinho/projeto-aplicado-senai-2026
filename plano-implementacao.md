# Plano de Implementação — CRUD Básico da Entidade Animal

## Projeto Aplicado II — Equipe 6 — Apoio Pet — SENAI SC

| Campo             | Valor                                      |
|-------------------|--------------------------------------------|
| Fase              | Primeira entrega técnica                   |
| Escopo            | CRUD básico da entidade `animal` — sem relacionamentos |
| Stack Backend     | Java 21 + Spring Boot 3.x + Spring Data JPA |
| Stack Frontend    | Angular (standalone components)            |
| Banco de dados    | PostgreSQL local (porta 5432)              |
| Estrutura         | Monorepo — `backend/` e `frontend/` na raiz do projeto |

---

## Estrutura de Diretórios

```
D:\github\projeto-aplicado-2-plan\
├── backend\
│   ├── pom.xml
│   └── src\main\
│       ├── java\br\senai\apoiopet\
│       │   ├── ApoioPetApplication.java
│       │   ├── animal\
│       │   │   ├── Animal.java
│       │   │   ├── AnimalDTO.java
│       │   │   ├── AnimalRepository.java
│       │   │   ├── AnimalService.java
│       │   │   └── AnimalController.java
│       │   └── config\
│       │       └── CorsConfig.java
│       └── resources\
│           └── application.properties
├── frontend\
│   └── src\app\
│       ├── animal\
│       │   ├── animal-list\
│       │   │   ├── animal-list.component.ts
│       │   │   └── animal-list.component.html
│       │   ├── animal-form\
│       │   │   ├── animal-form.component.ts
│       │   │   └── animal-form.component.html
│       │   └── animal.service.ts
│       ├── app.component.ts
│       ├── app.component.html
│       └── app.routes.ts
├── analise.md
└── plano-implementacao.md
```

---

## Pré-requisitos

Antes de iniciar, garantir que os seguintes itens estão instalados e funcionando:

| Ferramenta    | Verificação                    | Versão mínima |
|---------------|-------------------------------|---------------|
| Java          | `java -version`               | 21            |
| Maven         | `mvn -version`                | 3.9+          |
| Node.js       | `node -v`                     | 18+           |
| Angular CLI   | `ng version`                  | 17+           |
| PostgreSQL    | serviço rodando na porta 5432 | 14+           |

**Criar o banco de dados manualmente antes de subir o backend:**
```sql
CREATE DATABASE apoio_pet;
```

---

## Parte 1 — Backend (Java 21 + Spring Boot)

### 1.1 Configuração do Projeto (Maven)

- **Group ID:** `br.senai`
- **Artifact ID:** `apoio-pet`
- **Java:** 21
- **Spring Boot:** 3.3.x
- **Packaging:** Jar

**Dependências (`pom.xml`):**

| Dependência                      | Finalidade                              |
|----------------------------------|-----------------------------------------|
| `spring-boot-starter-web`        | API REST com Spring MVC                 |
| `spring-boot-starter-data-jpa`   | Persistência com Hibernate + JPA        |
| `spring-boot-starter-validation` | Validação de campos com Jakarta Bean    |
| `postgresql`                     | Driver JDBC do PostgreSQL               |
| `lombok`                         | Geração de getters/setters/construtores |

### 1.2 Configuração de Banco de Dados (`application.properties`)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/apoio_pet
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
server.port=8080
```

> `ddl-auto=update` cria/atualiza a tabela automaticamente ao subir o backend.

### 1.3 Campos da Entidade `Animal`

Baseado no MER da documentação (sem `responsavel_id` — sem relacionamentos nesta fase):

| Campo           | Tipo Java     | Coluna BD         | Obrigatório | Valores aceitos                        |
|-----------------|---------------|-------------------|-------------|----------------------------------------|
| `id`            | `Long`        | `BIGSERIAL PK`    | automático  | gerado pelo banco                      |
| `especie`       | `String`      | `VARCHAR(20)`     | sim         | `cao`, `gato`                          |
| `raca`          | `String`      | `VARCHAR(100)`    | não         | texto livre                            |
| `sexo`          | `String`      | `VARCHAR(10)`     | sim         | `macho`, `femea`                       |
| `faixaEtaria`   | `String`      | `VARCHAR(15)`     | sim         | `filhote`, `jovem`, `adulto`, `senior` |
| `porte`         | `String`      | `VARCHAR(10)`     | sim         | `pequeno`, `medio`, `grande`           |
| `cor`           | `String`      | `VARCHAR(50)`     | não         | texto livre                            |
| `caracteristicas`| `String`     | `TEXT`            | não         | texto livre                            |
| `status`        | `String`      | `VARCHAR(20)`     | sim         | default `disponivel`                   |
| `foto`          | `String`      | `VARCHAR(255)`    | não         | URL ou caminho da imagem               |
| `cidade`        | `String`      | `VARCHAR(100)`    | sim         | texto livre                            |
| `estado`        | `String`      | `VARCHAR(2)`      | sim         | sigla UF, ex: `SC`                     |
| `castrado`      | `Boolean`     | `BOOLEAN`         | sim         | `true` / `false`                       |
| `vacinado`      | `Boolean`     | `BOOLEAN`         | sim         | `true` / `false`                       |
| `criadoEm`      | `LocalDateTime`| `TIMESTAMP`      | automático  | preenchido no `@PrePersist`            |

### 1.4 Endpoints REST

| Método   | Endpoint            | Ação                    | Status de retorno     |
|----------|---------------------|-------------------------|-----------------------|
| `POST`   | `/api/animais`      | Cadastrar novo animal   | `201 Created`         |
| `GET`    | `/api/animais`      | Listar todos os animais | `200 OK`              |
| `GET`    | `/api/animais/{id}` | Buscar animal por ID    | `200 OK`              |
| `PUT`    | `/api/animais/{id}` | Atualizar animal        | `200 OK`              |
| `DELETE` | `/api/animais/{id}` | Remover animal          | `204 No Content`      |

### 1.5 Arquitetura das Classes (Backend)

```
AnimalController  →  AnimalService  →  AnimalRepository  →  PostgreSQL
      ↕                                        ↕
   AnimalDTO                              Animal (Entity)
```

| Classe              | Responsabilidade                                                  |
|---------------------|-------------------------------------------------------------------|
| `Animal.java`       | Entidade JPA mapeada para a tabela `animal`                       |
| `AnimalDTO.java`    | Objeto de transferência de dados (request/response da API)        |
| `AnimalRepository`  | Interface de acesso ao banco (`JpaRepository<Animal, Long>`)      |
| `AnimalService`     | Lógica de negócio: listar, buscar, salvar, atualizar, deletar     |
| `AnimalController`  | Endpoints REST, mapeamento DTO ↔ entidade                         |
| `CorsConfig`        | Libera requisições da origem `http://localhost:4200` (Angular)    |

### 1.6 CORS

Configuração via `WebMvcConfigurer` permitindo `http://localhost:4200` em todos os métodos HTTP para `/api/**`.

---

## Parte 2 — Frontend (Angular)

### 2.1 Geração do Projeto

```bash
cd D:\github\projeto-aplicado-2-plan
ng new frontend --routing --style=css --standalone --skip-git
cd frontend
npm install
```

### 2.2 Telas e Rotas

| Rota                  | Componente            | Descrição                                      |
|-----------------------|-----------------------|------------------------------------------------|
| `/`                   | —                     | Redireciona para `/animais`                    |
| `/animais`            | `AnimalListComponent` | Tabela com todos os animais + ações            |
| `/animais/novo`       | `AnimalFormComponent` | Formulário de cadastro                         |
| `/animais/editar/:id` | `AnimalFormComponent` | Formulário de edição pré-preenchido            |

### 2.3 Tela de Listagem (`AnimalListComponent`)

**Funcionalidades:**
- Tabela com colunas: Espécie, Raça, Sexo, Faixa Etária, Porte, Cidade/UF, Castrado, Vacinado, Status
- Coluna de Ações: botões **Editar** e **Excluir** por linha
- Botão **"Novo Animal"** no topo da página
- Exclusão com `window.confirm()` antes de chamar o DELETE
- Recarrega a lista automaticamente após exclusão

### 2.4 Tela de Formulário (`AnimalFormComponent`)

**Funcionalidades:**
- Reactive Form com todos os campos da entidade `animal`
- Modo **Cadastro** (`/animais/novo`): formulário em branco
- Modo **Edição** (`/animais/editar/:id`): formulário pré-preenchido com dados do banco
- Campos de seleção (dropdowns): espécie, sexo, faixa etária, porte, status
- Campos de texto: raça, cor, características, foto, cidade, estado
- Checkboxes: castrado, vacinado
- Botão **"Salvar"**: chama POST (novo) ou PUT (edição)
- Botão **"Cancelar"**: navega de volta para `/animais`

### 2.5 Interface TypeScript (`Animal`)

```typescript
export interface Animal {
  id?: number;
  especie: string;
  raca?: string;
  sexo: string;
  faixaEtaria: string;
  porte: string;
  cor?: string;
  caracteristicas?: string;
  status: string;
  foto?: string;
  cidade: string;
  estado: string;
  castrado: boolean;
  vacinado: boolean;
  criadoEm?: string;
}
```

### 2.6 Serviço HTTP (`AnimalService`)

Todos os métodos fazem requisições para `http://localhost:8080/api/animais`:

| Método                              | HTTP   | Endpoint            |
|-------------------------------------|--------|---------------------|
| `listar()`                          | GET    | `/api/animais`      |
| `buscarPorId(id)`                   | GET    | `/api/animais/{id}` |
| `criar(animal)`                     | POST   | `/api/animais`      |
| `atualizar(id, animal)`             | PUT    | `/api/animais/{id}` |
| `deletar(id)`                       | DELETE | `/api/animais/{id}` |

---

## Ordem de Implementação

### Etapa 1 — Backend
1. Criar estrutura Maven (`pom.xml`)
2. Criar `ApoioPetApplication.java`
3. Criar entidade `Animal.java`
4. Criar `AnimalDTO.java`
5. Criar `AnimalRepository.java`
6. Criar `AnimalService.java`
7. Criar `AnimalController.java`
8. Criar `CorsConfig.java`
9. Configurar `application.properties`
10. Criar banco: `CREATE DATABASE apoio_pet;`
11. Subir backend: `mvn spring-boot:run`

### Etapa 2 — Frontend
1. Gerar projeto Angular: `ng new frontend --routing --style=css --standalone --skip-git`
2. Criar interface `Animal`
3. Criar `AnimalService`
4. Criar `AnimalListComponent` (listagem)
5. Criar `AnimalFormComponent` (cadastro/edição)
6. Configurar rotas em `app.routes.ts`
7. Subir frontend: `ng serve`

---

## Verificação (Testes de Aceite)

### Backend (testar com Postman ou curl)

| Teste | Comando curl | Resultado esperado |
|-------|-------------|-------------------|
| Criar animal | `POST /api/animais` com body JSON | `201 Created` com ID gerado |
| Listar animais | `GET /api/animais` | `200 OK` com array de animais |
| Buscar por ID | `GET /api/animais/1` | `200 OK` com dados do animal |
| Atualizar | `PUT /api/animais/1` com body JSON | `200 OK` com dados atualizados |
| Deletar | `DELETE /api/animais/1` | `204 No Content` |

### Frontend (testar no navegador em `http://localhost:4200`)

| Teste | Ação | Resultado esperado |
|-------|------|-------------------|
| Rota padrão | Abrir `localhost:4200` | Redireciona para `/animais` |
| Listagem | Carregar `/animais` | Tabela com animais cadastrados |
| Cadastro | Clicar em "Novo Animal", preencher e salvar | Animal aparece na listagem |
| Edição | Clicar em "Editar" na linha | Formulário pré-preenchido; salvar atualiza a lista |
| Exclusão | Clicar em "Excluir", confirmar | Animal removido da tabela |

---

## Exemplo de Payload JSON (POST/PUT)

```json
{
  "especie": "cao",
  "raca": "Labrador",
  "sexo": "macho",
  "faixaEtaria": "jovem",
  "porte": "grande",
  "cor": "amarelo",
  "caracteristicas": "Dócil e brincalhão",
  "status": "disponivel",
  "foto": "",
  "cidade": "Florianópolis",
  "estado": "SC",
  "castrado": true,
  "vacinado": true
}
```
