package com.lab7.api.command;

import com.lab7.api.entity.Dragon;
import com.lab7.api.command.annotation.AttachedObj;
import com.lab7.api.io.ConsoleUserInput;
import com.lab7.api.io.UserInput;
import com.lab7.api.message.MessageReq;
import com.lab7.api.message.MessageReqObj;
import com.lab7.api.service.DragonService;
import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.Stack;

@Getter @Setter
@AttachedObj
public class Add extends Command {

    private UserInput consoleUserInput;

    public Add(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);

        consoleUserInput = new ConsoleUserInput();

    }

    @Override
    public String execute(MessageReq message) throws Exception {

        Dragon dragon = ((MessageReqObj) message).getDragon();

        Set<ConstraintViolation<Dragon>> violations = getValidator().validate(dragon);

        if(violations.isEmpty()) {

            if(getDragonService().save(dragon, message.getUser()) != null) {
                getDragonList().add(dragon);
                settleIds();
                return getFormatter().formatBooleanOperation(true);
            } else {
                return getFormatter().formatBooleanOperation(false);
            }
        }

        violations.forEach(v -> System.err.println(v.getMessage()));
        return getFormatter().formatBooleanOperation(false);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoAdd");
    }
}
