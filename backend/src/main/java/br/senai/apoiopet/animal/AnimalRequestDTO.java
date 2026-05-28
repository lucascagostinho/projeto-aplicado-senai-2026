package br.senai.apoiopet.animal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalRequestDTO {

    @NotNull(message = "Espécie é obrigatória")
    private Especie especie;

    private String raca;

    @NotNull(message = "Sexo é obrigatório")
    private Sexo sexo;

    @NotNull(message = "Faixa etária é obrigatória")
    private FaixaEtaria faixaEtaria;

    @NotNull(message = "Porte é obrigatório")
    private Porte porte;

    private String cor;

    private String caracteristicas;

    private AnimalStatus status;

    private String foto;

    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Size(max = 2, message = "Estado deve ter no máximo 2 caracteres")
    private String estado;

    @NotNull(message = "Castrado é obrigatório")
    private Boolean castrado;

    @NotNull(message = "Vacinado é obrigatório")
    private Boolean vacinado;

    private Long responsavelId;
}
