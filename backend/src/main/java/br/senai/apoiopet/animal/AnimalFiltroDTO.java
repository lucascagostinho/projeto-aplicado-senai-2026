package br.senai.apoiopet.animal;

public record AnimalFiltroDTO(
        String especie,
        String porte,
        String faixaEtaria,
        String sexo,
        String cidade,
        String status
) {}
