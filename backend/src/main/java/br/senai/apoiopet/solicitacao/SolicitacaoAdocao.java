package br.senai.apoiopet.solicitacao;

import br.senai.apoiopet.animal.Animal;
import br.senai.apoiopet.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao_adocao")
@Getter
@Setter
public class SolicitacaoAdocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adotante_id", nullable = false)
    private Usuario adotante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SolicitacaoStatus status = SolicitacaoStatus.PENDENTE;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    @Column(columnDefinition = "TEXT")
    private String justificativa;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
