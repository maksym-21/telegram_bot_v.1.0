package handlers.states;

public class NewsWantedHandler implements state.Handler {
    @Override
    public String handle() {
        return  "Please enter a city or country or keyword :";
    }
}
