# Análise Crítica da Documentação de Requisitos
## Sistema Apoio Pet — Projeto Aplicado II — AV02 — Equipe 6

| Campo             | Valor                                                      |
|-------------------|------------------------------------------------------------|
| Documento Analisado | Projeto Aplicado II - AV02 - Equipe 6 - Apoio Pet.pdf   |
| Versão Analisada  | 8.0 (28/04/2026)                                           |
| Papel do Analista | Analista Sênior — Java 21 / Spring / Angular / Req. ER     |
| Data da Análise   | 28/04/2026                                                 |
| Referências       | Livro 1 e 2 de Engenharia de Requisitos - 2025             |

---

## 1. Comentário Geral da Análise

A documentação da Equipe 6 demonstra evolução considerável ao longo das oito versões registradas no histórico, partindo de um escopo genérico até alcançar um conjunto estruturado de 17 Requisitos Funcionais, 9 Requisitos Não-Funcionais e um Diagrama MER coerente com o domínio. O contexto do problema está bem descrito, com embasamento em dados reais (Instituto Pet Brasil), o que confere legitimidade à proposta de valor do sistema.

No entanto, a documentação ainda apresenta **três problemas estruturais críticos** apontados pelo professor, que precisam ser corrigidos antes do início do desenvolvimento técnico. Essas falhas — que são típicas de equipes em formação — não comprometem o entendimento do negócio, mas criam ambiguidade no momento da implementação e dificultam o controle de qualidade por critérios objetivos.

**Pontos positivos reconhecidos:**
- Escopo bem delimitado e justificado com dados estatísticos relevantes
- MER com modelagem de herança de usuário (tabela base + especializações) adequada para o domínio
- Uso de status bem definidos para o ciclo de vida do animal e das solicitações
- RNFs com métricas mensuráveis (tempo em segundos, tamanho em MB)
- Histórico de versões detalhado, evidenciando processo iterativo de refinamento
- Separação clara entre listagem pública e gerenciamento autenticado (RF04)
- Sistema de notificações por e-mail com eventos mapeados (RF08)

**Resumo dos problemas a corrigir:**
1. RFs agrupam atores distintos (ONG, Protetor, Adotante) no mesmo requisito
2. RFs são muito verbosos e contêm detalhes técnicos que não pertencem a um RF
3. Regras de Negócio estão embutidas nos RFs, violando o princípio de separação de preocupações em ER

---

## 2. Problemas Identificados

### Problema 1 — RFs não separados por perfil de ator

**Descrição:** Diversos RFs tratam múltiplos atores simultaneamente, sem deixar claro quais ações pertencem a cada perfil. Segundo as boas práticas de Engenharia de Requisitos, cada requisito funcional deve estar vinculado a um ator específico, pois isso é a base para o controle de acesso, os casos de uso e o design das interfaces.

**Impacto no desenvolvimento:** O time de desenvolvimento em Java/Spring precisará inferir qual perfil tem acesso a cada endpoint. Em Angular, a proteção de rotas por perfil (route guards) ficará ambígua sem essa separação na documentação.

**Exemplos encontrados no documento:**

| RF   | Problema de separação por ator                                                                 |
|------|-----------------------------------------------------------------------------------------------|
| RF01 | Trata cadastro de ONG, Protetor e Adotante como um único requisito                           |
| RF02 | Login descrito para todos os perfis em um único RF                                           |
| RF04 | Mistura listagem pública (qualquer pessoa) com gerenciamento autenticado (ONG/Protetor)       |
| RF07 | Chat descrito para "adotante e responsável" sem separar as responsabilidades de cada ator     |
| RF08 | Notificações descritas para múltiplos atores sem identificar quem dispara e quem recebe       |

**Solução esperada:** Organizar os RFs em seções por ator:
- `RF-PUB-xx` — Público (não autenticado)
- `RF-ONG-xx` — ONG/Instituição
- `RF-PRO-xx` — Protetor Independente
- `RF-ADO-xx` — Adotante
- `RF-CMN-xx` — Funcionalidades comuns a ONG e Protetor (quando idênticas)

---

### Problema 2 — RFs muito verbosos com detalhes técnicos e de implementação

**Descrição:** Um Requisito Funcional deve descrever **O QUÊ** o sistema deve fazer (comportamento observável pelo usuário), e nunca **COMO** ele deve ser implementado. Detalhes como nomes de tabelas, campos de banco de dados, ordenação de listas e referências cruzadas entre RFs pertencem ao Dicionário de Dados, Especificação de Casos de Uso ou documentação de arquitetura.

