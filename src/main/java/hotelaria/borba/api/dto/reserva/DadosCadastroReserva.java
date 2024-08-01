package hotelaria.borba.api.dto.reserva;

import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record DadosCadastroReserva(
        @NotNull
        LocalDateTime checkin,
        @NotNull
        LocalDateTime checkout,
        @NotNull
        Long id_cliente,
        @NotNull
        List<DadosCadastroQuartoReserva> quartos) {
}
