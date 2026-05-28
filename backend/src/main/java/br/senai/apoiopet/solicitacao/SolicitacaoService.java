package br.senai.apoiopet.solicitacao;

import br.senai.apoiopet.animal.Animal;
import br.senai.apoiopet.animal.AnimalRepository;
import br.senai.apoiopet.animal.AnimalStatus;
import br.senai.apoiopet.exception.AnimalNotFoundException;
import br.senai.apoiopet.exception.SolicitacaoNotFoundException;
import br.senai.apoiopet.exception.SolicitacaoStatusInvalidoException;
import br.senai.apoiopet.exception.UsuarioNotFoundException;
import br.senai.apoiopet.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final AnimalRepository animalRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitacaoService(SolicitacaoRepository repository,
                               AnimalRepository animalRepository,
                               UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.animalRepository = animalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<SolicitacaoAdocao> listar(SolicitacaoFiltroDTO filtro) {
        if (filtro.animalId() != null) {
            return repository.findByAnimalId(filtro.animalId());
        }
        if (filtro.adotanteId() != null) {
            return repository.findByAdotanteId(filtro.adotanteId());
        }
        return repository.findAll();
    }

    public SolicitacaoAdocao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SolicitacaoNotFoundException(id));
    }

    // RN-06: Adotante não pode reenviar solicitação se já tem PENDENTE ou APROVADA para o mesmo animal
    // Animal deve estar DISPONIVEL
    @Transactional
    public SolicitacaoAdocao criar(SolicitacaoRequestDTO dto) {
        Animal animal = animalRepository.findById(dto.animalId())
                .orElseThrow(() -> new AnimalNotFoundException(dto.animalId()));

        if (animal.getStatus() != AnimalStatus.DISPONIVEL) {
            throw new SolicitacaoStatusInvalidoException(
                    "Não é possível solicitar adoção de um animal com status: " + animal.getStatus());
        }

        boolean jaExiste = repository.existsByAnimalIdAndAdotanteIdAndStatusIn(
                dto.animalId(), dto.adotanteId(),
                List.of(SolicitacaoStatus.PENDENTE, SolicitacaoStatus.APROVADA));
        if (jaExiste) {
            throw new SolicitacaoStatusInvalidoException(
                    "Adotante já possui solicitação ativa (PENDENTE ou APROVADA) para este animal.");
        }

        SolicitacaoAdocao solicitacao = new SolicitacaoAdocao();
        solicitacao.setAnimal(animal);
        solicitacao.setAdotante(usuarioRepository.findById(dto.adotanteId())
                .orElseThrow(() -> new UsuarioNotFoundException(dto.adotanteId())));
        solicitacao.setMensagem(dto.mensagem());
        solicitacao.setStatus(SolicitacaoStatus.PENDENTE);
        return repository.save(solicitacao);
    }

    // RN-01: Ao aprovar, todas as outras PENDENTES do mesmo animal → RECUSADA
    //        Animal → EM_PROCESSO
    @Transactional
    public SolicitacaoAdocao aprovar(Long id) {
        SolicitacaoAdocao solicitacao = buscarPorId(id);

        if (solicitacao.getStatus() != SolicitacaoStatus.PENDENTE) {
            throw new SolicitacaoStatusInvalidoException(
                    "Apenas solicitações PENDENTES podem ser aprovadas. Status atual: " + solicitacao.getStatus());
        }

        solicitacao.setStatus(SolicitacaoStatus.APROVADA);

        Animal animal = solicitacao.getAnimal();
        animal.setStatus(AnimalStatus.EM_PROCESSO);
        animalRepository.save(animal);

        List<SolicitacaoAdocao> outrasPendentes = repository.findByAnimalIdAndStatusIn(
                animal.getId(), List.of(SolicitacaoStatus.PENDENTE));
        outrasPendentes.stream()
                .filter(s -> !s.getId().equals(id))
                .forEach(s -> {
                    s.setStatus(SolicitacaoStatus.RECUSADA);
                    s.setJustificativa("Outra solicitação foi aprovada para este animal.");
                });
        repository.saveAll(outrasPendentes);

        return repository.save(solicitacao);
    }

    @Transactional
    public SolicitacaoAdocao recusar(Long id, String justificativa) {
        SolicitacaoAdocao solicitacao = buscarPorId(id);

        if (solicitacao.getStatus() != SolicitacaoStatus.PENDENTE) {
            throw new SolicitacaoStatusInvalidoException(
                    "Apenas solicitações PENDENTES podem ser recusadas. Status atual: " + solicitacao.getStatus());
        }

        solicitacao.setStatus(SolicitacaoStatus.RECUSADA);
        solicitacao.setJustificativa(justificativa);
        return repository.save(solicitacao);
    }

    // RN-02: Se estava APROVADA → animal volta para DISPONIVEL
    @Transactional
    public SolicitacaoAdocao cancelar(Long id, String justificativa) {
        SolicitacaoAdocao solicitacao = buscarPorId(id);

        if (solicitacao.getStatus() != SolicitacaoStatus.PENDENTE
                && solicitacao.getStatus() != SolicitacaoStatus.APROVADA) {
            throw new SolicitacaoStatusInvalidoException(
                    "Apenas solicitações PENDENTES ou APROVADAS podem ser canceladas. Status atual: " + solicitacao.getStatus());
        }

        boolean eraAprovada = solicitacao.getStatus() == SolicitacaoStatus.APROVADA;
        solicitacao.setStatus(SolicitacaoStatus.CANCELADA);
        solicitacao.setJustificativa(justificativa);

        if (eraAprovada) {
            Animal animal = solicitacao.getAnimal();
            animal.setStatus(AnimalStatus.DISPONIVEL);
            animalRepository.save(animal);
        }

        return repository.save(solicitacao);
    }

    // RN-03: Cancelar todas solicitações PENDENTE/APROVADA quando animal vai para INDISPONIVEL
    // Chamado por AnimalService ao mudar status para INDISPONIVEL
    @Transactional
    public void cancelarSolicitacoesDoAnimal(Long animalId) {
        List<SolicitacaoAdocao> ativas = repository.findByAnimalIdAndStatusIn(
                animalId, List.of(SolicitacaoStatus.PENDENTE, SolicitacaoStatus.APROVADA));
        ativas.forEach(s -> {
            s.setStatus(SolicitacaoStatus.CANCELADA);
            s.setJustificativa("Animal marcado como indisponível pelo responsável.");
        });
        repository.saveAll(ativas);
    }
}
