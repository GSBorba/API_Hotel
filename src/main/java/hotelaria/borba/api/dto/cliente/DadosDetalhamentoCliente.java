package hotelaria.borba.api.dto.cliente;

import hotelaria.borba.api.domain.Cliente;

public record DadosDetalhamentoCliente(Long id, String nome, String cpf, String telefone, String email, Boolean ativo, String cep, String uf, String cidade, String logradouro, String complemento, Integer numero) {

    public DadosDetalhamentoCliente(Cliente cliente){
        this(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getTelefone(), cliente.getEmail(), cliente.getAtivo(), cliente.getEndereco().getCep(), cliente.getEndereco().getUf().getNomeEstado(), cliente.getEndereco().getCidade(), cliente.getEndereco().getLogradouro(), cliente.getEndereco().getComplemento(), cliente.getEndereco().getNumero());
    }
}
