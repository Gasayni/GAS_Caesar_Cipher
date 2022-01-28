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
//      Спринг нужен для 95% работы

import java.io.*;
import java.util.*;

public class Main {
    static String s_Crypto = "";
    static int shift;
    static String s_original = "";
    static boolean b = true;
    static String s_user_file_path = "";

    //  создадим список из букв и символов для дальнейшей работы
    static String s_alphabet = "абвгдеёжзийклмнопрстуфхцчшщьыъэюя.,\":-!? ";
    static ArrayList<String> aAlphabet = new ArrayList<>(Arrays.asList(s_alphabet.split("")));
    static String symbol_frequency_easy = " оетасинмвлдркяып,бьйуч.жгзхюшщёэцф-?ъ!\":";
    static String[] mas_symbol_frequency = symbol_frequency_easy.split("");
    static HashMap<String, Integer> map_letters = new HashMap<>();

    public static void main(String[] args) {
        Scanner vv = new Scanner(System.in);

        System.out.print("Эта программа очень крутая на самом деле, ");
        System.out.println("тут можно не только расшифровать текст, но и зашифровать.");
        System.out.print("\t Если хочешь зашифровать текст нажми 'y', ");
        System.out.println("продолжить по умолчанию - любой другой символ");
        // Проверка на запуск метода шифрования
        if (vv.next().equals("y")) {
            System.out.println("Выбери смещение символов по Шифру Цезаря. " +
                    "Знак минус означает смещение влево");
            System.out.println("\t\tТолько выбирай сердцем (от -40 до 40)");
            shift = vv.nextInt();
            // Если пользователь ввел 3 раза подряд неправильное число, то программа закроется
            if (three_try()) {
                System.out.println("Если хочешь зашифровать свой собственный текст нажмите 'y'");
                // Проверка на желание пользователя загрузить собственный текст
                if (vv.next().equals("y")) {
                    System.out.println("\t Введи путь до твоего текстового файла");
                    System.out.println("\t\t Вот пример: C:\\Users\\User\\Desktop\\name.txt");
                    // дается 2 попытки ввести правильно путь,
                    // если они исчерпаны, то путь выбирается по умолчанию
                    tryCatch_openFile();
                }
            }
            s_original = readFile("original_text.txt");
            //  Зашифруем текст файла
            s_Crypto = toCrypto_noCase(s_original);
            writeFile("cipher_text.txt");
        }
//      Расшифровка текста начинается тут
        String cryptoLine = readFile("cipher_text.txt");

        System.out.println("Каким способом ты хочешь расшифровать текст?");
        System.out.println("\tЕсли Brute force (поиск грубой силой) набери \"1\"");
        System.out.println("\tЕсли Криптоанализ на основе статистических данных набери \"2\"");

        String one_or_two = vv.next();
        if (one_or_two.equals("1")) {
//              1. brute force
            System.out.println("Выбран метод Brute force (поиск грубой силой)");
            brutForse(cryptoLine);
        } else if (one_or_two.equals("2")) {
//              2. Криптоанализ на основе статистических данных
            System.out.println("Выбран метод Криптоанализ на основе статистических данных");
            System.out.println("По умолчанию стоит самый простой стиль дешифровки \n");
            cryptoAnalysis(cryptoLine);
        } else {
            // Если пользователь ввел 3 раза подряд неправильное число, то программа просто закроется
            three_try2(cryptoLine);
        }
    }


    public static void brutForse(String cryptoLine) {
//        Нужно посчитать вероятность ". " и ", "
//        Чтобы ускорить выборку сдвига, сначала сделаем перебор среди наиболее популярных чисел
        for (shift = -10; shift < 11; shift++) {
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
                    System.out.println("Сдвиг шифра был = " + -1 * shift);
                    writeFile("decipher_text.txt");
                    return;
                }
            }
        }

//        Если сдвиг не популярный, то проверяем остальные сдвиги
//        (от -40 до -10 хватит потому что у нас круговой массив)
        for (shift = -40; shift < -10; shift++) {
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
                    System.out.println("Сдвиг шифра был = " + -1 * shift + " или " + -1 * (41 + shift));
                    System.out.println("В данной программе он круговой, текст все равно правильно дешифруется");
                    writeFile("decipher_text.txt");
                    return;
                }
            }
        }
