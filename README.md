# JavaFX Pseudo-3D Racing

**JetBikeRace** is an arcade-style racing game prototype built in JavaFX, focused on implementing pseudo-3D road rendering with a retro aesthetic.

This project explores how to recreate classic “old-school” racing visuals using a 2D engine, without relying on modern 3D frameworks.

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
- `sprites/` → rendering of player and opponents  
- `scenes/` → scene management and game flow  
- `ai/` → opponent behavior and race logic  

This separation allows the rendering logic to remain distinct from gameplay and asset management.

---

## 🎮 Features

- pseudo-3D road rendering using perspective projection  
- sprite-based rendering (player, opponents, environment)  
- opponent AI and collision handling  
- scene-based architecture (menu, gameplay, credits)  
- audio management (music and sound effects)  

---

## 📸 Screenshots

*(you can add gameplay images here later)*

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

- rendering architecture  
- modular design  
- advanced pseudo-3D features  

are highly appreciated.

---

## 📄 Additional Material

- UML diagram (project architecture)
- project report (PDF)

---

## 🛠️ Technologies

- Java  
- JavaFX  
- Object-Oriented Programming  

---

## 📌 Author

Daniele Cesarini  
MSc student in Data Science @ University of Perugia  
