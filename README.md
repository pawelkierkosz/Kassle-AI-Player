# Kassle AI Player

This repository contains the implementation of an AI player for board games using the alpha-beta pruning algorithm with depth-limited search. It was developed as part of a university project.

## Authors
- **Bartłomiej Rudowicz**
- **Paweł Kierkosz**

## Project Description
The provided Java implementation utilizes the alpha-beta pruning technique to efficiently explore possible game moves and determine optimal plays within a specified search depth. The AI evaluates board states to maximize its chances of winning by prioritizing moves that either immediately secure a win or block an opponent's potential win.

### Key Features:
- **Alpha-Beta Pruning:** Optimizes minimax search by eliminating branches that cannot possibly affect the final decision.
- **Depth-limited Search:** Configurable search depth to balance between computation time and AI performance.
- **Time Management:** Ensures decisions are made within an allocated time limit, adding robustness in competitive scenarios.
- **Immediate Threat Detection:** Quickly identifies moves that could lead to immediate victory or prevent an imminent loss.

## Technical Details
- **Language:** Java
- **Core Algorithm:** Alpha-beta pruning
- **Max Search Depth:** Adjustable, default set to 4
