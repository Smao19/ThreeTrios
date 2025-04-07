package model.adapter;

import model.ReadOnlyThreeTriosModel;
import model.card.Colors;
import provider.model.cell.Player;

/**
 * Adapter used to convert the providers' model features to our model features.
 */
public class ProviderMFToRegMF implements controller.ModelFeatures {
  private final provider.model.ModelFeatures ftrs;
  private final ReadOnlyThreeTriosModel model;

  /**
   * Constructs the features adapter.
   * @param features features to be adapted
   */
  public ProviderMFToRegMF(provider.model.ModelFeatures features, ReadOnlyThreeTriosModel model) {
    this.ftrs = features;
    this.model = model;
  }

  @Override
  public void playerTurn(int playerIndex) {
    if (playerIndex == 0) {
      ftrs.yourTurn(Player.RED);
    } else {
      ftrs.yourTurn(Player.BLUE);
    }
  }

  @Override
  public void gameEnd() {
    // Get winner
    provider.model.cell.Player winnerColor;
    boolean isRed;
    player.Player[] winners = model.getWinner();
    if (winners[0].getColor() == Colors.RED) {
      winnerColor = Player.RED;
      isRed = true;
    } else {
      winnerColor = Player.BLUE;
      isRed = false;
    }

    // Get number of cells winner owns
    int winningScore = model.getPlayerScore(isRed);

    ftrs.gameOver(winnerColor, winningScore);
  }
}
