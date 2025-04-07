package model.cell;

import java.util.Objects;
import java.util.Optional;

import model.card.Card;
import model.card.CardInterface;
import model.card.Positions;

/**
 * CardCell class representing a non-hole cell that may or may not hold a card.
 * */
public class CardCell implements CardCellInterface {

  private CardInterface card;

  /**
   * Version of constructor that sets a card to the cell.
   * @param card the card we want to set the cell to hold
   * @throws NullPointerException if the inputted card is null
   * */
  public CardCell(CardInterface card) {
    this.card = Objects.requireNonNull(card);
  }

  /**
   * Version of constructor that does not set a card to the cell, meaning the cell is empty.
   * Sets the card value within the cell to null.
   * */
  public CardCell() {
    this.card = null;
  }


  @Override
  public Optional<CardInterface> getCard() {
    if (card == null) {
      return Optional.empty();
    }

    Card deepCopy = new Card(card.getName(), card.getValueFromPos(Positions.NORTH),
            card.getValueFromPos(Positions.SOUTH),
            card.getValueFromPos(Positions.EAST),
            card.getValueFromPos(Positions.WEST));
    deepCopy.setColor(card.getColor());
    return Optional.of(deepCopy);
  }

  @Override
  public Optional<CardInterface> getActualCard() {
    if (card == null) {
      return Optional.empty();
    }

    return Optional.of(this.card);
  }

  @Override
  public String toString() {
    if (card == null) {
      return "_";
    } else {
      return this.card.toString();
    }
  }

  @Override
  public void setCard(CardInterface card) {
    if (card == null) {
      throw new IllegalArgumentException("Can not set cell card to a null value");
    } else if (this.card != null) {
      throw new IllegalStateException("Can not set card to a cell that already has a card");
    }
    this.card = card;
  }

}
