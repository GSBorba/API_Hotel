package hotelaria.borba.api.domain;

import hotelaria.borba.api.dto.cama_quarto.DadosAtualizacaoCamaQuarto;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "cama_quarto")
@Entity(name = "CamaQuarto")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CamaQuarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer qt_cama;
    @ManyToOne
    @JoinColumn(name = "id_cama")
    private Cama cama;
    @ManyToOne
    @JoinColumn(name = "id_quarto")
    private Quarto quarto;

    public CamaQuarto(Integer qt_cama, Cama cama, Quarto quarto) {
        this.qt_cama = qt_cama;
        this.cama = cama;
        this.quarto = quarto;
    }

    public void atualizarInformacao(Integer qt_cama, Cama cama) {
        if(qt_cama != null) {
            this.qt_cama = qt_cama;
        }
        if(cama != null) {
            this.cama = cama;
        }
    }
}
