package model;

import java.util.Random;

import model.card.Colors;
import player.Player;
import player.UserPlayer;

/**
 * Concrete class using the abstract class specifically testing the GameModel.
 * */
public class ConcreteThreeTriosModelTest extends AbstractThreeTriosModelTest {

  @Override
  protected ThreeTriosGameModel createModel(String gridConfigFilePath,
                                        String cardConfigFilePath, Random rand) {
    return new ThreeTriosGameModel(gridConfigFilePath, cardConfigFilePath, rand);
  }

  @Override
  protected Player createPlayer(Colors clr) {
    return new UserPlayer(model, clr);
  }
}
