package player;

import java.util.List;

import model.card.CardInterface;
import model.card.Colors;

/**
 * Public interface representing the methods seen inside a read only version of a player class.
 * */
public interface ReadOnlyPlayer {
  
  /**
   * Gets the next placement request from the version of player. Will be useful in the controller.
   * @return game move data which can be used to make the desired play.
   * @throws IllegalStateException if it fails to get the next input.
   * */
  GameMoveData getNextPlacement();

  /**
   * Gets a deep copy of the whole hand within the player implementation. Used for view purposes.
   * @return a copy of the player's hand, which can be modified without consequence
   * */
  List<CardInterface> getHandCopy();

  /**
   * Gets the color of player.
   * @return Colors object
   * */
  Colors getColor();
}
