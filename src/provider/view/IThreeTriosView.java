package provider.view;

/**
 * Interface for any view of ThreeTrios. Only contains a render method to display the current model
 * state.
 */
public interface IThreeTriosView {

  /**
   * Appends current game state to the output.
   */
  void render();

}
