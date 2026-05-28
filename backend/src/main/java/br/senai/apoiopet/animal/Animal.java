package br.senai.apoiopet.animal;

import br.senai.apoiopet.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "animal")
@Getter
@Setter
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Especie especie;

    @Column(length = 100)
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "faixa_etaria", nullable = false, length = 15)
    private FaixaEtaria faixaEtaria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Porte porte;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(nullable = false)
    private Boolean castrado;

    @Column(nullable = false)
    private Boolean vacinado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AnimalStatus status;

    @Column(length = 50)
    private String cor;

    @Column(length = 255)
    private String foto;

    @Column(columnDefinition = "TEXT")
    private String caracteristicas;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        if (status == null) {
            status = AnimalStatus.DISPONIVEL;
        }
    }
}
