package com.company;

/*
  Программа, которая работает с шифром Цезаря

За основу криптографического алфавита возмем все буквы русского алфавита и знаки
пунктуации (. , ”” : - ! ? ПРОБЕЛ). Если попадутся символы,
которые не входят в мой криптографический алфавит, я не просто, но пропускаю их.

У программы 2 режима:
    У пользователя программы есть возможность выбрать один из двух методов криптоанализа
1. Шифрование / расшифровка. Программа зашифровывает и расшифровывать текст, используя заданный криптографический ключ.
Программа получает путь к текстовому файлу с исходным текстом и на его основе создает файл с зашифрованным текстом.
    Если пользователь выбирает brute force, программа должна самостоятельно, путем перебора, подобрать ключ и расшифровать текст
2. Криптоанализ. Программа взламывает зашифрованный текст, переданный в виде текстового файла.
    Если пользователь выбирает метод статистического анализа, ему нужно предложить загрузить еще один дополнительный файл с текстом,
желательно — того же автора и той же стилистики.
    На основе загруженного файла программа должна составить статистику вхождения символов
и после этого попытаться использовать полученную статистику для криптоанализа зашифрованного текста.
*/

// BrutForse
// Другой способ проверки выборки: Длина слова не больше 25 (после пробела)
// Другой способ проверки выборки: вероятность гласных и согласных под ряд
// Другой способ проверки выборки: Если сомневаемся спрашиваем у пользователя читабельно ли

// Аналитическая хуйня
// ключ не искать, каждый символ нужно отгадывать с вероятностью
// считаем, что есть пару абзацев, короче нормальный текст
// соединяем 2 отсортированных мапов, в результате слияния получим мапу а-п, в-о, ...

// Общая инфа занятия с 24.01.2022
// Спринг нужен для 95% работы

import java.io.*;
import java.util.*;

public class Main {
    static String s_Crypto;
    static int shift;

    public static void main(String[] args) {
        // write your code here
        //        Теперь напишем программу, которая зашифрует его методом шифра Цезаря на выбранное нами смещение
        Scanner vv = new Scanner(System.in);
        System.out.println("Выберите смещение символов по Шифру Цезаря. Знак минус означает смещение влево");
        shift = vv.nextInt();


//        Первым делом загрузим файл с обычным текстом
        String s = readFile("original_text.txt");
//        Зашифруем текст файла
        s_Crypto = toCrypto_noCase(s);
//        Запишем файл с готовым зашифрованным текстом
        writeFile("cipher_text.txt");
//        Ну вот, у нас есть зашифрованный текст


        String cryptoLine = readFile("cipher_text.txt");
//        Теперь нам нужно расшифровать его двумя способами
//        1. brute force
        find_shift_brutForse(cryptoLine);
        System.out.println();

//        2. Криптоанализ на основе статистических данных
//        find_shift_cryptoAnalysis(cryptoLine);
    }


