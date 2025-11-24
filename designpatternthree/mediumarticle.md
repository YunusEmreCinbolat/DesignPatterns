 # DesignPatternThree – Composite, Proxy ve Chain of Responsibility ile Modern Sipariş Yönetimi
Bu makale, DesignPatternThree projesini tamamen yeniden yazılmış profesyonel bir teknik dokümantasyon olarak sunar.
 Amaç, projede kullanılan üç ana tasarım desenini hem teorik hem de kod tabanlı bir perspektifte açıklamaktır.
4: Bu desenler: Composite Pattern, Proxy Pattern ve Chain of Responsibility Pattern’dir.
7: ---
8: ## Giriş
9: Günümüzde e-ticaret sistemlerinin artan karmaşıklığı, ürün modellemeyi ve sipariş akışını yönetmeyi zorlaştırmaktadır.
10: Gerçek bir sipariş oluşturma süreci onlarca farklı iş kuralı içerir.
11: Aynı şekilde ürün tarafında da tekil ürünler, paketler, setler ve iç içe geçmiş kombinasyonlar bulunabilir.
12: Bu nedenle sağlam bir yazılım mimarisi tasarlamak kritik öneme sahiptir.
13: DesignPatternThree projesi, bu ihtiyaca cevap veren örnek bir mimari sunar.
14: Ürün modellemesi Composite Pattern ile yapılmış, pahalı fiyat hesaplamaları Proxy Pattern ile optimize edilmiştir.
15: Ayrıca sipariş oluşturma ve sipariş durum geçişleri Chain of Responsibility Pattern ile yönetilmiştir.
16: Böylece uygulama hem genişletilebilir hem de yönetilebilir bir hâle gelmektedir.
17: ---
18: ## 1. Composite Pattern – Ürün Ağaçlarının Temeli
19: Composite Pattern, “bütün–parça” ilişkisini tek bir soyutlama üzerinden ele alan bir tasarım desenidir.
20: Bu desen özellikle ürün yapılandırmalarında sıklıkla kullanılır.
21: Tasarım, hem tekil hem de bileşik ürünlerin aynı interface üzerinden yönetilebilmesini sağlar.
22: Bu da sistemde önemli bir esneklik ve bütünlük sağlar.
23: DesignPatternThree içindeki Composite yapısı, `ProductComponent` interface’i ile başlar.
24: Tüm ürün türleri bu interface’i implement eder.
25: Böylece SingleProduct, ProductBundle veya gelecekte eklenebilecek başka herhangi bir ürün türü aynı tip olarak davranır.
26: Bu tasarım Liskov’un Yerine Geçme İlkesine birebir uyumludur.
27: Çünkü her ürün bileşeni, beklenen davranışı sorunsuz şekilde yerine getirir.
28: ---
29: ## 1.1 ProductComponent – Ortak Sözleşme
30: Bu interface, tüm ürün bileşenlerinin sahip olması gereken metotları tanımlar.
31: Aşağıda projenin gerçek kodundan alınmış hâlidir:
32: ```java
33: public interface ProductComponent {
34:     String getName();
35:     double getPrice();
36:     void print();
37: }
38: ```
39: Bu yapı sayesinde, sistemi oluşturan tüm ürünler aynı fonksiyonlere sahip olur.
40: `getPrice()` fiyat hesaplamanın, `print()` ise debug çıktılarının ortak temelidir.
41: Böylece ürün ağaçları üzerinde recursive işlemler uygulanabilir.
42: Özellikle ProductBundle içinde bu soyutlama büyük avantaj sağlar.
43: ---
44: ## 1.2 SingleProduct – Composite İçindeki Yaprak
45: SingleProduct sınıfı, Composite Pattern’in Leaf (Yaprak) rolüdür.
46: İçinde alt eleman bulunmaz ve fiyatı doğrudan geri döndürür.
47: Projedeki gerçek kod şu şekildedir:
48: ```java
49: public class SingleProduct implements ProductComponent {
50:     private final String name;
51:     private final double price;
52:
53:     public SingleProduct(String name, double price) {
54:         this.name = name;
55:         this.price = price;
56:     }
57:
58:     @Override
59:     public double getPrice() { return price; }
60:
61:     @Override
62:     public String getName() { return name; }
63:
64:     @Override
65:     public void print() {
66:         System.out.println("Product: " + name + " - Price: " + price);
67:     }
68: }
69: ```
70: Bu sınıfın basitliği ürüne dair temel yapıyı oluşturur.
71: Leaf sınıfı olmasına rağmen Composite yapının önemli bir parçasıdır.
72: ---
73: ## 1.3 ProductBundle – Ağaç İçindeki Bileşik Yapı
74: ProductBundle Composite Pattern’in Composite tarafını temsil eder.
75: İçinde birden fazla ProductComponent barındırabilir.
76: Bu bileşenler ister SingleProduct ister başka bir ProductBundle olabilir.
77: Böylece iç içe geçmiş çok seviyeli ürün ağaçları oluşturulabilir.
78: Projenin gerçek kodu aşağıdaki gibidir:
79: ```java
80: public class ProductBundle implements ProductComponent {
81:     private final String name;
82:     private final List<ProductComponent> items = new ArrayList<>();
83:
84:     public ProductBundle(String name) { this.name = name; }
85:
86:     public void add(ProductComponent component) { items.add(component); }
87:
88:     @Override
89:     public double getPrice() {
90:         return items.stream().mapToDouble(ProductComponent::getPrice).sum();
91:     }
92:
93:     @Override
94:     public String getName() { return name; }
95:
96:     @Override
97:     public void print() {
98:         System.out.println("Bundle: " + name);
99:         items.forEach(ProductComponent::print);
100:     }
101: }
102: ```
103: Bu yapıda dikkat edilmesi gereken en önemli nokta recursive fiyat toplamasıdır.
104: Bundle içinde başka bundle’lar olsa bile sistem fiyatı doğru hesaplar.
105: Bu Composite Pattern’in en güçlü yönlerinden biridir.
106: ---
107: ## 1.4 Composite Pattern’in Kazanımları
108: Composite Pattern sayesinde ürün modeli dinamik hâle gelir.
109: Bundle ürünler oluşturmak basit bir işlem hâline gelir.
110: Tekil ürünler ve paket ürünler aynı interface’e sahiptir.
111: Bu da yüksek soyutlama ve sade kod anlamına gelir.
112: Composite Pattern özellikle büyük ölçekli e-ticaret uygulamalarında kaçınılmazdır.
113: Çünkü kampanyalar, paketler ve setler sürekli değişir.
114: Böyle bir değişken modele en uygun desen Composite Pattern’dir.
115: ---
116: ## 2. Proxy Pattern – Ürün Fiyatlarını Cache’lemek
117: Composite fiyat hesaplaması maliyetli olabilir.
118: Çok geniş ürün ağaçlarında her seferinde fiyatı toplamak backend için yük oluşturabilir.
119: PriceCacheProxy bu problemi çözmek için uygulanmıştır.
120: Proxy Pattern, gerçek nesneye ek bir davranış sararak çalışır.

