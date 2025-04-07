package player;

import java.util.ArrayList;

import model.ReadOnlyThreeTriosModel;
import java.util.List;

import model.card.Card;
import model.card.CardInterface;
import model.card.Colors;
import model.card.Positions;

/**
 * Abstract player class implementing the PlayerInterface that defines some required methods
 * that do not vary.
 */
public abstract class AbstractPlayer implements Player {

  protected List<CardInterface> hand;
  protected ReadOnlyThreeTriosModel model;
  protected final Colors color;

  /**
   * Constructor which simply initializes an empty hand.
   */
  public AbstractPlayer(ReadOnlyThreeTriosModel model, Colors color) {
    hand = new ArrayList<>();
    this.model = model;
    this.color = color;
  }

  public Colors getColor() {
    return color;
  }

  protected void checkIfLegal() {
    if (model.isGameOver() || !model.isGameStarted()) {
      throw new IllegalStateException("Cannot get next placement: game is over or hasn't started.");
    }

    Colors currentColor = this.model.getPlayerInTurn().getColor();

    Colors ourColor = getColor();
    if (currentColor != ourColor) {
      throw new IllegalStateException("Can not get next placement because is not players turn!");
    }
  }

  @Override
  public List<CardInterface> getHandCopy() {
    List<CardInterface> copyHand = new ArrayList<>();

    for (CardInterface oldCard : this.hand) {
      Card copyCard = new Card(oldCard.getName(), oldCard.getValueFromPos(Positions.NORTH),
              oldCard.getValueFromPos(Positions.SOUTH),
              oldCard.getValueFromPos(Positions.EAST),
              oldCard.getValueFromPos(Positions.WEST));
      copyCard.setColor(oldCard.getColor());

      copyHand.add(copyCard);
    }

    return copyHand;
  }


  @Override
  public void appendToHand(CardInterface card) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null.");
    }

    this.hand.add(card);
  }


  @Override
  public CardInterface pop(int idx) {
    CardInterface card = hand.get(idx);
    hand.remove(idx);
    return card;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    AbstractPlayer other = (AbstractPlayer) obj;

    if (hand == null) {
      if (other.hand != null) {
        return false;
      }
    } else if (!hand.equals(other.hand)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((hand == null) ? 0 : hand.hashCode());
    // Optionally include model in hashCode if included in equals
    return result;
  }

}