package br.senai.apoiopet.usuario.adotante;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adotantes")
public class AdotanteController {

    private final AdotanteService service;
    private final AdotanteMapper mapper;

    public AdotanteController(AdotanteService service, AdotanteMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<AdotanteResponseDTO> listar() {
        return service.listar().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdotanteResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<AdotanteResponseDTO> criar(@Valid @RequestBody AdotanteRequestDTO dto) {
        Adotante salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdotanteResponseDTO> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody AdotanteRequestDTO dto) {
        return ResponseEntity.ok(mapper.toResponse(service.atualizar(id, mapper.toEntity(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
