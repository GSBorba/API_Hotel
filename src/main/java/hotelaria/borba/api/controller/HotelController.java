package hotelaria.borba.api.controller;

import hotelaria.borba.api.domain.Hotel;
import hotelaria.borba.api.dto.hotel.DadosAtualizacaoHotel;
import hotelaria.borba.api.dto.hotel.DadosCadastroHotel;
import hotelaria.borba.api.dto.hotel.DadosDetalhamentoHotel;
import hotelaria.borba.api.dto.hotel.DadosListagemHotel;
import hotelaria.borba.api.repository.HotelRepository;
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
@RequestMapping("hotel")
public class HotelController {

    private final HotelRepository repository;

    @Autowired
    public HotelController(HotelRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoHotel> cadastro(@RequestBody DadosCadastroHotel dados, UriComponentsBuilder uriComponentsBuilder) {
        Hotel hotel = new Hotel(dados);
        repository.save(hotel);

        URI uri = uriComponentsBuilder.path("/hotel/{id}").buildAndExpand(hotel.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoHotel(hotel));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemHotel>> listagem(@PageableDefault(size = 10)Pageable pageable) {
        Page<DadosListagemHotel> hotels = repository.findAll(pageable).map(DadosListagemHotel::new);

        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoHotel> detalhamento(@PathVariable Long id) {
        Hotel hotel = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoHotel(hotel));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoHotel> atualizar(@RequestBody DadosAtualizacaoHotel dados) {
        Hotel hotel = repository.getReferenceById(dados.id());
        hotel.atualizar(dados);

        return ResponseEntity.ok(new DadosDetalhamentoHotel(hotel));
    }
}
