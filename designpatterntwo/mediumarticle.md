
#  Akıllı Cihaz Yönetimi Üzerinden State, Decorator ve Command Pattern’lerini Derinlemesine Öğrenin

Akıllı cihaz yönetimi, günümüzde Internet of Things (IoT), akıllı ev sistemleri ve otomasyon teknolojilerinin hızla yayılmasıyla giderek daha karmaşık bir hâle geliyor. Bir cihazın sadece açılıp kapanmasından çok daha fazlası söz konusu:  
- Farklı çalışma modları  
- Ek özellikler  
- Kullanıcı davranışlarının kayıt altına alınması  
- Aynı cihazın farklı kombinasyonlarda çalışabilmesi  

Bu nedenle **temiz**, **ölçeklenebilir** ve **esnek** bir yazılım mimarisi olmazsa olmaz hâle geliyor.

Bu projede tam da bu mimariyi sağlamak için üç önemli yazılım tasarım desenini bir arada kullandım:

- **State Pattern** → Cihazların mevcut durumuna göre farklı davranış sergilemesi  
- **Decorator Pattern** → Cihazlara dinamik ve sınırsız sayıda özellik eklenebilmesi  
- **Command Pattern** → Kullanıcı aksiyonlarının nesneleştirilip yönetilebilmesi  

---

#  1. State Pattern — Davranışı Durumlara Bölerek Yönetmek

##  Problem

Gerçek bir cihaz birden fazla çalışma moduna sahiptir. Örnek: **ON**, **OFF**, **STANDBY**.  
Ve her bir modda aynı komuta verdiği tepki farklıdır.

Örneğin bir TV düşünün:

- TV kapalıyken **turnOn()** → TV açılır  
- TV kapalıyken **turnOff()** → “zaten kapalı” uyarısı  
- TV standby modundayken **turnOn()** → standby’dan devam eder  
- TV açıkken **turnOn()** → “zaten açık” uyarısı  

Bu davranışları tek bir sınıfın içine doldurursanız ortaya şu tarz bir kabus çıkar:

```java
if (state == ON) { ... }
else if (state == OFF) { ... }
else if (state == STANDBY) { ... }
```

Bu yapı birkaç durum eklenince bile yönetilemez hâle gelir.

---

##  Çözüm: Durumları ayrı sınıflara ayırmak

Projede kullanılan **State arayüzü**:

```java
public interface State {
    void turnOn(Device device);
    void turnOff(Device device);
}
```

Bu sayede her durum kendi davranışını kendi sınıfında taşır.  
Bu mimari, davranışı **durumun içine gömmek** olarak bilinir.

---

##  OnState (Cihaz Açık)

```java
public class OnState implements State {
    @Override
    public void turnOn(Device device) {
        System.out.println(device.getName() + " is already ON");
    }

    @Override
    public void turnOff(Device device) {
        device.setState(new OffState());
        System.out.println(device.getName() + " turned OFF");
    }
}
```

 Açıklama:

- Cihaz zaten açık olduğu için `turnOn()` yalnızca bilgi verir.
- `turnOff()` cihazın durumunu değiştirir → **Davranış değişikliği tamamen state değişimine bağlıdır.**

---

##  OffState (Cihaz Kapalı)

```java
public class OffState implements State {
    @Override
    public void turnOn(Device device) {
        device.setState(new OnState());
        System.out.println(device.getName() + " turned ON");
    }

    @Override
    public void turnOff(Device device) {
        System.out.println(device.getName() + " is already OFF");
    }
}
```

✔ Açıklama:

- Kapalı cihaz açılabilir ama tekrar kapatılamaz, çünkü zaten kapalıdır.
- Kodda gereksiz kontrol yoktur — davranışları durum belirler.

---

##  StandbyState (Uyku Modu)

```java
public class StandbyState implements State {
    @Override
    public void turnOn(Device device) {
        device.setState(new OnState());
        System.out.println(device.getName() + " resumed from STANDBY");
    }

    @Override
    public void turnOff(Device device) {
        device.setState(new OffState());
        System.out.println(device.getName() + " turned OFF from STANDBY");
    }
}
```

✔ Açıklama:

- Standby'dan çıkma özel bir davranıştır.
- Gerçek hayata birebir uygun bir model: bir bilgisayarın uyku modundan açılması gibi.

---

##  State Pattern’in Sağladıkları

