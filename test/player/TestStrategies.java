package player;

import org.junit.Assert;
import org.junit.Test;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.card.Colors;
import model.cell.Cell;
import model.ThreeTriosGameModel;
import model.mock.ThreeTriosMock;
import model.ThreeTriosModel;
import player.strategy.CornerStrategy;
import player.strategy.LeastFlippableStrategy;
import player.strategy.MaxCardsFlipStrategy;
import player.strategy.MiniMaxStrategy;
import player.strategy.Strategy;
import view.ThreeTriosGUI;
import view.ThreeTriosGUIView;

/**
 * Tests all strategies within player package.
 */
public class TestStrategies {

  private ThreeTriosModel createNormalModel() {
    return new ThreeTriosGameModel("resources/AllCardsReachableBoardConfig.txt",
            "resources/BigDeckCardConfig.txt", new Random(22));
  }

  private ThreeTriosModel createNoHoleModel() {
    return new ThreeTriosGameModel("resources/NoHolesBoardConfig.txt",
            "resources/BigDeckCardConfig.txt", new Random(22));
  }

  private ThreeTriosModel createBigModel() {
    return new ThreeTriosGameModel("resources/PotentialFourFlipBoardConfig.txt",
            "resources/FlippablesCardConfig.txt", new Random(22));
  }

  private ThreeTriosModel createModel() {
    return new ThreeTriosGameModel("resources/basicBoardConfig.txt",
            "resources/SmallDeckCardConfig.txt", new Random(22));

  }

  private void initModel(Player p1, Player p2, ThreeTriosModel model) {
    model.startGame(p1, p2, true);

  }

  private GameMoveData createMove(int r, int c, int i) {
    return new GameMoveData(r, c, i);
  }