//        Ну если совсем не получается определить, то выводим сообщение
        System.out.println("Не могу определить сдвиг, попробуй файл с большим текстом внутри");
    }

    public static void cryptoAnalysis(String cryptoLine) {
        Scanner vvod = new Scanner(System.in);

        // Вообще хорошо было бы, чтобы программа сама подбирала последующие символы на основе популярных слов

        // Есть предположение, что сбивается статистика потому что частота
        // вхождения символов у каких-то одинаковая из-за этого путаница
        // ПОДТВЕРДИЛОСЬ!!!    НУЖНО ИСПРАВИТЬ - либо текст побольше, либо дополнить условиями

        // хорошо если пользователю будет дана возможность поменять символы самостоятельно


        //        Каждой букве нужно присвоить номер, для этого скорее всего лучше подойдет ArrayMap
        map_letters.put("а", 0);
        map_letters.put("б", 0);
        map_letters.put("в", 0);
        map_letters.put("г", 0);
        map_letters.put("д", 0);
        map_letters.put("е", 0);
        map_letters.put("ё", 0);
        map_letters.put("ж", 0);
        map_letters.put("з", 0);
        map_letters.put("и", 0);
        map_letters.put("й", 0);
        map_letters.put("к", 0);
        map_letters.put("л", 0);
        map_letters.put("м", 0);
        map_letters.put("н", 0);
        map_letters.put("о", 0);
        map_letters.put("п", 0);
        map_letters.put("р", 0);
        map_letters.put("с", 0);
        map_letters.put("т", 0);
        map_letters.put("у", 0);
        map_letters.put("ф", 0);
        map_letters.put("х", 0);
        map_letters.put("ц", 0);
        map_letters.put("ч", 0);
        map_letters.put("ш", 0);
        map_letters.put("щ", 0);
        map_letters.put("ь", 0);
        map_letters.put("ы", 0);
        map_letters.put("ъ", 0);
        map_letters.put("э", 0);
        map_letters.put("ю", 0);
        map_letters.put("я", 0);
        map_letters.put(".", 0);
        map_letters.put(",", 0);
        map_letters.put("\"", 0);
        map_letters.put(":", 0);
        map_letters.put("-", 0);
        map_letters.put("!", 0);
        map_letters.put("?", 0);
        map_letters.put(" ", 0);

        // нужно отсортировать полученный Мап по убыванию частоты вхождения, (чтобы первый элементы был максимальным по значению)
        //  Разрезаем наш текст по символам
        String[] mas_cryptoLine = cryptoLine.split("");                         // массив символов из текста
        //  записываем в Мап сколько раз встретился тот или иной символ в тексте
        for (String s : aAlphabet) {
            for (String value : mas_cryptoLine) {
                if (s.equals(value)) {
                    map_letters.put(s, (map_letters.get(s) + 1));
                }
            }
        }

        String[] qw, wq, wqw;
        ArrayList<Integer> after_replace = new ArrayList<>();   // сюда будут заноситься символы, которые уже заменены
//        after_replace.clear();

        // Повторяем пока не заменим все символы в тексте
        for (int i = 0; i < aAlphabet.size(); i++) {
//      находим самый частый символ в Мап-е с частотой вхождения символов в тексте
            String max_symbol = map_letters.entrySet().stream().max(Map.Entry.comparingByValue()).toString();

//      Убираем шелуху от "Optional[к=167]", нам нужна только цифра
            qw = max_symbol.split("\\[");   // max_symbol = "Optional[к=167]"
            wq = qw[1].split("]");        // qw[1] = "к=167]"
            wqw = wq[0].split("=");         // wq[0] = "к=167"

            //   Нашли самый частый символ - wqw[0], wqw[1] - количество повторений
//      Теперь мы знаем какой символ встретился чаще всего,
//      Теперь нам нужно заменить каждый такой символ в тексте на самый частый по статистике
            for (int j = 0; j < mas_cryptoLine.length; j++) {
                // Если в зашифрованном массиве символов всего текста встретится символ с максимальным вхождением
                // И Нам не нужно менять то, что уже поменяно
                if (mas_cryptoLine[j].equals(wqw[0]) && !after_replace.contains(j)) {
                    // То заменяем его на символ с максимальным вхождением по статистике
                    mas_cryptoLine[j] = mas_symbol_frequency[i];
                    after_replace.add(j);   // сюда будут заноситься номера поменянных ячеек, чтобы больше их не менять
                }
            }
//        Теперь нам нужно удалить максимальную Map строку по ключу - для того чтобы потом найти следующую максимальную
            map_letters.remove(wqw[0]);
        }

        // Записываем в строку
        for (String s : mas_cryptoLine) {
            s_Crypto += s;
        }
        // Записываем расшифровку в файл
        writeFile("decipher_text.txt");

        // Выводим исправленный массив из 500 первых символов для проверки пользователем правильности расшифровки
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tВот примерно так получается:");
        for (int i = 0; i < 500; i++) {
            System.out.print(mas_cryptoLine[i]);
        }
        System.out.println("...");

        System.out.println("\n\n\tЕсли хочешь поменять стиль дешифровки, нажми \"y\"");
        System.out.println("\tЕсли хочешь поменять символы местами в этой дешифровке, нажми \"r\"");
        String y_or_r = vvod.next();
        if (y_or_r.equals("y")) {
            speech_style_for_cryptoAnalysis(cryptoLine);
        } else if (y_or_r.equals("r")) {
            swap_symbol(cryptoLine);
        }

    }

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
            for (int j = 0; j < aAlphabet.size(); j++) {
                if (s_mas[i].equals(aAlphabet.get(j))) {
                    try {
                        s_mas[i] = aAlphabet.get(j + shift);
                    } catch (IndexOutOfBoundsException e) {
                        if ((j + shift) < 0) {
                            s_mas[i] = aAlphabet.get(j + shift + aAlphabet.size());
                        } else s_mas[i] = aAlphabet.get(j + shift - aAlphabet.size());
                    }
                    break;
                }
            }
