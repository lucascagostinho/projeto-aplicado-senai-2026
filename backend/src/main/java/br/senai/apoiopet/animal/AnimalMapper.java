package br.senai.apoiopet.animal;

import org.springframework.stereotype.Component;

@Component
public class AnimalMapper {

    public AnimalResponseDTO toResponse(Animal a) {
        AnimalResponseDTO dto = new AnimalResponseDTO();
        dto.setId(a.getId());
        dto.setEspecie(a.getEspecie());
        dto.setRaca(a.getRaca());
        dto.setSexo(a.getSexo());
        dto.setFaixaEtaria(a.getFaixaEtaria());
        dto.setPorte(a.getPorte());
        dto.setCor(a.getCor());
        dto.setCaracteristicas(a.getCaracteristicas());
        dto.setStatus(a.getStatus());
        dto.setFoto(a.getFoto());
        dto.setCidade(a.getCidade());
        dto.setEstado(a.getEstado());
        dto.setCastrado(a.getCastrado());
        dto.setVacinado(a.getVacinado());
        dto.setCriadoEm(a.getCriadoEm());
        return dto;
    }

    public Animal toEntity(AnimalRequestDTO dto) {
        Animal a = new Animal();
        a.setEspecie(dto.getEspecie());
        a.setRaca(dto.getRaca());
        a.setSexo(dto.getSexo());
        a.setFaixaEtaria(dto.getFaixaEtaria());
        a.setPorte(dto.getPorte());
        a.setCor(dto.getCor());
        a.setCaracteristicas(dto.getCaracteristicas());
        a.setStatus(dto.getStatus());
        a.setFoto(dto.getFoto());
        a.setCidade(dto.getCidade());
        a.setEstado(dto.getEstado());
        a.setCastrado(dto.getCastrado());
        a.setVacinado(dto.getVacinado());
        return a;
    }
}
