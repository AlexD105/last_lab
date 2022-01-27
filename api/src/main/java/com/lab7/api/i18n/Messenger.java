package com.lab7.api.i18n;

import java.util.Locale;

public interface Messenger {
    String getMessage(String msg);

    static Locale initLang() {

        // Возможность выбора языка была отключена. Чтобы ее включить, необходимо заполнить файл re_RU.properties
        // и раскомментить код ниже, а также закомментить строку Language

        Language language = Language.ru_RU;

//        System.out.println("Пожалуйста, выберите язык (ru_RU, uk_UA). Please, choose your language");
//        try {
//            language = Language.valueOf(new Scanner(System.in).nextLine());
//        } catch (Exception e) {
//            System.out.println("Пожалуйста, введите корректные данные");
//            System.exit(-1);
//        }

        String[] array = language.toString().split("_");
        return new Locale(array[0], array[1]);
    }
}
