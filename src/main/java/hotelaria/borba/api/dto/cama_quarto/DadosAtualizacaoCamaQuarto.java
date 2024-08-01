package hotelaria.borba.api.dto.cama_quarto;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoCamaQuarto(
        @NotNull
        Long id,
        Integer qt_cama,
        Long id_cama) {
}
