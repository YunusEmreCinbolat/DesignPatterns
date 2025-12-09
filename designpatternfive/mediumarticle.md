# Pizza Sipariş Sistemi Üzerinden Builder ve Singleton Tasarım Desenlerini Derinlemesine İnceleme
Modern yazılım projelerinde nesne yapılarının doğru tasarlanması, sürdürülebilir ve esnek kodun temelidir.
Özellikle mutfak yönetimi, sipariş akışları ve parametre yoğun modellerde tasarım desenleri büyük rol oynar.
Bu makalede, demo bir **pizza sipariş sistemi** üzerinden iki kritik tasarım desenini ele alıyoruz:
- **Builder Pattern** → Pizza oluşturma sürecini modüler ve güvenli hâle getirmek
- **Singleton Pattern** → Sipariş yönetimini tek bir global noktada toplamak
Projedeki backend Spring Boot, frontend ise Angular ile geliştirilmiştir.
Kodlar üzerinden hem teorik hem pratik uygulamayı detaylı şekilde inceleyeceğiz.
---
## 1. Problem Tanımı — Neden Tasarım Deseni Kullanıyoruz?
Bir pizza siparişi şu parametrelerden oluşabilir:
- Boyut (SMALL, MEDIUM, LARGE)
- Hamur türü (THIN, REGULAR, THICK)
- Sos türü (TOMATO, BARBECUE, WHITE)
- Toppings listesi (mantar, sucuk, mısır, vb.)
- Spicy seçeneği (true/false)
Bu parametrelerin farklı kombinasyonları milyonlarca farklı pizza üretir.
Eğer bu obje doğrudan bir constructor üzerinden oluşturulmaya çalışılsaydı:
```java
new Pizza(Size.MEDIUM, DoughType.REGULAR, SauceType.TOMATO, toppings, false)
```
bu kullanım şu dezavantajları doğururdu:
- Parametre sıralama hataları
- Okunaksız ve bakım zorluğu
- Çok parametreli constructor karmaşası
- Nesne inşa sürecinin yönetilememesi
İşte tam burada **Builder Pattern** devreye girer.
---
## 2. Builder Pattern — Pizza Oluşturmanın En Temiz Yolu
Builder Pattern’ın temel amacı:
- Adım adım, okunabilir şekilde nesne oluşturmak
- Gereksiz constructor yükünü ortadan kaldırmak
- Kullanıcıya güvenli ve yönlendirmeli bir oluşturma deneyimi sunmak
Projedeki **Pizza.Builder** sınıfı bu mantığı mükemmel şekilde uygular.
---
### 2.1 Pizza Sınıfının Genel Yapısı
Backend modelimiz şöyle başlıyor:
```java
public class Pizza {
    private final Size size;
    private final DoughType doughType;
    private final SauceType sauceType;
    private final List<String> toppings;
    private final boolean spicy;
```
Tüm özellikler **immutable** — yani değiştirilemez — şekilde tanımlanmıştır.
Bu, Pizza nesnesinin güvenli (thread-safe) olmasını sağlar.
Nesnenin nasıl oluşturulduğuna bakalım:
```java
private Pizza(Builder builder) {
    this.size = builder.size;
    this.doughType = builder.doughType;
    this.sauceType = builder.sauceType;
    this.toppings = builder.toppings;
    this.spicy = builder.spicy;
}
```
Pizza yalnızca Builder üzerinden oluşturulabilir.  
Bu da nesne inşasını tamamen kontrol altında tutar.
---
## 2.2 Pizza.Builder — Adım Adım İnşa Süreci
Builder sınıfının omurgası:
```java
public static class Builder {
    private Size size;
    private DoughType doughType;
    private SauceType sauceType;
    private List<String> toppings = new ArrayList<>();
    private boolean spicy;
```
Builder’ın tüm metotları *fluency* sağlar:
```java
public Builder size(Size size) {
    this.size = size;
    return this;
}
```
Aynı şekilde:
```java
public Builder addTopping(String topping) {
    this.toppings.add(topping);
    return this;
}
```
En kritik nokta, build() metodu:
```java
public Pizza build() {
    return new Pizza(this);
}
```
Builder’ın bu yapısı sayesinde pizza inşası:
- Adım adım
- Okunabilir  
- Hatalara karşı dirençli  
- Genişletilebilir  
hâle gelir.
---
## 2.3 API Katmanında Builder Kullanımı
PizzaController’da Builder şöyle tetiklenir:
```java
Pizza.Builder builder = new Pizza.Builder()
        .size(Size.valueOf(request.getSize()))
        .doughType(DoughType.valueOf(request.getDoughType()))
        .sauceType(SauceType.valueOf(request.getSauceType()))
        .spicy(request.isSpicy());
```
Ardından toppings eklenir:
```java
for (String topping : request.getToppings()) {
    builder.addTopping(topping);
}
```
Ve final pizza oluşturulur:
```java
Pizza pizza = builder.build();
```
Bu akış Builder Pattern’ın API katmanı ile nasıl uyumlu çalıştığını gösterir.
---
## 3. Singleton Pattern — Siparişleri Merkezi Bir Noktada Yönetmek
Bir pizza sipariş sistemi, siparişlerin tek bir merkezden yönetilmesini gerektirir.
Singleton Pattern tam olarak burada devreye girer.
Sistem içerisinde **yalnızca bir adet OrderManager** olmalıdır.
Çünkü siparişlerin:
- numaralandırılması,
- saklanması,
- sıralı yönetilmesi
tek bir merkez üzerinden yapılmalıdır.
Eğer OrderManager’dan birden fazla instance oluşsaydı:
- Sipariş numaraları karışabilirdi,
- Farklı instance’lar birbirinin siparişini görmezdi,
- Veri tutarsızlığı ortaya çıkardı.
Singleton bunu engeller.
---
## 3.1 Singleton Pattern’ın Temel Fikri
Singleton’ın amacı:
- Sınıfın yalnızca 1 instance oluşturmasını garanti etmek
- Bu instance’a global erişim sağlamak
Projedeki OrderManager bunun ideal bir örneğidir.
---
## 3.2 OrderManager Singleton Uygulaması
Kod:
```java
private static OrderManager instance;

public static synchronized OrderManager getInstance() {
    if (instance == null) {
        instance = new OrderManager();
    }
    return instance;
}
```
Bu implementasyonun sağladıkları:
- **Lazy Initialization** → instance yalnızca ihtiyaç duyulduğunda oluşur.
- **synchronized** → eşzamanlı erişimlerde güvenli (thread-safe).
- **Tekil state yönetimi** → tüm siparişler tek merkezde toplanır.
---
## 3.3 Singleton’ın Constructor'ı Neden Private?
```java
private OrderManager() {
    System.out.println("[SINGLETON] OrderManager instance initialized");
}
```
Böylece başka hiçbir sınıf OrderManager’ı new ile üretemez.
Bu yaklaşım:
- Güvenlik sağlar,
- Doğru instance yönetimini zorunlu kılar.
---
## 3.4 Sipariş Oluşturma Akışı (placeOrder)
Singleton’ın pratikte nasıl kullanıldığına bakalım:
```java
public synchronized Order placeOrder(String customerName, Pizza pizza) {
    Order order = new Order((int) orderSequence++, customerName, pizza);
    orders.add(order);
    return order;
}
```
Buradaki kritik noktalar:
- **synchronized** → aynı anda iki sipariş geldiğinde sıra karışmaz.
- orderSequence++ → sipariş numaraları otomatik artar.
- orders.add(order) → siparişler tek listede tutulur.
---
## 3.5 Siparişleri Listeleme
Siparişleri dış dünyaya verirken:
```java
return Collections.unmodifiableList(orders);
```
Bu, listenin dışarıdan değiştirilememesini sağlar.
Güvenlik açısından çok doğru bir uygulamadır.
---
## 4. Controller Katmanı — Builder + Singleton’ın Birleştiği Yer
Controller, kullanıcıyla backend arasındaki köprüdür.
PizzaController iki önemli endpoint sunar:
- `/order` → sipariş oluşturma
- `/orders` → tüm siparişleri listeleme
Projenin iş akışını anlamak için bu yapıyı inceleyelim.
---
## 4.1 Sipariş Oluşturma Endpoint’i
Kod:
```java
@PostMapping("/order")
public ResponseEntity<Order> createOrder(@RequestBody PizzaOrderRequest request) {
    Pizza.Builder builder = new Pizza.Builder()
        .size(Size.valueOf(request.getSize()))
        .doughType(DoughType.valueOf(request.getDoughType()))
        .sauceType(SauceType.valueOf(request.getSauceType()))
        .spicy(request.isSpicy());
```
Bu aşamada Builder üzerinden pizza taslağı belirlenir.
Sonra toppings eklenir:
```java
for(String t : request.getToppings()) builder.addTopping(t);
```
Ve pizza oluşturulur:
```java
Pizza pizza = builder.build();
```
Ardından Singleton devreye girer:
```java
Order order = orderManager.placeOrder(request.getCustomerName(), pizza);
```
Bu sistemde Controller:
- Builder ile pizza üretir,
- Singleton ile siparişi kaydeder,
- Sonuçları API üzerinden döner.
---
## 4.2 Sipariş Listeleme Endpoint’i
Kod:
```java
@GetMapping("/orders")
public ResponseEntity<List<Order>> getAllOrders() {
    List<Order> orders = orderManager.getAllOrders();
    return ResponseEntity.ok(orders);
}
```
Bu endpoint doğrudan Singleton’ın tuttuğu listeyi döndürür.
Tüm sipariş yönetimi merkezî (centralized) bir noktadadır.
---
## 5. PizzaOrderRequest — API ile Backend Arasındaki Veri Köprüsü
Kullanıcıdan gelen sipariş verisi doğrudan Pizza nesnesine dönüştürülemez.
Bunun sebebi şudur:
- Kullanıcı text formatında gönderir,
- Backend enum ve gerçek nesnelerle çalışır.
Bu nedenle `PizzaOrderRequest` sınıfı doğru bir DTO (Data Transfer Object) örneğidir.
Kod:
```java
private String size;
private String doughType;
private String sauceType;
private List<String> toppings;
private boolean spicy;
```
DTO’nun avantajları:
- Backend’i dış etkilerden izole eder,
- Veri doğrulamasını kolaylaştırır,
- Controller'ı sade tutar.
---
## 6. Angular Frontend — Sipariş Oluşturma Akışını Tamamlayan Katman
Frontend, backend’in sunduğu API’yi kullanarak sipariş oluşturur.
Angular’da PizzaOrderComponent şu alanlara sahiptir:
- customerName
- size
- doughType
- sauceType
- toppings (string)
- spicy (boolean)
Bu değerler HTML form aracılığıyla kullanıcıdan alınır.
---
### 6.1 Formdan API'ye Veri Gönderme
Kullanıcı formu doldurduğunda submit() çalışır:
```typescript
const payload: PizzaOrderRequest = {
  customerName: this.customerName,
  size: this.size,
  doughType: this.doughType,
  sauceType: this.sauceType,
  toppings,
  spicy: this.spicy,
};
```
Angular tarafında bu veri HTTP POST ile backend'e gönderilir:
```typescript
this.api.createOrder(payload).subscribe(...)
```
Bu akış tamamen REST odaklıdır ve backend mimarisiyle tam uyumludur.
---
## 6.2 Toppings Alanı — Çoklu Değerlerin Yönetimi
Kullanıcı toppings değerini virgüllerle girer:
```
"mushroom, corn, olives"
```
Kod:
```typescript
const toppings = this.toppings
  .split(',')
  .map(t => t.trim())
  .filter(t => t.length > 0);
```
Bu dönüşüm frontend’de normalize edilir, backend’e temiz veri gider.
Bu hem UX’i hem sistem güvenilirliğini artırır.
---
## 6.3 Sipariş Gönderildikten Sonra UI Güncellemesi
Sipariş başarılı olduğunda Angular şu adımları yapar:
- lastOrder değişkeni güncellenir
- Kullanıcıya bildirim gösterilir
- loading kaldırılır
Kod:
```typescript
this.lastOrder = order;
this.orderResult = '🍕 Siparişiniz başarıyla oluşturuldu!';
```
lastOrder sayesinde HTML tarafında şu gösterilebilir:
```html
<div *ngIf="lastOrder">Sipariş ID: {{ lastOrder.id }}</div>
```
Bu, reactive Angular mimarisinin doğal bir parçasıdır.
---
## 7. OrderListComponent — Siparişlerin Listelenmesi
Siparişler backend’den çekilir:
```typescript
this.api.getOrders().subscribe({
  next: (data) => { this.orders = data; }
});
```
Backend:
```java
@GetMapping("/orders")
public ResponseEntity<List<Order>> getAllOrders() {
    return ResponseEntity.ok(orderManager.getAllOrders());
}
```
Bu endpoint:
- Singleton’daki sipariş listesini döner
- Angular ekranında tüm siparişler görüntülenir
Listeleme de aynı merkezi yapıyı kullanır.
---
## 8. Sistemin Uçtan Uca Akışı
Bir siparişin sistemde nasıl ilerlediğini adım adım inceleyelim:
1) Kullanıcı Angular formuna pizza detaylarını girer.
2) Angular → PizzaOrderRequest JSON’u oluşturur.
3) HTTP POST → `/api/pizzas/order`
4) Controller → Builder kullanarak Pizza nesnesi oluşturur.
5) Builder → adım adım pizza bileşenlerini hazırlar.
6) build() → final Pizza nesnesi oluşur.
7) Singleton → placeOrder() ile sipariş kaydedilir.
8) Order nesnesi oluşturulur.
9) Order frontend’e JSON olarak gönderilir.
10) Angular → lastOrder güncellenir.
11) Kullanıcı arayüzünde sipariş detayları gösterilir.
Bu akış, Builder ve Singleton Pattern’ın mükemmel iş birliğini gösterir.
---
## 9. Builder Pattern’ın Sağladığı Avantajlar
Pizza gibi parametre yoğun modellerde Builder Pattern devrim niteliğindedir:
- Çok okunabilir bir API sunar,
- Hata riskini azaltır,
- Parametrelerin eksik veya yanlış geçirilme ihtimalini ortadan kaldırır,
- Nesne oluşturma sürecini kontrollü hâle getirir,
- Nesnenin immutable olmasını kolaylaştırır,
- Yeni parametre eklemeyi sorunsuz hale getirir.
Bu nedenle modern Java projelerinde en sık kullanılan tasarım desenlerinden biridir.
---
## 10. Singleton Pattern’ın Sağladığı Avantajlar
Singleton yalnızca tek instance oluşturmak değildir. Aynı zamanda:
- Veri tutarlılığını garanti eder,
- Global state yönetimini kolaylaştırır,
- Performans açısından maliyeti düşürür,
- Paylaşılan kaynakların kontrolünü sağlar.
OrderManager için bu şu avantajları doğurur:
1) **Sipariş sırası karışmaz**  
Tek merkezden yönetilen sequence numarası sayesinde ID garantilidir.
2) **Tüm siparişler aynı yerde bulunur**  
Farklı instance’lar oluştukça veri dağılmaz.
3) **Thread-safe sipariş yönetimi**  
synchronized metodlar ile eşzamanlı erişim güvenlidir.
4) **Global erişilebilirlik**  
Uygulamanın her yerinde tek çağrı ile OrderManager kullanılabilir.
---
## 11. Builder + Singleton Kombinasyonunun Mükemmel Uyumu
Builder Pattern:
- Pizza oluşturmayı kontrol eder
- Parametre yönetimini düzenler
Singleton Pattern:
- Sipariş yönetimini merkezi hâle getirir
Bu iki desen bir araya geldiğinde:
- Nesne inşası → Builder
- Nesne saklama ve yönetim → Singleton
şeklinde güçlü bir sorumluluk ayırımı oluşur.
Bu mimari, hem backend hem frontend tarafından kolayca yönetilebilir.
---
## 12. Bu Mimarinin Gerçek Hayat Kullanım Alanları
Pizza sipariş sistemi bir örnek olsa da, aynı mimari şu alanlarda sıkça kullanılır:
- E-ticaret ürün sipariş sistemleri  
- Fast-food mobil sipariş uygulamaları  
- Otel rezervasyon modülleri  
- Kullanıcı profil oluşturma sihirbazları  
- Form temelli uygulamalar  
Bu alanların hepsi Builder Pattern’ın gücünden faydalanır.
Singleton ise genellikle:
- Sepet yönetimi  
- Kullanıcı session yönetimi  
- Global cache  
- Log yönetimi  
gibi durumlarda kullanılır.
---
## 13. Tasarımın Genişletilebilirliği
Bu mimari ileride kolayca geliştirilebilir.
Örneğin:
### 13.1 Pizza için yeni özellikler
- Peynir türü
- Kenar tipi
- İçecek seçeneği
Pizza.Builder’a yeni metotlar eklemek yeterlidir.
Kod stabil kalır, eski yapılar bozulmaz (OCP).
---
### 13.2 Sipariş yönetimi için yeni özellikler
OrderManager şu işlevlerle genişletilebilir:
- Sipariş iptal etme
- Sipariş güncelleme
- Veri kalıcılığı (DB entegrasyonu)
Bu geliştirmeler Singleton yapısını bozmadan eklenebilir.
---
### 13.3 Frontend için yeni özellikler
Angular tarafında:
- Gerçek zamanlı sipariş listesi
- Form doğrulama geliştirmeleri
- Fiyat hesaplama modülü
- Kullanıcıya özel öneriler
gibi özellikler eklenebilir.
Mevcut API yapısı bu geliştirmeleri destekler.
---
## 14. Genişletilebilirliğin Mimarî Açısından Önemi
Yazılımın iyi olup olmadığını anlamanın yollarından biri şudur:
**Yeni özellik eklemek sistemi ne kadar bozar?**
Bu projede:
- Pizza sınıfı Builder sayesinde bozulmadan genişler,
- Sipariş yönetimi Singleton sayesinde tutarlı kalır,
- Controller katmanı DTO ve Builder sayesinde temizdir.
Bu mimarî, SOLID prensiplerinin çoğunu destekler:
- Single Responsibility  
- Open/Closed  
- Dependency Inversion (dolaylı)
Bu yüzden proje hem öğretici hem üretim ortamına uygun bir yapıdadır.
---
## 15. API – Frontend – Backend Üçlüsünün Uyumlu Çalışması
Şimdi sistemin her katmanını birlikte inceleyelim:
### 15.1 Backend:
- Builder Pattern ile nesne üretilir,
- Singleton ile kaydedilir,
- Controller ile dış dünyaya açılır.
### 15.2 Frontend:
- Angular formu JSON request üretir,
- REST API’ye gönderir,
- Response’u ekranda gösterir.
### 15.3 Kullanıcı:
- Sipariş verir,
- Sipariş detaylarını görür,
- Listeyi görüntüler.
Bu üç katman birleşerek uçtan uca kullanıcı deneyimi oluşturur.
---
## 16. Örnek Bir Siparişin Uçtan Uca Akışı
Kullanıcı formu doldurur:
```
customerName = "Ahmet"
size = "LARGE"
toppings = "mantar, mısır, sucuk"
spicy = true
```
Angular bunu şu JSON’a dönüştürür:
```json
{
  "customerName": "Ahmet",
  "size": "LARGE",
  "doughType": "REGULAR",
  "sauceType": "TOMATO",
  "toppings": ["mantar", "mısır", "sucuk"],
  "spicy": true
}
```
Backend tarafında Controller bu JSON’u alır:
```java
PizzaOrderRequest request
```
Ardından Builder tetiklenir:
```java
Pizza.Builder builder = new Pizza.Builder()
     .size(Size.valueOf(request.getSize()))
     .doughType(DoughType.valueOf(request.getDoughType()))
     .sauceType(SauceType.valueOf(request.getSauceType()))
     .spicy(request.isSpicy());
```
Toppings eklenir:
```java
for(String topping : request.getToppings()) {
    builder.addTopping(topping);
}
```
Pizza oluşturulur:
```java
Pizza pizza = builder.build();
```
Ardından Singleton devreye girer:
```java
Order order = orderManager.placeOrder(request.getCustomerName(), pizza);
```
placeOrder içerisinde:
- Sipariş ID’si atanır,
- Sipariş listesine eklenir,
- Order nesnesi user’a döndürülür.
---
## 17. Sistemin Güvenilirlik Analizi
Pizza sipariş sistemi basit görünse de, arkasında doğru tasarım desenlerini barındırmaktadır.
Güvenilirlik açısından dört temel faktör ön plana çıkar:

