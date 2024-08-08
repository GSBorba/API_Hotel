package hotelaria.borba.api.service.quarto;

import hotelaria.borba.api.dto.cama_quarto.DTOsAtualizacaoCamaQuarto;
import hotelaria.borba.api.domain.Cama;
import hotelaria.borba.api.domain.CamaQuarto;
import hotelaria.borba.api.domain.Hotel;
import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.dto.cama_quarto.DadosAtualizacaoCamaQuarto;
import hotelaria.borba.api.dto.cama_quarto.DadosCadastroCamaQuarto;
import hotelaria.borba.api.dto.quarto.DadosAtualizacaoQuarto;
import hotelaria.borba.api.dto.quarto.DadosCadastroQuarto;
import hotelaria.borba.api.enums.Estado;
import hotelaria.borba.api.enums.TipoQuarto;
import hotelaria.borba.api.repository.CamaQuartoRepository;
import hotelaria.borba.api.repository.CamaRepository;
import hotelaria.borba.api.repository.HotelRepository;
import hotelaria.borba.api.repository.QuartoRepository;
import hotelaria.borba.api.service.quarto.validacoes_atualizacao.ValidadorAtualizacaoDeQuartos;
import hotelaria.borba.api.service.quarto.validacoes_cadastro.ValidadorCadastroDeQuartos;
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
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuartoServiceTest {

    @Mock
    private QuartoRepository quartoRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private CamaRepository camaRepository;

    @Mock
    private CamaQuartoRepository camaQuartoRepository;

    @Mock
    private List<ValidadorCadastroDeQuartos> validadorCadastroDeQuartos;

    @Mock
    private List<ValidadorAtualizacaoDeQuartos> validadorAtualizacaoDeQuartos;

    @InjectMocks
    private QuartoService service;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Vefifica se conseguie salvar um quarto no banco de dados")
    void validaCadastroConsegueSalvarQuarto() {
        Random random = new Random();
        final Long idHotel = random.nextLong(100) + 1;
        final Long idCama = random.nextLong(100) + 1;
        final Integer qtCamas = random.nextInt(10) + 1;
        final Cama cama = mock(Cama.class);
        final Hotel hotel = mock(Hotel.class);
        final Integer numero = 101;
        final TipoQuarto tipoQuarto = TipoQuarto.DELUX;
        final BigDecimal precoDiaria = new BigDecimal("249.90");
        final String descricao = "Teste";
        final DadosCadastroCamaQuarto camasQuarto = new DadosCadastroCamaQuarto(qtCamas, idCama);
        final List<DadosCadastroCamaQuarto> camas = List.of(camasQuarto);

        when(hotelRepository.existsById(idHotel)).thenReturn(true);
        when(hotelRepository.getReferenceById(idHotel)).thenReturn(hotel);
        when(camaRepository.findById(idCama)).thenReturn(Optional.of(cama));

        Quarto quartoResponse = service.validarCadastro(new DadosCadastroQuarto(numero, tipoQuarto, precoDiaria, descricao, idHotel, camas));
        CamaQuarto camaQuarto = new CamaQuarto(qtCamas, cama, quartoResponse);

        verify(quartoRepository).save(any(Quarto.class));
        verify(camaQuartoRepository).save(any(CamaQuarto.class));
    }

    @Test
    @DisplayName("Verifica se o método validarCadastro lança exceção para id de hotel inválido")
    void validarCadastroThrowsExceptionForInvalidHotelId() {
        DadosCadastroQuarto dados = new DadosCadastroQuarto(101, TipoQuarto.DELUX, new BigDecimal("249.90"), "Teste", 999L, List.of());

        when(hotelRepository.existsById(dados.id_hotel())).thenReturn(false);

        assertThrows(ValidationException.class, () -> service.validarCadastro(dados));
    }

    @Test
    @DisplayName("Verifica se validaCadastro lança exceção para id de cama inválido")
    void validarCadastroThrowsExceptionForInvalidCamaId() {
        Random random = new Random();
        Long idHotel = random.nextLong(100) + 1;
        Long idCama = random.nextLong(100) + 1;
        Integer qtCamas = random.nextInt(10) + 1;
        DadosCadastroCamaQuarto camasQuarto = new DadosCadastroCamaQuarto(qtCamas, idCama);
        DadosCadastroQuarto dados = new DadosCadastroQuarto(101, TipoQuarto.DELUX, new BigDecimal("249.90"), "Teste", idHotel, List.of(camasQuarto));

        when(hotelRepository.existsById(idHotel)).thenReturn(true);
        when(hotelRepository.getReferenceById(idHotel)).thenReturn(mock(Hotel.class));
        when(camaRepository.findById(idCama)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.validarCadastro(dados));
    }

    @Test
    @DisplayName("Verifica se o método validarAtualizacao atualiza corretamente um quarto")
    void verifyIfCanUpdateTheRoom() {
        Long idQuarto = 1L;
        Quarto quarto = mock(Quarto.class);
        DadosAtualizacaoQuarto dados = new DadosAtualizacaoQuarto(idQuarto, 102, TipoQuarto.STANDARD, new BigDecimal("199.90"), "Teste Atualização", new DTOsAtualizacaoCamaQuarto(null, null, null));

        when(quartoRepository.getReferenceById(idQuarto)).thenReturn(quarto);

        Quarto quartoResponse = service.validarAtualizacao(dados);

        verify(quarto).atualizarInformacoes(dados);
        assertEquals(quarto, quartoResponse);
    }

    @Test
    @DisplayName("Verifica se validarAtualizacao lança exceção para id de CamaQuarto inválido na atualização")
    void validarAtualizacaoThrowsExceptionForInvalidCamaQuartoIdOnUpdate() {
        Random random = new Random();
        Long idQuarto = random.nextLong(100) + 1;
        Long idCamaQuarto = random.nextLong(100) + 1;
        DadosAtualizacaoCamaQuarto atualizarCamaQuarto = new DadosAtualizacaoCamaQuarto(idCamaQuarto, 2, null);
        DTOsAtualizacaoCamaQuarto dadosAtualizacaoCama = new DTOsAtualizacaoCamaQuarto(null, List.of(atualizarCamaQuarto), null);
        DadosAtualizacaoQuarto dados = new DadosAtualizacaoQuarto(idQuarto, 102, TipoQuarto.STANDARD, new BigDecimal("199.90"), "Teste Atualização", dadosAtualizacaoCama);

        when(quartoRepository.getReferenceById(idQuarto)).thenReturn(mock(Quarto.class));
        when(camaQuartoRepository.findById(idCamaQuarto)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.validarAtualizacao(dados));
    }

    @Test
    @DisplayName("Verifica se validarAtualizacao lança exceção para id de CamaQuarto inválido na deleção")
    void validarAtualizacaoThrowsExceptionForInvalidCamaQuartoIdOnDelete() {
        Random random = new Random();
        Long idQuarto = random.nextLong(100) + 1;
        Long idCamaQuarto = random.nextLong(100) + 1;
        DTOsAtualizacaoCamaQuarto dadosAtualizacaoCama = new DTOsAtualizacaoCamaQuarto(null, null, List.of(idCamaQuarto));
        DadosAtualizacaoQuarto dados = new DadosAtualizacaoQuarto(idQuarto, 102, TipoQuarto.STANDARD, new BigDecimal("199.90"), "Teste Atualização", dadosAtualizacaoCama);

        when(quartoRepository.getReferenceById(idQuarto)).thenReturn(mock(Quarto.class));
        when(camaQuartoRepository.findById(idCamaQuarto)).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> service.validarAtualizacao(dados));

        assertEquals("Id da camaQuarto informado não existe!", exception.getMessage());
    }

    @Test
    @DisplayName("Verifica se o método listaQuartoPorHotelValor retorna a lista correta de quartos")
    void verifyListRoomsByHotelAndValue() {
        LocalDate checkin = LocalDate.now();
        LocalDate checkout = checkin.plusDays(5);
        Double valor = 200.0;
        List<Quarto> quartos = List.of(mock(Quarto.class), mock(Quarto.class));

        when(quartoRepository.listaQuartosPorHotelValor(checkin, checkout, Estado.RJ, valor)).thenReturn(quartos);

        List<Quarto> response = service.listaQuartoPorHotelValor(checkin, checkout, Estado.RJ, valor);

        assertEquals(quartos, response);
    }

    @Test
    @DisplayName("Verifica se o método isThisQuartoValid retorna verdadeiro para um quarto existente")
    void verifyIfRoomExists() {
        Long idQuarto = 1L;

        when(quartoRepository.existsById(idQuarto)).thenReturn(true);

        assertTrue(service.isThisQuartoValid(idQuarto));
    }

    @Test
    @DisplayName("Verifica se o método isThisQuartoValid retorna falso para um quarto inexistente")
    void verifyIfRoomDoesNotExist() {
        Long idQuarto = 1L;

        when(quartoRepository.existsById(idQuarto)).thenReturn(false);

        assertFalse(service.isThisQuartoValid(idQuarto));
    }
}