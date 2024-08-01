package hotelaria.borba.api.dto.cliente;

import hotelaria.borba.api.dto.endereco.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroCliente(
        @NotBlank
        String nome,
        @NotBlank
        @Pattern(regexp = "\\d{14}")
        String cpf,
        @NotBlank
        @Pattern(regexp = "\\d{15}")
        String telefone,
        @Email
        String email,
        @NotNull
        @Valid
        DadosEndereco endereco) {
}
