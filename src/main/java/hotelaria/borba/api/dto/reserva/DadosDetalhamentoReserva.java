package hotelaria.borba.api.dto.reserva;

import hotelaria.borba.api.domain.Reserva;
import hotelaria.borba.api.dto.quarto_reserva.DadosListagemQuartoReserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record DadosDetalhamentoReserva(Long id, LocalDate checkin, LocalDate checkout, BigDecimal valor, String cliente, List<DadosListagemQuartoReserva> quartos) {

    public DadosDetalhamentoReserva(Reserva reserva) {
        this(
                reserva.getId(),
                reserva.getCheckin(),
                reserva.getCheckout(),
                reserva.getValor(),
                reserva.getCliente().getNome(),
                reserva.getQuartoReservas().stream()
                        .map(DadosListagemQuartoReserva::new)
                        .collect(Collectors.toList())
        );
    }
}
