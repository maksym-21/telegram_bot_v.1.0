package config;

import handlers.PropertiesHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import model.*;


public class Bot extends TelegramLongPollingBot {

    /**
     * @return username of bot
     */
    public String getBotUsername() {
        return PropertiesHandler.getStringFromProperty("my_bot_username");
    }

    /** api is secure -> do not tell anyone
     *
     * @return http api which was given by Telegram
     */
    public String getBotToken() {
        return PropertiesHandler.getStringFromProperty("my_token");
    }

    /**
     * a method which adds a keyboard under text panel
     */
    public void setButton(SendMessage sendMessage) {
        // init of keyboard
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        // setting a markup
        sendMessage.setReplyMarkup(keyboardMarkup);

        //
        keyboardMarkup.setSelective(true);

        // adjust a scale to the number of buttons
        keyboardMarkup.setResizeKeyboard(true);

        // hide a keyboard after using a button
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();

        firstRow.add(new KeyboardButton("/help"));
        firstRow.add(new KeyboardButton("/settings "));

        keyboardRowList.add(firstRow);
        keyboardMarkup.setKeyboard(keyboardRowList);
    }

    /**
     * a method for sending responds on user's action
     */
    public void sendMsg(Message message, String input) {
        SendMessage sendMessage = new SendMessage();

        // setting a chat and relevant message id on which we should respond
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());

        sendMessage.setText(input);

        // trying to send our response
        try {
            // setting of keyboard , then execute message
            setButton(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * a method which reacts on updates
     */
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        Model model = new Model();

        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "This " + Emoji.BOT + " tells you a current forecast of\n " +
                            "weather in city, which you ask" + Emoji.SUNNY + Emoji.UMBRELLA );
                    break;
                case "/settings":
                    sendMsg(message,"What will we customize" + Emoji.QUESTION_MARK);
                    break;
                default:
                    try {
                        // if input doesn't contain a digit or is not a digit then process
                        if (!message.getText().matches(".*\\d.*")) {
                            sendMsg(message, Weather.getWeather(message.getText(), model));
                        }
                        else throw new IOException("Incorrect input!");
                    } catch (IOException e) {
                        sendMsg(message,"City was not found" + Emoji.GREY_EXCLAMATION + "\nPlease try again");
                    }
                    break;
            }
        }
    }
}
