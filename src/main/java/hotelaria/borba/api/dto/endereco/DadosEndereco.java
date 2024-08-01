package hotelaria.borba.api.dto.endereco;

import hotelaria.borba.api.enums.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(
        @NotBlank
        @Pattern(regexp = "\\d{9}")
        String cep,
        @NotNull
        @Pattern(regexp = "\\d{2}")
        Estado uf,
        @NotBlank
        String cidade,
        @NotBlank
        String logradouro,
        String complemento,
        Integer numero) {
}
