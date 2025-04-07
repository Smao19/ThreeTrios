package player;

/**
 * Composite class used to group together data for a potential move in the game.
 */
public class GameMoveData {
  private final int gridRow;
  private final int gridCol;
  private final int cardIndex;

  /**
   * Constructor for initializing game-move data to be grouped together.
   * @param rowIdx row in grid to play to
   * @param colIdx column in grid to play to
   * @param handIndex index in the players hand that points to the card to be played
   */
  public GameMoveData(int rowIdx, int colIdx, int handIndex) {
    this.gridRow = rowIdx;
    this.gridCol = colIdx;
    this.cardIndex = handIndex;
  }

  public int getCardIndex() {
    return cardIndex;
  }

  public int getGridRow() {
    return gridRow;
  }

  public int getGridCol() {
    return gridCol;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    // Check for null and ensure the object is the right type
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    // Cast the obj to GameMoveData for comparing fields
    GameMoveData other = (GameMoveData) obj;
    return gridRow == other.gridRow &&
            gridCol == other.gridCol &&
            cardIndex == other.cardIndex;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + gridRow;
    result = 31 * result + gridCol;
    result = 31 * result + cardIndex;
    return result;
  }

  @Override
  public String toString() {
    return "row: " + gridRow + " col: " + gridCol + " index: " + cardIndex;
  }
}
