package app;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import config.Bot;

public class Main {
    public static void main(String[] args) {
        try {
            // creating an object of api
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            // instance of bot
            Bot bot = new Bot();

            //then register our bot
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException exception) {
            exception.getStackTrace();
        }
    }
}
