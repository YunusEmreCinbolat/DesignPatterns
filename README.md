# DesignPatterns — 6 Mini Projects with Spring Boot + Angular

This repository contains 6 independent mini projects built to learn Design Patterns through **working, end-to-end examples**. Each project follows the same idea with a **backend (Spring Boot)** and a **frontend (Angular)**, and includes an accompanying article that explains the patterns used.

> Each project is standalone: pick one folder and run only that one.

---

## Projects (6/6)

| Folder | Scenario | Design Patterns | Article |
|---|---|---|---|
| `designpatternone` | Mini e-commerce: product creation, discounts, cart updates | Factory, Strategy, Observer, Null Object | `designpatternone/mediumarticle.md` |
| `designpatterntwo` | Smart device management (IoT): states, actions, dynamic add-ons | State, Command, Decorator | `designpatterntwo/mediumarticle.md` |
| `designpatternthree` | Order management: product trees, price caching, rule pipeline | Composite, Proxy, Chain of Responsibility | `designpatternthree/mediumarticle.md` |
| `designpatternfour` | Smart home orchestration: coordination and automation | Mediator, Visitor, Memento, Interpreter (+ Command integration) | `designpatternfour/mediumarticle.md` |
| `designpatternfive` | Pizza ordering system: object construction and centralized management | Builder, Singleton | `designpatternfive/mediumarticle.md` |
| `designpatternsix` | Cart pricing: efficiency, shipping/checkout decoupling, orchestration | Flyweight, Strategy, Bridge, Facade | `designpatternsix/mediumarticle.md` |

---

## Design Patterns Used (All)

### Creational

- Factory
- Builder
- Singleton

### Structural

- Decorator
- Composite
- Proxy
- Flyweight
- Bridge
- Facade

### Behavioral

- Strategy
- Observer
- Command
- State
- Chain of Responsibility
- Mediator
- Visitor
- Memento
- Interpreter
- Null Object

## Tech Stack

- **Backend:** Spring Boot 3.x, Java 21, Gradle (wrapper: `gradlew`/`gradlew.bat`)
- **Frontend:** Angular 19, TypeScript, RxJS (via npm scripts)
- **Communication:** REST APIs (some frontends use `proxy.conf.json` to forward `/api` calls to the backend)

---

## Running (Windows)

Each project follows the same directory structure:

```
<project-folder>/
	backend/
	frontend/
	mediumarticle.md
```

Example: run the `designpatternfour` project.

### 1) Backend (Spring Boot)

```powershell
cd .\designpatternfour\backend
.\gradlew.bat bootRun
```

To run tests:

```powershell
.\gradlew.bat test
```

> By default, the backend usually runs on `http://localhost:8080`.

### 2) Frontend (Angular)

```powershell
cd ..\frontend
npm install
npm start
```

> By default, the frontend runs on `http://localhost:4200`.

Note: If Angular CLI is not installed globally, prefer `npm start` (project script). If needed, `npx ng serve` also works.

---

## Notes

- If you run multiple projects at the same time, backend ports may conflict (most projects use 8080). The simplest approach is to run one project at a time, or change the port in `application.properties`/`application.yml`.
- Some frontends include `proxy.conf.json`, and `npm start` already runs with `--proxy-config proxy.conf.json` (e.g. `/api` → `http://localhost:8080`).

