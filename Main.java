package mainpackage; // Пакет для класу Main

import objpackage.Patient; // Імпорт класу Patient з іншого пакету
import java.io.*; // Для роботи з файлами (читання, запис)
import java.util.ArrayList; // Для використання динамічного масиву
import java.util.InputMismatchException; // Для обробки помилок введення числа
import java.util.Locale; // Для роботи з локалізацією (наприклад, нижній/верхній регістр)
import java.util.Scanner; // Для зчитування вводу з консолі
import java.util.regex.Matcher; // Для роботи з регулярними виразами
import java.util.regex.Pattern; // Для роботи з регулярними виразами
import java.nio.charset.StandardCharsets; // Для кодування при читанні/записі файлів


public class Main {

    public static void main(String[] args) {
        ArrayList<Patient> patsienty = new ArrayList<>();
        Scanner skaner = new Scanner(System.in);

        // 1. Отримую шлях до файлу лише один раз
        String shliakhDoFailu = otrymatyShliakhDoFailu(skaner);

        // 2. Зчитування даних
        zchytatyZFailu(patsienty, shliakhDoFailu);

        boolean programaPratsyye = true;
        while (programaPratsyye) {
            System.out.println("\n==================================");
            System.out.println("Що бажаєте вивести/зробити?");
            System.out.println("1. Список пацієнтів із заданим діагнозом");
            System.out.println("2. Список пацієнтів з № мед. карти в заданому інтервалі");
            System.out.println("3. Список пацієнтів, які проживають у вказаному місті");
            System.out.println("4. Список всіх пацієнтів");
            System.out.println("5. Додати нового пацієнта");
            System.out.println("6. Видалити пацієнта за ID");
            System.out.println("7. Список пацієнтів з таким ім'ям");
            System.out.println("8. Список пацієнтів за першою цифрою телефону");
            System.out.println("0. Закінчити виконання програми");
            System.out.print("Ваш вибір: ");

            try {
                if (skaner.hasNextInt()) {
                    int vybir = skaner.nextInt();
                    skaner.nextLine();

                    switch (vybir) {
                        case 0:
                            System.out.println("\nЗакінчення виконання програми!");
                            programaPratsyye = false;
                            break;
                        case 1:
                            System.out.print("Введіть діагноз для пошуку: ");
                            String diagnoz = skaner.nextLine();
                            vypysatyZaDiagnozom(patsienty, diagnoz);
                            break;
                        case 2:
                            int min = otrymatyTsiluZminnu(skaner, "Введіть мінімальний номер медичної картки (1-1800): ", 1, 1800);
                            int max = otrymatyTsiluZminnu(skaner, "Введіть максимальний номер медичної картки (1-1800): ", min, 1800);
                            vypysatyZaNomeromKarty(patsienty, min, max);
                            break;
                        case 3:
                            System.out.print("Введіть назву міста (напр. Львів): ");
                            String potribneMisto = skaner.nextLine();
                            vypysatyZaMistom(patsienty, potribneMisto);
                            break;
                        case 4:
                            vypysatyVsi(patsienty);
                            break;
                        case 5:
                            dodatyPatsiienta(shliakhDoFailu, patsienty, skaner);
                            break;
                        case 6:
                            vydalytyPatsiienta(patsienty, skaner, shliakhDoFailu);
                            break;
                        case 7:
                            System.out.print("Введіть ім'я пацієнта для пошуку: ");
                            String potribneImya = skaner.nextLine();
                            vypysatyZaImenam(patsienty, potribneImya);
                            break;
                        case 8:
                            System.out.print("Введіть першу цифру телефону для пошуку (0-9): ");
                            String pershaTsyfra = skaner.nextLine().trim();
                            vypysatyZaPershoiuTsyfroiuTelefonu(patsienty, pershaTsyfra);
                            break;
                        default:
                            System.out.println("Введіть коректне значення (0-8)!");
                    }
                } else {
                    System.out.println("Введіть правильне значення (лише цифру)!");
                    skaner.nextLine();
                }
            } catch (Exception e) {
                System.out.println("Виникла помилка: " + e.getMessage());
                skaner.nextLine();
            }
        }
        skaner.close();
    }

