# Apoio Pet

Sistema web de gerenciamento de adoção de animais desenvolvido como Projeto Aplicado II — Equipe 6 — SENAI SC.

## O que o sistema faz

O Apoio Pet organiza o ciclo completo de adoção de animais:

- **Animais** — cadastro de cães e gatos disponíveis para adoção, com espécie, raça, porte, faixa etária, cidade e status
- **ONGs** — organizações responsáveis pelos animais
- **Protetores** — pessoas físicas que cuidam de animais
- **Adotantes** — pessoas interessadas em adotar
- **Solicitações de adoção** — pedidos de adoção com fluxo de aprovação/recusa/cancelamento
- **Adoções confirmadas** — registro da entrega física do animal ao adotante

## Estrutura do repositório

```
projeto-aplicado-senai-2026/
├── backend/    # API REST — Spring Boot 3 + Java 21 + PostgreSQL
└── frontend/   # SPA — Angular 21 + PrimeNG 21
```

## Stack

| Camada   | Tecnologia                                    |
|----------|-----------------------------------------------|
| Backend  | Java 21, Spring Boot 3.3.5, Maven, Lombok     |
| Banco    | PostgreSQL 15+                                |
| Frontend | Angular 21, PrimeNG 21.1, TypeScript 5.9      |

## Pré-requisitos globais

| Ferramenta    | Versão mínima | Para quê                  |
|---------------|---------------|---------------------------|
| Java          | 21            | Executar o backend        |
| Maven         | 3.9           | Build e dependências Java |
| PostgreSQL    | 15            | Banco de dados            |
| Node.js       | 20            | Executar o frontend       |
| npm           | 10            | Dependências JavaScript   |

## Ordem de inicialização

Os serviços **devem** ser iniciados nesta ordem:

```
1. PostgreSQL  →  2. Backend (porta 8080)  →  3. Frontend (porta 4200)
```

O frontend consome a API do backend — iniciar na ordem errada causará erros de rede.

## Início rápido

```bash
# 1. Clone o repositório
git clone https://github.com/<seu-usuario>/projeto-aplicado-senai-2026.git
cd projeto-aplicado-senai-2026

# 2. Crie o banco de dados no PostgreSQL
psql -U postgres -c "CREATE DATABASE apoio_pet;"

# 3. Inicie o backend
cd backend
mvn spring-boot:run

# 4. Em outro terminal, inicie o frontend
cd frontend
npm install
ng serve
```

Acesse: **http://localhost:4200**

---

Consulte o `README.md` de cada subdiretório para configuração detalhada de cada serviço.
