package hotelaria.borba.api.repository;

import hotelaria.borba.api.domain.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
}
