package hotelaria.borba.api.service.reserva.validacoes_atualizacao;

import hotelaria.borba.api.dto.quarto_reserva.DTOsAtualizacaoQuartoReserva;
import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidaSeNaoTemQuartosDuplicadosAtualizacaoTest {

    private final ValidaSeNaoTemQuartosDuplicadosAtualizacao validador = new ValidaSeNaoTemQuartosDuplicadosAtualizacao();

    @Test
    @DisplayName("Deve lançar ValidationException quando houver quartos duplicados")
    void deveLancarExcecaoQuandoQuartosDuplicados() {
        DadosAtualizacaoReserva dados = new DadosAtualizacaoReserva(
                1L,
                null,
                null,
                new DTOsAtualizacaoQuartoReserva(
                        List.of(new DadosCadastroQuartoReserva(1L), new DadosCadastroQuartoReserva(1L)),
                        null,
                        null
                )
        );

        assertThrows(ValidationException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando não houver quartos duplicados")
    void naoDeveLancarExcecaoQuandoNaoHaQuartosDuplicados() {
        DadosAtualizacaoReserva dados = new DadosAtualizacaoReserva(
                1L,
                null,
                null,
                new DTOsAtualizacaoQuartoReserva(
                        List.of(new DadosCadastroQuartoReserva(1L), new DadosCadastroQuartoReserva(2L)),
                        null,
                        null
                )
        );

        assertDoesNotThrow(() -> validador.validar(dados));
    }
}