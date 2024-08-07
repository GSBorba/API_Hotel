package hotelaria.borba.api.service.reserva.validacoes_cadastro;

import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidaSeDatasSaoParaDiasDiferentesCadastro implements ValidadorCadastroDeReservas {

    public void validar(DadosCadastroReserva dados) {
        if(dados.checkin().isEqual(dados.checkout())){
            throw new ValidationException("As datas de check-in e check-out não podem ser para o mesmo dia!");
        }
    }
}
