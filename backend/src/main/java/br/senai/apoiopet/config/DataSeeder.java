package br.senai.apoiopet.config;

import br.senai.apoiopet.adocao.Adocao;
import br.senai.apoiopet.adocao.AdocaoRepository;
import br.senai.apoiopet.animal.*;
import br.senai.apoiopet.solicitacao.SolicitacaoAdocao;
import br.senai.apoiopet.solicitacao.SolicitacaoRepository;
import br.senai.apoiopet.solicitacao.SolicitacaoStatus;
import br.senai.apoiopet.usuario.adotante.Adotante;
import br.senai.apoiopet.usuario.adotante.AdotanteRepository;
import br.senai.apoiopet.usuario.ong.Ong;
import br.senai.apoiopet.usuario.ong.OngRepository;
import br.senai.apoiopet.usuario.protetor.Protetor;
import br.senai.apoiopet.usuario.protetor.ProtetorRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {

    private final AnimalRepository animalRepository;
    private final OngRepository ongRepository;
    private final ProtetorRepository protetorRepository;
    private final AdotanteRepository adotanteRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final AdocaoRepository adocaoRepository;

    public DataSeeder(AnimalRepository animalRepository,
                      OngRepository ongRepository,
                      ProtetorRepository protetorRepository,
                      AdotanteRepository adotanteRepository,
                      SolicitacaoRepository solicitacaoRepository,
                      AdocaoRepository adocaoRepository) {
        this.animalRepository = animalRepository;
        this.ongRepository = ongRepository;
        this.protetorRepository = protetorRepository;
        this.adotanteRepository = adotanteRepository;
        this.solicitacaoRepository = solicitacaoRepository;
        this.adocaoRepository = adocaoRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (animalRepository.count() > 0) return;

        // ONGs
        Ong ongFloripa = ong("floripa@apoiopet.com", "ONG Floripa Pets",
                "11.222.333/0001-44", "Organização sem fins lucrativos focada em resgates na Grande Florianópolis.");
        Ong ongPatinhas = ong("patinhas@apoiopet.com", "ONG Patinhas SC",
                "55.666.777/0001-88", "Dedicada ao resgate e adoção responsável em todo o estado de SC.");
        ongRepository.saveAll(List.of(ongFloripa, ongPatinhas));

        // Protetores
        Protetor mariaSilva = protetor("maria@protetor.com", "Maria Silva", "123.456.789-00");
        Protetor joaoSouza = protetor("joao@protetor.com", "João Souza", "987.654.321-00");
        protetorRepository.saveAll(List.of(mariaSilva, joaoSouza));

        // Adotantes
        Adotante anaLima = adotante("ana@adotante.com", "Ana Lima", "111.222.333-44");
        Adotante carlosBraga = adotante("carlos@adotante.com", "Carlos Braga", "555.666.777-88");
        adotanteRepository.saveAll(List.of(anaLima, carlosBraga));

        // Animais (alguns vinculados a responsáveis)
        Animal labrador = animal(Especie.CAO, "Labrador Retriever", Sexo.MACHO, FaixaEtaria.JOVEM, Porte.GRANDE,
                "Florianópolis", "SC", true, true,
                "Muito dócil e brincalhão, adora crianças e outros animais.", ongFloripa);

        Animal siames = animal(Especie.GATO, "Siamês", Sexo.FEMEA, FaixaEtaria.ADULTO, Porte.PEQUENO,
                "Florianópolis", "SC", true, true,
                "Calma e carinhosa, prefere ambientes tranquilos.", ongFloripa);

        Animal border = animal(Especie.CAO, "Border Collie", Sexo.FEMEA, FaixaEtaria.FILHOTE, Porte.MEDIO,
                "Joinville", "SC", false, true,
                "Muito inteligente e cheia de energia, ideal para espaços amplos.", mariaSilva);

        Animal persa = animal(Especie.GATO, "Persa", Sexo.MACHO, FaixaEtaria.SENIOR, Porte.PEQUENO,
                "Blumenau", "SC", true, true,
                "Tranquilo e independente, adaptado a apartamentos.", mariaSilva);

        Animal viraLata = animal(Especie.CAO, null, Sexo.MACHO, FaixaEtaria.ADULTO, Porte.MEDIO,
                "Chapecó", "SC", true, true,
                "Vira-lata amigável e vacinado. Bom guardião e companheiro fiel.", ongPatinhas);

        Animal maineCoon = animal(Especie.GATO, "Maine Coon", Sexo.FEMEA, FaixaEtaria.JOVEM, Porte.MEDIO,
                "Criciúma", "SC", false, true,
                "Pelagem longa e temperamento dócil, se dá bem com crianças.", joaoSouza);

        Animal poodle = animal(Especie.CAO, "Poodle", Sexo.FEMEA, FaixaEtaria.ADULTO, Porte.PEQUENO,
                "Itajaí", "SC", true, true,
                "Inteligente e alegre. Já adestrada com comandos básicos.", joaoSouza);

        Animal filhote = animal(Especie.GATO, null, Sexo.MACHO, FaixaEtaria.FILHOTE, Porte.PEQUENO,
                "São José", "SC", false, false,
                "Filhote resgatado da rua, precisa de vacinação e castração.", null);

        Animal golden = animal(Especie.CAO, "Golden Retriever", Sexo.MACHO, FaixaEtaria.JOVEM, Porte.GRANDE,
                "Florianópolis", "SC", true, true,
                "Sociável e gentil. Excelente com famílias e crianças pequenas.", ongFloripa);

        Animal beagle = animal(Especie.CAO, "Beagle", Sexo.MACHO, FaixaEtaria.FILHOTE, Porte.MEDIO,
                "Curitiba", "PR", false, true,
                "Curioso e animado. Ama explorar e brincar ao ar livre.", null);

        Animal angora = animal(Especie.GATO, "Angorá", Sexo.FEMEA, FaixaEtaria.SENIOR, Porte.PEQUENO,
                "Porto Alegre", "RS", true, true,
                "Muito afetiva e quieta. Gosta de colo e ambientes calmos.", null);

        Animal bulldog = animal(Especie.CAO, "Bulldog Francês", Sexo.MACHO, FaixaEtaria.ADULTO, Porte.PEQUENO,
                "Lages", "SC", true, true,
                "Companheiro e tranquilo, adaptado a apartamentos pequenos.", ongPatinhas);

        animalRepository.saveAll(List.of(labrador, siames, border, persa, viraLata,
                maineCoon, poodle, filhote, golden, beagle, angora, bulldog));

        // Solicitações de adoção em diferentes estados
        SolicitacaoAdocao solPendente = solicitacao(border, anaLima, "Tenho quintal grande, ideal para ela!", SolicitacaoStatus.PENDENTE);
        SolicitacaoAdocao solAprovada = solicitacao(labrador, carlosBraga, "Tenho experiência com cachorros grandes.", SolicitacaoStatus.APROVADA);
        SolicitacaoAdocao solRecusada = solicitacao(siames, anaLima, "Seria meu primeiro gato.", SolicitacaoStatus.RECUSADA);

        solAprovada.setJustificativa(null);
        solRecusada.setJustificativa("Adotante não possui experiência com felinos.");

        labrador.setStatus(AnimalStatus.EM_PROCESSO);
        animalRepository.save(labrador);

        solicitacaoRepository.saveAll(List.of(solPendente, solAprovada, solRecusada));

        // Adoção confirmada (golden já adotado)
        SolicitacaoAdocao solAdotada = solicitacao(golden, anaLima, "Quero muito!", SolicitacaoStatus.APROVADA);
        solicitacaoRepository.save(solAdotada);

        Adocao adocao = new Adocao();
        adocao.setSolicitacao(solAdotada);
        adocao.setAnimal(golden);
        adocao.setConfirmadoPor(ongFloripa);
        adocao.setDataAdocao(LocalDate.now().minusDays(10));
        adocaoRepository.save(adocao);

        golden.setStatus(AnimalStatus.ADOTADO);
        animalRepository.save(golden);
    }

    private Ong ong(String email, String razaoSocial, String cnpj, String descricao) {
        Ong o = new Ong();
        o.setEmail(email);
        o.setSenha("senha123");
        o.setRazaoSocial(razaoSocial);
        o.setCnpj(cnpj);
        o.setDescricao(descricao);
        return o;
    }

    private Protetor protetor(String email, String nome, String cpf) {
        Protetor p = new Protetor();
        p.setEmail(email);
        p.setSenha("senha123");
        p.setNome(nome);
        p.setCpf(cpf);
        return p;
    }

    private Adotante adotante(String email, String nome, String cpf) {
        Adotante a = new Adotante();
        a.setEmail(email);
        a.setSenha("senha123");
        a.setNome(nome);
        a.setCpf(cpf);
        return a;
    }

    private Animal animal(Especie especie, String raca, Sexo sexo, FaixaEtaria faixaEtaria,
                          Porte porte, String cidade, String estado,
                          boolean castrado, boolean vacinado, String caracteristicas,
                          br.senai.apoiopet.usuario.Usuario responsavel) {
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
        a.setResponsavel(responsavel);
        return a;
    }

    private SolicitacaoAdocao solicitacao(Animal animal, br.senai.apoiopet.usuario.Usuario adotante,
                                          String mensagem, SolicitacaoStatus status) {
        SolicitacaoAdocao s = new SolicitacaoAdocao();
        s.setAnimal(animal);
        s.setAdotante(adotante);
        s.setMensagem(mensagem);
        s.setStatus(status);
        return s;
    }
}