**Exemplos encontrados no documento:**

| RF   | Trecho problemático                                                                            | Por que é incorreto                                      |
|------|-----------------------------------------------------------------------------------------------|----------------------------------------------------------|
| RF01 | "utilizando uma tabela base compartilhada com campos comuns (e-mail, senha, telefone, CEP...)"| Detalhe de arquitetura de banco de dados, não RF        |
| RF01 | "tabelas especializadas para cada perfil"                                                     | Decisão de design de BD, não requisito funcional        |
| RF03 | Lista completa de campos: "espécie (cão/gato), raça, sexo (macho/fêmea), faixa etária..."    | Pertence ao Dicionário de Dados                         |
| RF03 | "Os campos castrado e vacinado são preenchidos pelo responsável; o detalhamento... fica no RF10" | Referência cruzada entre RFs — deve ser Regra de Negócio |
| RF04 | "Sem filtros ativos, a listagem exibe todos os animais... ordenados por data de cadastro decrescente" | Comportamento padrão de UI — detalhe de UX              |
| RF05 | Fluxo completo de estados com múltiplas ações e condições em um único RF                     | Deve ser atomizado em múltiplos RFs + RNs               |
| RF14 | Lista todos os campos do relatório dentro do RF                                               | Pertence ao Dicionário de Dados / especificação de relatório |

**Solução esperada:** Cada RF deve ter no máximo 2-3 linhas descrevendo o comportamento principal. Campos, formatos e detalhes técnicos devem ser movidos para artefatos auxiliares.

---

### Problema 3 — Regras de Negócio embutidas nos Requisitos Funcionais

**Descrição:** Regras de Negócio (RN) são políticas, restrições e invariantes do domínio que governam como o negócio opera. Elas **não são requisitos funcionais** — são restrições que os requisitos funcionais devem respeitar. Misturá-las dentro dos RFs torna os requisitos longos, difíceis de manter e impossíveis de rastrear individualmente.

**Regras de Negócio encontradas dentro dos RFs:**

| RF   | Trecho com Regra de Negócio                                                                              | Tipo de RN                      |
|------|----------------------------------------------------------------------------------------------------------|---------------------------------|
| RF05 | "O sistema deve bloquear novas solicitações para animais com status 'adotado' ou 'indisponivel'"         | Restrição de estado             |
| RF05 | "impedir que o mesmo adotante envie nova solicitação se já possui uma com status 'pendente' ou 'aprovada'" | Restrição de unicidade          |
| RF05 | "ao cancelar uma solicitação 'aprovada', o animal retorna automaticamente para 'disponivel'"             | Transição automática de estado  |
| RF06 | "o animal muda para 'em_processo' e as demais solicitações 'pendentes' do mesmo animal são automaticamente 'recusadas'" | Transição automática de estado |
| RF06 | "Ao alterar o status do animal para 'indisponivel', todas as suas solicitações 'pendentes' e 'aprovadas' são automaticamente 'canceladas'" | Transição em cascata |
| RF09 | "O sistema deve bloquear essa ação caso não exista uma solicitação com status 'aprovada' vinculada ao animal" | Pré-condição de negócio       |
| RF11 | "Apenas o autor do conteúdo pode editá-lo ou removê-lo"                                                  | Regra de autorização            |
| RF12 | "Apenas o responsável pelo cadastro da campanha pode editá-la ou removê-la"                              | Regra de autorização            |

**Solução esperada:** Criar seção dedicada **"Regras de Negócio (RN)"** no documento, numeradas (RN01, RN02...) e referenciadas pelos RFs quando necessário. O RF deve apenas descrever a capacidade, e a RN impõe a restrição.

---

### Problema 4 — Outros achados menores

| ID  | Problema                                                                                      | RF Afetado |
|-----|-----------------------------------------------------------------------------------------------|------------|
| P4a | RF usa linguagem negativa: "O sistema **não** realiza cadastro, armazenamento ou encaminhamento de denúncias" — isso não é um requisito, é uma delimitação de escopo | RF13 |
| P4b | RF02 mistura RF com RNF: "Tentativas com credenciais inválidas devem retornar mensagem de erro" é critério de usabilidade (RNF) | RF02 |
| P4c | RF08 é infraestrutura transversal; como está, mistura eventos de negócio com mecanismo técnico de entrega (e-mail) | RF08 |
| P4d | Ausência de seção de **Glossário** — termos como "adotante", "protetor", "em_processo", "disponivel" não estão formalmente definidos | Geral |
| P4e | Ausência de seção de **Regras de Negócio** — impede rastreabilidade das transições de estado | Geral |
| P4f | RFs não possuem **critério de aceitação** individual — impossível validar cada RF isoladamente | Geral |

