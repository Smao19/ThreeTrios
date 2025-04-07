package controller;

/**
 * Public interface representing the events triggered at certain times within the model.
 */
public interface ModelFeatures {

  /**
   * Triggered whenever a player's turn begins.
   * @param playerIndex the index of the current players turn. (0 for red 1 for blue)
   */
  void playerTurn(int playerIndex);

  /**
   * Triggered whenever the game ends.
   */
  void gameEnd();
}
