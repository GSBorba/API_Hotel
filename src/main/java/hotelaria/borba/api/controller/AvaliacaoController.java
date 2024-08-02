package hotelaria.borba.api.controller;

import hotelaria.borba.api.domain.Avaliacao;
import hotelaria.borba.api.dto.avaliacao.DadosAtualizacaoAvaliacao;
import hotelaria.borba.api.dto.avaliacao.DadosCadastroAvaliacao;
import hotelaria.borba.api.dto.avaliacao.DadosDetalhamentoAvaliacao;
import hotelaria.borba.api.repository.AvaliacaoRepository;
import hotelaria.borba.api.service.avaliacao.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("avaliacao")
public class AvaliacaoController {

    private final AvaliacaoRepository repository;
    private final AvaliacaoService service;

    @Autowired
    public AvaliacaoController(AvaliacaoRepository repository, AvaliacaoService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoAvaliacao> cadastro(DadosCadastroAvaliacao dados, UriComponentsBuilder uriComponentsBuilder) {
        Avaliacao avaliacao = service.validarCadastro(dados);

        URI uri = uriComponentsBuilder.path("/avaliacao/{id}").buildAndExpand(avaliacao.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoAvaliacao(avaliacao));
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoAvaliacao>> listagem(@PageableDefault(size = 10)Pageable pageable) {
        Page<DadosDetalhamentoAvaliacao> avaliacoes = repository.findAll(pageable).map(DadosDetalhamentoAvaliacao::new);

        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoAvaliacao> detalhamento(@PathVariable Long id) {
        Avaliacao avaliacao = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoAvaliacao(avaliacao));
    }

    @PutMapping
    public ResponseEntity<DadosDetalhamentoAvaliacao> atualizar(@RequestBody DadosAtualizacaoAvaliacao dados) {
        Avaliacao avaliacao = service.validarAtualizacao(dados);

        return ResponseEntity.ok(new DadosDetalhamentoAvaliacao(avaliacao));
    }
}
