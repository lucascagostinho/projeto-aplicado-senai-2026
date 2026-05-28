package br.senai.apoiopet.usuario.ong;

import org.springframework.stereotype.Component;

@Component
public class OngMapper {

    public OngResponseDTO toResponse(Ong ong) {
        return new OngResponseDTO(
                ong.getId(),
                ong.getEmail(),
                ong.getTelefone(),
                ong.getCep(),
                ong.getRazaoSocial(),
                ong.getCnpj(),
                ong.getDescricao(),
                ong.getCriadoEm()
        );
    }

    public Ong toEntity(OngRequestDTO dto) {
        Ong ong = new Ong();
        ong.setEmail(dto.email());
        ong.setSenha(dto.senha());
        ong.setTelefone(dto.telefone());
        ong.setCep(dto.cep());
        ong.setRazaoSocial(dto.razaoSocial());
        ong.setCnpj(dto.cnpj());
        ong.setDescricao(dto.descricao());
        return ong;
    }
}
