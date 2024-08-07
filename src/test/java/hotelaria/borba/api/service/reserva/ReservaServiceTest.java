package hotelaria.borba.api.service.reserva;

import hotelaria.borba.api.domain.Cliente;
import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.domain.QuartoReserva;
import hotelaria.borba.api.domain.Reserva;
import hotelaria.borba.api.dto.quarto_reserva.DTOsAtualizacaoQuartoReserva;
import hotelaria.borba.api.dto.quarto_reserva.DadosAtualizacaoQuartoReserva;
import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import hotelaria.borba.api.repository.ClienteRepository;
import hotelaria.borba.api.repository.QuartoRepository;
import hotelaria.borba.api.repository.QuartoReservaRepository;
import hotelaria.borba.api.repository.ReservaRepository;
import hotelaria.borba.api.service.reserva.validacoes_atualizacao.ValidadorAtualizacaoDeReservas;
import hotelaria.borba.api.service.reserva.validacoes_cadastro.ValidadorCadastroDeReservas;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private QuartoRepository quartoRepository;

    @Mock
    private QuartoReservaRepository quartoReservaRepository;

    @Mock
    private List<ValidadorCadastroDeReservas> validadorCadastroDeReservas;

    @Mock
    private List<ValidadorAtualizacaoDeReservas> validadorAtualizacaoDeReservas;

    @InjectMocks
    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verifica se o método validarCadastro lança exceção quando o ID do cliente não existe")
    void validarCadastroLancaExcecaoParaIdClienteInexistente() {
        DadosCadastroReserva dadosCadastro = new DadosCadastroReserva(
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 5),
                999L,
                List.of(new DadosCadastroQuartoReserva(1L))
        );

        when(clienteRepository.existsById(999L)).thenReturn(false);

        assertThrows(ValidationException.class, () -> reservaService.validarCadastro(dadosCadastro));
    }

    @Test
    @DisplayName("Verifica se o método validarCadastro salva uma nova reserva corretamente")
    void validarCadastroSalvaReservaCorretamente() {
        Long idCliente = 1L;
        DadosCadastroReserva dadosCadastro = new DadosCadastroReserva(
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 5),
                idCliente,
                List.of(new DadosCadastroQuartoReserva(1L))
        );

        Cliente cliente = mock(Cliente.class);
        when(clienteRepository.existsById(idCliente)).thenReturn(true);
        when(clienteRepository.getReferenceById(idCliente)).thenReturn(cliente);

        Quarto quarto = mock(Quarto.class);
        when(quarto.getPrecoDiaria()).thenReturn(BigDecimal.valueOf(100));
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));

        Reserva reserva = reservaService.validarCadastro(dadosCadastro);

        assertNotNull(reserva);
        assertEquals(cliente, reserva.getCliente());
        assertEquals(BigDecimal.valueOf(400), reserva.getValor());
        assertEquals(1, reserva.getQuartoReservas().size());

        verify(reservaRepository).save(reserva);
        verify(quartoReservaRepository).save(any(QuartoReserva.class));
    }

    @Test
    @DisplayName("Verifica se o método adicionarQuarto lança exceção quando o ID do quarto não existe")
    void adicionarQuartoLancaExcecaoParaIdQuartoInexistente() {
        DadosCadastroQuartoReserva dadosQuarto = new DadosCadastroQuartoReserva(999L);
        List<DadosCadastroQuartoReserva> dadosQuartos = List.of(dadosQuarto);
        Reserva reserva = mock(Reserva.class);
        BigDecimal valor = BigDecimal.ZERO;

        when(quartoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> reservaService.adicionarQuarto(dadosQuartos, reserva, valor));
    }

    @Test
    @DisplayName("Verifica se o método adicionarQuarto adiciona um quarto corretamente")
    void adicionarQuartoAdicionaQuartoCorretamente() {
        DadosCadastroQuartoReserva dadosQuarto = new DadosCadastroQuartoReserva(1L);
        List<DadosCadastroQuartoReserva> dadosQuartos = List.of(dadosQuarto);
        Reserva reserva = new Reserva(LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 5), BigDecimal.ZERO, mock(Cliente.class));
        BigDecimal valor = BigDecimal.ZERO;

        Quarto quarto = mock(Quarto.class);
        when(quarto.getPrecoDiaria()).thenReturn(BigDecimal.valueOf(100));
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));

        BigDecimal novoValor = reservaService.adicionarQuarto(dadosQuartos, reserva, valor);

        assertEquals(BigDecimal.valueOf(100), novoValor);
        assertEquals(1, reserva.getQuartoReservas().size());

        verify(quartoReservaRepository).save(any(QuartoReserva.class));
    }

    @Test
    @DisplayName("Verifica se o método validarAtualizacao atualiza uma reserva corretamente")
    void validarAtualizacaoAtualizaReservaCorretamente() {
        Long idReserva = 1L;
        DadosAtualizacaoReserva dadosAtualizacao = new DadosAtualizacaoReserva(
                idReserva,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 5),
                new DTOsAtualizacaoQuartoReserva(
                        List.of(new DadosCadastroQuartoReserva(1L)),
                        List.of(new DadosAtualizacaoQuartoReserva(1L, 2L)),
                        List.of(1L)
                )
        );

        Reserva reserva = mock(Reserva.class);
        when(reservaRepository.getReferenceById(idReserva)).thenReturn(reserva);

        Quarto quarto = mock(Quarto.class);
        when(quarto.getPrecoDiaria()).thenReturn(BigDecimal.TEN);
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(quartoRepository.findById(2L)).thenReturn(Optional.of(quarto));

        QuartoReserva quartoReserva = mock(QuartoReserva.class);
        when(quartoReserva.getQuarto()).thenReturn(quarto);
        when(quartoReserva.getPrecoDiaria()).thenReturn(BigDecimal.TEN);
        when(quartoReservaRepository.findById(1L)).thenReturn(Optional.of(quartoReserva));

        doNothing().when(reserva).atualizarInformacoes(dadosAtualizacao);

        Reserva reservaAtualizada = reservaService.validarAtualizacao(dadosAtualizacao);

        verify(reserva).atualizarInformacoes(dadosAtualizacao);
        assertEquals(reserva, reservaAtualizada);
    }

    @Test
    @DisplayName("Verifica se o método validarAtualizacao lança exceção quando o ID do quarto não existe")
    void validarAtualizacaoLancaExcecaoParaIdQuartoInexistente() {
        Long idReserva = 1L;
        DadosAtualizacaoReserva dadosAtualizacao = new DadosAtualizacaoReserva(
                idReserva,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 5),
                new DTOsAtualizacaoQuartoReserva(
                        List.of(new DadosCadastroQuartoReserva(999L)),
                        null,
                        null
                )
        );

        when(reservaRepository.getReferenceById(idReserva)).thenReturn(mock(Reserva.class));
        when(quartoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> reservaService.validarAtualizacao(dadosAtualizacao));
    }

    @Test
    @DisplayName("Verifica se o método validarAtualizacao lança exceção quando o ID do quartoReserva não existe")
    void validarAtualizacaoLancaExcecaoParaIdQuartoReservaInexistente() {
        Long idReserva = 1L;
        DadosAtualizacaoReserva dadosAtualizacao = new DadosAtualizacaoReserva(
                idReserva,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 5),
                new DTOsAtualizacaoQuartoReserva(
                        null,
                        List.of(new DadosAtualizacaoQuartoReserva(999L, 1L)),
                        null
                )
        );

        when(reservaRepository.getReferenceById(idReserva)).thenReturn(mock(Reserva.class));
        when(quartoReservaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> reservaService.validarAtualizacao(dadosAtualizacao));
    }

    @Test
    @DisplayName("Verifica se o método validarAtualizacao lança exceção quando o ID do quartoReserva a ser deletado não existe")
    void validarAtualizacaoLancaExcecaoParaIdQuartoReservaDeletadoInexistente() {
        Long idReserva = 1L;
        DadosAtualizacaoReserva dadosAtualizacao = new DadosAtualizacaoReserva(
                idReserva,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 5),
                new DTOsAtualizacaoQuartoReserva(
                        null,
                        null,
                        List.of(999L)
                )
        );

        when(reservaRepository.getReferenceById(idReserva)).thenReturn(mock(Reserva.class));
        when(quartoReservaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> reservaService.validarAtualizacao(dadosAtualizacao));
    }
}