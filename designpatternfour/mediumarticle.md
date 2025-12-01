# Akıllı Ev Yönetimi Üzerinden Visitor, Mediator, Memento ve Interpreter Tasarım Desenlerini Derinlemesine Öğrenin
Modern IoT sistemleri, her biri farklı davranışlara sahip akıllı cihazların birlikte çalıştığı yapılardır.
Akıllı ev sistemleri de bu mimarinin doğrudan uygulama alanlarından biridir.
Bu sistemlerde cihazların:
- Birbirleriyle koordineli şekilde çalışması,
- Durumlarının korunması ve geri yüklenmesi,
- Dışarıdan gelen kurallara göre otomasyon sağlaması,
- Yeni operasyonlara uyumlu olması
gibi gereksinimler oluşur.
Bu gereksinimler, sıradan bir OOP yaklaşımıyla yönetilemez hâle gelir.
Bu nedenle güçlü tasarım desenleri gerekir.
Bu projede dört ana desen birleştirilmiştir:
- Mediator Pattern
- Visitor Pattern
- Memento Pattern
- Interpreter Pattern
Ayrıca cihaz komutları Command Pattern ile nesneleştirilmiştir.
Bu makalede önce *teorik temeli*, ardından *gerçek koddaki uygulamayı* inceleyeceğiz.
---
## 1. Mediator Pattern — Cihazlar Arası Bağımsız İletişim
Akıllı evde onlarca cihaz olabilir: ışıklar, kapılar, klimlar, sensörler…
Eğer bu cihazlar birbirini doğrudan kontrol ederse sistem hızla kaotik hâle gelir.
Örneğin:
- Kapı AC’yi bilebilir
- AC ışığı bilebilir
- Işık kapıyı bilebilir
Bu, *dışa bağımlılık* yaratır.
Bu durum iki büyük probleme yol açar:
- Kod karmaşası
- Yeni cihaz ekleyememe
İşte Mediator Pattern bu problemi çözer.
Mediator, cihazların *birbirini bilme zorunluluğunu* ortadan kaldırır.
Tüm iletişimi tek bir merkez üzerinden gerçekleştirir.
---
## 1.1 Mediator Pattern’in Amacı
- Cihazlar arasındaki iletişimi merkezi bir noktada toplamak
- Command Pattern ile entegre çalışarak cihaz komutlarını yönetmek
- Sistemi loosely-coupled bir hâle getirmek
- Yeni cihaz ekleme sürecini kolaylaştırmak
Bu mimari özellikle *genişleyebilir IoT sistemleri* için olmazsa olmazdır.
---
## 1.2 Projedeki Mediator Arayüzü
Cihazlar Mediator ile çalışır, birbirleriyle değil.
Kod:
```java
public interface SmartHomeMediator {
    void registerDevice(Device device);
    Device getDevice(String name);
    String sendCommand(DeviceType type, boolean turnOn);
}
```
Bu arayüz üç önemli fonksiyon sunar:
- registerDevice → cihaza sisteme tanıtılır
- getDevice → direkt isimle erişim
- sendCommand → Command Pattern tetikleyici
---
## 1.3 DefaultSmartHomeMediator
Bu sınıf, ev içindeki tüm cihazların yöneticisidir.
Kodun kritik bölümü:
```java
private final Map<String, Device> devices = new HashMap<>();

@Override
public void registerDevice(Device device) {
    System.out.println("[MEDIATOR] Registering device → " + device.getName());
    devices.put(device.getName(), device);
}
```
registerDevice ile amaç:
- Cihazları sisteme tanıtmak
- Her cihazın tür bilgisiyle birlikte saklanmasını sağlamak
Cihazların artık birbirini bilmesine gerek yoktur.
---
## 1.4 Komut Gönderme — Mediator → Command Pattern
Şimdi Mediator aracılığıyla cihazlara komut gönderme kısmına bakalım:
```java
@Override
public String sendCommand(DeviceType type, boolean turnOn) {
    devices.values().stream()
            .filter(d -> d.getType() == type)
            .forEach(device -> {
               Command cmd = turnOn
                   ? new TurnOnCommand(device)
                   : new TurnOffCommand(device);

               cmd.execute();
           });
   return "OK";

``
bu yapı üç deseni birleştirir:
 Mediator → komutu yönlendirir
 Command → cihaz davranışını nesneleştirir
 Device → state değişimini uygular
--
# 1.5 Komut Nesneleri (Command Pattern Bağlantısı)
örnek:
```java
public class TurnOnCommand implements Command {
    private final Device device;

