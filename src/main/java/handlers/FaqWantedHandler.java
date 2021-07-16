package handlers;

import model.Emoji;
import state.Handler;

public class FaqWantedHandler implements Handler {
    @Override
    public String handle() {
        return "This " + Emoji.BOT + " tells you \n" +
                "current weather in city, \nwhich you ask " + Emoji.SUNNY + Emoji.UMBRELLA;
    }
}