---

## 3. Análise Detalhada — RF por RF

### Legenda de Status
- **OK** — Requisito bem escrito, apenas ajustes cosméticos
- **Ajustar** — Necessita extração de RN ou redução de verbosidade
- **Reestruturar** — Deve ser dividido em múltiplos RFs e/ou reorganizado por ator

---

### RF01 — Cadastro de Usuários em Três Perfis Distintos
**Status:** `Reestruturar`

**Problemas:**
- Trata três atores distintos em um único RF
- Cita "tabela base compartilhada" — decisão arquitetural, não RF
- Lista campos completos de cada perfil — pertence ao Dicionário de Dados

**Sugestão de reescrita:**
```
RF-ONG-01: O sistema deve permitir o cadastro de uma ONG/Instituição com
           razão social, CNPJ e dados de contato e endereço.

RF-PRO-01: O sistema deve permitir o cadastro de um Protetor Independente
           com nome completo, CPF e dados de contato e endereço.

RF-ADO-01: O sistema deve permitir o cadastro de um Adotante com nome
           completo, CPF e dados de contato e endereço.
```

---

### RF02 — Login e Autenticação
**Status:** `Ajustar`

**Problemas:**
- O RF é válido para todos os perfis, mas poderia ser explicitado como compartilhado
- "Tentativas com credenciais inválidas devem retornar mensagem de erro" → RNF de usabilidade
- "direcionar para a interface correspondente ao seu perfil" → pode ser Regra de Negócio (RN)

**Sugestão de reescrita:**
```
RF-CMN-01: O sistema deve permitir que usuários cadastrados realizem
           autenticação com e-mail e senha, sendo redirecionados para
           a interface correspondente ao seu perfil após o login.
```
Mover para RNF: feedback de erro em credenciais inválidas.

---

### RF03 — Cadastro de Animais para Adoção
**Status:** `Ajustar`

**Problemas:**
- Exclusivo de ONG e Protetor — deve estar nessas seções
- Lista completa de campos técnicos — pertence ao Dicionário de Dados
- "Os campos castrado e vacinado são preenchidos pelo responsável; o detalhamento fica no RF10" → Regra de Negócio (RN)

**Sugestão de reescrita:**
```
RF-ONG-02 / RF-PRO-02: O sistema deve permitir o cadastro de animais
                        disponíveis para adoção, com informações sobre
                        espécie, características físicas, localização,
                        saúde e foto.
```

---

### RF04 — Listagem Pública de Animais com Filtros
**Status:** `Reestruturar`

**Problemas:**
- Mistura dois contextos: listagem pública (qualquer usuário) e gerenciamento autenticado (ONG/Protetor)
- Comportamento de ordenação padrão e lógica de combinação de filtros → Regras de Negócio e UX
- "ordenados por data de cadastro decrescente" → detalhe de UI

**Sugestão de reescrita:**
```
RF-PUB-01: O sistema deve exibir, sem necessidade de login, a listagem
           paginada de animais disponíveis para adoção, com filtros por
           espécie, porte, faixa etária, sexo, localização, castrado e vacinado.

RF-ONG-03 / RF-PRO-03: O sistema deve permitir que ONGs e Protetores
                        autenticados visualizem e gerenciem todos os seus
                        animais cadastrados, independente do status.
```

---

### RF05 — Solicitação de Interesse para Adoção
**Status:** `Reestruturar`

**Problemas:**
- Exclusivo do Adotante — deve estar na seção correspondente
- Contém ao menos 4 Regras de Negócio distintas embutidas
- Mistura envio de solicitação, restrições de envio e cancelamento em um único RF

**Sugestão de reescrita:**
```
RF-ADO-02: O sistema deve permitir que o Adotante autenticado envie uma
           solicitação de interesse para um animal disponível, com
           mensagem opcional.

RF-ADO-03: O sistema deve permitir que o Adotante cancele uma solicitação
           de interesse enviada por ele.

RF-ADO-04: O sistema deve permitir que o Adotante consulte o histórico
           e status atual de todas as suas solicitações.
```
Extrair para RNs: bloqueio de reenvio, retorno automático de status ao cancelar aprovada.

---

### RF06 — Gerenciamento de Solicitações por ONG/Protetor
**Status:** `Reestruturar`

**Problemas:**
- Contém ao menos 5 Regras de Negócio (transições automáticas em cascata)
- Três ações distintas (aprovar, recusar, cancelar) poderiam ser RFs separados
- As transições de status do animal e das demais solicitações são Regras de Negócio puras

