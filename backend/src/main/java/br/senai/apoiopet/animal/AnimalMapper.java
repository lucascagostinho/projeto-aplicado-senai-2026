package br.senai.apoiopet.animal;

import br.senai.apoiopet.usuario.ong.Ong;
import br.senai.apoiopet.usuario.protetor.Protetor;
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
        if (a.getResponsavel() != null) {
            dto.setResponsavelId(a.getResponsavel().getId());
            if (a.getResponsavel() instanceof Ong ong) {
                dto.setResponsavelNome(ong.getRazaoSocial());
                dto.setResponsavelTipo("ONG");
            } else if (a.getResponsavel() instanceof Protetor protetor) {
                dto.setResponsavelNome(protetor.getNome());
                dto.setResponsavelTipo("PROTETOR");
            }
        }
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
