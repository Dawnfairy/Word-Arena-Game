# Word Arena Kelime Oyunu

**Hazırlayanlar:** Fatma Nur Kurt, Tayyib Okur  

---

## I. Özet

Bu çalışma, Kocaeli Üniversitesi Bilgisayar Mühendisliği Bölümü Yazılım Laboratuvarı II dersi kapsamında geliştirilen, iki oyunculu, kelime tabanlı bir mobil oyunun tasarım ve implementasyon sürecini kapsamaktadır. Projenin ismi **"FATAY Arama Motoru"** olarak belirlenmiş olsa da; esas olarak oyuncuların rastgele veya belirli bir harf seti kullanarak kelimeler oluşturarak rekabet ettikleri bir oyun geliştirilmiştir.  
Amaç, mobil programlama, sunucu-istemci mimarisi ve dinamik uygulama geliştirme becerilerini kazandırmaktır.

---

## II. Giriş

Mobil platformlar için tasarlanan bu oyun;  
- **Kayıt, Oturum Açma ve Oyun Odaları:**  
  Oyuncuların kullanıcı kaydı yapmaları, oturum açmaları ve belirlenen oyun odalarına katılarak rakipleriyle eşleşmeleri gerekmektedir.
- **Oyun Mekaniği:**  
  Oyunun ana mekanığı, oyuncuların belirli harf sayısına sahip odalarda kelime tahmin yarışı yapması üzerine kuruludur.  
  - İki oyuncu aynı odada aktif hale geldikten sonra, birbirlerine oyun başlatma isteği gönderirler.  
  - Oyuna başlamadan önce, rakiplerin bilmesi gereken gizli kelime girilir; kelimeyi girmeyen oyuncu oyunu kaybeder.
- **Tahmin Süreci ve Puanlama:**  
  Oyuncular, kelime tahminleri için belirli süre (örneğin, 1 dakika) içinde hamle yaparlar.  
  - Doğru harf ve doğru konumda olanlar yeşil (10 puan), doğru harf fakat yanlış konumda olanlar sarı (5 puan) olarak değerlendirilir.
  - Oyun sonunda, kalan süre ve tahmin doğrulukları üzerinden ek puanlar eklenerek toplam skor hesaplanır.
- **Ek Senaryolar:**  
  Bağlantı kopma, oyundan çıkma istekleri, duello teklifleri gibi durumlar da uygulama kapsamında ele alınmıştır.

---

## III. Kullanılan Teknolojiler

- **Mobil Geliştirme:**  
  - **Kotlin:** Oyun uygulamasının ana programlama dili (backend kısmı da Kotlin ile yazılmıştır).  
  - **Jetpack Compose:** Modern, deklaratif UI tasarımı için kullanılmıştır.
- **Sunucu-İstemci Modeli:**  
  - **Firebase:** Kullanıcı kaydı, oturum açma ve veri saklama işlemleri için kullanılmıştır.
- **Alternatif Platformlar:**  
  Proje raporunda Java, Flutter, React Native ve Swift gibi farklı dillerin avantajları değerlendirilmiş; ancak uygulamanın geliştirilmesi Android Studio ve Kotlin ile gerçekleştirilmiştir.

---

## IV. Proje İşleyişi ve Akışı

1. **Oyun Başlangıcı ve Oda Yönetimi:**  
   - Oyuncular, uygulamaya giriş yaptıktan sonra 4 farklı kelime uzunluğu (4, 5, 6, 7 harf) seçeneğinden birini tercih eder.  
   - Seçilen kelime uzunluğuna göre oluşturulan oyun odasına oyuncular otomatik olarak yönlendirilir.
   - Odada her iki oyuncu da aktif hale geldikten sonra, karşılıklı olarak oyun başlatma isteği gönderilir.
  
2. **Gizli Kelime ve Tahmin Süreci:**  
   - Oyun başlamadan önce, rakibin bilmesi gereken gizli kelime girilir.  
   - Her iki oyuncuya da kelime tahmin etme hakkı tanınır; tahmin süresi 1 dakika olarak belirlenmiştir.  
   - Eğer süre dolarsa, ek 10 saniyelik süre verilir; hala kelime girilmezse, rakip oyuncu kazanır.
  
3. **Puanlama ve Oyun Sonu:**  
   - Her tahmin, doğru harflerin konumuna göre yeşil (10 puan) veya sarı (5 puan) olarak değerlendirilir.  
   - Oyun sonunda, oyuncuların toplam puanları hesaplanır ve yüksek puan alan oyuncu kazanır.
   - Sonuç ekranı, oyuncuların tahmin süreleri, puanları ve genel performanslarını detaylı olarak gösterir.
   - Duello seçeneğiyle, oyuncular birbirlerine yeniden oyun başlatma isteği gönderebilir.

---

## V. Geliştirme Ortamı

1. **IDE ve Araçlar:**  
   - Proje, **Android Studio** kullanılarak geliştirilmiştir.
   - Kotlin dilinde yazılmış olup, Jetpack Compose ile modern bir kullanıcı arayüzü oluşturulmuştur.
   - Firebase ile sunucu-istemci işlemleri sağlanmıştır.
  
2. **Projeyi Açma ve Çalıştırma:**  
   - Android Studio’yu açın ve proje klasörünü "File > Open" seçeneği ile yükleyin.
   - Gerekli bağımlılıkların (Firebase, Jetpack Compose, vb.) yüklü olduğundan emin olun.
   - Projeyi derleyip çalıştırmak için "Run" veya "Debug" seçeneklerini kullanın.
   - Uygulama, test cihazınızda veya emülatörde otomatik olarak başlatılacaktır.

---

## VI. Kullanım

- **Oyun Başlatma:**  
  - Uygulamayı açtıktan sonra kullanıcı, ana ekranda yer alan oyun seçeneklerinden (kelime uzunluğu tercihleri) birini seçer.
  - Seçilen oyun odasında, oyuncular birbirlerine istek göndererek oyunu başlatır.
  
- **Oyun Süreci:**  
  - Gizli kelime girilmesi, kelime tahminleri ve puanlama işlemleri kullanıcı tarafından belirlenen süreler dahilinde gerçekleşir.
  - Oyuncuların her hamlesi, arayüzde görsel geri bildirim (renk kodlaması) ile desteklenir.
  
- **Sonuç ve Duello:**  
  - Oyun sonunda, skorlar ekranda detaylı olarak sunulur.
  - Oyuncular, duello isteği göndererek yeni bir oyun başlatabilir veya ana menüye dönebilir.

---

## VII. İletişim

Proje ile ilgili sorular, geri bildirimler veya katkı talepleri için aşağıdaki kişilerle iletişime geçebilirsiniz:

  Fatma Nur Kurt - kurtfatmanur8@gmail.com
  Tayyib Okur - ultratayyib@gmail.com