**Sugestão de reescrita:**
```
RF-ONG-04 / RF-PRO-04: O sistema deve permitir que ONG/Protetor aprove
                        uma solicitação de interesse pendente para um
                        animal sob sua responsabilidade.

RF-ONG-05 / RF-PRO-05: O sistema deve permitir que ONG/Protetor recuse
                        uma solicitação de interesse pendente, com
                        justificativa obrigatória.

RF-ONG-06 / RF-PRO-06: O sistema deve permitir que ONG/Protetor cancele
                        uma solicitação previamente aprovada, com
                        justificativa obrigatória.
```
Extrair para RNs: aprovação gera recusa automática das demais, mudança para indisponível cancela todas.

---

### RF07 — Chat Interno de Comunicação
**Status:** `Ajustar`

**Problemas:**
- Descreve o chat mas não deixa claro quem inicia a conversa
- "Após a confirmação da adoção, o chat é encerrado e passa a ser somente leitura" → Regra de Negócio

**Sugestão de reescrita:**
```
RF-ADO-05: O sistema deve permitir que o Adotante envie e receba
           mensagens no chat vinculado à sua solicitação de interesse.

RF-ONG-07 / RF-PRO-07: O sistema deve permitir que ONG/Protetor envie e
                        receba mensagens no chat vinculado às solicitações
                        dos animais sob sua responsabilidade.
```
Extrair para RN: encerramento do chat após confirmação da adoção.

---

### RF08 — Notificações por E-mail
**Status:** `Ajustar`

**Problemas:**
- É um requisito transversal (infraestrutura de notificação) que serve múltiplos RFs
- A lista de eventos está correta, mas o RF poderia ser mais sucinto se referenciar os RFs de origem

**Sugestão de reescrita:**
```
RF-CMN-02: O sistema deve enviar notificações automáticas por e-mail aos
           atores envolvidos nas seguintes situações: nova solicitação de
           interesse, mudança de status da solicitação (aprovação, recusa,
           cancelamento) e nova mensagem no chat.
```

---

### RF09 — Confirmação da Entrega do Animal
**Status:** `Ajustar`

**Problemas:**
- Exclusivo de ONG/Protetor — deve estar nessas seções
- "O sistema deve bloquear essa ação caso não exista uma solicitação com status 'aprovada'" → Regra de Negócio (pré-condição)

**Sugestão de reescrita:**
```
RF-ONG-08 / RF-PRO-08: O sistema deve permitir que ONG/Protetor registre
                        a confirmação da entrega física do animal ao
                        Adotante, informando a data da adoção.
```
Extrair para RN: pré-condição de existência de solicitação aprovada; mudança automática de status para "adotado".

---

### RF10 — Histórico de Saúde Pré-Adoção
**Status:** `OK com ajuste menor`

**Pontos positivos:** RF bem escrito, ação clara, ator explícito.

**Ajuste menor:** Remover a referência ao RF03 que aparece implicitamente. O histórico de saúde deve ser um artefato independente.

**Sugestão de reescrita:**
```
RF-ONG-09 / RF-PRO-09: O sistema deve permitir que ONG/Protetor cadastre,
                        edite e remova registros do histórico de saúde
                        de um animal, incluindo tipo de procedimento, data
                        e descrição.
```

---

### RF11 — Conteúdo Educativo
**Status:** `Ajustar`

**Problemas:**
- "Apenas o autor do conteúdo pode editá-lo ou removê-lo" → Regra de Negócio de autorização
- RF de acesso público (conteúdo publicado visível sem login) deveria ser separado

**Sugestão de reescrita:**
```
RF-ONG-10 / RF-PRO-10: O sistema deve permitir que ONG/Protetor cadastre,
                        edite e remova conteúdo educativo, com título,
                        texto, categoria e status (rascunho/publicado).

RF-PUB-02: O sistema deve exibir publicamente, sem necessidade de login,
           os conteúdos educativos com status "publicado".
```
Extrair para RN: apenas o autor pode editar ou remover seu conteúdo.

---

### RF12 — Campanhas de Castração, Vacinação e Eventos de Adoção
**Status:** `Ajustar`

**Problemas:**
- "Apenas o responsável pelo cadastro da campanha pode editá-la ou removê-la" → Regra de Negócio
- "O mural é público e informativo" pode ser RF separado de acesso público