### 17.1 Veri Tutarlılığı
Singleton sayesinde sipariş sırası hiçbir zaman karışmaz.
OrderManager tüm siparişleri aynı merkezde topladığı için veri kaybı yaşanmaz.

### 17.2 Genişleyebilirlik
Builder Pattern ile Pizza modeline yeni parametre eklemek çok kolaydır.
Yeni alanlar Builder’a eklenir → hiçbir eski kod bozulmaz.
Bu sayede OCP prensibi başarıyla uygulanır.

### 17.3 Bakım Kolaylığı
Controller sade tutulmuştur.
Pizza oluşturma logic’i Builder’a, sipariş yönetimi logic’i Singleton’a dağıtılmıştır.
Bu Separation of Concerns (SoC) ilkesinin tam karşılığıdır.

### 17.4 Frontend–Backend Senkronizasyonu
Angular bileşenleri backend ile aynı veri modelini kullanır.
PizzaOrderRequest hem Angular hem Spring Boot tarafında aynıdır.
Bu API tasarımında “contract-first” yaklaşımını temsil eder.
---
## 18. Bu Projede Builder ve Singleton Kullanmanın Mimari Değeri
Bu proje küçük görünse de, kurumsal yazılımlardaki çok önemli bir kavramı gösterir:
**İyi tasarım desenleri küçük projelerde değil, büyük projelerde fark yaratır.**
Ancak küçük projelerde doğru alışkanlıklar edinmek büyük sistemlerde verim sağlar.
Çünkü:
- Kod okunabilirliği artar,
- Modüller bağımsızlaşır,
- Hata oranı düşer,
- Genişletilebilirlik garantilenir.
---
## 19. Bu Mimari Üzerine Potansiyel Geliştirmeler
Pizza sipariş sistemi şu geliştirmeleri kolayca destekler:

