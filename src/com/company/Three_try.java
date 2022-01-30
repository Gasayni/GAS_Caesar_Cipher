package com.company;

import java.util.Scanner;

public class Three_try {
    public static boolean three_try() {
        // Если пользователь ввел 3 раза подряд неправильное число, то программа закрывается
        Scanner vv = new Scanner(System.in);
        if (Main.shift < -40 || Main.shift > 40) {
            for (int i = 0; i < 2; i++) {
                System.out.println("Шифр только от -40 до 40");
                Main.shift = vv.nextInt();
                if (Main.shift > -40 && Main.shift < 40) {
                    break;
                }
            }
        }
        if (Main.shift < -40 || Main.shift > 40) {
            System.out.println("Извини, ты, видимо, слишком умен для этой программы. " +
                    "Попробуй пожалуйста снова.");
            return false;
        } return true;
    }
}