    public static void find_shift_cryptoAnalysis(String cryptoLine) {
//        Каждой букве нужно присвоить номер, для этого скорее всего лучше подойдет ArrayMap
        HashMap<String, Integer> letters = new HashMap<>();
        letters.put("а", 0);
        letters.put("б", 0);
        letters.put("в", 0);
        letters.put("г", 0);
        letters.put("д", 0);
        letters.put("е", 0);
        letters.put("ё", 0);
        letters.put("ж", 0);
        letters.put("з", 0);
        letters.put("и", 0);
        letters.put("й", 0);
        letters.put("к", 0);
        letters.put("л", 0);
        letters.put("м", 0);
        letters.put("н", 0);
        letters.put("о", 0);
        letters.put("п", 0);
        letters.put("р", 0);
        letters.put("с", 0);
        letters.put("т", 0);
        letters.put("у", 0);
        letters.put("ф", 0);
        letters.put("х", 0);
        letters.put("ц", 0);
        letters.put("ч", 0);
        letters.put("ш", 0);
        letters.put("щ", 0);
        letters.put("ь", 0);
        letters.put("ы", 0);
        letters.put("ъ", 0);
        letters.put("э", 0);
        letters.put("ю", 0);
        letters.put("я", 0);
        letters.put(".", 0);
        letters.put(",", 0);
        letters.put("\"", 0);
        letters.put(":", 0);
        letters.put("-", 0);
        letters.put("!", 0);
        letters.put("?", 0);
        letters.put(" ", 0);

//          Также создаем ArrayList для сравнения символов
        ArrayList<String> alphabet = new ArrayList<>();
        alphabet.add("а");
        alphabet.add("б");
        alphabet.add("в");
        alphabet.add("г");
        alphabet.add("д");
        alphabet.add("е");
        alphabet.add("ё");
        alphabet.add("ж");
        alphabet.add("з");
        alphabet.add("и");
        alphabet.add("й");
        alphabet.add("к");
        alphabet.add("л");
        alphabet.add("м");
        alphabet.add("н");
        alphabet.add("о");
        alphabet.add("п");
        alphabet.add("р");
        alphabet.add("с");
        alphabet.add("т");
        alphabet.add("у");
        alphabet.add("ф");
        alphabet.add("х");
        alphabet.add("ц");
        alphabet.add("ч");
        alphabet.add("ш");
        alphabet.add("щ");
        alphabet.add("ь");
        alphabet.add("ы");
        alphabet.add("ъ");
        alphabet.add("э");
        alphabet.add("ю");
        alphabet.add("я");
        alphabet.add(".");
        alphabet.add(",");
        alphabet.add("\"");
        alphabet.add(":");
        alphabet.add("-");
        alphabet.add("!");
        alphabet.add("?");
        alphabet.add(" ");


        //        Определяем статискику вхождения символов
//            Средняя частота вхождения " " в нашем тексте 15%
//            Средняя частота вхождения "о" в тексте 10,97%
//            Средняя частота вхождения "е" в тексте 8,45%
//            Средняя частота вхождения "а" в тексте 8,01%
//            Средняя частота вхождения "и" в тексте 7,35%
//            Средняя частота вхождения "н" в тексте 6,7%
//            Средняя частота вхождения "т" в тексте 6,26%
//            Средняя частота вхождения "с" в тексте 5,47%
//            Средняя частота вхождения "р" в тексте 4,73%
//            Средняя частота вхождения "в" в тексте 4,54%
//            Средняя частота вхождения "л" в тексте 4,4%
//            Средняя частота вхождения "к" в тексте 3,49%


        // Способ 1: находим самый частый символ в зашифрованном тексте и понимаем, что это " "
//        Разрезаем наш текст по символам
        String[] s_mas = cryptoLine.split("");
        //        склько раз встретился тот или иной символ в тексте
        for (int i = 0; i < alphabet.size(); i++) {
            for (int j = 0; j < s_mas.length; j++) {
                if (alphabet.get(i).equals(s_mas[j])) {
                    letters.put(alphabet.get(i), (letters.get(alphabet.get(i)) + 1));
                }
            }
        }
//        letters.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue().reversed()).
//                forEach(System.out::println); // или любой другой конечный метод

        String q = letters.entrySet().stream().max(Map.Entry.<String, Integer>comparingByValue()).toString();

        String[] qw = q.split("\\[");
        String[] wq = qw[1].split("\\]");
        String[] wqw = wq[0].split("=");

//        System.out.println(wqw[0] + " " + wqw[1]);

//        Теперь мы знаем какой символ встретился чаще всего,
//        осталось только найти его положение и посчитать смещение относительно " "
        int k = 0;
        for (int i = 0; i < alphabet.size(); i++) {
            if (alphabet.get(i).equals(wqw[0])) {
                k = i;
                break;
            }
        }
//        Мы знаем, что " " находится под индексом 41 (последним) в alphabet
//        Мы узнали под каким индексом находится наш наиболее часто встречающийся символ
//        Т.о. мы можем узнать сдвиг шифра

        if (k > alphabet.size() / 2) { // Проверяем сдвиг вправо или влево
            shift = -1 * (alphabet.size() - k - 1);
        } else shift = k + 1;                 // +1, так как в массивах счет начинается с 0
//        System.out.println("shift = " + shift);

//        Теперь обратно нужно сдвинуть на место, для этого меняем знак у shift
        shift = -1 * shift;

        s_Crypto = toCrypto_noCase(cryptoLine);  // Сдвинули каждый символ на найденный "shift"
        writeFile("decipher_text.txt");
    }

