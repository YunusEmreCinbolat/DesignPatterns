# Sepet Fiyatlandırma Sistemi Üzerinden Flyweight, Strategy, Bridge ve Facade Tasarım Desenlerini Derinlemesine İnceleme
Modern yazılım projelerinde indirim, kargo ve fiyat hesaplama gibi “iş kuralı yoğun” alanları doğru tasarlamak; sürdürülebilir, test edilebilir ve genişletilebilir kodun temelidir.
Özellikle e‑ticaret benzeri senaryolarda **sepet hesaplama** akışı birden fazla bağımsız sorumluluğu aynı anda taşır.

Bu makalede, demo bir **sepet fiyatlandırma sistemi** üzerinden dört kritik tasarım desenini ele alıyoruz:
- **Flyweight Pattern** → Ürün nesnelerini paylaşımlı (cache) yöneterek bellek/verim kazanmak
- **Strategy Pattern** → İndirim hesaplarını değiştirilebilir algoritmalar olarak kurgulamak
- **Bridge Pattern** → Checkout akışı ile kargo hesaplamasını gevşek bağlamak
- **Facade Pattern** → Tüm fiyatlandırma orkestrasyonunu tek bir noktada sadeleştirmek

Projedeki backend Spring Boot, frontend ise Angular ile geliştirilmiştir.
Kodlar üzerinden hem teorik hem pratik uygulamayı detaylı şekilde inceleyeceğiz.
---

## 1. Problem Tanımı — Neden Tasarım Deseni Kullanıyoruz?
Bir sepet fiyatlandırma isteği (tek endpoint) aslında şu alt işleri içerir:
- Sepetteki ürünlerden **ara toplam** (subtotal) hesaplamak
- Seçilen indirim tipine göre **indirim miktarı** üretmek
- İndirim tipine göre **kargo bedeli** belirlemek (ör. ücretsiz kargo)
- Kullanıcıya anlaşılır bir **kırılım** döndürmek

Eğer bu işlerin tamamı tek bir sınıfta “if/else” ile çözülmeye çalışılsaydı şu sorunlar ortaya çıkardı:
- Kod şişer ve okunabilirlik düşer
- Yeni indirim eklemek mevcut akışı bozar (OCP ihlali)
- Kargo/checkout kombinasyonları arttıkça sınıf patlaması olur
- Test etmek zorlaşır

Bu projede çözüm: sorumlulukları 4 pattern ile bölmek.
---

## 2. Flyweight Pattern — Ürün Nesnelerini Paylaşımlı (Cache) Yönetmek
Sepette aynı ürün (ör. `P1`) farklı satırlarda veya farklı isteklerde tekrar tekrar kullanılabilir.
Her seferinde yeni `Product` nesnesi üretmek yerine, **tek bir paylaşımlı instance** tutup tekrar kullanmak daha verimlidir.

Projedeki `ProductFlyweightFactory` tam olarak bunu yapar:

```java
public class ProductFlyweightFactory {

    private static final Map<String, Product> CACHE = new HashMap<>();

    public static Product getOrCreate(String id, String name, double price) {
        if (!CACHE.containsKey(id)) {
            System.out.println("[FLYWEIGHT] Cache MISS → creating shared Product instance (Flyweight) → " + id + " - " + name);
            CACHE.put(id, new Product(id, name, price));
        } else {
            System.out.println("[FLYWEIGHT] Cache HIT → reusing shared Product instance (Flyweight) → " + id);
        }
        return CACHE.get(id);
    }
}
```

### 2.1 Ürünlerin Uygulama Açılışında Cache’e Yüklenmesi
Uygulama ayağa kalkınca 10 ürün cache’e pre-load edilir:

```java
@Configuration
public class ProductCatalogConfig {

    @PostConstruct
    public void preloadProducts() {

        System.out.println("[INIT] Preloading 10 products into Flyweight cache (shared Product instances)");

        ProductFlyweightFactory.getOrCreate("P1", "Samsung TV", 1200);
        // ...
        ProductFlyweightFactory.getOrCreate("P10", "Desk Lamp", 39);

        System.out.println("[INIT] Flyweight cache size → " + ProductFlyweightFactory.cacheSize());
    }
}
```

