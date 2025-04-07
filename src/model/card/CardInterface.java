package model.card;

/**
 * Interface representing all functionality required by a Card.
 * */
public interface CardInterface {

  /**
   * Determines if this card is winning compared to an inputted card that is assumed adjacent.
   * If it returns true, that means this card won and the adjacent cards color switched.
   * If it returns false, that means this card lost and the adjacent cards color didn't switch.
   * @param adjacentCard the card we are comparing ourselves to
   * @param relativePosition the relative position of the adjacent card
   * @return a boolean that signifies whether this card won against the other card.
   * */
  boolean determineWin(CardInterface adjacentCard, Positions relativePosition);

  /**
   * Sets the color field of card.
   * @param color the color value to be set.
   * @throws IllegalArgumentException if desired color is null
   */
  void setColor(Colors color);

  /**
   * Gets color value of card.
   * @return color value of card
   */
  Colors getColor();

  /**
   * Gets name of card.
   * */
  String getName();

  /**
   * Gets the value of the card at the desired position.
   * @param pos the desired position of the value of the card we're trying to get.
   * */
  Value getValueFromPos(Positions pos);

  /**
   * Switches current color of card to be inverse.
   * */
  void switchColor();
}
