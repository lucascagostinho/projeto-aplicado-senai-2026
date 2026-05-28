package br.senai.apoiopet.solicitacao;

import br.senai.apoiopet.usuario.adotante.Adotante;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoMapper {

    public SolicitacaoResponseDTO toResponse(SolicitacaoAdocao s) {
        String adotanteNome = null;
        if (s.getAdotante() instanceof Adotante adotante) {
            adotanteNome = adotante.getNome();
        }
        return new SolicitacaoResponseDTO(
                s.getId(),
                s.getAnimal().getId(),
                s.getAnimal().getEspecie() != null ? s.getAnimal().getEspecie().name() : null,
                s.getAnimal().getRaca(),
                s.getAdotante().getId(),
                adotanteNome,
                s.getStatus(),
                s.getMensagem(),
                s.getJustificativa(),
                s.getCriadoEm(),
                s.getAtualizadoEm()
        );
    }
}
