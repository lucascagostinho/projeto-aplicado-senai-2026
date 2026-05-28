package br.senai.apoiopet.solicitacao;

import java.time.LocalDateTime;

public record SolicitacaoResponseDTO(
        Long id,
        Long animalId,
        String animalEspecie,
        String animalRaca,
        Long adotanteId,
        String adotanteNome,
        SolicitacaoStatus status,
        String mensagem,
        String justificativa,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
