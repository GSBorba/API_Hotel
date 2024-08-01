package hotelaria.borba.api.domain;

import hotelaria.borba.api.dto.cama.DadosAtualizacaoCama;
import hotelaria.borba.api.dto.cama.DadosCadastroCama;
import hotelaria.borba.api.enums.TipoCama;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Table(name = "cama")
@Entity(name = "Cama")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cama {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TipoCama tipoCama;

    @OneToMany(mappedBy = "cama")
    private Set<CamaQuarto> camaQuarto;

    public Cama(DadosCadastroCama dados) {
        this.tipoCama = dados.tipoCama();
        this.camaQuarto = new HashSet<>();
    }

    public void atualizar(DadosAtualizacaoCama dados) {
        if(dados.tipoCama() != null) {
            this.tipoCama = dados.tipoCama();
        }
    }
}
