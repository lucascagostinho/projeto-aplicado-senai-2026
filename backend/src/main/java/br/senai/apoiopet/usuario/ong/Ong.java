package br.senai.apoiopet.usuario.ong;

import br.senai.apoiopet.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ong")
@DiscriminatorValue("ONG")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Getter
@Setter
public class Ong extends Usuario {

    @Column(name = "razao_social", nullable = false, length = 150)
    private String razaoSocial;

    @Column(length = 18, unique = true)
    private String cnpj;

    @Column(columnDefinition = "TEXT")
    private String descricao;
}