- If-else blokları tamamen ortadan kalktı.  
- Her durum kendi davranışını yönetir → **Davranış = Durum**  
- Yeni durum eklemek çok kolaydır.  
- Kod daha okunabilir, genişletilebilir ve bakımı kolay bir hâle gelir.

---

---

#  2. Decorator Pattern — Cihazlara Dinamik Özellik Eklemek

Akıllı cihazlar sadece “aç/kapa” yapan basit yapılar değil; çoğu zaman ek özelliklerle zenginleştirilmiş hâlde çalışıyorlar. Örneğin bir cihaz:

* Otomatik kapanma için **zamanlayıcıya** sahip olabilir,
* Daha az enerji tüketmesi için **enerji tasarrufu modunda** başlatılabilir,
* Her açılış/kapanışta **loglama** yapılabilir,
* Çalışma süresi, güç tüketimi gibi verileri **IoT telemetri** servisine gönderebilir.

Bu özelliklerin hepsi aynı “cihaz” üzerinde, farklı kombinasyonlarda kullanılabilir. İşte tam bu noktada şu soru ortaya çıkıyor:

> “Bu kadar farklı kombinasyonu, miras (inheritance) kullanarak nasıl yöneteceğim?”

##  Problem — Miras ile Özellik Yönetmenin Çıkmazı

Diyelim ki cihazımız `Light` sınıfı olsun. Eklemek istediğimiz özellikler:

* Timer
* EnergySaver
* Logger

Klasik kalıtım yaklaşımıyla gittiğimizde şu sınıflar ortaya çıkar:

```text
Light
LightWithTimer
LightWithEnergySaver
LightWithTimerAndEnergySaver
LightWithLogger
LightWithLoggerAndEnergySaver
LightWithTimerAndLogger
LightWithTimerAndLoggerAndEnergySaver
...
```

Her yeni özellik veya kombinasyon, yeni bir sınıf demek.
Bu da şu sorunlara yol açıyor:

*  **Class Explosion**: Kombinasyonlar arttıkça sınıf sayısı kontrolden çıkıyor.
*  Esneklik kaybı: Yeni bir özellik eklemek için birçok sınıfı yeniden yazman gerekiyor.
*  Tekrar eden kod: Aynı davranış farklı sınıflarda tekrar tekrar yazılıyor.
*  Bakım zorluğu: Hangi sınıfın hangi özellik kombinasyonuna sahip olduğunu takip etmek zorlaşıyor.

Bu problemi çözmek için, projede **Decorator Pattern** tercih edildi.

---

##  Çözüm: Cihazları “Sarmak” (Wrap) — Katmanlı Özellik Mimarisi

Decorator Pattern’in temel fikri şu:

> “Cihaza özellik eklemek için yeni alt sınıf üretmek yerine, cihazı saran (wrap eden) nesneler üretelim.”

Yani:

* **Cihaz**: Çekirdek davranış (Light, AirConditioner, Television, vb.)
* **Dekoratörler**: Ek özellik katmanları (Timer, EnergySaver, vb.)

Bu sayede:

* Cihazın ana sınıfı sade kalır.
* Ek özellikler bağımsız sınıflarda tanımlanır.
* Özellikler runtime’da (çalışma anında) esnek bir şekilde birbiriyle zincirlenebilir.

---

##  DeviceDecorator (Temel Dekoratör) — Ortak Altyapı

Projede tüm dekoratörlerin miras aldığı temel sınıf şu şekilde:

```java
public abstract class DeviceDecorator implements Device {
    protected Device decoratedDevice;

    public DeviceDecorator(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("Decorated device cannot be null!");
        }
        this.decoratedDevice = device;
    }

    @Override
    public void turnOn() {
        decoratedDevice.turnOn();
    }

    @Override
    public void turnOff() {
        decoratedDevice.turnOff();
    }

    @Override
    public String getName() {
        return decoratedDevice.getName();
    }

    @Override
    public void setState(State state) {
        decoratedDevice.setState(state);
    }

    @Override
    public State getState() {
        return decoratedDevice.getState();
    }

    @Override
    public String getDecorators() {
        return decoratedDevice.getDecorators() + " + " + this.getClass().getSimpleName();
    }
}
```

### Burada neler oluyor?

* `DeviceDecorator`, `Device` arayüzünü implemente ediyor → Yani her dekoratör de başlı başına bir **Device** gibi davranabiliyor.
* İçinde `decoratedDevice` adında gerçek cihaz (veya başka bir dekoratör) tutuluyor.
* Tüm temel metotlar (`turnOn`, `turnOff`, `getName` vs.) varsayılan olarak içteki cihaza delegasyon yapıyor.
* `getDecorators()` metodu, zincirdeki dekoratörleri üst üste ekleyerek *frontend’e gösterilebilir bir açıklama* üretmeye yardımcı oluyor.

