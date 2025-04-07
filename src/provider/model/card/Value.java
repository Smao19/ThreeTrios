package provider.model.card;

/**
 * Represents a value.
 */
public enum Value {
  ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10);

  private final int value;

  Value(int value) {
    if (value < 1 || value > 10) {
      throw new IllegalArgumentException("Value must be between 1 and 10 inclusive.");
    }
    this.value = value;
  }

  /**
   * Gets the value.
   * @return The value associated with the Value.
   */
  public int getValue() {
    return value;
  }

  /**
   * Compares this value to another value.
   * @param other the other value to compare to.
   * @return true if this value is strictly larger than the other value.
   */
  public boolean compare(Value other) {
    return this.value > other.getValue();
  }

  @Override
  public String toString() {
    if (value == 10) {
      return "A";
    } else {
      return Integer.toString(value);
    }
  }
}
