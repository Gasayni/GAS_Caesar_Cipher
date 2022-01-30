package com.company;

public class ToCrypto_noCase {
    public static String toCrypto_noCase(String s) {
//        char_char();          // другой метод - шифратор, предложенный менторами


        //        У нас (по заданию) есть русский алфавит и знаки пунктуации (. , ”” : - ! ? ПРОБЕЛ).
//        !!! Пока на регистр не будем обращать внимание. Поэтому нужно все буквы перевести в нижний регистр
        String s_Lower = s.toLowerCase();

//        Разрезаем наш текст по символам
        String[] s_mas = s_Lower.split("");


        StringBuilder finish_line = new StringBuilder();
//        Берем каждый символ нашего текста и проверяем его по списку, затем сдвигаем
        for (int i = 0; i < s_mas.length; i++) {
            for (int j = 0; j < CryptoAnalysis.aAlphabet.size(); j++) {
                if (s_mas[i].equals(CryptoAnalysis.aAlphabet.get(j))) {
                    try {
                        s_mas[i] = CryptoAnalysis.aAlphabet.get(j + Main.shift);
                    } catch (IndexOutOfBoundsException e) {
                        if ((j + Main.shift) < 0) {
                            s_mas[i] = CryptoAnalysis.aAlphabet.get(j + Main.shift + CryptoAnalysis.aAlphabet.size());
                        } else s_mas[i] = CryptoAnalysis.aAlphabet.get(j + Main.shift - CryptoAnalysis.aAlphabet.size());
                    }
                    break;
                }
            }
//             И сразу записываем символ в финальную строку
            finish_line.append(s_mas[i]);
        }
        return finish_line.toString();
    }
}
