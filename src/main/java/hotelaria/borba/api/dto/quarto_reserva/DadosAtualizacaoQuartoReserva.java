package hotelaria.borba.api.dto.quarto_reserva;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoQuartoReserva(
        @NotNull
        Long id,
        Long id_quarto
) {
}
