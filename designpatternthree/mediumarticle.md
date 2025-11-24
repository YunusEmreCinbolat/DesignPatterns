# Composite, Proxy ve Chain of Responsibility ile Modern Sipariş Yönetimi

## Giriş
Günümüzde e-ticaret sistemlerinin artan karmaşıklığı, ürün modellemeyi ve sipariş akışını yönetmeyi zorlaştırmaktadır.
Gerçek bir sipariş oluşturma süreci onlarca farklı iş kuralı içerir.
Aynı şekilde ürün tarafında da tekil ürünler, paketler, setler ve iç içe geçmiş kombinasyonlar bulunabilir.
Bu nedenle sağlam bir yazılım mimarisi tasarlamak kritik öneme sahiptir.
bu proje, bu ihtiyaca cevap veren örnek bir mimari sunar.
Ürün modellemesi Composite Pattern ile yapılmış, pahalı fiyat hesaplamaları Proxy Pattern ile optimize edilmiştir.
Ayrıca sipariş oluşturma ve sipariş durum geçişleri Chain of Responsibility Pattern ile yönetilmiştir.
Böylece uygulama hem genişletilebilir hem de yönetilebilir bir hâle gelmektedir.
---
## 1. Composite Pattern – Ürün Ağaçlarının Temeli
Composite Pattern, “bütün–parça” ilişkisini tek bir soyutlama üzerinden ele alan bir tasarım desenidir.
Bu desen özellikle ürün yapılandırmalarında sıklıkla kullanılır.
Tasarım, hem tekil hem de bileşik ürünlerin aynı interface üzerinden yönetilebilmesini sağlar.
Bu da sistemde önemli bir esneklik ve bütünlük sağlar.
DesignPatternThree içindeki Composite yapısı, `ProductComponent` interface’i ile başlar.
Tüm ürün türleri bu interface’i implement eder.
Böylece SingleProduct, ProductBundle veya gelecekte eklenebilecek başka herhangi bir ürün türü aynı tip olarak davranır.
Bu tasarım Liskov’un Yerine Geçme İlkesine birebir uyumludur.
Çünkü her ürün bileşeni, beklenen davranışı sorunsuz şekilde yerine getirir.
---
## 1.1 ProductComponent – Ortak Sözleşme
Bu interface, tüm ürün bileşenlerinin sahip olması gereken metotları tanımlar.
Aşağıda projenin gerçek kodundan alınmış hâlidir
```java
public interface ProductComponent {
    String getName();
    double getPrice();
    void print();
}
```
Bu yapı sayesinde, sistemi oluşturan tüm ürünler aynı fonksiyonlere sahip olur.
`getPrice()` fiyat hesaplamanın, `print()` ise debug çıktılarının ortak temelidir.
Böylece ürün ağaçları üzerinde recursive işlemler uygulanabilir.
Özellikle ProductBundle içinde bu soyutlama büyük avantaj sağlar.
---
## 1.2 SingleProduct – Composite İçindeki Yaprak
SingleProduct sınıfı, Composite Pattern’in Leaf (Yaprak) rolüdür.
İçinde alt eleman bulunmaz ve fiyatı doğrudan geri döndürür.
Projedeki gerçek kod şu şekildedir
```java
public class SingleProduct implements ProductComponent {
    private final String name;
    private final double price;
    public SingleProduct(String name, double price) {
        this.name = name;
        this.price = price;
    }
    @Override
    public double getPrice() { return price; }
    @Override
    public String getName() { return name; }
    @Override
    public void print() {
        System.out.println("Product " + name + " - Price " + price);
    }
}
```
Bu sınıfın basitliği ürüne dair temel yapıyı oluşturur.
Leaf sınıfı olmasına rağmen Composite yapının önemli bir parçasıdır.
---
## 1.3 ProductBundle – Ağaç İçindeki Bileşik Yapı
ProductBundle Composite Pattern’in Composite tarafını temsil eder.
İçinde birden fazla ProductComponent barındırabilir.
Bu bileşenler ister SingleProduct ister başka bir ProductBundle olabilir.
Böylece iç içe geçmiş çok seviyeli ürün ağaçları oluşturulabilir.
Projenin gerçek kodu aşağıdaki gibidir
```java
public class ProductBundle implements ProductComponent {
    private final String name;
    private final List<ProductComponent> items = new ArrayList<>();
    public ProductBundle(String name) { this.name = name; }
    public void add(ProductComponent component) { items.add(component); }
    @Override
    public double getPrice() {
        return items.stream().mapToDouble(ProductComponentgetPrice).sum();
    }
    @Override
    public String getName() { return name; }
    @Override
    public void print() {
        System.out.println("Bundle " + name);
        items.forEach(ProductComponentprint);
     }
 }
 ```
 Bu yapıda dikkat edilmesi gereken en önemli nokta recursive fiyat toplamasıdır.
 Bundle içinde başka bundle’lar olsa bile sistem fiyatı doğru hesaplar.
 Bu Composite Pattern’in en güçlü yönlerinden biridir.
 ---
 ## 1.4 Composite Pattern’in Kazanımları
 Composite Pattern sayesinde ürün modeli dinamik hâle gelir.
 Bundle ürünler oluşturmak basit bir işlem hâline gelir.
 Tekil ürünler ve paket ürünler aynı interface’e sahiptir.
 Bu da yüksek soyutlama ve sade kod anlamına gelir.
 Composite Pattern özellikle büyük ölçekli e-ticaret uygulamalarında kaçınılmazdır.
 Çünkü kampanyalar, paketler ve setler sürekli değişir.
 Böyle bir değişken modele en uygun desen Composite Pattern’dir.
 ---
 ## 2. Proxy Pattern – Ürün Fiyatlarını Cache’lemek
 Composite fiyat hesaplaması maliyetli olabilir.
 Çok geniş ürün ağaçlarında her seferinde fiyatı toplamak backend için yük oluşturabilir.
 PriceCacheProxy bu problemi çözmek için uygulanmıştır.
 Proxy Pattern, gerçek nesneye ek bir davranış sararak çalışır. PriceCacheProxy sınıfı, Composite yapıların fiyatını yalnızca bir kez hesaplar.
 Böylece tekrar eden fiyat isteklerinde hesaplama maliyetinden tasarruf edilir.
 Bu yaklaşım performansı ciddi şekilde artırır.
 Proxy Pattern burada lazy-loading mantığıyla uygulanmıştır.
 ---
 ## 2.1 PriceCacheProxy – Gerçek Kod Analizi
 Projede kullanılan PriceCacheProxy’nin kodu şu şekildedir
 ```java
 public class PriceCacheProxy implements ProductComponent {
     private final ProductComponent realProduct;
     private Double cachedPrice = null;

     public PriceCacheProxy(ProductComponent realProduct) {
         this.realProduct = realProduct;
     }

     @Override
     public String getName() { return realProduct.getName(); }

     @Override
     public double getPrice() {
         if (cachedPrice == null) {
             System.out.println("[Proxy] Fiyat ilk kez hesaplanıyor...");
             cachedPrice = realProduct.getPrice();
         } else {
             System.out.println("[Proxy] Cached fiyat kullanılıyor...");
         }
         return cachedPrice;
     }

     @Override
     public void print() { realProduct.print(); }

     public void invalidate() { cachedPrice = null; }
 }
 ```
 Bu sınıf, Composite fiyat hesaplamasında ciddi bir optimizasyon sağlar.
 Özellikle bundle içinde yüzlerce ürün olduğunda fark daha net hissedilir.
 Ayrıca `invalidate()` fonksiyonu, ürün fiyatı değiştiğinde cache’i temizlemek için kullanılır.
 Bu sayede sistem hem doğru hem de hızlı çalışır.
 ---
 ## 2.2 Proxy Pattern’in Sağladığı Faydalar
 Proxy Pattern kullanımı birçok avantaj sunar
 - Fiyat hesaplama yükü azalır.
 - Composite yapı büyüdükçe performans kaybı yaşanmaz.
 - Hesaplama mantığı ve cache mantığı tamamen ayrılır.
 - Kod okunabilirliği artar.
 - Davranış genişletme çok kolay hâle gelir.
 Ayrıca Proxy Pattern, sistemde gelecekte yeni optimizasyonlar eklemeyi de kolaylaştırır.
 ---
 ## 3. Chain of Responsibility Pattern – Sipariş İşleme Boru Hattı
 Chain of Responsibility, bir işlemi ardışık adımlara bölmek için kullanılan bir tasarım desenidir.
 DesignPatternThree projesinde sipariş oluşturma süreci tamamen Chain yapısıyla düzenlenmiştir.
 Bu sayede her iş kuralı kendi handler sınıfında bulunur.
 Handler’lar birbirine zincir şeklinde bağlanır.
 Sipariş, zincirin en başından içeri girer ve adımlar sırayla uygulanır.
 Eğer herhangi bir handler siparişi reddederse zincir durur.
 Bu yaklaşım hem temizdir hem de genişletilebilirliği son derece kolaylaştırır.
 ---
 ## 3.1 OrderHandler – Tüm Handlerların Temel Sınıfı
 Aşağıda projenin gerçek OrderHandler sınıfı görülmektedir
 ```java
 public abstract class OrderHandler {
     protected OrderHandler next;

     public OrderHandler setNext(OrderHandler next) {
         this.next = next;
         return next;
     }

     public final void handle(Order order) {
         if (doHandle(order) && next != null) {
             next.handle(order);
         }
     }

     protected abstract boolean doHandle(Order order);
 }
 ```
 Bu sınıf design pattern’in çekirdeğini oluşturur.
 `setNext` fonksiyonu zincirin kurulmasını sağlar.
 `handle` fonksiyonu ise zincirin akışını yönetir.
 `doHandle` ise her handler’ın kendi iş kuralını uyguladığı yerdir.
 Eğer `doHandle` false dönerse zincir durur.
 Bu mekanizma sipariş oluşturma sürecini mükemmel bir şekilde kontrol eder.
 ---
 ## 3.2 FraudHandler – Fraud Kontrolü
 Siparişin fraud içerip içermediği ilk adımda kontrol edilir.
 ```java
 public class FraudHandler extends OrderHandler {
     @Override
     protected boolean doHandle(Order order) {
         if (!order.isNotFraud()) {
             System.out.println("[FraudHandler] Fraud tespit edildi.");
             order.markRejected("Fraud detected");
             return false;
         }
         return true;
     }
 }
 ```
 Eğer fraud şüphesi varsa zincir derhal durur.
 Bu, güvenlik açısından kritik bir adımdır.
 ---
 ## 3.3 PaymentHandler – Ödeme Doğrulaması
 Fraud kontrolü geçildikten sonra ödeme kontrolü yapılır.
 ```java
 public class PaymentHandler extends OrderHandler {
     @Override
     protected boolean doHandle(Order order) {
         if (!order.isPaymentCompleted()) {
             System.out.println("[PaymentHandler] Ödeme başarısız.");
             order.markRejected("Payment failed");
             return false;
         }
         return true;
     }
 }
 ```
 Eğer ödeme başarısızsa sipariş reddedilir ve zincir sonlanır. PaymentHandler sipariş akışındaki en kritik aşamalardan biridir.
 Çünkü ödeme gerçekleşmeden siparişin ilerlemesine izin verilmez.
 Bu mekanizma Chain of Responsibility’nin doğal bir kullanım örneğidir.
 ---
 ## 3.4 StockHandler – Stok Doğrulaması
 PaymentHandler geçildikten sonra stok kontrolü yapılır.
 ```java
 public class StockHandler extends OrderHandler {
     @Override
     protected boolean doHandle(Order order) {
         System.out.println("[StockHandler] Stok kontrol edildi.");
         return true;
     }
 }
 ```
 Bu örnekte basit bir stok kontrolü yapılmaktadır.
 Gerçek sistemde ürün bazlı stok düşümü de bu aşamada yapılabilir.
 Stok yetersizse burada zincir durdurulabilir.
 Bu tasarım ileride çok daha karmaşık stok politikalarını destekleyebilir.
 ---
 ## 3.5 TaxHandler – KDV Hesabı
 Stok kontrolünden sonra siparişin vergisi hesaplanır.
 Türkiye’de KDV genelde %20 olduğundan örnek hesaplama şu şekildedir
 ```java
 public class TaxHandler extends OrderHandler {
     @Override
     protected boolean doHandle(Order order) {
         double total = order.getTotalPrice();
         double tax = total * 0.20;
         order.setTotalPrice(total + tax);
         System.out.println("[TaxHandler] KDV uygulandı.");
         return true;
     }
 }
 ```
 Bu aşama fiyatın doğru hesaplanması açısından önemlidir.
 Composite yapıdaki ürünlerin fiyatları hesaplanmış olsa bile vergi her zaman sonradır.
 Vergi ekleme aşaması Chain içinde ayrı bir adım olarak modellenmiştir.
 Bu da kodun sadeleşmesini sağlar.
 ---
 ## 3.6 DiscountHandler – Büyük Sipariş İndirimi
 DiscountHandler belirli bir eşik üzerinde indirim uygular.
 Projedeki gerçek örnek
 ```java
 public class DiscountHandler extends OrderHandler {
     @Override
     protected boolean doHandle(Order order) {
         double total = order.getTotalPrice();
         if (total >= 20000) {
             double discount = total * 0.05;
             order.setTotalPrice(total - discount);
             System.out.println("[DiscountHandler] %5 indirim uygulandı.");
         }
         return true;
     }
 }
 ```
 Burada threshold bazlı bir indirim modeli uygulanmıştır.
 Chain of Responsibility bu tarz indirimi modüler hâle getirir.
 İstenirse farklı indirim handler’ları da eklenebilir.
 ---
 ## 3.7 ShippingHandler – Kargo Ücreti Uygulaması
 Kargo, toplam fiyat belirli bir eşik altındaysa eklenen bir maliyettir.
 Kod şu şekildedir
 ```java
 public class ShippingHandler extends OrderHandler {
     @Override
     protected boolean doHandle(Order order) {
         double total = order.getTotalPrice();
         if (total >= 1000) {
             System.out.println("[ShippingHandler] Ücretsiz kargo uygulandı.");
         } else {
             order.setTotalPrice(total + 49.90);
             System.out.println("[ShippingHandler] Kargo ücreti eklendi.");
         }
         return true;
     }
 }
 ```
 Ücretsiz kargo sınırları ülkeden ülkeye değişir.
 Bu handler’ın ayrı olması bu kuralların kolayca güncellenebilmesini sağlar.
 ---
 ## 3.8 CreateOrderHandler – Siparişin Kalıcı Olarak Kaydedilmesi
 Zincirin en sonunda sipariş kaydedilir
 ```java
 public class CreateOrderHandler extends OrderHandler {
     private final OrderRepository repo;

     public CreateOrderHandler(OrderRepository repo) {
         this.repo = repo;
     }

     @Override
     protected boolean doHandle(Order order) {
         if (order.getStatus() == OrderStatus.REJECTED) {
             System.out.println("[CreateOrderHandler] Reddedilen sipariş kaydedilmedi.");
             return false;
         }
         repo.save(order);
         System.out.println("[CreateOrderHandler] Sipariş kaydedildi.");
         return true;
     }
 }
 ```
 Bu aşamada veri veritabanına yazılır.
 Handler zinciri başarılı şekilde tamamlandıysa sipariş başarıyla oluşturulmuş olur.
 Bu yapı, sipariş akışını temiz ve adım adım yönetilebilir hâle getirir.
 ---
 ## 3.9 Chain of Responsibility’nin Mimariye Etkisi
 Chain yapısı sayesinde
 - Her kural bağımsız bir sınıfa taşınır.
 - Kod daha okunabilir hâle gelir.
 - Yeni kurallar eklemek kolaylaşır.
 - Zincirin sırası değiştirilebilir.
 - Her adımın test edilmesi ayrı ayrı mümkündür.
 Bu nedenle Chain of Responsibility, iş kuralı yoğun sistemlerde ideal bir desendir.
 DesignPatternThree bunu başarılı bir şekilde uygular.
 Sipariş oluşturma süreci tamamen modüler hâle getirilmiştir.
 Bu da gerçek dünyada karşılaşılacak tüm iş senaryolarına kolay uyum sağlar.
  ## 4. Sipariş Durum Yönetimi – İkinci Chain of Responsibility
 DesignPatternThree yalnızca sipariş oluşturmayı değil, siparişin durum yaşam döngüsünü de Chain of Responsibility ile yönetir.
 Bu ikinci chain, siparişin RECEIVED → PREPARING → SHIPPED → OUT_FOR_DELIVERY → DELIVERED şeklinde ilerlemesini sağlar.
 Bu mimari sayesinde sipariş durum geçişleri modüler bir hâle gelir.
 Durum işlemleri kontrol edilebilir, genişletilebilir ve test edilebilir olur.
 ---
 ## 4.1 OrderStatusHandler – Durum Chain’inin Arayüzü
 Aşağıda durum chain’inin temel interface’i bulunmaktadır
 ```java
 public interface OrderStatusHandler {
     void handle(Order order);
     void setNext(OrderStatusHandler next);
 }
 ```
 Bu interface, tüm durum handler’larının implement ettiği sözleşmedir.
 `handle()` metodu siparişin durumunu ilerletmek için çağrılır.
 `setNext()` zincirin kurulmasını sağlar.
 ---
 ## 4.2 BaseOrderStatusHandler – Ortak Davranış
 Bu sınıf diğer handler’lara ortak davranışı sağlar
 ```java
 public abstract class BaseOrderStatusHandler implements OrderStatusHandler {
     protected OrderStatusHandler next;

     @Override
     public void setNext(OrderStatusHandler next) {
         this.next = next;
     }

     protected void forward(Order order) {
         if (next != null) next.handle(order);
     }
 }
 ```
 Bu yapı sayesinde, her handler yalnızca kendi durumuyla ilgilenir.
 Diğer sorumluluklar temel sınıf tarafından karşılanır.
 ---
 ## 4.3 ReceivedHandler – İlk Durum
 Siparişin başlangıç durumudur.
 ```java
 public class ReceivedHandler extends BaseOrderStatusHandler {
     @Override
     public void handle(Order order) {
         if (order.getStatus() == OrderStatus.RECEIVED) {
             order.setStatus(OrderStatus.PREPARING);
             return;
         }
         forward(order);
     }
 }
 ```
 Bu handler, siparişin ilk durumdan ikinci duruma geçişini kontrol eder.
 ---
 ## 4.4 PreparingHandler – Hazırlanıyor
 ```java
 public class PreparingHandler extends BaseOrderStatusHandler {
     @Override
     public void handle(Order order) {
         if (order.getStatus() == OrderStatus.PREPARING) {
             order.setStatus(OrderStatus.SHIPPED);
             return;
         }
         forward(order);
     }
 }
 ```
 Sipariş hazırlanır ve paketleme aşamasına geçilir.
 ---
 ## 4.5 ShippedHandler – Kargoda
 ```java
 public class ShippedHandler extends BaseOrderStatusHandler {
     @Override
     public void handle(Order order) {
         if (order.getStatus() == OrderStatus.SHIPPED) {
             order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
             return;
         }
         forward(order);
     }
 }
 ```
 Bu aşamada sipariş dağıtım merkezine ulaşmıştır.
 ---
 ## 4.6 OutForDeliveryHandler – Dağıtıma Çıktı
 ```java
 public class OutForDeliveryHandler extends BaseOrderStatusHandler {
     @Override
     public void handle(Order order) {
         if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
             order.setStatus(OrderStatus.DELIVERED);
             return;
         }
         forward(order);
     }
 }
 ```
 Sipariş müşterinin kapısına doğru yola çıkmıştır.
 ---
 ## 4.7 DeliveredHandler – Teslim Edildi
 ```java
 public class DeliveredHandler extends BaseOrderStatusHandler {
     @Override
     public void handle(Order order) {
         if (order.getStatus() == OrderStatus.DELIVERED) return;
         forward(order);
     }
 }
 ```