**Sugestão de reescrita:**
```
RF-ONG-11 / RF-PRO-11: O sistema deve permitir que ONG/Protetor cadastre,
                        edite e remova campanhas de castração, vacinação
                        e eventos de adoção, com título, tipo, data,
                        localização e contato.

RF-PUB-03: O sistema deve exibir publicamente, sem necessidade de login,
           as campanhas cadastradas na plataforma.
```
Extrair para RN: apenas o responsável pelo cadastro pode editar ou remover a campanha.

---

### RF13 — Página Informativa de Denúncias
**Status:** `Ajustar`

**Problemas:**
- Usa linguagem negativa ("O sistema **não** realiza cadastro...") — delimitação de escopo não é RF
- A delimitação deve constar no Escopo do documento, não como RF

**Sugestão de reescrita:**
```
RF-PUB-04: O sistema deve disponibilizar uma página com link de
           redirecionamento para o canal oficial de denúncias de abandono
           ou maus-tratos do município ou estado correspondente.
```

---

### RF14 — Relatório de Adoções
**Status:** `OK com ajuste`

**Ajuste:** Remover a lista de campos do RF ("nome e ID do animal, espécie e raça...") — pertence ao Dicionário de Dados / especificação do relatório.

**Sugestão de reescrita:**
```
RF-ONG-12 / RF-PRO-12: O sistema deve gerar relatório de adoções
                        realizadas, filtrado por período, acessível
                        apenas para os animais sob responsabilidade
                        do usuário autenticado.
```

---

### RF15 — Relatório de Campanhas
**Status:** `OK`

RF bem escrito, sucinto e com ator identificado. Manter com ajuste de seção (mover para RF-CMN).

---

### RF16 — Dashboard de Métricas
**Status:** `OK`

RF bem escrito. Manter com ajuste de seção (mover para RF-CMN).

---

### RF17 — Página de Perguntas Frequentes (FAQ)
**Status:** `OK`

RF bem escrito, público. Mover para RF-PUB.

---

## 4. Análise do Diagrama MER

### Pontos Positivos
- Modelagem de herança por FK com tabela base `usuario` + tabelas `ong`, `protetor`, `adotante` é adequada para o domínio
- Entidade `adocao` separada de `solicitacao_adocao` — boa decisão para registrar o fato da entrega
- Campos de auditoria presentes: `criado_em` em `animal` e `mensagem`, `enviado_em` em `mensagem`
- Relacionamentos principais corretos e coerentes com os RFs

### Pontos de Atenção

| ID   | Entidade/Campo          | Problema Identificado                                                                 | Sugestão                                                |
|------|-------------------------|---------------------------------------------------------------------------------------|---------------------------------------------------------|
| MER1 | `ong`, `protetor`       | Possuem `id` E `usuario_id` — redundância em herança por FK (o `id` já é o FK)      | Usar apenas `usuario_id` como PK e FK                  |
| MER2 | `animal.responsavel_id` | FK genérica sem especificar se aponta para `ong.id` ou `protetor.id`                 | Criar FK explícita ou usar campo `responsavel_tipo`     |
| MER3 | `adocao.confirmado_por` | Campo sem sufixo `_id` e sem FK explícita documentada                                | Renomear para `confirmado_por_id` com FK para `usuario` |
| MER4 | `animal`, `conteudo`, `campanha` | Ausência de campo `updated_at` em entidades que suportam edição             | Adicionar `atualizado_em timestamp` nessas entidades    |
| MER5 | `solicitacao_adocao.data` | Campo `date` pode perder precisão temporal; pedidos feitos no mesmo dia serão ordenados arbitrariamente | Usar `timestamp` ou `datetime`   |
| MER6 | Status em `varchar`     | Status como `varchar` permitem valores inválidos; risco de inconsistência de dados   | Usar `enum` no banco ou constraint `CHECK`             |
| MER7 | `campanha`              | Sem campo `criado_em` para auditoria e relatórios por período                        | Adicionar `criado_em timestamp not null`               |

---

## 5. Questionamentos e Dúvidas Técnicas

As questões abaixo precisam de resposta antes ou durante a revisão da documentação, pois impactam decisões de arquitetura e implementação.

