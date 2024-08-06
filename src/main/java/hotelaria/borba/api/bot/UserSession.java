package hotelaria.borba.api.bot;

import hotelaria.borba.api.domain.Cliente;
import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class UserSession {

    private UserState state;
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private LocalDate checkin;
    private LocalDate checkout;
    private Long Hotel;
    private List<DadosCadastroQuartoReserva> quartos = new ArrayList<>();
    private Double orcamento;

    public UserSession() {
        this.state = UserState.START;
    }

    public void setCliente(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.telefone = cliente.getTelefone();
        this.email = cliente.getEmail();
    }
}
