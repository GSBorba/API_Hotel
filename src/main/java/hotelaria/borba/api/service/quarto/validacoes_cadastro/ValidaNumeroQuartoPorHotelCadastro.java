package hotelaria.borba.api.service.quarto.validacoes_cadastro;

import hotelaria.borba.api.domain.Hotel;
import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.dto.quarto.DadosCadastroQuarto;
import hotelaria.borba.api.repository.HotelRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaNumeroQuartoPorHotelCadastro implements ValidadorCadastroDeQuartos{

    private final HotelRepository hotelRepository;

    @Autowired
    public ValidaNumeroQuartoPorHotelCadastro(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public void validar(DadosCadastroQuarto dados) {
        Hotel hotel = hotelRepository.getReferenceById(dados.id_hotel());

        for(Quarto quartos : hotel.getQuartos()){
            if(quartos.getNumero().equals(dados.numero())){
                throw new ValidationException("Já existe um quarto nesse hotel com o mesmo número passado!");
            }
        }
    }
}
