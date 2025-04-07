package model;

import controller.ModelFeatures;
import player.GameMoveData;
import player.Player;

/**
 * Interface for encapsulating all the necessary methods any ThreeTriosModel would need.
 */
public interface ThreeTriosModel extends ReadOnlyThreeTriosModel {
  /**
   * Starts the game with the supplied players and assigns cards from the
   * deck appropriately to both players.
   *
   * @param redPlayer the player which will be treated as Red
   * @param bluePlayer the player which will be treated as Blue
   * @param shuffle whether the deck should be shuffled before dealing or not
   * @throws IllegalArgumentException if either of the supplied players are null
   * @throws IllegalArgumentException if there aren't enough cards in the deck to
   *                                  play the grid to completion
   * @throws IllegalStateException if the game is over or has already started
   */
  void startGame(Player redPlayer, Player bluePlayer, boolean shuffle);

  /**
   * Plays desired card from player in turns hand to desired location on grid.
   *
   * @param move the data for the desired move to be made
   * @throws IllegalArgumentException if desired play is considered illegal
   */
  void playMove(GameMoveData move);

  /**
   * Add features to the model for interacting with controllers.
   * @param features An individual controller performing the desired action(s) upon trigger.
   */
  void addFeatures(ModelFeatures features);
}
