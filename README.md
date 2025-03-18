# Othello AI by Stoak

## Introduction
This project is an implementation of the Minimax Search algorithm with Alpha-Beta Pruning for the game Othello. It was developed as part of the Introduction to AI course at the IT University of Copenhagen during the Spring 2025 semester.

### Implemented by:
- alby@itu.dk
- brop@itu.dk
- kegr@itu.dk
- stwp@itu.dk

### How to run it

* To run our AI against DumAI:
    * ```java Othello OthelloAIStoak DumAI <size>```
* To run our AI against yourself:
    * ```java Othello human OthelloAIStoak <size>```

*You may want to build and run the command from the build folder*
*(In Intellij it is the .\out\production\AI-Othello\ by default)*

### Key Features
**Minimax Algorithm with Alpha-Beta Pruning** – Optimises move selection by reducing the number of states explored.

**Penalty Matrix for Board Evaluation** – Assigns different values to board positions to favour positions.

**Cut-Off Function** – Limits the search depth to maintain a reasonable response time under 10 seconds per move.