package hotelaria.borba.api.service.quarto;

import hotelaria.borba.api.domain.CamaQuarto;
import hotelaria.borba.api.domain.Hotel;
import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.domain.Cama;
import hotelaria.borba.api.dto.cama_quarto.DTOsAtualizacaoCamaQuarto;
import hotelaria.borba.api.dto.cama_quarto.DadosAtualizacaoCamaQuarto;
import hotelaria.borba.api.dto.cama_quarto.DadosCadastroCamaQuarto;
import hotelaria.borba.api.dto.quarto.DadosAtualizacaoQuarto;
import hotelaria.borba.api.dto.quarto.DadosCadastroQuarto;
import hotelaria.borba.api.repository.CamaQuartoRepository;
import hotelaria.borba.api.repository.CamaRepository;
import hotelaria.borba.api.repository.HotelRepository;
import hotelaria.borba.api.repository.QuartoRepository;
import hotelaria.borba.api.service.quarto.validacoesCadastro.ValidadorCadastroDeQuartos;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartoService {

    private final QuartoRepository quartoRepository;
    private final HotelRepository hotelRepository;
    private final CamaRepository camaRepository;
    private final CamaQuartoRepository camaQuartoRepository;
    private final List<ValidadorCadastroDeQuartos> validadorCadastro;

    @Autowired
    public QuartoService(QuartoRepository quartoRepository, HotelRepository hotelRepository, CamaRepository camaRepository, CamaQuartoRepository camaQuartoRepository, List<ValidadorCadastroDeQuartos> validadorCadastro) {
        this.quartoRepository = quartoRepository;
        this.hotelRepository = hotelRepository;
        this.camaRepository = camaRepository;
        this.camaQuartoRepository = camaQuartoRepository;
        this.validadorCadastro = validadorCadastro;
    }

    public Quarto validarCadastro(DadosCadastroQuarto dados) {
        if(!hotelRepository.existsById(dados.id_hotel())) {
            throw new ValidationException("Id do hotel informado não existe!");
        }

        validadorCadastro.forEach(v -> v.validar(dados));

        Hotel hotel = hotelRepository.getReferenceById(dados.id_hotel());
        Quarto quarto = new Quarto(dados.numero(), dados.tipoQuarto(), dados.preco_diaria(), dados.descricao(), hotel);
        quartoRepository.save(quarto);

        adicionarCama(dados.camas(), quarto);

        return quarto;
    }

    private void adicionarCama(List<DadosCadastroCamaQuarto> dados, Quarto quarto) {
        for(DadosCadastroCamaQuarto camas : dados) {
            Cama cama = camaRepository.findById(camas.id_cama()).orElseThrow(() -> new ValidationException("Id da cama informada não existe!"));

            CamaQuarto camaQuarto = new CamaQuarto(camas.qt_cama(), cama, quarto);
            camaQuartoRepository.save(camaQuarto);

            quarto.getCamaQuarto().add(camaQuarto);
        }
    }

    public Quarto validarAtualizacao(DadosAtualizacaoQuarto dados) {
        Quarto quarto = quartoRepository.getReferenceById(dados.id());

        //Adicionar regra de negócio para não permitir adicionar um quarto com o mesmo numero de outro no mesmo hotel

        quarto.atualizarInformacoes(dados);
        atualizarCama(dados.camas(), quarto);

        return quarto;
    }

    public void atualizarCama(DTOsAtualizacaoCamaQuarto dados, Quarto quarto) {
        if(dados.cadastroCamaQuartos() != null) {
            adicionarCama(dados.cadastroCamaQuartos(), quarto);
        }
        if(dados.atualizarCamaQuartos() != null) {
            for (DadosAtualizacaoCamaQuarto camas : dados.atualizarCamaQuartos()) {
                Cama cama = null;
                if (camas.id_cama() != null) {
                    cama = camaRepository.getReferenceById(camas.id_cama());
                }
                CamaQuarto camaQuarto = camaQuartoRepository.findById(camas.id()).orElseThrow(() -> new ValidationException("Id da camaQuarto informado não existe!"));

                camaQuarto.atualizarInformacao(camas.qt_cama(), cama);
            }
        }
        if(dados.deletarCamaQuartos() != null) {
            for (Long idsToRemove : dados.deletarCamaQuartos()) {
                camaQuartoRepository.findById(idsToRemove).orElseThrow(() -> new ValidationException("Id da camaQuarto informado não existe!"));
                camaQuartoRepository.deleteById(idsToRemove);
            }
        }
    }
}
