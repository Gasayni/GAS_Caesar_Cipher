package com.company;

import java.util.Scanner;

public class TryCatch_openFile2 {
    static String s_user_file_path = "";

    public static String tryCatch_openFile2() {
        Scanner vv = new Scanner(System.in);

        s_user_file_path = vv.next();
        try {
            return ReadFile.readFile(s_user_file_path);
        } catch (Exception e) {
            System.out.println("Что-то пошло не так, скорее всего путь указан не верно.");
            System.out.println("Попробуй снова");
            System.out.println("Введи путь до твоего текстового файла");
            System.out.println("Вот пример: C:\\Users\\User\\Desktop\\name.txt");
            s_user_file_path = vv.next();
            try {
                return ReadFile.readFile(s_user_file_path);
            } catch (Exception ee) {
                System.out.print("Что-то снова пошло не так, скорее всего путь введен не верно. ");
                System.out.println("Тогда открою свой файл");
                return ReadFile.readFile("original_text.txt");
            }
        }
    }
}
