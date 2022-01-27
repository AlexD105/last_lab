package com.lab7.api.command;

import com.lab7.api.command.annotation.AttachedObj;
import com.lab7.api.entity.Dragon;
import com.lab7.api.entity.DragonCave;
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
@AttachedObj(type = DragonCave.class)
public class RemoveAllByCave extends Command {

    private UserInput userInput;

    public RemoveAllByCave(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq message) throws Exception {

        DragonCave dragonCave = ((MessageReqObj) message).getDragon().getCave();

        Set<ConstraintViolation<DragonCave>> violations = getValidator().validate(dragonCave);

        if(violations.isEmpty()) {

            for (int i = 0; i < getDragonList().size(); i++) {
                if(getDragonList().get(i).getCave().equals(dragonCave)
                        && getDragonService().delete(getDragonList().get(i), message.getUser())) {
                    getDragonList().remove(getDragonList().get(i));
                }
            }

            return getFormatter().formatBooleanOperation(true);
        }

        violations.forEach(v -> System.err.println(v.getMessage()));
        return getFormatter().formatBooleanOperation(false);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoRemoveAllByCave");
    }
}