Bu yapı, tüm dekoratörlerin üzerine inşa edildiği iskelet.

---

##  EnergySaverDecorator — Enerji Tasarrufu Katmanı

Enerji tasarrufu modunu cihaza ekleyen dekoratör şu şekilde:

```java
public class EnergySaverDecorator extends DeviceDecorator {

    public EnergySaverDecorator(Device device) {
        super(device);
    }

    @Override
    public void turnOn() {
        System.out.println("🔋 Energy saver mode enabled for " + getName());
        super.turnOn();
    }

    @Override
    public void turnOff() {
        System.out.println("🔋 Energy saver data saved for " + getName());
        super.turnOff();
    }
}
```

* `turnOn()` çağrıldığında:

  * Önce enerji tasarrufu modunun aktif olduğu loglanıyor.
  * Ardından `super.turnOn()` ile içteki cihaza (veya bir önceki dekoratöre) de açılma komutu iletiliyor.
* `turnOff()` çağrıldığında:

  * Enerji kullanımıyla ilgili verilerin kaydedildiği varsayılıyor.
  * Sonra gerçek cihaz kapatılıyor.

Bu sayede:

* Cihazın **temel koduna dokunmadan** enerji tasarrufu davranışı ekleniyor.
* İstenirse aynı dekoratör TV, klima, ışık vs. tüm cihaz türlerinde tekrar kullanılabiliyor.

---

##  TimerDecorator — Zamanlayıcı Katmanı

Zamanlayıcı özelliği ekleyen dekoratör:

```java
public class TimerDecorator extends DeviceDecorator {

    public TimerDecorator(Device device) {
        super(device);
    }

    @Override
    public void turnOn() {
        super.turnOn();
        System.out.println("⏱ Timer started for " + getName() + ": device will auto-off after 5 minutes");
    }

    @Override
    public void turnOff() {
        System.out.println("⏱ Timer stopped for " + getName());
        super.turnOff();
    }
}
```

* `turnOn()`:

  * Önce içteki cihaz açılıyor (veya enerji tasarrufu dekoratörü varsa onun davranışı da çalışıyor).
  * Sonra cihaza 5 dakika gibi sabit bir süreyle otomatik kapanma özelliği kazandırıldığı loglanıyor.
* `turnOff()`:

  * Zamanlayıcının durdurulduğu bilgisi veriliyor.
  * Sonrasında cihaz kapatılıyor.

Bu yapı çok gerçekçi bir senaryoyu simüle ediyor:
TV, klima veya ışıklarda sık görülen “X dakika sonra otomatik kapan” özelliği.

---

##  Çoklu Decorator Zinciri

Projede bir TV cihazına hem enerji tasarrufu modu hem de zamanlayıcı eklemek için şu yapı kullanılıyor:

```java
case TV:
    return new TimerDecorator(
             new EnergySaverDecorator(
                 new Television()
             ));
```

Bunu parçalayalım:

1. `new Television()` → Temel cihaz.
2. `new EnergySaverDecorator(new Television())` → Cihaz artık enerji tasarrufu özellikli.
3. `new TimerDecorator(energySaverWrappedTv)` → Aynı cihaza bir de zamanlayıcı katmanı ekleniyor.

Bu sayede:

*  TV aynı anda **Energy Saver + Timer** özelliklerine sahip oluyor.
*  TV’nin kendi kodu hiçbir ek özellikten **haberdar değil**.
*  Özellikler tamamen **dekoratörler üzerinden** yönetiliyor.
*  Farklı kombinasyonlar için yeni sınıf yazmaya gerek kalmıyor.

Örneğin yarın yeni bir `LoggingDecorator` yazarsan:

```java
Device tv = new LoggingDecorator(
              new TimerDecorator(
                new EnergySaverDecorator(
                  new Television()
                )));
```

gibi bir zincir ile, hem loglama, hem timer, hem enerji tasarrufu davranışını üst üste bindirebilirsin.

---

##  Mimari Açıdan Kazanımlar

Decorator Pattern sayesinde:

* **Open/Closed Principle** uygulanır:
  Mevcut sınıfları değiştirmeden, yeni dekoratör ekleyerek sistem genişletilebilir.