121: PriceCacheProxy sınıfı, Composite yapıların fiyatını yalnızca bir kez hesaplar.
122: Böylece tekrar eden fiyat isteklerinde hesaplama maliyetinden tasarruf edilir.
123: Bu yaklaşım performansı ciddi şekilde artırır.
124: Proxy Pattern burada lazy-loading mantığıyla uygulanmıştır.
125: ---
126: ## 2.1 PriceCacheProxy – Gerçek Kod Analizi
127: Projede kullanılan PriceCacheProxy’nin kodu şu şekildedir:
128: ```java
129: public class PriceCacheProxy implements ProductComponent {
130:     private final ProductComponent realProduct;
131:     private Double cachedPrice = null;
132:
133:     public PriceCacheProxy(ProductComponent realProduct) {
134:         this.realProduct = realProduct;
135:     }
136:
137:     @Override
138:     public String getName() { return realProduct.getName(); }
139:
140:     @Override
141:     public double getPrice() {
142:         if (cachedPrice == null) {
143:             System.out.println("[Proxy] Fiyat ilk kez hesaplanıyor...");
144:             cachedPrice = realProduct.getPrice();
145:         } else {
146:             System.out.println("[Proxy] Cached fiyat kullanılıyor...");
147:         }
148:         return cachedPrice;
149:     }
150:
151:     @Override
152:     public void print() { realProduct.print(); }
153:
154:     public void invalidate() { cachedPrice = null; }
155: }
156: ```
157: Bu sınıf, Composite fiyat hesaplamasında ciddi bir optimizasyon sağlar.
158: Özellikle bundle içinde yüzlerce ürün olduğunda fark daha net hissedilir.
159: Ayrıca `invalidate()` fonksiyonu, ürün fiyatı değiştiğinde cache’i temizlemek için kullanılır.
160: Bu sayede sistem hem doğru hem de hızlı çalışır.
161: ---
162: ## 2.2 Proxy Pattern’in Sağladığı Faydalar
163: Proxy Pattern kullanımı birçok avantaj sunar:
164: - Fiyat hesaplama yükü azalır.
165: - Composite yapı büyüdükçe performans kaybı yaşanmaz.
166: - Hesaplama mantığı ve cache mantığı tamamen ayrılır.
167: - Kod okunabilirliği artar.
168: - Davranış genişletme çok kolay hâle gelir.
169: Ayrıca Proxy Pattern, sistemde gelecekte yeni optimizasyonlar eklemeyi de kolaylaştırır.
170: ---
171: ## 3. Chain of Responsibility Pattern – Sipariş İşleme Boru Hattı
172: Chain of Responsibility, bir işlemi ardışık adımlara bölmek için kullanılan bir tasarım desenidir.
173: DesignPatternThree projesinde sipariş oluşturma süreci tamamen Chain yapısıyla düzenlenmiştir.
174: Bu sayede her iş kuralı kendi handler sınıfında bulunur.
175: Handler’lar birbirine zincir şeklinde bağlanır.
176: Sipariş, zincirin en başından içeri girer ve adımlar sırayla uygulanır.
177: Eğer herhangi bir handler siparişi reddederse zincir durur.
178: Bu yaklaşım hem temizdir hem de genişletilebilirliği son derece kolaylaştırır.
179: ---
180: ## 3.1 OrderHandler – Tüm Handlerların Temel Sınıfı
181: Aşağıda projenin gerçek OrderHandler sınıfı görülmektedir:
182: ```java
183: public abstract class OrderHandler {
184:     protected OrderHandler next;
185:
186:     public OrderHandler setNext(OrderHandler next) {
187:         this.next = next;
188:         return next;
189:     }
190:
191:     public final void handle(Order order) {
192:         if (doHandle(order) && next != null) {
193:             next.handle(order);
194:         }
195:     }
196:
197:     protected abstract boolean doHandle(Order order);
198: }
199: ```
200: Bu sınıf design pattern’in çekirdeğini oluşturur.
201: `setNext` fonksiyonu zincirin kurulmasını sağlar.
202: `handle` fonksiyonu ise zincirin akışını yönetir.
203: `doHandle` ise her handler’ın kendi iş kuralını uyguladığı yerdir.
204: Eğer `doHandle` false dönerse zincir durur.
205: Bu mekanizma sipariş oluşturma sürecini mükemmel bir şekilde kontrol eder.
206: ---
207: ## 3.2 FraudHandler – Fraud Kontrolü
208: Siparişin fraud içerip içermediği ilk adımda kontrol edilir.
209: ```java
210: public class FraudHandler extends OrderHandler {
211:     @Override
212:     protected boolean doHandle(Order order) {
213:         if (!order.isNotFraud()) {
214:             System.out.println("[FraudHandler] Fraud tespit edildi.");
215:             order.markRejected("Fraud detected");
216:             return false;
217:         }
218:         return true;
219:     }
220: }
221: ```
222: Eğer fraud şüphesi varsa zincir derhal durur.
223: Bu, güvenlik açısından kritik bir adımdır.
224: ---
225: ## 3.3 PaymentHandler – Ödeme Doğrulaması
226: Fraud kontrolü geçildikten sonra ödeme kontrolü yapılır.
227: ```java
228: public class PaymentHandler extends OrderHandler {
229:     @Override
230:     protected boolean doHandle(Order order) {
231:         if (!order.isPaymentCompleted()) {
232:             System.out.println("[PaymentHandler] Ödeme başarısız.");
233:             order.markRejected("Payment failed");
234:             return false;
235:         }
236:         return true;
237:     }
238: }
239: ```
240: Eğer ödeme başarısızsa sipariş reddedilir ve zincir sonlanır.

