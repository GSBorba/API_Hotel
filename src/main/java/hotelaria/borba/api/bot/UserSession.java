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

    public UserSession() {
        this.state = UserState.START;
    }

}
