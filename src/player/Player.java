package player;

import model.card.CardInterface;

/**
 * Player interface that represents all methods needed to be implemented by a version of player.
 * */
public interface Player extends ReadOnlyPlayer {

  /**
   * Appends supplied card to the hand of this player object.
   * @param card card to be appended to hand
   * @throws IllegalArgumentException card is null
   * */
  void appendToHand(CardInterface card);

  /**
   * Pops the card at a given index within the hand.
   * @param idx the index of the card we want to get within the hand.
   * @return the card at the given index within the hand.
   * */
  CardInterface pop(int idx);


}