241: PaymentHandler sipariş akışındaki en kritik aşamalardan biridir.
242: Çünkü ödeme gerçekleşmeden siparişin ilerlemesine izin verilmez.
243: Bu mekanizma Chain of Responsibility’nin doğal bir kullanım örneğidir.
244: ---
245: ## 3.4 StockHandler – Stok Doğrulaması
246: PaymentHandler geçildikten sonra stok kontrolü yapılır.
247: ```java
248: public class StockHandler extends OrderHandler {
249:     @Override
250:     protected boolean doHandle(Order order) {
251:         System.out.println("[StockHandler] Stok kontrol edildi.");
252:         return true;
253:     }
254: }
255: ```
256: Bu örnekte basit bir stok kontrolü yapılmaktadır.
257: Gerçek sistemde ürün bazlı stok düşümü de bu aşamada yapılabilir.
258: Stok yetersizse burada zincir durdurulabilir.
259: Bu tasarım ileride çok daha karmaşık stok politikalarını destekleyebilir.
260: ---
261: ## 3.5 TaxHandler – KDV Hesabı
262: Stok kontrolünden sonra siparişin vergisi hesaplanır.
263: Türkiye’de KDV genelde %20 olduğundan örnek hesaplama şu şekildedir:
264: ```java
265: public class TaxHandler extends OrderHandler {
266:     @Override
267:     protected boolean doHandle(Order order) {
268:         double total = order.getTotalPrice();
269:         double tax = total * 0.20;
270:         order.setTotalPrice(total + tax);
271:         System.out.println("[TaxHandler] KDV uygulandı.");
272:         return true;
273:     }
274: }
275: ```
276: Bu aşama fiyatın doğru hesaplanması açısından önemlidir.
277: Composite yapıdaki ürünlerin fiyatları hesaplanmış olsa bile vergi her zaman sonradır.
278: Vergi ekleme aşaması Chain içinde ayrı bir adım olarak modellenmiştir.
279: Bu da kodun sadeleşmesini sağlar.
280: ---
281: ## 3.6 DiscountHandler – Büyük Sipariş İndirimi
282: DiscountHandler belirli bir eşik üzerinde indirim uygular.
283: Projedeki gerçek örnek:
284: ```java
285: public class DiscountHandler extends OrderHandler {
286:     @Override
287:     protected boolean doHandle(Order order) {
288:         double total = order.getTotalPrice();
289:         if (total >= 20000) {
290:             double discount = total * 0.05;
291:             order.setTotalPrice(total - discount);
292:             System.out.println("[DiscountHandler] %5 indirim uygulandı.");
293:         }
294:         return true;
295:     }
296: }
297: ```
298: Burada threshold bazlı bir indirim modeli uygulanmıştır.
299: Chain of Responsibility bu tarz indirimi modüler hâle getirir.
300: İstenirse farklı indirim handler’ları da eklenebilir.
301: ---
302: ## 3.7 ShippingHandler – Kargo Ücreti Uygulaması
303: Kargo, toplam fiyat belirli bir eşik altındaysa eklenen bir maliyettir.
304: Kod şu şekildedir:
305: ```java
306: public class ShippingHandler extends OrderHandler {
307:     @Override
308:     protected boolean doHandle(Order order) {
309:         double total = order.getTotalPrice();
310:         if (total >= 1000) {
311:             System.out.println("[ShippingHandler] Ücretsiz kargo uygulandı.");
312:         } else {
313:             order.setTotalPrice(total + 49.90);
314:             System.out.println("[ShippingHandler] Kargo ücreti eklendi.");
315:         }
316:         return true;
317:     }
318: }
319: ```
320: Ücretsiz kargo sınırları ülkeden ülkeye değişir.
321: Bu handler’ın ayrı olması bu kuralların kolayca güncellenebilmesini sağlar.
322: ---
323: ## 3.8 CreateOrderHandler – Siparişin Kalıcı Olarak Kaydedilmesi
324: Zincirin en sonunda sipariş kaydedilir:
325: ```java
326: public class CreateOrderHandler extends OrderHandler {
327:     private final OrderRepository repo;
328:
329:     public CreateOrderHandler(OrderRepository repo) {
330:         this.repo = repo;
331:     }
332:
333:     @Override
334:     protected boolean doHandle(Order order) {
335:         if (order.getStatus() == OrderStatus.REJECTED) {
336:             System.out.println("[CreateOrderHandler] Reddedilen sipariş kaydedilmedi.");
337:             return false;
338:         }
339:         repo.save(order);
340:         System.out.println("[CreateOrderHandler] Sipariş kaydedildi.");
341:         return true;
342:     }
343: }
344: ```
345: Bu aşamada veri veritabanına yazılır.
346: Handler zinciri başarılı şekilde tamamlandıysa sipariş başarıyla oluşturulmuş olur.
347: Bu yapı, sipariş akışını temiz ve adım adım yönetilebilir hâle getirir.
348: ---
349: ## 3.9 Chain of Responsibility’nin Mimariye Etkisi
350: Chain yapısı sayesinde:
351: - Her kural bağımsız bir sınıfa taşınır.
352: - Kod daha okunabilir hâle gelir.
353: - Yeni kurallar eklemek kolaylaşır.
354: - Zincirin sırası değiştirilebilir.
355: - Her adımın test edilmesi ayrı ayrı mümkündür.
356: Bu nedenle Chain of Responsibility, iş kuralı yoğun sistemlerde ideal bir desendir.
357: DesignPatternThree bunu başarılı bir şekilde uygular.
358: Sipariş oluşturma süreci tamamen modüler hâle getirilmiştir.
359: Bu da gerçek dünyada karşılaşılacak tüm iş senaryolarına kolay uyum sağlar.
360: ---

