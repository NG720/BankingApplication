/**
 * Proje: BankingApplication (Banka Simülasyonu)
 * Ödev Sahibi: Nasrulla Emin
 * Açıklama: In-Memory Repository mantığıyla çalışan, 
 * login destekli konsol bankacılık uygulaması.
 */


import java.util.ArrayList;
import java.util.Scanner;

public class BankingApplication {

    // Veritabanı simülasyonu (RAM'de tutuluyor)
    static ArrayList<String> musteriler = new ArrayList<>();
    static ArrayList<String> sifreler = new ArrayList<>();
    static ArrayList<Double> bakiyeler = new ArrayList<>();
    
    static Scanner scanner = new Scanner(System.in);
    static int oturumdakiMusteriIndex = -1; // Giriş yapan kullanıcıyı takip eder

    public static void main(String[] args) {
        // Test için bir tane örnek kullanıcı ekleyelim
        musteriler.add("admin");
        sifreler.add("1234");
        bakiyeler.add(1000.0);

        musteriler.add("mehmet_bankaci");
        sifreler.add("5555");
        bakiyeler.add(2500.0);

        musteriler.add("ayse_müsteri");
        sifreler.add("9999");
        bakiyeler.add(50.0);

        System.out.println("=== BANKA SISTEMINE HOS GELDINIZ ===");
 

        while (true) {
            // GİRİŞ EKRANI
            if (oturumdakiMusteriIndex == -1) {
                System.out.println("\n--- ANA MENU ---");
                System.out.println("1- Kayıt Ol");
                System.out.println("2- Giriş Yap");
                System.out.println("3- Veritabanını Gör (Admin)"); 
                System.out.println("4- Çıkış");
                System.out.print("Seçiminiz: ");
                int secim;
                try { secim = scanner.nextInt(); } 
                catch (Exception e) { scanner.nextLine(); continue; }
                scanner.nextLine();


                if (secim == 1) kayitOl();
                else if (secim == 2) girisYap();
                else if (secim == 3) veritabaniGor(); 
                else break;
            } else {
                // BANKACILIK İŞLEMLERİ (Login olduktan sonra)
                System.out.println("\nHoş geldiniz, " + musteriler.get(oturumdakiMusteriIndex));
                System.out.println("1- Bakiye Sorgula");
                System.out.println("2- Para Yatır");
                System.out.println("3- Para Çek");
                System.out.println("4- Para Transferi"); 
                System.out.println("5- Oturumu Kapat");
                System.out.print("Seçiminiz: ");
                int islem;
                try { islem = scanner.nextInt(); } 
                catch (Exception e) { scanner.nextLine(); continue; }
                scanner.nextLine();

                if (islem == 1) bakiyeGoster();
                else if (islem == 2) paraYatir();
                else if (islem == 3) paraCek();
                else if (islem == 4) paraTransferi();
                else oturumdakiMusteriIndex = -1;
            }
        }
    }

    // --- VERITABANI GORME ---
    public static void veritabaniGor() {
        System.out.println("\n--- SISTEMDEKI TUM KAYITLAR (In-Memory Repository) ---");
        if (musteriler.isEmpty()) {
            System.out.println("Sistemde henüz kayıtlı kullanıcı yok.");
        } else {
            for (int i = 0; i < musteriler.size(); i++) {
                System.out.println("ID: " + i + 
                                   " | Kullanıcı: " + musteriler.get(i) + 
                                   " | Şifre: " + sifreler.get(i) + 
                                   " | Bakiye: " + bakiyeler.get(i) + " TL");
            }
        }
    }

    // ÖZELLİK 1: KAYIT OLMA
    public static void kayitOl() {
        System.out.print("Yeni Kullanıcı Adı: ");
        String yeniAd = scanner.nextLine();
        if (musteriler.contains(yeniAd)) {
            System.out.println("Bu kullanıcı adı zaten alınmış!");
            return;
        }
        musteriler.add(yeniAd);
        System.out.print("Yeni Şifre: ");
        sifreler.add(scanner.nextLine());
        bakiyeler.add(0.0);
        System.out.println("Kayıt başarılı!");
    }

    // ÖZELLİK 2: LOGİN SİSTEMİ
    public static void girisYap() {
        System.out.print("Kullanıcı Adı: ");
        String ad = scanner.nextLine();
        System.out.print("Şifre: ");
        String sifre = scanner.nextLine();

        for (int i = 0; i < musteriler.size(); i++) {
            if (musteriler.get(i).equals(ad) && sifreler.get(i).equals(sifre)) {
                oturumdakiMusteriIndex = i;
                System.out.println("Giriş başarılı!");
                return;
            }
        }
        System.out.println("Hatalı kullanıcı adı veya şifre!");
    }

    // ÖZELLİK 3: BAKİYE SORGULAMA
    public static void bakiyeGoster() {
        System.out.println("Güncel Bakiyeniz: " + bakiyeler.get(oturumdakiMusteriIndex) + " TL");
    }

    // ÖZELLİK 4: PARA YATIRMA
    public static void paraYatir() {
        System.out.print("Yatırılacak tutar: ");
        double miktar = scanner.nextDouble();
        scanner.nextLine(); 
        bakiyeler.set(oturumdakiMusteriIndex, bakiyeler.get(oturumdakiMusteriIndex) + miktar);
        System.out.println("İşlem başarılı.");
    }

    // ÖZELLİK 5: PARA ÇEKME (Bakiye Kontrollü)
    public static void paraCek() {
        System.out.print("Çekilecek tutar: ");
        double miktar = scanner.nextDouble();
        scanner.nextLine();
        if (bakiyeler.get(oturumdakiMusteriIndex) >= miktar) {
            bakiyeler.set(oturumdakiMusteriIndex, bakiyeler.get(oturumdakiMusteriIndex) - miktar);
            System.out.println("İşlem başarılı.");
        } else {
            System.out.println("Yetersiz bakiye!");
        }
    }
    
    // ÖZELLİK 6 : TRANSFER (HAVALE)
    public static void paraTransferi() {
        System.out.print("Para göndermek istediğiniz Kullanıcı Adı: ");
        String aliciAd = scanner.nextLine();
        
        int aliciIndex = musteriler.indexOf(aliciAd);
        
        if (aliciIndex != -1 && aliciIndex != oturumdakiMusteriIndex) {
            System.out.print("Gönderilecek miktar: ");
            double miktar = scanner.nextDouble();
            scanner.nextLine();
            
            if (bakiyeler.get(oturumdakiMusteriIndex) >= miktar) {
                // Gönderen bakiyesini düşür
                bakiyeler.set(oturumdakiMusteriIndex, bakiyeler.get(oturumdakiMusteriIndex) - miktar);
                // Alıcı bakiyesini artır
                bakiyeler.set(aliciIndex, bakiyeler.get(aliciIndex) + miktar);
                System.out.println("Transfer başarıyla gerçekleşti!");
            } else {
                System.out.println("Yetersiz bakiye!");
            }
        } else {
            System.out.println("Geçersiz alıcı!");
        }
    }

}