//             И сразу записываем символ в финальную строку
            finish_line.append(s_mas[i]);
        }
        return finish_line.toString();
    }

    public static String readFile(String path) {
        StringBuilder s = new StringBuilder();
        String line;
        try {
            FileReader file = new FileReader(path);
            BufferedReader br = new BufferedReader(file);
            while ((line = br.readLine()) != null) {
                s.append(line).append("\n");    // Записываем все строки в "s"
            }
            br.close();
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return s.toString();
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

    public static void tryCatch_openFile() {
        Scanner vv = new Scanner(System.in);

        s_user_file_path = vv.next();
        try {
            s_original = readFile(s_user_file_path);
        } catch (Exception e) {
            System.out.println("Что-то пошло не так, скорее всего путь указан не верно.");
            System.out.println("Попробуй снова");
            System.out.println("Введи путь до твоего текстового файла");
            System.out.println("Вот пример: C:\\Users\\User\\Desktop\\name.txt");
            s_user_file_path = vv.next();
            try {
                s_original = readFile(s_user_file_path);
            } catch (Exception ee) {
                System.out.print("Что-то снова пошло не так, скорее всего путь введен не верно. ");
                System.out.println("Тогда открою свой файл");
                s_original = readFile("original_text.txt");
            }
        }
    }

    public static boolean three_try() {
        // Если пользователь ввел 3 раза подряд неправильное число, то программа закрывается
        Scanner vv = new Scanner(System.in);
        if (shift < -40 || shift > 40) {
            for (int i = 0; i < 2; i++) {
                System.out.println("Шифр только от -40 до 40");
                shift = vv.nextInt();
                if (shift > -40 && shift < 40) {
                    break;
                }
            }
        }
        if (shift < -40 || shift > 40) {
            System.out.println("Извини, ты, видимо, слишком умен для этой программы. " +
                    "Попробуй пожалуйста снова.");
            b = false;
        } else b = true;
        return b;
    }

    public static void three_try2(String cryptoLine) {
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
                brutForse(cryptoLine);
                break;
            } else if (one_or_two.equals("2")) {
                cryptoAnalysis(cryptoLine);
                break;
            }
        }
        if (one_or_two.equals("1")) {
            System.out.println("Выбран метод Brute force (поиск грубой силой)");
            brutForse(cryptoLine);
        } else if (one_or_two.equals("2")) {
            System.out.println("Выбран метод Криптоанализ на основе статистических данных");
            cryptoAnalysis(cryptoLine);
        } else {
            System.out.println("Извини, ты, видимо, слишком умен для этой программы. " +
                    "Попробуй пожалуйста снова.");
        }
    }

    public static void speech_style_for_cryptoAnalysis(String cryptoLine) {
        Scanner vvod = new Scanner(System.in);

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

        switch (vvod.next()) {
            case "0" -> {
                mas_symbol_frequency = symbol_frequency_easy.split("");
                cryptoAnalysis(cryptoLine);
            }
            case "1" -> {
                mas_symbol_frequency = symbol_frequency_science.split("");
                cryptoAnalysis(cryptoLine);
            }
            case "2" -> {
                mas_symbol_frequency = symbol_frequency_journalistic.split("");
                cryptoAnalysis(cryptoLine);
            }
            case "3" -> {
                mas_symbol_frequency = symbol_frequency_official.split("");
                cryptoAnalysis(cryptoLine);
            }
            case "4" -> {
                mas_symbol_frequency = symbol_frequency_artistic.split("");
                cryptoAnalysis(cryptoLine);
            }
            case "5" -> {
                mas_symbol_frequency = symbol_frequency_conversational.split("");
                cryptoAnalysis(cryptoLine);
            }
            case "6" -> {
                mas_symbol_frequency = symbol_frequency_martian.split("");
                cryptoAnalysis(cryptoLine);
            }
            default -> System.out.println("\n Спасибо за внимание. Ты лучший!");
        }
    }

    public static void swap_symbol(String cryptoLine) {
        Scanner vvod = new Scanner(System.in);

        System.out.println("Введи заменяемый символ");
        String swap_symbol_1 = vvod.next();
        System.out.println("Введи, каким символом заменить");
        String swap_symbol_2 = vvod.next();

        for (int i = 0; i < mas_symbol_frequency.length; i++) {
            if (mas_symbol_frequency[i].equals(swap_symbol_1)) {
                mas_symbol_frequency[i] = swap_symbol_2;
            } else if (mas_symbol_frequency[i].equals(swap_symbol_2)) {
                mas_symbol_frequency[i] = swap_symbol_1;
            }
        }
        cryptoAnalysis(cryptoLine);
    }
}