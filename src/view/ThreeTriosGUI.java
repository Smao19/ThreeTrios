package view;

import controller.ViewFeatures;

/**
 * Interface representing all required methods within ThreeTriosGUIView.
 * */
public interface ThreeTriosGUI extends ThreeTriosView {

  /**
   * Connects the View with the Controller by providing a Features implementation.
   * @param f The Features implementation to handle user actions.
   */
  void addFeatures(ViewFeatures f);

  /**
   * Highlight a specified card panel.
   * @param playerIndex the index of the player we want to highlight a card from (0 or 1)
   * @param cardIndex the index of the card we want to highlight in the players hand
   * */
  void highlightCardPanel(int playerIndex, int cardIndex);

  /**
   * Un highlight a specified card panel.
   * @param playerIndex the index of the player we want to un highlight a card from (0 or 1)
   * @param cardIndex the index of the card we want to un highlight in the players hand
   */
  void unhighlightCardPanel(int playerIndex, int cardIndex);

  /**
   * Displays inputted message to user.
   * @param message the message wanting to be displayed.
   */
  void showMessage(String message);

}
