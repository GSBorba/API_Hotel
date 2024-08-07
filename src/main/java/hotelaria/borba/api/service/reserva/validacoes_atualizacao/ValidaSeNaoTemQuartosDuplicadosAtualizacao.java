package hotelaria.borba.api.service.reserva.validacoes_atualizacao;

import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ValidaSeNaoTemQuartosDuplicadosAtualizacao implements ValidadorAtualizacaoDeReservas{

    public void validar(DadosAtualizacaoReserva dados) {
        List<Long> idList = dados.quartos().cadastroQuartoReserva().stream()
                .map(DadosCadastroQuartoReserva::id_quarto)
                .toList();
        Set<Long> setList = new HashSet<>(idList);
        if(setList.size() != idList.size()) {
            throw new ValidationException("Foram adicionados quartos duplicados, remova-os para fazer o cadastro!");
        }
    }
}
