package hotelaria.borba.api.service.quarto.validacoes_cadastro;

import hotelaria.borba.api.dto.quarto.DadosCadastroQuarto;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidaPrecoDiariaCadastro implements ValidadorCadastroDeQuartos{

    public void validar(DadosCadastroQuarto dados) {
        if(dados.precoDiaria().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Já existe um quarto nesse hotel com o mesmo número passado!");
        }
    }
}
