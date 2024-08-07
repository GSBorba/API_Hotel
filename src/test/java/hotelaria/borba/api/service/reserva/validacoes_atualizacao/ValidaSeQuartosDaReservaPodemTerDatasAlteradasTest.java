package hotelaria.borba.api.service.reserva.validacoes_atualizacao;

import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import hotelaria.borba.api.repository.QuartoRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidaSeQuartosDaReservaPodemTerDatasAlteradasTest {

    private final QuartoRepository quartoRepository = mock(QuartoRepository.class);
    private final ValidaSeQuartosDaReservaPodemTerDatasAlteradas validador = new ValidaSeQuartosDaReservaPodemTerDatasAlteradas(quartoRepository);

    @Test
    @DisplayName("Deve lançar ValidationException quando algum quarto está indisponível")
    void deveLancarExcecaoQuandoQuartoIndisponivel() {
        DadosAtualizacaoReserva dados = new DadosAtualizacaoReserva(
                1L,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 5),
                null
        );

        when(quartoRepository.retornaSePodeAtualizarDataDaReservaDoQuarto(dados.checkin(), dados.checkout(), dados.id()))
                .thenReturn(false);

        assertThrows(ValidationException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando todos os quartos estão disponíveis")
    void naoDeveLancarExcecaoQuandoQuartosDisponiveis() {
        DadosAtualizacaoReserva dados = new DadosAtualizacaoReserva(
                1L,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 5),
                null
        );

        when(quartoRepository.retornaSePodeAtualizarDataDaReservaDoQuarto(dados.checkin(), dados.checkout(), dados.id()))
                .thenReturn(true);

        assertDoesNotThrow(() -> validador.validar(dados));
    }
}