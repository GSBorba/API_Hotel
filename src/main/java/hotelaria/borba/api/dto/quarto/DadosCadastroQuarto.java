package hotelaria.borba.api.dto.quarto;

import hotelaria.borba.api.dto.cama_quarto.DadosCadastroCamaQuarto;
import hotelaria.borba.api.enums.TipoQuarto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record DadosCadastroQuarto(
        @NotNull
        Integer numero,
        @NotNull
        TipoQuarto tipoQuarto,
        @NotNull
        BigDecimal precoDiaria,
        @NotBlank
        String descricao,
        @NotNull
        Long id_hotel,
        @NotNull
        List<DadosCadastroCamaQuarto> camas) {
}
