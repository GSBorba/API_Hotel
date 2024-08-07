package hotelaria.borba.api.domain;

import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Table(name = "reserva")
@Entity(name = "Reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate checkin;
    private LocalDate checkout;
    private BigDecimal valor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<QuartoReserva> quartoReservas;

    public Reserva(LocalDate checkin, LocalDate checkout, BigDecimal valor, Cliente cliente) {
        this.checkin = checkin;
        this.checkout = checkout;
        this.valor = valor;
        this.cliente = cliente;
        this.quartoReservas = new HashSet<>();
    }

    public void atualizarInformacoes(DadosAtualizacaoReserva dados) {
        if(dados.checkin() != null) {
            this.checkin = dados.checkin();
        }
        if(dados.checkout() != null) {
            this.checkout = dados.checkout();
        }
    }
}
