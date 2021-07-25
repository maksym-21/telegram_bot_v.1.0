package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import config.Bot;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            // creating an object of api
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            // instance of bot
            Bot bot = new Bot();

            //then register our bot
            telegramBotsApi.registerBot(bot);

            LOG.info("Registration of Telegram Bot was successful.");
        } catch (TelegramApiException exception) {
            exception.getStackTrace();

            LOG.info("Registration of Telegram Bot has failed.");
        }
    }
}