    // Метод для коректного зчитування цілого числа
    private static int otrymatyTsiluZminnu(Scanner skaner, String zapyt, int min, int max) {
        int znachennia = -1;
        while (znachennia < min || znachennia > max) {
            System.out.print(zapyt);
            try {
                if (skaner.hasNextInt()) {
                    znachennia = skaner.nextInt();
                }
                skaner.nextLine();
                if (znachennia < min || znachennia > max) {
                    System.out.printf("Помилка: Значення повинно бути в межах від %d до %d.\n", min, max);
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: введіть ціле число.");
                skaner.nextLine();
            }
        }
        return znachennia;
    }

    // --- Методи для валідації ---
    private static String otrymatyTekstDliaPIB(Scanner skaner, String zapyt) {
        String tekst = "";
        boolean valid = false;
        while (!valid) {
            System.out.print(zapyt);
            tekst = skaner.nextLine().trim();
            if (!tekst.matches("^[\\p{IsCyrillic}'\\- ]+$")) {
                System.out.println("Помилка: Поле може містити лише кириличні літери, пробіл, дефіс та апостроф.");
                continue;
            }
            if (tekst.isEmpty()) {
                System.out.println("Помилка: Поле не може бути пустим.");
                continue;
            }
            valid = true;
        }
        return tekst;
    }

    private static String otrymatyDiagnoz(Scanner skaner, String zapyt) {
        String tekst = "";
        boolean valid = false;
        while (!valid) {
            System.out.print(zapyt);
            tekst = skaner.nextLine().trim();
            if (!tekst.matches("^[\\p{L}0-9'\\- ]+$")) {
                System.out.println("Помилка: Діагноз може містити лише літери, цифри, пробіл, дефіс або апостроф.");
                continue;
            }
            if (tekst.isEmpty()) {
                System.out.println("Помилка: Поле не може бути пустим.");
                continue;
            }
            valid = true;
        }
        return tekst;
    }

    private static String otrymatyTekstBezTsyfr(Scanner skaner, String zapyt) {
        String tekst = "";
        boolean valid = false;
        while (!valid) {
            System.out.print(zapyt);
            tekst = skaner.nextLine().trim();
            if (!tekst.matches("^[\\p{IsCyrillic}'\\- ]+$")) {
                System.out.println("Помилка: Назва може містити лише кириличні літери, пробіл, дефіс та апостроф.");
                continue;
            }
            if (tekst.isEmpty()) {
                System.out.println("Помилка: Поле не може бути пустим.");
                continue;
            }
            valid = true;
        }
        return tekst;
    }

    private static String otrymatyNomerBudynku(Scanner skaner, String zapyt) {
        String nomer = "";
        boolean valid = false;
        Pattern housePattern = Pattern.compile("^\\d+[\\p{IsCyrillic}]?$");

        while (!valid) {
            System.out.print(zapyt);
            nomer = skaner.nextLine().trim();
            if (!housePattern.matcher(nomer).matches()) {
                System.out.println("Помилка: Номер будинку має бути цифрою або цифрою з однією кириличною літерою (напр. 5, 5А).");
                continue;
            }
            try {
                Matcher numberMatcher = Pattern.compile("^\\d+").matcher(nomer);
                numberMatcher.find();
                int mainNumber = Integer.parseInt(numberMatcher.group());

                if (mainNumber < 1 || mainNumber > 500) {
                    System.out.println("Помилка: Номер будинку має бути в діапазоні від 1 до 500.");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("Помилка: Некоректне числове значення в номері будинку.");
                continue;
            }
            valid = true;
        }
        return nomer;
    }


    // --- Робота з файлами ---

    private static String otrymatyShliakhDoFailu(Scanner skaner) {
        String shliakhDoFailu;
        File novyiFail;

        while (true) {
            System.out.print("Введіть шлях текстового файлу бази даних: ");
            shliakhDoFailu = skaner.nextLine();

            novyiFail = new File(shliakhDoFailu);

            if (!novyiFail.exists() || !novyiFail.isFile()) {
                System.out.println("Файл не знайдено або введено некоректний шлях. Спробуйте ще раз.");
                continue;
            }
            return shliakhDoFailu;
        }
    }

    public static void zchytatyZFailu(ArrayList<Patient> patsienty, String shliakhDoFailu) {
        patsienty.clear();

        try (BufferedReader chytach = new BufferedReader(new FileReader(shliakhDoFailu))) {
            String riadok;
            int kilkistZavantazheno = 0;
            while ((riadok = chytach.readLine()) != null) {
                if (riadok.trim().isEmpty()) continue;

                // Розділяю рядок
                String[] fields = riadok.split(",");

                if (fields.length >= 8) {
                    try {
                        int id = Integer.parseInt(fields[0].trim());

                        // Поля з фіксованими позиціями (початок)
                        String prizvyshche = fields[1].trim();
                        String imya = fields[2].trim();
                        String poBatkovi = fields[3].trim();

                        // Поля з фіксованими позиціями (кінець)
                        String diagnoz = fields[fields.length - 1].trim();
                        int nomerKarty = Integer.parseInt(fields[fields.length - 2].trim());
                        String telefon = fields[fields.length - 3].trim();

                        // Склеювання Адреси: всі поля між 3 (по батькові) та fields.length - 3 (телефон)
                        StringBuilder adresaBuilder = new StringBuilder();
                        for (int i = 4; i <= fields.length - 4; i++) {
                            if (adresaBuilder.length() > 0) adresaBuilder.append(",");
                            adresaBuilder.append(fields[i].trim());
                        }
                        String adresa = adresaBuilder.toString();

                        // Перевірка, що Адреса не порожня, якщо полів було 8
                        if (fields.length == 8 && fields[4].trim().isEmpty()) {
                            throw new Exception("Адреса не може бути пустою.");
                        }

                        patsienty.add(new Patient(id, prizvyshche, imya, poBatkovi, adresa, telefon, nomerKarty, diagnoz));
                        kilkistZavantazheno++;
                    } catch (NumberFormatException e) {
                        System.out.println("Помилка формату числа/телефону у рядку: " + riadok);
                    } catch (Exception e) {
                        System.out.println("Помилка обробки рядка: " + riadok + ". Причина: " + e.getMessage());
                    }
                } else {
                    System.out.println("Помилка: Некоректна кількість полів (" + fields.length + ") у рядку: " + riadok);
                }
            }
            System.out.printf("Дані успішно завантажено з: %s. Зчитано пацієнтів: %d\n", shliakhDoFailu, kilkistZavantazheno);

        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
        }
    }

    // Метод перезапису файлу
    public static void perezapysatyFail(ArrayList<Patient> patsienty, String shliakhDoFailu) {
        try (BufferedWriter pysach = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(shliakhDoFailu), StandardCharsets.UTF_8))) {

            // Записую всі рядки, розділені комою
            for (Patient patsient : patsienty) {
                String riadok = String.format(
                        Locale.ROOT,
                        "%d, %s, %s, %s, %s, %s, %d, %s",
                        patsient.getId(),
                        patsient.getPrizvyshche(),
                        patsient.getImya(),
                        patsient.getPoBatkovi(),
                        patsient.getAdresa(),
                        patsient.getTelefon(),
                        patsient.getNomerKarty(),
                        patsient.getDiagnoz()
                );
                pysach.write(riadok);
                pysach.newLine();
            }

            System.out.println("Дані успішно збережено у файл.");
        } catch (IOException e) {
            System.out.println("Помилка запису у файл: " + e.getMessage());
        }
    }