### 2.2 Flyweight’in Sağladığı Avantajlar
- Aynı ürün için tekrar tekrar nesne üretmez
- Bellek kullanımını ve GC baskısını azaltır
- Ürün objeleri “tek kaynaktan” yönetildiği için tutarlılık artar
---

## 3. Strategy Pattern — İndirim Hesaplamasını Değiştirilebilir Algoritma Yap
İndirim türleri çeşitlenebilir:
- Yüzde indirim
- X al Y bedava
- Ücretsiz kargo (indirim = 0 ama shipping davranışı değişir)

Bu değişken algoritmaları `Discount` stratejileri olarak ayırmak en temiz çözümdür.

### 3.1 Strategy Arayüzü

```java
public interface Discount {
    double calculate(Cart cart);
    String getDescription();
}
```

### 3.2 PercentageDiscount — Yüzde İndirimi

```java
public class PercentageDiscount implements Discount {

    private final double percent;

    public PercentageDiscount(double percent) {
        this.percent = percent;
    }

    @Override
    public double calculate(Cart cart) {
        double discount = cart.getSubtotal() * (percent / 100.0);
        System.out.println("[DISCOUNT] Percentage " + percent + "% → " + discount);
        return discount;
    }

    @Override
    public String getDescription() {
        return "Percentage discount (" + percent + "%)";
    }
}
```

### 3.3 BuyXGetYDiscount — “3 al 1 bedava”

```java
public class BuyXGetYDiscount implements Discount {

    private final String productId;
    private final int x;
    private final int y;

    @Override
    public double calculate(Cart cart) {
        int count = cart.countProduct(productId);
        if (count >= x) {
            int freeItems = count / x * y;
            double unitPrice = cart.getProductPrice(productId);
            double discount = unitPrice * freeItems;
            System.out.println("[DISCOUNT] Buy " + x + " Get " + y + " applied → " + discount);
            return discount;
        }
        return 0.0;
    }
}
```

### 3.4 Strategy Pattern’ın Sağladığı Avantajlar
- Yeni indirim eklemek için mevcut kodu “delip” if/else büyütmezsin
- Her indirim algoritması ayrı sınıf olduğu için test etmek kolaylaşır
- `Cart` gibi domain objesi sade kalır; hesap “strategies”de yaşar
---

## 4. Bridge Pattern — Checkout ile Shipping’i Gevşek Bağlamak
E‑ticarette “kargo hesaplama” farklı sebeplerle değişebilir:
- Düz sabit ücret
- Ücretsiz kargo
- Sepet tutarına göre kargo

Eğer checkout (abstraction) ile shipping (implementation) birbirine sıkı bağlanırsa her kombinasyonda yeni sınıf üretmek zorunda kalırsın.

Bu projede `Checkout` soyutlaması, `ShippingImplementor` implementasyonuna köprü olur:

```java
public abstract class Checkout {

    protected final ShippingImplementor shipping;

    protected Checkout(ShippingImplementor shipping) {
        this.shipping = shipping;
    }

    public double calculateShippingFee(Cart cart) {
        return shipping.calculateShipping(cart);
    }

    public double calculateFinalTotal(double totalAfterDiscount, Cart cart) {
        return totalAfterDiscount + calculateShippingFee(cart);
    }
}
```

Sabit kargo:

```java
public class FlatRateShipping implements ShippingImplementor {
    private final double fee;

    @Override
    public double calculateShipping(Cart cart) {
        return fee;
    }
}
```

### 4.1 Bridge Pattern’ın Sağladığı Avantajlar
- Checkout akışı ile kargo algoritması bağımsız evrilir
- “Yeni checkout türü” ve “yeni shipping türü” kolayca kombine edilir
---

