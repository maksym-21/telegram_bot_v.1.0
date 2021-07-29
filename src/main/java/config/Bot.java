package config;

import handlers.PropertiesHandler;
import model.Emoji;
import model.news.News;
import model.news.NewsLanguage;
import model.news.NewsModel;
import model.weather.WeatherModel;
import model.weather.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 + ?logging for something functions
 + ?size of buttons(size and color)
 + ?refactoring
 */
public class Bot extends TelegramLongPollingBot {

    private String KEY_WORD_FOR_NEWS = null;
    private static final Logger LOG = LoggerFactory.getLogger(TelegramLongPollingBot.class);

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
        keyboardMarkup.setResizeKeyboard(true);

        // hide a keyboard after using a button
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();

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

        // for emoji specialized due to problem with "_"
        sendMessage.enableHtml(true);

        sendMessage.setText(input);

        LOG.info("Trying to execute a message reply{}", sendMessage.toString());

        // trying to send our response
        try {
            // setting of keyboard , then execute message
            setButton(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Exception -> {}", e.getMessage());

            e.printStackTrace();
        }
    }


    /**
     * @throws TelegramApiException -> bad execution of reply-message
     */
    public void sendMsgWithKeyboard(Message message, InlineKeyboardMarkup markup) throws TelegramApiException {
        SendMessage reply = new SendMessage();

        reply.setChatId(String.valueOf(message.getChatId()));
        reply.setReplyToMessageId(message.getMessageId());

        reply.setText("Please choose one : ");

        // for emoji specialized due to problem with "_"
        reply.enableHtml(true);

        setButton(reply);

        reply.setReplyMarkup(markup);

        execute(reply);
    }


    /**
     * @return Telegram inline keyboard-message
     */
    public InlineKeyboardMarkup getGameInlineMarkup(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Snake" + Emoji.SNAKE);
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Tic-tac-toe" + Emoji.TIC_TAC);
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Dinosaur" + Emoji.DINOSAUR);
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Quiz" + Emoji.TRIDENT);

        // every button should have callback data or error
        button1.setCallbackData("Snake");
        button2.setCallbackData("Tic-tac-toe");
        button3.setCallbackData("Dinosaur");
        button4.setCallbackData("Quiz");

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
     * @return Telegram inline keyboard-message to choose a language of news
     */
    public InlineKeyboardMarkup getNewsLanguageInlineMarkup(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        InlineKeyboardButton button6 = new InlineKeyboardButton();

        String var = String.valueOf(NewsLanguage.de);
        button1.setText(var + Emoji.getFlagByCountryIndex(var));
        var = String.valueOf(NewsLanguage.en);
        button2.setText(var + Emoji.getFlagByCountryIndex("gb"));
        var = String.valueOf(NewsLanguage.es);
        button3.setText(var + Emoji.getFlagByCountryIndex(var));
        var = String.valueOf(NewsLanguage.fr);
        button4.setText(var + Emoji.getFlagByCountryIndex(var));
        var = String.valueOf(NewsLanguage.ru);
        button5.setText(var + Emoji.getFlagByCountryIndex(var));
        var = String.valueOf(NewsLanguage.it);
        button6.setText(var + Emoji.getFlagByCountryIndex(var));


        // every button should have callback data or error
        button1.setCallbackData("de");
        button2.setCallbackData("en");
        button3.setCallbackData("es");
        button4.setCallbackData("fr");
        button5.setCallbackData("ru");
        button6.setCallbackData("it");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(button1);
        row1.add(button2);
        row1.add(button3);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(button4);
        row2.add(button5);
        row2.add(button6);

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
     * @param callbackQuery -> returned value from inline keyboard
     */
    public void processCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException, IOException, RuntimeException {
        SendMessage message = new SendMessage();
        String output;
        boolean flag = false;

        if (callbackQuery.getData().contains("Snake")){
            output = "Sorry game \"Snake\" at the moment is not available";
        }
        else if (callbackQuery.getData().contains("Tic-tac-toe")){
            output = "Sorry game \"Tic-tac-toe\" at the moment is not available";
        }
        else if (callbackQuery.getData().contains("Dinosaur")){
            output = "Sorry game \"Dinosaur\" at the moment is not available";
        }
        else if (callbackQuery.getData().contains("Quiz")){
            output = "Sorry game \"Quiz\" at the moment is not available";
        }
        else if (callbackQuery.getData().contains("de") ||
                callbackQuery.getData().contains("en") ||
                callbackQuery.getData().contains("es") ||
                callbackQuery.getData().contains("fr") ||
                callbackQuery.getData().contains("ru") ||
                callbackQuery.getData().contains("it")){

            flag = true;
            output = null;
            // message.setText(output);
            //message.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));


            NewsLanguage language = NewsLanguage.valueOf(callbackQuery.getData().substring(0,2));
            String date = Bot.getCurrentDate();
            String chatID = String.valueOf(callbackQuery.getMessage().getChatId());

            if (KEY_WORD_FOR_NEWS!=null) {
                sendAndEXeCUTeXAllNewsFromArray(News.getNews(KEY_WORD_FOR_NEWS,date,language),chatID);
            }
            else throw new RuntimeException();
        }
        else {
            output = "Your request can not currently be processed";
        }


        if (!flag) {
            message.setText(output);
            message.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));

            execute(message);
        }
    }