    @Override
    public void execute() {
        System.out.println("COMMAND → TURN ON " + device.getName());
        device.turnOn();
    }
 }
 ```
Komutlar *soyutlanmış davranışlardır*.
Mediator sadece “hangi cihazlara” komut gideceğini bilir.
Command sadece “ne yapılacağını” bilir.
Bu ayırım mükemmel bir sorumluluk dağılımıdır.
---
## 2. Visitor Pattern — Cihazlara Dokunmadan Yeni Operasyon Eklemek
Visitor Pattern’ın en büyük avantajı, mevcut cihaz sınıflarını değştirmeden yeni işlemler ekleyebilmektir.
Bu proje özelinde Visitor Pattern:
- Enerji raporu üretmek
- Cihaz türüne göre ayrı işlem yapmak
için uygulanır.
Zamanla “diagnostic”, “usage-log”, “firmware-report” gibi visitor'lar eklenebilir.

Visitor Pattern’ın temel amacı cihaz sınıflarını değiştirmeden yeni işlemler ekleyebilmektir.
Eğer tüm cihazlarda “enerji tüketimini hesaplama” davranışı direkt içeride yazılsaydı:
- Kod tekrarı oluşurdu,
- OCP (Open-Closed Principle) ihlal edilirdi,
- Her yeni rapor için cihaz sınıfları değiştirilmek zorunda kalırdı.
Visitor bu sorunların hepsini tek seferde çözer.
---
## 2.1 Visitor Arayüzü — Tüm Cihaz Türlerini Destekleyen Yapı
Projedeki Visitor arayüzü:
```java
public interface DeviceVisitor {
    void visitLight(Light light);
    void visitAC(AC ac);
    void visitDoor(Door door);
}
```
Bu tasarım kritiktir çünkü Visitor:
- Tür bazlı işlem yapacağı için,
- Türleri metot imzası üzerinden ayrıştırır.
Tür bazlı polymorphism burada uygulanır.
---
## 2.2 Cihazlar Visitor’ı Nasıl Kabul Eder?
Her cihaz sınıfında `accept` fonksiyonu bulunur.
Örneğin Light:
```java
@Override
public void accept(DeviceVisitor visitor) {
    visitor.visitLight(this);
}
```
AC:
```java
@Override
public void accept(DeviceVisitor visitor) {
    visitor.visitAC(this);
}
```
Door:
```java
@Override
public void accept(DeviceVisitor visitor) {
    visitor.visitDoor(this);
}
```
Burada kritik nokta şudur:
- Visitor nesnesi tüm cihaz türlerini bilir,
- Cihaz sınıfı ise yalnızca Visitor’ı “kabul eder”.
Cihazın Visitor’ın yaptığı işten haberi yoktur.
---
## 2.3 EnergyReportVisitor — Enerji Tüketimi Raporu
Bu Visitor, cihazın durumuna göre harcadığı gücü hesaplar.
```java
@Override
public void visitLight(Light light) {
    String watt = light.isOn() ? "10W" : "0W";
    report.append("Light '")
          .append(light.getName())
          .append("' → ")
          .append(watt)
          .append("\n");
}
```
Aynı mantık AC için:
```java
String watt = ac.isOn() ? "1200W" : "0W";
```
Door için:
```java
String watt = "0W"; // kapı enerji harcamaz :)
```
---
## 2.4 Visitor’ın Akış İçerisindeki Kullanımı
Controller seviyesinde Visitor şu şekilde tetiklenir:
```java
devices.forEach(d -> d.accept(visitor));
```
`accept()` → cihaz Visitor’a kendisini teslim eder.
Visitor → cihaz türüne göre doğru metodu çalıştırır.
Bu süreç 3 avantaj sağlar:
- Cihaz sınıflarına dokunmadan rapor eklemek,
- Cihaz türüne göre farklı işlem uygulamak,
- Yeni cihaz türleri eklemek için Visitor’ın genişletilebilir olması.
---
## 3. Memento Pattern — Cihaz Durumlarını Geri Alabilmek
Kullanıcı geçmiş durumlara dönebilmek ister:
- “Işıkları eski hâline getir”
- “Kapı en son hangi durumdaydı?”
- “Klima otomasyon öncesi açık mıydı?”
Bu ihtiyaç Memento Pattern ile çözülür.
Bu desen üç ana bileşenden oluşur:
1)  Device
2)  DeviceSnapshot
3)  DeviceHistory
---
## 3.1 Device Sınıfı
Device sınıfı cihazın mevcut durumunu tutar.
Ayrıca “snapshot” oluşturma ve geri yükleme yeteneğine sahiptir.
Kritik fonksiyonlar:
```java
public DeviceSnapshot createSnapshot() {
    return new DeviceSnapshot(name, this.isOn());
}

