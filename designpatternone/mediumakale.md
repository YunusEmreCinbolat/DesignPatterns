# ☁ Mini E-Ticaret Uygulamasıyla Design Pattern Öğrenin: Factory, Strategy, Observer ve Null Pointer Yönetimi

##  Giriş

Bir yazılım geliştirici olarak projeler büyüdükçe karşılaşılan en temel sorunlardan biri, **kodun karmaşıklaşması** ve **yönetilebilirliğini kaybetmesidir**.  
İşte bu noktada **Design Pattern (Tasarım Desenleri)** devreye girer. Tasarım desenleri, tekrar eden yazılım problemlerine yeniden kullanılabilir çözümler sunar.  

Bu makalede, Spring Boot ve Angular tabanlı mini e-ticaret projemde kullandığım üç önemli pattern'i ve **Enum + Null Object yaklaşımıyla NullPointerException yönetimini** ele alacağız.

Kullanılan desenler:

-  **Factory Pattern** → Ürün nesnelerini dinamik olarak oluşturmak  
-  **Strategy Pattern** → Farklı indirim stratejilerini yönetmek  
-  **Observer Pattern** → Gerçek zamanlı sepet güncellemeleri sağlamak  
-  **Enum & Null Object** → Tip güvenliği ve null değerlerle güvenli çalışma

---

##  Factory Pattern — Ürün Üretiminde Esneklik

###  Neden Kullandım?

Uygulamada farklı türde ürünler (kitap, elektronik, giyim vb.) bulunuyor.  
Her biri farklı alanlara sahip: kitapların `author` bilgisi, elektroniklerin `warrantyMonths` değeri gibi.  
Bu çeşitlilik, doğrudan `new` operatörüyle nesne oluşturulduğunda kodun her yerde dağılmasına neden oluyordu.

###  Çözüm: ProductFactory

```java

public class ProductFactory {

    public static Product create(ProductType type, String id, String name, BigDecimal price, Map<String, Object> extra) {

        if (type == null) {
            throw new IllegalArgumentException("ProductType cannot be null");
        }

        return switch (type) {
            case BOOK -> new Book(
                    id, name, price,
                    (String) extra.getOrDefault("author", "Unknown")
            );

            case ELECTRONIC -> new Electronic(
                    id, name, price,
                    (int) extra.getOrDefault("warrantyMonths", 24)
            );

            case CLOTHING -> new Clothing(
                    id, name, price,
                    (String) extra.getOrDefault("size", "M")
            );
        };
    }
}
```

###  Açıklama

`ProductFactory`, ürün oluşturma sürecini tek bir merkezde toplar.  
Bu sayede:

- Yeni ürün tipleri eklemek çok kolay hale gelir,  
- Kodun diğer bölümleri “hangi tür nesnenin oluşturulduğuyla” ilgilenmez,  
- Uygulama **Open/Closed Principle**’a uygun hale gelir.  

Gerçek dünyada bu desen, **müşteri siparişi**, **ürün kaydı** veya **fatura oluşturma** gibi nesne çeşitliliği olan tüm sistemlerde büyük kolaylık sağlar.

---

##  Strategy Pattern — Esnek İndirim Hesaplaması

###  Neden Kullandım?

İndirim hesaplama, e-ticaret uygulamalarında sık sık değişen bir iş kuralıdır.  
Bazen yüzde 10 indirim, bazen sabit 50₺ indirim, bazen de ücretsiz kargo kampanyası devreye girer.  
Bu durum klasik `if-else` zincirleriyle yönetilirse kod hızla karmaşıklaşır.

###  Çözüm: DiscountStrategy Arayüzü

```java
public interface DiscountStrategy {
    BigDecimal applyTo(BigDecimal itemsTotal, BigDecimal shipping);
}
```

Tüm indirim stratejileri bu arayüzü uygular.

###  Örnek Stratejiler

```java
public class FixedAmountDiscount implements DiscountStrategy {
    public BigDecimal applyTo(BigDecimal total, BigDecimal shipping) {
        return total.subtract(new BigDecimal("50")).max(BigDecimal.ZERO).add(shipping);
    }
}
```

```java
public class NoDiscount implements DiscountStrategy {
    public BigDecimal applyTo(BigDecimal total, BigDecimal shipping) {
        System.out.println("İndirim uygulanmadı.");
        return total.add(shipping);
    }
}
```

###  Neden Strategy Pattern?

Bu yapı sayesinde:

