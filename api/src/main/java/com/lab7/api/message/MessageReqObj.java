package com.lab7.api.message;

import com.lab7.api.entity.Dragon;
import com.lab7.api.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MessageReqObj extends MessageReq {

    public MessageReqObj(User user, String command, Dragon dragon) {
        super(user, command);

        this.dragon = dragon;
    }

    public MessageReqObj(String command, Dragon dragon) {
        this(null, command, dragon);
    }

    private Dragon dragon;

}
