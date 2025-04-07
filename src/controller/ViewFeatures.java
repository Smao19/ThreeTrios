package controller;

/**
 * Represents high level events that can occur in the view.
 */
public interface ViewFeatures {
  /**
   * Called when a player selects a card from their hand.
   * @param playerIndex The index of the player (0 or 1).
   * @param cardIndex The index of the selected card in the player's hand.
   */
  void selectCard(int playerIndex, int cardIndex);

  /**
   * Called when a player selects a cell on the grid to place a card.
   * @param row The row index of the selected grid cell.
   * @param col The column index of the selected grid cell.
   */
  void selectGridCell(int row, int col);
}

