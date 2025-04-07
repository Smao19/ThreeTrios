package provider.strategy;

import java.util.Objects;

/**
 * Represents a possible Play for a player in ThreeTrios.
 * Contains only the row, column, and hand index of the possible play.
 */
public class Move {
  private int row;
  private int col;
  private int handInd;

  /**
   * Constructs a new Move object.
   * @param row the row of the move.
   * @param col the column of the move.
   * @param handInd the index of the hand that is being played.
   */
  public Move(int row, int col, int handInd) {
    this.row = row;
    this.col = col;
    this.handInd = handInd;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public int getHandInd() {
    return handInd;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Move) {
      Move other = (Move) o;
      return other.row == row && other.col == col && other.handInd == handInd;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col, handInd);
  }
}
