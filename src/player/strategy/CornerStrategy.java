package player.strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.card.CardInterface;
import model.card.Positions;
import model.cell.CardCell;
import model.cell.Cell;
import model.cell.HoleCell;
import model.ReadOnlyThreeTriosModel;
import player.GameMoveData;

/**
 * Represents the functionality for executing the CornerStrategy.
 */
public class CornerStrategy extends AbstractStrategy {

  /**
   * Constructor for initializing a CornerStrategy with a ReadOnly Model.
   *
   * @param model ReadOnly Model to be read from
   */
  public CornerStrategy(ReadOnlyThreeTriosModel model) {
    super(model);
  }

  @Override
  public List<GameMoveData> execute() {
    List<Point> points = getCornerCells();
    Cell[][] grid = model.getGrid();
    Map<Point, List<Positions>> scores = new HashMap<>();

    for (int i = 0; i < points.size(); i++) {
      Point p = points.get(i);
      if (grid[p.x][p.y] instanceof HoleCell) {
        // If is hole cell, skip
        continue;
      } else if (model.getGridCellCard(p.x, p.y) != null) {
        // If card is present in corner cell, skip
        continue;
      }
      if (i < 2) {
        // If is one of the first two cell values, it is a topmost cell, meaning we check south
        Positions pos1 = Positions.SOUTH;
        if (i == 0) {
          List<Positions> potentialOpenPos = List.of(pos1, Positions.EAST);
          List<Positions> openPos = getOpenSidesOfCell(grid, p, potentialOpenPos);
          scores.put(p, openPos);
        } else {
          List<Positions> potentialOpenPos = List.of(pos1, Positions.WEST);
          List<Positions> openPos = getOpenSidesOfCell(grid, p, potentialOpenPos);
          scores.put(p, openPos);
        }
      } else {
        // If it is not one of the first two values, it is bottommost cell, meaning we check north
        Positions pos1 = Positions.NORTH;
        if (i == 2) {
          List<Positions> potentialOpenPos = List.of(pos1, Positions.EAST);
          List<Positions> openPos = getOpenSidesOfCell(grid, p, potentialOpenPos);
          scores.put(p, openPos);
        } else {
          List<Positions> potentialOpenPos = List.of(pos1, Positions.WEST);
          List<Positions> openPos = getOpenSidesOfCell(grid, p, potentialOpenPos);
          scores.put(p, openPos);
        }
      }
    }
    List<Point> bestPoints = getPointsWithSmallestListSize(scores);
    // The best points are the ones with the least amount of open sides
    // Meaning, in our map, it is the smallest list
    List<GameMoveData> bestMoves = assessBestMoves(bestPoints, scores);
    // Assess the best actual moves given these best points

    if (bestMoves.isEmpty()) {
      bestMoveValue = 0;
      // If no corners are available, return uppermost-leftmost open cell
      Point p = this.getEmptyCardCellsPositions().get(0);
      return List.of(new GameMoveData(p.x, p.y, 0));
    }

    return bestMoves;
  }

  private List<GameMoveData> assessBestMoves(List<Point> bestPoints,
                                             Map<Point, List<Positions>> scores) {
    int score = 0;
    List<GameMoveData> possibleMoves = new ArrayList<>();
    List<CardInterface> hand = model.getPlayerInTurn().getHandCopy();

    for (Point p : bestPoints) {
      // for each possible point
      List<Positions> openPoss = scores.get(p);
      for (int i = 0; i < hand.size(); i++) {
        // check to find best value(s) relevant to its open position(s) for each card in hand
        CardInterface c = hand.get(i);
        int tempScore = 0;
        for (Positions pos : openPoss) {
          tempScore = tempScore + c.getValueFromPos(pos).toInt();
        }
        if (tempScore > score) {
          score = tempScore;
          possibleMoves.clear();
          possibleMoves.add(new GameMoveData(p.x, p.y, i));
        } else if (tempScore == score) {
          possibleMoves.add(new GameMoveData(p.x, p.y, i));
        }
      }
    }
    bestMoveValue = score;
    // Return all best possible moves
    return possibleMoves;
  }

  private List<Point> getPointsWithSmallestListSize(Map<Point, List<Positions>> map) {
    List<Point> result = new ArrayList<>();
    int minSize = Integer.MAX_VALUE;

    for (Map.Entry<Point, List<Positions>> entry : map.entrySet()) {
      Point point = entry.getKey();
      List<Positions> positions = entry.getValue();
      int size = positions.size();
      if (size < minSize) {
        minSize = size;
        result.clear();
        result.add(point);
      } else if (size == minSize) {
        result.add(point);
      }
    }
    return result;
  }

  private Point getAdjacentPointInDirection(Point basePoint, Positions direction) {
    int row = basePoint.x;
    int col = basePoint.y;
    switch (direction) {
      case NORTH:
        row = row - 1;
        break;
      case EAST:
        col = col + 1;
        break;
      case SOUTH:
        row = row + 1;
        break;
      case WEST:
        col = col - 1;
        break;
      default:
        throw new IllegalArgumentException("Invalid position");
    }
    return new Point(row, col);
  }

  private List<Positions> getOpenSidesOfCell(Cell[][] grid, Point p,
                                                  List<Positions> openPoss) {
    // For a position on the grid at point p,
    // Check if the cell one further in the direction of pos1 or pos2 is a hole card cell
    // If yes, increment by one, if no, continue
    List<Positions> positions = new ArrayList<>();

    for (Positions pos : openPoss) {
      Point adjP = getAdjacentPointInDirection(p, pos);
      Cell adjCell = grid[adjP.x][adjP.y];
      if (adjCell instanceof CardCell) {
        positions.add(pos);
      }
    }
    return positions;
  }

  private List<Point> getCornerCells() {
    List<Point> cellPoints = new ArrayList<>();
    int[] dims = this.model.getGridSize();
    int lastr = dims[0] - 1;
    int lastc = dims[1] - 1;
    // Get cell at 0, 0), 0, last), last, 0), last, last)
    // Creates points in (c, r) fashion i.e. x = c, y = r
    cellPoints.add(createPoint(0, 0)); // TL
    cellPoints.add(createPoint(0, lastc)); // TR
    cellPoints.add(createPoint(lastr, 0)); // BL
    cellPoints.add(createPoint(lastr, lastc)); // BR


    return cellPoints;
  }

  private Point createPoint(int r, int c) {
    return new Point(r, c);
  }
}
