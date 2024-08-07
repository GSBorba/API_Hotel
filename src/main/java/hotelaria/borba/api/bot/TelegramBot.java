package hotelaria.borba.api.bot;

import hotelaria.borba.api.domain.*;
import hotelaria.borba.api.dto.quarto_reserva.DadosCadastroQuartoReserva;
import hotelaria.borba.api.dto.reserva.DadosCadastroReserva;
import hotelaria.borba.api.service.cliente.ClienteService;
import hotelaria.borba.api.service.hotel.HotelService;
import hotelaria.borba.api.service.quarto.QuartoService;
import hotelaria.borba.api.service.reserva.ReservaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final Map<Long, UserSession> userSessions = new HashMap<>();
    private final ClienteService clienteService;
    private final HotelService hotelService;
    private final QuartoService quartoService;
    private final ReservaService reservaService;

    public TelegramBot(String botUsername,
                       String botToken,
                       ClienteService clienteService,
                       HotelService hotelService,
                       QuartoService quartoService,
                       ReservaService reservaService) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.clienteService = clienteService;
        this.hotelService = hotelService;
        this.quartoService = quartoService;
        this.reservaService = reservaService;
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String messageText = message.getText();

            UserSession userSession = userSessions.computeIfAbsent(chatId, k -> new UserSession());

            try {
                switch (userSession.getState()) {
                    case START:
                        sendMessage(chatId, """
                                                Olá, tudo bem?
                                                
                                                Seja bem vindo ao Assistente Virtual da Hotelaria Borba!
                                                Para que eu possa auxiliá-lo da melhor maneira favor digitar o seu cpf para conferirmos se você já possuí um cadastro em nossa base, caso não criaremos uma conta para você.
                                                Digite o seu CPF igual no exemplo: 123.456.789-00
                                                """);
                        userSession.setState(UserState.ASK_CPF);
                        break;
                    case ASK_CPF:
                        if(!isValidCPF(messageText)){
                            sendMessage(chatId, """
                                                    Desculpe mas o formato do CPF que você escreveu não é valido, favor escrever no seguinte formato: 123.456.789-00
                                                    """);
                            break;
                        }
                        userSession.setCpf(messageText);
                        if(clienteService.thisClientHasAnAccount(messageText)) {
                            sendMessage(chatId, """
                                                    Verificamos aqui que você ja possuí um cadastro!
                                                    
                                                    Digite o número do que você gostaria de fazer:
                                                    1 - Fazer um reserva
                                                    2 - Ver minhas reservas
                                                    3 - Sair
                                                    """);
                            userSession.setCliente(clienteService.findClientByCpf(messageText));
                            userSession.setState(UserState.ASK_OPCOES_MENU);
                            break;
                        } else {
                            sendMessage(chatId, """
                                                    Verificamos aqui que você não possuí um cadastro, gostaria de fazer seu cadastro por aqui?
                                                    
                                                    Digite Sim para fazer o cadastro
                                                    """);
                            userSession.setState(UserState.ASK_CADASTRO);
                            break;
                        }
                    case ASK_CADASTRO:
                        if(messageText.equalsIgnoreCase("Sim")){
                            sendMessage(chatId, """
                                                    Certo, então vamos começar!
                                                    
                                                    Por favor, digite o seu nome:
                                                    """);
                            userSession.setState(UserState.ASK_NAME);
                            break;
                        }
                        sendMessage(chatId, """
                                                Certo, caso mude de ideia estarei a disposição.
                                                
                                                Tenha um bom dia!
                                                """);
                        userSession.setState(UserState.START);
                        break;
                    case ASK_NAME:
                        if(isValidLength(messageText, 255)){
                            userSession.setNome(messageText);
                            sendMessage(chatId, """
                                                    Digite o seu telefone:
                                                    """);
                            userSession.setState(UserState.ASK_PHONE);
                            break;
                        }
                        sendMessage(chatId, """
                                                Desculpe mas você digitou um nome muito grande. O nome pode ter no máximo 255 caracteres.
                                                
                                                Favor digitar novamente:
                                                """);
                        break;
                    case ASK_PHONE:
                        if(isValidLength(messageText, 15)){
                            userSession.setTelefone(messageText);
                            sendMessage(chatId, """
                                                    Digite o seu email:
                                                    """);
                            userSession.setState(UserState.ASK_EMAIL);
                            break;
                        }
                        sendMessage(chatId, """
                                                Desculpe mas você digitou um telefone muito grande. O nome pode ter no máximo 15 caracteres.
                                                
                                                Favor digitar novamente:
                                                """);
                        break;
                    case ASK_EMAIL:
                        if(isValidLength(messageText, 255) && isValidEmail(messageText)){
                            userSession.setEmail(messageText);
                            sendMessage(chatId, getCadastro(userSession));
                            userSession.setState(UserState.ASK_CORRECT);
                            break;
                        }
                        sendMessage(chatId, """
                                                Desculpe mas você digitou um email invalido.
                                                
                                                Favor digitar novamente:
                                                """);
                        break;
                    case ASK_CORRECT:
                        if(messageText.equalsIgnoreCase("Não")){
                            sendMessage(chatId, """
                                                    Certo, vamos recomeçar então.
                                                    
                                                    Digite o seu CPF:
                                                    """);
                            userSession.setState(UserState.ASK_CPF);
                            break;
                        }
                        sendMessage(chatId, """
                                                Meus parabéns, você concluiu o seu cadastro!!!
                                                
                                                Agora você gostaria de fazer uma reserva?
                                                """);
                        userSession.setState(UserState.ASK_RESERVA);
                        Long id = clienteService.saveUserInformation(userSession);
                        userSession.setId(id);
                        break;
                    case ASK_RESERVA:
                        if(messageText.equalsIgnoreCase("Não")){
                            sendMessage(chatId, """
                                                    Certo, qualquer coisa entre em contado que ficaremos feliz em fazer a sua reserva
                                                    """);
                            userSession.setState(UserState.ASK_MENU);
                            break;
                        }
                        sendMessage(chatId, """
                                                Por favor, digite a data do check-in:
                                                """);
                        userSession.setState(UserState.ASK_CHECKIN);
                        break;
                    case ASK_CHECKIN:
                        if(isValidDate(messageText)) {
                            userSession.setCheckin(LocalDate.parse(messageText));
                            sendMessage(chatId, """
                                                Por favor, digite a data do check-out:
                                                """);
                            userSession.setState(UserState.ASK_CHECKOUT);
                            break;
                        }
                        sendMessage(chatId, """
                                Data inválida, fovor digitar novamente!
                                """);
                        break;
                    case ASK_CHECKOUT:
                        if(isValidDate(messageText)) {
                            userSession.setCheckout(LocalDate.parse(messageText));
                            sendMessage(chatId, """
                                                Qual dos nosso hoteis você gostaria de se hospedar?
                                                """);
                            for(Hotel hotel : hotelService.listaHoteis()){
                                sendMessage(chatId, getHoteis(hotel));
                            }
                            sendMessage(chatId, """
                                                Favor digite o Id do hotel que você gostaria de se hospedar:
                                                """);
                            userSession.setState(UserState.ASK_HOTEL);
                            break;
                        }
                        sendMessage(chatId, """
                                Data inválida, fovor digitar novamente!
                                """);
                        break;
                    case ASK_HOTEL:
                        if(isValidNumber(messageText)) {
                            if(hotelService.isThisHotelValid(messageText)) {
                                userSession.setHotel(Long.parseLong(messageText));
                                sendMessage(chatId, """
                                                Qual preço máximo você gostaria de pagar na diária?
                                                """);
                                userSession.setState(UserState.ASK_PRICE);
                                break;
                            }
                            sendMessage(chatId, """
                                Hotel inválido, fovor digitar novamente!
                                """);
                            break;
                        }
                        sendMessage(chatId, """
                                Hotel inválido, fovor digitar novamente!
                                """);
                        break;
                    case ASK_PRICE:
                        if(isValidValue(messageText)) {
                            userSession.setOrcamento(Double.parseDouble(messageText));
                            List<Quarto> quartos = quartoService.listaQuartoPorHotelValor(userSession.getCheckin(),
                                    userSession.getCheckout(),
                                    userSession.getHotel(),
                                    userSession.getOrcamento());
                            if(quartos == null || quartos.isEmpty()) {
                                sendMessage(chatId, """
                                                Infelizmente não temos quartos disponíveis para essa data e valor, digite uma nova date de checkin:
                                                """);
                                userSession.setState(UserState.ASK_CHECKIN);
                                break;
                            }
                            sendMessage(chatId, """
                                                Certo, aqui vai os quartos que estão no seu orçamento!
                                                """);
                            for(Quarto quarto : quartos){
                                sendMessage(chatId, getQuartos(quarto));
                            }
                            sendMessage(chatId, """
                                                Favor digite o Id dos quarto que você gostaria de se hospedar (um id por vez), caso ja tenha escolhido todos os quartos digite SAIR:
                                                """);
                            userSession.setState(UserState.ASK_QUARTO);
                            break;
                        }
                        sendMessage(chatId, """
                                Preço inválido, fovor digitar novamente!
                                """);
                        break;
                    case ASK_QUARTO:
                        if(isValidNumber(messageText)) {
                            if(quartoService.isThisQuartoValid(Long.parseLong(messageText))){
                                List<DadosCadastroQuartoReserva> quartoReserva = userSession.getQuartos();
                                quartoReserva.add(new DadosCadastroQuartoReserva(Long.parseLong(messageText)));
                                userSession.setQuartos(quartoReserva);
                                break;
                            }
                            sendMessage(chatId, """
                                Id inválido, fovor digitar novamente!
                                """);
                            break;
                        }
                        if(messageText.equalsIgnoreCase("sair")) {
                            reservaService.validarCadastro(new DadosCadastroReserva(userSession.getCheckin(),
                                    userSession.getCheckout(),
                                    userSession.getId(),
                                    userSession.getQuartos()));
                            sendMessage(chatId, """
                                Sua reserva está feita, muito obrigado pela preferência!
                                
                                Digite o número respectivo:
                                1 - Fazer um reserva
                                2 - Ver minhas reservas
                                3 - Sair
                                """);
                            userSession.setState(UserState.ASK_OPCOES_MENU);
                            break;
                        }
                        sendMessage(chatId, """
                                Id inválido, fovor digitar novamente!
                                """);
                        break;
                    case ASK_MENU:
                        sendMessage(chatId, """
                                Olá, o que você gostaria de fazer?
                                Digite o número respectivo:
                                1 - Fazer um reserva
                                2 - Ver minhas reservas
                                3 - Sair
                                """);
                        userSession.setState(UserState.ASK_OPCOES_MENU);
                        break;
                    case ASK_OPCOES_MENU:
                        if(isValidNumber(messageText)){
                            if(Integer.parseInt(messageText) == 1) {
                                sendMessage(chatId, """
                                                Por favor, digite a data do check-in:
                                                """);
                                userSession.setState(UserState.ASK_CHECKIN);
                                break;
                            } else if(Integer.parseInt(messageText) == 2) {
                                for(Reserva reserva : reservaService.listarReservas(userSession.getId())){
                                    sendMessage(chatId, getReserva(reserva));
                                }
                                sendMessage(chatId, """
                                Digite o número respectivo do que você gostaria de fazer:
                                1 - Fazer um reserva
                                2 - Ver minhas reservas
                                3 - Sair
                                """);
                                break;
                            } else if (Integer.parseInt(messageText) == 3) {
                                sendMessage(chatId, """
                                                Certo, qualquer coisa me chame!
                                                """);
                                userSession.setState(UserState.ASK_MENU);
                                break;
                            }
                            sendMessage(chatId, """
                                                Opção inválida, mas vou entender isso como um sair!
                                                
                                                Qualquer coisa mande uma mensagem que estarei aqui à sua disposição.
                                                Tchau.
                                                """);
                            userSession.setState(UserState.ASK_MENU);
                            break;
                        }
                        sendMessage(chatId, """
                                                Opção inválida, mas vou entender isso como um sair!
                                                
                                                Qualquer coisa mande uma mensagem que estarei aqui à sua disposição.
                                                Tchau.
                                                """);
                        userSession.setState(UserState.ASK_MENU);
                        break;
                }
            } catch (TelegramApiException e) {
                log.error("Exception during processing telegram api: {}", e.getMessage());
            }
        }
    }

    private void sendMessage(Long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        execute(message);
    }

    private static boolean isValidCPF(String cpf) {
        Pattern pattern = Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
        Matcher matcher = pattern.matcher(cpf);
        return matcher.matches();
    }

    private static boolean isValidLength(String text, int maxLength) {
        int length = text.length();
        return length <= maxLength;
    }

    private static boolean isValidEmail(String email) {
        String EMAIL_PATTERN =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidValue(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            LocalDate today = LocalDate.now();
            return !date.isBefore(today);
        } catch (Exception e) {
            return false;
        }
    }

    private String getCadastro(UserSession userSession){
        return String.format(
                "Verifique se suas informações estão corretas:\n" +
                "Nome: %s\n" +
                "CPF: %s\n" +
                "Telefone: %s\n" +
                "Email: %s\n" +
                "\nDigite Não caso algum de seus dados estejam incorretos, ou Sim caso estejam corretas.",
                userSession.getNome(), userSession.getCpf(), userSession.getTelefone(), userSession.getEmail()
        );
    }

    private String getHoteis(Hotel hotel) {
        return String.format(
          "Id: %s\n" +
          "Nome: %s\n" +
          "Descricao: %s\n" +
          "Categoria: %s\n" +
          "Estado: %s\n" +
          "Cidade: %s\n",
          hotel.getId(), hotel.getNome(), hotel.getDescricao(), hotel.getCategoria().getNomeCategoria(), hotel.getEndereco().getUf().getNomeEstado(), hotel.getEndereco().getCidade()
        );
    }

    private String getQuartos(Quarto quarto) {
        StringBuilder s = new StringBuilder(String.format(
            "Id: %s\n" +
            "Número: %s\n" +
            "Tipo de Quarto: %s\n" +
            "Descricao: %s\n" +
            "Preço da Diária: %s",
            quarto.getId(), quarto.getNumero(), quarto.getTipoQuarto().getNomeTipo(), quarto.getDescricao(), quarto.getPrecoDiaria()
        ));
        for(CamaQuarto camaQuarto : quarto.getCamaQuarto()){
            s.append("\n").append(camaQuarto.getQt_cama()).append(" ").append(camaQuarto.getCama().getTipoCama().getNomeTipo());
        }
        return s.toString();
    }

    private String getReserva(Reserva reserva) {
        StringBuilder s = new StringBuilder(String.format(
            "Número da Reserva: %s\n" +
            "Check-in: %s\n" +
            "Check-out: %s\n" +
            "Valor: %s\n" +
            "Hotel: %s",
            reserva.getId(), reserva.getCheckin(), reserva.getCheckout(), reserva.getValor(), reserva.getQuartoReservas().stream().toList().get(0).getQuarto().getHotel().getNome()
    ));
        for(QuartoReserva quartoReserva : reserva.getQuartoReservas()){
            s.append("\nNumero do Quarto: ").append(quartoReserva.getQuarto().getNumero());
            s.append("\nTipo do Quarto: ").append(quartoReserva.getQuarto().getTipoQuarto().getNomeTipo());
        }
        return s.toString();
    }
}
