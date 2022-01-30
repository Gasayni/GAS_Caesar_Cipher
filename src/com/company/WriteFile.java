package com.company;

import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    public static void writeFile(String path) {
        //        Записываем строку в файл

        try (
                FileWriter writer = new FileWriter(path, false)) {
            // запись всей строки
            new FileWriter(path, false).close();
            writer.write(Main.s_Crypto);

            // запись по символам
//            writer.append('\n');
//            writer.append('E');
            writer.flush();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
