# ECM2414_CA
Software development CA

###Development Plan
Create thread-safe classes for
- Card
    Via synchronisatoon/immutable properties
    Attributes including faceValue
- CardDeck
    Will be shared amongst players.
- Player
    Attributes include
    Methods include:
      Drawing from the left cardDeck
      Discarding cards via the right cardDeck
    Game Strategy 
- CardGame
    Will be the main class & game entry point
    Reads no. of players
    Initialise the game, distributes hands & fill the cardDecks
    Game logic including monitoring the game state, win/lose conditions
    Handle game termination
    Create player output files and deck output files as described.

**Input Pack File**:
The input pack file should contain 8n rows of non-negative integers. You can read and parse this file in the `CardGame` class.

**Output Files**:
Create output files for each player to record their actions during the game, following the specified format.
Create deck output files to display the contents of each deck at the end of the game.

**Ring Topology**:
Implement the ring topology for players and card decks to mimic the game's layout.

**Game Logic**:
Allow players to draw, discard, and check for a winning hand.
Make sure that the game continues until a player wins or the game ends.

**Error Handling**:
Handle cases where the input pack file is invalid & prompt the user to provide a valid file.

**Display Messages**:
When a player wins or the game ends.

**Testing**:
Test your implementation with different numbers of players and input packs to ensure it functions correctly.
