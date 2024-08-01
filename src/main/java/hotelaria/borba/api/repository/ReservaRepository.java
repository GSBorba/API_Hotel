package hotelaria.borba.api.repository;

import hotelaria.borba.api.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}