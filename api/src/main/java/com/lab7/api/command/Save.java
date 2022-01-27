package com.lab7.api.command;

import com.lab7.api.entity.Dragon;
import com.lab7.api.message.MessageReq;
import com.lab7.api.service.DragonService;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

@Getter @Setter
public class Save extends Command {

    public Save(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq ignore) {

        getDragonService().deleteAll();
        getDragonService().saveAll(getDragonList());

        return getFormatter().formatBooleanOperation(true);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoSave");
    }
}
