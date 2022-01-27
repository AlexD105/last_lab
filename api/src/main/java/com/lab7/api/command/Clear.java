package com.lab7.api.command;

import com.lab7.api.entity.Dragon;
import com.lab7.api.message.MessageReq;
import com.lab7.api.service.DragonService;

import java.util.Stack;

public class Clear extends Command {

    public Clear(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq ignore) {
        getDragonList().clear();
        return getFormatter().formatBooleanOperation(true);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoClear");
    }
}
