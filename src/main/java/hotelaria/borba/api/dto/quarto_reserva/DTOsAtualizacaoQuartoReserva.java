package hotelaria.borba.api.dto.quarto_reserva;

import java.util.List;

public record DTOsAtualizacaoQuartoReserva(
        List<DadosCadastroQuartoReserva> cadastroQuartoReserva,
        List<DadosAtualizacaoQuartoReserva> atualizacaoQuartoReserva,
        List<Long> deletarQuartoReserva) {
}
