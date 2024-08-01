package hotelaria.borba.api.domain;

import hotelaria.borba.api.dto.hotel.DadosAtualizacaoHotel;
import hotelaria.borba.api.dto.hotel.DadosCadastroHotel;
import hotelaria.borba.api.enums.Categoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Table(name = "hotel")
@Entity(name = "Hotel")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String descricao;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;

    @OneToMany(mappedBy = "hotel")
    private Set<Quarto> quartos;

    public Hotel(DadosCadastroHotel dados) {
        this.nome = dados.nome();
        this.telefone = dados.telefone();
        this.email = dados.email();
        this.descricao = dados.descricao();
        this.categoria = dados.categoria();
        this.endereco = new Endereco(dados.endereco());
        this.quartos = new HashSet<>();
    }

    public void atualizar(DadosAtualizacaoHotel dados) {
        if(dados.nome() != null) {
            this.nome = dados.nome();
        }
        if(dados.telefone() != null) {
            this.telefone = dados.telefone();
        }
        if(dados.email() != null) {
            this.email = dados.email();
        }
        if(dados.descricao() != null) {
            this.descricao = dados.descricao();
        }
        if(dados.categoria() != null) {
            this.categoria = dados.categoria();
        }
    }
}
