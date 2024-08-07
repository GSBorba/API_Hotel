package hotelaria.borba.api.service.reserva.validacoes_atualizacao;

import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidaSeDatasSaoParaDiasDiferentesAtualizacaoTest {

    private final ValidaSeDatasSaoParaDiasDiferentesAtualizacao validador = new ValidaSeDatasSaoParaDiasDiferentesAtualizacao();

    @Test
    @DisplayName("Deve lançar ValidationException quando checkin e checkout forem o mesmo dia")
    void deveLancarExcecaoQuandoDatasSaoIguais() {
        DadosAtualizacaoReserva dados = new DadosAtualizacaoReserva(
                1L,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 1),
                null
        );

        assertThrows(ValidationException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando checkin e checkout forem dias diferentes")
    void naoDeveLancarExcecaoQuandoDatasSaoDiferentes() {
        DadosAtualizacaoReserva dados = new DadosAtualizacaoReserva(
                1L,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 2),
                null
        );

        assertDoesNotThrow(() -> validador.validar(dados));
    }
}