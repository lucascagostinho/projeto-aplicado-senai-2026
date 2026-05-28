package br.senai.apoiopet.usuario.ong;

import br.senai.apoiopet.exception.UsuarioNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OngService {

    private final OngRepository repository;

    public OngService(OngRepository repository) {
        this.repository = repository;
    }

    public List<Ong> listar() {
        return repository.findAll();
    }

    public Ong buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    @Transactional
    public Ong salvar(Ong ong) {
        return repository.save(ong);
    }

    @Transactional
    public Ong atualizar(Long id, Ong dados) {
        Ong existente = buscarPorId(id);
        existente.setEmail(dados.getEmail());
        existente.setSenha(dados.getSenha());
        existente.setTelefone(dados.getTelefone());
        existente.setCep(dados.getCep());
        existente.setRazaoSocial(dados.getRazaoSocial());
        existente.setCnpj(dados.getCnpj());
        existente.setDescricao(dados.getDescricao());
        return existente;
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
