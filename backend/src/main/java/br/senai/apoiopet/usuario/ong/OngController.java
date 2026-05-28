package br.senai.apoiopet.usuario.ong;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ongs")
public class OngController {

    private final OngService service;
    private final OngMapper mapper;

    public OngController(OngService service, OngMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<OngResponseDTO> listar() {
        return service.listar().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OngResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<OngResponseDTO> criar(@Valid @RequestBody OngRequestDTO dto) {
        Ong salva = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OngResponseDTO> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody OngRequestDTO dto) {
        return ResponseEntity.ok(mapper.toResponse(service.atualizar(id, mapper.toEntity(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
