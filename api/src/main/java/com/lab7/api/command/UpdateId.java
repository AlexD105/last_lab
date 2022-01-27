package com.lab7.api.command;

import com.lab7.api.command.annotation.AttachedObj;
import com.lab7.api.entity.Dragon;
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
public class UpdateId extends Command {

    public UpdateId(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq message) {

        String id = getArg(message.getCommand());

        for (Dragon d: getDragonList()) {
            if(d.getId().equals(Integer.parseInt(id))) {
                try {
                    Dragon dragon = ((MessageReqObj) message).getDragon();
                    Set<ConstraintViolation<Dragon>> violations = getValidator().validate(dragon);

                    if(violations.isEmpty() && getDragonService().update(dragon, message.getUser())) {
                        d = dragon;
                        return getFormatter().formatBooleanOperation(true);
                    }
                    violations.forEach(v -> System.err.println(v.getMessage()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return getFormatter().formatBooleanOperation(false);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoUpdateId");
    }
}
