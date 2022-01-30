package com.company;

public class BrutForse {
    public static void brutForse(String cryptoLine) {
//        Нужно посчитать вероятность ". " и ", "
//        Чтобы ускорить выборку сдвига, сначала сделаем перебор среди наиболее популярных чисел
        for (Main.shift = -10; Main.shift < 11; Main.shift++) {
            Main.s_Crypto = ToCrypto_noCase.toCrypto_noCase(cryptoLine);  // Сдвинули каждый символ на "shift"
            String[] mas_Crypto = Main.s_Crypto.split("");
//            Считаем отношение "." к ". "
            float amount_dot = 0;
            float amount_dot_space = 0;
            float amount_comma = 0;
            float amount_comma_space = 0;

            for (int i = 0; i < mas_Crypto.length; i++) {
                if (mas_Crypto[i].equals(".")) {
                    amount_dot++;
                    if (mas_Crypto[i + 1].equals(" ")) {
                        amount_dot_space++;
                    }
                }
                if (mas_Crypto[i].equals(",")) {
                    amount_comma++;
                    if (mas_Crypto[i + 1].equals(" ")) {
                        amount_comma_space++;
                    }
                }
            }

            if (amount_dot != 0 || amount_dot_space != 0 || amount_comma != 0 || amount_comma_space != 0) {
                if (amount_dot / amount_dot_space < 1.2 || amount_comma / amount_comma_space < 1.2) {
                    System.out.println("Сдвиг шифра был = " + -1 * Main.shift);
                    WriteFile.writeFile("decipher_text.txt");
                    return;
                }
            }
        }

//        Если сдвиг не популярный, то проверяем остальные сдвиги
//        (от -40 до -10 хватит потому что у нас круговой массив)
        for (Main.shift = -40; Main.shift < -10; Main.shift++) {
            Main.s_Crypto = ToCrypto_noCase.toCrypto_noCase(cryptoLine);  // Сдвинули каждый символ на "Main.shift"
            String[] mas_Crypto = Main.s_Crypto.split("");
//            Считаем отношение "." к ". "
            float amount_dot = 0;
            float amount_dot_space = 0;
            float amount_comma = 0;
            float amount_comma_space = 0;

            for (int i = 0; i < mas_Crypto.length; i++) {
                if (mas_Crypto[i].equals(".")) {
                    amount_dot++;
                    if (mas_Crypto[i + 1].equals(" ")) {
                        amount_dot_space++;
                    }
                }
                if (mas_Crypto[i].equals(",")) {
                    amount_comma++;
                    if (mas_Crypto[i + 1].equals(" ")) {
                        amount_comma_space++;
                    }
                }
            }
            if (amount_dot != 0 || amount_dot_space != 0 || amount_comma != 0 || amount_comma_space != 0) {
                if (amount_dot / amount_dot_space < 1.2 || amount_comma / amount_comma_space < 1.2) {
                    System.out.println("Сдвиг шифра был = " + -1 * Main.shift + " или " + -1 * (41 + Main.shift));
                    System.out.println("В данной программе он круговой, текст все равно правильно дешифруется");
                    WriteFile.writeFile("decipher_text.txt");
                    return;
                }
            }
        }
//        Ну если совсем не получается определить, то выводим сообщение
        System.out.println("Не могу определить сдвиг, попробуй файл с большим текстом внутри");
    }
}
