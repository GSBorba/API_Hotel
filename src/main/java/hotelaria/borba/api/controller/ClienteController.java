package hotelaria.borba.api.controller;

import hotelaria.borba.api.domain.Cliente;
import hotelaria.borba.api.dto.cliente.DadosAtualizacaoCliente;
import hotelaria.borba.api.dto.cliente.DadosCadastroCliente;
import hotelaria.borba.api.dto.cliente.DadosDetalhamentoCliente;
import hotelaria.borba.api.dto.cliente.DadosListagemCliente;
import hotelaria.borba.api.repository.ClienteRepository;
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
@RequestMapping("cliente")
public class ClienteController {

    private final ClienteRepository repository;

    @Autowired
    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> cadastro(@RequestBody DadosCadastroCliente dados, UriComponentsBuilder uriComponentsBuilder) {
        Cliente cliente = new Cliente(dados);
        repository.save(cliente);

        URI uri = uriComponentsBuilder.path("/cliente/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoCliente(cliente));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemCliente>> listagem(@PageableDefault(size = 10) Pageable pageable) {
        Page<DadosListagemCliente> clientes = repository.findAllByAtivoTrue(pageable).map(DadosListagemCliente::new);

        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoCliente> detalhamento(@PathVariable Long id) {
        Cliente cliente = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> atualizar(@RequestBody DadosAtualizacaoCliente dados) {
        Cliente cliente = repository.getReferenceById(dados.id());
        cliente.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repository.getReferenceById(id).excluir();

        return ResponseEntity.noContent().build();
    }
}
