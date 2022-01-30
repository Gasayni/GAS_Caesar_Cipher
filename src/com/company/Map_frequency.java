package com.company;

import java.util.HashMap;

public class Map_frequency {
    public static HashMap<String, Integer> map_frequency(String[] mas_cryptoLine) {
        HashMap<String, Integer> map_letters = new HashMap<>();
        //        Каждой букве нужно присвоить номер, для этого скорее всего лучше подойдет ArrayMap
        map_letters.put("а", 0);
        map_letters.put("б", 0);
        map_letters.put("в", 0);
        map_letters.put("г", 0);
        map_letters.put("д", 0);
        map_letters.put("е", 0);
        map_letters.put("ё", 0);
        map_letters.put("ж", 0);
        map_letters.put("з", 0);
        map_letters.put("и", 0);
        map_letters.put("й", 0);
        map_letters.put("к", 0);
        map_letters.put("л", 0);
        map_letters.put("м", 0);
        map_letters.put("н", 0);
        map_letters.put("о", 0);
        map_letters.put("п", 0);
        map_letters.put("р", 0);
        map_letters.put("с", 0);
        map_letters.put("т", 0);
        map_letters.put("у", 0);
        map_letters.put("ф", 0);
        map_letters.put("х", 0);
        map_letters.put("ц", 0);
        map_letters.put("ч", 0);
        map_letters.put("ш", 0);
        map_letters.put("щ", 0);
        map_letters.put("ь", 0);
        map_letters.put("ы", 0);
        map_letters.put("ъ", 0);
        map_letters.put("э", 0);
        map_letters.put("ю", 0);
        map_letters.put("я", 0);
        map_letters.put(".", 0);
        map_letters.put(",", 0);
        map_letters.put("\"", 0);
        map_letters.put(":", 0);
        map_letters.put("-", 0);
        map_letters.put("!", 0);
        map_letters.put("?", 0);
        map_letters.put(" ", 0);

        // нужно отсортировать полученный Мап по убыванию частоты вхождения, (чтобы первый элементы был максимальным по значению)

        //  записываем в Мап сколько раз встретился тот или иной символ в тексте
        for (String s : CryptoAnalysis.aAlphabet) {
            for (String value : mas_cryptoLine) {
                if (s.equals(value)) {
                    map_letters.put(s, (map_letters.get(s) + 1));
                }
            }
        }
        return map_letters;
    }
}
