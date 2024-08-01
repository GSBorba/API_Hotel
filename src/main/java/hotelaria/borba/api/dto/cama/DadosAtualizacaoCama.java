package hotelaria.borba.api.dto.cama;

import hotelaria.borba.api.enums.TipoCama;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoCama(
        @NotNull
        Long id,
        TipoCama tipoCama) {
}
