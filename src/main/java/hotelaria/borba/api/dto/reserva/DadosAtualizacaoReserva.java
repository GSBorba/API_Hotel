package hotelaria.borba.api.dto.reserva;

import hotelaria.borba.api.dto.quarto_reserva.DTOsAtualizacaoQuartoReserva;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record DadosAtualizacaoReserva(
        @NotNull
        Long id,
        LocalDateTime checkin,
        LocalDateTime checkout,
        DTOsAtualizacaoQuartoReserva quartos) {
}
