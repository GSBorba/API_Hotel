package hotelaria.borba.api.dto.hotel;

import hotelaria.borba.api.dto.endereco.DadosEndereco;
import hotelaria.borba.api.enums.Categoria;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroHotel(
        @NotBlank
        String nome,
        @NotBlank
        @Pattern(regexp = "\\d{15}")
        String telefone,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String descricao,
        @NotNull
        Categoria categoria,
        @NotNull
        @Valid
        DadosEndereco endereco
) {
}
