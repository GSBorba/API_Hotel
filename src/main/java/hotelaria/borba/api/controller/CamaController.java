package hotelaria.borba.api.controller;

import hotelaria.borba.api.domain.Cama;
import hotelaria.borba.api.dto.cama.DadosAtualizacaoCama;
import hotelaria.borba.api.dto.cama.DadosCadastroCama;
import hotelaria.borba.api.dto.cama.DadosListagemCama;
import hotelaria.borba.api.repository.CamaRepository;
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
@RequestMapping("cama")
public class CamaController {

    private final CamaRepository repository;

    @Autowired
    public CamaController(CamaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemCama> cadastro(@RequestBody DadosCadastroCama dados, UriComponentsBuilder uriComponentsBuilder) {
        Cama cama = new Cama(dados);
        repository.save(cama);

        URI uri = uriComponentsBuilder.path("/cama/{id}").buildAndExpand(cama.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemCama(cama));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemCama>> listagem(@PageableDefault(size = 10)Pageable pageable) {
        Page<DadosListagemCama> camas = repository.findAll(pageable).map(DadosListagemCama::new);

        return ResponseEntity.ok(camas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemCama> detalhamento(@PathVariable Long id) {
        Cama cama = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosListagemCama(cama));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosListagemCama> atualizacao(@RequestBody DadosAtualizacaoCama dados) {
        Cama cama = repository.getReferenceById(dados.id());
        cama.atualizar(dados);

        return ResponseEntity.ok(new DadosListagemCama(cama));
    }
}