* Kod yeniden kullanımı artar:
  `TimerDecorator` herhangi bir `Device` ile çalışabilir (TV, klima, lamba fark etmeksizin).
* Cihaz sınıfları sade kalır:
  `Television`, `Light` veya `AirConditioner` sınıfları sadece temel davranışları içerir.
* Özellik kombinasyonları patlamaz:
  Her kombinasyon için yeni sınıf yazmak yerine, dekoratör zinciri kurarsın.

Kısacası, Decorator ile “**özellik eklemek**” ve “**özellikleri yönetmek**” konusunu, kod seviyesinde son derece esnek ve profesyonel bir hâle getirmiş oluyorsun.

---
Aynen, şimdi Command Pattern bölümünü de **çok daha detaylı**, Medium’a uygun, profesyonel ve açıklayıcı bir biçimde genişletiyorum.
Aşağıdaki metni direkt olarak yazındaki mevcut Command bölümünün yerine ekleyebilirsin:

---

#  3. Command Pattern — Kullanıcı İşlemlerini Komut Nesnesine Dönüştürmek

Akıllı cihazlarla etkileşimde kullanıcıların gerçekleştirdiği her işlem (turn on, turn off, mode change, schedule action vb.) aslında sistem içinde önemli bir olaydır. Bu olaylar çoğu IoT mimarisinde:

* Loglanır
* Geri alınabilir hâle getirilir
* Uzaktan tetiklenebilir (ör. mobil uygulamadan)
* Cihaz çalışmıyorsa bir kuyruğa alınabilir
* IoT hub’ına komut olarak gönderilebilir
* Otonom senaryolarda makro hâline getirilebilir

Dolayısıyla “cihazı aç” gibi basit gözükse de **bu işlem bir nesne olarak temsil edilmek zorundadır**.

Tam burada **Command Pattern** devreye girer.

Command Pattern'in ana fikri şu:

> “Her kullanıcı operasyonu, tek bir `execute()` metodu olan bir komut nesnesi ile temsil edilmelidir.”

Bu sayede komutu:

* depolayabilir,
* çalıştırabilir,
* yeniden çalıştırabilir,
* uzaktan gönderebilir,
* sıraya alabilir,
* loglayabilir veya geri alabilirsiniz.

Bu mimari özellikle **IoT sistemlerinde standardın ta kendisidir**.

---

##  Problem — İşlemleri tek bir yöntem ile yönetmenin sınırları

Geleneksel yaklaşımda bir cihazı açmak veya kapatmak için doğrudan:

```java
device.turnOn();
device.turnOff();
```

gibi çağrılar yapılır.

Ama bu yaklaşımın birden fazla problemi vardır:

* İşlem **loglanamaz** (kimin, ne zaman yaptığı belirsiz).
* İşlem **geri alınamaz**.
* İşlemi **uzaktan göndermek** mümkün değildir (örneğin bir mesajlaşma protokolüyle).
* İşlem **kuyruğa alınamaz**.
* İşlem **otomasyon akışlarına** dahil edilemez.
* İşlem **veri tabanında saklanamaz**.

Yani akıllı cihaz sistemleri için yetersizdir.

Bu nedenle kullanılan en doğru yaklaşım:

**Her işlemi ayrı bir komut nesnesi ile temsil etmek.**

---

#  Komut Arayüzü — Tüm Komutların Temeli

Projede kullanılan komut arayüzü oldukça yalın fakat güçlüdür:

```java
public interface Command {
    void execute();
}
```

Bu arayüz şunu garanti eder:

* Tüm komutlar bir eylem gerçekleştirebilir
* Tüm komutlar aynı tiptedir → zincirlenebilir, saklanabilir
* Invoker bunları çalıştırmak için ortak bir yapıya sahiptir

Bu bir **IoT komut protokolünün temelidir.**

---

# 🔌 TurnOnCommand — Cihaz Açma Komutu

```java
public class TurnOnCommand implements Command {
    private final Device device;

    public TurnOnCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOn();
    }
}
```


* Komut nesnesi oluşturulurken hedef cihaz içeride tutulur.
* `execute()` çağrıldığında bu cihaz açılır.
* Bu komut nesnesi veritabanına kaydedilebilir,
  mesajlaşma sistemi ile başka servislere gönderilebilir,
  geçmişe yazılabilir veya otomasyon akışlarında kullanılabilir.

Bir komutu JSON formatına çevirip IoT bulutuna bile gönderebilirsin:

```json
{
  "type": "TurnOnCommand",
  "device": "LivingRoomLight"
}
```

