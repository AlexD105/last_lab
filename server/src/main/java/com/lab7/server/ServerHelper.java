package com.lab7.server;

import com.lab7.api.command.manager.CommandManager;
import com.lab7.api.entity.Dragon;
import com.lab7.api.i18n.Messenger;
import com.lab7.api.i18n.MessengerFactory;
import com.lab7.api.print.api.Formatter;
import com.lab7.api.print.api.Printer;
import com.lab7.api.service.DragonService;
import com.lab7.api.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Stack;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ServerHelper {

    // Константы, служащие флагами при регистрации/авторизации пользователя
    public static final String SL = "success_login";
    public static final String FL = "failure_login";
    public static final String SR = "success_registration";
    public static final String FR = "failure_registration";

    private static final Messenger messenger = MessengerFactory.getMessenger();

    private Stack<Dragon> mDataSet;
    private CommandManager commandManager;
    private Formatter formatter;
    private Printer printer;
    private UserService userService;
    private DragonService dragonService;

    public void init() {
        commandManager.setFormatter(formatter);
        commandManager.setPrinter(printer);
        commandManager.setMDataSet(mDataSet);
        commandManager.setDragonService(dragonService);
    }

}
