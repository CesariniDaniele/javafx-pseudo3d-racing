# 🎮 Features

The application satisfies the following functional and technical requirements:

## Gameplay

- The player controls the bike using left/right inputs
- The opponent (Johnny) is driven by an AI capable of avoiding obstacles
- The player can collide with NPCs, resulting in speed reduction
- The goal is to finish the race before the opponent

## Game Mechanics

- Dynamic scoring system based on player position relative to the opponent
- Collision system:
  - player vs track boundaries
  - player vs NPCs (with bounce effect)
- Progressive acceleration at race start

## UI & UX

- Start screen with best score
- In-game HUD with score and minimap
- End screen with result (win/lose)

## Audio

- Background music for different game states
- Sound effects for countdown and collisions

## Graphics

- Retro 2D sprite-based assets
- Pseudo-3D rendering of the road and environment
- Dynamic scaling of sprites based on depth

## Rendering

- Perspective-based pseudo-3D road
- Segment-based track representation
- Depth-based sprite scaling and positioning