package com.lab7.api.command;

import com.lab7.api.entity.Dragon;
import com.lab7.api.message.MessageReq;
import com.lab7.api.service.DragonService;

import java.util.Collections;
import java.util.Stack;

public class Shuffle extends Command {

    public Shuffle(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq ignore) {

        Collections.shuffle(getDragonList());

        return getFormatter().formatCollection(getDragonList());
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoShuffle");
    }
}
