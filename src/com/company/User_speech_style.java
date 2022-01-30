package com.company;

import java.util.HashMap;
import java.util.Map;

public class User_speech_style {
    static String symbol_frequency_user_style="";

    public static String user_speech_style() {
        //  Определяем статистику вхождения символов по стилю пользователя
        System.out.println("\t Введи путь до твоего текстового файла");
        System.out.println("\t\t Вот пример: C:\\Users\\User\\Desktop\\name.txt");
        // дается 2 попытки ввести правильно путь,
        // если они исчерпаны, то путь выбирается по умолчанию
        String s_user_style = TryCatch_openFile2.tryCatch_openFile2();
        System.out.println("Файл принят!\n");

        //  !!! Пока на регистр не будем обращать внимание. Поэтому нужно все буквы перевести в нижний регистр
        s_user_style = s_user_style.toLowerCase();
        // Для этого нужно отсортировать полученный мап из (буква - частота вхождения)
//        Разрезаем наш текст по символам
        String[] mas_user = s_user_style.split("");

        // Каждой букве нужно присвоить номер, для этого скорее всего лучше подойдет ArrayMap
        HashMap<String, Integer> map_letters_user_style = new HashMap<>(Map_frequency.map_frequency(mas_user));



//        map_letters.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue().reversed()).
//                forEach(System.out::println); // или любой другой конечный метод              //        Показать Мап отсортированный по убыванию

        String[] qw, wq, wqw;
        for (int i = 0; i < CryptoAnalysis.aAlphabet.size(); i++) {
//      находим самый частый символ в Мап-е с частотой вхождения символов в тексте
            String max_symbol = map_letters_user_style.entrySet().stream().max(Map.Entry.comparingByValue()).toString();

//      Убираем шелуху от "Optional[к=167]", нам нужна только буква
            qw = max_symbol.split("\\[");   // max_symbol = "Optional[к=167]"
            wq = qw[1].split("]");        // qw[1] = "к=167]"
            wqw = wq[0].split("=");         // wq[0] = "к=167"
            //   Нашли самый частый символ - wqw[0], wqw[1] - количество повторений
            symbol_frequency_user_style += wqw[0];
//        Теперь нам нужно удалить максимальную Map строку по ключу - для того чтобы потом найти следующую максимальную
            map_letters_user_style.remove(wqw[0]);
        }
        return symbol_frequency_user_style;
    }
}
