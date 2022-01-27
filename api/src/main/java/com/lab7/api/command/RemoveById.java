package com.lab7.api.command;

import com.lab7.api.entity.Dragon;
import com.lab7.api.message.MessageReq;
import com.lab7.api.service.DragonService;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

@Getter @Setter
public class RemoveById extends Command {

    public RemoveById(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq message) {

        Dragon dragon = getDragonList()
                .stream()
                .filter(d -> d.getId().equals(Integer.parseInt(message.getCommand().split(" ")[1])))
                .findFirst()
                .orElse(null);

        if(dragon != null && getDragonService().delete(dragon, message.getUser())) {
            return getFormatter().formatBooleanOperation(true);
        }

        return getFormatter().formatBooleanOperation(false);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoRemoveById");
    }
}
