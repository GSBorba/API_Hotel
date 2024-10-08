package hotelaria.borba.api.config;

import hotelaria.borba.api.bot.TelegramBot;
import hotelaria.borba.api.service.cliente.ClienteService;
import hotelaria.borba.api.service.hotel.HotelService;
import hotelaria.borba.api.service.quarto.QuartoService;
import hotelaria.borba.api.service.reserva.ReservaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class TelegramConfig {

    @Bean
    public TelegramBot telegramBot(@Value("${bot.username}") String botUsername,
                                   @Value("${bot.token}") String botToken,
                                   ClienteService clienteService,
                                   HotelService hotelService,
                                   QuartoService quartoService,
                                   ReservaService reservaService) {
        TelegramBot telegramBot = new TelegramBot(botUsername, botToken, clienteService, hotelService, quartoService, reservaService);
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            log.error("Exception during registration telegram api: {}", e.getMessage());
        }
        return telegramBot;
    }
}
