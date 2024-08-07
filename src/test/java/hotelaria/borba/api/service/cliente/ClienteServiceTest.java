package hotelaria.borba.api.service.cliente;

import hotelaria.borba.api.bot.UserSession;
import hotelaria.borba.api.domain.Cliente;
import hotelaria.borba.api.dto.cliente.DadosCadastroCliente;
import hotelaria.borba.api.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve salvar informações do cliente e retornar o ID do cliente")
    void deveSalvarInformacoesDoClienteERetornarId() {
        UserSession userSession = new UserSession();
        userSession.setNome("João");
        userSession.setCpf("12345678900");
        userSession.setTelefone("987654321");
        userSession.setEmail("joao@example.com");

        clienteService.saveUserInformation(userSession);

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve verificar se o cliente tem uma conta com o CPF fornecido")
    void deveVerificarSeClienteTemContaComCpf() {
        String cpf = "12345678900";
        Cliente cliente = new Cliente();

        when(clienteRepository.findByCpf(cpf)).thenReturn(cliente);

        boolean hasAccount = clienteService.thisClientHasAnAccount(cpf);

        assertTrue(hasAccount);
        verify(clienteRepository, times(1)).findByCpf(cpf);
    }

    @Test
    @DisplayName("Não deve encontrar o cliente com o CPF fornecido")
    void naoDeveEncontrarClienteComCpf() {
        String cpf = "12345678900";

        when(clienteRepository.findByCpf(cpf)).thenReturn(null);

        boolean hasAccount = clienteService.thisClientHasAnAccount(cpf);

        assertFalse(hasAccount);
        verify(clienteRepository, times(1)).findByCpf(cpf);
    }

    @Test
    @DisplayName("Deve encontrar o cliente pelo CPF")
    void deveEncontrarClientePeloCpf() {
        String cpf = "12345678900";
        Cliente cliente = new Cliente(new DadosCadastroCliente("Teste", cpf, "(47) 99999-8888", "teste@gmail.com"));

        when(clienteRepository.findByCpf(cpf)).thenReturn(cliente);

        Cliente foundClient = clienteService.findClientByCpf(cpf);

        assertNotNull(foundClient);
        assertEquals(cpf, foundClient.getCpf());
        verify(clienteRepository, times(1)).findByCpf(cpf);
    }
}