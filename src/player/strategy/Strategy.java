package player.strategy;

import java.util.List;

import player.GameMoveData;

/**
 * Represents a strategy that a player can use to make moves in the game.
 */
public interface Strategy {
  /**
   * Executes strategy and recommends game move to be made.
   * @return the recommended game move (or tied best moves) according to the strategy
   */
  List<GameMoveData> execute();

  /**
   * Returns the bestMoveValue of the strategy.
   * @return value representing how great the bestMove the strategy would make is
   */
  int getBestMoveValue();
}
