package handlers;

import state.Handler;

public class GamesWantedHandler implements Handler {
    @Override
    public String handle() {
        return new NotImplememtedHandler().handle();
    }
}
