package br.senai.apoiopet.solicitacao;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService service;
    private final SolicitacaoMapper mapper;

    public SolicitacaoController(SolicitacaoService service, SolicitacaoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<SolicitacaoResponseDTO> listar(@ModelAttribute SolicitacaoFiltroDTO filtro) {
        return service.listar(filtro).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criar(@Valid @RequestBody SolicitacaoRequestDTO dto) {
        SolicitacaoAdocao criada = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(criada));
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<SolicitacaoResponseDTO> aprovar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.aprovar(id)));
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<SolicitacaoResponseDTO> recusar(@PathVariable Long id,
                                                           @RequestBody SolicitacaoAcaoDTO dto) {
        return ResponseEntity.ok(mapper.toResponse(service.recusar(id, dto.justificativa())));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<SolicitacaoResponseDTO> cancelar(@PathVariable Long id,
                                                            @RequestBody SolicitacaoAcaoDTO dto) {
        return ResponseEntity.ok(mapper.toResponse(service.cancelar(id, dto.justificativa())));
    }
}
