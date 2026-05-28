package br.senai.apoiopet.usuario.protetor;

import org.springframework.stereotype.Component;

@Component
public class ProtetorMapper {

    public ProtetorResponseDTO toResponse(Protetor p) {
        return new ProtetorResponseDTO(
                p.getId(),
                p.getEmail(),
                p.getTelefone(),
                p.getCep(),
                p.getNome(),
                p.getCpf(),
                p.getCriadoEm()
        );
    }

    public Protetor toEntity(ProtetorRequestDTO dto) {
        Protetor p = new Protetor();
        p.setEmail(dto.email());
        p.setSenha(dto.senha());
        p.setTelefone(dto.telefone());
        p.setCep(dto.cep());
        p.setNome(dto.nome());
        p.setCpf(dto.cpf());
        return p;
    }
}
