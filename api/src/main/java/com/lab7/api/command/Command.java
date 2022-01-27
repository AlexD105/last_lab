package com.lab7.api.command;

import com.lab7.api.entity.Dragon;
import com.lab7.api.exception.NoSuchCommandException;
import com.lab7.api.i18n.Messenger;
import com.lab7.api.i18n.MessengerFactory;
import com.lab7.api.message.MessageReq;
import com.lab7.api.print.api.Formatter;
import com.lab7.api.print.implementation.FormatterImpl;

import com.lab7.api.service.DragonService;
import lombok.*;
import org.reflections.Reflections;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.Stack;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public abstract class Command {

    private Messenger messenger = MessengerFactory.getMessenger();

    private Stack<Dragon> dragonList;

    private DragonService dragonService;

    private Formatter formatter;

    private Validator validator;

    public Command(Stack<Dragon> dragonList, DragonService dragonService) {
        this.dragonList = dragonList;
        this.formatter = new FormatterImpl();

        this.dragonService = dragonService;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public abstract String execute(MessageReq message) throws Exception;

    protected String getArg(String command) {
        return command.split(" ")[1];
    }

    public static Class<? extends Command> validateCommand(String commandName) {

        Reflections reflections = new Reflections("com.lab7.api");
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);

        for (Class<? extends Command> c: classes) {
            if(c.getSimpleName().equalsIgnoreCase(commandName)) {
                return c;
            }
        }

        throw new NoSuchCommandException("Такой команды не существует");
    }

    protected void settleIds() {
        int id = 0;
        for (Dragon d : dragonList) {
            d.setId(++id);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
