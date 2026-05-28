package br.senai.apoiopet.usuario.adotante;

import org.springframework.stereotype.Component;

@Component
public class AdotanteMapper {

    public AdotanteResponseDTO toResponse(Adotante a) {
        return new AdotanteResponseDTO(
                a.getId(),
                a.getEmail(),
                a.getTelefone(),
                a.getCep(),
                a.getNome(),
                a.getCpf(),
                a.getCriadoEm()
        );
    }

    public Adotante toEntity(AdotanteRequestDTO dto) {
        Adotante a = new Adotante();
        a.setEmail(dto.email());
        a.setSenha(dto.senha());
        a.setTelefone(dto.telefone());
        a.setCep(dto.cep());
        a.setNome(dto.nome());
        a.setCpf(dto.cpf());
        return a;
    }
}
