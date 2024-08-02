package hotelaria.borba.api.service.reserva.validacoes_atualizacao;

import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;

public interface ValidadorAtualizacaoDeReservas {

    void validar(DadosAtualizacaoReserva dados);
}
