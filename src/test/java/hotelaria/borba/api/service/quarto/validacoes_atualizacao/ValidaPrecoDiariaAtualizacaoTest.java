package hotelaria.borba.api.service.quarto.validacoes_atualizacao;

import hotelaria.borba.api.dto.cama_quarto.DTOsAtualizacaoCamaQuarto;
import hotelaria.borba.api.dto.quarto.DadosAtualizacaoQuarto;
import hotelaria.borba.api.enums.TipoQuarto;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ValidaPrecoDiariaAtualizacaoTest {

    private final ValidaPrecoDiariaAtualizacao validador = new ValidaPrecoDiariaAtualizacao();

    @Test
    @DisplayName("Deve lançar exceção quando o preço da diária é negativo na atualização")
    void deveLancarExcecaoQuandoPrecoDiariaEhNegativoNaAtualizacao() {
        DadosAtualizacaoQuarto dados = new DadosAtualizacaoQuarto(
                1L,
                101,
                TipoQuarto.DELUX,
                new BigDecimal("-1.00"),
                "Teste Atualização",
                new DTOsAtualizacaoCamaQuarto(null, null, null)
        );

        assertThrows(ValidationException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando o preço da diária é positivo na atualização")
    void naoDeveLancarExcecaoQuandoPrecoDiariaEhPositivoNaAtualizacao() {
        DadosAtualizacaoQuarto dados = new DadosAtualizacaoQuarto(
                1L,
                101,
                TipoQuarto.DELUX,
                new BigDecimal("1.00"),
                "Teste Atualização",
                new DTOsAtualizacaoCamaQuarto(null, null, null)
        );

        validador.validar(dados);
    }
}