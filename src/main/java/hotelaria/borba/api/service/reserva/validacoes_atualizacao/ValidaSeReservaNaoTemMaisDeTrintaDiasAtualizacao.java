package hotelaria.borba.api.service.reserva.validacoes_atualizacao;

import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class ValidaSeReservaNaoTemMaisDeTrintaDiasAtualizacao implements ValidadorAtualizacaoDeReservas{

    public void validar(DadosAtualizacaoReserva dados) {
        if(ChronoUnit.DAYS.between(dados.checkin(), dados.checkout()) > 30) {
            throw new ValidationException("Uma reserva não pode ter mais de 30 dias");
        }
    }
}
