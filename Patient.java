package objpackage;

import java.util.Locale;

public class Patient {
    private int id;
    private String prizvyshche;     // Прізвище
    private String imya;            // Ім'я
    private String poBatkovi;       // По батькові
    private String adresa;          // Адреса
    private String telefon;         // Телефон (10 цифр)
    private int nomerKarty;         // Номер медичної карти
    private String diagnoz;         // Діагноз

    // Конструктор за замовчуванням
    public Patient() {
        this.id = 0;
        this.prizvyshche = "Не вказано";
        this.imya = "Н";
        this.poBatkovi = "В";
        this.adresa = "Не вказано";
        this.telefon = "0000000000";
        this.nomerKarty = 0;
        this.diagnoz = "Не вказано";
    }

    // Конструктор з усіма параметрами
    public Patient(int id, String pr, String im, String pb, String adr, String tel, int nmk, String dg) {
        this.id = id;
        this.prizvyshche = pr;
        this.imya = im;
        this.poBatkovi = pb;
        this.adresa = adr;
        this.telefon = tel;
        this.nomerKarty = nmk;
        this.diagnoz = dg;
    }

    // Методи getValue()
    public int getId() { return id; }
    public String getPrizvyshche() { return prizvyshche; }
    public String getImya() { return imya; }
    public String getPoBatkovi() { return poBatkovi; }
    public String getAdresa() { return adresa; }
    public String getTelefon() { return telefon; }
    public int getNomerKarty() { return nomerKarty; }
    public String getDiagnoz() { return diagnoz; }

    // Методи setValue()
    public void setId(int novyiId) { this.id = novyiId; }
    public void setPrizvyshche(String novePrizvyshche) { this.prizvyshche = novePrizvyshche; }
    public void setImya(String noveImya) { this.imya = noveImya; }
    public void setPoBatkovi(String novePoBatkovi) { this.poBatkovi = novePoBatkovi; }
    public void setAdresa(String novaAdresa) { this.adresa = novaAdresa; }
    public void setTelefon(String novyiTelefon) { this.telefon = novyiTelefon; }
    public void setNomerKarty(int novyiNomerKarty) { this.nomerKarty = novyiNomerKarty; } // ⬅️ ЗМІНЕНО
    public void setDiagnoz(String novyiDiagnoz) { this.diagnoz = novyiDiagnoz; }

    // Метод для коректного форматування адреси (робить вивід красивим)
    private String formatAdresa(String adresa) {
        if (adresa == null || adresa.isEmpty()) return "";

        // Видаляю всі пробіли, що йдуть за комою, та залишаю один,
        // потім прибираю зайві пробіли.
        return adresa.trim()
                .replaceAll(",\\s*", ", ")
                .replaceAll("\\s{2,}", " ");
    }

    // Метод для короткого виведення
    public String getKorotkyiRiadok() {
        char imyaInit = this.imya != null && !this.imya.isEmpty() ? this.imya.charAt(0) : ' ';
        char poBatkoviInit = this.poBatkovi != null && !this.poBatkovi.isEmpty() ? this.poBatkovi.charAt(0) : ' ';

        String cleanAdresa = formatAdresa(this.adresa);

        return String.format("%d. %s %c. %c., Адреса: %s, Телефон: %s, № мед. карти: %d, Діагноз: %s",
                this.id, this.prizvyshche, imyaInit, poBatkoviInit,
                cleanAdresa, this.telefon, this.nomerKarty, this.diagnoz);
    }

    // Метод toString() (для детального виводу)
    @Override
    public String toString() {
        String cleanAdresa = formatAdresa(this.adresa);

        return String.format(
                "\n*** ПАЦІЄНТ (ID: %d) ***\n" +
                        "П.І.Б.: %s %s %s\n" +
                        "Адреса: %s\n" +
                        "Телефон: %s\n" +
                        "№ мед. карти: %d\n" +
                        "Діагноз: %s",
                this.id, this.prizvyshche, this.imya, this.poBatkovi,
                cleanAdresa,
                this.telefon, this.nomerKarty, this.diagnoz);
    }
}