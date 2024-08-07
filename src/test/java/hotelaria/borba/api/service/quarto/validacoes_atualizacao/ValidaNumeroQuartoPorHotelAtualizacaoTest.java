package hotelaria.borba.api.service.quarto.validacoes_atualizacao;

import hotelaria.borba.api.domain.Hotel;
import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.dto.cama_quarto.DTOsAtualizacaoCamaQuarto;
import hotelaria.borba.api.dto.quarto.DadosAtualizacaoQuarto;
import hotelaria.borba.api.enums.TipoQuarto;
import hotelaria.borba.api.repository.HotelRepository;
import hotelaria.borba.api.repository.QuartoRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidaNumeroQuartoPorHotelAtualizacaoTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private QuartoRepository quartoRepository;

    @InjectMocks
    private ValidaNumeroQuartoPorHotelAtualizacao validador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o número do quarto já existe no hotel durante a atualização")
    void deveLancarExcecaoQuandoNumeroQuartoJaExisteNoHotelDuranteAtualizacao() {
        Long idQuarto = 1L;
        DadosAtualizacaoQuarto dados = new DadosAtualizacaoQuarto(
                idQuarto,
                101,
                TipoQuarto.DELUX,
                new BigDecimal("249.90"),
                "Teste Atualização",
                new DTOsAtualizacaoCamaQuarto(null, null, null)
        );

        Quarto quarto = mock(Quarto.class);
        Hotel hotel = mock(Hotel.class);
        Quarto quartoExistente = new Quarto(101, TipoQuarto.DELUX, new BigDecimal("249.90"), "Teste", hotel);

        when(quarto.getHotel()).thenReturn(hotel); // Adicionando essa linha
        when(quartoRepository.getReferenceById(idQuarto)).thenReturn(quarto);
        when(hotelRepository.getReferenceById(hotel.getId())).thenReturn(hotel);
        when(hotel.getQuartos()).thenReturn(Set.of(quartoExistente));

        assertThrows(ValidationException.class, () -> validador.validar(dados));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando o número do quarto é único no hotel durante a atualização")
    void naoDeveLancarExcecaoQuandoNumeroQuartoEhUnicoNoHotelDuranteAtualizacao() {
        Long idQuarto = 1L;
        DadosAtualizacaoQuarto dados = new DadosAtualizacaoQuarto(
                idQuarto,
                102,
                TipoQuarto.STANDARD,
                new BigDecimal("199.90"),
                "Teste Atualização",
                new DTOsAtualizacaoCamaQuarto(null, null, null)
        );

        Quarto quarto = mock(Quarto.class);
        Hotel hotel = mock(Hotel.class);

        when(quarto.getHotel()).thenReturn(hotel); // Adicionando essa linha
        when(quartoRepository.getReferenceById(idQuarto)).thenReturn(quarto);
        when(hotelRepository.getReferenceById(hotel.getId())).thenReturn(hotel);
        when(hotel.getQuartos()).thenReturn(Set.of());

        validador.validar(dados);
    }
}