469 Bu final handler siparişin artık tamamlandığını belirtir.
470 Daha fazla ilerleme olmadığı için zincir burada durur.
---
## 4.8 OrderStatusService – Durum Zincirinin Kurulması
Bu service tüm durum handler’larını birbirine bağlar.
```java
received.setNext(preparing);
preparing.setNext(shipped);
shipped.setNext(outForDelivery);
outForDelivery.setNext(delivered);
received.handle(order);
```
## 5. Controller Katmanı – İki Zincirin Tetiklendiği Yer
Controller katmanı, backend’e gelen istekleri karşılayan ve chain mekanizmalarını tetikleyen yapıdır.
Bu projede iki önemli endpoint bulunmaktadır `/orders` ve `/orders/{id}/next`.
İlki sipariş oluşturmayı, ikincisi sipariş durum geçişini yönetir.
Kod aşağıdaki gibidir
```java
@PostMapping
public Order createOrder(@RequestBody Order order) {
    orderService.processOrder(order);
    orderRepository.save(order);
    return order;
}
```
Bu fonksiyon sipariş oluşturma chain’ini çalıştırır.
Eğer chain sırasında sipariş reddedilirse bu işlem repository katmanına yansıtılmaz.
Çünkü CreateOrderHandler reddedilen siparişleri kaydetmez.
Bu, Chain of Responsibility’nin mimari bütünlüğüne katkı sunduğu noktalardan biridir.
---
## 5.1 Sipariş Durumunu İlerletme Endpoint’i
Siparişin durumunu bir sonraki aşamaya taşımak için şu endpoint kullanılır
```java
@PostMapping("/{id}/next")
public Order nextStatus(@PathVariable Long id) {
    Order order = orderRepository.findById(id);
    orderStatusService.nextStep(order);
    orderRepository.save(order);
    return order;
}
```
Burada durum zinciri çalıştırılır.
Sipariş RECEIVED durumundaysa PREPARING’e geçer; PREPARING ise SHIPPED olur.
Zincir, final durum olan DELIVERED aşamasına kadar ilerler.
Bu endpoint, gerçek dünyada sipariş takip sistemlerinin kalbini temsil eder.
---
## 6. Genel Mimari Değerlendirme
DesignPatternThree, üç büyük tasarım desenini tek bir modern sipariş yönetim sisteminde birleştirir.
Composite Pattern → Ürünlerin ağaç yapısıyla modellenmesini sağlar.
Proxy Pattern → Fiyat hesaplamasında performans optimizasyonu getirir.
Chain of Responsibility → Sipariş akışını ve sipariş durum değişikliklerini adımlara ayırır.
Bu mimarinin en büyük gücü, tüm bu desenlerin birbirine zarar vermeden birlikte çalışabilmesidir.
Bu sayede sistem hem genişletilebilir hem de okunabilir kalır.
---
## 7. Neden Bu Mimarinin Gerçek Dünyada Karşılığı Büyük?
Çünkü gerçek e-ticaret sistemlerinde iş kuralları sürekli değişir.
Yeni kampanyalar eklenir, fiyat hesaplama modeli değişir, fraud politikaları güncellenir.
Bu gibi durumlarda chain yapısı sayesinde yalnızca ilgili handler değiştirilir.
Ürün modellemesi tarafında bundle yapıları son derece önemlidir.
Composite Pattern olmadan paket ürünleri yönetmek çok zorlaşır.
Ayrıca Proxy Pattern gibi performans desenleri büyük trafik altındaki sistemlerde hayati önem taşır.
Bu nedenle DesignPatternThree mimari açıdan modern, güçlü ve sürdürülebilir bir çözümdür.
---
## 8. Sonuç
Bu makale boyunca Composite, Proxy ve Chain of Responsibility desenlerinin nasıl uygulandığını gördük.
Ayrıca bu desenlerin bir arada nasıl çalıştığını, kod örnekleri üzerinden adım adım inceledik.
DesignPatternThree projesi, temiz mimariyi gerçek bir senaryoda başarıyla göstermektedir.
Bu yapıyı temel alarak çok daha büyük bir e-ticaret motoru kolayca inşa edilebilir.