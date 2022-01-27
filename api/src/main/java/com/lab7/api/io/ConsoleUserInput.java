package com.lab7.api.io;

import com.lab7.api.entity.*;
import com.lab7.api.entity.User;

import java.util.Scanner;

/**
 * Класс, предоставляющий логику чтения элементов с консоли
 */
public class ConsoleUserInput extends UserInput {

    public ConsoleUserInput() {
        super(new Scanner(System.in));
    }

    /**
     * Вводя элемент с консоли, мы должны вводить каждое поле в цикле до тех пор, пока ввод не будет корректным.
     * В случае некорректного ввода - бросается исключение и программа снова просит нас ввести значение
     * @return - введенный объект
     */
    @Override
    public Dragon enterElement(User user) throws Exception {

        String name;
        Coordinates coordinates;
        long age;
        Color color;
        DragonType type;
        DragonCharacter character;

        while (true) {
            System.out.println(messenger.getMessage("enterName"));
            try {
                name = readName();
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            System.out.println(messenger.getMessage("enterCoordinates"));
            try {
                coordinates = readCoordinates();
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            System.out.println(messenger.getMessage("enterAge"));
            try {
                age = readAge();
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            System.out.println(messenger.getMessage("enterColor"));
            try {
                color = readColor();
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            System.out.println(messenger.getMessage("enterType"));
            try {
                type = readDragonType();
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            System.out.println(messenger.getMessage("enterCharacter"));
            try {
                character = readDragonCharacter();
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println(messenger.getMessage("askForNull"));
        String result = new Scanner(System.in).nextLine();

        return new Dragon(name, coordinates, age, color, type, character, result.equals("NULL") ? new DragonCave() : enterCave(), user.getName());
    }

    @Override
    public DragonCave enterCave() throws Exception {
        DragonCave cave = new DragonCave();

        while (true) {
            System.out.println(messenger.getMessage("enterDepth"));
            try {
                cave.setDepth(readDepth());
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            System.out.println(messenger.getMessage("enterWealth"));
            try {
                cave.setNumberOfTreasures(readNumberOfTreasures());
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return cave;
    }
}

