package br.senai.apoiopet.adocao;

import br.senai.apoiopet.usuario.ong.Ong;
import br.senai.apoiopet.usuario.protetor.Protetor;
import org.springframework.stereotype.Component;

@Component
public class AdocaoMapper {

    public AdocaoResponseDTO toResponse(Adocao a) {
        String confirmadoPorNome = null;
        if (a.getConfirmadoPor() instanceof Ong ong) {
            confirmadoPorNome = ong.getRazaoSocial();
        } else if (a.getConfirmadoPor() instanceof Protetor protetor) {
            confirmadoPorNome = protetor.getNome();
        }
        return new AdocaoResponseDTO(
                a.getId(),
                a.getSolicitacao().getId(),
                a.getAnimal().getId(),
                a.getAnimal().getEspecie() != null ? a.getAnimal().getEspecie().name() : null,
                a.getAnimal().getRaca(),
                a.getConfirmadoPor().getId(),
                confirmadoPorNome,
                a.getDataAdocao(),
                a.getCriadoEm()
        );
    }
}
