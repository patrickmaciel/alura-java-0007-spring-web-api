package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=43e78112";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository serieRepository;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Lista series buscadas
                4 - Buscar serie por titulo
                5 - Buscar séries por ator
                6 - Top 5 series
                7 - Séries por categoria
                8 - Desafio busca detalhada
                9 - Desafio busca detalhada query
                10 - Desafio busca detalhada jpql
                11 - Busca episodio por trecho
                12 - Top episodios por série
                13 - Buscar episodios depois de uma data
                
                0 - Sair
                """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    desafioBuscaDetalhada();
                    break;
                case 9:
                    desafioBuscaDetalhadaQuery();
                    break;
                case 10:
                    desafioBuscaDetalhadaQueryJpql();
                    break;
                case 11:
                    buscaEpisodioPorTrecho();
                    break;
                case 12:
                    buscaTopEpisodiosPorSerie();
                    break;
                case 13:
                    buscarEpisodioDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarEpisodioDepoisDeUmaData() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();

            System.out.println("Digite o ano limite de lançamento: ");
            int ano = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodios = serieRepository.buscaEpisodioPorSerieEAno(serie, ano);
            System.out.println("Episódios da série " + serie.getTitulo() + " lançados após " + ano + ": ");
            episodios.forEach(e -> System.out.printf(
                "Temporada: %d, episódio: %s, data de lançamento: %s%n",
                e.getTemporada(), e.getTitulo(), e.getDataLancamento()));
        }
    }

    private void buscaTopEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> episodios = serieRepository.buscaTopEpisodiosPorSerie(serie);
            System.out.println("Top episódios da série " + serie.getTitulo() + ": ");
            episodios.forEach(e -> System.out.printf(
                "Temporada: %d, episódio: %s, avaliação: %.2f%n",
                e.getTemporada(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscaEpisodioPorTrecho() {
        System.out.println("Digite o trecho do episódio: ");
        String trecho = leitura.nextLine();
        List<Episodio> episodios = serieRepository.buscaEpisodioPorTrecho(trecho);
        System.out.println("Episódios encontrados com o trecho " + trecho + ": ");
//        episodios.forEach(e -> System.out.println(e.getSerie().getTitulo() + ", temporada: " + e.getTemporada() + ", episódio: " + e.getTitulo()));
        episodios.forEach(e -> System.out.printf("Serie: %s, temporada: %d, episódio: %s%n", e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo()));
    }

    private void desafioBuscaDetalhada() {
        System.out.println("Digite o número de temporadas máximo: ");
        int numeroTemporadas = leitura.nextInt();

        System.out.println("Digite a avaliação mínima: ");
        Double avaliacao = leitura.nextDouble();

        List<Serie> seriesBuscadas = serieRepository.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(numeroTemporadas, avaliacao);
        System.out.println("Séries encontradas com até " + numeroTemporadas + " temporadas e avaliação maior que " + avaliacao + ": ");
        seriesBuscadas.forEach(s -> System.out.println(s.getTitulo() + ", temporadas: " + s.getTotalTemporadas() + ", avaliacao: " + s.getAvaliacao()));
    }

    private void desafioBuscaDetalhadaQuery() {
        System.out.println("Digite o número de temporadas máximo: ");
        int numeroTemporadas = leitura.nextInt();

        System.out.println("Digite a avaliação mínima: ");
        Double avaliacao = leitura.nextDouble();

        List<Serie> seriesBuscadas = serieRepository.seriesPorTemporadaEAvaliacao(numeroTemporadas, avaliacao);
        System.out.println("Séries encontradas com até " + numeroTemporadas + " temporadas e avaliação maior que " + avaliacao + ": ");
        seriesBuscadas.forEach(s -> System.out.println(s.getTitulo() + ", temporadas: " + s.getTotalTemporadas() + ", avaliacao: " + s.getAvaliacao()));
    }

    private void desafioBuscaDetalhadaQueryJpql() {
        System.out.println("Digite o número de temporadas máximo: ");
        int numeroTemporadas = leitura.nextInt();

        System.out.println("Digite a avaliação mínima: ");
        Double avaliacao = leitura.nextDouble();

        List<Serie> seriesBuscadas = serieRepository.seriesPorTemporadaEAvaliacaoUsandoJPQL(numeroTemporadas, avaliacao);
        System.out.println("Séries encontradas com até " + numeroTemporadas + " temporadas e avaliação maior que " + avaliacao + ": ");
        seriesBuscadas.forEach(s -> System.out.println(s.getTitulo() + ", temporadas: " + s.getTotalTemporadas() + ", avaliacao: " + s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Digite o nome do gênero: ");
        String genero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(genero);
        List<Serie> series = serieRepository.findByGenero(categoria);
        System.out.println("Séries encontradas no gênero " + genero + ": ");
        series.forEach(s -> System.out.println(s.getTitulo()));
    }

    private void buscarTop5Series() {
        List<Serie> top5Series = serieRepository.findTop5ByOrderByAvaliacaoDesc();
        System.out.println("Top 5 séries: ");
        top5Series.forEach(s -> System.out.println(s.getTitulo() + ", avaliacao: " + s.getAvaliacao()));
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator: ");
        String nomeAtor = leitura.nextLine();
        System.out.println("Digite a avaliação mínima: ");
        Double avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = serieRepository
            .findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries encontradas que o ator " + nomeAtor + " participa: ");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + ", avaliacao: " + s.getAvaliacao()));
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolhaa série pelo nome: ");
        String nomeSerie = leitura.nextLine();
        serieBuscada = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dadosSerie = getDadosSerieFromApi();
        if (dadosSerie != null) {
            Serie serie = new Serie(dadosSerie);
            serieRepository.save(serie);
        }

        System.out.println(dadosSerie);
    }

    private void listarSeriesBuscadas() {
        // Anteriormente estávamos instanciando um ArrayList vazio
        // Mas agora utilizando o repositorio + o banco de dados, vamos consumir do que já existe
        series = serieRepository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                    .forEach(System.out::println);
    }

    private void listarSeriesBuscadasOne() {
        List<Serie> series = new ArrayList<>();

        series = dadosSeries.stream()
                .map(d -> new Serie(d))
                .collect(Collectors.toList());

        series.stream()
            .sorted(Comparator.comparing(Serie::getGenero))
            .forEach(System.out::println);
    }

    private void listarSeriesBuscadasTwo() {
        List<Serie> series = new ArrayList<>();

        series = dadosSeries.stream()
            .map(Serie::new)
            .toList();

        series.stream()
            .sorted(Comparator.comparing(Serie::getGenero))
            .forEach(System.out::println);
    }

    private Optional<Serie> getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        String nomeSerie = leitura.nextLine();

      return serieRepository.findByTituloContainingIgnoreCase(nomeSerie);
    }

    private DadosSerie getDadosSerieFromApi() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
      return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();

        Optional<Serie> serie = getDadosSerie();
        if (serie.isPresent()) {
            Serie dadosSerie = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= dadosSerie.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO +
                    dadosSerie.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                .flatMap(d -> d.episodios().stream()
                    .map(e -> new Episodio(d.numero(), e)))
                .toList();

            dadosSerie.setEpisodios(episodios);
            serieRepository.save(dadosSerie);
        } else {
            System.out.println("Série não encontrada");
        }
    }
}
