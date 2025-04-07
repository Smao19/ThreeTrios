package provider.strategy;

import provider.model.cell.Player;
import provider.model.IReadOnlyTTModel;

/**
 * A class representing a strategy object for an AI Player in ThreeTrios.
 */
public interface TTStrategy {

  /**
   * Chooses the move the strategy will make.
   * @param model model to decide a move for.
   * @param forWhom the player for which to decide a move for
   * @return a Move object representing the move to play.
   */
  Move chooseMove(IReadOnlyTTModel model, Player forWhom);
}
