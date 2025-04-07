package model.cell;

import java.util.Optional;

import model.card.CardInterface;

/**
 * Interface for a cell which can hold a card.
 */
public interface CardCellInterface extends Cell {

  /**
   * Gets deep copy of the card within the cell. May be empty
   * @return Optional deep copy of the card held in the cell
   * */
  Optional<CardInterface> getCard();

  /**
   * Gets the actual card object held within this cell. May be empty
   * @return Optional card.
   */
  Optional<CardInterface> getActualCard();

  /**
   * Sets the card of this cell to hold the inputted card.
   * @param card the card we want to set the cell to hold
   * @throws IllegalArgumentException if inputted card is null
   * @throws IllegalStateException if cell already holds a card
   * */
  void setCard(CardInterface card);
}
