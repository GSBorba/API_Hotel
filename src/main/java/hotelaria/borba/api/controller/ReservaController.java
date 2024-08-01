package hotelaria.borba.api.controller;

import hotelaria.borba.api.domain.Reserva;
import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import hotelaria.borba.api.dto.reserva.DadosDetalhamentoReserva;
import hotelaria.borba.api.dto.reserva.DadosListagemReserva;
import hotelaria.borba.api.repository.ReservaRepository;
import hotelaria.borba.api.service.ReservaService;
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
@RequestMapping("reserva")
public class ReservaController {

    private final ReservaRepository repository;
    private final ReservaService service;

    @Autowired
    public ReservaController(ReservaRepository repository, ReservaService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoReserva> cadastro(@RequestBody DadosCadastroReserva dados, UriComponentsBuilder uriComponentsBuilder) {
        Reserva reserva = service.validarCadastro(dados);

        URI uri = uriComponentsBuilder.path("/reserva/{id}").buildAndExpand(reserva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoReserva(reserva));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemReserva>> listagem(@PageableDefault(size = 10) Pageable pageable) {
        Page<DadosListagemReserva> reservas = repository.findAll(pageable).map(DadosListagemReserva::new);

        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoReserva> detalhamento(@PathVariable Long id) {
        Reserva reserva = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoReserva(reserva));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoReserva> atualizar(@RequestBody DadosAtualizacaoReserva dados) {
        Reserva reserva = service.validarAtualizacao(dados);

        return ResponseEntity.ok(new DadosDetalhamentoReserva(reserva));
    }
}
