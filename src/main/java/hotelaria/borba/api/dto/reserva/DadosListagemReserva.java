package hotelaria.borba.api.dto.reserva;

import hotelaria.borba.api.domain.Reserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosListagemReserva(Long id, LocalDateTime checkin, LocalDateTime checkout, BigDecimal valor, String cliente) {

    public DadosListagemReserva(Reserva reserva) {
        this(
                reserva.getId(),
                reserva.getCheckin(),
                reserva.getCheckout(),
                reserva.getValor(),
                reserva.getCliente().getNome()
        );
    }
}
