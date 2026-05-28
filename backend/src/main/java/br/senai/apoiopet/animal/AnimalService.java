package br.senai.apoiopet.animal;

import br.senai.apoiopet.exception.AnimalNotFoundException;
import br.senai.apoiopet.solicitacao.SolicitacaoService;
import br.senai.apoiopet.usuario.Usuario;
import br.senai.apoiopet.usuario.UsuarioRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnimalService {

    private final AnimalRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitacaoService solicitacaoService;

    public AnimalService(AnimalRepository repository,
                         UsuarioRepository usuarioRepository,
                         @Lazy SolicitacaoService solicitacaoService) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.solicitacaoService = solicitacaoService;
    }

    public List<Animal> listarComFiltro(AnimalFiltroDTO filtro) {
        return repository.findAll(AnimalSpec.build(filtro));
    }

    public Animal buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException(id));
    }

    @Transactional
    public Animal salvar(Animal animal, Long responsavelId) {
        animal.setResponsavel(resolverResponsavel(responsavelId));
        return repository.save(animal);
    }

    // RN-03: ao mudar para INDISPONIVEL, cancela todas as solicitações ativas
    @Transactional
    public Animal atualizar(Long id, Animal dados, Long responsavelId) {
        Animal animal = buscarPorId(id);
        animal.setEspecie(dados.getEspecie());
        animal.setRaca(dados.getRaca());
        animal.setSexo(dados.getSexo());
        animal.setFaixaEtaria(dados.getFaixaEtaria());
        animal.setPorte(dados.getPorte());
        animal.setCor(dados.getCor());
        animal.setCaracteristicas(dados.getCaracteristicas());

        AnimalStatus novoStatus = dados.getStatus() != null ? dados.getStatus() : animal.getStatus();
        if (novoStatus == AnimalStatus.INDISPONIVEL && animal.getStatus() != AnimalStatus.INDISPONIVEL) {
            solicitacaoService.cancelarSolicitacoesDoAnimal(animal.getId());
        }
        animal.setStatus(novoStatus);

        animal.setFoto(dados.getFoto());
        animal.setCidade(dados.getCidade());
        animal.setEstado(dados.getEstado());
        animal.setCastrado(dados.getCastrado());
        animal.setVacinado(dados.getVacinado());
        animal.setResponsavel(resolverResponsavel(responsavelId));
        return repository.save(animal);
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    private Usuario resolverResponsavel(Long responsavelId) {
        if (responsavelId == null) return null;
        return usuarioRepository.findById(responsavelId).orElse(null);
    }
}