public void restore(DeviceSnapshot snapshot) {
    this.on = snapshot.isOn();
}
```
Burada:
- Snapshot cihazın geçici “donmuş hâlidir”,
- restore(), cihazı geçmiş durumuna döndürür.
---
## 3.2 DeviceSnapshot
Her snapshot cihazın:
- ismi
- açık/kapalı durumu
bilgilerini saklar.


Kod:
```java
public class DeviceSnapshot {
    private final String name;
    private final boolean on;

    public DeviceSnapshot(String name, boolean on) {
        this.name = name;
        this.on = on;
    }

    public String getName() { return name; }
    public boolean isOn() { return on; }
}
```
DeviceSnapshot değiştirilemez (immutable) şekilde tasarlanmıştır.
Bu, Memento Pattern’ın temel gerekliliklerinden biridir.
Çünkü snapshot geçmişteki bir durumu “bozulmadan” temsil etmelidir.
---
## 3.3 DeviceHistory
Snapshot’lar stack-benzeri bir yapıda tutulur:
```java
public class DeviceHistory {
    private final Deque<DeviceSnapshot> history = new ArrayDeque<>();

    public void push(DeviceSnapshot snapshot) {
        history.push(snapshot);
    }

    public DeviceSnapshot pop() {
        return history.isEmpty() ? null : history.pop();
    }
}
```
Bu yapı sayesinde:
- Çoklu geri alım (undo)
- Tekrarlanabilir senaryolar
- Kullanıcı deneyimi geliştirme
mümkün hâle gelir.
---
## 3.4 Memento Pattern’ın Uygulamadaki Rolü
Memento Pattern bu projede şu durumlarda önemlidir:
- Kullanıcı cihazı yanlışlıkla kapattı → geri al
- Otomasyon kuralı ışığı kapattı → eski hâline döndür
- Enerji tasarrufu modu aktif oldu → önceki hâle dön
- Mobil uygulama “geri al” butonu
Bu desenin esnekliği, cihaz durumlarının bağımsız şekilde saklanabilmesini sağlar.
---
## 4. Interpreter Pattern — Sensör Verilerine Göre Otomatik Karar Alma
Akıllı ev sistemlerinde otomasyon kuralları son derece önemlidir:
- “Sıcaklık 26’dan büyükse klimayı çalıştır”
- “Hareket algılandıysa ışığı aç”
- “Sıcaklık 18’in altına düşerse klimayı kapat”
Bu kurallar insan-doğal diline yakındır.
Ancak sistem tarafından yorumlanabilmesi için bir dil (“DSL”) tasarımı gerekir.
İşte Interpreter Pattern bu sorunu çözer.
Kullanıcı kuralı JSON olarak gönderir → sistem bunu bir Expression hiyerarşisine dönüştürür.
---
## 4.1 RuleRequest — Kullanıcıdan Gelen DSL
Kullanıcı şu formatta bir kural gönderir:
```json
{
  "sensor": "TEMPERATURE",
  "operator": ">",
  "value": 26,
  "action": "TURN_ON",
  "deviceType": "AC"
}
```
Bu yapı otomasyon sisteminin temel DSLidir.
Kullanıcı dostudur ve genişletilebilir.
---
## 4.2 ConditionExpression — Terminal Expression
ConditionExpression sensör verisini yorumlar.
Kod:
```java
public boolean interpret(Context ctx) {
    if(sensor.equalsIgnoreCase("TEMPERATURE")) {
        return operator.equals(">") 
                ? ctx.getTemperature() > value
                : ctx.getTemperature() < value;
    }

    if(sensor.equalsIgnoreCase("MOTION")) {
        return ctx.isMotionDetected();
    }

    return false;
}
```
Bu yapı kuralın “şart” tarafını ifade eder.
Kullanıcı sıcaklığı veya hareketi kontrol edebilir.
---
## 4.3 Context — Sistemin Gerçek Zamanlı Durumu
Context sınıfı sensör değerlerini taşır:
```java
public class Context {
    private final int temperature;
    private final boolean motionDetected;

