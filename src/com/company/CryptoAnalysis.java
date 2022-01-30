package com.company;

import java.util.*;

public class CryptoAnalysis {
    HashMap<String, Integer> map_letters = new HashMap<>();
    static String symbol_frequency_easy = " оетасинмвлдркяып,бьйуч.жгзхюшщёэцф-?ъ!\":";
    static String[] mas_symbol_frequency = symbol_frequency_easy.split("");

    //  создадим список из букв и символов для дальнейшей работы
    static String s_alphabet = "абвгдеёжзийклмнопрстуфхцчшщьыъэюя.,\":-!? ";
    static ArrayList<String> aAlphabet = new ArrayList<>(Arrays.asList(s_alphabet.split("")));

    public void cryptoAnalysis(String cryptoLine) {
        Scanner vvod = new Scanner(System.in);

        // Вообще хорошо было бы, чтобы программа сама подбирала последующие символы на основе популярных слов
        // Есть предположение, что сбивается статистика потому что частота
        // вхождения символов у каких-то одинаковая из-за этого путаница
        // ПОДТВЕРДИЛОСЬ!!!    НУЖНО ИСПРАВИТЬ - либо текст побольше, либо дополнить условиями

        // массив символов из текста
        String[] mas_cryptoLine = cryptoLine.split("");
        map_letters.putAll(Map_frequency.map_frequency(mas_cryptoLine));


        String[] qw, wq, wqw;
        ArrayList<Integer> after_replace = new ArrayList<>();   // сюда будут заноситься символы, которые уже заменены
//        after_replace.clear();

        // Повторяем пока не заменим все символы в тексте
        for (int i = 0; i < aAlphabet.size(); i++) {
//      находим самый частый символ в Мап-е с частотой вхождения символов в тексте
            String max_symbol = map_letters.entrySet().stream().max(Map.Entry.comparingByValue()).toString();

//      Убираем шелуху от "Optional[к=167]", нам нужна только цифра
            qw = max_symbol.split("\\[");   // max_symbol = "Optional[к=167]"
            wq = qw[1].split("]");        // qw[1] = "к=167]"
            wqw = wq[0].split("=");         // wq[0] = "к=167"

            //   Нашли самый частый символ - wqw[0], wqw[1] - количество повторений
//      Теперь мы знаем какой символ встретился чаще всего,
//      Теперь нам нужно заменить каждый такой символ в тексте на самый частый по статистике
            for (int j = 0; j < mas_cryptoLine.length; j++) {
                // Если в зашифрованном массиве символов всего текста встретится символ с максимальным вхождением
                // И Нам не нужно менять то, что уже поменяно
                if (mas_cryptoLine[j].equals(wqw[0]) && !after_replace.contains(j)) {
                    // То заменяем его на символ с максимальным вхождением по статистике
                    mas_cryptoLine[j] = mas_symbol_frequency[i];
                    after_replace.add(j);   // сюда будут заноситься номера поменянных ячеек, чтобы больше их не менять
                }
            }
//        Теперь нам нужно удалить максимальную Map строку по ключу - для того чтобы потом найти следующую максимальную
            map_letters.remove(wqw[0]);
        }

        // Записываем в строку
        for (String s : mas_cryptoLine) {
            Main.s_Crypto += s;
        }
        // Записываем расшифровку в файл
        WriteFile.writeFile("decipher_text.txt");

        // Выводим исправленный массив из 500 первых символов для проверки пользователем правильности расшифровки
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tВот примерно так получается:");
        for (int i = 0; i < 500; i++) {
            System.out.print(mas_cryptoLine[i]);
        }
        System.out.println("...");

        System.out.println("\n\n\tЕсли хочешь поменять стиль дешифровки, нажми \"y\"");
        System.out.println("\tЕсли хочешь поменять символы местами в этой дешифровке, нажми \"r\"");
        String y_or_r = vvod.next();
        if (y_or_r.equals("y")) {
            Speech_style_for_cryptoAnalysis.speech_style_for_cryptoAnalysis(cryptoLine);
        } else if (y_or_r.equals("r")) {
            Swap_symbol.swap_symbol(cryptoLine);
        }
    }
}
