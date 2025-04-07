package model;

import java.util.List;

import model.card.CardInterface;
import model.cell.Cell;
import player.GameMoveData;
import player.Player;
import player.ReadOnlyPlayer;

/**
 * Interface for encapsulating all the necessary methods for simply observing any ThreeTriosModel
 * would need.
 */
public interface ReadOnlyThreeTriosModel {
  /**
   * Predicate which informs if the game has ended or not.
   * @return boolean representing if game is over
   * @throws IllegalStateException if game hasn't started
   */
  boolean isGameOver();

  /**
   * Predicate which informs if the game has started or not.
   * @return boolean representing if game has started
   */
  boolean isGameStarted();

  /**
   * Gets the winner(s) of the game. Multiple in case of ties.
   * @return array of the player(s) that won the game
   * @throws IllegalStateException if game hasn't started or isn't over
   */
  Player[] getWinner();

  /**
   * Gets a deep copy of the game grid.
   * @return a copy of the grid which can be modified without impacting the game.
   */
  Cell[][] getGrid();

  /**
   * Gets the player whose turn it currently is.
   * @return the player whose turn it is to play in the game
   */
  ReadOnlyPlayer getPlayerInTurn();

  /**
   * Gets a list player classes representing all active players.
   * @return all active players in the game.
   * */
  List<ReadOnlyPlayer> getPlayers();

  /**
   * Gets the size of the grid represented as an array.
   * @return array representing gridSize: [numRows, numCols]
   */
  int[] getGridSize();

  /**
   * Gets the card, if any, at the supplied location on the grid.
   * @param row desired row idx on the grid
   * @param col desired col idx on the grid
   * @return the card at the desired cell location, or null if no
   *         card at that location (including off grid locations)
   */
  model.card.CardInterface getGridCellCard(int row, int col);

  /**
   * Checks if a supplied playing of a card to a cell is considered legal.
   * @param handIndex index to desired card in the players hand
   * @param gridRow desired row idx on the grid
   * @param gridCol desired col idx on the grid
   * @throws IllegalArgumentException if handIndex outside the valid indexes of the hand list
   * @throws IllegalArgumentException if desired cell is a hole or an already filled card cell
   *                                  or not located on the grid
   * @throws IllegalStateException if game hasn't started or is over
   */
  void isLegalPlay(int handIndex, int gridRow, int gridCol);

  /**
   * Gets the owner, if any, at a supplied grid location.
   * @param gridRow desired row idx on grid
   * @param gridCol desired col idx on grid
   * @return Owner of card at supplied grid location, or null if no card at location
   */
  Player getOwnerAtCell(int gridRow, int gridCol);

  /**
   * Returns the number of cards that would be flipped if
   * the supplied card was played at the supplied coordinate.
   *
   * @param move GameMoveData object containing the card index in hand, and grid position of
   *             desired move.
   * @return number of cards that would be flipped if that play occurred
   * @throws IllegalArgumentException if desired move is considered illegal
   * @throws IllegalStateException if game hasn't started or is over
   */
  int potentialCardsFlipped(GameMoveData move);

  /**
   * Return the current score of the desired player.
   * @param isRedPlayer true if trying to get red players score, false if blue players score.
   * @return score of desired player
   */
  int getPlayerScore(boolean isRedPlayer);

  /**
   * Getter used for populating a read only mock.
   * @return the newly placed card
   */
  public CardInterface getNewlyPlacedCard();

  /**
   * Getter used for populating a read only mock.
   * @return the models red player
   */
  public ReadOnlyPlayer getRedPlayer();

  /**
   * Getter used for populating a read only mock.
   * @return the models blue player
   */
  public ReadOnlyPlayer getBluePlayer();
}
