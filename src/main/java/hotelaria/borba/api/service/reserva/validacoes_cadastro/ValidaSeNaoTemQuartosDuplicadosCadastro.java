package hotelaria.borba.api.service.reserva.validacoes_cadastro;

import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ValidaSeNaoTemQuartosDuplicadosCadastro implements ValidadorCadastroDeReservas{


    public void validar(DadosCadastroReserva dados) {
        List<Long> idList = dados.quartos().stream()
                .map(DadosCadastroQuartoReserva::id_quarto)
                .toList();
        Set<Long> setList = new HashSet<>(idList);
        if(setList.size() != idList.size()) {
            throw new ValidationException("Foram adicionados quartos duplicados, remova-os para fazer o cadastro!");
        }
    }
}
