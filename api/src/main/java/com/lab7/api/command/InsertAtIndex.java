package com.lab7.api.command;

import com.lab7.api.command.annotation.AttachedObj;
import com.lab7.api.entity.Dragon;
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
public class InsertAtIndex extends Command {

    private UserInput consoleUserInput;

    public InsertAtIndex(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq message) throws Exception {

        String index = getArg(message.getCommand());

        Dragon dragon = ((MessageReqObj) message).getDragon();

        Set<ConstraintViolation<Dragon>> violations = getValidator().validate(dragon);

        if(violations.isEmpty()) {

            Dragon dragon1 = getDragonList().set(Integer.parseInt(index), dragon);

            if(dragon1 != null && getDragonService().update(dragon1, message.getUser())) {
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
        return super.toString() + ": " + getMessenger().getMessage("infoInsertAtIndex");
    }
}
