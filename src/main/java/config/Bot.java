package config;

import handlers.PropertiesHandler;
import model.Emoji;
import model.Model;
import model.Weather;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import state.BotStateContext;
import state.BotStates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    /**
     * a method which adds a keyboard under text panel
     */
    public void setButton(SendMessage sendMessage) {
        // init of keyboard
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        // setting a markup
        sendMessage.setReplyMarkup(keyboardMarkup);

        keyboardMarkup.setSelective(true);

        // adjust a scale to the number of buttons
        //keyboardMarkup.setResizeKeyboard(true);

        // hide a keyboard after using a button
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();
        // KeyboardRow thirdRow = new KeyboardRow();

        firstRow.add(new KeyboardButton("Weather" + Emoji.SUNNY));
        firstRow.add(new KeyboardButton("News" + Emoji.MAIL_BOX));
        secondRow.add(new KeyboardButton("Games" + Emoji.VIDEO_GAME ));
        secondRow.add(new KeyboardButton("FAQ" + Emoji.ROCKET));

        keyboardRowList.add(firstRow);
        keyboardRowList.add(secondRow);

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
     * a method which reacts on updates and sets relevant state
     */
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        // Model model = new Model();

        boolean flag = false;

        // add code and states
        if (message != null && message.hasText()) {
            if (BotStateContext.getInstance().getCurrentState()==BotStates.WEATHER_WANTED){
                try {
                    String weather_prompt = update.getMessage().getText();
                    Model model = new Model();

                    // if input doesn't contain a digit or is not a digit then process
                    if (!weather_prompt.matches(".*\\d.*")) {
                        sendMsg(message, Weather.getWeather(weather_prompt, model));
                        flag = true;
                    }
                    else throw new IOException("Incorrect input!");
                } catch (IOException e) {
                    sendMsg(message,"City was not found" + Emoji.GREY_EXCLAMATION + "\nPlease try again");
                    flag = true;
                }
            }
            else if (message.getText().contains("Games")){
                BotStateContext.getInstance().setCurrentState(BotStates.GAMES_WANTED);
            }
            else if (message.getText().contains("FAQ")){
                BotStateContext.getInstance().setCurrentState(BotStates.FAQ_WANTED);
            }
            else if (message.getText().contains("Weather")){
                BotStateContext.getInstance().setCurrentState(BotStates.WEATHER_WANTED);
            }
            else {
                BotStateContext.getInstance().setCurrentState(BotStates.NOT_IMPLEMENTED);
            }

            String reply = BotStateContext.getInstance().chooseHandlerForActualState();

            if(!flag) sendMsg(message, reply);
        }
    }
}
