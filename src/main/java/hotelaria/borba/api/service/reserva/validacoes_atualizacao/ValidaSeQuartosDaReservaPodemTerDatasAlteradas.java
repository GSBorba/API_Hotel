package hotelaria.borba.api.service.reserva.validacoes_atualizacao;

import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import hotelaria.borba.api.repository.QuartoRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidaSeQuartosDaReservaPodemTerDatasAlteradas implements ValidadorAtualizacaoDeReservas{

    private final QuartoRepository quartoRepository;

    public ValidaSeQuartosDaReservaPodemTerDatasAlteradas(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    }


    public void validar(DadosAtualizacaoReserva dados) {
        if(!quartoRepository.retornaSePodeAtualizarDataDaReservaDoQuarto(dados.checkin(), dados.checkout(), dados.id())){
            throw new ValidationException("Algum quarto esta indisponível na data desejada!");
        }
    }
}
