package hotelaria.borba.api.repository;

import hotelaria.borba.api.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