### 19.1 Fiyat Hesaplama Modülü
Her topping ve boyut için fiyat hesaplayacak bir Service katmanı eklenebilir.
Böyle bir yapı Builder ile entegre çalışabilir.

### 19.2 Sipariş Durumu Yönetimi
Order nesnesine:
- RECEIVED  
- PREPARING  
- BAKING  
- OUT_FOR_DELIVERY  
- DELIVERED  
gibi durumlar eklenebilir.
Singleton bu akışı da yönetebilir.

### 19.3 Veritabanı Entegrasyonu
OrderManager başlangıçta memory-based çalışsa da:
- MySQL  
- PostgreSQL  
- MongoDB  
gibi sistemlerle kolayca genişletilebilir.
Singleton burada repository pattern ile birlikte çalışabilir.

### 19.4 Kullanıcı Arayüzü Geliştirme
Angular tarafında:
- Sipariş geçmişi
- Pizza önizleme
- Toppings autocomplete
- Loading animasyonları
gibi özellikler eklenebilir.
---
## 20. Kapanış 
Bu pizza sipariş sistemi; Builder ve Singleton Pattern’lerinin
**gerçek dünyada nasıl birlikte çalıştığını** açık bir şekilde göstermektedir.
Builder Pattern:
- Karmaşık nesneleri adım adım oluşturmayı sağlar,
- Kodun okunabilirliğini artırır,
- Modülerliği güçlendirir.
Singleton Pattern:
- Tüm sipariş akışını merkezi bir yerden yönetir,
- Global state sağlar,
- Veri tutarlılığını garanti eder.

Bu iki desen bir araya geldiğinde yazılım:
- Daha temiz,
- Daha sağlam,
- Daha genişletilebilir,
- Daha sürdürülebilir bir mimariye dönüşür.
