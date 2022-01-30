package com.company;

import java.util.Scanner;

public class Swap_symbol {
    public static void swap_symbol(String cryptoLine) {
        Scanner vvod = new Scanner(System.in);

        System.out.println("Введи заменяемый символ");
        String swap_symbol_1 = vvod.next();
        System.out.println("Введи, каким символом заменить");
        String swap_symbol_2 = vvod.next();

        for (int i = 0; i < CryptoAnalysis.mas_symbol_frequency.length; i++) {
            if (CryptoAnalysis.mas_symbol_frequency[i].equals(swap_symbol_1)) {
                CryptoAnalysis.mas_symbol_frequency[i] = swap_symbol_2;
            } else if (CryptoAnalysis.mas_symbol_frequency[i].equals(swap_symbol_2)) {
                CryptoAnalysis.mas_symbol_frequency[i] = swap_symbol_1;
            }
        }
        CryptoAnalysis cryptoAnalysis = new CryptoAnalysis();
        cryptoAnalysis.cryptoAnalysis(cryptoLine);
    }
}
