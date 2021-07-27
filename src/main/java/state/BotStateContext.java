package state;

import handlers.states.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class BotStateContext {
    @Setter
    @Getter
    private BotStates currentState = null;

    private BotStateContext(){}

    private static BotStateContext botStateContext;
    private static final Map<BotStates,Handler> navigator = new HashMap<>();

    static {
        navigator.put(BotStates.INITIAL, new InitialHandler());
        navigator.put(BotStates.WEATHER_WANTED, new WeatherWantedHandler());
        navigator.put(BotStates.NEWS_WANTED, new NewsWantedHandler());
        navigator.put(BotStates.COUNTRY_FOR_NEWS_WANTED, new CountryForNewsWantedHandler());
        navigator.put(BotStates.FAQ_WANTED, new FaqWantedHandler());
        navigator.put(BotStates.NOT_IMPLEMENTED, new NotImplememtedHandler());
        navigator.put(BotStates.GAMES_WANTED, new GamesWantedHandler());
    }

    public static BotStateContext getInstance() {
        if (botStateContext==null) botStateContext = new BotStateContext();
        return botStateContext;
    }

    public void checkIfStateIsFinished(){
        if (currentState==BotStates.NOT_IMPLEMENTED ||
            currentState==BotStates.FAQ_WANTED ||
            currentState==BotStates.GAMES_DONE ||
            currentState==BotStates.WEATHER_DONE ||
            currentState==BotStates.NEWS_DONE)
                currentState = BotStates.INITIAL;
    }

    public String chooseHandlerForActualState(){
        return navigator.get(currentState).handle();
    }
}