    public static void find_shift_brutForse(String cryptoLine) {
//        Нужно посчитать вероятность ". "     ", "      " и "    " или "
//        Чтобы ускорить выборку сдвига, сначала сделаем перебор среди наиболее популярных чисел
        for (shift = -5; shift < 6; shift++) {
            s_Crypto = toCrypto_noCase(cryptoLine);  // Сдвинули каждый символ на "shift"
            String[] mas_Crypto = s_Crypto.split("");
//            Считаем отношение "." к ". "
            float amount_dot = 0;
            float amount_dot_space = 0;
            float amount_comma = 0;
            float amount_comma_space = 0;

            for (int i = 0; i < mas_Crypto.length; i++) {
                if (mas_Crypto[i].equals(".")) {
                    amount_dot++;
                    if (mas_Crypto[i+1].equals(" ")) {
                        amount_dot_space++;
                    }
                }
                if (mas_Crypto[i].equals(",")) {
                    amount_comma++;
                    if (mas_Crypto[i+1].equals(" ")) {
                        amount_comma_space++;
                    }
                }
            }
            /*System.out.println("Сдвиг: " + shift);
            System.out.println("Зашифрованный текст:");
            System.out.println(s_Crypto);
            System.out.println("amount_dot = " + amount_dot);
            System.out.println("amount_dot_space = " + amount_dot_space);
            System.out.println("amount_comma = " + amount_comma);
            System.out.println("amount_comma_space = " + amount_comma_space);
            System.out.println();*/  // Проверка


            if (amount_dot != 0 || amount_dot_space != 0 || amount_comma != 0 || amount_comma_space != 0) {
                if (amount_dot / amount_dot_space < 1.2 || amount_comma / amount_comma_space < 1.2) {
                    System.out.println("Сдвиг шифра был = " + -1 * shift);
                    writeFile("decipher_text.txt");
                    return;
                }
            }
        }

//        Если сдвиг не популярный, то проверяем остальные сдвиги
        for (shift = -40; shift < -5; shift++) {
            s_Crypto = toCrypto_noCase(cryptoLine);  // Сдвинули каждый символ на "shift"
            String[] mas_Crypto = s_Crypto.split("");
//            Считаем отношение "." к ". "
            float amount_dot = 0;
            float amount_dot_space = 0;
            float amount_comma = 0;
            float amount_comma_space = 0;

            for (int i = 0; i < mas_Crypto.length; i++) {
                if (mas_Crypto[i].equals(".")) {
                    amount_dot++;
                    if (mas_Crypto[i+1].equals(" ")) {
                        amount_dot_space++;
                    }
                }
                if (mas_Crypto[i].equals(",")) {
                    amount_comma++;
                    if (mas_Crypto[i+1].equals(" ")) {
                        amount_comma_space++;
                    }
                }
            }
            if (amount_dot != 0 || amount_dot_space != 0 || amount_comma != 0 || amount_comma_space != 0) {
                if (amount_dot / amount_dot_space < 1.2 || amount_comma / amount_comma_space < 1.2) {
                    System.out.println("Сдвиг шифра был = " + -1 * shift + " или " + -1 *(41+shift));
                    System.out.println("В данной программе это не важно, так как текст все равно правильно дешифруется");
                    writeFile("decipher_text.txt");
                    return;
                }
            }
        }

        // До этого условия все равно не доходит
        /*for (shift = -6; shift < 41; shift++) {
            s_Crypto = toCrypto_noCase(cryptoLine);  // Сдвинули каждый символ на "shift"
            String[] mas_Crypto = s_Crypto.split("");
//            Считаем отношение "." к ". "
            float amount_dot = 0;
            float amount_dot_space = 0;
            float amount_comma = 0;
            float amount_comma_space = 0;

            for (int i = 0; i < mas_Crypto.length; i++) {
                if (mas_Crypto[i].equals(".")) {
                    amount_dot++;
                    if (mas_Crypto[i+1].equals(" ")) {
                        amount_dot_space++;
                    }
                }
                if (mas_Crypto[i].equals(",")) {
                    amount_comma++;
                    if (mas_Crypto[i+1].equals(" ")) {
                        amount_comma_space++;
                    }
                }
            }
            if (amount_dot != 0 || amount_dot_space != 0 || amount_comma != 0 || amount_comma_space != 0) {
                if (amount_dot / amount_dot_space < 1.2 || amount_comma / amount_comma_space < 1.2) {
                    System.out.print("Сдвиг шифра был = " + -1 * shift);
                    writeFile("decipher_text.txt");
                    return;
                }
            }
        }*/  // До этого условия все равно не доходит
//        Ну если совсем не получается определить, то выводим сообщение
        System.out.println("Не могу определить сдвиг, попробуйте файл с большим текстом внутри");
/*//        Чтобы ускорить выборку сдвига, сначала сделаем перебор среди наиболее популярных чисел
        for (shift = -5; shift < 6; shift++) {
            s_Crypto = toCrypto_noCase(cryptoLine);  // Сдвинули каждый символ на "shift"
            if (s_Crypto.contains(". ") && s_Crypto.contains(", ") && s_Crypto.contains(" и ") &&
                    (s_Crypto.contains(" или ")
                            || s_Crypto.contains(" не ")
                            || s_Crypto.contains(" в ")
                            || s_Crypto.contains(" к ")
                            || s_Crypto.contains(" с ")
                            || s_Crypto.contains(" - ")
                            || s_Crypto.contains(" на ")
                            || s_Crypto.contains(" они "))) {

//                System.out.println();
//                System.out.println(s_Crypto);
//                System.out.println("\n\n\n");
                System.out.println("Сдвиг шифра был = " + -1 * shift);
                writeFile("decipher_text.txt");
                return;
            }
        }
//        Если сдвиг не популярный, то проверяем остальные сдвиги (в пределах разумного) сначала от -40 до -4
        for (shift = -40; shift < -5; shift++) {
            s_Crypto = toCrypto_noCase(cryptoLine);  // Сдвинули каждый символ на "shift"
            if (s_Crypto.contains(". ") && s_Crypto.contains(", ") && s_Crypto.contains(" и ") &&
                    (s_Crypto.contains(" или ")
                            || s_Crypto.contains(" не ")
                            || s_Crypto.contains(" в ")
                            || s_Crypto.contains(" к ")
                            || s_Crypto.contains(" с ")
                            || s_Crypto.contains(" - ")
                            || s_Crypto.contains(" на ")
                            || s_Crypto.contains(" они "))) {
//                System.out.println(s_Crypto);
//                System.out.println("\n\n\n");
                System.out.println("Сдвиг шифра был = " + -1 * shift);

                writeFile("decipher_text.txt");
                return;
            }
        }
//        Если сдвиг не популярный, то проверяем остальные сдвиги (в пределах разумного) потом от 6 до 40
        for (shift = 6; shift < 41; shift++) {
            s_Crypto = toCrypto_noCase(cryptoLine);  // Сдвинули каждый символ на "shift"
            if (s_Crypto.contains(". ") && s_Crypto.contains(", ") && s_Crypto.contains(" и ") &&
                    (s_Crypto.contains(" или ")
                            || s_Crypto.contains(" не ")
                            || s_Crypto.contains(" в ")
                            || s_Crypto.contains(" к ")
                            || s_Crypto.contains(" с ")
                            || s_Crypto.contains(" - ")
                            || s_Crypto.contains(" на ")
                            || s_Crypto.contains(" они "))) {
//                System.out.println(s_Crypto);
//                System.out.println("\n\n\n");
                System.out.println("Сдвиг шифра был = " + -1 * shift);

                writeFile("decipher_text.txt");
                return;
            }
        }*/
    }




