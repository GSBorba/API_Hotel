package hotelaria.borba.api.dto.hotel;

import hotelaria.borba.api.domain.Hotel;

public record DadosListagemHotel(Long id, String nome, String telefone, String email, String categoria, String cidade) {

    public DadosListagemHotel(Hotel hotel) {
        this(hotel.getId(), hotel.getNome(), hotel.getTelefone(), hotel.getEmail(), hotel.getCategoria().getNomeCategoria(), hotel.getEndereco().getCidade());
    }
}
