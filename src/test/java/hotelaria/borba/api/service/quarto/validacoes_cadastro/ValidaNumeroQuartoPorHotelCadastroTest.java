package hotelaria.borba.api.service.quarto.validacoes_cadastro;

import hotelaria.borba.api.domain.Hotel;
import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.dto.cama_quarto.DadosCadastroCamaQuarto;
import hotelaria.borba.api.dto.quarto.DadosCadastroQuarto;
import hotelaria.borba.api.enums.TipoQuarto;
import hotelaria.borba.api.repository.HotelRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidaNumeroQuartoPorHotelCadastroTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private ValidaNumeroQuartoPorHotelCadastro validador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o número do quarto já existe no hotel")
    void deveLancarExcecaoQuandoNumeroQuartoJaExisteNoHotel() {
        Long idHotel = 1L;
        Integer numeroQuarto = 101;
        TipoQuarto tipoQuarto = TipoQuarto.DELUX;
        BigDecimal precoDiaria = new BigDecimal("249.90");
        String descricao = "Teste";
        List<DadosCadastroCamaQuarto> camas = List.of();
        DadosCadastroQuarto dados = new DadosCadastroQuarto(numeroQuarto, tipoQuarto, precoDiaria, descricao, idHotel, camas);

        Hotel hotel = mock(Hotel.class);
        Quarto quartoExistente = new Quarto(numeroQuarto, tipoQuarto, precoDiaria, descricao, hotel);

        when(hotelRepository.getReferenceById(idHotel)).thenReturn(hotel);
        when(hotel.getQuartos()).thenReturn(Set.of(quartoExistente));

        ValidationException exception = assertThrows(ValidationException.class, () -> validador.validar(dados));

        assertEquals("Já existe um quarto nesse hotel com o mesmo número passado!", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve lançar exceção quando o número do quarto é único no hotel")
    void naoDeveLancarExcecaoQuandoNumeroQuartoEhUnicoNoHotel() {
        Long idHotel = 1L;
        Integer numeroQuarto = 101;
        TipoQuarto tipoQuarto = TipoQuarto.DELUX;
        BigDecimal precoDiaria = new BigDecimal("249.90");
        String descricao = "Teste";
        List<DadosCadastroCamaQuarto> camas = List.of();
        DadosCadastroQuarto dados = new DadosCadastroQuarto(numeroQuarto, tipoQuarto, precoDiaria, descricao, idHotel, camas);

        Hotel hotel = mock(Hotel.class);

        when(hotelRepository.getReferenceById(idHotel)).thenReturn(hotel);
        when(hotel.getQuartos()).thenReturn(Set.of());

        assertDoesNotThrow(() -> validador.validar(dados));
    }
}