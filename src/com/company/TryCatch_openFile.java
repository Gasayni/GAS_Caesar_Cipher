package com.company;

import java.util.Scanner;

public class TryCatch_openFile {
    static String s_user_file_path = "";
    public static void tryCatch_openFile() {
        Scanner vv = new Scanner(System.in);

        s_user_file_path = vv.next();
        try {
            Main.s_original = ReadFile.readFile(s_user_file_path);
        } catch (Exception e) {
            System.out.println("Что-то пошло не так, скорее всего путь указан не верно.");
            System.out.println("Попробуй снова");
            System.out.println("Введи путь до твоего текстового файла");
            System.out.println("Вот пример: C:\\Users\\User\\Desktop\\name.txt");
            s_user_file_path = vv.next();
            try {
                Main.s_original = ReadFile.readFile(s_user_file_path);
            } catch (Exception ee) {
                System.out.print("Что-то снова пошло не так, скорее всего путь введен не верно. ");
                System.out.println("Тогда открою свой файл");
                Main.s_original = ReadFile.readFile("original_text.txt");
            }
        }
    }
}
