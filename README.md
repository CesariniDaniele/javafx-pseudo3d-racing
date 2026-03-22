# JavaFX Pseudo-3D Racing

**JetBikeRace** is an arcade-style racing game prototype built in JavaFX, focused on implementing pseudo-3D road rendering with a retro aesthetic.

This project explores how to recreate classic “old-school” racing visuals using a 2D engine, without relying on modern 3D frameworks.

The current implementation focuses on a **straight-track racing scenario**, serving as a foundation for future extensions such as curves and elevation changes.
---

## 🚀 Overview

The project implements a pseudo-3D rendering technique inspired by classic arcade games such as *OutRun* and *Pole Position*.

Unlike most available resources, this implementation is entirely written in **Java using JavaFX**, providing a complete, object-oriented architecture and a working game prototype.

---

## 🧠 Motivation

Most pseudo-3D tutorials available online:

- are written in JavaScript or C++
- focus only on mathematical intuition
- do not provide a full application architecture

This project aims to fill that gap by:

- providing a **Java/JavaFX implementation**
- designing a **complete OOP architecture**
- handling rendering, game logic, and AI from scratch

---

## 🏗️ Architecture

The project is structured into several main components:

- `roadComponents/` → camera, road, and segment projection logic  
- `assetsManagers/` → resource and entity management  
- `sprites/` → modelling of player, opponents and decorative elements
- `scenes/` → scene management and game flow  
- `ai/` → opponent behavior and race logic, included collision detection  

This separation helps enforce the **single responsibility principle** and makes future refactoring toward a more modular design easier.

A detailed architectural description and UML diagrams are available in:

👉 [Architecture Documentation](docs/architecture.md)

---

## 🎮 Features

Main implemented features include:

- pseudo-3D road rendering on a straight track
- player movement and opponent AI
- NPC obstacles and collision handling
- dynamic score system
- minimap and HUD
- multiple scenes (start, gameplay, ending)
- sound effects

A detailed description of gameplay mechanics and system behavior is available here:

👉 [Features Documentation](docs/features.md)

---

## 📸 Screenshots

![Gameplay](docs/images/gameplay.png)

---

## ⚙️ How to Run

### Requirements

- Java (JDK 8+ or compatible with JavaFX setup)
- JavaFX SDK (if not bundled with your JDK)

### Run from source

Compile and run the project using NetBeans or via command line using the provided `build.xml`.

---

## 🧪 Technical Notes

The core of the project is the pseudo-3D projection:

- road is divided into segments
- each segment is projected using perspective scaling
- screen-space interpolation is used to simulate depth

This approach allows creating a 3D illusion using only 2D transformations.

---

## 🔮 Future Work

Planned improvements include:

- curved roads  
- hills and elevation changes  
- modularization of the rendering pipeline  
- improved architecture (decoupling game logic and rendering)  
- tutorial-style documentation of the pseudo-3D technique  

---

## 🤝 Contributions

Suggestions, discussions, and contributions are welcome.

In particular, contributions related to:

- modular design  
- advanced pseudo-3D features  

are highly appreciated.

---

## 📄 Additional Material

- [Architecture Documentation](docs/architecture.md)
- [Features Documentation](docs/features.md)
- UML diagrams in `docs/uml/`

---

## 🛠️ Technologies

- Java  
- JavaFX  

---

## 📦 Assets Notice

This project is developed for educational and demonstration purposes.

Some visual and audio elements are inspired by or derived from classic games such as *Chrono Trigger* and *Pole Position*.

These materials are included solely for non-commercial use. If required, they can be removed or replaced with original assets.

## 📌 Author

Daniele Cesarini  
MSc student in Data Science @ University of Perugia  
