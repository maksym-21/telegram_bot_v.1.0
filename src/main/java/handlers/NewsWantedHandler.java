package handlers;

public class NewsWantedHandler implements state.Handler {
    @Override
    public String handle() {
        return new NotImplememtedHandler().handle();
    }
}
