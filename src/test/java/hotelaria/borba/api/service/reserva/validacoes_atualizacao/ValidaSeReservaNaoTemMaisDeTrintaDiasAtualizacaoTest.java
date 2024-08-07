package hotelaria.borba.api.service.reserva.validacoes_atualizacao;

import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidaSeReservaNaoTemMaisDeTrintaDiasAtualizacaoTest {

    private final ValidaSeReservaNaoTemMaisDeTrintaDiasAtualizacao validador = new ValidaSeReservaNaoTemMaisDeTrintaDiasAtualizacao();

    @Test
    @DisplayName("Deve lançar ValidationException quando a reserva tiver mais de 30 dias")
    void deveLancarExcecaoQuandoReservaMaisDe30Dias() {
        DadosAtualizacaoReserva dados = new DadosAtualizacaoReserva(
                1L,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 9, 2), // mais de 30 dias
                null
        );

        assertThrows(ValidationException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando a reserva tem 30 dias ou menos")
    void naoDeveLancarExcecaoQuandoReserva30DiasOuMenos() {
        DadosAtualizacaoReserva dados = new DadosAtualizacaoReserva(
                1L,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 30), // 30 dias
                null
        );

        assertDoesNotThrow(() -> validador.validar(dados));
    }
}