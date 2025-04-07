package model.card;

/**
 * Enum class representing all possible values a card can hold.
 * Has a valid toString method for displaying.
 * */
public enum Value {
  ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN;

  @Override
  public String toString() {
    return this == TEN ? "A" : String.valueOf(toInt());
    // Returns "A" if the value is ten, otherwise return ordinal + 1 (ordinal is 0 index)
  }

  /**
   * Converts this object to its integer form for comparative operations.
   * */
  public int toInt() {
    return this.ordinal() + 1;
  }
}
