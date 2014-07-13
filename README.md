Checkers
========

This game does not support user play. Instead, it allows the PC to play itself using different AIs to determine the optimal move.
The AIs used are Minimax, Quiescence Search and UCT (Monte Carlo); random moves are also supported. The point of this project was to determine which algorithm was more successful at selecting the optimal move.

Quiescence Search was deemed the victor as it always had the most pieces remaining by the end of the game. The reason the AIs would never completely win is that eventually you would reach a situation where moving back and forth was a sure way to ensure your piece would not be captured and therefore not lose regardless of you had more pieces.
