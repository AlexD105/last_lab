package com.lab7.api.command;

import com.lab7.api.entity.Dragon;
import com.lab7.api.message.MessageReq;
import com.lab7.api.service.DragonService;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Formatter;
import java.util.Stack;

@Getter @Setter
public class Info extends Command {

    public Info(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }

    @Override
    public String execute(MessageReq ignore) {
        String fileCreationDate = null;
        String elementType = null;

        try {
            BasicFileAttributes attr = Files.readAttributes(new File(System.getenv("XML_FILE")).toPath(), BasicFileAttributes.class);
            fileCreationDate = String.valueOf(attr.creationTime());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Field hashSet = this.getClass().getSuperclass().getDeclaredField("dragonList");
            elementType = String.valueOf(hashSet.getGenericType());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return new Formatter()
                .format(getMessenger().getMessage("type") + ": %s\n" +
                                getMessenger().getMessage("size") + ": %d\n" +
                                getMessenger().getMessage("creationDate") +": %s\n",
                        elementType, getDragonList().toArray().length, fileCreationDate)
                .toString();
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoInfo");
    }
}
