package hotelaria.borba.api.dto.quarto;

import hotelaria.borba.api.dto.cama_quarto.DTOsAtualizacaoCamaQuarto;
import hotelaria.borba.api.enums.TipoQuarto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DadosAtualizacaoQuarto(
        @NotNull
        Long id,
        Integer numero,
        TipoQuarto tipoQuarto,
        BigDecimal precoDiaria,
        String descricao,
        DTOsAtualizacaoCamaQuarto camas
) {
}
