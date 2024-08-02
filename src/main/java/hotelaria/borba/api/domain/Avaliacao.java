package hotelaria.borba.api.domain;

import hotelaria.borba.api.dto.avaliacao.DadosAtualizacaoAvaliacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "avaliacao")
@Entity(name = "Avaliacao")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer nota;
    private String descricao;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    public Avaliacao(Integer nota, String descricao, Reserva reserva) {
        this.nota = nota;
        this.descricao = descricao;
        this.reserva = reserva;
    }

    public void atualizarInformacoes(DadosAtualizacaoAvaliacao dados) {
        if(dados.nota() != null) {
            this.nota = dados.nota();
        }
        if(dados.descricao() != null) {
            this.descricao = dados.descricao();
        }
    }
}
