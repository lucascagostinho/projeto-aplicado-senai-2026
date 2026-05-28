package br.senai.apoiopet.usuario.protetor;

import java.time.LocalDateTime;

public record ProtetorResponseDTO(
        Long id,
        String email,
        String telefone,
        String cep,
        String nome,
        String cpf,
        LocalDateTime criadoEm
) {}
