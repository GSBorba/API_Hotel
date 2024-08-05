package hotelaria.borba.api.dto.cliente;

import hotelaria.borba.api.domain.Cliente;

public record DadosDetalhamentoCliente(Long id, String nome, String cpf, String telefone, String email, Boolean ativo) {

    public DadosDetalhamentoCliente(Cliente cliente){
        this(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getTelefone(), cliente.getEmail(), cliente.getAtivo());
    }
}
