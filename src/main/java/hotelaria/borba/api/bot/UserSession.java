package hotelaria.borba.api.bot;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class UserSession {

    private UserState state;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String cep;
    private String uf;
    private String cidade;
    private String logradouro;
    private Integer numero;
    private String complemento;

    public UserSession() {
        this.state = UserState.START;
    }

}