## 5. Facade Pattern — Tüm Orkestrasyonu Tek Bir Yerde Toplamak
Şimdiye kadar parçaladığımız sorumlulukları, dış dünya için tek bir “giriş noktası” altında toplamak gerekir.
Facade Pattern bunu sağlar.

Bu projede `DiscountFacade`, Strategy + Bridge bileşenlerini orkestre ederek **tek bir metotla** fiyatlandırma sonucu üretir:

```java
@Service
public class DiscountFacade {

    private static final double SHIPPING_FEE = 50.0;

    public DiscountResult applyDiscount(Cart cart, DiscountType type) {

        Discount discountStrategy = resolveStrategy(type);

        double discountAmount = discountStrategy.calculate(cart);
        cart.applyDiscount(discountAmount);

        double totalAfterDiscount = cart.getFinalTotal();

        ShippingImplementor shipping = (type == DiscountType.FREE_SHIPPING)
            ? new FreeShipping()
            : new FlatRateShipping(SHIPPING_FEE);

        Checkout checkout = new StandardCheckout(shipping);

        double shippingFee = checkout.calculateShippingFee(cart);
        double finalTotalWithShipping = checkout.calculateFinalTotal(totalAfterDiscount, cart);

        return new DiscountResult(
            cart.getSubtotal(),
            discountAmount,
            totalAfterDiscount,
            shippingFee,
            finalTotalWithShipping,
            discountStrategy.getDescription()
        );
    }
}
```

### 5.1 Facade Pattern’ın Sağladığı Avantajlar
- Dışarıdan bakan için tek bir temiz API: `applyDiscount(cart, type)`
- İç detaylar (hangi strateji? hangi kargo?) dışarı sızmaz
- Controller/Service katmanı sadeleşir
---

## 6. Controller Katmanı — UI ile Pattern Dünyasının Buluştuğu Yer
Controller, kullanıcı ile backend arasındaki köprüdür.
Bu projede iki kritik endpoint öne çıkar:
- `/api/products` → ürün listesini Flyweight cache’inden döner
- `/api/cart/price` → sepet fiyatlandırma sonucunu döner

### 6.1 Ürünleri Listeleme (Flyweight Cache’inden)

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        Map<String, Product> cache = ProductFlyweightFactory.getAll();
        List<Product> list = new ArrayList<>(cache.values());
        return ResponseEntity.ok(list);
    }
}
```

### 6.2 Sepet Fiyatı Hesaplama Endpoint’i

```java
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @PostMapping("/price")
    public ResponseEntity<CartPriceResponse> calculatePrice(@RequestBody CartPriceRequest request) {
        return ResponseEntity.ok(cartPricingService.calculatePrice(request));
    }
}
```
---

## 7. Service Katmanı — Flyweight + Facade ile Akışı Birleştirmek
`CartPricingService` sepeti domain objesine çevirir, ürünleri Flyweight’ten alır ve Facade’i çağırır:

```java
@Service
public class CartPricingService {

    private final DiscountFacade discountFacade;

    public CartPriceResponse calculatePrice(CartPriceRequest request) {

        Cart cart = new Cart();
        for (CartItemRequest itemReq : request.getItems()) {
            var product = ProductFlyweightFactory.getOrCreate(
                itemReq.getProductId(),
                itemReq.getName(),
                itemReq.getPrice()
            );
            cart.addItem(new CartItem(product, itemReq.getQuantity()));
        }

        DiscountResult result = discountFacade.applyDiscount(cart, request.getDiscountType());

        return new CartPriceResponse(
            result.getSubtotal(),
            result.getDiscountAmount(),
            result.getTotalAfterDiscount(),
            result.getShippingFee(),
            result.getFinalTotal(),
            result.getDescription()
        );
    }
}
```
---

## 8. Angular Frontend — Sepet Akışını Tamamlayan Katman
Frontend tarafında kullanıcı:
- Ürünleri backend’den çeker
- Sepete ekler
- İndirim tipini seçer
- “Hesapla” der ve sonucu görür

### 8.1 Sepeti Backend’e Gönderme

```typescript
const payload: CartPriceRequest = {
  items: this.cartItems,
  discountType: this.discountType,
};

