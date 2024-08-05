package hotelaria.borba.api.controller;

import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.dto.quarto.DadosAtualizacaoQuarto;
import hotelaria.borba.api.dto.quarto.DadosCadastroQuarto;
import hotelaria.borba.api.dto.quarto.DadosDetalhamentoQuarto;
import hotelaria.borba.api.dto.quarto.DadosListagemQuartosLivres;
import hotelaria.borba.api.repository.QuartoRepository;
import hotelaria.borba.api.service.quarto.QuartoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("quarto")
public class QuartoController {

    private final QuartoRepository repository;
    private final QuartoService service;

    @Autowired
    public QuartoController(QuartoRepository repository,
                            QuartoService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoQuarto> cadastro(@RequestBody DadosCadastroQuarto dados, UriComponentsBuilder uriComponentsBuilder) {
        Quarto quarto = service.validarCadastro(dados);

        URI uri = uriComponentsBuilder.path("/quarto/{id}").buildAndExpand(quarto.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoQuarto(quarto));
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoQuarto>> listagem(@PageableDefault(size = 10) Pageable pageable) {
        Page<DadosDetalhamentoQuarto> quartos = repository.findAll(pageable).map(DadosDetalhamentoQuarto::new);

        return ResponseEntity.ok(quartos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoQuarto> detalhamento(@PathVariable Long id) {
        Quarto quarto = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoQuarto(quarto));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoQuarto> atualizar(@RequestBody DadosAtualizacaoQuarto dados) {
        Quarto quarto = service.validarAtualizacao(dados);

        return ResponseEntity.ok(new DadosDetalhamentoQuarto(quarto));
    }

    @GetMapping("/livre")
    public ResponseEntity<Page<DadosDetalhamentoQuarto>> listagemDeQuartosLivresNaDataEspecifica(@RequestBody DadosListagemQuartosLivres dados,
                                                                                                 @PageableDefault(size = 10) Pageable pageable) {
        Page<DadosDetalhamentoQuarto> quartos = repository.listaQuartosSemReservaNaDataEspecifica(dados.checkin(), dados.checkout(), pageable).map(DadosDetalhamentoQuarto::new);

        return ResponseEntity.ok(quartos);
    }
}
