# 🌐 Anatomy of an HTTP Request — HTTP İsteğinin Anatomisi

---

## 📌 HTTP Nedir?

**HTTP (HyperText Transfer Protocol)**, istemci (client) ile sunucu (server) arasındaki iletişimi sağlayan, uygulama katmanında çalışan bir protokoldür. Web'in temel iletişim dilidir.

- **Durumsuz (Stateless):** Her istek birbirinden bağımsızdır.
- **İstek-Yanıt (Request-Response):** İstemci istek gönderir, sunucu yanıt döner.
- **TCP tabanlı:** Güvenilir iletim için TCP üzerinde çalışır (HTTP/3'te QUIC).

---

## 🔬 HTTP İsteğinin Yapısı

Bir HTTP isteği 3 ana bölümden oluşur:

```
┌──────────────────────────────────────────┐
│            REQUEST LINE                  │  ← 1. Satır
├──────────────────────────────────────────┤
│              HEADERS                     │  ← Başlıklar
├──────────────────────────────────────────┤
│            (BLANK LINE)                  │  ← Boş satır (zorunlu)
├──────────────────────────────────────────┤
│               BODY                       │  ← Gövde (opsiyonel)
└──────────────────────────────────────────┘
```

---

## 1️⃣ Request Line (İstek Satırı)

İlk satır üç bileşenden oluşur:

```
METHOD  /path/to/resource  HTTP/VERSION
```

**Örnek:**
```http
GET /api/users/42 HTTP/1.1
```

### 🔹 HTTP Metodları (Methods)

| Method   | Açıklama                                      | Body? | Idempotent? |
|----------|-----------------------------------------------|-------|-------------|
| `GET`    | Kaynak getir                                  | ❌    | ✅          |
| `POST`   | Yeni kaynak oluştur                           | ✅    | ❌          |
| `PUT`    | Kaynağı tamamen güncelle / oluştur            | ✅    | ✅          |
| `PATCH`  | Kaynağı kısmen güncelle                       | ✅    | ❌          |
| `DELETE` | Kaynağı sil                                   | ❌*   | ✅          |
| `HEAD`   | Yalnızca başlıkları getir (body yok)          | ❌    | ✅          |
| `OPTIONS`| Desteklenen metodları sorgula                 | ❌    | ✅          |
| `CONNECT`| Tünel bağlantısı kur (HTTPS proxy)            | ❌    | ❌          |
| `TRACE`  | Döngüsel test (debug amaçlı)                  | ❌    | ✅          |

> \* DELETE isteğinde body teknik olarak gönderilebilir, ancak genellikle kullanılmaz.

### 🔹 URL / URI Yapısı

```
https://api.example.com:8080/v1/users?role=admin&active=true#section2
│       │               │    │        │                       │
scheme  host            port path     query string            fragment
```

| Bileşen          | Açıklama                                      |
|------------------|-----------------------------------------------|
| `scheme`         | Protokol: `http`, `https`, `ftp`              |
| `host`           | Sunucu adresi veya IP                         |
| `port`           | Bağlantı noktası (HTTP: 80, HTTPS: 443)       |
| `path`           | Kaynak yolu                                   |
| `query string`   | `?key=value` biçiminde ek parametreler        |
| `fragment`       | Sayfa içi konum (sunucuya gitmez)             |

### 🔹 HTTP Versiyonları

| Versiyon   | Özellikler                                                         |
|------------|--------------------------------------------------------------------|
| `HTTP/1.0` | Her istek için ayrı bağlantı                                       |
| `HTTP/1.1` | Persistent connection, Host header zorunlu, pipelining             |
| `HTTP/2`   | Multiplexing, header compression (HPACK), binary framing           |
| `HTTP/3`   | QUIC üzerinde çalışır, UDP tabanlı, daha az gecikme                |

---

## 2️⃣ Headers (Başlıklar)

Başlıklar `Key: Value` formatında yazılır ve istek hakkında meta bilgi taşır.

```http
Host: api.example.com
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)
Accept: application/json
Accept-Language: tr-TR, en-US
Accept-Encoding: gzip, deflate, br
Content-Type: application/json
Content-Length: 87
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Connection: keep-alive
Cache-Control: no-cache
```

### 🔹 Yaygın Request Header'ları

| Header               | Açıklama                                                    |
|----------------------|-------------------------------------------------------------|
| `Host`               | Hedef sunucunun alan adı **(HTTP/1.1'de zorunlu)**         |
| `User-Agent`         | İstemci uygulama/tarayıcı bilgisi                          |
| `Accept`             | İstemcinin kabul ettiği içerik tipleri                     |
| `Accept-Language`    | Tercih edilen dil(ler)                                      |
| `Accept-Encoding`    | Desteklenen sıkıştırma algoritmaları                       |
| `Content-Type`       | Body içeriğinin tipi                                        |
| `Content-Length`     | Body boyutu (byte cinsinden)                               |
| `Authorization`      | Kimlik doğrulama bilgisi (Basic, Bearer, Digest…)          |
| `Cookie`             | Sunucudan daha önce alınmış cookie'ler                     |
| `Referer`            | İsteğin yapıldığı önceki sayfa URL'si                      |
| `Origin`             | CORS için kaynak domain bilgisi                            |
| `Cache-Control`      | Önbellekleme davranışını kontrol eder                      |
| `Connection`         | Bağlantının `keep-alive` veya `close` olacağını belirtir   |
| `If-Modified-Since`  | Koşullu GET: Belirtilen tarihten sonra değiştiyse getir    |
| `ETag` / `If-None-Match` | Önbellek doğrulama için                              |

### 🔹 Content-Type Değerleri

| Content-Type                        | Kullanım Alanı                  |
|-------------------------------------|---------------------------------|
| `application/json`                  | JSON veri                       |
| `application/x-www-form-urlencoded` | HTML form gönderimi             |
| `multipart/form-data`               | Dosya yükleme (form)            |
| `text/html`                         | HTML içerik                     |
| `text/plain`                        | Düz metin                       |
| `application/xml`                   | XML veri                        |
| `application/octet-stream`          | Binary veri                     |

---

## 3️⃣ Body (Gövde)

İstek gövdesi, sunucuya gönderilecek veriyi içerir. `GET` ve `HEAD` isteklerinde genellikle bulunmaz.

### 🔹 JSON Body (En yaygın kullanım)

```http
POST /api/users HTTP/1.1
Host: api.example.com
Content-Type: application/json
Content-Length: 87

{
  "name": "Ahmet Yılmaz",
  "email": "ahmet@example.com",
  "role": "admin"
}
```

### 🔹 Form URL Encoded

```http
POST /login HTTP/1.1
Content-Type: application/x-www-form-urlencoded

username=ahmet&password=s3cur3pass
```

### 🔹 Multipart Form Data (Dosya Yükleme)

```http
POST /upload HTTP/1.1
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxk

------WebKitFormBoundary7MA4YWxk
Content-Disposition: form-data; name="file"; filename="photo.jpg"
Content-Type: image/jpeg

[binary data...]
------WebKitFormBoundary7MA4YWxk--
```

---

## 📨 Tam Bir HTTP İsteği Örneği

```http
POST /api/v1/auth/login HTTP/1.1
Host: api.example.com
User-Agent: MyApp/2.1.0 (Android 14)
Accept: application/json
Content-Type: application/json
Content-Length: 62
Authorization: Basic dXNlcjpwYXNz
Connection: keep-alive

{
  "email": "ahmet@example.com",
  "password": "gizli_sifre_123"
}
```

---

## 📩 HTTP Response (Yanıt) Yapısı

İsteğe karşılık gelen yanıt da benzer bir yapıya sahiptir:

```
┌──────────────────────────────────────────┐
│           STATUS LINE                    │  ← HTTP/1.1 200 OK
├──────────────────────────────────────────┤
│             HEADERS                      │
├──────────────────────────────────────────┤
│           (BLANK LINE)                   │
├──────────────────────────────────────────┤
│              BODY                        │
└──────────────────────────────────────────┘
```

### 🔹 HTTP Durum Kodları (Status Codes)

| Kod Aralığı | Kategori       | Yaygın Örnekler                                                              |
|-------------|----------------|------------------------------------------------------------------------------|
| `1xx`       | Bilgi          | `100 Continue`, `101 Switching Protocols`                                    |
| `2xx`       | Başarı         | `200 OK`, `201 Created`, `204 No Content`                                    |
| `3xx`       | Yönlendirme    | `301 Moved Permanently`, `302 Found`, `304 Not Modified`                     |
| `4xx`       | İstemci Hatası | `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `429 Too Many Requests` |
| `5xx`       | Sunucu Hatası  | `500 Internal Server Error`, `502 Bad Gateway`, `503 Service Unavailable`    |

---

## 🔒 HTTPS ve TLS El Sıkışması

HTTPS, HTTP'nin TLS (Transport Layer Security) üzerinden şifrelenmiş halidir.

```
İstemci                              Sunucu
   │                                    │
   │──── Client Hello ────────────────► │  (TLS sürümü, cipher suites)
   │◄─── Server Hello ────────────────  │  (Seçilen cipher, sertifika)
   │◄─── Certificate ─────────────────  │  (SSL/TLS sertifikası)
   │──── Key Exchange ────────────────► │  (Pre-master secret)
   │◄─── Finished ────────────────────  │
   │──── Finished ────────────────────► │
   │                                    │
   │════ Şifreli HTTP İletişimi ═══════ │
```

---

## 🚀 HTTP/2 ve HTTP/3 Farkları

### HTTP/2 Avantajları
- **Multiplexing:** Tek bağlantıda paralel istek/yanıt
- **Header Compression:** HPACK ile başlık sıkıştırma
- **Server Push:** Sunucu, istemci sormadan kaynak gönderebilir
- **Binary Protocol:** Metin yerine binary framing

### HTTP/3 Avantajları
- **QUIC protokolü:** TCP yerine UDP kullanır
- **0-RTT:** Tanınan sunuculara sıfır el sıkışma gecikmesi
- **Daha iyi mobil performans:** Ağ değişimlerinde bağlantı kesilmez

---

## 🍪 Cookie Mekanizması

```http
# Sunucu, cookie'yi yanıtta gönderir:
Set-Cookie: session_id=abc123; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=3600

# İstemci, sonraki isteklerde cookie'yi gönderir:
Cookie: session_id=abc123
```

| Özellik    | Açıklama                                              |
|------------|-------------------------------------------------------|
| `HttpOnly` | JavaScript ile erişilemez (XSS koruması)              |
| `Secure`   | Yalnızca HTTPS üzerinden gönderilir                   |
| `SameSite` | CSRF koruması: `Strict`, `Lax`, `None`                |
| `Max-Age`  | Cookie'nin kaç saniye geçerli olacağı                 |
| `Path`     | Cookie'nin geçerli olduğu URL yolu                    |
| `Domain`   | Cookie'nin geçerli olduğu alan adı                    |

---

## 🔁 CORS (Cross-Origin Resource Sharing)

Farklı origin'den yapılan istekler için tarayıcı güvenlik mekanizması.

```http
# Preflight İsteği (OPTIONS):
OPTIONS /api/data HTTP/1.1
Origin: https://frontend.example.com
Access-Control-Request-Method: POST
Access-Control-Request-Headers: Content-Type, Authorization

# Sunucu Yanıtı:
Access-Control-Allow-Origin: https://frontend.example.com
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
Access-Control-Allow-Headers: Content-Type, Authorization
Access-Control-Max-Age: 86400
```

---

## 📊 Özet Şema

```
                    HTTP REQUEST
┌─────────────────────────────────────────────────┐
│  REQUEST LINE                                   │
│  ┌─────────┐  ┌────────────────┐  ┌──────────┐ │
│  │ METHOD  │  │      URL       │  │ VERSION  │ │
│  │  POST   │  │ /api/users/42  │  │ HTTP/1.1 │ │
│  └─────────┘  └────────────────┘  └──────────┘ │
├─────────────────────────────────────────────────┤
│  HEADERS                                        │
│  Host: api.example.com                          │
│  Content-Type: application/json                 │
│  Authorization: Bearer <token>                  │
│  ...                                            │
├─────────────────────────────────────────────────┤
│  (blank line)                                   │
├─────────────────────────────────────────────────┤
│  BODY                                           │
│  { "name": "Ahmet", "email": "..." }            │
└─────────────────────────────────────────────────┘
```

---

## 📚 Kaynaklar

- [MDN Web Docs — HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP)
- [RFC 9110 — HTTP Semantics](https://www.rfc-editor.org/rfc/rfc9110)
- [RFC 9114 — HTTP/3](https://www.rfc-editor.org/rfc/rfc9114)
- [HTTP/2 Explained](https://http2.github.io/)

---

> 📝 **Not:** Bu döküman HTTP/1.1 temel alınarak hazırlanmış olup HTTP/2 ve HTTP/3'e ait farklılıklar ilgili bölümlerde ele alınmıştır.
