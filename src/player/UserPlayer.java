package player;

import model.ReadOnlyThreeTriosModel;
import model.card.Colors;

/**
 * Implementation of PlayerInterface that represents a human user playing the game.
 * Uses a scanner object to determine user inputs.
 * Defines the lastly needed methods not previously defined by AbstractPlayer.
 * */
public class UserPlayer extends AbstractPlayer {


  /**
   * Constructor for the UserPlayer class.
   * */
  public UserPlayer(ReadOnlyThreeTriosModel model, Colors clr) {
    super(model, clr);
  }

  @Override
  public GameMoveData getNextPlacement() {
    //TODO Once instructions on how to handle user input are given in the next assignment
    // Well it can be left as empty because we should never have to use this
    return null;
  }
}
