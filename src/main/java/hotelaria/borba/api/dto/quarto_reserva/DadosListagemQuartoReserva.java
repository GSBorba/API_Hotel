package hotelaria.borba.api.dto.quarto_reserva;

import hotelaria.borba.api.domain.QuartoReserva;

import java.math.BigDecimal;

public record DadosListagemQuartoReserva(Long id, BigDecimal preco, String tipoQuarto, Integer numero) {

    public DadosListagemQuartoReserva(QuartoReserva quartoReserva) {
        this(quartoReserva.getId(), quartoReserva.getPrecoDiaria(), quartoReserva.getQuarto().getTipoQuarto().getNomeTipo(), quartoReserva.getQuarto().getNumero());
    }
}
