package hotelaria.borba.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "quarto_reserva")
@Entity(name = "QuartoReserva")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class QuartoReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal precoDiaria;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_quarto")
    private Quarto quarto;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    public QuartoReserva(BigDecimal precoDiaria, Quarto quarto, Reserva reserva) {
        this.precoDiaria = precoDiaria;
        this.quarto = quarto;
        this.reserva = reserva;
    }

    public void atualizarInformacoes(Quarto quarto) {
        if(quarto != null) {
            this.quarto = quarto;
            this.precoDiaria = quarto.getPrecoDiaria();
        }
    }
}
