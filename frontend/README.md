# Frontend — Apoio Pet

Interface web do sistema Apoio Pet, construída como SPA (Single Page Application).

## Tecnologias

| Dependência      | Versão  | Função                                    |
|------------------|---------|-------------------------------------------|
| Angular          | 21.2    | Framework SPA (standalone components)     |
| PrimeNG          | 21.1.6  | Biblioteca de componentes UI              |
| PrimeIcons       | 7.0     | Ícones                                    |
| PrimeUix/themes  | 2.0     | Tema visual Aura                          |
| TypeScript       | 5.9     | Linguagem                                 |
| RxJS             | 7.8     | Comunicação assíncrona com a API          |

> O app usa `provideZonelessChangeDetection()` — detecção de mudanças baseada em signals, sem Zone.js.

## Pré-requisitos

- Node.js 20+
- npm 10+
- Angular CLI 21 (`npm install -g @angular/cli`)
- Backend rodando em `http://localhost:8080`

## Instalação

```bash
cd frontend
npm install
```

## Executar em desenvolvimento

```bash
ng serve
```

Acesse: **http://localhost:4200**

O backend precisa estar rodando antes de iniciar o frontend. Sem ele, todas as chamadas à API falharão.

## Build para produção

```bash
ng build
```

Os arquivos estáticos gerados ficam em `dist/frontend/browser/` e podem ser servidos por qualquer servidor HTTP (nginx, Apache, etc).

## Configuração da URL da API

A URL base da API está definida em cada arquivo `*.service.ts` de cada módulo:

| Arquivo de serviço                                          | URL configurada                         |
|-------------------------------------------------------------|-----------------------------------------|
| `src/app/animal/animal.service.ts`                          | `http://localhost:8080/api/animais`     |
| `src/app/usuario/ong/ong.service.ts`                        | `http://localhost:8080/api/ongs`        |
| `src/app/usuario/protetor/protetor.service.ts`              | `http://localhost:8080/api/protetores`  |
| `src/app/usuario/adotante/adotante.service.ts`              | `http://localhost:8080/api/adotantes`   |
| `src/app/solicitacao/solicitacao.service.ts`                | `http://localhost:8080/api/solicitacoes`|
| `src/app/adocao/adocao.service.ts`                          | `http://localhost:8080/api/adocoes`     |

Altere a constante `apiUrl` em cada serviço caso o backend esteja em outro host ou porta.

## Telas e rotas

| Rota                        | Tela                                          |
|-----------------------------|-----------------------------------------------|
| `/animais`                  | Listagem de animais com filtros               |
| `/animais/novo`             | Formulário de cadastro de animal              |
| `/animais/editar/:id`       | Formulário de edição de animal                |
| `/ongs`                     | Listagem de ONGs                              |
| `/ongs/nova`                | Formulário de cadastro de ONG                 |
| `/ongs/editar/:id`          | Formulário de edição de ONG                   |
| `/protetores`               | Listagem de protetores                        |
| `/protetores/novo`          | Formulário de cadastro de protetor            |
| `/protetores/editar/:id`    | Formulário de edição de protetor              |
| `/adotantes`                | Listagem de adotantes                         |
| `/adotantes/novo`           | Formulário de cadastro de adotante            |
| `/adotantes/editar/:id`     | Formulário de edição de adotante              |
| `/solicitacoes`             | Listagem de solicitações com ações de fluxo   |
| `/solicitacoes/nova`        | Formulário de nova solicitação                |
| `/adocoes`                  | Listagem de adoções confirmadas               |
| `/adocoes/nova`             | Formulário de confirmação de adoção           |

A rota `/` redireciona automaticamente para `/animais`.

## Estrutura de pastas

```
src/app/
├── shared/
│   └── navbar/              # Barra lateral de navegação
├── animal/
│   ├── animal.model.ts      # Interface TypeScript do Animal
│   ├── animal.service.ts    # Chamadas HTTP para /api/animais
│   ├── animal-options.ts    # Listas de opções (espécie, porte, etc.)
│   ├── animal-list/         # Tela de listagem com filtros por coluna
│   └── animal-form/         # Formulário de cadastro/edição
├── usuario/
│   ├── ong/
│   │   ├── ong.model.ts
│   │   ├── ong.service.ts
│   │   ├── ong-list/        # Listagem com filtro por razão social, CNPJ, e-mail
│   │   └── ong-form/
│   ├── protetor/
│   │   ├── protetor-list/   # Listagem com filtro por nome, CPF, e-mail
│   │   └── protetor-form/
│   └── adotante/
│       ├── adotante-list/   # Listagem com filtro por nome, CPF, e-mail
│       └── adotante-form/
├── solicitacao/
│   ├── solicitacao.model.ts
│   ├── solicitacao.service.ts
│   ├── solicitacao-list/    # Listagem com filtro por status; ações Aprovar/Recusar/Cancelar
│   └── solicitacao-form/    # Formulário de nova solicitação (Animal + Adotante + Mensagem)
└── adocao/
    ├── adocao.model.ts
    ├── adocao.service.ts
    ├── adocao-list/         # Listagem com filtro por data e responsável
    └── adocao-form/         # Confirmação de entrega (Solicitação + Responsável + Data)
```

## Padrões do projeto

**Componentes standalone** — todos os componentes usam `standalone: true`, sem NgModules.

**Signals para estado reativo** — dados exibidos nas listas são armazenados em `signal<T[]>([])`. O array original fica em uma propriedade privada `_todos: T[]` para suportar filtragem client-side sem nova chamada à API.

**Filtragem client-side** — todas as telas de listagem carregam os dados completos na inicialização e filtram localmente no array em memória. Isso simplifica a implementação dado o volume reduzido de dados do projeto.

**Design system global** — classes de layout (`page-wrapper`, `page-header`, `card-title`, `filtros-campos`, `filtros-rodape`, `empty-state`, `acoes`, `form-row`, `form-field`) estão definidas em `src/styles.css` e compartilhadas por todas as telas.
