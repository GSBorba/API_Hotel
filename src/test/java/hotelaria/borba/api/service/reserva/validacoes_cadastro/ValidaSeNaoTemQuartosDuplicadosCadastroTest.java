package hotelaria.borba.api.service.reserva.validacoes_cadastro;

import hotelaria.borba.api.domain.Hotel;
import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import hotelaria.borba.api.enums.TipoQuarto;
import hotelaria.borba.api.repository.QuartoRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ValidaSeNaoTemQuartosDuplicadosCadastroTest {
    @Test
    @DisplayName("Verifica se valida lança exceção quando há quartos duplicados")
    void validarLancaExcecaoParaQuartosDuplicados() {
        ValidaSeNaoTemQuartosDuplicadosCadastro validador = new ValidaSeNaoTemQuartosDuplicadosCadastro();
        DadosCadastroReserva dados = new DadosCadastroReserva(
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 2),
                1L,
                List.of(new DadosCadastroQuartoReserva(1L), new DadosCadastroQuartoReserva(1L))
        );

        assertThrows(ValidationException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Verifica se valida não lança exceção quando não há quartos duplicados")
    void validarNaoLancaExcecaoParaQuartosNaoDuplicados() {
        ValidaSeNaoTemQuartosDuplicadosCadastro validador = new ValidaSeNaoTemQuartosDuplicadosCadastro();
        DadosCadastroReserva dados = new DadosCadastroReserva(
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 2),
                1L,
                List.of(new DadosCadastroQuartoReserva(1L), new DadosCadastroQuartoReserva(2L))
        );

        assertDoesNotThrow(() -> validador.validar(dados));
    }
}