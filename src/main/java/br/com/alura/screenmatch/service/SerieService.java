package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import java.util.List;
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
}
