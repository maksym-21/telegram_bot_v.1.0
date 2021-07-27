package handlers.states;

import state.Handler;

public class CountryForNewsWantedHandler implements Handler {
    @Override
    public String handle() {
        return new NotImplememtedHandler().handle();
    }
}
