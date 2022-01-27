package com.lab7.api.command;

import com.lab7.api.entity.Dragon;
import com.lab7.api.message.MessageReq;
import com.lab7.api.service.DragonService;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

@Getter @Setter
public class RemoveAtIndex extends Command {

    public RemoveAtIndex(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq message) {

        int index = Integer.parseInt(getArg(message.getCommand()));

        for (int i = 0; i < getDragonList().size(); i++) {
            if(index == i && getDragonService().delete(getDragonList().get(i), message.getUser())) {
                getDragonList().remove(getDragonList().get(i));
                return getFormatter().formatBooleanOperation(true);
            }
        }

        return getFormatter().formatBooleanOperation(false);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoRemoveAtIndex");
    }
}
