package model.adapter;

import java.io.IOException;

import controller.ViewFeatures;
import provider.model.cell.Player;
import provider.view.EventFeatures;
import view.ThreeTriosGUI;

/**
 * Adapter used to convert the Providers GUI view to our own GUI view.
 */
public class ProviderViewToRegView implements ThreeTriosGUI {
  provider.view.ThreeTriosGUIView providerView;

  /**
   * Constructor for GUI view adapter.
   * @param oldView provider GUI view to be adapted.
   */
  public ProviderViewToRegView(provider.view.ThreeTriosGUIView oldView) {
    this.providerView = oldView;
  }

  @Override
  public void addFeatures(ViewFeatures f) {
    EventFeatures provFeature = new ViewFeaturesToEventFeatures(f);
    providerView.addEventListener(provFeature);
  }

  private void adaptedSelectCard(int playerIdx, int cardIdx) {
    provider.model.cell.Player playerColor;

    if (playerIdx == 0) {
      playerColor = Player.RED;
    } else {
      playerColor = Player.BLUE;
    }

    providerView.selectCard(cardIdx, playerColor);
  }

  @Override
  public void highlightCardPanel(int playerIndex, int cardIndex) {
    adaptedSelectCard(playerIndex, cardIndex);
  }

  @Override
  public void unhighlightCardPanel(int playerIndex, int cardIndex) {
    adaptedSelectCard(playerIndex, -1);
  }

  @Override
  public void showMessage(String message) {
    providerView.error(message);
  }

  @Override
  public void render() throws IOException {
    providerView.makeVisible();
  }
}
