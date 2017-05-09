The program crashes if a King gets captured, but that's actually perfectly acceptable.

To avoid making one of the Rook's ordinary moves unavailable,
the player should only be able to castle by dragging the King.

I'm starting to think that I might not be able to have checkmate/stalemate indications in the move previews.
It creates a recursive mess.

I think the StackOverflowError is caused when you click on a piece that could move to create a situation where both players could castle.
The infinite loop is occuring somewhere among the getValidMoves, isInCheck, and canCastle methods.

Giving the player a choice would be too much work, so I'll just automatically give them a queen.


NOTE: It's entirely possible that this version of the project is outdated. [Give the URL for the Github location]