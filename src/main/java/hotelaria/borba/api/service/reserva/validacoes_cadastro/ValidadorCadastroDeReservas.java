package hotelaria.borba.api.service.reserva.validacoes_cadastro;

import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;

public interface ValidadorCadastroDeReservas {

    void validar(DadosCadastroReserva dados);
}
