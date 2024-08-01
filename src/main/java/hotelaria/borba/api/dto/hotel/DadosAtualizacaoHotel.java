package hotelaria.borba.api.dto.hotel;

import hotelaria.borba.api.enums.Categoria;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizacaoHotel(
        @NotNull
        Long id,
        String nome,
        @Nullable
        @Pattern(regexp = "\\d{15}")
        String telefone,
        @Nullable
        @Email
        String email,
        @Nullable
        String descricao,
        @Nullable
        Categoria categoria
) {
}