    public static String toCrypto_noCase(String s) {
        //        У нас (по заданию) есть русский алфавит и знаки пунктуации (. , ”” : - ! ? ПРОБЕЛ).
//        !!! Пока на регистр не будем обращать внимание. Поэтому нужно все буквы перевести в нижний регистр
        String s_Lower = s.toLowerCase();

//        Каждой букве нужно присвоить номер, для этого скорее всего лучше подойдет Array list
        ArrayList<String> alphabet = new ArrayList<>();
        alphabet.add("а");
        alphabet.add("б");
        alphabet.add("в");
        alphabet.add("г");
        alphabet.add("д");
        alphabet.add("е");
        alphabet.add("ё");
        alphabet.add("ж");
        alphabet.add("з");
        alphabet.add("и");
        alphabet.add("й");
        alphabet.add("к");
        alphabet.add("л");
        alphabet.add("м");
        alphabet.add("н");
        alphabet.add("о");
        alphabet.add("п");
        alphabet.add("р");
        alphabet.add("с");
        alphabet.add("т");
        alphabet.add("у");
        alphabet.add("ф");
        alphabet.add("х");
        alphabet.add("ц");
        alphabet.add("ч");
        alphabet.add("ш");
        alphabet.add("щ");
        alphabet.add("ь");
        alphabet.add("ы");
        alphabet.add("ъ");
        alphabet.add("э");
        alphabet.add("ю");
        alphabet.add("я");
        alphabet.add(".");
        alphabet.add(",");
        alphabet.add("\"");
        alphabet.add(":");
        alphabet.add("-");
        alphabet.add("!");
        alphabet.add("?");
        alphabet.add(" ");


//        Разрезаем наш текст по символам
        String[] s_mas = s_Lower.split("");
        String finish_line = "";
//        Берем каждый символ нашего текста и проверяем его по списку, затем сдвигаем
        for (int i = 0; i < s_mas.length; i++) {
            for (int j = 0; j < alphabet.size(); j++) {
                if (s_mas[i].equals(alphabet.get(j))) {
                    try {
                        s_mas[i] = alphabet.get(j + shift);
                    } catch (IndexOutOfBoundsException e) {
                        if ((j + shift) < 0) {
                            s_mas[i] = alphabet.get(j + shift + alphabet.size());
                        } else s_mas[i] = alphabet.get(j + shift - alphabet.size());
                    }
                    break;
                }
            }
//             И сразу записываем символ в финальную строку
            finish_line = finish_line + s_mas[i];
        }
        return finish_line;
    }

