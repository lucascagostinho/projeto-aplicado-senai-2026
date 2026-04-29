package br.senai.apoiopet.animal;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    @GetMapping
    public List<AnimalDTO> listar() {
        return service.listarTodos().stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<AnimalDTO> criar(@Valid @RequestBody AnimalDTO dto) {
        Animal salvo = service.salvar(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AnimalDTO dto) {
        Animal atualizado = service.atualizar(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private AnimalDTO toDTO(Animal a) {
        AnimalDTO dto = new AnimalDTO();
        dto.setId(a.getId());
        dto.setEspecie(a.getEspecie());
        dto.setRaca(a.getRaca());
        dto.setSexo(a.getSexo());
        dto.setFaixaEtaria(a.getFaixaEtaria());
        dto.setPorte(a.getPorte());
        dto.setCor(a.getCor());
        dto.setCaracteristicas(a.getCaracteristicas());
        dto.setStatus(a.getStatus());
        dto.setFoto(a.getFoto());
        dto.setCidade(a.getCidade());
        dto.setEstado(a.getEstado());
        dto.setCastrado(a.getCastrado());
        dto.setVacinado(a.getVacinado());
        dto.setCriadoEm(a.getCriadoEm());
        return dto;
    }

    private Animal toEntity(AnimalDTO dto) {
        Animal a = new Animal();
        a.setEspecie(dto.getEspecie());
        a.setRaca(dto.getRaca());
        a.setSexo(dto.getSexo());
        a.setFaixaEtaria(dto.getFaixaEtaria());
        a.setPorte(dto.getPorte());
        a.setCor(dto.getCor());
        a.setCaracteristicas(dto.getCaracteristicas());
        a.setStatus(dto.getStatus());
        a.setFoto(dto.getFoto());
        a.setCidade(dto.getCidade());
        a.setEstado(dto.getEstado());
        a.setCastrado(dto.getCastrado());
        a.setVacinado(dto.getVacinado());
        return a;
    }
}
