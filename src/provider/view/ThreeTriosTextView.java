package provider.view;

import java.io.IOException;

import provider.model.cell.Cell;
import provider.model.IThreeTriosModel;
import provider.model.card.ICard;

/**
 * Represents a view of the game using the console. This implementation is entirely text based.
 */
public class ThreeTriosTextView implements IThreeTriosView {
  private final Appendable output;
  private final IThreeTriosModel model;

  /**
   * Constructor.
   * @param output the appendable.
   * @param model the model.
   */
  public ThreeTriosTextView(Appendable output, IThreeTriosModel model) {
    if (output == null) {
      throw new IllegalArgumentException("Appendable cannot be null");
    } else if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.output = output;
    this.model = model;
  }

  @Override
  public void render() {
    drawPlayerTurn();
    drawBoard();
    drawHand();
  }

  /**
   * Writes a message to the output and adds a newline.
   * @param message message to be transmitted
   */
  private void transmit(String message) {
    try {
      output.append(message + '\n');
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  /**
   * Transmits a formatted string describing the current player's turn.
   */
  private void drawPlayerTurn() {
    transmit("Player: " + model.getCurrentTurn().toString());
  }

  /**
   * Transmit the current board state to the output.
   */
  private void drawBoard() {
    Cell[][] grid = model.getGrid();
    for (Cell[] row : grid) {
      StringBuilder line = new StringBuilder();
      for (Cell cell : row) {
        line.append(cell.toString());
      }
      transmit(line.toString());
    }
  }

  /**
   * Transmit the current player's hand to the output.
   */
  private void drawHand() {
    transmit("Hand:");
    for (ICard c : model.getPlayerHand(model.getCurrentTurn())) {
      transmit(c.toString());
    }
  }
}
