package br.senai.apoiopet.adocao;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AdocaoRequestDTO(
        @NotNull Long solicitacaoId,
        @NotNull Long confirmadoPorId,
        @NotNull LocalDate dataAdocao
) {}
