package hotelaria.borba.api.service;

import hotelaria.borba.api.domain.Cliente;
import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.domain.QuartoReserva;
import hotelaria.borba.api.domain.Reserva;
import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import hotelaria.borba.api.repository.ClienteRepository;
import hotelaria.borba.api.repository.QuartoRepository;
import hotelaria.borba.api.repository.QuartoReservaRepository;
import hotelaria.borba.api.repository.ReservaRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final QuartoRepository quartoRepository;
    private final QuartoReservaRepository quartoReservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, ClienteRepository clienteRepository, QuartoRepository quartoRepository, QuartoReservaRepository quartoReservaRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.quartoRepository = quartoRepository;
        this.quartoReservaRepository = quartoReservaRepository;
    }

    public Reserva validarCadastro(DadosCadastroReserva dados) {
        if(!clienteRepository.existsById(dados.id_cliente())) {
            throw new ValidationException("Id do cliente informado não existe!");
        }

        //Adicionar aqui as regras de negócio

        Cliente cliente = clienteRepository.getReferenceById(dados.id_cliente());
        Reserva reserva = new Reserva(dados.checkin(), dados.checkout(), cliente);
        reservaRepository.save(reserva);

        adicionarQuarto(dados.quartos(), reserva);

        return reserva;
    }

    public void adicionarQuarto(List<DadosCadastroQuartoReserva> dados, Reserva reserva) {
        for(DadosCadastroQuartoReserva quartos : dados) {
            Quarto quarto = quartoRepository.findById(quartos.id_quarto()).orElseThrow(() -> new ValidationException("Id da cama informada não existe!"));

            QuartoReserva quartoReserva = new QuartoReserva(quarto.getPreco_diaria(), quarto, reserva);
            quartoReservaRepository.save(quartoReserva);

            quarto.getQuartoReserva().add(quartoReserva);
            reserva.getQuartoReservas().add(quartoReserva);
        }
    }

    public Reserva validarAtualizacao(DadosAtualizacaoReserva dados) {
        Reserva reserva = reservaRepository.getReferenceById(dados.id());

        //Regra de negócio

        reserva.atualizarInformacoes(dados);

        //Regra de negócio atualizar quarto

        return reserva;
    }
}
