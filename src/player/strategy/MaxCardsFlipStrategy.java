package player.strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.ReadOnlyThreeTriosModel;
import player.GameMoveData;

/**
 * Represents the functionality for executing the MaxCardsFlipStrategy.
 */
public class MaxCardsFlipStrategy extends AbstractStrategy {
  /**
   * Constructor for initializing a MaxCardsFlipStrategy with a ReadOnly Model.
   *
   * @param model ReadOnly Model to be read from
   */
  public MaxCardsFlipStrategy(ReadOnlyThreeTriosModel model) {
    super(model);
  }

  @Override
  public List<GameMoveData> execute() {
    // Iterate through all cards in hand
    // For each card, calculate its potential score in all battle-phase afflicting cells
    // return maximum score

    int handSize = model.getPlayerInTurn().getHandCopy().size();
    int maxScore = 0;
    List<Point> emptyCellPoss = getEmptyCardCellsPositions(); // Empty cell positions
    List<GameMoveData> potentialBestMoves = new ArrayList<>();

    // Check each card in hand against each empty card cell in the grid
    for (int i = 0; i < handSize; i++) {
      for (Point p : emptyCellPoss) {
        GameMoveData potentialMove = new GameMoveData(p.x, p.y, i);
        int pmScore = model.potentialCardsFlipped(potentialMove); // potential move score
        if (pmScore > maxScore) {
          // If pmScore is greater than maxScore, update max value, and update best move list
          maxScore = pmScore;
          potentialBestMoves.clear();
          potentialBestMoves.add(potentialMove);
        } else if (pmScore == maxScore) {
          // If pm score equal, add to list of potential moves
          potentialBestMoves.add(potentialMove);
        }
      }
    }
    bestMoveValue = maxScore;
    return potentialBestMoves;
  }
}
