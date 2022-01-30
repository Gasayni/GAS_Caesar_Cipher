package com.company;

import java.util.Scanner;

public class Three_try2 {
    public static void three_try2(String cryptoLine) {
        CryptoAnalysis cryptoAnalysis = new CryptoAnalysis();

        // Если пользователь ввел 3 раза подряд неправильное число, то программа закрывается
        Scanner vv = new Scanner(System.in);
        String one_or_two = "";
        for (int i = 0; i < 2; i++) {
            System.out.println("Что-то снова пошло не так, скорее всего выбор не корректен.");
            System.out.println("Попробуй снова");
            System.out.println("Каким способом ты хочешь расшифровать текст?");
            System.out.println("Если Brute force (поиск грубой силой) - набери \"1\"");
            System.out.println("Если Криптоанализ на основе статистических данных - набери \"2\"");
            one_or_two = vv.next();
            if (one_or_two.equals("1")) {
                BrutForse.brutForse(cryptoLine);
                break;
            } else if (one_or_two.equals("2")) {
                cryptoAnalysis.cryptoAnalysis(cryptoLine);
                break;
            }
        }
        if (one_or_two.equals("1")) {
            System.out.println("Выбран метод Brute force (поиск грубой силой)");
            BrutForse.brutForse(cryptoLine);
        } else if (one_or_two.equals("2")) {
            System.out.println("Выбран метод Криптоанализ на основе статистических данных");
            cryptoAnalysis.cryptoAnalysis(cryptoLine);
        } else {
            System.out.println("Извини, ты, видимо, слишком умен для этой программы. " +
                    "Попробуй пожалуйста снова.");
        }
    }
}
