package config;

import handlers.PropertiesHandler;
import model.Emoji;
import model.WeatherModel;
import model.Weather;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
     *
     * @throws TelegramApiException -> bad execution of reply-message
     */
    public void sendMsgWithGameKeyboard(Message message) throws TelegramApiException {
        SendMessage reply = new SendMessage();

        reply.setChatId(String.valueOf(message.getChatId()));
        reply.setReplyToMessageId(message.getMessageId());

        reply.setText("Please choose one : ");

        setButton(reply);

        reply.setReplyMarkup(getInlineMarkup());

        execute(reply);
    }

    /**
     * @return Telegram keyboard-message
     */
    public InlineKeyboardMarkup getInlineMarkup(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Snake" + Emoji.SNAKE);
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Tic-tac-toe" + Emoji.TIC_TAC);
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Dinosaur" + Emoji.DINOSAUR);
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("smth else" + Emoji.TRIDENT);

        // every button should have callback data or error
        button1.setCallbackData("Snake");
        button2.setCallbackData("Tic-tac-toe");
        button3.setCallbackData("Dinosaur");
        button4.setCallbackData("smth else");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(button1);
        row1.add(button2);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(button3);
        row2.add(button4);

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(row1);
        lists.add(row2);

        inlineKeyboardMarkup.setKeyboard(lists);

        return inlineKeyboardMarkup;
    }

    /**
     * @return username of bot
     */
    public String getBotUsername() {
        return PropertiesHandler.getStringFromProperty("my_bot_username");
    }

    /** api is secure -> do not tell anyone
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

        // add code and states
        if (message != null && message.hasText()) {
            if (BotStateContext.getInstance().getCurrentState()==BotStates.WEATHER_WANTED &&
                    (   !update.getMessage().getText().contains("Games") &&
                        !update.getMessage().getText().contains("FAQ") &&
                        !update.getMessage().getText().contains("Weather") &&
                        !update.getMessage().getText().contains("News")    ) ){
                try {
                    String weather_prompt = update.getMessage().getText();
                    WeatherModel model = new WeatherModel();

                    // if input doesn't contain a digit or is not a digit then process
                    if (!weather_prompt.matches(".*\\d.*")) {
                        sendMsg(message, Weather.getWeather(weather_prompt, model));
                    }
                    else throw new IOException("Incorrect input!");
                } catch (IOException e) {
                    sendMsg(message,"City was not found" + Emoji.GREY_EXCLAMATION + "\nPlease try again");
                }finally {
                    BotStateContext.getInstance().setCurrentState(BotStates.WEATHER_DONE);
                }
            }
            else if (message.getText().contains("Games")){
                BotStateContext.getInstance().setCurrentState(BotStates.GAMES_WANTED);

                try {
                    sendMsgWithGameKeyboard(update.getMessage());
                } catch (TelegramApiException exception) {
                    sendMsg(update.getMessage(),"Sorry something went wrong!");
                    exception.printStackTrace();
                }finally {
                    BotStateContext.getInstance().setCurrentState(BotStates.GAMES_DONE);
                }
            }
            else if (message.getText().contains("FAQ")){
                BotStateContext.getInstance().setCurrentState(BotStates.FAQ_WANTED);

                sendMsg(update.getMessage(),BotStateContext.getInstance().chooseHandlerForActualState());
            }
            else if (message.getText().contains("Weather")){
                BotStateContext.getInstance().setCurrentState(BotStates.WEATHER_WANTED);

                sendMsg(update.getMessage(),BotStateContext.getInstance().chooseHandlerForActualState());
            }
            else {
                BotStateContext.getInstance().setCurrentState(BotStates.NOT_IMPLEMENTED);

                sendMsg(update.getMessage(),BotStateContext.getInstance().chooseHandlerForActualState());
            }

            BotStateContext.getInstance().checkIfStateIsFinished();
        }
    }
}