| ID  | Pergunta                                                                                                    | Impacto                                              |
|-----|-------------------------------------------------------------------------------------------------------------|------------------------------------------------------|
| Q01 | O sistema diferencia permissões entre ONG e Protetor além dos campos de cadastro (CNPJ vs CPF)?             | Controle de acesso no Spring Security / JWT          |
| Q02 | O Adotante autenticado vê os mesmos filtros da listagem pública, ou tem filtros/visibilidade adicionais?    | Design da interface Angular e do endpoint REST       |
| Q03 | "em_processo" implica exclusividade total? Um animal pode ter apenas UMA solicitação aprovada por vez?      | Regra de Negócio — deve ser documentada formalmente  |
| Q04 | O chat é iniciado por quem — o Adotante após enviar a solicitação, ou o ONG/Protetor após receber?          | UX do chat e permissões de criação de mensagem       |
| Q05 | A data de adoção informada manualmente (RF09) pode ser uma data retroativa? Há validação de intervalo?      | Regra de Negócio de validação de data               |
| Q06 | O histórico de saúde (RF10) fica visível para o Adotante que ainda não adotou o animal?                    | Controle de visibilidade — impacta query e Spring    |
| Q07 | Um usuário de uma ONG pode editar conteúdo publicado por outro usuário da mesma ONG?                        | Granularidade da autorização — usuário vs. ONG       |
| Q08 | O link de denúncias (RF13) é um único link fixo configurado no sistema, ou varia por estado/município?      | Se dinâmico: necessita CRUD de configuração          |
| Q09 | O CPF do adotante aparece completo no relatório (RF14)? Há requisito de mascaramento por LGPD?              | Conformidade legal — pode adicionar RNF de segurança |
| Q10 | O Dashboard (RF16) agrega métricas de todos os usuários de uma ONG, ou apenas do usuário logado?            | Modelo de dado e query de agregação no Spring        |
| Q11 | Como o sistema implementará autenticação — JWT (stateless) ou sessão? (Impacta Sprint 1 do backend)         | Arquitetura do Spring Security                       |
| Q12 | Haverá paginação no chat? Quantas mensagens são carregadas por padrão?                                      | Performance — RNF de carregamento pode ser necessário|

---

## 6. Sugestões Estruturais para a Documentação Revisada

### 6.1. Nova Organização dos RFs

```
3. REQUISITOS FUNCIONAIS

  3.1. Usuário Público (não autenticado)
       RF-PUB-01  Listagem pública de animais com filtros
       RF-PUB-02  Visualização de conteúdo educativo publicado
       RF-PUB-03  Visualização do mural de campanhas
       RF-PUB-04  Página informativa de denúncias com link externo
       RF-PUB-05  Página de Perguntas Frequentes (FAQ)

  3.2. Comum a todos os perfis autenticados
       RF-CMN-01  Autenticação com e-mail e senha
       RF-CMN-02  Notificações automáticas por e-mail

  3.3. ONG/Instituição
       RF-ONG-01  Cadastro de ONG
       RF-ONG-02  Cadastro de animal para adoção
       RF-ONG-03  Gerenciamento dos animais cadastrados (visualização por status)
       RF-ONG-04  Aprovação de solicitação de interesse
       RF-ONG-05  Recusa de solicitação de interesse
       RF-ONG-06  Cancelamento de solicitação aprovada
       RF-ONG-07  Comunicação via chat com adotante
       RF-ONG-08  Confirmação de entrega do animal
       RF-ONG-09  Gerenciamento do histórico de saúde do animal
       RF-ONG-10  Gerenciamento de conteúdo educativo
       RF-ONG-11  Gerenciamento de campanhas e eventos
       RF-ONG-12  Relatório de adoções por período
       RF-ONG-13  Relatório de campanhas por período
       RF-ONG-14  Dashboard de métricas

  3.4. Protetor Independente
       RF-PRO-01  Cadastro de Protetor
       (RF-PRO-02 a RF-PRO-14: análogos a RF-ONG-02 a RF-ONG-14)

  3.5. Adotante
       RF-ADO-01  Cadastro de Adotante
       RF-ADO-02  Envio de solicitação de interesse para adoção
       RF-ADO-03  Cancelamento de solicitação de interesse
       RF-ADO-04  Consulta ao histórico de solicitações
       RF-ADO-05  Comunicação via chat com ONG/Protetor

4. REGRAS DE NEGÓCIO
       RN-01 a RN-xx (extraídas dos RFs acima)

5. REQUISITOS NÃO-FUNCIONAIS
       RNF-01 a RNF-09 (mantidos com ajuste menor)

6. GLOSSÁRIO

7. DIAGRAMA MER (revisado)

8. DICIONÁRIO DE DADOS
```

### 6.2. Template de Regra de Negócio

```
RN-XX — [Nome curto da regra]
Descrição: [O que a regra impõe]
Entidade(s): [animal, solicitacao_adocao, etc.]
Condição: [Quando se aplica]
Consequência: [O que acontece automaticamente]
RF Relacionado: [RF que dispara a regra]
```

