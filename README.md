# Apoio Pet

Sistema de gerenciamento de adoção de animais desenvolvido como Projeto Aplicado II — Equipe 6 — SENAI SC.

## Visão geral

O Apoio Pet permite cadastrar, listar, editar e excluir animais disponíveis para adoção. A listagem conta com filtros por espécie, sexo, faixa etária, porte, cidade e status, além de paginação e ordenação por coluna.

## Estrutura do repositório

```
projeto-aplicado-senai-2026/
├── backend/    # API REST — Spring Boot 3 + Java 21 + PostgreSQL
└── frontend/   # SPA — Angular 21 + PrimeNG 21
```

## Stack

| Camada     | Tecnologia                          |
|------------|-------------------------------------|
| Backend    | Java 21, Spring Boot 3.3.5, Maven   |
| Banco      | PostgreSQL 15+                      |
| Frontend   | Angular 21, PrimeNG 21, TypeScript  |

## Pré-requisitos globais

- Java 21+
- Maven 3.9+
- Node.js 20+ e npm 10+
- PostgreSQL 15+
- Angular CLI 21 (`npm install -g @angular/cli`)

## Ordem de inicialização

1. Subir o banco de dados PostgreSQL
2. Iniciar o backend (`backend/`)
3. Iniciar o frontend (`frontend/`)

Consulte o `README.md` de cada subdiretório para instruções detalhadas.
