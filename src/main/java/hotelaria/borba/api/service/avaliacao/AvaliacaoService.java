package hotelaria.borba.api.service.avaliacao;

import hotelaria.borba.api.domain.Avaliacao;
import hotelaria.borba.api.domain.Reserva;
import hotelaria.borba.api.dto.avaliacao.DadosAtualizacaoAvaliacao;
import hotelaria.borba.api.dto.avaliacao.DadosCadastroAvaliacao;
import hotelaria.borba.api.repository.AvaliacaoRepository;
import hotelaria.borba.api.repository.ReservaRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ReservaRepository reservaRepository;

    @Autowired
    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
                            ReservaRepository reservaRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.reservaRepository = reservaRepository;
    }

    public Avaliacao validarCadastro(DadosCadastroAvaliacao dados) {
        if(!reservaRepository.existsById(dados.id_reserva())) {
            throw new ValidationException("Id da reserva informado não existe!");
        }
        if(avaliacaoRepository.existsByReserva(dados.id_reserva())) {
            throw new ValidationException("Já existe uma avaliação para essa reserva!");
        }

        Reserva reserva = reservaRepository.getReferenceById(dados.id_reserva());
        Avaliacao avaliacao = new Avaliacao(dados.nota(), dados.descricao(), reserva);
        avaliacaoRepository.save(avaliacao);

        return avaliacao;
    }

    public Avaliacao validarAtualizacao(DadosAtualizacaoAvaliacao dados) {
        Avaliacao avaliacao = avaliacaoRepository.findById(dados.id()).orElseThrow(() -> new ValidationException("Id da avaliação informado não existe!"));

        avaliacao.atualizarInformacoes(dados);

        return avaliacao;
    }
}
