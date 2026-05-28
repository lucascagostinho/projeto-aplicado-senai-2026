-- Script de criação do banco de dados — Sistema Apoio Pet
-- Projeto Aplicado II — Equipe 6 — SENAI SC
-- Executar conectado ao banco: apoio_pet

-- ============================================================
-- TABELA BASE DE USUÁRIOS (hierarquia JOINED)
-- ============================================================
CREATE TABLE IF NOT EXISTS usuario (
    id        BIGSERIAL    PRIMARY KEY,
    email     VARCHAR(150) NOT NULL UNIQUE,
    senha     VARCHAR(255) NOT NULL,
    telefone  VARCHAR(20),
    cep       VARCHAR(10),
    tipo      VARCHAR(20)  NOT NULL,
    criado_em TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ong (
    usuario_id   BIGINT       PRIMARY KEY REFERENCES usuario(id),
    razao_social VARCHAR(150) NOT NULL,
    cnpj         VARCHAR(18)  UNIQUE,
    descricao    TEXT
);

CREATE TABLE IF NOT EXISTS protetor (
    usuario_id BIGINT      PRIMARY KEY REFERENCES usuario(id),
    nome       VARCHAR(100) NOT NULL,
    cpf        VARCHAR(14)  UNIQUE
);

CREATE TABLE IF NOT EXISTS adotante (
    usuario_id BIGINT      PRIMARY KEY REFERENCES usuario(id),
    nome       VARCHAR(100) NOT NULL,
    cpf        VARCHAR(14)  UNIQUE
);

-- ============================================================
-- ANIMAL (instalação limpa já inclui responsavel_id)
-- ============================================================
CREATE TABLE IF NOT EXISTS animal (
    id              BIGSERIAL    PRIMARY KEY,
    especie         VARCHAR(20)  NOT NULL,
    raca            VARCHAR(100),
    sexo            VARCHAR(10)  NOT NULL,
    faixa_etaria    VARCHAR(15)  NOT NULL,
    porte           VARCHAR(10)  NOT NULL,
    cidade          VARCHAR(100) NOT NULL,
    estado          VARCHAR(2)   NOT NULL,
    castrado        BOOLEAN      NOT NULL,
    vacinado        BOOLEAN      NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'DISPONIVEL',
    cor             VARCHAR(50),
    foto            VARCHAR(255),
    caracteristicas TEXT,
    criado_em       TIMESTAMP    NOT NULL DEFAULT NOW(),
    responsavel_id  BIGINT       REFERENCES usuario(id)
);

-- Migração idempotente para banco já existente sem a coluna
ALTER TABLE animal ADD COLUMN IF NOT EXISTS responsavel_id BIGINT REFERENCES usuario(id);

-- ============================================================
-- SOLICITAÇÃO DE ADOÇÃO
-- ============================================================
CREATE TABLE IF NOT EXISTS solicitacao_adocao (
    id            BIGSERIAL   PRIMARY KEY,
    animal_id     BIGINT      NOT NULL REFERENCES animal(id),
    adotante_id   BIGINT      NOT NULL REFERENCES usuario(id),
    status        VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    mensagem      TEXT,
    justificativa TEXT,
    criado_em     TIMESTAMP   NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- ============================================================
-- ADOÇÃO (confirmação de entrega física)
-- ============================================================
CREATE TABLE IF NOT EXISTS adocao (
    id                BIGSERIAL PRIMARY KEY,
    solicitacao_id    BIGINT    NOT NULL UNIQUE REFERENCES solicitacao_adocao(id),
    animal_id         BIGINT    NOT NULL REFERENCES animal(id),
    confirmado_por_id BIGINT    NOT NULL REFERENCES usuario(id),
    data_adocao       DATE      NOT NULL,
    criado_em         TIMESTAMP NOT NULL DEFAULT NOW()
);