### 6.3. Exemplos de RNs a serem criadas

```
RN-01 — Aprovação exclusiva de solicitação
Descrição: Ao aprovar uma solicitação de interesse, todas as demais
           solicitações pendentes do mesmo animal são automaticamente recusadas.
Entidade(s): solicitacao_adocao, animal
Condição: ONG/Protetor aprova uma solicitação com status "pendente"
Consequência: animal.status → "em_processo"; demais solicitações → "recusada"
RF Relacionado: RF-ONG-04 / RF-PRO-04

RN-02 — Cancelamento de aprovação reverte disponibilidade
Descrição: Ao cancelar uma solicitação aprovada, o animal retorna ao status disponível.
Condição: ONG/Protetor ou Adotante cancela solicitação com status "aprovada"
Consequência: animal.status → "disponivel"
RF Relacionado: RF-ONG-06 / RF-PRO-06 / RF-ADO-03

RN-03 — Indisponibilidade cancela todas as solicitações ativas
Descrição: Ao alterar o status do animal para "indisponivel", todas as
           solicitações pendentes e aprovadas são canceladas.
Condição: ONG/Protetor muda animal.status → "indisponivel"
Consequência: todas solicitacoes com status "pendente" ou "aprovada" → "cancelada"
RF Relacionado: RF-ONG-03 / RF-PRO-03

RN-04 — Pré-condição para confirmação de entrega
Descrição: A confirmação de entrega do animal só pode ser registrada se
           existir uma solicitação com status "aprovada" vinculada ao animal.
Condição: ONG/Protetor tenta confirmar entrega
Consequência: bloqueio da operação se pré-condição não atendida
RF Relacionado: RF-ONG-08 / RF-PRO-08

RN-05 — Chat encerrado após adoção confirmada
Descrição: Após a confirmação da entrega do animal, o chat da solicitação
           torna-se somente leitura.
Condição: animal.status → "adotado"
RF Relacionado: RF-ONG-07 / RF-PRO-07 / RF-ADO-05

RN-06 — Restrição de reenvio de solicitação
Descrição: Um Adotante não pode enviar nova solicitação para o mesmo
           animal se já possui uma com status "pendente" ou "aprovada".
RF Relacionado: RF-ADO-02

RN-07 — Autorização de edição de conteúdo por autoria
Descrição: Somente o usuário autor de um conteúdo educativo pode
           editá-lo ou removê-lo.
RF Relacionado: RF-ONG-10 / RF-PRO-10

RN-08 — Autorização de edição de campanha por autoria
Descrição: Somente o usuário responsável pelo cadastro de uma campanha
           pode editá-la ou removê-la.
RF Relacionado: RF-ONG-11 / RF-PRO-11
```

---

## 7. Lista de Critérios de Aceite para Validação da Documentação

Os critérios abaixo definem o que a documentação revisada deve atender **antes do início do desenvolvimento técnico**. Cada critério é objetivo e verificável por inspeção direta do documento.

---

### CA01 — Separação de RFs por perfil de ator
**Critério:** O documento possui seções distintas de RFs para: Público, ONG, Protetor, Adotante e Comum.
**Como verificar:** Inspecionar o índice/sumário — devem existir subseções por ator.
**Prioridade:** Alta (bloqueante para início do desenvolvimento)

---

### CA02 — Atomicidade dos RFs
**Critério:** Cada RF descreve exatamente uma funcionalidade do sistema. Nenhum RF contém listas de ações ou cenários alternativos.
**Como verificar:** Cada RF deve ter no máximo 3 linhas e começar com "O sistema deve permitir que [ator] [verbo de negócio]."
**Prioridade:** Alta

---

### CA03 — Ausência de Regras de Negócio nos RFs
**Critério:** Nenhum RF descreve transições automáticas de estado, pré-condições de negócio, restrições de fluxo ou cascatas automáticas.
**Como verificar:** Buscar nos RFs por termos como "automaticamente", "bloquear", "retorna para", "são canceladas", "apenas o autor".
**Prioridade:** Alta (bloqueante)

---

### CA04 — Seção de Regras de Negócio (RN) criada
**Critério:** O documento contém seção "Regras de Negócio" com ao menos as 8 RNs identificadas nesta análise, numeradas e com estrutura: Descrição, Condição, Consequência, RF Relacionado.
**Como verificar:** Contar as RNs documentadas e verificar se cobrem as transições de status do animal e das solicitações.
**Prioridade:** Alta

---

