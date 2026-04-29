package br.senai.apoiopet.animal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimalDTO {

    private Long id;

    @NotBlank(message = "Espécie é obrigatória")
    private String especie;

    private String raca;

    @NotBlank(message = "Sexo é obrigatório")
    private String sexo;

    @NotBlank(message = "Faixa etária é obrigatória")
    private String faixaEtaria;

    @NotBlank(message = "Porte é obrigatório")
    private String porte;

    private String cor;

    private String caracteristicas;

    private String status;

    private String foto;

    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    private String estado;

    @NotNull(message = "Castrado é obrigatório")
    private Boolean castrado;

    @NotNull(message = "Vacinado é obrigatório")
    private Boolean vacinado;

    private LocalDateTime criadoEm;
}
