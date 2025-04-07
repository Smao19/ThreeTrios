package provider.view;

import provider.model.cell.Player;

/**
 * PROVIDER DIDN'T INCLUDE JAVADOC, ADDED THIS FOR STYLING.
 */
public interface IThreeTriosGUIView extends IThreeTriosView {

  /**
   * Adds a listener to the event listeners.
   * @param listener the listener to listen for.
   */
  void addEventListener(EventFeatures listener);

  /**
   * Displays the view to the screen and sets to default size.
   */
  void makeVisible();

  /**
   * Selects a card given a hand index and player of whom to select.
   * If the given player doesn't match up with the controller's player, controller does nothing.
   * @param handInd the given hand index.
   * @param forWhom the player whose hand index to select.
   */
  void selectCard(int handInd, Player forWhom);

  /**
   * Displays an error popup window.
   * @param message message to be put in the window.
   */
  void error(String message);


}
