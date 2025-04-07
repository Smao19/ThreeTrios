# Three Trios – Java Strategy Card Game 🃏

**Three Trios** is a fully configurable two-player strategy card game built in Java, showcasing modular architecture, combinable AI strategies, and a rich GUI experience. Developed as a deep dive into object-oriented design, the game offers customizable rulesets, deck configurations, and board layouts — GUI.

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Principles-informational)
![MVC](https://img.shields.io/badge/Architecture-MVC-blue)

---

## 🎮 Features

- 🔧 Configurable rule sets, game boards, and card decks
- 🧠 AI opponents with pluggable strategies
- 🖼️ Java Swing GUI with multi-window interface
- 🧩 MVC architecture with key design patterns:
  - Factory, Builder, Command, Adapter, Decorator, Callback
- ⚙️ Flexible framework to support alternate interfaces via adapters
- ✅ Unit, integration, and end-to-end tested

---

## 🚀 Getting Started

### 🖥️ Requirements
- Java 8+ installed
- Terminal or IDE (e.g., IntelliJ) to run

### 📦 Download

1. Clone or download this repo
2. Navigate to the `/dist` folder
3. Use the pre-built `.jar` file for easy access, or compile from source

---

## 🕹️ Running the Game

From the root directory or the `/dist` folder, run:

```bash
java -jar ThreeTrios.jar [args...]
```

### 🧭 Usage Parameters

The main entry point is documented in `src/ThreeTrios.java`, which explains how to run various modes:

```bash
# Start a standard two-player GUI game
java -jar ThreeTrios.jar

# Start a game with custom card and board configs
java -jar ThreeTrios.jar path/to/cardConfig.txt path/to/boardConfig.txt

# Start a game with a specific deck + board + optional strategy for AI
java -jar ThreeTrios.jar resources/SmallDeckCardConfig.txt resources/basicBoardConfig.txt
```

You can find various pre-built configuration files in `/resources/`.

---

## 🧪 Testing

Test code is located in `/test/`, organized alongside main modules like `model/` and `player/`. Tests cover game logic, strategies, and game state transitions.

To run tests:
- Import the project into IntelliJ
- Run test files using the built-in test runner

---

## 🧩 Adapter Interfaces

The `/src/provider/` directory contains interfaces and enums from a peer's project. This module demonstrates adapter pattern usage — allowing the game to be played using either the original architecture or the peer-provided structures, without any changes to core logic.

This highlights:
- Code flexibility
- Interface-driven design
- Adapter compatibility with external systems

---

## 🧠 Architecture Overview

- **Model**: Core game logic and rules
- **View**: Graphical interface
- **Controller**: Orchestrates game flow and interactions
- **Design Patterns**: Implemented throughout to promote modularity and testability

---

## 📁 Project Structure

```
├── src/            # Source code (MVC-based)
│   ├── controller/
│   ├── model/
│   ├── player/
│   ├── view/
│   ├── provider/   # Adapter interfaces for external compatibility
│   └── ThreeTrios.java
├── test/           # Unit and integration tests
├── resources/      # Board and card configuration files
├── dist/           # Executable JAR
│   └── ThreeTrios.jar
```
