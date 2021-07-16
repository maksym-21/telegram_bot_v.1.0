package handlers;

import model.Emoji;
import state.Handler;

public class WeatherWantedHandler implements Handler {
    @Override
    public String handle() {
        return "Please enter city name" + Emoji.CITYSCAPE + ":";
    }
}