    public static String readFile(String path) {
        String s = "";
        String line = "";
        try {
            FileReader file = new FileReader(path);
            BufferedReader br = new BufferedReader(file);
            while ((line = br.readLine()) != null) {
                s += line + "\n";    // Записываем все строки в "s"
            }
            br.close();
            file.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//        System.out.println(s);
//        System.out.println("\n\n\n\n");
//        System.out.println(s.toLowerCase());

        return s;
    }

    public static void writeFile(String path) {
        //        Записываем строку в файл
        try (
                FileWriter writer = new FileWriter(path, false)) {
            // запись всей строки
            writer.write(s_Crypto);

            // запись по символам
//            writer.append('\n');
//            writer.append('E');
            writer.flush();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }


    /*String alphabet = "абвгдежзийклмнопрстуфхцчшщъыьэюя., ";
    char[] chars = alphabet.toCharArray();
    String str = "привет, как дела. ты приехал.";
    //        String str =   "еж,ыюичшащашэюбщцшисшеж,юлщбц";
    char[] strChars = str.toCharArray();
    char[] result = new char[strChars.length];

    int keyA = 25;
    int keyB = chars.length - keyA;
        for (int i = 0; i < strChars.length; i++) {
        char strChar = strChars[i];
        for (int j = 0; j < chars.length; j++) {
            char ch = chars[j];
            if (strChar == ch) {
                result[i] = chars[(j + keyA) % chars.length];
            }
        }
    }

        System.out.println(new String(result));*/

}