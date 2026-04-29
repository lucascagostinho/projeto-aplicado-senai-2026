package br.senai.apoiopet.animal;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository repository;

    public AnimalService(AnimalRepository repository) {
        this.repository = repository;
    }

    public List<Animal> listarTodos() {
        return repository.findAll();
    }

    public Animal buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado com id: " + id));
    }

    public Animal salvar(Animal animal) {
        return repository.save(animal);
    }

    public Animal atualizar(Long id, Animal dados) {
        Animal animal = buscarPorId(id);
        animal.setEspecie(dados.getEspecie());
        animal.setRaca(dados.getRaca());
        animal.setSexo(dados.getSexo());
        animal.setFaixaEtaria(dados.getFaixaEtaria());
        animal.setPorte(dados.getPorte());
        animal.setCor(dados.getCor());
        animal.setCaracteristicas(dados.getCaracteristicas());
        animal.setStatus(dados.getStatus() != null ? dados.getStatus() : animal.getStatus());
        animal.setFoto(dados.getFoto());
        animal.setCidade(dados.getCidade());
        animal.setEstado(dados.getEstado());
        animal.setCastrado(dados.getCastrado());
        animal.setVacinado(dados.getVacinado());
        return repository.save(animal);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