- Her indirim tipi ayrı sınıfa taşınarak kod sadeleşir,  
- Yeni indirim eklemek için mevcut kodlar değiştirilmez, sadece yeni bir sınıf yazılır,  
- Sistem çalışma anında hangi stratejiyi kullanacağını seçebilir (**runtime flexibility**).  

Gerçek uygulamada bu desen, **kampanya sistemleri**, **ödeme planları**, **kargo ücret hesaplamaları** gibi senaryolarda sıklıkla kullanılır.

---

##  Observer Pattern — Angular’da Gerçek Zamanlı Sepet Güncellemesi

###  Neden Kullandım?

Kullanıcı sepete ürün eklediğinde, bu değişikliğin UI üzerinde anında yansımasını istedim.  
Örneğin kullanıcı yeni bir ürün eklediğinde toplam fiyat, sepet simgesindeki ürün sayısı veya “checkout” sayfası otomatik güncellenmeliydi.

###  Çözüm: BehaviorSubject ile Reaktif Yaklaşım

```typescript
private cartSubject = new BehaviorSubject<Product[]>([]);
cart$ = this.cartSubject.asObservable();

addToCart(product: Product) {
  this.cartItems.push(product);
  this.cartSubject.next(this.cartItems);
}
```

###  Açıklama

- `BehaviorSubject`, Angular’da **Observer Pattern**’in temelidir.  
- `cartSubject` tüm bileşenlere güncel sepet durumunu yayınlar.  
- `cart$` observable’ına abone olan her bileşen, değişiklikleri otomatik olarak alır.

Bu sayede **UI reaktif hale gelir**.  
Herhangi bir bileşende manuel “refresh” işlemi yapmadan tüm güncellemeler otomatik olarak yayılır.

Gerçek dünya karşılığı: Netflix’te film listesi değiştiğinde veya bir dashboard’da yeni veri geldiğinde ekranın kendini güncellemesi bu prensiple çalışır.

---

##  Enum ve Null Object Pattern — NullPointerException’a Karşı

###  Enum ile Tip Güvenliği

```java
public enum DiscountType {
    PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING
}
```

`Enum`, yalnızca geçerli indirim türlerinin kullanılmasına izin verir.  
Bu sayede “yazım hatası” kaynaklı bug’lar ortadan kalkar.  

Örneğin `"PERCENTAGE"` yerine `"Percantage"` yazılsa bile hata derleme aşamasında yakalanır.

###  Null Object Pattern ile NullPointer Önleme

Java’da sık karşılaşılan `NullPointerException`, genellikle beklenmeyen `null` değerlerden kaynaklanır.  
Bu sorunu çözmek için **varsayılan bir davranış nesnesi** (`NoDiscount`) tanımladım.

```java
if (type == null) {
    return new NoDiscount();
}
```

Bu sayede hiçbir zaman `null` dönülmez, sistemin akışı kesilmeden devam eder.  
Bu yaklaşım, kodun hem güvenilirliğini hem de okunabilirliğini ciddi oranda artırır.

# Sonuç ve Kapanış

Bu mini e-ticaret projesinde kullandığım dört farklı tasarım deseni, uygulamanın hem mimari bütünlüğünü hem de sürdürülebilirliğini ciddi ölçüde artırdı.

Factory Pattern, ürün nesnelerinin oluşturulma sürecini tek bir merkezde toplayarak kodu daha esnek ve yönetilebilir hale getirdi. Yeni bir ürün tipi eklemek için artık sistemin diğer bölümlerine dokunmam gerekmiyor.

Strategy Pattern, indirim hesaplamalarını modüler hale getirerek farklı kampanya türlerinin kolayca eklenmesini sağladı. Kodun okunabilirliği arttı ve karmaşık if-else blokları tamamen ortadan kalktı.

Observer Pattern, Angular tarafında kullanıcı arayüzünü reaktif hale getirdi. Kullanıcı sepete ürün eklediğinde veya çıkardığında sayfa hiçbir manuel işlem yapılmadan otomatik olarak güncelleniyor.

Son olarak, Enum ve Null Object Pattern kullanımı, sistemde hataya neden olabilecek null değerleri güvenli bir şekilde yönetmemi sağladı. Böylece hem hata olasılığı azaldı hem de kod daha güvenilir hale geldi.
Tüm bu desenlerin bir arada kullanımı, projenin ölçeklenebilirliğini artırırken temiz kod prensiplerini koruyan, profesyonel bir mimari yapı oluşturdu.

