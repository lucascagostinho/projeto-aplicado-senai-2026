package br.senai.apoiopet.usuario.adotante;

import br.senai.apoiopet.exception.UsuarioNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdotanteService {

    private final AdotanteRepository repository;

    public AdotanteService(AdotanteRepository repository) {
        this.repository = repository;
    }

    public List<Adotante> listar() {
        return repository.findAll();
    }

    public Adotante buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    @Transactional
    public Adotante salvar(Adotante adotante) {
        return repository.save(adotante);
    }

    @Transactional
    public Adotante atualizar(Long id, Adotante dados) {
        Adotante existente = buscarPorId(id);
        existente.setEmail(dados.getEmail());
        existente.setSenha(dados.getSenha());
        existente.setTelefone(dados.getTelefone());
        existente.setCep(dados.getCep());
        existente.setNome(dados.getNome());
        existente.setCpf(dados.getCpf());
        return existente;
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