    /**
     * @return date of current day for the news
     */
    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(new Date());
    }

    private void sendAndEXeCUTeXAllNewsFromArray(ArrayList<NewsModel> arrayList,String chatID) throws TelegramApiException {
        for (NewsModel news : arrayList) {
            SendMessage sendMessage = new SendMessage();

            sendMessage.setText(news.toString());
            sendMessage.setChatId(chatID);

            execute(sendMessage);

            LOG.info("Successful execution of news #" + news.getHashId());
        }
    }

    /**
     * a main method which reacts on updates and sets relevant state for our bot
     */
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            if (BotStateContext.getInstance().getCurrentState()==BotStates.WEATHER_WANTED &&
                    (   !update.getMessage().getText().contains("Games") &&
                        !update.getMessage().getText().contains("FAQ") &&
                        !update.getMessage().getText().contains("Weather") &&
                        !update.getMessage().getText().contains("News")    ) ){

                String weather_prompt = update.getMessage().getText();

                WeatherModel model = new WeatherModel();

                try {
                    // if input doesn't contain a digit or is not a digit then process
                    if (!weather_prompt.matches(".*\\d.*")) {
                        LOG.info("Preparing weather prompt of weather forecast");

                        sendMsg(message, Weather.getWeather(weather_prompt, model));
                    }
                    else throw new IOException("Incorrect input!");
                } catch (IOException e) {
                    LOG.error("Wanted prompt wasn't processed successfully due to bad input -> {}",weather_prompt);

                    sendMsg(message,"City was not found" + Emoji.GREY_EXCLAMATION + "\nPlease try again");
                }finally {
                    LOG.info("Switching to other state from {} to {}",BotStateContext.getInstance().getCurrentState(),BotStates.WEATHER_DONE);

                    BotStateContext.getInstance().setCurrentState(BotStates.WEATHER_DONE);
                }
            }
            else if (BotStateContext.getInstance().getCurrentState()==BotStates.NEWS_WANTED &&
                    (!update.getMessage().getText().contains("Games") &&
                     !update.getMessage().getText().contains("FAQ") &&
                     !update.getMessage().getText().contains("Weather") &&
                     !update.getMessage().getText().contains("News")    )){

                /*
                 *  ? -> show how many news were found
                 */

                LOG.info("Switching to other state from {} to {}",BotStateContext.getInstance().getCurrentState(),BotStates.NEWS_DONE);

                KEY_WORD_FOR_NEWS = update.getMessage().getText();

                try {
                    sendMsgWithKeyboard(update.getMessage(),getNewsLanguageInlineMarkup());
                } catch (TelegramApiException exception) {
                    sendMsg(update.getMessage(),"Sorry something went wrong!");

                    LOG.error("Exception -> {}",exception.getMessage());
                }
                finally {
                    LOG.info("Switching to other state from {} to {}",BotStateContext.getInstance().getCurrentState(),BotStates.GAMES_DONE);

                    BotStateContext.getInstance().setCurrentState(BotStates.GAMES_DONE);
                }
            }
            else if (message.getText().contains("Games")){
                LOG.info("Switching to other state from {} to {}",BotStateContext.getInstance().getCurrentState(),BotStates.GAMES_PROPOSITION);

                try {
                    BotStateContext.getInstance().setCurrentState(BotStates.GAMES_PROPOSITION);

                    sendMsgWithKeyboard(update.getMessage(),getGameInlineMarkup());
                } catch (TelegramApiException exception) {
                    sendMsg(update.getMessage(),"Sorry something went wrong!");

                    LOG.error("Exception -> {}",exception.getMessage());
                }
                finally {
                    LOG.info("Switching to other state from {} to {}",BotStateContext.getInstance().getCurrentState(),BotStates.WEATHER_DONE);

                    BotStateContext.getInstance().setCurrentState(BotStates.GAMES_DONE);
                }
            }
            else if (message.getText().contains("FAQ")){
                LOG.info("Switching to other state from {} to {}",BotStateContext.getInstance().getCurrentState(),BotStates.FAQ_WANTED);

                BotStateContext.getInstance().setCurrentState(BotStates.FAQ_WANTED);

                sendMsg(update.getMessage(),BotStateContext.getInstance().chooseHandlerForActualState());
            }
            else if (message.getText().contains("Weather")){
                LOG.info("Switching to other state from {} to {}",BotStateContext.getInstance().getCurrentState(),BotStates.WEATHER_WANTED);

                BotStateContext.getInstance().setCurrentState(BotStates.WEATHER_WANTED);

                sendMsg(update.getMessage(),BotStateContext.getInstance().chooseHandlerForActualState());
            }
            else if (message.getText().contains("News")){
                LOG.info("Switching to other state from {} to {}",BotStateContext.getInstance().getCurrentState(),BotStates.NEWS_WANTED);

                BotStateContext.getInstance().setCurrentState(BotStates.NEWS_WANTED);

                sendMsg(update.getMessage(),BotStateContext.getInstance().chooseHandlerForActualState());
            }
            else {
                LOG.info("Installing new state ( from {} to {} )",BotStateContext.getInstance().getCurrentState(),BotStates.NOT_IMPLEMENTED);

                BotStateContext.getInstance().setCurrentState(BotStates.NOT_IMPLEMENTED);

                sendMsg(update.getMessage(),BotStateContext.getInstance().chooseHandlerForActualState());
            }

            BotStateContext.getInstance().checkIfStateIsFinished();
        }
        else if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();

            BotStateContext.getInstance().setCurrentState(BotStates.INITIAL);

            try {
                processCallbackQuery(callbackQuery);

                LOG.info("Successful execution of reply callback query");
            }
             catch (TelegramApiException exception) {
                LOG.error("Failed execution of reply callback query -> {}",exception.getMessage());
             }
             catch (RuntimeException exception){
                LOG.error("String KEY_WORD_FOR_NEWS (value -> {}) is null -> {}", KEY_WORD_FOR_NEWS,exception.getMessage());
             }
             catch (IOException exception){
                LOG.error("Problem with API News.getNews() -> {}",exception.getMessage());
             }
        }
    }
}
