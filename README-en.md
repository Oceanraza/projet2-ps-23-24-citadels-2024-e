# Citadels - Team E

Welcome to Citadels!
[French Version](README.md)

This project was developed as part of a collaborative programming project. It is a Java program allowing 4 robots to play the first edition of the Citadels game.

---

## Table of Contents

- [Topic](#topic)
- [Team Members](#team-members)
- [Usage Instructions](#usage-instructions)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Exploring the Project](#exploring-the-project)
- [Usage](#usage)
  - [Compile and Execute](#compile-and-execute)
  - [Running Tests](#running-tests)

---

## Topic

This project aims to create a computerized version of the "Citadels" game, based on the first edition, integrating the following features:

### Features:

- **Game Interface:**
  - Representation of cards and cities for each player.
- **Game Engine:**
  - Management of setup, turns, actions, interactions, game progression, and end.
- **Development of Game Robots:**
  - From basic level to demonstrable sophisticated strategies.
- **Game Simulation:**
  - Implementation of a game simulation between 4 robots, with points calculation and ranking establishment at the end.
- **Simplified Textual Visualization:**
  - Textual display of the current state of the game.

---

## Team Members

This project was carried out by Team Polytâche (E), consisting of 4 students in their 3rd year of the 2023 Polytech Nice Sophia promotion, specializing in Computer Science:
- [Darina Chan](https://github.com/DarinaChan)
- [Océan Razafiarison](https://github.com/Oceanraza)
- [Mathis Jullien](https://github.com/Mathis-Jullien)
- [Quentin Elleon](https://github.com/QuentinELLEON)

## Usage Instructions

### Prerequisites

Before using this program, make sure you have the following installed on your machine:

- **Java 17**: The project is developed in Java 17. You can download it from the [official OpenJDK website](https://jdk.java.net/17/).

- **Git**: If you don't have it already, install Git from the [official Git website](https://git-scm.com).

- **Maven**: Used for compiling and running the program, you can download it from the [official Maven website](https://maven.apache.org/download.cgi).

### Installation

To install the program:

Open a console and navigate to the directory where you want to download the program.
```bash
git clone https://github.com/pns-si3-projects/projet2-ps-23-24-citadels-2024-e.git
cd projet2-ps-23-24-citadels-2024-e
```

### Exploring the Project

- **'src/main/java/fr/cotedazur/univ/polytech/startingpoint'**: Contains the source code.
- **'src/test/java/fr/cotedazur/univ/polytech/startingpoint'**: Contains unit tests.

## Usage

### Compile and Execute

Make sure you are in the root directory of the project (**'projet2-ps-23-24-citadels-2024-e'**). You can then compile and execute the main program using the following Maven command:

```bash
mvn exec:java
```

### Running Tests

Unit tests are located in the directory `src/test/java/fr/cotedazur/univ/polytech/startingpoint`. To run them, use the following command:

```bash 
mvn clean package
```

Ensure that all tests pass without errors, confirming the robustness of the program.

We hope you enjoy using our program! Feel free to play multiple games to discover the different possibilities.