package br.senai.apoiopet.adocao;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adocoes")
public class AdocaoController {

    private final AdocaoService service;
    private final AdocaoMapper mapper;

    public AdocaoController(AdocaoService service, AdocaoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<AdocaoResponseDTO> listar() {
        return service.listar().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdocaoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<AdocaoResponseDTO> confirmar(@Valid @RequestBody AdocaoRequestDTO dto) {
        Adocao adocao = service.confirmar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(adocao));
    }
}
