package com.lab7.api.i18n;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ResourceBundle;

@AllArgsConstructor
@Getter @Setter
public class MessengerImpl implements Messenger {

    private ResourceBundle rb;

    @Override
    public String getMessage(String msg) {
        return rb.getString(msg);
    }
}