    public Context(int temperature, boolean motionDetected) {
        this.temperature = temperature;
        this.motionDetected = motionDetected;
    }

    public int getTemperature() { return temperature; }
    public boolean isMotionDetected() { return motionDetected; }
}
```
Interpreter bu veriyi okur → ConditionExpression yorumlar → karar çıkar.
---
## 4.4 RuleExpression — Non-terminal Expression
RuleExpression hem ConditionExpression hem de aksiyon mantığını içerir.
Kod:
```java
  public void interpret(Context ctx, SmartHomeMediator mediator) {

        boolean result = conditionExpression.interpret(ctx);

        if(result) {
            boolean turnOn = action.equalsIgnoreCase("TURN_ON");
            mediator.sendCommand(deviceType, turnOn);
        }
    }
```
Interpreter Pattern burada tam anlamıyla DSL (Domain-Specific Language) yorumlayıcı rolündedir.
RuleExpression, kullanıcının gönderdiği kuralı tek bir üst seviye ifade olarak kapsar.
Bu ifade, ConditionExpression ile “şartı”, action + deviceType ile “sonucu” temsil eder.
Yani RuleExpression aslında iki öğeli birleşik bir gramer düğümüdür.
---
## 4.5 Interpreter’ın Mediator ile Entegrasyonu
Interpreter, kuralı doğruladıktan sonra cihaz üzerinde aksiyon almak zorundadır.
Bu noktada devreye Mediator Pattern girer.
Kod:
```java
if(result) {
    mediator.sendCommand(deviceType, turnOn);
}
```
Burada dikkat edilmesi gereken kritik özellik şudur:
- Interpreter cihazın nasıl açıldığını bilmez,
- Cihaz nesnelerine erişim sağlayamaz,
- Cihazların birbirleriyle ilişkisini yönetmez.
Interpreter sadece:
- “Şart sağlandı mı?”
- “Eylem TURN_ON mı TURN_OFF mu?”
sorularıyla ilgilenir.
Cihaz kontrolü tamamen Mediator’a devredilir.
Böylece her iki desen görev ayrımı yapar.
---
## 4.6 RuleInterpreterService — Sistemin “Otomasyon Beyni”
Bu servis Controller tarafından çağrılır.
Görevi:
- Kullanıcıdan gelen JSON’u RuleExpression’a dönüştürmek,
- Context ile birlikte yorumlamak,
- Çıkan sonuca göre mediator üzerinden aksiyon almak.
Koddan kritik bölüm:
```java
if (condition) {
    System.out.println("Condition MET → executing action");
    mediator.sendCommand(
        DeviceType.valueOf(rule.deviceType()),
        rule.action().equals("TURN_ON")
    );
    return rule.deviceType() + " -> " + rule.action().replace("TURN_", "");
}
```
RuleInterpreterService tüm sistemi uçtan uca birbirine bağlayan katmandır.
---
## 4.7 Interpreter Pattern’ın Akıştaki Rolü
Kural motorunun çalışmasını bir örnekle açıklayalım.
Kullanıcı şöyle bir JSON gönderdi:
```json
{
  "sensor": "TEMPERATURE",
  "operator": ">",
  "value": 26,
  "action": "TURN_ON",
  "deviceType": "AC"
}
```
Sensör verisi:
```
temperature = 28
motionDetected = true
```
Akış şöyle gerçekleşir:
1) ConditionExpression → 28 > 26 → TRUE
2) RuleExpression → “TURN_ON AC”
3) Mediator.sendCommand(AC, true)
4) Mediator → AC cihazını bulur
5) Command Pattern → TurnOnCommand çalışır
6) AC.turnOn() çağrılır
7) AC'nin state'i OffState → OnState’e geçer
8) Rapor: “AC -> ON”
Bu zincir, tüm desenlerin birlikte ahenkle çalıştığını gösterir.
---
## 5. Cihaz Yapısı — State + Memento + Visitor + Command Entegrasyonu
Bu projedeki Device hiyerarşisi eşsizdir çünkü tek bir sınıf:
- State Pattern (OnState / OffState)
- Memento Pattern (Snapshot oluşturma)
- Visitor Pattern (accept)
- Command Pattern’ın hedefi
gibi dört farklı rolü aynı anda üstlenir.
Bu çok katmanlı davranış akıllı ev mimarilerinde oldukça yaygındır.
---
## 5.1 Device Soyut Sınıfı
Device sınıfı soyut bir yapıdadır.
Cihazların ortak davranışlarını içerir:
- getName()
- getType()
- isOn()
- turnOn() (soyut)
- turnOff() (soyut)
- createSnapshot() (somut)
- restore() (somut)
- accept() (soyut, Visitor için)
---
Koddan kritik bölüm:
```java
public abstract class Device {
    protected String name;
    protected State state;     // State Pattern
    protected boolean on;      // FE uyumluluğu için

