package hotelaria.borba.api.service.quarto.validacoes_cadastro;

import hotelaria.borba.api.dto.cama_quarto.DadosCadastroCamaQuarto;
import hotelaria.borba.api.dto.quarto.DadosCadastroQuarto;
import hotelaria.borba.api.enums.TipoQuarto;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidaPrecoDiariaCadastroTest {

    private final ValidaPrecoDiariaCadastro validador = new ValidaPrecoDiariaCadastro();

    @Test
    @DisplayName("Deve lançar exceção quando o preço da diária é negativo")
    void deveLancarExcecaoQuandoPrecoDiariaEhNegativo() {
        DadosCadastroQuarto dados = new DadosCadastroQuarto(
                101,
                TipoQuarto.DELUX,
                new BigDecimal("-1.00"),
                "Teste",
                1L,
                List.of(new DadosCadastroCamaQuarto(1, 1L))
        );

        assertThrows(ValidationException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando o preço da diária é positivo")
    void naoDeveLancarExcecaoQuandoPrecoDiariaEhPositivo() {
        DadosCadastroQuarto dados = new DadosCadastroQuarto(
                101,
                TipoQuarto.DELUX,
                new BigDecimal("1.00"),
                "Teste",
                1L,
                List.of(new DadosCadastroCamaQuarto(1, 1L))
        );

        validador.validar(dados);
    }
}