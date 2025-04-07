package provider.controller.player;

import controller.ThreeTriosController;
import provider.model.cell.Player;

/**
 * Represents a ThreeTriosPlayer to play the game with.
 */
public interface ITTPlayer {

  /**
   * Method telling the player it is their turn and to make a move.
   */
  void yourTurn();

  /**
   * Adds a FeatureListener to the player so it can be notified when to play.
   * @param controller TTController
   */
  void addFeatureListener(ThreeTriosController controller);

  /**
   * Returns the Player color of this Player.
   * @return Player red or blue
   */
  Player getColor();
}
