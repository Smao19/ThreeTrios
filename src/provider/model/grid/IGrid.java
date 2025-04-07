package provider.model.grid;

import model.cell.Cell;
import provider.model.card.ICard;
import provider.model.cell.Player;

/**
 * Interface for a cell grid in ThreeTrios. Used as the game board
 * for the model to play to and enforce game rules.
 */
public interface IGrid {

  /**
   * Plays a card to location on the grid.
   * @param row 0-based row index.
   * @param col 0-based column index.
   * @param card card to be played.
   * @param player player that played the card.
   * @throws IllegalArgumentException if row or col are outside of grid or card is null.
   * @throws IllegalStateException if the location has a card already or is a hole.
   * */
  void playToGrid(int row, int col, ICard card, Player player);

  /**
   * Triggers a recursive battle starting from the given location.
   * @param row 0-based row index.
   * @param col 0-based column index.
   * @throws IllegalArgumentException if row or col are outside of grid or card is null.
   * @throws IllegalStateException if the location does not have a card or is a hole.
   */
  void battle(int row, int col);

  /**
   * Determines if the grid is full.
   * @return true if the grid is full, otherwise false.
   */
  boolean isFull();

  /**
   * Counts the number of cells that have a card that belongs to the given player.
   * @param player the given player.
   * @return the number of cells.
   */
  int countPlayerCards(Player player);

  /**
   * Gets the Cell at the given row and column.
   * @param row the 0-indexed row.
   * @param col the 0-indexed column.
   * @return The Cell.
   */
  Cell getCell(int row, int col);

  /**
   * Counts the number of playable cells.
   * @return int of number of cells a player can play to
   */
  int countPlayableCells();

  /**
   * Converts the grid into a 2-d array of cells.
   * @return a 2-d array of all the cells in the grid.
   */
  Cell[][] getGridCells();

  /**
   * Counts how many cards a hypothetical move would flip.
   * @param row row where the move would be played
   * @param col column where the move would be played
   * @param card card that would be play
   * @param player play that would play the move
   * @return int count of the number that would be flipped
   */
  int hypotheticalFlip(int row, int col, ICard card, Player player);

}