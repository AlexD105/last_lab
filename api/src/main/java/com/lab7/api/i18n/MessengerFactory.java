package com.lab7.api.i18n;

import java.util.ResourceBundle;

public class MessengerFactory {

    private static Messenger instance;

    public static Messenger getMessenger() {
        if(instance == null) {
            return new MessengerImpl(ResourceBundle.getBundle("text", Messenger.initLang()));
        }
        return instance;
    }
}
