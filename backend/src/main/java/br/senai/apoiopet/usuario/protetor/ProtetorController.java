package br.senai.apoiopet.usuario.protetor;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/protetores")
public class ProtetorController {

    private final ProtetorService service;
    private final ProtetorMapper mapper;

    public ProtetorController(ProtetorService service, ProtetorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ProtetorResponseDTO> listar() {
        return service.listar().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProtetorResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ProtetorResponseDTO> criar(@Valid @RequestBody ProtetorRequestDTO dto) {
        Protetor salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProtetorResponseDTO> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ProtetorRequestDTO dto) {
        return ResponseEntity.ok(mapper.toResponse(service.atualizar(id, mapper.toEntity(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