this.cartApi.calculatePrice(payload).subscribe({
  next: (res) => {
    this.result = res;
  },
});
```

Ürünleri çekme:

```typescript
this.productApi.getProducts().subscribe({
  next: (products) => {
    this.products = products;
  },
});
```
---

## 9. Sistemin Uçtan Uca Akışı
Bir “sepeti hesapla” isteğinin sistemde nasıl ilerlediğini adım adım inceleyelim:

1) Kullanıcı Angular UI’da ürün seçer ve sepete ekler.
2) Angular, `CartPriceRequest` payload’unu üretir.
3) HTTP POST → `/api/cart/price`
4) `CartController` isteği `CartPricingService`’e iletir.
5) Service, ürünleri `ProductFlyweightFactory.getOrCreate(...)` ile paylaşımlı şekilde alır.
6) `Cart` objesi oluşturulur ve subtotal hesaplanır.
7) `DiscountFacade.applyDiscount(...)` çağrılır.
8) Facade içinde:
   - Strategy seçilir ve indirim hesaplanır.
   - Bridge ile shipping hesaplanır.
9) `CartPriceResponse` kırılımı Angular’a döner.
10) Angular sonucu ekranda gösterir.

Bu akış, dört pattern’in aynı problem üzerinde nasıl uyumlu çalıştığını net şekilde gösterir.
---

## 10. Flyweight Pattern’ın Sağladığı Avantajlar
- Ürün nesneleri paylaşımlı olduğu için bellek/verim kazanımı
- Ürün yönetimi tek cache üzerinden tutarlı hale gelir

## 11. Strategy Pattern’ın Sağladığı Avantajlar
- İndirim algoritmaları bağımsızdır; yeni indirim eklemek kolaylaşır
- Test edilebilirlik artar

## 12. Bridge Pattern’ın Sağladığı Avantajlar
- Kargo hesaplama değişse bile checkout akışı bozulmaz
- Kombinasyon patlaması önlenir

## 13. Facade Pattern’ın Sağladığı Avantajlar
- Controller/Service katmanı sadeleşir
- “Tek metot” ile kompleks orkestrasyon gizlenir
---

## 14. Bu Mimarinin Gerçek Hayat Kullanım Alanları
Bu sepet sistemi bir demo olsa da, aynı yaklaşım şu alanlarda sıkça kullanılır:
- E‑ticaret sepet/checkout modülleri
- Kampanya/kupon motorları
- Kargo/lojistik fiyatlandırma
- Abonelik/plan fiyatlandırma

---

## 15. Tasarımın Genişletilebilirliği
Bu mimari ileride kolayca geliştirilebilir.

### 15.1 Yeni indirim eklemek
Örneğin “1000 TL üstü 100 TL indirim” gibi bir indirim için:
- Yeni bir `Discount` sınıfı yazılır
- `resolveStrategy(...)` içine yeni enum case eklenir

### 15.2 Yeni shipping kuralı eklemek
Örneğin “sepette 5 ürün üstü kargo ücretsiz”:
- Yeni bir `ShippingImplementor` sınıfı eklenir
- Facade içinde hangi durumda seçileceği belirlenir

---

## 16. API – Frontend – Backend Üçlüsünün Uyumlu Çalışması
Bu projede katmanlar net ayrılmıştır:

### 16.1 Backend
- Flyweight ile ürünler yönetilir
- Service katmanı sepeti kurar
- Facade fiyatlandırmayı orkestre eder

### 16.2 Frontend
- Ürünleri listeler
- Sepet payload’unu üretir
- Sonucu ekranda gösterir

---

## 17. Örnek Bir İsteğin Uçtan Uca Görünümü
Örnek payload:

```json
{
  "items": [
    { "productId": "P1", "name": "Samsung TV", "price": 1200, "quantity": 3 }
  ],
  "discountType": "BUY_X_GET_Y"
}
```

Bu istek backend’de:
- Flyweight cache’inden `P1` ürününü reuse eder
- Strategy olarak `BuyXGetYDiscount("P1", 3, 1)` seçer
- Sonra Bridge ile kargoyu hesaplayıp sonucu döner

---

## 18. Sistemin Güvenilirlik Analizi

### 18.1 Veri Tutarlılığı
`Cart` subtotal ve discount sonrası total’i tek yerden hesaplar:

```java
public void applyDiscount(double discountAmount) {
    this.discountAmount = discountAmount;
    this.finalTotal = subtotal - discountAmount;
}
```

### 18.2 Bakım Kolaylığı
İndirim, kargo ve orkestrasyon katmanları ayrıldığı için değişiklikler lokal kalır.

---

## 19. Bu Mimari Üzerine Potansiyel Geliştirmeler
- `resolveStrategy(...)` seçim mantığını Spring üzerinden “registry” mantığına taşımak
- Shipping tarafına “tutar bazlı” veya “ağırlık bazlı” implementasyonlar eklemek
- Product cache’ini memory yerine dış cache ile yönetmek (gerektiğinde)

---

## 20. Kapanış
Bu sepet fiyatlandırma projesi; Flyweight, Strategy, Bridge ve Facade Pattern’lerinin **aynı problem üzerinde** nasıl birlikte çalıştığını net şekilde gösterir.
İlk bakışta “sepeti hesapla” gibi tek bir iş gibi görünen süreç; ürün yönetimi, indirim seçimi, kargo hesaplama ve sonuç kırılımı gibi bağımsız sorumluluklar içerir.
Bu sorumlulukları tek bir sınıfta toplamak kısa vadede hızlı gibi dursa da, ürün/indirim/kargo çeşitliliği arttıkça bakım maliyeti katlanarak büyür.

Bu mimaride her desen, soruna farklı bir yerden çözüm getirir:
- **Flyweight** ile ürünler paylaşımlı hale gelir; aynı ürün için tekrar tekrar nesne üretmek yerine cache’den reuse edilir.
- **Strategy** ile indirim algoritmaları “tak‑çıkar” hale gelir; yeni bir indirim eklemek mevcut akışı bozmak yerine yeni bir sınıf eklemekle çözülür.
- **Bridge** ile checkout (abstraction) ve shipping (implementation) birbirinden ayrılır; kargo kuralları değişirken ödeme/toplam hesap akışı sabit kalır.
- **Facade** ile tüm bu parçalar dışarıya tek bir sade API olarak sunulur; Controller/Service katmanı ayrıntılara gömülmez.

Asıl mimari değer, tek tek desenlerden ziyade **birlikte kurdukları sınırlar**dır:
- Domain model (`Cart`, `Product`) gereksiz “iş kuralı” yükünü taşımaz.
- Hesaplama algoritmaları birbirinden ayrıldığı için test edilebilirlik artar.
- Yeni özellik eklemek, “mevcut kodu değiştirmek” yerine çoğu zaman “yeni sınıf eklemek” ile mümkün olur (Open/Closed).
- Frontend (Angular) ile backend (Spring Boot) arasında akış anlaşılır kalır; UI sadece payload üretir, karmaşık orkestrasyon backend’de tek bir noktada yönetilir.

Özetle bu proje şunu öğretir: Tasarım desenleri, büyük sistemlerdeki karmaşıklığın erken sinyallerini küçük bir projede doğru şekilde yönetebilmek için güçlü bir araçtır.
Bugün 4 indirim türü var; yarın 40’a çıktığında da aynı yapı bozulmadan büyüyebilir.

Bir sonraki adım olarak (istersen):
- `resolveStrategy(...)` kısmını “registry/DI ile strategy seçimi” şeklinde refactor ederek yeni indirim eklemeyi tamamen konfigürasyonel hale getirebiliriz.
- Shipping tarafına “tutar bazlı” veya “ürün adedi bazlı” implementasyon ekleyip Bridge’in esnekliğini daha görünür yapabiliriz.
