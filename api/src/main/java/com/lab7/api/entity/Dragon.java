package com.lab7.api.entity;

import com.lab7.api.i18n.Messenger;
import com.lab7.api.i18n.MessengerFactory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
public class Dragon implements Comparable<Dragon>, Serializable {

    private static Integer static_id = 1;

    public static Messenger messenger = MessengerFactory.getMessenger();

    @NotNull(message = "id can't be null")
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotNull(message = "Name can't be null")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull(message = "Coordinates can't be null")
    private Coordinates coordinates; //Поле не может быть null

    @NotNull(message = "Creation date can't be null")
    private String creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Min(value = 1, message = "Age can't be less than 1")
    private long age; //Значение поля должно быть больше 0

    private Color color; //Поле может быть null

    @NotNull(message = "Type can't be null")
    private DragonType type; //Поле не может быть null

    @NotNull(message = "Character can't be null")
    private DragonCharacter character; //Поле не может быть null

    private DragonCave cave; //Поле может быть null

    @NotNull
    private String user_name;

    public Dragon(String name,
                  Coordinates coordinates,
                  long age,
                  Color color,
                  DragonType type,
                  DragonCharacter character,
                  DragonCave cave,
                  String user_name) {
        this.id = static_id++;
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.color = color;
        this.type = type;
        this.character = character;
        this.cave = cave;
        this.user_name = user_name;

        // Форматируем дату создания в обычную строку, чтобы ее было удобно сереализовывать
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        this.creationDate = LocalDate.now().format(dateFormatter);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dragon)) return false;
        Dragon dragon = (Dragon) o;
        return age == dragon.age
                && id.equals(dragon.id)
                && name.equals(dragon.name)
                && coordinates.equals(dragon.coordinates)
                && creationDate.equals(dragon.creationDate)
                && color == dragon.color
                && type == dragon.type
                && character == dragon.character
                && Objects.equals(cave, dragon.cave);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, age, color, type, character, cave);
    }

    @Override
    public String toString() {
        return messenger.getMessage("dragon") + "{" + "\n" +
                messenger.getMessage("id") + " = " + id + "\n" +
                messenger.getMessage("name") + " = '" + name + '\'' + "\n" +
                messenger.getMessage("coordinates") + " = " + coordinates + "\n" +
                messenger.getMessage("creationDate") + " = " + creationDate + "\n" +
                messenger.getMessage("age") + " = " + age + "\n" +
                messenger.getMessage("color") + " = " + color + "\n" +
                messenger.getMessage("type") + " = " + type + "\n" +
                messenger.getMessage("character") + " = " + character + "\n" +
                messenger.getMessage("cave") + " = " + cave + "\n" +
                "}\n";
    }

    @Override
    public int compareTo(Dragon o) {
        return Comparator
                .comparing(Dragon::getName)
                .thenComparing(Dragon::getAge)
                .compare(this, o); // Здесь мы сортируем объекты в алфавитном порядке, а после в порядке возраста
    }
}