package br.senai.apoiopet.animal;

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

    @Column(nullable = false, length = 20)
    private String especie;

    @Column(length = 100)
    private String raca;

    @Column(nullable = false, length = 10)
    private String sexo;

    @Column(name = "faixa_etaria", nullable = false, length = 15)
    private String faixaEtaria;

    @Column(nullable = false, length = 10)
    private String porte;

    @Column(length = 50)
    private String cor;

    @Column(columnDefinition = "TEXT")
    private String caracteristicas;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(length = 255)
    private String foto;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(nullable = false)
    private Boolean castrado;

    @Column(nullable = false)
    private Boolean vacinado;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        if (status == null) {
            status = "disponivel";
        }
    }
}