    public DeviceSnapshot createSnapshot() {
        return new DeviceSnapshot(name, this.isOn());
    }

    public void restore(DeviceSnapshot snapshot) {
        this.on = snapshot.isOn();
    }

    public abstract void accept(DeviceVisitor visitor);
}
```
Device sınıfı bir “davranış merkezi”dir.
Tüm desenler burada birleşir.
---
## 5.2 AC Sınıfı — Tüm Pattern’lerin Kullanıldığı Örnek
AC sınıfı hem State hem Memento hem Visitor hem Command işlemine maruz kalır.
Koddan kritik bölüm:
```java
@Override
public void turnOn() {
    state = new OnState();
    state.printStateChange(name);
    on = true;
    System.out.println("AC turned ON");
}
```
turnOff için:
```java
state = new OffState();
on = false;
System.out.println("AC turned OFF");
```
AC şu davranışları taşır:
- turnOn / turnOff → Command Pattern tarafından tetiklenir
- state değişikliği → State Pattern
- snapshot → Memento
- accept(visitor) → Visitor Pattern
Bu bağlamda AC, çoklu pattern entegrasyonunun en iyi örneğidir.
---
## 5.3 Door Sınıfı — Alternatif Durum Mantığı
Door açık/kapalı mantığı ile çalışır ancak internal olarak on/off aynıdır.
Kod:
```java
@Override
public boolean isOn() {
    return open;
}
```
turnOn():
```java
open = true;
```
turnOff():
```java
open = false;
```
Visitor’a uygun hâle getirilmiştir:
```java
visitor.visitDoor(this);
```
Bu sayede kapı diğer cihazlarla aynı mimari içinde yönetilebilir.
---
## 5.4 Light Sınıfı — En Basit Cihaz
Light:
- turnOn → on = true
- turnOff → on = false
- snapshot → Memento için
- accept → Visitor için
Light cihazı sistemdeki en hafif sınıftır.
Ancak aynı mimari kurallara uymaya devam eder.
Bu, tasarım bütünlüğünü sağlar.
---
## 6. Controller Katmanı — İnsan ve Sistem Arasındaki Arayüz
Controller katmanı hem Interpreter hem Visitor hem de Mediator akışlarını tetikler.
Bu katman sistemin dış dünya ile temas noktasıdır.
İki ana controller bulunmaktadır: RuleController ve ReportController.
## 6.1 RuleController — Interpreter Pattern’ın Giriş Kapısı
Kullanıcıdan gelen kurallar RuleController üzerinden sisteme alınır.
Koddan kritik bölüm:
```java
@PostMapping("/execute")
public ResponseEntity<?> execute(@RequestBody RuleRequest request) {
    Context ctx = new Context(28, true); // demo sensör verisi
    String result = interpreter.evaluateAndExecute(request, ctx);
    return ResponseEntity.ok(Map.of("status", "success", "message", result));
}
```
Burada şu işlemler gerçekleşir:
1) JSON → RuleRequest objesi
2) RuleInterpreterService → kural değerlendirmesi
3) Interpreter Pattern → ConditionExpression yorumlama
4) Mediator Pattern → cihaz kontrolünün yönlendirilmesi
5) Command Pattern → cihaz davranışının uygulanması
Bu endpoint sistemde “otomasyon motorunun dışa açılan kapısıdır”.
---
## 6.2 ReportController — Visitor Pattern’ın Giriş Kapısı
Ziyaretçi (Visitor) tasarım deseni burada aktifleşir.
Enerji raporu oluşturmak için:
```java
devices.forEach(d -> d.accept(visitor));
```
controller:
```java
@GetMapping("/energy")
public ResponseEntity<?> energy() {
    EnergyReportVisitor visitor = new EnergyReportVisitor();
    devices.forEach(d -> d.accept(visitor));
    return ResponseEntity.ok(Map.of("status", "success", "report", visitor.getReport()));
}
```
Bu akış şu operasyonları gerçekleştirir:
- Tüm cihazlar ziyaret edilir,
- Türlerine göre enerji harcaması hesaplanır,
- Tek bir rapor hâline getirilir,
- Kullanıcıya döndürülür.
Visitor Pattern sayesinde bu rapor cihazlara dokunmadan üretilebilir.
---
## 7. Tüm Desenlerin Uçtan Uca Entegrasyonu
Bu akıllı ev projesi, yazılım mimarisi açısından son derece öğretici bir yapıya sahiptir.
Çünkü dört büyük desen tek bir senaryoda birleşir:
- Interpreter → Kuralı yorumlar
- Mediator → Komutu yönlendirir
- Command → Cihaz davranışını çalıştırır
- State → Cihazın iç durumunu yönetir
- Memento → Durumu geri alınabilir kılar
- Visitor → Raporlama gibi ek operasyonları sağlar
Bu zincir gerçek dünyada sık karşılaşılan IoT süreçlerini doğrudan modellemektedir.
---
## 7.1 Örnek Senaryo — “Sıcaklık 26°C’den Büyükse AC Aç”
Kullanıcı JSON gönderir:
```json
{
  "sensor": "TEMPERATURE",
  "operator": ">",
  "value": 26,
  "action": "TURN_ON",
  "deviceType": "AC"
}
```
Sensör verisi:
- temperature = 28
- motionDetected = true
Sistem akışı:
1) RuleController JSON’u alır
2) RuleInterpreterService kuralı parçalar
3) ConditionExpression → 28 > 26 = TRUE
4) RuleExpression → TURN_ON AC
5) Mediator → tüm AC cihazlarını bulur
6) Command Pattern → TurnOnCommand
7) AC.turnOn() → State değişimi
8) Memento → snapshot alınabilir
9) EnergyReportVisitor → istenirse enerji raporunda görünür
Bu süreç IoT’de gerçek otomasyon zincirinin aynısıdır.
---
## 7.2 Örnek Senaryo — “Hareket Algılanırsa Kapıyı Aç”
Kullanıcı:
```json
{
  "sensor": "MOTION",
  "operator": "=", 
  "value": 1,
  "action": "TURN_ON",
  "deviceType": "DOOR"
}
```
Sensör → motionDetected = true
Akış:
- ConditionExpression → TRUE
- Mediator → Door türünü bulur
- Command → TurnOnCommand
- Door.turnOn() → open = true
- Visitor → kapının enerji tüketimini 0W olarak raporlar
Bu zincir cihaz bağımsız kontrol sağlar.
---
## 7.3 Örnek Senaryo — “Undo” İşlemi (Memento)
Kullanıcı:
- “AC’yi eski hâline getir”
Bunun için:
```java
DeviceSnapshot snap = history.pop();
ac.restore(snap);
```
Memento Pattern sayesinde:
- Cihaz durumu bozulmaz
- Önceki değerlere tam dönüş sağlanır
Bu özellik otomasyon sistemlerinde çok kritiktir.
---
## 7.4 Visitor + Mediator Kombinasyonu
Visitor cihazları ziyaret eder → ancak cihazları bulmak Mediator’ın işidir.
Yani Controller:
- Mediator’dan cihaz listesini alabilir,
- Visitor ile hepsini gezebilir,
- Rapor oluşturabilir.
Bu desenlerin birbirini desteklemesi mimariyi çok güçlü kılar.

## 8. Kapanış — Modern IoT Sistemleri İçin Profesyonel Bir Tasarım Yaklaşımı
e, yazılım tasarım desenlerinin bir arada nasıl güçlü bir mimari oluşturduğunu kanıtlar niteliktedir.
Visitor, Mediator, Memento ve Interpreter desenleri:
- Birbirlerini tamamlayan,
- Sorumlulukları net şekilde bölüştüren,
- IoT dünyasının gerçek problemlerine çözüm üreten
yapılardır.
Bu yapı yalnızca işlevsel değil, mimari açıdan da son derece sağlıklıdır.
Bu tür bir yaklaşım küçük bir akıllı evden milyonlarca cihazın yönetildiği dev IoT sistemlerine kadar kolaylıkla ölçeklenebilir.