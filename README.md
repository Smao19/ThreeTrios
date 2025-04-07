# Three Trios – Java Strategy Card Game 🃏

**Three Trios** is a fully configurable two-player strategy card game built in Java, showcasing modular architecture, combinable AI strategies, and a rich GUI experience. Developed as a deep dive into object-oriented design, the game offers customizable rulesets, deck configurations, and board layouts — GUI.

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

### ✨ Usage Parameters

The program accepts a flexible set of command-line arguments, allowing you to customize the gameplay experience. Note you can enter anything (e.g. "0" for the first 2 params if you want to use the default configs). Also if you choose to play a computer opponent (e.g. 3rd param is "C") you can list 1 or more strategies to be used by the opponent.

```bash
java -jar ThreeTrios.jar [board_config_path] [card_config_path] [player_color] [opponent_type] [opponent_strategies...]
```

| Argument Position | Description |
|-------------------|-------------|
| `1` – `board_config` *(required)* | Path to a board layout `.txt` file. If the path is invalid, a default board config is used. |
| `2` – `card_config` *(required)* | Path to a card deck config `.txt` file. If invalid, a default card config is used. |
| `3` – `player_color` *(required)* | `"R"` for Red or `"B"` for Blue. |
| `4` – `opponent_type` *(required)* | `"H"` for human or `"C"` for computer opponent. |
| `5+` – `opponent_strategies` *(optional)* | List of integers representing AI strategies (**only applies if opponent is a computer**) |

#### 🧠 Available AI Strategies:
- `1`: **Corner Strategy** — favors corner moves
- `2`: **Least Flippable Strategy** — minimizes flippable cards
- `3`: **Max Cards Flip Strategy** — maximizes flipped cards

> 🔍 Multiple strategies can be passed and will be combined during gameplay.

---

#### 🧪 Example Usages:

```bash
# Will error out: not enough arguments
java -jar ThreeTrios.jar

# Use board and card config (defaults used if paths are invalid)
java -jar ThreeTrios.jar resources/basicBoardConfig.txt resources/SmallDeckCardConfig.txt

# Play as Blue vs a computer using max-flip strategy
java -jar ThreeTrios.jar resources/basicBoardConfig.txt resources/SmallDeckCardConfig.txt B C 3

# Play as Red vs a computer using a combo of least-flippable and corner strategies
java -jar ThreeTrios.jar resources/basicBoardConfig.txt resources/SmallDeckCardConfig.txt R C 2 1
```

👉 **Explore `/src/player/strategy/`** to see how each AI strategy works and interacts with various board and deck configurations.

You can also find various pre-built configuration files in `/resources/`.

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
