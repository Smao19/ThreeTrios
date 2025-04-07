package view;

import java.io.IOException;

/**
 * Behaviors needed for a view of the ThreeTrios implementation
 * that transmits information to the user.
 */
public interface ThreeTriosView {
  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   * @throws IOException if the rendering fails for some reason
   */
  void render() throws IOException;
}
