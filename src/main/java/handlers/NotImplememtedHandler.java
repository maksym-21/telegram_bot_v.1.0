package handlers;

import model.Emoji;
import state.Handler;

public class NotImplememtedHandler implements Handler {
    @Override
    public String handle() {
        return "Sorry that function is currently unavailable" + Emoji.GREY_EXCLAMATION;
    }
}