Bu Command mimarisi sayesinde **aynı komut hem backend hem IoT cihazda** yorumlanabilir hâle gelir.

---

# TurnOffCommand — Cihaz Kapatma Komutu

```java
public class TurnOffCommand implements Command {
    private final Device device;

    public TurnOffCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOff();
    }
}
```

TurnOnCommand ile tamamen aynı yapıya sahiptir ancak amacı farklıdır.

Bu sayede:

* İşlem türleri ayrışır
* Loglama kolaylaşır
* Kullanıcı davranışları net şekilde izlenir
* Farklı cihaz tiplerine uygulanabilir (TV, klima, ampul, sensör…)

---

#  Command Nesneleri Neden Bu Kadar Güçlü?

Command nesnesi:

* Bellekte saklanabilir
* Dosyaya yazılabilir
* Veritabanına kaydedilebilir
* İnternetten başka bir servise gönderilebilir
* Kuyruğa alınabilir
* Macro (otomasyon) içinde zincirlenebilir
* Geri alınabilir (undo/redo)

Örneğin:

> “Evi terk et” makrosu:
>
> * ışıkları kapat
> * klimayı kapat
> * TV’yi kapat
> * perdeyi kapat
> * güvenlik sistemini aktif et

Bu işlemlerin tümü **Command Pattern ile yapılır.**

---

#  Invoker — Komutların Yürütücüsü

```java
public class Invoker {
    private final List<Command> history = new ArrayList<>();
    
    public void executeCommand(Command command) {
        command.execute();
        history.add(command);
    }

    public List<Command> getHistory() {
        return history;
    }
}
```

### Invoker’ın rolü nedir?

Invoker:

* Komutu çalıştırır
* Komutu geçmişe kaydeder
* Gerekirse bu geçmişi geri döndürmek için kullanabilir

Invoker, akıllı cihaz sistemlerinde sıklıkla şu rolde çalışır:

* IoT Hub Komut İşleyicisi
* Kullanıcı İşlem Yönetimi
* Otomasyon Makro Motoru
* Smart Home Scenario Engine

Yani bu yapı sadece backend değil, tüm Smart Home sistem mimarisinde kritik bir bileşendir.

---

#  Command Pattern ile Yapılabilen Gelişmiş Senaryolar

Aşağıdaki özellikler Command Pattern sayesinde **çok kolay ve temiz şekilde** uygulanabilir:

###  1. Undo / Redo

History listesinden son komutu çekip ters işlem yapılabilir.

###  2. Makro Komutlar

Bir komut içinde başka komutları zincirleyebilirsin → “evden çıkış modu”.

###  3. Zamanlanmış komutlar

“5 dakika sonra ışığı kapat” → TimerDecorator ile birleşince inanılmaz güçlü olur.

---

# Sonuç ve Kapanış

Bu projede birlikte kullanılan **State**, **Decorator** ve **Command** tasarım desenleri; modern IoT ve akıllı cihaz yazılımlarında karşılaşılan tüm davranışsal zorlukları çözmek için mükemmel bir mimari bütünlük sağlar. State Pattern cihazların farklı iç durumlarda nasıl hareket ettiğini net ve sürdürülebilir bir şekilde modelleyerek kod karmaşasını ortadan kaldırır. Decorator Pattern, cihazların yeteneklerini sınırsız kombinasyonlarla genişletmeye olanak tanır; enerji tasarrufu, zamanlayıcı, loglama gibi özellikler dinamik olarak eklenebilir. Command Pattern ise kullanıcı eylemlerinin nesneleştirilmesini sağlayarak geçmiş yönetimi, geri alma mekanizmaları, makro sistemleri ve otomasyon senaryolarının kolayca uygulanmasına kapı açar.

Bu üç desen bir arada kullanıldığında, basit bir akıllı ampul bile profesyonel bir yazılım mimarisinin parçası hâline gelir. Yeni cihaz eklemek, yeni özellik tanımlamak veya yeni kullanıcı senaryoları oluşturmak minimum eforla yapılabilir hâle gelir. Üstelik kod hem daha test edilebilir, hem daha sürdürülebilir, hem de gelecekteki IoT genişlemelerine tamamen hazır bir yapıda olur.

Bu mimari yaklaşım, hem gerçek dünya akıllı cihazlarının davranışlarını birebir modellemesi hem de endüstri standartlarına uygun olması nedeniyle, profesyonel IoT uygulamalarında tercih edilen en güçlü çözümlerden biridir.
