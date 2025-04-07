package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.ReadOnlyThreeTriosModel;
import model.card.Colors;
import player.strategy.Strategy;

/**
 * ComputerPlayer implementation that takes in a list of possible strategies to play with.
 * The way it works is it deduces any point that multiple strategies agree upon, then
 * plays the most agreed point. If there is a tie, tiebreak it like usual.
 * */
public class ComputerPlayer extends AbstractPlayer {
  List<Strategy> strategies;

  /**
   * Constructor which allows for one or more strategies to be used.
   * Multiple strategies are compounded as tiebreaks
   *
   * @param strategy list of strategies that will make up some overall strategy
   * @throws IllegalArgumentException if strategy is null or empty
   */
  public ComputerPlayer(List<Strategy> strategy, ReadOnlyThreeTriosModel model, Colors clr) {
    super(model, clr);

    if (strategy == null || strategy.isEmpty()) {
      throw new IllegalArgumentException("Strategy passed to ComputerPlayer cannot be"
              + " null or empty.");
    }

    this.strategies = strategy;
  }

  /**
   * Setter for the computerPlayer's strategy in case you want to change
   * strategies throughout a game.
   * @param strategies Some combination of strategies to be used in making move decisions
   */
  public void setStrategies(List<Strategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public GameMoveData getNextPlacement() {
    checkIfLegal();
    List<GameMoveData> bestMoves = new ArrayList<>();
    for (Strategy strategy : strategies) {
      bestMoves.addAll(strategy.execute());
    }

    // Populate hashmap of every potential bestMove and tally how many times it was recommended
    HashMap<GameMoveData, Integer> bestMoveTally = new HashMap<>();
    for (GameMoveData bestMove : bestMoves) {
      // If the map already contains the move, increment the tally, otherwise set it to 1
      bestMoveTally.put(bestMove, bestMoveTally.getOrDefault(bestMove, 0) + 1);
    }

    // Get the list of move(s) with the maximum tally
    // i.e. the move(s) that the most strategies thought was an optimal solution
    int maxTally = Integer.MIN_VALUE;
    List<GameMoveData> maxBestMoves = new ArrayList<>();

    // Find max tally
    for (int tally : bestMoveTally.values()) {
      maxTally = Math.max(maxTally, tally);
    }

    // Collect all moves with the maximum tally
    for (HashMap.Entry<GameMoveData, Integer> entry : bestMoveTally.entrySet()) {
      if (entry.getValue() == maxTally) {
        maxBestMoves.add(entry.getKey());
      }
    }

    return tieBreak(maxBestMoves);
  }

  private GameMoveData tieBreak(List<GameMoveData> moves) {
    // Assumes list of moves is never null
    GameMoveData bestMove = moves.get(0);
    int bestRow = bestMove.getGridRow();
    int bestCol = bestMove.getGridCol();
    int bestCardIndex = bestMove.getCardIndex();

    for (GameMoveData move : moves) {
      int moveRow = move.getGridRow();
      int moveCol = move.getGridCol();
      int moveCardIndex = move.getCardIndex();

      if (moveRow < bestRow) {
        // Found a move with a smaller row index (uppermost position)
        bestMove = move;
        bestRow = moveRow;
        bestCol = moveCol;
        bestCardIndex = moveCardIndex;
      } else if (moveRow == bestRow) {
        if (moveCol < bestCol || moveCol == bestCol && moveCardIndex < bestCardIndex) {
          bestMove = move;
          bestRow = moveRow;
          bestCol = moveCol;
          bestCardIndex = moveCardIndex;
        }
      }
    }
    return bestMove;
  }
}
