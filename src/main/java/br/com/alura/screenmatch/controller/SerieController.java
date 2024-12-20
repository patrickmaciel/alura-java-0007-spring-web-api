package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.SerieService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/series")
public class SerieController {
  @Autowired
  private SerieService service;

  @GetMapping
  public List<SerieDTO> getSeries() {
    return service.findAllSeries();
  }

  @GetMapping("/top5")
  public List<SerieDTO> getTop5Series() {
    return service.findTop5Series();
  }

  @GetMapping("/lancamentos")
  public List<SerieDTO> getReleases() {
    return service.findReleases();
  }

  @GetMapping("/{id}")
  public SerieDTO getSerieById(@PathVariable Long id) {
    return service.findById(id);
  }

  @GetMapping("/{id}/temporadas/todas")
  public List<EpisodioDTO> findAllSeasonsFromSerie(@PathVariable Long id) {
    return service.findAllSeasonsFromSerie(id);
  }

  @GetMapping("/{id}/temporadas/{temporada}")
  public List<EpisodioDTO> findEpisodeFromSeason(@PathVariable Long id, @PathVariable Integer temporada) {
    return service.findEpisodeFromSeason(id, temporada);
  }

  @GetMapping("/categoria/{genre}")
  public List<SerieDTO> findByGenre(@PathVariable String genre) {
    return service.findByGenre(genre);
  }

  @GetMapping("/{id}/temporadas/top")
  public List<EpisodioDTO> findTop5Episodes(@PathVariable Long id) {
    return service.findTop5Episodes(id);
  }
}
