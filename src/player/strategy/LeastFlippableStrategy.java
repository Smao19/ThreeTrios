package player.strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.card.CardInterface;
import model.card.Colors;
import model.card.Positions;
import model.cell.CardCell;
import model.cell.HoleCell;
import model.cell.Cell;
import model.ReadOnlyThreeTriosModel;
import player.GameMoveData;

/**
 * Represents the functionality for executing the LeastFlippableStrategy.
 */
public class LeastFlippableStrategy extends AbstractStrategy {

  /**
   * Constructor for initializing a LeastFlippableStrategy with a ReadOnly Model.
   *
   * @param model ReadOnly Model to be read from
   */
  public LeastFlippableStrategy(ReadOnlyThreeTriosModel model) {
    super(model);
  }

  @Override
  public List<GameMoveData> execute() {
    List<Point> candidateCells = getCellsWithMostHoleNeighbors();
    List<CardInterface> opponentHand;
    if (model.getPlayerInTurn().getColor() == Colors.RED) {
      opponentHand = model.getPlayers().get(1).getHandCopy();
    } else {
      opponentHand = model.getPlayers().get(0).getHandCopy();
    }

    Map<GameMoveData, Integer> moveScores = new HashMap<>();
    List<CardInterface> hand = model.getPlayerInTurn().getHandCopy();

    // For each candidate cell and each card, calculate the 'flippable' score
    for (Point cell : candidateCells) {
      List<Positions> openSides = getOpenSidesOfCell(cell);
      if (openSides.isEmpty()) {
        continue; // Skip cells with no open sides
      }

      for (int cardIndex = 0; cardIndex < hand.size(); cardIndex++) {
        CardInterface card = hand.get(cardIndex);
        int flipScore = 0;

        for (Positions side : openSides) {
          flipScore += calculateFlipScoreForSide(card, side, opponentHand);
        }

        GameMoveData move = new GameMoveData(cell.x, cell.y, cardIndex);
        moveScores.put(move, flipScore);
      }
    }

    // Step 3: Select moves with the least 'flippable' score
    int minFlipScore = moveScores.values().stream().
            min(Integer::compareTo).orElse(Integer.MAX_VALUE);
    List<GameMoveData> bestMoves = new ArrayList<>();
    bestMoveValue = Integer.MAX_VALUE - minFlipScore;

    for (Map.Entry<GameMoveData, Integer> entry : moveScores.entrySet()) {
      if (entry.getValue() == minFlipScore) {
        bestMoves.add(entry.getKey());
      }
    }

    return bestMoves.isEmpty() ? List.of(new GameMoveData(candidateCells.get(0).x,
            candidateCells.get(0).y, 0)) : bestMoves;
  }

  private List<Point> getCellsWithMostHoleNeighbors() {
    Map<Point, Integer> cellHoleCounts = new HashMap<>();
    Cell[][] grid = model.getGrid();
    int[] gridSize = model.getGridSize();

    for (Point p : getEmptyCardCellsPositions()) {
      int holeCount = 0;

      for (Positions dir : Positions.values()) {
        Point adj = getAdjacentPoint(p, dir);
        if (!isValidCell(adj, gridSize) || grid[adj.x][adj.y] instanceof HoleCell) {
          holeCount++;
        }
      }

      cellHoleCounts.put(p, holeCount);
    }

    int maxHoleCount = cellHoleCounts.values().stream().max(Integer::compareTo).orElse(0);
    List<Point> bestCells = new ArrayList<>();

    for (Map.Entry<Point, Integer> entry : cellHoleCounts.entrySet()) {
      if (entry.getValue() == maxHoleCount) {
        bestCells.add(entry.getKey());
      }
    }

    return bestCells;
  }

  private List<Positions> getOpenSidesOfCell(Point p) {
    List<Positions> openSides = new ArrayList<>();
    Cell[][] grid = model.getGrid();
    int[] gridSize = model.getGridSize();

    for (Positions dir : Positions.values()) {
      Point adj = getAdjacentPoint(p, dir);
      if (isValidCell(adj, gridSize) && grid[adj.x][adj.y] instanceof CardCell) {
        openSides.add(dir);
      }
    }

    return openSides;
  }

  private int calculateFlipScoreForSide(CardInterface card, Positions side,
                                        List<CardInterface> opponentHand) {
    int flipScore = 0;
    int cardValue = card.getValueFromPos(side).toInt();
    Positions opponentSide = getOppositeSide(side);

    for (CardInterface opponentCard : opponentHand) {
      int opponentValue = opponentCard.getValueFromPos(opponentSide).toInt();
      if (opponentValue > cardValue) {
        flipScore++;
      }
    }

    return flipScore;
  }

  private Positions getOppositeSide(Positions side) {
    switch (side) {
      case NORTH: return Positions.SOUTH;
      case SOUTH: return Positions.NORTH;
      case EAST:  return Positions.WEST;
      case WEST:  return Positions.EAST;
      default: throw new IllegalArgumentException("Invalid side");
    }
  }

  private Point getAdjacentPoint(Point p, Positions dir) {
    switch (dir) {
      case NORTH: return new Point(p.x - 1, p.y);
      case SOUTH: return new Point(p.x + 1, p.y);
      case EAST:  return new Point(p.x, p.y + 1);
      case WEST:  return new Point(p.x, p.y - 1);
      default: throw new IllegalArgumentException("Invalid direction");
    }
  }

  private boolean isValidCell(Point p, int[] gridSize) {
    return p.x >= 0 && p.x < gridSize[0] && p.y >= 0 && p.y < gridSize[1];
  }
}
