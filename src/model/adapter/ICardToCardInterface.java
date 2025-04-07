package model.adapter;

import java.util.Objects;

import model.card.CardInterface;
import model.card.Colors;
import model.card.Positions;
import model.card.Value;
import provider.model.card.ICard;

/**
 * Adapter used to convert providers' card interface to our card interface.
 */
public class ICardToCardInterface implements CardInterface {
  ICard pcard;

  /**
   * Constructs the card interface adapter.
   * @param card card to be adapted
   */
  public ICardToCardInterface(ICard card) {
    this.pcard = Objects.requireNonNull(card);
  }

  @Override
  public boolean determineWin(CardInterface adjacentCard, Positions relativePosition) {
    ICard convertedCard = new CardInterfaceToICard(adjacentCard);
    switch (relativePosition) {
      case NORTH:
        return pcard.compareNorth(convertedCard);
      case EAST:
        return pcard.compareEast(convertedCard);
      case WEST:
        return pcard.compareWest(convertedCard);
      case SOUTH:
        return pcard.compareSouth(convertedCard);
      default:
        throw new IllegalArgumentException("Relative position invalid.");
    }
  }

  @Override
  public void setColor(Colors color) {
    // Left empty because impossible to implement.
    // ICard does not have any color altering methods.
  }

  @Override
  public Colors getColor() {
    // Returns null because also impossible to implement. ICard does not have any color getting.
    return null;
  }

  @Override
  public String getName() {
    return pcard.getName();
  }

  @Override
  public Value getValueFromPos(Positions pos) {
    switch (pos) {
      case NORTH:
        return convertValue(pcard.getNorth());
      case EAST:
        return convertValue(pcard.getEast());
      case WEST:
        return convertValue(pcard.getWest());
      case SOUTH:
        return convertValue(pcard.getSouth());
      default:
        throw new IllegalArgumentException("Relative position invalid.");
    }
  }

  @Override
  public void switchColor() {
    // Also impossible to implement so left blank again.
    // ICard does not have any methods having to do anything with its color.
  }

  private Value convertValue(provider.model.card.Value pValue) {
    switch (pValue) {
      case ONE:
        return Value.ONE;
      case TWO:
        return Value.TWO;
      case THREE:
        return Value.THREE;
      case FOUR:
        return Value.FOUR;
      case FIVE:
        return Value.FIVE;
      case SIX:
        return Value.SIX;
      case SEVEN:
        return Value.SEVEN;
      case EIGHT:
        return Value.EIGHT;
      case NINE:
        return Value.NINE;
      case TEN:
        return Value.TEN;
      default:
        throw new IllegalArgumentException("Unexpected value: " + pValue);
    }
  }
}
