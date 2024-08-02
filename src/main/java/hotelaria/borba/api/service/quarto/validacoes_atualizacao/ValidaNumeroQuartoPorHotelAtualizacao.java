package hotelaria.borba.api.service.quarto.validacoes_atualizacao;

import hotelaria.borba.api.domain.Hotel;
import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.dto.quarto.DadosAtualizacaoQuarto;
import hotelaria.borba.api.repository.HotelRepository;
import hotelaria.borba.api.repository.QuartoRepository;
import hotelaria.borba.api.service.quarto.validacoes_cadastro.ValidadorCadastroDeQuartos;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaNumeroQuartoPorHotelAtualizacao implements ValidadorAtualizacaoDeQuartos {

    private final HotelRepository hotelRepository;
    private final QuartoRepository quartoRepository;

    @Autowired
    public ValidaNumeroQuartoPorHotelAtualizacao(HotelRepository hotelRepository, QuartoRepository quartoRepository) {
        this.hotelRepository = hotelRepository;
        this.quartoRepository = quartoRepository;
    }

    public void validar(DadosAtualizacaoQuarto dados) {
        Quarto quarto = quartoRepository.getReferenceById(dados.id());
        Hotel hotel = hotelRepository.getReferenceById(quarto.getHotel().getId());

        for(Quarto quartos : hotel.getQuartos()){
            if(quartos.getNumero().equals(dados.numero())){
                throw new ValidationException("Já existe um quarto nesse hotel com o mesmo número passado!");
            }
        }
    }
}