361: ## 4. Sipariş Durum Yönetimi – İkinci Chain of Responsibility
362: DesignPatternThree yalnızca sipariş oluşturmayı değil, siparişin durum yaşam döngüsünü de Chain of Responsibility ile yönetir.
363: Bu ikinci chain, siparişin RECEIVED → PREPARING → SHIPPED → OUT_FOR_DELIVERY → DELIVERED şeklinde ilerlemesini sağlar.
364: Bu mimari sayesinde sipariş durum geçişleri modüler bir hâle gelir.
365: Durum işlemleri kontrol edilebilir, genişletilebilir ve test edilebilir olur.
366: ---
367: ## 4.1 OrderStatusHandler – Durum Chain’inin Arayüzü
368: Aşağıda durum chain’inin temel interface’i bulunmaktadır:
369: ```java
370: public interface OrderStatusHandler {
371:     void handle(Order order);
372:     void setNext(OrderStatusHandler next);
373: }
374: ```
375: Bu interface, tüm durum handler’larının implement ettiği sözleşmedir.
376: `handle()` metodu siparişin durumunu ilerletmek için çağrılır.
377: `setNext()` zincirin kurulmasını sağlar.
378: ---
379: ## 4.2 BaseOrderStatusHandler – Ortak Davranış
380: Bu sınıf diğer handler’lara ortak davranışı sağlar:
381: ```java
382: public abstract class BaseOrderStatusHandler implements OrderStatusHandler {
383:     protected OrderStatusHandler next;
384:
385:     @Override
386:     public void setNext(OrderStatusHandler next) {
387:         this.next = next;
388:     }
389:
390:     protected void forward(Order order) {
391:         if (next != null) next.handle(order);
392:     }
393: }
394: ```
395: Bu yapı sayesinde, her handler yalnızca kendi durumuyla ilgilenir.
396: Diğer sorumluluklar temel sınıf tarafından karşılanır.
397: ---
398: ## 4.3 ReceivedHandler – İlk Durum
399: Siparişin başlangıç durumudur.
400: ```java
401: public class ReceivedHandler extends BaseOrderStatusHandler {
402:     @Override
403:     public void handle(Order order) {
404:         if (order.getStatus() == OrderStatus.RECEIVED) {
405:             order.setStatus(OrderStatus.PREPARING);
406:             return;
407:         }
408:         forward(order);
409:     }
410: }
411: ```
412: Bu handler, siparişin ilk durumdan ikinci duruma geçişini kontrol eder.
413: ---
414: ## 4.4 PreparingHandler – Hazırlanıyor
415: ```java
416: public class PreparingHandler extends BaseOrderStatusHandler {
417:     @Override
418:     public void handle(Order order) {
419:         if (order.getStatus() == OrderStatus.PREPARING) {
420:             order.setStatus(OrderStatus.SHIPPED);
421:             return;
422:         }
423:         forward(order);
424:     }
425: }
426: ```
427: Sipariş hazırlanır ve paketleme aşamasına geçilir.
428: ---
429: ## 4.5 ShippedHandler – Kargoda
430: ```java
431: public class ShippedHandler extends BaseOrderStatusHandler {
432:     @Override
433:     public void handle(Order order) {
434:         if (order.getStatus() == OrderStatus.SHIPPED) {
435:             order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
436:             return;
437:         }
438:         forward(order);
439:     }
440: }
441: ```
442: Bu aşamada sipariş dağıtım merkezine ulaşmıştır.
443: ---
444: ## 4.6 OutForDeliveryHandler – Dağıtıma Çıktı
445: ```java
446: public class OutForDeliveryHandler extends BaseOrderStatusHandler {
447:     @Override
448:     public void handle(Order order) {
449:         if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
450:             order.setStatus(OrderStatus.DELIVERED);
451:             return;
452:         }
453:         forward(order);
454:     }
455: }
456: ```
457: Sipariş müşterinin kapısına doğru yola çıkmıştır.
458: ---
459: ## 4.7 DeliveredHandler – Teslim Edildi
460: ```java
461: public class DeliveredHandler extends BaseOrderStatusHandler {
462:     @Override
463:     public void handle(Order order) {
464:         if (order.getStatus() == OrderStatus.DELIVERED) return;
465:         forward(order);
466:     }
467: }
468: ```
469: Bu final handler siparişin artık tamamlandığını belirtir.
470: Daha fazla ilerleme olmadığı için zincir burada durur.
471: ---
472: ## 4.8 OrderStatusService – Durum Zincirinin Kurulması
473: Bu service tüm durum handler’larını birbirine bağlar.
474: ```java
475: received.setNext(preparing);
476: preparing.setNext(shipped);
477: shipped.setNext(outForDelivery);
478: outForDelivery.setNext(delivered);
479: received.handle(order);
480: ```

