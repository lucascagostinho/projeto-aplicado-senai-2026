package br.senai.apoiopet.usuario.ong;

import java.time.LocalDateTime;

public record OngResponseDTO(
        Long id,
        String email,
        String telefone,
        String cep,
        String razaoSocial,
        String cnpj,
        String descricao,
        LocalDateTime criadoEm
) {}
