package model.adapter;

import java.util.Objects;
import java.util.Optional;

import model.card.CardInterface;
import model.card.Colors;
import model.cell.CardCell;
import model.cell.HoleCell;
import provider.model.card.ICard;
import provider.model.cell.CellType;
import provider.model.cell.Player;
import model.cell.Cell;

/**
 * Adapter used to convert our cell to the providers' cell.
 */
public class AdaptCellToProviderCell implements provider.model.cell.Cell {
  private Cell ourCell;

  /**
   * Constructs the cell adapter.
   * @param ourCell cell to be adapted
   */
  public AdaptCellToProviderCell(Cell ourCell) {
    this.ourCell = Objects.requireNonNull(ourCell);
  }

  @Override
  public boolean canPlayCard() {
    if (ourCell instanceof HoleCell) {
      return false;
    } else {
      CardCell ccell = (CardCell) ourCell;
      return ccell.getCard().isEmpty();
    }
  }

  @Override
  public void addCard(ICard card) {
    if (ourCell instanceof HoleCell) {
      throw new IllegalArgumentException("Can not add card to hole cell!");
    } else {
      CardCell ccell = (CardCell) ourCell;
      ccell.setCard(new ICardToCardInterface(card));
    }
  }

  @Override
  public ICard getCard() {
    if (ourCell instanceof HoleCell) {
      throw new IllegalArgumentException("Cant get card from hole cell");
    } else {
      CardCell ccell = (CardCell) ourCell;
      Optional<CardInterface> card = ccell.getCard();
      if (card.isEmpty()) {
        throw new IllegalStateException("Can not get card from empty card cell!");
      } else {
        return new CardInterfaceToICard(card.get());
      }
    }
  }

  @Override
  public void setPlayer(Player player) {
    // While this implementation is a little janky, it works in part.
    // Each cell our provider creates is assigned a player at some point.
    // I assume this also means that whenever they tally points, they tally the
    // count of each cell belonging to a player.

    // This means that it is possible for a cell to be empty and still score points.
    // Or for a cell to be occupied by the red player, while it will score to blue.
    // It is a flaw.
    if (ourCell instanceof HoleCell) {
      throw new IllegalStateException("Can't set player of hole cell!");
    }
    CardCell ccell = (CardCell) ourCell;


    Colors clr = (player == Player.RED) ? Colors.RED : Colors.BLUE;

    Optional<CardInterface> card = ccell.getCard();
    if (card.isPresent()) {
      CardInterface c = card.get();
      c.setColor(clr);
    } else {
      throw new IllegalStateException("In this adaptation, this card cell MUST hold a card"
              + "in order to be assigned a player! So, can't set player because cell is empty!");
    }
    // This is where the implementation breaks!
    // If there isn't a card already in the card cell, we will not be able to set its color.
    // i.e. we can not set CELLS as assigned to a player.
  }


  @Override
  public Player getPlayer() {
    if (ourCell instanceof HoleCell) {
      throw new IllegalStateException("Can not get player of hole cell!");
    }
    CardCell ccell = (CardCell) ourCell;
    if (ccell.getCard().isEmpty()) {
      throw new IllegalStateException("Can't get player because cell is empty!");
    }
    CardInterface card = ccell.getCard().get();
    return (card.getColor() == Colors.RED) ? Player.RED : Player.BLUE;
  }

  @Override
  public CellType getType() {
    if (ourCell instanceof HoleCell) {
      return CellType.HOLE;
    } else {
      return CellType.CARD;
    }
  }

  @Override
  public String toString() {
    return ourCell.toString();
  }

  @Override
  public provider.model.cell.Cell cloneCell() {
    return new AdaptCellToProviderCell(ourCell);
  }

}
