package br.senai.apoiopet.usuario.adotante;

import br.senai.apoiopet.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adotante")
@DiscriminatorValue("ADOTANTE")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Getter
@Setter
public class Adotante extends Usuario {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 14, unique = true)
    private String cpf;
}
