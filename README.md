# 🏨 Smart Hospitality Management System (SHMS)

> A Java-based hotel management system built on **7 classic design patterns**, developed as a final project for the Software Design & Architecture Lab at the Islamic University of Gaza.

---

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Design Patterns Used](#design-patterns-used)
- [Project Structure](#project-structure)
- [System Flow](#system-flow)
- [Classes & Responsibilities](#classes--responsibilities)
- [How to Run](#how-to-run)
- [Sample Output](#sample-output)
- [Technologies](#technologies)
- [Team Members](#team-members)

---

## Project Overview

The **Smart Hospitality Management System (SHMS)** simulates a modern hotel environment covering:

- Room creation and booking
- Guest check-in and check-out workflows
- Event-driven notification system
- Dynamic billing with multiple pricing strategies
- Legacy payment system integration
- Global system configuration

The system is built around **clean architecture principles**: loose coupling, separation of concerns, and extensibility without modifying existing code.

---

## Design Patterns Used

| Pattern | Role in System |
|---|---|
| **Singleton** | Global hotel configuration (`Config`) |
| **Factory Method** | Creating room types (Standard, Deluxe, Suite) |
| **Builder** | Constructing complex booking objects step-by-step |
| **Observer** | Event-driven notifications to Guest, Staff, Manager |
| **Template Method** | Fixed workflow structure for check-in and check-out |
| **Strategy** | Dynamic billing: Regular, Member Discount, Seasonal |
| **Adapter** | Integrating the legacy payment processor |
---

## System Flow
---

## Classes & Responsibilities

### 🔧 Singleton — `Config`

Holds all hotel-wide settings. Instantiated once and shared across the system.

```java
Config config = Config.getInstance();
config.getHotelName();    // "HDR Hotel"
config.getTaxRate();      // 0.15
config.getCheckInHour();  // 3 PM
config.getCheckOutHour(); // 11 AM
```

---

### 🏗️ Factory Method — Room Creation

Each room type has its own factory. The client depends only on `RoomFactory` — never on concrete room classes.

```java
RoomFactory factory = new DeluxeRoomFactory();
Room room = factory.createRoom();
// → DeluxeRoom: $402.50/night, capacity 5, [TV, AC, Sea View]
```

| Room Type | Price (before tax) | Capacity | Features |
|---|---|---|---|
| Standard | $150 | 2 | TV, AC |
| Deluxe | $350 | 5 | TV, AC, Sea View |
| Suite | $700 | 8 | TV, AC, Sea View, Jacuzzi, Kitchen |

---

### 🧱 Builder — `BookingRoom`

Constructs a booking object step-by-step with optional services. Required fields are enforced in the constructor; optional services use fluent setters.

```java
BookingRoom booking = new BookingRoom.Builder(room, "Sara Ahmed", "6-10-2026", "6-15-2026")
    .setBreakfast(true)
    .setDinner(true)
    .setDessert(true)
    .build();
```

---

### 📢 Observer — Notification System

`EventManager` maintains a list of `NotificationListener` subscribers. When an event fires, all subscribers are notified automatically — no conditional logic involved.

**Events:** `BookingEvent` · `CheckInEvent` · `PaymentEvent` · `CheckOutEvent`

**Observers:** `GuestObserver` · `StaffObserver` · `ManagerObserver`

```java
eventManager.subscribe(new StaffObserver());
eventManager.subscribe(new GuestObserver());
eventManager.publish(new BookingEvent(), booking.getFullName());
```

---

### 📐 Template Method — Hotel Workflows

`HotelWorkflow` defines a **fixed skeleton** in `executeWorkflow()` (declared `final`). Subclasses implement the variant steps.

**Workflow steps:**
1. `validateBooking()` — private, validates booking is not null
2. `prepareProcess()` — abstract, preparation before main action
3. `performMainAction()` — abstract, core check-in or check-out logic
4. `processServices()` — private, logs requested meal services
5. `notifyGuest()` — abstract, fires Observer events
6. `logToSystem()` — abstract, logs record to system

`CheckOutWorkflow` additionally runs billing (Strategy) and payment (Adapter) inside `performMainAction()`.

---

### 💰 Strategy — Billing

`BillingContext` holds a `BillingStrategy` reference. The strategy can be switched at runtime with `setStrategy()` — no `if/switch` needed.

```java
BillingContext billing = new BillingContext(new MemberDiscountStrategy());
// Switch at runtime:
billing.setStrategy(new SeasonalPricingStrategy());
```

| Strategy | Calculation |
|---|---|
| `RegularPricingStrategy` | `price × nights` |
| `MemberDiscountStrategy` | `price × nights × 0.80` (20% off) |
| `SeasonalPricingStrategy` | `price × nights × 1.30` (+30% peak) |

---

### 🔌 Adapter — Legacy Payment Integration

`LegacyPaymentProcessor` exposes `makePayment(clientName, amount)`. The new system expects `processPayment(guestName, amount)` via the `PaymentProcessor` interface. `PaymentAdapter` bridges the gap without touching the legacy code.
---

## How to Run

### Requirements

- Java 8 or higher
- NetBeans IDE (project was built with NetBeans) **or** any Java IDE / command line

### Run with NetBeans

1. Clone or download the repository
2. Open NetBeans → `File` → `Open Project` → select the `SHMS` folder
3. Right-click the project → `Run`

### Run from Command Line

```bash
# Navigate to the src directory
cd SHMS/src

# Compile all Java files
javac -d ../out $(find . -name "*.java")

# Run the main class
java -cp ../out NewMain
```

---

## Sample Output
---

## How to Run

### Requirements

- Java 8 or higher
- NetBeans IDE (project was built with NetBeans) **or** any Java IDE / command line

### Run with NetBeans

1. Clone or download the repository
2. Open NetBeans → `File` → `Open Project` → select the `SHMS` folder
3. Right-click the project → `Run`

### Run from Command Line

```bash
# Navigate to the src directory
cd SHMS/src

# Compile all Java files
javac -d ../out $(find . -name "*.java")

# Run the main class
java -cp ../out NewMain
```

---

## Sample Output

---

## Project Structure
