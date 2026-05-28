package br.senai.apoiopet.adocao;

import br.senai.apoiopet.animal.AnimalRepository;
import br.senai.apoiopet.animal.AnimalStatus;
import br.senai.apoiopet.exception.AdocaoNotFoundException;
import br.senai.apoiopet.exception.SolicitacaoNotFoundException;
import br.senai.apoiopet.exception.SolicitacaoStatusInvalidoException;
import br.senai.apoiopet.exception.UsuarioNotFoundException;
import br.senai.apoiopet.solicitacao.SolicitacaoAdocao;
import br.senai.apoiopet.solicitacao.SolicitacaoRepository;
import br.senai.apoiopet.solicitacao.SolicitacaoStatus;
import br.senai.apoiopet.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdocaoService {

    private final AdocaoRepository repository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final AnimalRepository animalRepository;
    private final UsuarioRepository usuarioRepository;

    public AdocaoService(AdocaoRepository repository,
                         SolicitacaoRepository solicitacaoRepository,
                         AnimalRepository animalRepository,
                         UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.solicitacaoRepository = solicitacaoRepository;
        this.animalRepository = animalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Adocao> listar() {
        return repository.findAll();
    }

    public Adocao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AdocaoNotFoundException(id));
    }

    // RN-04: solicitacao deve ter status APROVADA; animal → ADOTADO
    @Transactional
    public Adocao confirmar(AdocaoRequestDTO dto) {
        SolicitacaoAdocao solicitacao = solicitacaoRepository.findById(dto.solicitacaoId())
                .orElseThrow(() -> new SolicitacaoNotFoundException(dto.solicitacaoId()));

        if (solicitacao.getStatus() != SolicitacaoStatus.APROVADA) {
            throw new SolicitacaoStatusInvalidoException(
                    "A confirmação de adoção requer uma solicitação APROVADA. Status atual: " + solicitacao.getStatus());
        }

        Adocao adocao = new Adocao();
        adocao.setSolicitacao(solicitacao);
        adocao.setAnimal(solicitacao.getAnimal());
        adocao.setConfirmadoPor(usuarioRepository.findById(dto.confirmadoPorId())
                .orElseThrow(() -> new UsuarioNotFoundException(dto.confirmadoPorId())));
        adocao.setDataAdocao(dto.dataAdocao());

        solicitacao.getAnimal().setStatus(AnimalStatus.ADOTADO);
        animalRepository.save(solicitacao.getAnimal());

        return repository.save(adocao);
    }
}
