package model.adapter;

import java.util.Objects;

import model.card.CardInterface;
import model.card.Positions;
import provider.model.card.ICard;
import provider.model.card.Value;

/**
 * Adapter used to convert our card interface to the providers' card interface.
 */
public class CardInterfaceToICard implements ICard {
  private CardInterface card;

  /**
   * Constructs the card interface adapter.
   * @param card card to be adapted
   */
  public CardInterfaceToICard(CardInterface card) {
    this.card = Objects.requireNonNull(card);
  }

  @Override
  public String getName() {
    return card.getName();
  }

  @Override
  public Value getNorth() {
    return convertValue(card.getValueFromPos(Positions.NORTH));
  }

  @Override
  public Value getSouth() {
    return convertValue(card.getValueFromPos(Positions.SOUTH));
  }

  @Override
  public Value getEast() {
    return convertValue(card.getValueFromPos(Positions.EAST));
  }

  @Override
  public Value getWest() {
    return convertValue(card.getValueFromPos(Positions.WEST));
  }

  @Override
  public boolean compareNorth(ICard card) {
    return this.card.determineWin(new ICardToCardInterface(card), Positions.NORTH);
  }

  @Override
  public boolean compareSouth(ICard card) {
    return this.card.determineWin(new ICardToCardInterface(card), Positions.SOUTH);
  }

  @Override
  public boolean compareEast(ICard card) {
    return this.card.determineWin(new ICardToCardInterface(card), Positions.EAST);
  }

  @Override
  public boolean compareWest(ICard card) {
    return this.card.determineWin(new ICardToCardInterface(card), Positions.WEST);
  }

  private Value convertValue(model.card.Value pValue) {
    if (pValue == null) {
      throw new IllegalArgumentException("Unexpected value: null");
    }
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
