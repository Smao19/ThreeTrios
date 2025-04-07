package model.adapter;

import controller.ViewFeatures;
import provider.model.cell.Player;
import provider.view.EventFeatures;

/**
 * Adapter for converting a ViewFeatures object to the provided EventFeatures.
 */
public class ViewFeaturesToEventFeatures implements EventFeatures {
  ViewFeatures f;

  /**
   * Constructor for adapting ViewFeature to EventFeature.
   * @param f ViewFeature to be adapted
   */
  public ViewFeaturesToEventFeatures(ViewFeatures f) {
    this.f = f;
  }

  @Override
  public void playToGrid(int row, int col) {
    f.selectGridCell(row, col);
  }

  @Override
  public void selectCard(int handInd, Player player) {
    int playerIdx;

    if (player == Player.RED) {
      playerIdx = 0;
    } else {
      playerIdx = 1;
    }

    f.selectCard(playerIdx, handInd);
  }
}
