package provider.model.cell;

/**
 * Represents a player in the game.
 */
public enum Player {
  RED, BLUE;

  @Override
  public String toString() {
    if (this == RED) {
      return "RED";
    } else {
      return "BLUE";
    }
  }
}