    // Метод для перевірки унікальності телефону
    private static boolean telefonUnikalnyi(ArrayList<Patient> patsienty, String telefon) {
        for (Patient p : patsienty) {
            if (telefon.equals(p.getTelefon())) {
                return false;
            }
        }
        return true;
    }

    // Метод для перевірки унікальності номера карти
    private static boolean kartaUnikalna(ArrayList<Patient> patsienty, int nomerKarty) {
        for (Patient p : patsienty) {
            if (nomerKarty == p.getNomerKarty()) {
                return false;
            }
        }
        return true;
    }

    // Метод додавання пацієнта
    public static void dodatyPatsiienta(String shliakhDoFailu, ArrayList<Patient> patsienty, Scanner skaner) {
        Patient novyiPatsiient = new Patient();
        System.out.println("\n*** Додавання нового пацієнта ***");

        // 1. ID
        int novyiId = -1;
        boolean idUnikalne = false;
        while (!idUnikalne) {
            novyiId = otrymatyTsiluZminnu(skaner, "Введіть ID пацієнта (1-9999): ", 1, 9999);
            idUnikalne = true;
            for (Patient p : patsienty) {
                if (novyiId == p.getId()) {
                    System.out.println("Пацієнт з таким ID вже існує! Спробуйте інший.");
                    idUnikalne = false;
                    break;
                }
            }
        }
        novyiPatsiient.setId(novyiId);

        // 2-4. ПІБ
        novyiPatsiient.setPrizvyshche(otrymatyTekstDliaPIB(skaner, "Введіть прізвище (напр. Іваненко): "));
        novyiPatsiient.setImya(otrymatyTekstDliaPIB(skaner, "Введіть ім'я (напр. Петро): "));
        novyiPatsiient.setPoBatkovi(otrymatyTekstDliaPIB(skaner, "Введіть по батькові (напр. Степанович): "));

        // 5. Адреса
        String misto = otrymatyTekstBezTsyfr(skaner, "Введіть Місто (напр. Львів): ");
        String vulytsia = otrymatyTekstBezTsyfr(skaner, "Введіть назву вулиці (напр. Зелена): ");
        String budynok = otrymatyNomerBudynku(skaner, "Введіть номер будинку (від 1 до 500, напр. 5А): ");
        String povnaAdresa = String.format("м. %s, вул. %s, буд. %s",
                misto, vulytsia, budynok);
        novyiPatsiient.setAdresa(povnaAdresa);


        // 6. Телефон
        String novyiTelefon = "";
        boolean telefonValidnyi = false;
        Pattern telefonPattern = Pattern.compile("^\\d{10}$");
        while (!telefonValidnyi) {
            System.out.print("Введіть телефон (10 цифр, напр. 0501234567): ");
            novyiTelefon = skaner.nextLine().trim();
            Matcher matcher = telefonPattern.matcher(novyiTelefon);

            if (!matcher.matches()) {
                System.out.println("Помилка: Некоректний формат телефону (має бути рівно 10 цифр).");
                continue;
            }
            if (!telefonUnikalnyi(patsienty, novyiTelefon)) {
                System.out.println("Номер телефону вже використовується! Спробуйте інший.");
                continue;
            }
            telefonValidnyi = true;
        }
        novyiPatsiient.setTelefon(novyiTelefon);


        // 7. Номер медичної картки
        int novyiNomerKarty = -1;
        boolean kartaValidna = false;
        while (!kartaValidna) {
            novyiNomerKarty = otrymatyTsiluZminnu(skaner, "Введіть номер медичної карти (від 1 до 1800): ", 1, 1800);
            if (!kartaUnikalna(patsienty, novyiNomerKarty)) {
                System.out.println("Номер медичної карти вже використовується! Спробуйте інший.");
            } else {
                kartaValidna = true;
            }
        }
        novyiPatsiient.setNomerKarty(novyiNomerKarty);

        // 8. Діагноз
        novyiPatsiient.setDiagnoz(otrymatyDiagnoz(skaner, "Введіть діагноз (напр. COVID-19): "));

        patsienty.add(novyiPatsiient);

        // Дозаписую доданий об'єкт у файл
        try (BufferedWriter pysach = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(shliakhDoFailu, true), StandardCharsets.UTF_8))) {

            // Додаю новий рядок перед записом, якщо файл не порожній
            File file = new File(shliakhDoFailu);
            if (file.length() > 0) {
                pysach.newLine();
            }

            String riadok = String.format(
                    Locale.ROOT,
                    "%d, %s, %s, %s, %s, %s, %d, %s",
                    novyiPatsiient.getId(),
                    novyiPatsiient.getPrizvyshche(),
                    novyiPatsiient.getImya(),
                    novyiPatsiient.getPoBatkovi(),
                    novyiPatsiient.getAdresa(),
                    novyiPatsiient.getTelefon(),
                    novyiPatsiient.getNomerKarty(),
                    novyiPatsiient.getDiagnoz()
            );
            pysach.write(riadok);

            System.out.println("Пацієнта успішно додано та збережено!");
        } catch (IOException e) {
            System.out.println("Помилка запису нового пацієнта у файл: " + e.getMessage());
        }
    }

    // Решта методів (vydalytyPatsiienta, vypysatyZaDiagnozom і т.д.)
    public static void vydalytyPatsiienta(ArrayList<Patient> patsienty, Scanner skaner, String shliakhDoFailu) {
        int idDliaVydalennia = otrymatyTsiluZminnu(skaner, "Введіть ID пацієнта, якого Ви хочете видалити: ", 1, Integer.MAX_VALUE);

        Patient patsientDliaVydalennia = null;
        int indexToDelete = -1;

        for (int i = 0; i < patsienty.size(); i++) {
            if (patsienty.get(i).getId() == idDliaVydalennia) {
                patsientDliaVydalennia = patsienty.get(i);
                indexToDelete = i;
                break;
            }
        }

        if (patsientDliaVydalennia != null) {
            patsienty.remove(indexToDelete);
            perezapysatyFail(patsienty, shliakhDoFailu);
            System.out.printf("Пацієнта з ID=%d (%s %s) було видалено!\n", idDliaVydalennia, patsientDliaVydalennia.getPrizvyshche(), patsientDliaVydalennia.getImya());
        } else {
            System.out.printf("Пацієнта з ID=%d не існує!\n", idDliaVydalennia);
        }
    }

    public static void vypysatyZaDiagnozom(ArrayList<Patient> patsienty, String potribnyiDiagnoz) {
        System.out.printf("\n*** Список пацієнтів із діагнозом %s ***\n", potribnyiDiagnoz);
        int kilkist = 0;
        for (Patient p : patsienty) {
            if (p.getDiagnoz().trim().equalsIgnoreCase(potribnyiDiagnoz.trim())) {
                System.out.println(p.toString());
                kilkist++;
            }
        }
        if (kilkist == 0) {
            System.out.println("Немає пацієнтів із таким діагнозом.");
        } else {
            System.out.printf("Знайдено пацієнтів: %d\n", kilkist);
        }
    }

    public static void vypysatyZaNomeromKarty(ArrayList<Patient> patsienty, int min, int max) {
        System.out.printf("\n*** Список пацієнтів, № мед. карти яких у діапазоні [%d - %d] ***\n", min, max);
        int kilkist = 0;
        for (Patient p : patsienty) {
            int nomer = p.getNomerKarty();
            if (nomer >= min && nomer <= max) {
                System.out.println(p.toString());
                kilkist++;
            }
        }
        if (kilkist == 0) {
            System.out.println("Немає пацієнтів, що відповідають критерію.");
        } else {
            System.out.printf("Знайдено пацієнтів: %d\n", kilkist);
        }
    }

    public static void vypysatyZaMistom(ArrayList<Patient> patsienty, String potribneMisto) {
        System.out.printf("\n*** Список пацієнтів, які проживають у місті %s ***\n", potribneMisto);
        int kilkist = 0;
        String searchCityPrefix = "м. " + potribneMisto.trim().toLowerCase(Locale.ROOT);

        for (Patient p : patsienty) {
            String adresaLower = p.getAdresa().toLowerCase(Locale.ROOT);
            if (adresaLower.startsWith(searchCityPrefix)) {
                int prefixLength = searchCityPrefix.length();
                if (adresaLower.length() > prefixLength) {
                    char charAfterCity = adresaLower.charAt(prefixLength);

                    if (charAfterCity == ' ' || charAfterCity == ',') {
                        System.out.println(p.toString());
                        kilkist++;
                    }
                }
            }
        }

        if (kilkist == 0) {
            System.out.println("Немає пацієнтів, що відповідають критерію.");
        } else {
            System.out.printf("Знайдено пацієнтів: %d\n", kilkist);
        }
    }

    public static void vypysatyZaImenam(ArrayList<Patient> patsienty, String potribneImya) {
        System.out.printf("\n*** Список пацієнтів з ім'ям %s ***\n", potribneImya);
        int kilkist = 0;

        String searchString = potribneImya.trim().toLowerCase(Locale.ROOT);

        for (Patient p : patsienty) {
            if (p.getImya().toLowerCase(Locale.ROOT).equals(searchString)) {
                System.out.println(p.toString());
                kilkist++;
            }
        }

        if (kilkist == 0) {
            System.out.printf("Не знайдено пацієнтів з ім'ям %s.\n", potribneImya);
        } else {
            System.out.printf("Знайдено пацієнтів: %d\n", kilkist);
        }
    }


    public static void vypysatyZaPershoiuTsyfroiuTelefonu(ArrayList<Patient> patsienty, String pershaTsyfra) {
        // Використовуємо регулярний вираз для перевірки, що введена лише одна цифра
        if (!pershaTsyfra.matches("^[0-9]$")) {
            System.out.println("Помилка: Будь ласка, введіть лише одну цифру (0-9).");
            return;
        }

        System.out.printf("\n*** Знайдено пацієнтів, телефон яких починається з '%s' ***\n", pershaTsyfra);
        int kilkist = 0;

        for (Patient p : patsienty) {
            String telefon = p.getTelefon().trim();

            // Перевіряю, чи номер телефону починається з вказаної цифри
            if (telefon.startsWith(pershaTsyfra)) {
                System.out.println(p.toString());
                kilkist++;
            }
        }

        if (kilkist == 0) {
            System.out.printf("Не знайдено пацієнтів, номер телефону яких починається з %s.\n", pershaTsyfra);
        } else {
            System.out.printf("Знайдено пацієнтів: %d\n", kilkist);
        }
    }

    public static void vypysatyVsi(ArrayList<Patient> patsienty) {
        System.out.println("\n*** Список всіх пацієнтів ***");
        if (patsienty.isEmpty()) {
            System.out.println("Список пацієнтів порожній.");
            return;
        }
        for (Patient p : patsienty) {
            System.out.println(p.getKorotkyiRiadok());
        }
    }
}