package br.senai.apoiopet.solicitacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<SolicitacaoAdocao, Long> {

    List<SolicitacaoAdocao> findByAnimalId(Long animalId);

    List<SolicitacaoAdocao> findByAdotanteId(Long adotanteId);

    List<SolicitacaoAdocao> findByAnimalIdAndStatusIn(Long animalId, List<SolicitacaoStatus> statuses);

    boolean existsByAnimalIdAndAdotanteIdAndStatusIn(Long animalId, Long adotanteId,
                                                      List<SolicitacaoStatus> statuses);
}
