package hotelaria.borba.api.dto.avaliacao;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoAvaliacao(
        @NotNull
        Long id,
        Integer nota,
        String descricao) {
}
