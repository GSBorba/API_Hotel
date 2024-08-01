package hotelaria.borba.api.dto.quarto_reserva;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroQuartoReserva(
        @NotNull
        Long id_quarto) {
}
