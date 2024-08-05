package hotelaria.borba.api.service.cliente;

import hotelaria.borba.api.bot.UserSession;
import hotelaria.borba.api.domain.Cliente;
import hotelaria.borba.api.dto.cliente.DadosCadastroCliente;
import hotelaria.borba.api.repository.ClienteRepository;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void saveUserInformation(UserSession userSession) {
        Cliente cliente = new Cliente(new DadosCadastroCliente(userSession.getNome(), userSession.getCpf(), userSession.getTelefone(), userSession.getEmail()));
        clienteRepository.save(cliente);
    }

    public boolean thisClientHasAnAccount(String cpf) {
        return clienteRepository.findByCpf(cpf) != null;
    }
}
