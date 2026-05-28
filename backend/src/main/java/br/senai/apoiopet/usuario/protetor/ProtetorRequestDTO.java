package br.senai.apoiopet.usuario.protetor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProtetorRequestDTO(
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 255) String senha,
        @Size(max = 20) String telefone,
        @Size(max = 10) String cep,
        @NotBlank @Size(max = 100) String nome,
        @Size(max = 14) String cpf
) {}
