package hotelaria.borba.api.service.cliente;

import hotelaria.borba.api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public boolean thisClientHasAnAccount(String cpf) {
        return clienteRepository.findByCpf(cpf) != null;
    }
}
