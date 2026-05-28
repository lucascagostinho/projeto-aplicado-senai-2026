package br.senai.apoiopet.usuario.protetor;

import br.senai.apoiopet.exception.UsuarioNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProtetorService {

    private final ProtetorRepository repository;

    public ProtetorService(ProtetorRepository repository) {
        this.repository = repository;
    }

    public List<Protetor> listar() {
        return repository.findAll();
    }

    public Protetor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    @Transactional
    public Protetor salvar(Protetor protetor) {
        return repository.save(protetor);
    }

    @Transactional
    public Protetor atualizar(Long id, Protetor dados) {
        Protetor existente = buscarPorId(id);
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
