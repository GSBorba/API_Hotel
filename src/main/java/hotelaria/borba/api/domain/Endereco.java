package hotelaria.borba.api.domain;

import hotelaria.borba.api.dto.endereco.DadosEndereco;
import hotelaria.borba.api.enums.Estado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "endereco")
@Entity(name = "Endereco")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cep;
    @Enumerated(EnumType.STRING)
    private Estado uf;
    private String cidade;
    private String logradouro;
    private String complemento;
    private Integer numero;

    public Endereco(DadosEndereco dados) {
        this.cep = dados.cep();
        this.uf = dados.uf();
        this.cidade = dados.cidade();
        this.logradouro = dados.logradouro();
        this.complemento = dados.complemento();
        this.numero = dados.numero();
    }

    public void atualizarDados(DadosEndereco dados) {
        if(dados.cep() != null) {
            this.cep = dados.cep();
        }
        if(dados.uf() != null) {
            this.uf = dados.uf();
        }
        if(dados.cidade() != null) {
            this.cidade = dados.cidade();
        }
        if(dados.logradouro() != null) {
            this.logradouro = dados.logradouro();
        }
        if(dados.complemento() != null) {
            this.complemento = dados.complemento();
        }
        if(dados.numero() != null) {
            this.numero = dados.numero();
        }
    }
}