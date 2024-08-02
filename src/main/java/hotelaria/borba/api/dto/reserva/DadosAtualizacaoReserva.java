package hotelaria.borba.api.dto.reserva;

import hotelaria.borba.api.dto.quarto_reserva.DTOsAtualizacaoQuartoReserva;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DadosAtualizacaoReserva(
        @NotNull
        Long id,
        LocalDate checkin,
        LocalDate checkout,
        DTOsAtualizacaoQuartoReserva quartos) {
}
