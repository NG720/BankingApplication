# BankingApplication - Banka Simülasyonu

Bu proje, temel Java mantığı kullanılarak geliştirilmiş bir **In-Memory Bankacılık Uygulamasıdır**.
Veriler bir veritabanı yerine çalışma zamanında RAM (ArrayList) üzerinde tutulmaktadır.

## 🚀 Özellikler

- **Müşteri Kaydı:** Yeni kullanıcılar sisteme eklenebilir. (Duplicate kontrolü yapılır.)
- **Login Sistemi:** Kullanıcı adı ve şifre kontrolü ile güvenli giriş. (Hatalı girişte uyarı verir.)
- **Veritabanı İzleme:** Admin yetkisiyle tüm kayıtlı kullanıcıları ve bakiyeleri görebilme.
- **Bakiye Sorgulama:** Anlık bakiye görüntüleme.
- **Para Yatırma:** Hesaba para yükleme işlemi.
- **Para Çekme:** Bakiye kontrollü para çekme işlemi.
- **Para Transferi (Havale):** Kullanıcılar arası anlık para transferi.

## 🛠️ Nasıl Çalıştırılır?

1. Java JDK'nın sisteminizde kurulu olduğundan emin olun.
2. `BankingApplication.java` dosyasını indirin.
3. Terminalde şu komutları çalıştırın:

```bash
   javac BankingApplication.java
   java BankingApplication
```

## 🧪 Test Kullanıcıları

| Kullanıcı Adı | Şifre | Bakiye |
|---------------|-------|--------|
| admin | 1234 | 1000 TL |
| mehmet_bankaci | 5555 | 2500 TL |
| ayse_müsteri | 9999 | 50 TL |