package provider.view;

import provider.model.cell.Player;

/**
 * Represents actions that a player can take while playing the game.
 */
public interface EventFeatures {

  /**
   * Plays to the grid given the location and index of the players hand.
   * @param row row to play to
   * @param col column to play to
   */
  void playToGrid(int row, int col);

  /**
   * Selects the specified players handInd.
   * @param handInd index to select
   * @param player player specified.
   */
  void selectCard(int handInd, Player player);
}
