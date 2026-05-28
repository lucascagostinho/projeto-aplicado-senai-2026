package br.senai.apoiopet.usuario.adotante;

import java.time.LocalDateTime;

public record AdotanteResponseDTO(
        Long id,
        String email,
        String telefone,
        String cep,
        String nome,
        String cpf,
        LocalDateTime criadoEm
) {}
