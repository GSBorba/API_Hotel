package hotelaria.borba.api.service.reserva.validacoes_cadastro;

import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class ValidaSeReservaNaoTemMaisDeTrintaDiasCadastro implements ValidadorCadastroDeReservas{

    public void validar(DadosCadastroReserva dados) {
        if(ChronoUnit.DAYS.between(dados.checkin(), dados.checkout()) > 30) {
            throw new ValidationException("Uma reserva não pode ter mais de 30 dias");
        }
    }
}
