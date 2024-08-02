package hotelaria.borba.api.dto.cama_quarto;

import java.util.List;

public record DTOsAtualizacaoCamaQuarto(
        List<DadosCadastroCamaQuarto> cadastroCamaQuartos,
        List<DadosAtualizacaoCamaQuarto> atualizarCamaQuartos,
        List<Long> deletarCamaQuartos) {
}
