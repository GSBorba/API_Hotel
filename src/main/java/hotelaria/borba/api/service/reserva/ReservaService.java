package hotelaria.borba.api.service.reserva;

import hotelaria.borba.api.domain.Cliente;
import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.domain.QuartoReserva;
import hotelaria.borba.api.domain.Reserva;
import hotelaria.borba.api.dto.quarto_reserva.DTOsAtualizacaoQuartoReserva;
import hotelaria.borba.api.dto.quarto_reserva.DadosAtualizacaoQuartoReserva;
import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import hotelaria.borba.api.dto.reserva.DadosAtualizacaoReserva;
import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import hotelaria.borba.api.repository.ClienteRepository;
import hotelaria.borba.api.repository.QuartoRepository;
import hotelaria.borba.api.repository.QuartoReservaRepository;
import hotelaria.borba.api.repository.ReservaRepository;
import hotelaria.borba.api.service.reserva.validacoes_atualizacao.ValidadorAtualizacaoDeReservas;
import hotelaria.borba.api.service.reserva.validacoes_cadastro.ValidadorCadastroDeReservas;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final QuartoRepository quartoRepository;
    private final QuartoReservaRepository quartoReservaRepository;
    private final List<ValidadorCadastroDeReservas> validadorCadastroDeReservas;
    private final List<ValidadorAtualizacaoDeReservas> validadorAtualizacaoDeReservas;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository,
                          ClienteRepository clienteRepository,
                          QuartoRepository quartoRepository,
                          QuartoReservaRepository quartoReservaRepository,
                          List<ValidadorCadastroDeReservas> validadorCadastroDeReservas,
                          List<ValidadorAtualizacaoDeReservas> validadorAtualizacaoDeReservas) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.quartoRepository = quartoRepository;
        this.quartoReservaRepository = quartoReservaRepository;
        this.validadorCadastroDeReservas = validadorCadastroDeReservas;
        this.validadorAtualizacaoDeReservas = validadorAtualizacaoDeReservas;
    }

    @Transactional
    public Reserva validarCadastro(DadosCadastroReserva dados) {
        if(!clienteRepository.existsById(dados.id_cliente())) {
            throw new ValidationException("Id do cliente informado não existe!");
        }

        validadorCadastroDeReservas.forEach(v -> v.validar(dados));

        Cliente cliente = clienteRepository.getReferenceById(dados.id_cliente());

        BigDecimal valor = BigDecimal.ZERO;
        Reserva reserva = new Reserva(dados.checkin(), dados.checkout(), valor, cliente);
        reservaRepository.save(reserva);

        valor = adicionarQuarto(dados.quartos(), reserva, valor);
        reserva.setValor(valor.multiply(new BigDecimal(ChronoUnit.DAYS.between(dados.checkin(), dados.checkout()))));

        return reserva;
    }

    public BigDecimal adicionarQuarto(List<DadosCadastroQuartoReserva> dados, Reserva reserva, BigDecimal valor) {
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }

        for (DadosCadastroQuartoReserva dadosCadastro : dados) {
            Quarto quarto = quartoRepository.findById(dadosCadastro.id_quarto())
                    .orElseThrow(() -> new ValidationException("Id do quarto informado não existe!"));

            if (quarto.getPrecoDiaria() == null) {
                throw new ValidationException("Preço diário do quarto não pode ser nulo!");
            }

            QuartoReserva quartoReserva = new QuartoReserva(quarto.getPrecoDiaria(), quarto, reserva);
            quartoReservaRepository.save(quartoReserva);

            valor = valor.add(quarto.getPrecoDiaria());

            quarto.getQuartoReserva().add(quartoReserva);
            reserva.getQuartoReservas().add(quartoReserva);
        }
        return valor;
    }

    @Transactional
    public Reserva validarAtualizacao(DadosAtualizacaoReserva dados) {
        Reserva reserva = reservaRepository.getReferenceById(dados.id());

        validadorAtualizacaoDeReservas.forEach(v -> v.validar(dados));

        atualizarQuarto(dados.quartos(), reserva);
        reserva.atualizarInformacoes(dados);

        BigDecimal valor = BigDecimal.ZERO;
        for(QuartoReserva quartoReserva : reserva.getQuartoReservas()){
            valor = valor.add(quartoReserva.getPrecoDiaria());
        }
        reserva.setValor(valor);

        return reserva;
    }

    public void atualizarQuarto(DTOsAtualizacaoQuartoReserva dados, Reserva reserva) {
        BigDecimal valor = reserva.getValor();
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }
        if (dados.cadastroQuartoReserva() != null) {
            adicionarQuarto(dados.cadastroQuartoReserva(), reserva, valor);
        }
        if (dados.atualizacaoQuartoReserva() != null) {
            for (DadosAtualizacaoQuartoReserva quartos : dados.atualizacaoQuartoReserva()) {
                Quarto quarto = quartoRepository.findById(quartos.id_quarto())
                        .orElseThrow(() -> new ValidationException("Id do quarto informado não existe!"));
                QuartoReserva quartoReserva = quartoReservaRepository.findById(quartos.id())
                        .orElseThrow(() -> new ValidationException("Id do quartoReserva informado não existe!"));

                valor = valor.subtract(quartoReserva.getQuarto().getPrecoDiaria());
                valor = valor.add(quarto.getPrecoDiaria());
                quartoReserva.atualizarInformacoes(quarto);
            }
        }
        if (dados.deletarQuartoReserva() != null) {
            for (Long idsToRemove : dados.deletarQuartoReserva()) {
                QuartoReserva quartoReserva = quartoReservaRepository.findById(idsToRemove)
                        .orElseThrow(() -> new ValidationException("Id do quartoReserva informado não existe!"));
                valor = valor.subtract(quartoReserva.getPrecoDiaria());
                quartoReservaRepository.deleteById(idsToRemove);
            }
        }
    }

    public List<Reserva> listarReservas(Long id) {
        return reservaRepository.findAllByClient(id);
    }
}
