package com.company;

import java.util.Scanner;

public class Speech_style_for_cryptoAnalysis {
    public static void speech_style_for_cryptoAnalysis(String cryptoLine) {
        Scanner vvod = new Scanner(System.in);
        CryptoAnalysis cryptoAnalysis = new CryptoAnalysis();

        //  хорошо бы добавить для разных стилей речи
        String symbol_frequency_science = " оеитнасрвлкмдпуыячз,ьйхбг.жющцфэшё-ъ:!\"?";
        String symbol_frequency_journalistic = " оеаинтсрвлкмдпыуязьгб,чй.жхцшюэфщ-:?!ъ\"ё";
        String symbol_frequency_official = " оеианстрвлкмпдуяыгб,йьзч.жхцюшщфэ-ъ:ё!\"?";
        String symbol_frequency_artistic = " оаеинтслрвкмпудя,ыьг.бзйхчшжюцщ-!эф:?ёъ\"";
        String symbol_frequency_conversational = " оаетинсвлркмдуп,ябьы.згчйхшжю-!щц?эф:ёъ\"";
        String symbol_frequency_martian = " оеатнисрлвмдпкуяь.зы,бчгйжхюшэцщф-?:!ъё\"";


        System.out.println("Выбери другие типы дешифровки по стилям речи:");
        System.out.println("0 - Простой стиль");
        System.out.println("1 - Научный стиль");
        System.out.println("2 - Публицистический стиль");
        System.out.println("3 - Официально-деловой стиль");
        System.out.println("4 - Художественный стиль");
        System.out.println("5 - Разговорный стиль");
        System.out.println("6 - Стиль Энди Вейера - \"Марсианин\"");
        System.out.println("7 - Свой стиль");

        switch (vvod.next()) {
            case "0" -> {
                CryptoAnalysis.mas_symbol_frequency = CryptoAnalysis.symbol_frequency_easy.split("");
                cryptoAnalysis.cryptoAnalysis(cryptoLine);
            }
            case "1" -> {
                CryptoAnalysis.mas_symbol_frequency = symbol_frequency_science.split("");
                cryptoAnalysis.cryptoAnalysis(cryptoLine);
            }
            case "2" -> {
                CryptoAnalysis.mas_symbol_frequency = symbol_frequency_journalistic.split("");
                cryptoAnalysis.cryptoAnalysis(cryptoLine);
            }
            case "3" -> {
                CryptoAnalysis.mas_symbol_frequency = symbol_frequency_official.split("");
                cryptoAnalysis.cryptoAnalysis(cryptoLine);
            }
            case "4" -> {
                CryptoAnalysis.mas_symbol_frequency = symbol_frequency_artistic.split("");
                cryptoAnalysis.cryptoAnalysis(cryptoLine);
            }
            case "5" -> {
                CryptoAnalysis.mas_symbol_frequency = symbol_frequency_conversational.split("");
                cryptoAnalysis.cryptoAnalysis(cryptoLine);
            }
            case "6" -> {
                CryptoAnalysis.mas_symbol_frequency = symbol_frequency_martian.split("");
                cryptoAnalysis.cryptoAnalysis(cryptoLine);
            }
            case "7" -> {
                CryptoAnalysis.mas_symbol_frequency = User_speech_style.user_speech_style().split("");
                cryptoAnalysis.cryptoAnalysis(cryptoLine);
            }
            default -> System.out.println("\n Спасибо за внимание. Ты лучший!");
        }
    }
}
