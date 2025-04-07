import java.util.ArrayList;
import java.util.List;

import controller.ThreeTriosController;
import model.adapter.ProviderViewToRegView;
import model.adapter.ThreeTriosGameAdapter;
import model.ThreeTriosGameModel;
import player.ComputerPlayer;
import player.Player;
import player.UserPlayer;
import player.strategy.CornerStrategy;
import player.strategy.LeastFlippableStrategy;
import player.strategy.MaxCardsFlipStrategy;
import player.strategy.Strategy;
import view.ThreeTriosGUI;
import view.ThreeTriosGUIView;
import model.card.Colors;

/**
 * Executable file that can run this whole program.
 */
public final class ThreeTrios {
  private static Player[] initializePlayers(String[] args, ThreeTriosGameModel model) {
    Colors userColor;
    Colors oppColor;
    switch (args[2].toLowerCase()) {
      case "r":
        userColor = Colors.RED;
        oppColor =  Colors.BLUE;
        break;
      case "b":
        userColor = Colors.BLUE;
        oppColor = Colors.RED;
        break;
      default:
        throw new IllegalArgumentException("Third argument must either be R (for user is red) "
                + "or B (for user is blue). NOT case-sensitive.");
    }
    Player user = new UserPlayer(model, userColor);
    Player opponent;
    switch (args[3].toLowerCase()) {
      case "h":
        opponent = new UserPlayer(model, oppColor);
        break;
      case "c":
        if (args.length < 5) {
          throw new IllegalArgumentException("No strategies supplied for computer opponent.");
        }
        List<Strategy> strats = new ArrayList<>();
        for (int i = 4; i < args.length; i++) {
          switch (args[i]) {
            case "1":
              strats.add(new CornerStrategy(model));
              break;
            case "2":
              strats.add(new LeastFlippableStrategy(model));
              break;
            case "3":
              strats.add(new MaxCardsFlipStrategy(model));
              break;
            default:
              throw new IllegalArgumentException(String.format("Invalid number entered for "
                      + "opponent strategy: %s", args[i]));
          }
        }
        opponent = new ComputerPlayer(strats, model, oppColor);
        break;
      default:
        throw new IllegalArgumentException("Fourth argument must either be H "
                + "(for human opponent) or C (for computer opponent). NOT case-sensitive.");
    }
    return new Player[]{user, opponent};
  }

  /**
   * - First arg expected to be filepath to a grid config file, if invalid path is supplied a
   * built-in default is used.
   * - Second arg expected to be filepath to a card config file, if invalid path is supplied
   * a built-in default is used.
   * - Third arg expected to be "R" for red or "B" for blue, signifies which color the user would
   * like to play as.
   * - Fourth arg expected to be "H" for human opponent or "C" for computer opponent, signifies the
   * type of opponent the user would like to play against.
   * - Any additional args are assumed to be strategies (given as integers, see below) for the
   * opponent to use, given that the second arg was "C".
   *  - Corner Strategy = 1
   *  - Least Flippable Strategy = 2
   *  - Max Cards Flip Strategy = 3
   *
   * @param args the strings used to determine what model to make.
   */
  public static void main(String[] args) {
    if (args.length < 4) {
      throw new IllegalArgumentException("Too few arguments entered.");
    }

    ThreeTriosGameModel model;
    String defaultGridConfigPath = "C:\\Users\\sebmi\\OneDrive\\Documents\\GitHub\\OODhw5\\"
            + "resources\\NoHolesBoardConfig.txt";
    String defaultCardConfigPath = "C:\\Users\\sebmi\\OneDrive\\Documents\\GitHub\\OODhw5\\"
            + "resources\\BigDeckCardConfig.txt";

    // Initialize model
    String usedGridPath;
    String usedDeckPath;
    try {
      model = new ThreeTriosGameModel(args[0], args[1]);
      usedGridPath = args[0];
      usedDeckPath = args[1];
    } catch (IllegalStateException e) {
      model = new ThreeTriosGameModel(defaultGridConfigPath, defaultCardConfigPath);
      usedGridPath = defaultGridConfigPath;
      usedDeckPath = defaultCardConfigPath;
    }

    // Initialize user player and opponent player
    Player[] players = initializePlayers(args, model);
    Player user = players[0];
    Player opponent = players[1];

    // Designate red and blue player
    Player redPlayer;
    Player bluePlayer;
    if (args[2].equalsIgnoreCase("r")) {
      redPlayer = user;
      bluePlayer = opponent;
    } else {
      redPlayer = opponent;
      bluePlayer = user;
    }

    // Adapt provided view
    provider.model.IReadOnlyTTModel provModel = new ThreeTriosGameAdapter(model, redPlayer,
            bluePlayer);
    provider.view.ThreeTriosGUIView provOppView = new provider.view.ThreeTriosGUIView(provModel);
    ThreeTriosGUI oppView = new ProviderViewToRegView(provOppView);

    // Initialize views and controller for each player
    ThreeTriosGUI userView = new ThreeTriosGUIView(model);
    ThreeTriosController userController = new ThreeTriosController(model, user, userView);
    ThreeTriosController oppController = new ThreeTriosController(model, opponent, oppView);

    // Start the game
    model.startGame(redPlayer, bluePlayer, true);
  }
}