### CA05 — Ausência de detalhes técnicos/arquiteturais nos RFs
**Critério:** Nenhum RF menciona nomes de tabelas de banco de dados, tipos de campos, estruturas de herança ou tecnologias específicas.
**Como verificar:** Buscar nos RFs por termos como "tabela", "campo", "FK", "varchar", "boolean", "timestamp", "CNPJ", "CPF" (campos específicos).
**Prioridade:** Média

---

### CA06 — Linguagem positiva nos RFs
**Critério:** Todos os RFs descrevem o que o sistema **faz**, não o que o sistema **não faz**. Delimitações de escopo estão na seção de Escopo, não nos RFs.
**Como verificar:** Buscar por "O sistema não" em todos os RFs.
**Prioridade:** Média

---

### CA07 — Ator explícito em cada RF
**Critério:** Todo RF identifica o ator que inicia a ação (ONG, Protetor, Adotante, Público).
**Como verificar:** Todo RF deve conter a expressão "O sistema deve permitir que [ator]..." ou equivalente.
**Prioridade:** Alta

---

### CA08 — RNFs mensuráveis
**Critério:** Todos os RNFs de desempenho têm valores numéricos específicos. Todos os RNFs de segurança especificam os algoritmos ou padrões mínimos esperados.
**Como verificar:** Verificar se RNF01-09 possuem valores concretos (já atendem em grande parte — manter e revisar).
**Prioridade:** Baixa (já em boa forma)

---

### CA09 — Consistência MER × RFs
**Critério:** Toda entidade e atributo essencial mencionado nos RFs revisados está representado no MER. Todas as FKs do MER têm entidades de destino explícitas.
**Como verificar:** Para cada RF de cadastro, verificar se a entidade correspondente existe no MER. Para `responsavel_id` no animal: verificar se a FK aponta para ONG ou Protetor.
**Prioridade:** Alta

---

### CA10 — Glossário presente
**Critério:** O documento contém seção de Glossário com definição formal dos termos de domínio: adotante, protetor, ONG, disponível, em_processo, adotado, indisponível, solicitação de interesse, histórico de saúde.
**Como verificar:** Verificar existência da seção e a presença de ao menos 8 termos definidos.
**Prioridade:** Média

---

### CA11 — RF de Notificação claro por evento e ator
**Critério:** O RF de notificações descreve cada evento (nova solicitação, aprovação, recusa, cancelamento, nova mensagem), quem é o remetente do evento e quem recebe a notificação por e-mail.
**Como verificar:** Para cada evento listado no RF-CMN-02, deve haver clareza de ator emissor e ator receptor.
**Prioridade:** Alta

---

### CA12 — Ausência de duplicidade entre perfis
**Critério:** Funcionalidades idênticas entre ONG e Protetor estão agrupadas em RFs comuns (RF-CMN) ou explicitamente duplicadas com justificativa. Não há RFs que descrevam a mesma funcionalidade de formas diferentes para o mesmo ator.
**Como verificar:** Comparar RF-ONG-xx com RF-PRO-xx; para funções idênticas, verificar se foram movidas para RF-CMN ou marcadas como comuns.
**Prioridade:** Média

---

## 8. Resumo Executivo dos Ajustes Necessários

| Categoria           | Quantidade de Itens a Ajustar | Prioridade     |
|---------------------|-------------------------------|----------------|
| RFs a reestruturar  | RF01, RF04, RF05, RF06 (4)    | Alta           |
| RFs a ajustar       | RF02, RF03, RF07, RF08, RF09, RF11, RF12, RF13, RF14 (9) | Alta |
| RFs OK (com mínimos)| RF10, RF15, RF16, RF17 (4)    | Baixa          |
| RNs a criar         | Ao menos 8 (RN01-RN08)        | Alta           |
| MER — itens a revisar | 7 pontos (MER1-MER7)        | Média          |
| Novas seções        | Glossário, Dicionário de Dados, Regras de Negócio | Alta |
| Questões em aberto  | 12 questionamentos (Q01-Q12)  | Média-Alta     |

**Estimativa de esforço para revisão:** 1-2 dias de trabalho focado da equipe, considerando a reorganização dos RFs existentes, extração das RNs e criação das seções faltantes.

**Recomendação:** Iniciar pela criação da seção de Regras de Negócio e reorganização dos RFs por ator — esses dois ajustes têm maior impacto e permitem que os demais sigam naturalmente.

---

*Análise elaborada por: Analista Sênior — Java 21 / Spring / Angular / Engenharia de Requisitos*
*Data: 28/04/2026*
*Versão da documentação analisada: 8.0*