  private void display(ThreeTriosModel model) {
    ThreeTriosGUI view = new ThreeTriosGUIView(model);
    try {
      view.render();
      synchronized (this) {
        this.wait();
      }
    } catch (IOException e) {
      System.out.println("error rendering");
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void displayAll(ThreeTriosModel model) {
    ThreeTriosGUI view = new ThreeTriosGUIView(model);
    try {
      view.render();
      synchronized (this) {
        this.wait(1000);
      }
    } catch (IOException e) {
      System.out.println("error rendering");
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testComputerPlayerGetPlacementFailsOutOfTurn() {
    ThreeTriosModel model = createModel();
    Player blueStrategy = new ComputerPlayer(
            List.of(new MaxCardsFlipStrategy(model)), model, Colors.BLUE);
    Player redStrategy = new ComputerPlayer(
            List.of(new CornerStrategy(model)), model, Colors.RED);
    initModel(redStrategy, blueStrategy, model);

    Assert.assertThrows(IllegalStateException.class, blueStrategy::getNextPlacement);
    model.playMove(createMove(0, 0, 0));
    Assert.assertThrows(IllegalStateException.class, redStrategy::getNextPlacement);
  }

  @Test
  public void testFunctionalMaxStrat() {
    ThreeTriosModel model = createModel();
    Player blueStrategy = new ComputerPlayer(
            List.of(new MaxCardsFlipStrategy(model)), model, Colors.BLUE);
    Player redStrategy = new ComputerPlayer(
            List.of(new MaxCardsFlipStrategy(model)), model, Colors.RED);
    initModel(redStrategy, blueStrategy, model);

    // create board state where there are two possible best moves and ensure chooses uppermost
    model.playMove(createMove(0, 1, 0));
    model.playMove(createMove(1, 0, 1));
    Assert.assertEquals(createMove(0, 0, 0), redStrategy.getNextPlacement());
    model.playMove(redStrategy.getNextPlacement());
    Cell[][] grid = model.getGrid();
    Assert.assertTrue(model.getPlayerScore(true)
            > model.getPlayerScore(false));
    // Assert red score > blue score meaning strategy worked and flipped card


    // Correctly assesses uppermost - leftmost 0th index best move
    // Multiple options to flip same amount of cards but chose best one
  }

  @Test
  public void testGetPlacementFailsOutOfTurnMaxStrat() {
    ThreeTriosModel model = createModel();
    Player blueStrategy = new ComputerPlayer(
            List.of(new MaxCardsFlipStrategy(model)), model, Colors.BLUE);
    Player redStrategy = new ComputerPlayer(
            List.of(new MaxCardsFlipStrategy(model)), model, Colors.RED);
    initModel(redStrategy, blueStrategy, model);

    Assert.assertThrows(IllegalStateException.class, blueStrategy::getNextPlacement);
    model.playMove(createMove(0, 0, 0));
    Assert.assertThrows(IllegalStateException.class, redStrategy::getNextPlacement);
  }

  @Test
  public void testFunctionalCornerStrat() {
    ThreeTriosModel model = createBigModel();
    Player blueStrategy = new ComputerPlayer(
            List.of(new CornerStrategy(model)), model, Colors.BLUE);
    Player redStrategy = new ComputerPlayer(
            List.of(new CornerStrategy(model)), model, Colors.RED);
    initModel(redStrategy, blueStrategy, model);

    // We know the seemingly best corner is the bottom left corner
    // bc its surrounded by 2 holes
    // and in this case we will just choose 0th card
    // so assert equal
    Assert.assertEquals(createMove(3, 0, 0), redStrategy.getNextPlacement());
    model.playMove(redStrategy.getNextPlacement());

    // the next corner move is the bottom right, since it has 1 adjacent hole cell.
    // Meaning it is harder to flip.
    // So, assert blue move is that.
    // Ensure it chooses highest, most close-to-0-index card on that side as well. Which it does.
    Assert.assertEquals(createMove(3, 3, 0), blueStrategy.getNextPlacement());
    model.playMove(blueStrategy.getNextPlacement());

    // The last move should be a corner with 2 adjacent, empty card cells.
    // It should choose to card with the best outward facing values.
    Assert.assertEquals(createMove(0, 3, 0), redStrategy.getNextPlacement());
    model.playMove(redStrategy.getNextPlacement());
  }

  @Test
  public void testCornerChoosesHighestCardValues() {
    ThreeTriosModel model = createModel();
    Player blueStrategy = new ComputerPlayer(
            List.of(new CornerStrategy(model)), model, Colors.BLUE);
    Player redStrategy = new ComputerPlayer(
            List.of(new CornerStrategy(model)), model, Colors.RED);
    initModel(redStrategy, blueStrategy, model);

    Assert.assertThrows(IllegalStateException.class, blueStrategy::getNextPlacement);
    model.playMove(createMove(0, 0, 0));
    Assert.assertThrows(IllegalStateException.class, redStrategy::getNextPlacement);

    // Need to test that the corner strategy chooses the best card to go into any corner
    // And chooses the overall best card
    // i.e. will choose the card that can best defend against adjacent card cells


    Assert.assertEquals(createMove(0, 2, 2), blueStrategy.getNextPlacement());
    model.playMove(blueStrategy.getNextPlacement());


    model.playMove(createMove(1, 2, 0));
    model.playMove(blueStrategy.getNextPlacement());
    // Next corner is checked appropriately as well
    model.playMove(createMove(1, 0, 0));
    model.playMove(blueStrategy.getNextPlacement());
    // And next corner
    model.playMove(createMove(0, 1, 0));
    model.playMove(blueStrategy.getNextPlacement());
    // Chooses best card for last corner (i.e. highest avg value for sides facing out)
  }

  @Test
  public void testCornerStrategyNoValidCorner() {
    ThreeTriosModel model = createNormalModel();
    Player blueStrategy = new ComputerPlayer(
            List.of(new CornerStrategy(model)), model, Colors.BLUE);
    Player redStrategy = new ComputerPlayer(
            List.of(new CornerStrategy(model)), model, Colors.RED);
    initModel(redStrategy, blueStrategy, model);

    model.playMove(redStrategy.getNextPlacement());
    model.playMove(blueStrategy.getNextPlacement());
    // Fill both corners...
    Assert.assertEquals(createMove(1, 0, 0), redStrategy.getNextPlacement());
    // No more corners to play to... so should choose uppermost-leftmost 0th index move
    // Which in this case is move(1, 0, 0).
  }

  @Test
  public void testCornerStrategyWithMock() {
    // Create the mock model (Auto initializes with a 3x3 grid with only empty card cells)
    ThreeTriosMock mockModel = new ThreeTriosMock();

    // Create players with the CornerStrategy
    Player redPlayer = new ComputerPlayer(
            List.of(new CornerStrategy(mockModel)), mockModel, Colors.RED);
    Player bluePlayer = new ComputerPlayer(
            List.of(new CornerStrategy(mockModel)), mockModel, Colors.BLUE);

    // Initialize the mock model with the players
    mockModel.startGame(redPlayer, bluePlayer, false);

    // Assume that the red player is the current player
    // Execute the strategy by getting the next placement
    GameMoveData redMove = redPlayer.getNextPlacement();

    // Print out the move chosen by the red player
    System.out.println("Red player's move: " + redMove);

    // Print out the log entries from the mock model
    List<String> expectedList = new ArrayList<>();
    expectedList.add("getGridCellCard(0, 0) called");
    expectedList.add("getGridCellCard(0, 2) called");
    expectedList.add("getGridCellCard(2, 0) called");
    expectedList.add("getGridCellCard(2, 2) called");
    List<String> actualList = mockModel.getLog();
    for (int i = 0; i < expectedList.size(); i++) {
      String expected = expectedList.get(i);
      String actual = actualList.get(i);
      Assert.assertEquals(expected, actual);
    }
    // If passes, means correctly checked all four corners
  }

  @Test
  public void testCombinedStrategies() {
    ThreeTriosModel model = createBigModel();
    Strategy corner = new CornerStrategy(model);
    Strategy maxFlip = new MaxCardsFlipStrategy(model);
    Player blueStrategy = new ComputerPlayer(
            List.of(corner, maxFlip), model, Colors.BLUE);
    Player redStrategy = new ComputerPlayer(
            List.of(corner, maxFlip), model, Colors.RED);
    initModel(redStrategy, blueStrategy, model);

    // First two moves will be cornerStrategy moves because the first situation
    // has two adjacent hole cells, so its not capturable. This means the next move will also
    // be tied in score for corner move, so it will be same.

    Assert.assertEquals(createMove(3, 0, 0), redStrategy.getNextPlacement());
    model.playMove(redStrategy.getNextPlacement());

    Assert.assertEquals(createMove(3, 3, 0), blueStrategy.getNextPlacement());
    model.playMove(blueStrategy.getNextPlacement());
  }

  @Test
  public void testFunctionalLeastFlippable() {
    ThreeTriosModel model = createNormalModel();
    Player blueStrategy = new ComputerPlayer(
            List.of(new MaxCardsFlipStrategy(model)), model, Colors.BLUE);
    Player redStrategy = new ComputerPlayer(
            List.of(new LeastFlippableStrategy(model)), model, Colors.RED);
    initModel(redStrategy, blueStrategy, model);

    Assert.assertEquals(createMove(2, 1, 0), redStrategy.getNextPlacement());
    // This move is impossible to be taken over. Its only open side is covered by an A value
    // It is closest to 0 so must be chosen.
    model.playMove(redStrategy.getNextPlacement());
    Assert.assertEquals(createMove(0, 0, 0), blueStrategy.getNextPlacement());
    // blueStrategy should just do top left 0 card because the red card places is impossible to
    // flip.
    model.playMove(blueStrategy.getNextPlacement());
    Assert.assertEquals(createMove(2, 3, 0), redStrategy.getNextPlacement());
    // This move is also impossible to capture because its only open facing side is an A.
    // Correctly assesses an out-of-bounds cell to be non-existent so it doesnt have to defend it.
    model.playMove(redStrategy.getNextPlacement());

    Assert.assertEquals(createMove(1, 0, 0), blueStrategy.getNextPlacement());
    // Should again be uppermost-leftmost 0 move because it is not possible to capture any card.
    model.playMove(blueStrategy.getNextPlacement());

    Assert.assertEquals(createMove(1, 2, 2), redStrategy.getNextPlacement());
    // This last placement must also be correct.
    // There are a lot of ties in this consideration, but this move going to the center
    // is flippable by 2 cards. So is playing that same card one to the right, or one to the left.
    // But, it doesnt like being placed next to present cards, so moving left is not an option,
    // while moving right would make it not the uppermost-leftmost, so we keep it there.
    model.playMove(redStrategy.getNextPlacement());
  }

  @Test
  public void fullPlaythroughTestUsingStrategy() {
    ThreeTriosModel model = createNoHoleModel();
    Player blueStrategy = new ComputerPlayer(
            List.of(new LeastFlippableStrategy(model),
                    new MaxCardsFlipStrategy(model),
                    new CornerStrategy(model)), model, Colors.BLUE);
    Player redStrategy = new ComputerPlayer(
            List.of(new LeastFlippableStrategy(model),
                    new MaxCardsFlipStrategy(model),
                    new CornerStrategy(model)), model, Colors.RED);
    initModel(redStrategy, blueStrategy, model);

    while (!model.isGameOver()) {
      try {
        model.playMove(redStrategy.getNextPlacement());
        model.playMove(blueStrategy.getNextPlacement());
      } catch (IllegalStateException ignored) {
      }
    }


    Assert.assertTrue(model.isGameOver());
    Assert.assertTrue(model.getPlayerScore(true)
            > model.getPlayerScore(false));
  }

  @Test
  public void testMiniMaxStratFunctional() {
    ThreeTriosModel model = createModel();
    Strategy redStrat = new MaxCardsFlipStrategy(model);
    Player redStrategy = new ComputerPlayer(
            List.of(redStrat), model, Colors.BLUE);
    Player blueStrategy = new ComputerPlayer(
            List.of(new MiniMaxStrategy(model, redStrat)), model, Colors.RED);
    initModel(redStrategy, blueStrategy, model);

    model.playMove(redStrategy.getNextPlacement());
    Assert.assertEquals(createMove(0,1,0), blueStrategy.getNextPlacement());
    // Is the uppermost, leftmost card and 0th most index. Many ties for this case.
    model.playMove(blueStrategy.getNextPlacement());
    model.playMove(redStrategy.getNextPlacement());
    Assert.assertEquals(createMove(1,0,0), blueStrategy.getNextPlacement());
    // Is also uppermost leftmost 0th card because many options and ties.
    model.playMove(blueStrategy.getNextPlacement());
    model.playMove(redStrategy.getNextPlacement());
    model.playMove(blueStrategy.getNextPlacement());
  }

  @Test
  public void testMaxCardsFlipStrategyWithMock() {
    // Create the mock model (Auto initializes with a 3x3 grid with only empty card cells)
    ThreeTriosMock mockModel = new ThreeTriosMock();

    // Create players with the CornerStrategy
    Player redPlayer = new ComputerPlayer(
            List.of(new MaxCardsFlipStrategy(mockModel)), mockModel, Colors.RED);
    Player bluePlayer = new ComputerPlayer(
            List.of(new MaxCardsFlipStrategy(mockModel)), mockModel, Colors.BLUE);

    // Initialize the mock model with the players
    mockModel.startGame(redPlayer, bluePlayer, false);

    // Assume that the red player is the current player
    // Execute the strategy by getting the next placement (is the step that calls mock methods)
    redPlayer.getNextPlacement();

    List<Point> allPossiblePoints = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        allPossiblePoints.add(new Point(i, j));
      }
    }

    List<String> expectedStrings = new ArrayList<>();
    // Create the list of expected strings, that is just the list of all possible points and all
    // possible cards because the maxCardsFlip strategy is supposed to exhaustively
    // check all possible cells against all possible cards in hand.
    for (int i = 0; i < redPlayer.getHandCopy().size(); i++) {
      for (Point p  : allPossiblePoints) {
        String expectedString = String.format(
                "potentialCardsFlipped(row: %d col: %d index: %d) called", p.x, p.y, i);
        expectedStrings.add(expectedString);
      }
    }

    // Assert that each string in the log is within the expectedStrings
    List<String> actualStrings = mockModel.getLog();
    for (int i = 0; i < expectedStrings.size(); i++) {
      String expected = expectedStrings.get(i);
      String actual = actualStrings.get(i);
      Assert.assertEquals(expected, actual);
    }
    // If passes, that means all possible moves were checked
  }



}
