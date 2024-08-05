package hotelaria.borba.api.bot;

import hotelaria.borba.api.enums.Estado;
import hotelaria.borba.api.service.cliente.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final Map<Long, UserSession> userSessions = new HashMap<>();
    private final ClienteService clienteService;

    public TelegramBot(String botUsername,
                       String botToken,
                       ClienteService clienteService) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.clienteService = clienteService;
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
                                                    Dale family, tamo aqui, achamo você :)
                                                    """);
                            userSession.setState(UserState.ASK_MENU);
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
                        clienteService.saveUserInformation(userSession);
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
                        userSession.setState(UserState.ASK_RESERVA);
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

    public static boolean isValidCPF(String cpf) {
        Pattern pattern = Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
        Matcher matcher = pattern.matcher(cpf);
        return matcher.matches();
    }

    public static boolean isValidLength(String text, int maxLength) {
        int length = text.length();
        return length <= maxLength;
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidEstado(String estado) {
        try {
            Estado.valueOf(estado);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isValidNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getCadastro(UserSession userSession){
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
}
