package hotelaria.borba.api.dto.cliente;

import hotelaria.borba.api.dto.endereco.DadosEndereco;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizacaoCliente(
        @NotNull
        Long id,
        @Nullable
        @Pattern(regexp = "\\d{15}")
        String telefone,
        @Nullable
        @Email
        String email,
        @Nullable
        Boolean ativo,
        @Nullable
        DadosEndereco endereco
) {
}
