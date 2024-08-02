package hotelaria.borba.api.dto.avaliacao;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroAvaliacao(
        @NotNull
        Integer nota,
        String descricao,
        @NotNull
        Long id_reserva) {
}
