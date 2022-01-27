package com.lab7.api.command;

import com.lab7.api.entity.Dragon;
import com.lab7.api.message.MessageReq;
import com.lab7.api.service.DragonService;

import java.util.Stack;

public class Exit extends Command {

    public Exit(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq ignore) {

        // Сохраняем коллекция перед выходом
        new Save(getDragonList(), getDragonService()).execute(null);

        System.exit(0);
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoExit");
    }
}
