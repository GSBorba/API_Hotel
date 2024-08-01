package hotelaria.borba.api.dto.cliente;

import hotelaria.borba.api.domain.Cliente;

public record DadosListagemCliente(Long id, String nome, String cpf, String telefone, String email) {

    public DadosListagemCliente(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getTelefone(), cliente.getEmail());
    }
}
