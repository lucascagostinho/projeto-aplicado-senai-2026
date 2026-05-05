# Frontend — Apoio Pet

SPA construída com Angular 21 e PrimeNG 21.

## Pré-requisitos

| Ferramenta    | Versão mínima |
|---------------|---------------|
| Node.js       | 20            |
| npm           | 10            |
| Angular CLI   | 21            |

### Instalar o Angular CLI globalmente (caso ainda não tenha)

```bash
npm install -g @angular/cli
```

## Instalação das dependências

```bash
cd frontend
npm install
```

## Executar em desenvolvimento

> O backend precisa estar rodando em `http://localhost:8080` antes de iniciar o frontend.

```bash
ng serve
```

A aplicação estará disponível em: `http://localhost:4200`

## Build para produção

```bash
ng build
```

Os arquivos gerados ficam em `dist/frontend/browser/`.

## Configuração da URL da API

A URL base da API está definida em:

```
src/app/animal/animal.service.ts
```

```typescript
private readonly apiUrl = 'http://localhost:8080/api/animais';
```

Altere esse valor caso o backend esteja em outro host ou porta.

## Estrutura relevante

```
src/app/animal/
├── animal.model.ts        # Interfaces e tipos TypeScript
├── animal.service.ts      # Comunicação com a API
├── animal-options.ts      # Listas de opções compartilhadas (espécie, porte, etc.)
├── animal-list/           # Tela de listagem com filtros
└── animal-form/           # Tela de cadastro e edição
```
