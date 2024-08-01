package hotelaria.borba.api.dto.hotel;

import hotelaria.borba.api.domain.Hotel;

public record DadosDetalhamentoHotel(Long id, String nome, String telefone, String email, String descricao, String categoria, String cep, String uf, String cidade, String logradouro, String complemento, Integer numero) {

    public DadosDetalhamentoHotel(Hotel hotel) {
        this(hotel.getId(), hotel.getNome(), hotel.getTelefone(), hotel.getEmail(), hotel.getDescricao(), hotel.getCategoria().getNomeCategoria(), hotel.getEndereco().getCep(), hotel.getEndereco().getUf().getNomeEstado(), hotel.getEndereco().getCidade(), hotel.getEndereco().getLogradouro(), hotel.getEndereco().getComplemento(), hotel.getEndereco().getNumero());
    }
}
