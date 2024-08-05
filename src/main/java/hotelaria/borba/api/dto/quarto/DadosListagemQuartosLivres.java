package hotelaria.borba.api.dto.quarto;

import java.time.LocalDate;

public record DadosListagemQuartosLivres(LocalDate checkin, LocalDate checkout) {
}
