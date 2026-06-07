# Smart Hospitality Management System (SHMS)

> A Java-based hotel management system built on **7 classic design patterns**, developed as a final project for the Software Design & Architecture Lab at the Islamic University of Gaza.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Design Patterns Used](#design-patterns-used)
- [Project Structure](#project-structure)
- [System Flow](#system-flow)
- [Classes and Responsibilities](#classes-and-responsibilities)
- [How to Run](#how-to-run)
- [Sample Output](#sample-output)
- [Technologies](#technologies)
- [Course Information](#course-information)
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
| Singleton | Global hotel configuration (Config) |
| Factory Method | Creating room types (Standard, Deluxe, Suite) |
| Builder | Constructing complex booking objects step-by-step |
| Observer | Event-driven notifications to Guest, Staff, Manager |
| Template Method | Fixed workflow structure for check-in and check-out |
| Strategy | Dynamic billing: Regular, Member Discount, Seasonal |
| Adapter | Integrating the legacy payment processor |

---

## Project Structure

    SHMS/
    └── src/
        ├── NewMain.java
        ├── Singleton/
        │   └── Config.java
        ├── FactoryMethod/
        │   ├── Room.java
        │   ├── RoomFactory.java
        │   ├── StandardRoom.java
        │   ├── StanderdRoomFactory.java
        │   ├── DeluxeRoom.java
        │   ├── DeluxeRoomFactory.java
        │   ├── SuiteRoom.java
        │   └── SuiteRoomFactory.java
        ├── Builder/
        │   └── BookingRoom.java
        ├── Observer/
        │   ├── EventManager.java
        │   ├── NotificationListener.java
        │   ├── GuestObserver.java
        │   ├── StaffObserver.java
        │   ├── ManagerObserver.java
        │   └── Events/
        │       ├── Event.java
        │       ├── BookingEvent.java
        │       ├── CheckInEvent.java
        │       ├── PaymentEvent.java
        │       └── CheckOutEvent.java
        ├── Strategy/
        │   ├── BillingStrategy.java
        │   ├── BillingContext.java
        │   ├── RegularPricingStrategy.java
        │   ├── MemberDiscountStrategy.java
        │   └── SeasonalPricingStrategy.java
        ├── TemplateMethod/
        │   ├── HotelWorkflow.java
        │   ├── CheckInWorkflow.java
        │   └── CheckOutWorkflow.java
        └── Adapter/
            ├── PaymentProcessor.java
            ├── PaymentAdapter.java
            └── LegacyPaymentProcessor.java

---

## System Flow

    1. Config.getInstance()            Load hotel settings       (Singleton)
    2. DeluxeRoomFactory.createRoom()  Create room               (Factory Method)
    3. BookingRoom.Builder.build()     Construct booking         (Builder)
    4. eventManager.publish()          Notify observers          (Observer)
    5. CheckInWorkflow.execute()       Run check-in steps        (Template Method)
    6. BillingContext.calculateBill()  Apply pricing strategy    (Strategy)
    7. PaymentAdapter.processPayment() Pay via legacy system     (Adapter)
    8. CheckOutWorkflow.execute()      Run check-out steps       (Template Method)

---

## Classes and Responsibilities

### Singleton — Config

Holds all hotel-wide settings. Instantiated once and shared across the system.

    Config config = Config.getInstance();
    config.getHotelName();    // "HDR Hotel"
    config.getTaxRate();      // 0.15
    config.getCheckInHour();  // 3 PM
    config.getCheckOutHour(); // 11 AM

---

### Factory Method — Room Creation

Each room type has its own factory. The client depends only on RoomFactory, never on concrete room classes.

    RoomFactory factory = new DeluxeRoomFactory();
    Room room = factory.createRoom();

| Room Type | Price (before tax) | Capacity | Features |
|---|---|---|---|
| Standard | $150 | 2 | TV, AC |
| Deluxe | $350 | 5 | TV, AC, Sea View |
| Suite | $700 | 8 | TV, AC, Sea View, Jacuzzi, Kitchen |

---

### Builder — BookingRoom

Constructs a booking object step-by-step with optional services. Required fields are enforced in the constructor; optional services use fluent setters.

    BookingRoom booking = new BookingRoom.Builder(room, "Sara Ahmed", "6-10-2026", "6-15-2026")
        .setBreakfast(true)
        .setDinner(true)
        .setDessert(true)
        .build();

---

### Observer — Notification System

EventManager maintains a list of NotificationListener subscribers. When an event fires, all subscribers are notified automatically with no conditional logic involved.

**Events:** BookingEvent — CheckInEvent — PaymentEvent — CheckOutEvent

**Observers:** GuestObserver — StaffObserver — ManagerObserver

    eventManager.subscribe(new StaffObserver());
    eventManager.subscribe(new GuestObserver());
    eventManager.publish(new BookingEvent(), booking.getFullName());

---

### Template Method — Hotel Workflows

HotelWorkflow defines a fixed skeleton in executeWorkflow() declared as final. Subclasses implement the variant steps.

**Workflow steps:**

1. validateBooking() — private, validates booking is not null
2. prepareProcess() — abstract, preparation before main action
3. performMainAction() — abstract, core check-in or check-out logic
4. processServices() — private, logs requested meal services
5. notifyGuest() — abstract, fires Observer events
6. logToSystem() — abstract, logs record to system

CheckOutWorkflow additionally runs billing (Strategy) and payment (Adapter) inside performMainAction().

---

### Strategy — Billing

BillingContext holds a BillingStrategy reference. The strategy can be switched at runtime with no if/switch needed.

    BillingContext billing = new BillingContext(new MemberDiscountStrategy());
    billing.setStrategy(new SeasonalPricingStrategy());

| Strategy | Calculation |
|---|---|
| RegularPricingStrategy | price x nights |
| MemberDiscountStrategy | price x nights x 0.80 (20% off) |
| SeasonalPricingStrategy | price x nights x 1.30 (+30% peak) |

---

### Adapter — Legacy Payment Integration

LegacyPaymentProcessor exposes makePayment(clientName, amount). The new system expects processPayment(guestName, amount) via the PaymentProcessor interface. PaymentAdapter bridges the gap without touching the legacy code.

    PaymentProcessor (interface)
         implements
    PaymentAdapter.processPayment()
         calls
    LegacyPaymentProcessor.makePayment()

---

## How to Run

### Requirements

- Java 8 or higher
- NetBeans IDE or any Java IDE or command line

### Run with NetBeans

1. Clone or download the repository
2. Open NetBeans and select File then Open Project then select the SHMS folder
3. Right-click the project and select Run

### Run from Command Line

    cd SHMS/src
    javac -d ../out $(find . -name "*.java")
    java -cp ../out NewMain

---

## Sample Output

    === HDR Hotel ===
    Currency : USD
    Tax Rate : 15.0%
    Check-In from  : 3:00 PM
    Check-Out by   : 11:00 AM

    --- Room Creation ---
    Room Details: Deluxe Room
    Capacity: 5
    Price: 402.50
    Features:[TV, Air Conditioning, Sea View]

    --- Booking ---
    [Staff Notification] Hello Sara Ahmed! Booking has been confirmed!
    [Manager Notification] Hello Sara Ahmed! Booking has been confirmed!
    [Guest Notification] Hello Sara Ahmed! Booking has been confirmed!

    Starting: CHECK-IN WORKFLOW
    [Step 1] Validating booking...
    [Step 2] Preparing room for guest arrival...
    [Step 3] Performing Check-In...
    Welcome, Sara Ahmed!
    [Step 5] Sending notifications...
    [Step 6] Logging check-in to system...
    Workflow [CHECK-IN WORKFLOW] completed successfully.

    Starting: CHECK-OUT WORKFLOW
    [Step 3] Performing Check-Out...

    ========== BILLING ==========
    Strategy : Member Discount (20% OFF)
    Guest    : Sara Ahmed
    Nights   : 5
    [Member Discount] Original Price : $2012.50
    [Member Discount] Discount (20%) : -$402.50
    TOTAL    : $1610.00

    --- Payment Processing (Adapter Pattern) ---
    [Legacy System] Processing payment...
    [Legacy System] Client : Sara Ahmed
    [Legacy System] Amount : $1610.00
    [Legacy System] Payment approved.

    Workflow [CHECK-OUT WORKFLOW] completed successfully.
    System completed successfully.

---

## Technologies

- Language: Java 8+
- IDE: Apache NetBeans
- Architecture: Design Patterns (GoF)
- Build: NetBeans Ant build (build.xml)

---

## Course Information

| | |
|---|---|
| Course | Software Design and Architecture Lab |
| Project | Design Patterns Lab Final Project |
| University | Islamic University of Gaza |
| Faculty | Faculty of Information Technology |
| Teaching Assistant | Maryam Skaik |

---

## Team Members

| Name |
|---|
| Doaa Oukal |
| Raghad Saqallah |
| Haneen Hamdia |