481: ## 5. Controller Katmanı – İki Zincirin Tetiklendiği Yer
482: Controller katmanı, backend’e gelen istekleri karşılayan ve chain mekanizmalarını tetikleyen yapıdır.
483: Bu projede iki önemli endpoint bulunmaktadır: `/orders` ve `/orders/{id}/next`.
484: İlki sipariş oluşturmayı, ikincisi sipariş durum geçişini yönetir.
485: Kod aşağıdaki gibidir:
486: ```java
487: @PostMapping
488: public Order createOrder(@RequestBody Order order) {
489:     orderService.processOrder(order);
490:     orderRepository.save(order);
491:     return order;
492: }
493: ```
494: Bu fonksiyon sipariş oluşturma chain’ini çalıştırır.
495: Eğer chain sırasında sipariş reddedilirse bu işlem repository katmanına yansıtılmaz.
496: Çünkü CreateOrderHandler reddedilen siparişleri kaydetmez.
497: Bu, Chain of Responsibility’nin mimari bütünlüğüne katkı sunduğu noktalardan biridir.
498: ---
499: ## 5.1 Sipariş Durumunu İlerletme Endpoint’i
500: Siparişin durumunu bir sonraki aşamaya taşımak için şu endpoint kullanılır:
501: ```java
502: @PostMapping("/{id}/next")
503: public Order nextStatus(@PathVariable Long id) {
504:     Order order = orderRepository.findById(id);
505:     orderStatusService.nextStep(order);
506:     orderRepository.save(order);
507:     return order;
508: }
509: ```
510: Burada durum zinciri çalıştırılır.
511: Sipariş RECEIVED durumundaysa PREPARING’e geçer; PREPARING ise SHIPPED olur.
512: Zincir, final durum olan DELIVERED aşamasına kadar ilerler.
513: Bu endpoint, gerçek dünyada sipariş takip sistemlerinin kalbini temsil eder.
514: ---
515: ## 6. Genel Mimari Değerlendirme
516: DesignPatternThree, üç büyük tasarım desenini tek bir modern sipariş yönetim sisteminde birleştirir.
517: Composite Pattern → Ürünlerin ağaç yapısıyla modellenmesini sağlar.
518: Proxy Pattern → Fiyat hesaplamasında performans optimizasyonu getirir.
519: Chain of Responsibility → Sipariş akışını ve sipariş durum değişikliklerini adımlara ayırır.
520: Bu mimarinin en büyük gücü, tüm bu desenlerin birbirine zarar vermeden birlikte çalışabilmesidir.
521: Bu sayede sistem hem genişletilebilir hem de okunabilir kalır.
522: ---
523: ## 7. Neden Bu Mimarinin Gerçek Dünyada Karşılığı Büyük?
524: Çünkü gerçek e-ticaret sistemlerinde iş kuralları sürekli değişir.
525: Yeni kampanyalar eklenir, fiyat hesaplama modeli değişir, fraud politikaları güncellenir.
526: Bu gibi durumlarda chain yapısı sayesinde yalnızca ilgili handler değiştirilir.
527: Ürün modellemesi tarafında bundle yapıları son derece önemlidir.
528: Composite Pattern olmadan paket ürünleri yönetmek çok zorlaşır.
529: Ayrıca Proxy Pattern gibi performans desenleri büyük trafik altındaki sistemlerde hayati önem taşır.
530: Bu nedenle DesignPatternThree mimari açıdan modern, güçlü ve sürdürülebilir bir çözümdür.
531: ---
532: ## 8. Sonuç
533: Bu makale boyunca Composite, Proxy ve Chain of Responsibility desenlerinin nasıl uygulandığını gördük.
534: Ayrıca bu desenlerin bir arada nasıl çalıştığını, kod örnekleri üzerinden adım adım inceledik.
535: DesignPatternThree projesi, temiz mimariyi gerçek bir senaryoda başarıyla göstermektedir.
536: Bu yapıyı temel alarak çok daha büyük bir e-ticaret motoru kolayca inşa edilebilir.
