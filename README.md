# 🎯 Design Patterns Application — Spring Boot & Angular

A full-stack application built with **Spring Boot (Backend)** and **Angular (Frontend)** demonstrating real-world implementations of multiple **Design Patterns** such as **Factory**, **Strategy**, **Observer**, **Command**, **State**, and **Decorator**.

This project merges architectural principles with practical software engineering to show how design patterns can create flexible, maintainable, and scalable applications.

---

## 🧩 Implemented Design Patterns

| Category | Pattern | Description |
|-----------|----------|--------------|
| **Creational** | 🏭 Factory | Creates product or device objects dynamically without exposing the instantiation logic. |
| **Behavioral** | 💡 Strategy | Defines interchangeable algorithms for applying different discount logics. |
| **Behavioral** | 🔔 Observer | Automatically updates dependent components (e.g., cart totals, UI states). |
| **Behavioral** | ⚙️ Command | Encapsulates user actions (turn on/off devices) as command objects. |
| **Behavioral** | 🔁 State | Changes the behavior of devices based on their internal ON/OFF state. |
| **Structural** | ⚡ Decorator | Dynamically adds extra features (Timer, Energy Saver) to devices. |

---

## ⚙️ Technologies Used

| Layer | Technology |
|--------|-------------|
| **Frontend** | Angular 17, TypeScript, RxJS |
| **Backend** | Spring Boot 3, Java 17, Maven |
| **Tools** | Node.js, npm, REST APIs |
| **Architecture** | Layered + Pattern-Oriented Design |

---

## 🚀 Installation & Run (Combined Setup)

Follow these steps to **install and run the entire application** (backend + frontend) in one flow:

```bash
# 1️⃣ Clone the repository
git clone https://github.com/YunusEmreCinbolat/DesignPatterns.git
cd DesignPatterns

# 2️⃣ Build backend (Spring Boot)
cd designpatterntwo/backend
mvn clean install

# 3️⃣ Install frontend dependencies (Angular)
cd ../frontend
npm instalm

# 4️⃣ Start backend in background
cd ../backend
mvn spring-boot:run &

# 5️⃣ Start frontend application
cd ../frontend
ng serve
