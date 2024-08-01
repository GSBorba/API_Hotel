package hotelaria.borba.api.dto.cama_quarto;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroCamaQuarto(
        @NotNull
        Integer qt_cama,
        @NotNull
        Long id_cama) {
}
