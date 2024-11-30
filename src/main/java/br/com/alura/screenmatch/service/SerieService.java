package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SerieService {
  @Autowired
  private SerieRepository repository;

  private List<SerieDTO> convertData(List<Serie> series) {
    return series.stream()
        .map(s -> new SerieDTO(
            s.getId(),
            s.getTitulo(),
            s.getTotalTemporadas(),
            s.getAvaliacao(),
            s.getGenero().name(),
            s.getAtores(),
            s.getPoster(),
            s.getSinopse()))
        .collect(Collectors.toList());

  }

  public List<SerieDTO> findAllSeries() {
    return convertData(repository.findAll());
  }

  public List<SerieDTO> findTop5Series() {
    return convertData(repository.findTop5ByOrderByAvaliacaoDesc());
  }

  public List<SerieDTO> findReleases() {
//    return convertData(repository.findTop5ByOrderByEpisodiosDataLancamentoDesc());
    return convertData(repository.buscaSeriesComEpisodiosMaisRecentes());
  }

  public SerieDTO findById(Long id) {
    Optional<Serie> serie = repository.findById(id);
//    Serie dadosSerie = serie.get();
//    List<DadosTemporada> temporadas = new ArrayList<>();
//    ConsumoApi consumo = new ConsumoApi();
//    String ENDERECO = "https://www.omdbapi.com/?t=";
//    String API_KEY = "&apikey=43e78112";
//    ConverteDados conversor = new ConverteDados();
//
//    for (int i = 1; i <= dadosSerie.getTotalTemporadas(); i++) {
//      var json = consumo.obterDados(ENDERECO +
//          dadosSerie.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
//      DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
//      temporadas.add(dadosTemporada);
//    }
//    temporadas.forEach(System.out::println);
//
//    List<Episodio> episodios = temporadas.stream()
//        .flatMap(d -> d.episodios().stream()
//            .map(e -> new Episodio(d.numero(), e)))
//        .toList();
//
//    dadosSerie.setEpisodios(episodios);
//    repository.save(dadosSerie);

    return serie.map(value -> new SerieDTO(
        value.getId(),
        value.getTitulo(),
        value.getTotalTemporadas(),
        value.getAvaliacao(),
        value.getGenero().name(),
        value.getAtores(),
        value.getPoster(),
        value.getSinopse())).orElse(null);

  }

  public List<EpisodioDTO> findAllSeasonsFromSerie(Long id) {
    Optional<Serie> serieFound = repository.findById(id);

    if (serieFound.isPresent()) {
      Serie serie = serieFound.get();
      return serie.getEpisodios().stream()
          .map(e -> new EpisodioDTO(
              e.getTemporada(),
              e.getNumeroEpisodio(),
              e.getTitulo())
          ).toList();
    }

    return null;
  }

  public List<EpisodioDTO> findEpisodeFromSeason(Long id, Integer temporada) {
    return repository.buscaEpisodiosPorTemporada(id, temporada).stream()
        .map(e -> new EpisodioDTO(
            e.getTemporada(),
            e.getNumeroEpisodio(),
            e.getTitulo())
        ).toList();
  }
}
