package provider.model;

import provider.model.cell.Player;

/**
 * Features the model can call to update the controller of the game state.
 */
public interface ModelFeatures {

  /**
   * Tells the featureListeners which turn it is.
   * @param forWhom Player whose turn it is
   */
  void yourTurn(Player forWhom);

  /**
   * Tells the controller the game is over and game score.
   * @param winner Player who won
   * @param winningScore Number of grids controlled by the winning player
   */
  void gameOver(Player winner, int winningScore);

  /**
   * Tells the feature listeners the board state has been updated.
   */
  void updateView();
}
