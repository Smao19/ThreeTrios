package provider.model.cell;

import provider.model.card.ICard;

/**
 * Represents a cell in the board, can either be a Hole which cannot be played to, or a Card Cell
 * which can hold a card and which player controls it.
 */
public interface Cell {

  /**
   * Checks if a new card can be played to the cell.
   * @return true if a player can play a card to the cell.
   */
  boolean canPlayCard();

  /**
   * Adds a card to the cell.
   * canPlayCard() will now return true.
   * @param card the card to add.
   * @throws IllegalStateException if the cell is a hole.
   * @throws IllegalStateException if the cell already has a card.
   * @throws IllegalArgumentException if the card is null.
   */
  void addCard(ICard card);

  /**
   * Gets the card.
   * @return The cell's card.
   * @throws IllegalStateException if the cell is a hole.
   * @throws IllegalStateException if the cell does not have a card.
   */
  ICard getCard();

  /**
   * Sets the owner of this cell to player.
   * @param player the given player.
   * @throws IllegalStateException if the cell is a hole.
   */
  void setPlayer(Player player);

  /**
   * Gets the player.
   * @return the player associated with this cell.
   * @throws IllegalStateException if the cell is a hole.
   */
  Player getPlayer();

  /**
   * Gets the type.
   * @return the type of the cell.
   */
  CellType getType();

  /**
   * Converts the Cell to a String.
   * @return Either the first letter of the player of the card in the cell or
   *     _ if there is no card or " " if hole.
   */
  String toString();

  /**
   * Checks equality.
   * @return true if the card and player are the same for both classes.
   */
  boolean equals(Object obj);

  /**
   * Gets the hashcode.
   * @return a hash based on the card and player.
   */
  int hashCode();

  Cell cloneCell();
}
