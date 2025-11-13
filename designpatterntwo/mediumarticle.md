
# ☁ Akıllı Cihaz Yönetimi Üzerinden State, Decorator ve Command Pattern’lerini Derinlemesine Öğrenin

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

Bu .md dokümanı, hem projede kullanılan gerçek kodlardan hem de teorik arka plandan yararlanarak sana kapsamlı bir teknik anlatım sunar.

---

# 🟦 1. State Pattern — Davranışı Durumlara Bölerek Yönetmek

## 🎯 Problem

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

## ✔ Çözüm: Durumları ayrı sınıflara ayırmak

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

## 🟢 OnState (Cihaz Açık)

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

✔ Açıklama:

- Cihaz zaten açık olduğu için `turnOn()` yalnızca bilgi verir.
- `turnOff()` cihazın durumunu değiştirir → **Davranış değişikliği tamamen state değişimine bağlıdır.**

---

## 🔴 OffState (Cihaz Kapalı)

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

## 🟡 StandbyState (Uyku Modu)

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

## 📌 State Pattern’in Sağladıkları

- If-else blokları tamamen ortadan kalktı.  
- Her durum kendi davranışını yönetir → **Davranış = Durum**  
- Yeni durum eklemek çok kolaydır.  
- Kod daha okunabilir, genişletilebilir ve bakımı kolay bir hâle gelir.

---

# 🟪 2. Decorator Pattern — Cihazlara Dinamik Özellik Eklemek

## 🎯 Problem

Bir cihazda şu özellikler olabilir:

- Zamanlayıcı  
- Enerji tasarrufu modu  
- Loglama  
- IoT telemetri takibi  

Bu özellikleri **miras ile** çözmeye çalışsaydık:

```
Light
LightWithTimer
LightWithEnergySaver
LightWithTimerAndEnergySaver
LightWithLogger
LightWithLoggerAndEnergySaver
...
```

➡ Bu **class explosion** (sınıf patlaması) problemidir.

---

## ✔ Çözüm: Cihazları "sarmak" (wrap)

Her ek özellik bir “katman" olarak tanımlanır.

### DeviceDecorator (Temel Dekoratör)

```java
public abstract class DeviceDecorator implements Device {
    protected Device decoratedDevice;
```

👉 Bu sayede cihazı "sarar" ve davranışı genişletir.

---

## 🔋 EnergySaverDecorator

```java
@Override
public void turnOn() {
    System.out.println("🔋 Energy saver mode enabled for " + getName());
    super.turnOn();
}
```

✔ Açıklama:  
Cihaz açılmadan önce enerji tasarrufu modu aktive edilir.

---

## ⏱ TimerDecorator

```java
@Override
public void turnOn() {
    super.turnOn();
    System.out.println("⏱ Timer started for " + getName());
}
```

✔ Açıklama:  
Cihaz açılıp zamanlayıcı devreye girer.

---

## 🧩 Çoklu Decorator Zinciri

```java
case TV:
    return new TimerDecorator(
             new EnergySaverDecorator(
                 new Television()
             ));
```

Bu sayede:

✔ TV aynı anda **Energy Saver + Timer** özelliklerine sahip olur  
✔ Sınıf çoğalması olmadan  
✔ Dinamik şekilde

---

# 🟩 3. Command Pattern — Kullanıcı İşlemlerini Komut Nesnesine Dönüştürmek

## 🎯 Problem

Kullanıcı bir cihazı açtığında bu işlemi:

- Nesne olarak temsil etmek  
- Kayıt altına almak  
- Gerekirse geri almak  
- Uzaktan iletmek  

gereklidir.

Bu yüzden **Command Pattern** ideal bir çözümdür.

---

## ✔ TurnOnCommand

```java
public class TurnOnCommand implements Command {
    @Override
    public void execute() {
        device.turnOn();
    }
}
```

---

## ✔ TurnOffCommand

```java
public class TurnOffCommand implements Command {
    @Override
    public void execute() {
        device.turnOff();
    }
}
```

---

## 🗂 Invoker — Komut çalıştırıcı

```java
public class Invoker {
    private final List<Command> history = new ArrayList<>();
```

➡ Tüm komutlar burada saklanır → geçmiş izleme yapılabilir.

---

# 🏁 Sonuç ve Kapanış

Bu projede birlikte kullanılan **State**, **Decorator** ve **Command** tasarım desenleri, akıllı cihaz mimarisine tam anlamıyla profesyonel bir yapı kazandırdı. State Pattern cihazların davranışlarını açık, düzenli ve yönetilebilir hâle getirirken; Decorator Pattern cihazlara ihtiyaca göre dinamik, sınırsız sayıda özellik eklemeyi mümkün kıldı. Command Pattern ise kullanıcı aksiyonlarını nesneleştirerek geçmiş yönetimi, geri alma (undo) ve daha gelişmiş kontrol mekanizmaları için temel oluşturdu. Üç desenin birlikte kullanımı sayesinde sistem yalnızca bugünkü özelliklere değil, gelecekte eklenebilecek tüm gereksinimlere de kolayca uyarlanabilir bir yapıya dönüştü. Özellikle IoT ve akıllı cihaz projelerinde böyle bir mimari, ölçeklenebilirlik, sürdürülebilirlik ve temiz kod açısından büyük avantaj sağlar.

