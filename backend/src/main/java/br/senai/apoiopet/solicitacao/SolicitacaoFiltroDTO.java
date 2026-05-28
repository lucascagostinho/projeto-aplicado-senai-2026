package br.senai.apoiopet.solicitacao;

public record SolicitacaoFiltroDTO(
        Long animalId,
        Long adotanteId,
        String status
) {}
