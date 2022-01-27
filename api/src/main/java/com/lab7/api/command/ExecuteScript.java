package com.lab7.api.command;

import com.lab7.api.command.manager.CommandManager;
import com.lab7.api.entity.Dragon;
import com.lab7.api.io.FileUserInput;
import com.lab7.api.io.UserInput;
import com.lab7.api.message.MessageReq;
import com.lab7.api.service.DragonService;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

@Getter @Setter
public class ExecuteScript extends Command {

    private UserInput userInput;
    private CommandManager manager;

    public ExecuteScript(Stack<Dragon> dragonList, DragonService dragonService) {
        super(dragonList, dragonService);
    }


    @Override
    public String execute(MessageReq message) throws Exception {
        return execute_script(getArg(message.getCommand()), message);
    }

    /**
     * Метод выполняет чтения файла-скрипта
     * Построчно считывает команды с файла и посылает их execute_command()
     * @param fileName - имя файла скрипта
     * @return - Результат работы всего скрипта
     * @throws Exception - В случае отсутствия файла или ошибок в скрипте
     */
    public String execute_script(String fileName, MessageReq messageReq) throws Exception {

        this.userInput = new FileUserInput(new Scanner(new File(fileName)));

        if(!new File(fileName).exists()) throw new IOException(getMessenger().getMessage("fileNotFound"));

        try(Scanner scanner = new Scanner(new File(fileName))) {

            // Перед выполнением скрипта меняем источник ввода объектов на файловый
            StringBuilder result = new StringBuilder();

            while (scanner.hasNextLine()) {

                String command = scanner.nextLine();

                // Обязательно нужно проверить условие зацикливания, чтобы скрипт не вызвал сам себя
                if(command.equals("executescript " + fileName)) {
                    result.append(getMessenger().getMessage("recursiveCallScript")).append("\n");
                } else {
                    result.append(manager.executeCommand(new MessageReq(messageReq.getUser(), command))).append("\n");
                }
            }

            return result.toString();

        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception(getMessenger().getMessage("scriptNotValid"));
        }
    }

    public void setCommandManager(CommandManager manager) {
        this.manager = manager;
    }


    @Override
    public String toString() {
        return super.toString() + ": " + getMessenger().getMessage("infoExecuteScript");
    }
}
