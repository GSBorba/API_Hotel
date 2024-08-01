package hotelaria.borba.api.domain;

import hotelaria.borba.api.dto.quarto.DadosAtualizacaoQuarto;
import hotelaria.borba.api.enums.TipoQuarto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Table(name = "quarto")
@Entity(name = "Quarto")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numero;
    @Enumerated(EnumType.STRING)
    private TipoQuarto tipoQuarto;
    private BigDecimal preco_diaria;
    private String descricao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hotel")
    private Hotel hotel;

    @OneToMany(mappedBy = "quarto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CamaQuarto> camaQuarto;
    @OneToMany(mappedBy = "quarto")
    private Set<QuartoReserva> quartoReserva;

    public Quarto(Integer numero, TipoQuarto tipoQuarto, BigDecimal preco_diaria, String descricao, Hotel hotel) {
        this.numero = numero;
        this.tipoQuarto = tipoQuarto;
        this.preco_diaria = preco_diaria;
        this.descricao = descricao;
        this.hotel = hotel;
        this.camaQuarto = new HashSet<>();
        this.quartoReserva = new HashSet<>();
    }

    public void atualizarInformacoes(DadosAtualizacaoQuarto dados) {
        if(dados.numero() != null) {
            this.numero = dados.numero();
        }
        if(dados.tipoQuarto() != null) {
            this.tipoQuarto = dados.tipoQuarto();
        }
        if(dados.preco_diaria() != null) {
            this.preco_diaria = dados.preco_diaria();
        }
        if(dados.descricao() != null) {
            this.descricao = dados.descricao();
        }
    }
}
