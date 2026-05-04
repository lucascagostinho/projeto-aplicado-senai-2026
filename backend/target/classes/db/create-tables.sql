-- Script de criação do banco de dados — Sistema Apoio Pet
-- Projeto Aplicado II — Equipe 6 — SENAI SC
-- Executar conectado ao banco: apoio_pet

CREATE TABLE IF NOT EXISTS animal (
    id              BIGSERIAL       PRIMARY KEY,
    especie         VARCHAR(20)     NOT NULL,
    raca            VARCHAR(100),
    sexo            VARCHAR(10)     NOT NULL,
    faixa_etaria    VARCHAR(15)     NOT NULL,
    porte           VARCHAR(10)     NOT NULL,
    cidade          VARCHAR(100)    NOT NULL,
    estado          VARCHAR(2)      NOT NULL,
    castrado        BOOLEAN         NOT NULL,
    vacinado        BOOLEAN         NOT NULL,
    status          VARCHAR(20)     NOT NULL DEFAULT 'disponivel',
    cor             VARCHAR(50),
    foto            VARCHAR(255),
    caracteristicas TEXT,
    criado_em       TIMESTAMP       NOT NULL DEFAULT NOW()
);
