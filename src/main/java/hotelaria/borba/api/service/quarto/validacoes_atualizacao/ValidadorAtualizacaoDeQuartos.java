package hotelaria.borba.api.service.quarto.validacoes_atualizacao;

import hotelaria.borba.api.dto.quarto.DadosAtualizacaoQuarto;

public interface ValidadorAtualizacaoDeQuartos {

    void validar(DadosAtualizacaoQuarto dados);
}
