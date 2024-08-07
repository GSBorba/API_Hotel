package hotelaria.borba.api.repository;

import hotelaria.borba.api.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
            select  r
            from    Reserva r
            where   r.cliente.id = :id
            """)
    List<Reserva> findAllByClient(@Param("id") Long id);
}
