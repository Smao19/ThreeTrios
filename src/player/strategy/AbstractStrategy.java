package player.strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.cell.CardCell;
import model.cell.Cell;
import model.ReadOnlyThreeTriosModel;

/**
 * Abstract strategy class that groups together common code for any strategies.
 */
public abstract class AbstractStrategy implements Strategy {
  protected ReadOnlyThreeTriosModel model;
  protected int bestMoveValue;


  /**
   * Constructor for initializing an AbstractStrategy with a ReadOnly Model.
   *
   * @param model ReadOnly Model to be read from
   */
  public AbstractStrategy(ReadOnlyThreeTriosModel model) {
    this.model = model;
    bestMoveValue = 0;
  }

  @Override
  public int getBestMoveValue() {
    return this.bestMoveValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AbstractStrategy)) {
      return false;
    }
    AbstractStrategy that = (AbstractStrategy) o;
    return Objects.equals(model, that.model);
  }

  @Override
  public int hashCode() {
    return Objects.hash(model);
  }

  protected List<Point> getEmptyCardCellsPositions() {
    Cell[][] grid = model.getGrid();
    List<Point> cellPoints = new ArrayList<>();
    int[] dims = model.getGridSize();
    for (int i = 0; i < dims[0]; i++) {
      // row
      for (int j = 0; j < dims[1]; j++) {
        // col
        if (grid[i][j] instanceof CardCell) {
          CardCell cell = (CardCell) grid[i][j];
          if (cell.getCard().isEmpty()) {
            cellPoints.add(new Point(i, j));
            // Points are row, col
          }
        }
      }
    }
    return cellPoints;
  }
}
