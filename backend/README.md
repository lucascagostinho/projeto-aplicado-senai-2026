# Backend — Apoio Pet

API REST construída com Spring Boot 3.3.5 e Java 21.

## Pré-requisitos

| Ferramenta  | Versão mínima |
|-------------|---------------|
| Java        | 21            |
| Maven       | 3.9           |
| PostgreSQL  | 15            |

## Configuração do banco de dados

1. Crie o banco no PostgreSQL:

```sql
CREATE DATABASE apoio_pet;
```

2. As credenciais padrão configuradas em `src/main/resources/application.properties` são:

```
Host:     localhost:5432
Banco:    apoio_pet
Usuário:  postgres
Senha:    admin
```

Para usar credenciais diferentes, edite o arquivo `application.properties` antes de iniciar:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/apoio_pet
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

> A tabela `animal` é criada automaticamente na primeira execução pelo script `src/main/resources/db/create-tables.sql`.  
> O banco também é populado automaticamente com 12 animais de exemplo caso esteja vazio.

## Instalação das dependências

```bash
cd backend
mvn dependency:resolve
```

## Executar em desenvolvimento

```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

## Build para produção

```bash
mvn clean package -DskipTests
java -jar target/apoio-pet-0.0.1-SNAPSHOT.jar
```

## Endpoints principais

| Método | Rota                   | Descrição                      |
|--------|------------------------|--------------------------------|
| GET    | `/api/animais`         | Listar animais (com filtros)   |
| GET    | `/api/animais/{id}`    | Buscar animal por ID           |
| POST   | `/api/animais`         | Cadastrar novo animal          |
| PUT    | `/api/animais/{id}`    | Atualizar animal               |
| DELETE | `/api/animais/{id}`    | Excluir animal                 |

### Filtros disponíveis (query params em GET `/api/animais`)

| Parâmetro    | Valores aceitos                                      |
|--------------|------------------------------------------------------|
| `especie`    | `CAO`, `GATO`                                        |
| `sexo`       | `MACHO`, `FEMEA`                                     |
| `faixaEtaria`| `FILHOTE`, `JOVEM`, `ADULTO`, `SENIOR`               |
| `porte`      | `PEQUENO`, `MEDIO`, `GRANDE`                         |
| `status`     | `DISPONIVEL`, `EM_PROCESSO`, `ADOTADO`, `INDISPONIVEL` |
| `cidade`     | texto livre (busca parcial, sem distinção de maiúsculas) |

Exemplo: `GET /api/animais?especie=CAO&porte=PEQUENO&cidade=florianópolis`
