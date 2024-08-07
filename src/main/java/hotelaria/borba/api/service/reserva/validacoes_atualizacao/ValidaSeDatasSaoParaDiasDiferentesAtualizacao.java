package hotelaria.borba.api.service.reserva.validacoes_atualizacao;

import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidaSeDatasSaoParaDiasDiferentesAtualizacao implements ValidadorAtualizacaoDeReservas{

    public void validar(DadosAtualizacaoReserva dados) {
        if(dados.checkin().isEqual(dados.checkout())){
            throw new ValidationException("As datas de check-in e check-out não podem ser para o mesmo dia!");
        }
    }
}
