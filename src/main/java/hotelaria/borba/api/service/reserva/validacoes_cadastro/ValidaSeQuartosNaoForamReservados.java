package hotelaria.borba.api.service.reserva.validacoes_cadastro;

import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import hotelaria.borba.api.repository.QuartoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidaSeQuartosNaoForamReservados implements ValidadorCadastroDeReservas {

    private final QuartoRepository quartoRepository;

    @Autowired
    public ValidaSeQuartosNaoForamReservados(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    }

    public void validar(DadosCadastroReserva dados) {
        List<Quarto> quartos = quartoRepository.listaTodosQuartosSemReservaNaDataEspecifica(dados.checkin(), dados.checkout());

        Set<Long> idQuartos = quartos.stream()
                .map(Quarto::getId)
                .collect(Collectors.toSet());
        List<Long> idRecebidos = dados.quartos().stream()
                .map(DadosCadastroQuartoReserva::id_quarto)
                .toList();
        List<Long> idQuartosIndisponiveis = idRecebidos.stream()
                .filter(id -> !idQuartos.contains(id))
                .toList();
        if(!idQuartosIndisponiveis.isEmpty()){
            throw new ValidationException("O ID " + idQuartosIndisponiveis.get(0) + " é de um quarto indisponível para essa data ou não ele existe!");
        }
    }
}
