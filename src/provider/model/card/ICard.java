package provider.model.card;

/**
 * Interface for a card in the Three Trios game. Has a value in cardinal directions and a name.
 * Used to compare to other cards.
 */
public interface ICard {
  /**
   * Gets the name.
   * @return The name the card belongs to.
   */
  String getName();

  /**
   * Gets the north value.
   * @return The north value.
   */
  Value getNorth();

  /**
   * Gets the south value.
   * @return The south value.
   */
  Value getSouth();

  /**
   * Gets the east value.
   * @return The east value.
   */
  Value getEast();

  /**
   * Gets the west value.
   * @return The west value.
   */
  Value getWest();

  /**
   * A readable text form of this cards attributes.
   * @return string in {name} {N} {S} {E} {W} format
   */
  String toString();

  /**
   * Is this card equal to other.
   * @param o Other object
   * @return true o is the same as this card depending on if the name and four values are the same.
   */
  boolean equals(Object o);

  /**
   * Gets the hashcode.
   * @return a hashcode based on the name, and the four values.
   */
  int hashCode();


  /**
   * Compares this card to another assuming the other is above this one.
   * @param card the above card to compare to.
   * @return true if this card beats the other card, else false.
   */
  boolean compareNorth(ICard card);

  /**
   * Compares this card to another assuming the other is below this one.
   * @param card the below card to compare to.
   * @return true if this card beats the other card, else false.
   */
  boolean compareSouth(ICard card);

  /**
   * Compares this card to another assuming the other is right of this one.
   * @param card the right card to compare to.
   * @return true if this card beats the other card, else false.
   */
  boolean compareEast(ICard card);

  /**
   * Compares this card to another assuming the other is left of this one.
   * @param card the left card to compare to.
   * @return true if this card beats the other card, else false.
   */
  boolean compareWest(ICard card);

}
