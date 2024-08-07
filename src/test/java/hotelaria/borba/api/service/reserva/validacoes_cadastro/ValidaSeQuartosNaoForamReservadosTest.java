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

class ValidaSeQuartosNaoForamReservadosTest {

    @Mock
    private QuartoRepository quartoRepository;

    @InjectMocks
    private ValidaSeQuartosNaoForamReservados validador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verifica se valida lança exceção quando há quartos indisponíveis")
    void validarLancaExcecaoParaQuartosIndisponiveis() {
        LocalDate checkin = LocalDate.of(2024, 8, 1);
        LocalDate checkout = LocalDate.of(2024, 8, 5);
        DadosCadastroReserva dados = new DadosCadastroReserva(
                checkin,
                checkout,
                1L,
                List.of(new DadosCadastroQuartoReserva(1L), new DadosCadastroQuartoReserva(2L))
        );

        when(quartoRepository.listaTodosQuartosSemReservaNaDataEspecifica(checkin, checkout))
                .thenReturn(List.of(new Quarto()));

        assertThrows(ValidationException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Verifica se valida não lança exceção quando todos os quartos estão disponíveis")
    void validarNaoLancaExcecaoParaQuartosDisponiveis() {
        LocalDate checkin = LocalDate.of(2024, 8, 1);
        LocalDate checkout = LocalDate.of(2024, 8, 5);
        Quarto quarto1 = new Quarto(1L, 101, TipoQuarto.DELUX, new BigDecimal("249.9"), "teste", new Hotel(), null, null);
        Quarto quarto2 = new Quarto(2L, 101, TipoQuarto.DELUX, new BigDecimal("249.9"), "teste", new Hotel(), null, null);
        DadosCadastroReserva dados = new DadosCadastroReserva(
                checkin,
                checkout,
                1L,
                List.of(new DadosCadastroQuartoReserva(1L), new DadosCadastroQuartoReserva(2L))
        );

        when(quartoRepository.listaTodosQuartosSemReservaNaDataEspecifica(checkin, checkout)).thenReturn(List.of(quarto1, quarto2));

        assertDoesNotThrow(() -> validador.validar(dados));
    }
}