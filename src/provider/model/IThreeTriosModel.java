package provider.model;

import java.util.List;

import provider.model.card.ICard;
import provider.model.cell.Player;
import provider.model.cell.Cell;

/**
 * Interface for the ThreeTrios game model.
 */
public interface IThreeTriosModel {
  /**
   * draws the possibly shuffled deck into the players hands one card at a time.
   * @param shuffled whether the deck is to be shuffled.
   * @throws IllegalStateException if game is in progress or has finished
   */
  void startGame(boolean shuffled);

  /**
   * Adds a feature listener to the models feature list.
   * @param feature the feature to add.
   */
  void addFeature(ModelFeatures feature);

  /**
   * Plays a card to the Grid for the current player's turn.
   * @param row 0-based row index
   * @param col 0-based column index
   * @param handIndex 0-based hand index
   * @throws IllegalStateException if the game has not started or has finished
   * @throws IllegalArgumentException row col or handIndex out of bounds
   */
  void playToGrid(int row, int col, int handIndex);

  /**
   * Return true if the game is over.
   * @return true if game is over
   */
  boolean isGameOver();

  /**
   * Gets the hand of the specified player.
   * @param player player whose hand you need
   * @return list of player's hand. Modifying this list has no effect on the player's hand
   * @throws IllegalStateException if the game has not started.
   */
  List<ICard> getPlayerHand(Player player);

  /**
   * Gets the cells on the board.
   * @return 2D array of Cells on grid. Modifying this array has no effect on the grid.
   */
  Cell[][] getGrid();

  /**
   * Returns the number of cells a player controls.
   * @param player player specified
   * @return int number of cells owned
   */
  int countPlayerCells(Player player);

  /**
   * Gets the current turn.
   * @return The player whose turn it is.
   */
  Player getCurrentTurn();

  /**
   * Gets the winner.
   * @return the player who won.
   * @throws IllegalStateException if game is not over.
   */
  Player getWinner();

  /**
   * Counts how many cards a hypothetical move would flip.
   * @param row row where the move would be played
   * @param col column where the move would be played
   * @param card card that would be play
   * @return int count of the number that would be flipped
   */
  int hypotheticalFlip(int row, int col, ICard card);
}
