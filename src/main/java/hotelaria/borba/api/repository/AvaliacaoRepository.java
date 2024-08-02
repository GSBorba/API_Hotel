package hotelaria.borba.api.repository;

import hotelaria.borba.api.domain.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    @Query("""
            select  (count(a) > 0)
            from    Avaliacao a
            where   a.reserva.id = :reserva
            """)
    boolean existsByReserva(@Param("reserva") Long reserva);
}
