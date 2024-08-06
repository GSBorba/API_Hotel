package hotelaria.borba.api.dto.reserva;

import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record DadosCadastroReserva(
        @NotNull
        LocalDate checkin,
        @NotNull
        LocalDate checkout,
        @NotNull
        Long id_cliente,
        @NotNull
        List<DadosCadastroQuartoReserva> quartos) {
}
