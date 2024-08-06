package hotelaria.borba.api.service.hotel;

import hotelaria.borba.api.domain.Hotel;
import hotelaria.borba.api.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> listaHoteis() {
        return hotelRepository.findAll();
    }

    public boolean isThisHotelValid(String messageText) {
        return hotelRepository.existsById(Long.parseLong(messageText));
    }
}
