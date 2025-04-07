package view;

import java.io.IOException;
import java.util.List;

import model.card.CardInterface;
import model.card.Positions;
import model.cell.Cell;
import model.card.Colors;
import model.ThreeTriosModel;
import player.ReadOnlyPlayer;

/**
 * Represents a textual view of a ThreeTrios game. Useful for development and
 * playing in the terminal.
 */
public class ThreeTriosTextualView implements ThreeTriosView {
  private final ThreeTriosModel model;
  private final Appendable appendableOutput;

  /**
   * Constructor for supplying model to have view made as well as an Appendable.
   * @param model the model which will be textually represented by view
   * @param appendable to be used for appending/rendering output
   */
  public ThreeTriosTextualView(ThreeTriosModel model, Appendable appendable) {
    this.model = model;
    this.appendableOutput = appendable;
  }

  private String gridToString(Cell[][] grid) {
    StringBuilder output = new StringBuilder();

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        output.append(grid[i][j].toString());
      }
      if ((i + 1) < grid.length) {
        output.append("\n");
      }
    }

    return output.toString();
  }

  private String handToString(List<CardInterface> hand) {
    StringBuilder handView = new StringBuilder();

    for (int i = 0; i < hand.size(); i++) {
      CardInterface card = hand.get(i);

      handView.append(card.getName()).append(" ");
      handView.append(card.getValueFromPos(Positions.NORTH)).append(" ");
      handView.append(card.getValueFromPos(Positions.SOUTH)).append(" ");
      handView.append(card.getValueFromPos(Positions.EAST)).append(" ");
      handView.append(card.getValueFromPos(Positions.WEST)).append(" ");

      if (i + 1 < hand.size()) {
        handView.append("\n");
      }
    }

    return handView.toString();
  }

  private String buildPlayerView(ReadOnlyPlayer player) {
    Colors playerColor = player.getHandCopy().get(0).getColor();

    return String.format("Player: %s\n", playerColor) + gridToString(model.getGrid()) + "\n"
            + "Hand:\n" + handToString(player.getHandCopy());
  }

  @Override
  public void render() throws IOException {
    try {
      appendableOutput.append(buildPlayerView(model.getPlayerInTurn()));
    } catch (Exception ioException) {
      throw new IOException("Rendering failed.");
    }
  }
}
