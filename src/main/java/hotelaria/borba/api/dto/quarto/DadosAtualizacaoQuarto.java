package hotelaria.borba.api.dto.quarto;

import hotelaria.borba.api.dto.cama_quarto.DadosAtualizacaoCamaQuarto;
import hotelaria.borba.api.dto.cama_quarto.DadosCadastroCamaQuarto;
import hotelaria.borba.api.enums.TipoQuarto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record DadosAtualizacaoQuarto(
        @NotNull
        Long id,
        Integer numero,
        TipoQuarto tipoQuarto,
        BigDecimal preco_diaria,
        String descricao,
        List<DadosAtualizacaoCamaQuarto> camas
) {
}
