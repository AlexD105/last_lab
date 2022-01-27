package com.lab7.client;

import com.lab7.api.command.Command;
import com.lab7.api.command.annotation.AttachedObj;
import com.lab7.api.command.annotation.AttachedObjFactory;
import com.lab7.api.entity.Dragon;
import com.lab7.api.entity.User;
import com.lab7.api.message.MessageReq;
import com.lab7.api.message.MessageResp;
import com.lab7.api.print.api.Printer;
import com.lab7.api.print.implementation.PrinterImpl;
import lombok.AllArgsConstructor;

import java.util.Scanner;

@AllArgsConstructor
public class ClientExecutor {

    private final Client client;
    private final Printer printer;

    public void start() throws Exception {

        User user = client.auth();

        System.out.println("Приветствие");

        Scanner sc = new Scanner(System.in);
        String response;
        while (!(response = sc.nextLine()).equals("exit")) {
            try {
                // Вызываем команду и выводим результат
                // В случае, если метод execute_command бросил исключение,
                // оно обрабатывается, а программа продолжает работу.
                if (response.equals("")) {
                    System.out.println("Пожалуйста, введите корректные данные");
                } else {
                    String[] array = response.split(" ");
                    String commandName = array[0];

                    MessageReq message = client.prepareRequest(response, validateAnnotation(Command.validateCommand(commandName), user));
                    message.setUser(user);
                    MessageResp result = client.sendRequest(message);

                    printer.printResult(result.getResult());
                }

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        client.stop("Программа завершилась успешно");
    }

    private Dragon validateAnnotation(Class<? extends Command> c, User user) throws Exception {

        if(c.isAnnotationPresent(AttachedObj.class)) {
            AttachedObj attachedObj = c.getAnnotation(AttachedObj.class);

            return AttachedObjFactory.newInstance(attachedObj.type(), user);
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        new ClientExecutor(new Client(), new PrinterImpl()).start();
    }
}
