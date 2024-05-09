Bu projede, mobil programlama kullanılarak kelime tabanlı bir oyun geliştirilmesi hedeflenmektedir. Oyun, iki
kişilik olacak ve dinamik özellikler içerecektir. Oyunun alt yapısının sunucu istemci mimarisi kullanılarak
tasarlanması ve platform üzerinden birden fazla oyunun aynı anda oynanabilmesi beklenmektedir.
Amaç:
1. Mobil programlama konusunda bilgi ve beceri kazanılması
2. Mobil programlama aracılığıyla oyun geliştirme ve uygulama oluşturma becerisinin geliştirilmesi.
3. Dinamik özelliklere sahip bir program geliştirebilme
Programlama Dili: Java, Kotlin, Flutter, React Native, Swift vb.
Mobil Programlama:
1. Mobil programlama kodları Android veya IOS için geliştirilecektir. Her ikisinden birisi için
geliştirme işlemi yapabilirsiniz.
2. Kodlama için herhangi bir programlama dili kullanılabilir.
3. Geliştirilecek uygulama kelime tabanlı bir oyundur. Bundan dolayı oyunda kelime listesinin kullanılması
gereklidir..
4. Oyun üyelikler üzerine ilerleyeceği için, oyuncuların oyuna kayıt olması, kayıtlı oyuncuların ise kullanıcı
adı ve şifresi ile oyuna giriş yapabilmesi gerekmektedir.
Uygulama Yapısı:
1. Oyun iki kişinin karşılıklı oynayabileceği şekilde tasarlanacaktır.
2. Sunucu-istemci mantığı kullanılacak ve oyun genel olarak sunucu üzerinde ilerleyecektir. Oyun
içerisinde daha sonra bahsedilecek oyun odaları oluşturulacaktır. Oyuncular bu odalara giriş yaparak
rakipleri ile eşleşebilecektir. Bahsedilen odalar oyuncular oyuna girdiğinde hazır halde bulunmalıdır.
Aksi takdirde oyuncular yalnızca uygulamaya giriş yapabilecektir.
3. İki tür oyun seçeneği sunulacak; birinde rastgele harf sabiti üretilerek, diğerinde ise harf sabiti
kısıtlaması olmadan oyuncudan direkt kelime istenecektir.
4. Minimum 4 harfli maksimum 7 harfli olacak şekilde oyun kanalları tasarlanacaktır.
5. Kanallar tasarlanırken öncelikle oyun türüne göre, daha sonra kelime sayısına göre bölünecektir.
6. Oyuncu mobil uygulamaya giriş yaptığında, üye değilse üye olma seçeneğine tıklayarak üyelik
işlemlerini yapmalıdır. Üyeliği varsa kullanıcı adı ve şifresi ile giriş yapacaktır.
7. Oyuncu oyun türünü seçecek, daha sonra ise kelime sayısını seçerek ilgili kanala giriş yapacaktır. Aynı
kanalda bulunan kullanıcılar birbirlerini görebilecekler.
8. Kanalda oyuncular birbirlerini oyuncu durum tiplerine (aktif, oyunda) göre görüntüleyebilecektir.
9. Kullanıcı aktif oyunculardan yalnızca birisine istekte bulunabilecektir. Diğer oyuncu kabul ederse oyun
başlayacaktır. Diğer oyuncu 10sn içerisinde cevap vermezse veya isteği reddederse oyuncuya red
mesajı gelecektir. Oyuncu red mesajı aldığında aynı kullanıcıya veya farklı bir kullanıcıya tekrardan
oyun isteğinde bulunabilecektir. Aynı anda birden fazla oyuncuya istek atmayacaktır. Bütün bu istek
mesajları ve sayma işlemleri her iki kullanıcıda da gösterilecektir.
10. İsteği kabul etmesi durumunda oyuncular kanal özelliğine göre (örneğin Harf sabiti olmadan 5 harfi
oda olsun) kelime giriş ekranı açılacaktır.
11. Açılan ekranda kullanıcılar rakiplerine soracağı kelimeleri girecektir ve daha sonrasında her iki oyuncu
da birbirlerine sordukları kelimeyi tahmin etmeye çalışacaktır. Örneğin yukarıdaki gibi oyuncu 5 harfli
bir oyun seçtiyse 5 harfli bir kelime girecek ve ardından "Onayla" butonuna basacaktır.
12. Oyuncuların kelime girmesi için 1 dakika süreleri bulunmaktadır. Bu süre sağ üst köşede geri sayım
olarak gösterilmelidir.
13. Oyunculardan birisi rakibinin tahmin edeceği kelimeyi 1 dakika içinde girip onaylarsa, diğeri ise girip
onay butonuna basamaz ise oyunu kaybedecektir.
14. Her ikiside 1 dakika içinde rakibinin tahmin etmesi gereken kelimeyi onaylamaz ise kelime yazma
ekranı tekrar açılacak kelime girmesi için tekrardan 1 dakika süre verilecektir.
15. Örnek kelime girme işlemi aşağıdaki gibidir.
16. İki oyuncudan da tahmin edilecek kelimelerin onayı alındıktan sonra, oyun eş zamanlı olarak
başlayacaktır.
17. Oyun sırasında her iki oyuncuya da harf sayısı kadar tahmin edecekleri alanlar oluşturulacaktır.
Örneğin, 5 harfli bir oyunda ise 5 kez tahmin hakkı olacak şekilde alanlar oluşturulacaktır. Örnek
gösterim aşağıdaki gibidir.
18. Oyuncular açılan ekranda rakibinin sorduğu kelimeyi tahmin etmeye çalışacaktır. Oyunda bulunan
satır sayısı kadar (aynı zamanda kelime sayısı) deneme şansı bulunan oyuncular, her bir satırda
tahmin ettiği kelimeyi oluşturduktan sonra enter tuşuna basacaktır.
19. Girilen kelimenin geçerli bir kelime olması gereklidir. Geçerli bir kelime değil ise oyuncuya geçerli bir
kelime olmadığı bildirilecek ve geçerli bir kelime girmesi istenecektir.
20. Geçerli bir kelime girmesi durumunda tahmin kelimesi, rakibin sorduğu kelime ile karşılaştırılacaktır.
Karşılaştırma sonucunda kelime doğru ise oyun bitecektir ve oyuncu rakibinden önce tahmin etti ise
oyunu kazanacaktır. Eğer kelime doğru değil ise harfler karşılaştırılacaktır. Tahmin edilen kelimedeki
harfler rakibin sorduğu kelime içinde varsa; doğru yerde bulunan harfler, yeşil yanlış yerde bulunan
harfler ise sarı renkte gösterilecektir. Eğer harf yok ise gri renkte gösterilecektir. Örnek tahmin
işlemleri aşağıda gösterilmiştir.
21. Oyun sırasında 1 dakika boyunca deneme yapılmadığı zaman uyarı gönderilecektir ve sağ üstte 10
saniye sayım başlatılacaktır. Sayım sonucunda uyarılan kişi mağlup sayılacaktır. Eğer süre bitmeden
önce oyuna devam ederse, süre kaldırılacaktır. Tekrar aynı durumun olması halinde süre sayımı tekrar
başlatılacaktır.
22. Oyun sırasında oyundan çıkılması durumunda kullanıcıya uyarı olarak “Oyundan çıkmanız halinde
oyunu kaybedeceksiniz. Çıkmak istiyorsanız onay butonuna basınız” uyarısı verilecektir ve uyarıda
onayla, red butonları bulunacaktır.
23. Oyun sırasında sunucu ile bağlantı kopma durumunda 10 saniye geri sayım başlatılacaktır. Sayım
sonucunda uyarılan kişi mağlup sayılacaktır.
24. Oyuncular oyun başladıktan sonra (tahmin aşaması) rakibi gör butonuna basarak rakibinin oyundaki
ekranını görüntüleyebilecektir.
25. Oyun bir oyuncu sorulan kelimeyi doğru tahmin edene kadar devam edecektir. Doğru tahmin eden
oyuncu oyunu kazanacaktır. Örnek sonuçlar aşağıdaki gibidir.
26. Eğer bir oyuncu sorulan kelimeyi doğru tahmin edemedi ve tahmin hakkı kalmadı ise diğer oyuncuya
kalan her tahmin hakkı için 10 saniye süre verilecektir. Oyuncu bu süre içerisinde doğru şekilde
tahmin ederse oyunu kazanacaktır. Bu süre içerisinde bir tahmin girmemesi durumunda sistem
tarafından, kelime listesi içerisinde bulunan kelimelerden rastgele bir kelime seçilip onaylanacaktır. Bu
rastgele girilen kelimede benzer şekilde kontrol edilip harf durumlarına göre renklendirilecektir.
27. Eğer iki oyuncuda doğru tahmin edemedi ise en son satırda yeşil olan harfler 10 ile çarpılacak ve
toplanacaktır. Benzer şekilde sarı olan harfler ise 5 ile çarpılarak toplanacaktır. Ayrıca oyunun başında
rakibe sorulacak kelimenin girildiği ekranda kelimenin girilmesi istenen dakikadan geriye kalan saniye
de puanlamaya dahil edilerek toplam puan elde edilecektir. En son toplam puan karşılaştırılacak ve
yüksek olan oyunu kazanacaktır. Eğer eşit ise oyun berabere bitecektir. Örnek gösterim aşağıdaki
gibidir.
Kullanıcı 1 için puan hesabı 3 tane yeşil var bundan dolayı 10*3 = 30 puan
1 tane sarı var bundan dolayı 5*1 = 5 puan
Kelime yazma ekranından kalan saniye 30 saniye
Kullanıcı 1 için toplam puan 30 + 5 + 30 = 65 puandır
Kullanıcı 2 için puan hesabı 4 tane yeşil var bundan dolayı 10*4 = 40 puan
0 tane sarı var bundan dolayı 5*0 = 0 puan
Kelime yazma ekranından kalan saniye 10 saniye
Kullanıcı 2 için toplam puan 40 + 0 + 10 = 50 puandır
Oyun galibi 65 >50 olduğu için oyunu kullanıcı 1 kazanacaktır.
28. Oyun bitiminde her iki oyuncunun sonuç ekranları aynı ekran üzerinde oyunculara gösterilecek ve
galibiyet durumları ekranda verilecektir. Bu ekranda oyuncuların tahmin ekranları, tahmin etme
süreleri ve puanları (her bir puan kalemi de belirtilerek) gösterilecektir.
29. Oyun sonucunda açılan ekranda sağ üst köşede düello butonu bulunacaktır. Oyunculardan birisinin
düello butonuna basması durumunda karşı oyuncuya düello isteği düşecektir. Eğer onaylaması
durumunda oyun tekrardan açılacak ve yeni bir oyun başlatılacaktır. Oyuna düello ile devam edilmesi
durumunda, oyun sonunda önceki oyun skorları da dahil edilerek sonuçlar gösterilecektir (Puan
kalemine önceki oyun sonucu da eklenecektir.). Düello isteğinde yapılacak süre ve işlemler, oyuna
başlama isteğindeki süre ve işlemler ile aynı olacaktır.
30. Rakibin düelloyu reddetmesi durumunda ise oyun bitecek ve oyuncu bulunduğu kanalda aktif
durumda gözükecektir. Oyuncular kanalda rakip listesine geri dönecek ve yeni oyun için rakip
oyunculara istekte bulunabilecektir. Bu sırada farklı kanallar arasında geçiş yaparak diğer kanallardaki
oyuncuları görebilir ve istekte bulunarak oyun oynayabilecektir.
31. Rakiplerden birisi oyundan çıkarsa diğer rakip yine düello isteğinde bulunabilecektir. Rakiplerden her
ikisi de oyundan çıktı ise düello isteği yapılmayacaktır. Oyuncular oyun bitiminden sonra 30 sn içinde
herhangi bir işlem yapmazsa oyundan otomatik olarak çıkarak kanala geçecektir.
İsterler:
1. İstemci - Sunucu mantığının oluşturulması
2. Sunucu üzerinde kanalların açılması
3. Oyun giriş ekranı ve kanal seçimi yapılması
4. Oyun başlangıç ekranında istenen durumların yapılması
5. Oyun için kelime girilmesi ve kontrol işlemleri
6. Oyun deneme işlemlerinin doğru şekilde yapılması
7. Bağlantı kopması, oyundan çıkma durumları ve oyun süresinin kontrol edilmesi
8. Oyun sonucunun belirlenmesi ve puan hesaplamasının yapılması
9. Oyun sonucunda her iki oyunucuya sonuçların gösterilmesi ve düello isteği gönderilmesi
10. Kanala geri dönüş ve yeniden oyun isteğinin gönderilmesi
11. Oyuncunun ekranının diğer oyuncuda görüntülenmesi
