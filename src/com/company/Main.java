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
    static String s_Crypto = "";
    static int shift;
    static String s_original = "";
    static boolean b = true;
    static String s_user_file_path = "";

    //  создадим список из букв и символов для дальнейшей работы
    static String s_alphabet = "абвгдеёжзийклмнопрстуфхцчшщьыъэюя.,\":-!? ";
    static ArrayList<String> aAlphabet = new ArrayList<>(Arrays.asList(s_alphabet.split("")));

    public static void main(String[] args) {
        Scanner vv = new Scanner(System.in);

        System.out.println("Эта программа очень крутая на самом деле");
        System.out.println("Тут можно не только расшифровать текст, но и зашифровать");
        System.out.println("Если хотите зашифровать текст нажмите 'y'");
        System.out.println("Продолжить по умолчанию - любой другой символ");
        // Проверка на запуск метода шифрования
        if (vv.next().equals("y")) {
            System.out.println("Выберите смещение символов по Шифру Цезаря. " +
                    "Знак минус означает смещение влево");
            System.out.println("Только выбирайте пожалуйста от -40 до 40");
            shift = vv.nextInt();
            // Если пользователь ввел 3 раза подряд неправильное число, то программа закроется
            if (three_try()) {
                System.out.println("Если хотите зашифровать свой собственный текст нажмите 'y'");
                // Проверка на желание пользователя загрузить собственный текст
                if (vv.next().equals("y")) {
                    System.out.println("Введите путь до вашего текстового файла файла");
                    System.out.println("Пример: C:\\Users\\User\\Desktop\\name.txt");
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

        System.out.println("Каким способом вы хотите расшифровать текст?");
        System.out.println("Если Brute force (брутфорс, поиск грубой силой) наберите \"1\"");
        System.out.println("Если Криптоанализ на основе статистических данных наберите \"2\"");

        String one_or_two = vv.next();
        if (one_or_two.equals("1")) {
//              1. brute force
            System.out.println("Выбран метод Brute force (брутфорс, поиск грубой силой)");
            find_shift_brutForse(cryptoLine);
        } else if (one_or_two.equals("2")) {
//              2. Криптоанализ на основе статистических данных
            System.out.println("Выбран метод Криптоанализ на основе статистических данных");
            find_shift_cryptoAnalysis(cryptoLine);
        } else {
            // Если пользователь ввел 3 раза подряд неправильное число, то программа просто закроется
            three_try2(cryptoLine);
        }
    }




    public static void find_shift_brutForse(String cryptoLine) {
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
                    System.out.println("В данной программе это не важно, так как текст все равно правильно дешифруется");
                    writeFile("decipher_text.txt");
                    return;
                }
            }
        }
//        Ну если совсем не получается определить, то выводим сообщение
        System.out.println("Не могу определить сдвиг, попробуйте файл с большим текстом внутри");
    }

    /*public static void find_shift_cryptoAnalysis(String cryptoLine) {
//        Каждой букве нужно присвоить номер, для этого скорее всего лучше подойдет ArrayMap
        HashMap<String, Integer> map_letters = new HashMap<>();
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


        // На самом деле можно сделать легче, точнее будет легко для моего текста,
        // так как мы можем точно посчитать частоту вхождения каждого символа оригинального текста
        // и просто приложить как кальку к зашифрованному.
        // Но это скорее всего не будет работать к другим текстам, поэтому лучше сразу сделать нормальную,
        // чтобы нигде нормально не работало :)



//        Определяем статистику вхождения символов среднестатистического текста
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

        // Для этого нужно отсортировать полученный мап из (буква - частота вхождения)
//        Разрезаем наш текст по символам
        String[] s_mas = cryptoLine.split("");
        //        склько раз встретился тот или иной символ в тексте
        for (int i = 0; i < aAlphabet.size(); i++) {
            for (int j = 0; j < s_mas.length; j++) {
                if (aAlphabet.get(i).equals(s_mas[j])) {
                    map_letters.put(aAlphabet.get(i), (map_letters.get(aAlphabet.get(i)) + 1));
                }
            }
        }


        String q = map_letters.entrySet().stream().max(Map.Entry.comparingByValue()).toString();

        String[] qw = q.split("\\[");
        String[] wq = qw[1].split("\\]");
        String[] wqw = wq[0].split("=");   // Нашли самый частый символ - " "
//        Теперь мы знаем какой символ встретился чаще всего,
// Теперь по идее нам нужно его заменить на пробел
        int k = 0;
        for (int i = 0; i < aAlphabet.size(); i++) {
            if (aAlphabet.get(i).equals(wqw[0])) {
                k = i;
                break;
            }
        }






        // Способ 1: находим самый частый символ в зашифрованном тексте и понимаем, что это " "

//        letters.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue().reversed()).
//                forEach(System.out::println); // или любой другой конечный метод



//        System.out.println(wqw[0] + " " + wqw[1]);

//        Теперь мы знаем какой символ встретился чаще всего,
//        осталось только найти его положение и посчитать смещение относительно " "
        int k = 0;
        for (int i = 0; i < aAlphabet.size(); i++) {
            if (aAlphabet.get(i).equals(wqw[0])) {
                k = i;
                break;
            }
        }
//        Мы знаем, что " " находится под индексом 41 (последним) в alphabet
//        Мы узнали под каким индексом находится наш наиболее часто встречающийся символ
//        Т.о. мы можем узнать сдвиг шифра

        if (k > aAlphabet.size() / 2) { // Проверяем сдвиг вправо или влево
            shift = -1 * (aAlphabet.size() - k - 1);
        } else shift = k + 1;                 // +1, так как в массивах счет начинается с 0
//        System.out.println("shift = " + shift);

//        Теперь обратно нужно сдвинуть на место, для этого меняем знак у shift
        shift = -1 * shift;

        s_Crypto = toCrypto_noCase(cryptoLine);  // Сдвинули каждый символ на найденный "shift"
        writeFile("decipher_text.txt");
    }*/

    public static void find_shift_cryptoAnalysis(String cryptoLine) {
        String[] qw, wq, wqw;
        ArrayList<Integer> after_replace = new ArrayList<>();   // сюда будут заноситься символы, которые уже заменены

//        Каждой букве нужно присвоить номер, для этого скорее всего лучше подойдет ArrayMap
        HashMap<String, Integer> map_letters = new HashMap<>();
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


        // На самом деле можно сделать легче, точнее будет легко для моего текста,
        // так как мы можем точно посчитать частоту вхождения каждого символа оригинального текста
        // и просто приложить как кальку к зашифрованному.
        // Но это скорее всего не будет работать к другим текстам, поэтому лучше сразу сделать нормальную,
        // чтобы нигде нормально не работало :)

        // Вообще хорошо было бы, чтобы программа сама подбирала последующие символы на основе популярных слов

//        Определяем статистику вхождения символов среднестатистического текста
        String symbol_frequency_easy = " оетасинвмлдркяыпб,ьйуч.гжзхюшщэёцф-?ъ!\":";
        String symbol_frequency_middle = " оетаинсвлрмдку,ьяпыбчг.зжйхюш-цщэфё:?!ъ\"";
        String symbol_frequency_mars = " оеатнисрлвмдпкуяь.зы,бчгйжхюшэцщф-?:!ъё\"";
        // хорошо бы добавить для разных стилей, штук 20 разных
        // хорошо если пользователю будет возможноть поменять символы самостоятельно

        // пользователю будет дана возможность выбрать наиболее понятную расшифровку
        // и в дополнение он сможет поменять испорченные символы


        ArrayList<String> aAlphabet = new ArrayList<>(Arrays.asList(s_alphabet.split("")));

        // Для этого нужно отсортировать полученный мап из (буква - частота вхождения)
//        Разрезаем наш текст по символам
        String[] mas_cryptoLine = cryptoLine.split("");                         // массив символов из текста
        String[] mas_symbol_frequency_easy = symbol_frequency_easy.split("");   // отсортированный массив символов из букв

//        записываем в Мап сколько раз встретился тот или иной символ в тексте
        for (int i = 0; i < aAlphabet.size(); i++) {
            for (int j = 0; j < mas_cryptoLine.length; j++) {
                if (aAlphabet.get(i).equals(mas_cryptoLine[j])) {
                    map_letters.put(aAlphabet.get(i), (map_letters.get(aAlphabet.get(i)) + 1));
                }
            }
        }       // Теперь у нас есть Мап с частотой вхождения символов в тексте


        // Повторяем пока не заменим все символы в тексте

        for (int i = 0; i < aAlphabet.size(); i++) {
//      находим самый частый символ в Мап-е с частотой вхождения символов в тексте
            String max_symbol = map_letters.entrySet().stream().max(Map.Entry.comparingByValue()).toString();

//      Убираем шелуху
            qw = max_symbol.split("\\[");   // max_symbol = "Optional[к=167]"
            wq = qw[1].split("\\]");        // qw[1] = "к=167]"
            wqw = wq[0].split("=");         // wq[0] = "к=167"

            //   Нашли самый частый символ - wqw[0], wqw[1] - количество повторений
//      Теперь мы знаем какой символ встретился чаще всего,
//      Теперь нам нужно заменить каждый такой символ в тексте на самый частый по статистике

            for (int j = 0; j < mas_cryptoLine.length; j++) {
                // Если в зашифрованном массиве символов всего текста встретится символ с максимальным вхождением
                // И Нам не нужно менять то, что уже поменяно
                if (mas_cryptoLine[j].equals(wqw[0]) && !after_replace.contains(j)) {
                    // То заменяем его на символ с максимальным вхождением по статистике
                    mas_cryptoLine[j] = mas_symbol_frequency_easy[i];
                    after_replace.add(j);                       // сюда будут заноситься номера поменянных ячеек

                }
            }
//        Теперь нам нужно удалить максимальную Map строку по ключу,
//        чтобы найти следующую максимальную
            map_letters.remove(wqw[0]);
        }

        s_Crypto = "";
        // записываем исправленный массив всех символов текста в строку, чтобы записать в файл
        for (int i = 0; i < mas_cryptoLine.length; i++) {
            s_Crypto += mas_cryptoLine[i];
        }


        writeFile("decipher_text.txt");
        System.out.println("Сделано, наслаждайтесь");
    }

    public static String toCrypto_noCase(String s) {
//        char_char();          // другой метод - шифратор, предложенный менторами


        //        У нас (по заданию) есть русский алфавит и знаки пунктуации (. , ”” : - ! ? ПРОБЕЛ).
//        !!! Пока на регистр не будем обращать внимание. Поэтому нужно все буквы перевести в нижний регистр
        String s_Lower = s.toLowerCase();

//        Разрезаем наш текст по символам
        String[] s_mas = s_Lower.split("");


        String finish_line = "";
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

    public static void tryCatch_openFile() {
        Scanner vv = new Scanner(System.in);

        s_user_file_path = vv.next();
        try {
            s_original = readFile(s_user_file_path);
        } catch (Exception e) {
            System.out.println("Что-то пошло не так, скорее всего путь указан не верно.");
            System.out.println("Попробуйте снова");
            System.out.println("Введите путь до вашего текстового файла файла");
            System.out.println("Пример: C:\\Users\\User\\Desktop\\name.txt");
            s_user_file_path = vv.next();
            try {
                s_original = readFile(s_user_file_path);
            } catch (Exception ee) {
                System.out.println("Что-то снова пошло не так, скорее всего путь введен не верно.");
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
            System.out.println("Извините, вы, видимо, слишком умны для этой программы. " +
                    "Попробуйте пожалуйста снова.");
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
            System.out.println("Попробуйте снова");
            System.out.println("Каким способом вы хотите расшифровать текст?");
            System.out.println("Если Brute force (брутфорс, поиск грубой силой) наберите \"1\"");
            System.out.println("Если Криптоанализ на основе статистических данных наберите \"2\"");
            one_or_two = vv.next();
            if (one_or_two.equals("1")) {
                find_shift_brutForse(cryptoLine);
                break;
            } else if (one_or_two.equals("2")) {
                find_shift_cryptoAnalysis(cryptoLine);
                break;
            }
        }
        if (one_or_two.equals("1")) {
            System.out.println("Выбран метод Brute force (брутфорс, поиск грубой силой)");
            find_shift_brutForse(cryptoLine);
        } else if (one_or_two.equals("2")) {
            System.out.println("Выбран метод Криптоанализ на основе статистических данных");
            find_shift_cryptoAnalysis(cryptoLine);
        } else {
            System.out.println("Извините, вы, видимо, слишком умны для этой программы. " +
                    "Попробуйте пожалуйста снова.");
        }
    }
}