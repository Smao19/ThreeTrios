package model.card;

import java.util.Objects;

/**
 * Card class representing an individual card that may be in a hand or a cell.
 */
public class Card implements CardInterface {

  private final String name;
  private final Value north;
  private final Value south;
  private final Value east;
  private final Value west;
  private Colors color;

  /**
   * Card constructor taking in all values needed to create a card.
   *
   * @param nm the name of the card
   * @param n  the northern value of the card
   * @param s  the southern value of the card
   * @param e  the eastern value of the card
   * @param w  the western value of the card
   * @throws IllegalArgumentException if the name is empty or has more than one word
   * @throws NullPointerException     if any of the inputted objects are null
   */
  public Card(String nm, Value n, Value s, Value e, Value w) {
    if (nm.isEmpty()) {
      throw new IllegalArgumentException("Name can not be empty");
    } else if (nm.split(" ").length > 1) {
      throw new IllegalArgumentException("Name can not have more than one word");
    }
    name = Objects.requireNonNull(nm);
    north = Objects.requireNonNull(n);
    south = Objects.requireNonNull(s);
    east = Objects.requireNonNull(e);
    west = Objects.requireNonNull(w);
    this.color = Colors.EMPTY;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Value getValueFromPos(Positions pos) {
    switch (pos) {
      case NORTH:
        return this.north;
      case EAST:
        return this.east;
      case WEST:
        return this.west;
      case SOUTH:
        return this.south;
      default:
        throw new IllegalArgumentException("Invalid position");
    }
  }

  @Override
  public Colors getColor() {
    return this.color;
  }

  @Override
  public void setColor(Colors color) {
    if (color == null) {
      throw new IllegalArgumentException("Cannot set color to null.");
    }
    this.color = color;
  }

  @Override
  public String toString() {
    if (Objects.equals(this.color, Colors.RED)) {
      return "R";
    } else if (Objects.equals(this.color, Colors.BLUE)) {
      return "B";
    } else {
      return "EMPTY";
    }
  }

  @Override
  public void switchColor() {
    if (this.color == Colors.EMPTY) {
      throw new IllegalStateException("Cannot switch a cards color, when cards "
              + "color hasn't been set yet.");
    }
    switch (this.color) {
      case RED:
        this.color = Colors.BLUE;
        break;
      case BLUE:
        this.color = Colors.RED;
        break;
      default:
        throw new IllegalStateException("Cannot switch a cards color, when cards "
                + "color hasn't been set yet.");
    }
  }

  @Override
  public boolean determineWin(CardInterface adjacentCard, Positions relativePosition) {
    if (adjacentCard.getColor() == this.color) {
      throw new IllegalArgumentException("Can not combat card of same color");
    }

    Value thisVal;
    Value rivalVal;

    switch (relativePosition) {
      case NORTH:
        thisVal = this.north;
        rivalVal = adjacentCard.getValueFromPos(Positions.SOUTH);
        break;
      case SOUTH:
        thisVal = this.south;
        rivalVal = adjacentCard.getValueFromPos(Positions.NORTH);
        break;
      case EAST:
        thisVal = this.east;
        rivalVal = adjacentCard.getValueFromPos(Positions.WEST);
        break;
      case WEST:
        thisVal = this.west;
        rivalVal = adjacentCard.getValueFromPos(Positions.EAST);
        break;
      default:
        throw new IllegalArgumentException("Invalid relevantPosition");
    }

    if (thisVal.toInt() > rivalVal.toInt()) {
      adjacentCard.switchColor();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Card other = (Card) obj;
    return Objects.equals(name, other.name)
            && Objects.equals(north, other.north)
            && Objects.equals(south, other.south)
            && Objects.equals(east, other.east)
            && Objects.equals(west, other.west);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, north, south, east, west);
  }

}
