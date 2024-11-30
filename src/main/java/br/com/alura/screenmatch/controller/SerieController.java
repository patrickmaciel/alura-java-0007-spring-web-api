package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.SerieService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
