package br.senai.apoiopet.animal;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimalResponseDTO {

    private Long id;
    private Especie especie;
    private String raca;
    private Sexo sexo;
    private FaixaEtaria faixaEtaria;
    private Porte porte;
    private String cor;
    private String caracteristicas;
    private AnimalStatus status;
    private String foto;
    private String cidade;
    private String estado;
    private Boolean castrado;
    private Boolean vacinado;
    private LocalDateTime criadoEm;
    private Long responsavelId;
    private String responsavelNome;
    private String responsavelTipo;
}
