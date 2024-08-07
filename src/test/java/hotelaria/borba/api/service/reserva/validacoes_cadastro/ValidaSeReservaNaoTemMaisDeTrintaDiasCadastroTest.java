package hotelaria.borba.api.service.reserva.validacoes_cadastro;

import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidaSeReservaNaoTemMaisDeTrintaDiasCadastroTest {

    @Test
    @DisplayName("Verifica se valida lança exceção quando a reserva tem mais de 30 dias")
    void validarLancaExcecaoParaReservaComMaisDeTrintaDias() {
        ValidaSeReservaNaoTemMaisDeTrintaDiasCadastro validador = new ValidaSeReservaNaoTemMaisDeTrintaDiasCadastro();
        DadosCadastroReserva dados = new DadosCadastroReserva(
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 9, 2),
                1L,
                List.of(new DadosCadastroQuartoReserva(1L))
        );

        assertThrows(ValidationException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Verifica se valida não lança exceção quando a reserva tem 30 dias ou menos")
    void validarNaoLancaExcecaoParaReservaComTrintaDiasOuMenos() {
        ValidaSeReservaNaoTemMaisDeTrintaDiasCadastro validador = new ValidaSeReservaNaoTemMaisDeTrintaDiasCadastro();
        DadosCadastroReserva dados = new DadosCadastroReserva(
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 31),
                1L,
                List.of(new DadosCadastroQuartoReserva(1L))
        );

        assertDoesNotThrow(() -> validador.validar(dados));
    }
}