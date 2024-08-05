package hotelaria.borba.api.domain;

import hotelaria.borba.api.dto.cliente.DadosAtualizacaoCliente;
import hotelaria.borba.api.dto.cliente.DadosCadastroCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Table(name = "cliente")
@Entity(name = "Cliente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private Boolean ativo;

    @OneToMany(mappedBy = "cliente")
    private Set<Reserva> reservas;

    public Cliente(DadosCadastroCliente dados) {
        this.nome = dados.nome();
        this.cpf = dados.cpf();
        this.telefone = dados.telefone();
        this.email = dados.email();
        this.ativo = true;
    }

    public void atualizarInformacoes(DadosAtualizacaoCliente dados) {
        if(dados.telefone() != null) {
            this.telefone = dados.telefone();
        }
        if(dados.email() != null) {
            this.email = dados.email();
        }
        if(dados.ativo() != null && dados.ativo()) {
            this.ativo = true;
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}
