package com.lab7.api.command;

import com.lab7.api.entity.Dragon;
import com.lab7.api.entity.DragonCharacter;
import com.lab7.api.message.MessageReq;
import com.lab7.api.service.DragonService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Getter @Setter
public class FilterLessThanCharacter extends Command {

    public FilterLessThanCharacter(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq message) {

        String character = getArg(message.getCommand());

        List<Dragon> resultList = getDragonList()
                .stream()
                .filter(d -> d.getCharacter() != null && d.getCharacter().compareTo(DragonCharacter.valueOf(character)) < 0)
                .collect(Collectors.toList());

        return getFormatter().formatCollection(resultList);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoFilterLessThanCharacter");
    }
}
