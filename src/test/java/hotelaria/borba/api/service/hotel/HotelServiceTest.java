package hotelaria.borba.api.service.hotel;

import hotelaria.borba.api.domain.Hotel;
import hotelaria.borba.api.enums.Categoria;
import hotelaria.borba.api.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar a lista de hotéis")
    void deveRetornarListaDeHoteis() {
        List<Hotel> hoteisMock = Arrays.asList(
                new Hotel(1L, "Hotel A", "123456789", "emailA@hotel.com", "Descrição A", Categoria.CINCO, null, null),
                new Hotel(2L, "Hotel B", "987654321", "emailB@hotel.com", "Descrição B", Categoria.QUATRO, null, null)
        );

        when(hotelRepository.findAll()).thenReturn(hoteisMock);

        List<Hotel> hoteis = hotelService.listaHoteis();
        assertEquals(2, hoteis.size());
        assertEquals("Hotel A", hoteis.get(0).getNome());
        assertEquals("Hotel B", hoteis.get(1).getNome());
    }

    @Test
    @DisplayName("Deve retornar verdadeiro se o hotel existir")
    void deveRetornarVerdadeiroSeHotelExistir() {
        String messageText = "1";
        when(hotelRepository.existsById(1L)).thenReturn(true);

        boolean result = hotelService.isThisHotelValid(messageText);
        assertTrue(result);
    }

    @Test
    @DisplayName("Deve retornar falso se o hotel não existir")
    void deveRetornarFalsoSeHotelNaoExistir() {
        String messageText = "1";
        when(hotelRepository.existsById(1L)).thenReturn(false);

        boolean result = hotelService.isThisHotelValid(messageText);
        assertFalse(result);
    }

    @Test
    @DisplayName("Deve lançar exceção ao passar texto inválido para isThisHotelValid")
    void deveLancarExcecaoAoPassarTextoInvalidoParaIsThisHotelValid() {
        String invalidMessageText = "invalid";

        assertThrows(NumberFormatException.class, () -> {
            hotelService.isThisHotelValid(invalidMessageText);
        });
    }
}