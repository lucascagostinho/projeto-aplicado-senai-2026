package br.senai.apoiopet.adocao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdocaoResponseDTO(
        Long id,
        Long solicitacaoId,
        Long animalId,
        String animalEspecie,
        String animalRaca,
        Long confirmadoPorId,
        String confirmadoPorNome,
        LocalDate dataAdocao,
        LocalDateTime criadoEm
) {}
