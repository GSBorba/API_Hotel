package hotelaria.borba.api.service.quarto.validacoes_atualizacao;

import hotelaria.borba.api.dto.quarto.DadosAtualizacaoQuarto;
import hotelaria.borba.api.service.quarto.validacoes_cadastro.ValidadorCadastroDeQuartos;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidaPrecoDiariaAtualizacao implements ValidadorAtualizacaoDeQuartos {

    public void validar(DadosAtualizacaoQuarto dados) {
        if(dados.precoDiaria().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Já existe um quarto nesse hotel com o mesmo número passado!");
        }
    }
}
