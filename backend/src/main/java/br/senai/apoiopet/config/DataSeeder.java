package br.senai.apoiopet.config;

import br.senai.apoiopet.animal.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {

    private final AnimalRepository repository;

    public DataSeeder(AnimalRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() > 0) return;

        repository.saveAll(List.of(

            animal(Especie.CAO, "Labrador Retriever", Sexo.MACHO, FaixaEtaria.JOVEM, Porte.GRANDE,
                    "Florianópolis", "SC", true, true,
                    "Muito dócil e brincalhão, adora crianças e outros animais."),

            animal(Especie.GATO, "Siamês", Sexo.FEMEA, FaixaEtaria.ADULTO, Porte.PEQUENO,
                    "Florianópolis", "SC", true, true,
                    "Calma e carinhosa, prefere ambientes tranquilos."),

            animal(Especie.CAO, "Border Collie", Sexo.FEMEA, FaixaEtaria.FILHOTE, Porte.MEDIO,
                    "Joinville", "SC", false, true,
                    "Muito inteligente e cheia de energia, ideal para espaços amplos."),

            animal(Especie.GATO, "Persa", Sexo.MACHO, FaixaEtaria.SENIOR, Porte.PEQUENO,
                    "Blumenau", "SC", true, true,
                    "Tranquilo e independente, adaptado a apartamentos."),

            animal(Especie.CAO, null, Sexo.MACHO, FaixaEtaria.ADULTO, Porte.MEDIO,
                    "Chapecó", "SC", true, true,
                    "Vira-lata amigável e vacinado. Bom guardião e companheiro fiel."),

            animal(Especie.GATO, "Maine Coon", Sexo.FEMEA, FaixaEtaria.JOVEM, Porte.MEDIO,
                    "Criciúma", "SC", false, true,
                    "Pelagem longa e temperamento dócil, se dá bem com crianças."),

            animal(Especie.CAO, "Poodle", Sexo.FEMEA, FaixaEtaria.ADULTO, Porte.PEQUENO,
                    "Itajaí", "SC", true, true,
                    "Inteligente e alegre. Já adestrada com comandos básicos."),

            animal(Especie.GATO, null, Sexo.MACHO, FaixaEtaria.FILHOTE, Porte.PEQUENO,
                    "São José", "SC", false, false,
                    "Filhote resgatado da rua, precisa de vacinação e castração."),

            animal(Especie.CAO, "Golden Retriever", Sexo.MACHO, FaixaEtaria.JOVEM, Porte.GRANDE,
                    "Florianópolis", "SC", true, true,
                    "Sociável e gentil. Excelente com famílias e crianças pequenas."),

            animal(Especie.CAO, "Beagle", Sexo.MACHO, FaixaEtaria.FILHOTE, Porte.MEDIO,
                    "Curitiba", "PR", false, true,
                    "Curioso e animado. Ama explorar e brincar ao ar livre."),

            animal(Especie.GATO, "Angorá", Sexo.FEMEA, FaixaEtaria.SENIOR, Porte.PEQUENO,
                    "Porto Alegre", "RS", true, true,
                    "Muito afetiva e quieta. Gosta de colo e ambientes calmos."),

            animal(Especie.CAO, "Bulldog Francês", Sexo.MACHO, FaixaEtaria.ADULTO, Porte.PEQUENO,
                    "Lages", "SC", true, true,
                    "Companheiro e tranquilo, adaptado a apartamentos pequenos.")
        ));
    }

    private Animal animal(Especie especie, String raca, Sexo sexo, FaixaEtaria faixaEtaria,
                          Porte porte, String cidade, String estado,
                          boolean castrado, boolean vacinado, String caracteristicas) {
        Animal a = new Animal();
        a.setEspecie(especie);
        a.setRaca(raca);
        a.setSexo(sexo);
        a.setFaixaEtaria(faixaEtaria);
        a.setPorte(porte);
        a.setCidade(cidade);
        a.setEstado(estado);
        a.setCastrado(castrado);
        a.setVacinado(vacinado);
        a.setCaracteristicas(caracteristicas);
        return a;
    }
}
