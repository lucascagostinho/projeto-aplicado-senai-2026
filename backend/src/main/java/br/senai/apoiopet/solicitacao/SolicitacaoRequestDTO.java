package br.senai.apoiopet.solicitacao;

import jakarta.validation.constraints.NotNull;

public record SolicitacaoRequestDTO(
        @NotNull Long animalId,
        @NotNull Long adotanteId,
        String mensagem
) {}
