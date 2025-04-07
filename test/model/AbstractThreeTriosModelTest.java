package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.card.CardInterface;
import model.card.Colors;
import model.card.Value;
import model.cell.CardCell;
import model.cell.Cell;
import model.cell.HoleCell;
import player.GameMoveData;
import player.Player;
import view.ThreeTriosGUI;
import view.ThreeTriosGUIView;


/**
 * Abstract class that can test any model implementing ThreeTriosModel.
 **/
public abstract class AbstractThreeTriosModelTest {

  protected ThreeTriosGameModel model;
  protected Player redPlayer;
  protected Player bluePlayer;
  protected Random rand;

  /**
   * Method to create the specific model implementation.
   * To be implemented in the concrete test class.
   */
  protected abstract ThreeTriosGameModel createModel(String gridConfigFilePath,
                                                  String cardConfigFilePath, Random rand);

  /**
   * Method to create a player.
   * To be implemented in the concrete test class.
   */
  protected abstract Player createPlayer(model.card.Colors clr);

  @Before
  public void setUp() {
    rand = new Random(42);
    redPlayer = createPlayer(Colors.RED);
    bluePlayer = createPlayer(Colors.BLUE);
  }

  private String generatePath(String fileName) {
    String basePath = ("resources/");
    return basePath + fileName;
  }

  @Test
  public void testStartGameAlreadyStarted() {
    model = createModel(generatePath("NoHolesBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);
    Assert.assertThrows(IllegalStateException.class, () -> {
      model.startGame(redPlayer, bluePlayer, false);
    });
  }

  @Test
  public void testStartGameNullPlayers() {
    model = createModel(generatePath("NoHolesBoardConfig.txt"),
            generatePath("SmallDeckCardConfig.txt"), rand);
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      model.startGame(null, null, false);
    });
  }

  @Test
  public void testStartGameDeckSizeInsufficient() {
    model = createModel(generatePath("NoHolesBoardConfig.txt"),
            generatePath("SmallDeckCardConfig.txt"), rand);
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      model.startGame(redPlayer, bluePlayer, false);
    });
  }

  @Test
  public void testStartGameSuccess() {
    model = createModel(generatePath("AllCardsReachableBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);
    Assert.assertEquals(redPlayer, model.getPlayerInTurn());
    Assert.assertFalse(redPlayer.getHandCopy().isEmpty());
    Assert.assertFalse(bluePlayer.getHandCopy().isEmpty());
  }

  @Test
  public void testPlayToCellGameNotStarted() {
    model = createModel(generatePath("NoHolesBoardConfig.txt"),
            generatePath("SmallDeckCardConfig.txt"), rand);
    Assert.assertThrows(IllegalStateException.class, () -> {
      model.playMove(new GameMoveData(0, 0, 0));
    });
  }


  @Test
  public void testPlayToCellInvalidHandIndex() {
    model = createModel(generatePath("AllCardsReachableBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      // redPlayer tries to play a card with an invalid hand index
      model.playMove(new GameMoveData(0, 0, -1));
    });
  }

  @Test
  public void testPlayToCellOccupiedCell() {
    model = createModel(generatePath("AllCardsReachableBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);
    model.playMove(new GameMoveData(0, 0, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      // redPlayer tries to play again to the same cell
      model.playMove(new GameMoveData(0, 0, 1));
    });
  }

  @Test
  public void testPlayToCellValidMove() {
    model = createModel(generatePath("AllCardsReachableBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);

    // Assume redPlayer has at least one card
    model.playMove(new GameMoveData(0, 0, 0));

    // Verify that the card is placed on the grid
    model.cell.Cell[][] grid = model.getGrid();
    model.cell.Cell cell = grid[0][0];
    Assert.assertTrue(cell instanceof model.cell.CardCell);
    model.cell.CardCell cardCell = (model.cell.CardCell) cell;

    // Verify that the card on the grid is redPlayer's card
    CardInterface playedCard = new model.card.Card("placeholder", Value.ONE,
            Value.ONE, Value.ONE, Value.ONE);
    if (cardCell.getCard().isPresent()) {
      playedCard = cardCell.getCard().get();
    }
    Assert.assertEquals(Colors.RED, playedCard.getColor());

    // Verify that it's now bluePlayer's turn
    Assert.assertEquals(bluePlayer, model.getPlayerInTurn());
  }


  @Test
  public void testValidBattle() {
    model = createModel(generatePath("AllCardsReachableBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);

    // Red player places a card
    model.playMove(new GameMoveData(1, 1, 0));

    // Blue player places a card adjacent to red player's card
    model.playMove(new GameMoveData(1, 2, 0));

    // Check if the battle resulted in blue player's card switching color
    model.cell.Cell[][] grid = model.getGrid();
    model.cell.CardCell blueCardCell = (model.cell.CardCell) grid[2][1];
    // Depending on the card values, we need to check if the color has switched
    // For this test, we can check if the color is now RED
    if (blueCardCell.getCard().isPresent()) {
      Assert.assertEquals(Colors.RED, blueCardCell.getCard().get().getColor());
    }

  }

  @Test
  public void testIsGameOverGameNotStarted() {
    model = createModel(generatePath("AllCardsReachableBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    Assert.assertThrows(IllegalStateException.class, () -> {
      model.isGameOver();
    });
  }

  @Test
  public void testFullPlaythroughIsGameOver() {
    // By proxy this method also tests that a player does not flip a card if it's already their own.
    model = createModel(generatePath("NoHolesBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);

    // Simulate playing cards to fill the grid
    int handIndex = 0;

    while (!model.isGameOver()) {
      model.cell.Cell[][] grid = model.getGrid();
      Point nextEmptyCell = getNextEmptyCell(grid);
      if (nextEmptyCell == null) {
        break;
      }
      GameMoveData move = new GameMoveData(nextEmptyCell.x, nextEmptyCell.y, handIndex);
      model.playMove(move);

    }

    Assert.assertTrue(model.isGameOver());
  }

  private void display(ReadOnlyThreeTriosModel model) {
    ThreeTriosGUI view = new ThreeTriosGUIView(model);
    try {
      view.render();
      synchronized (this) {
        this.wait();
      }
    } catch (IOException e) {
      System.out.println(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetWinnerGameNotOver() {
    model = createModel(generatePath("AllCardsReachableBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);
    Assert.assertThrows(IllegalStateException.class, () -> {
      model.getWinner();
    });
  }

  @Test
  public void testGetWinner() {
    model = createModel(generatePath("NoHolesBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);

    // Simulate playing moves to fill the grid, ensuring redPlayer has more cards on the grid
    model.cell.Cell[][] grid = model.getGrid();

    // Get the list of empty cells
    List<Point> emptyCells = getAllEmptyCardCells(grid);

    // Simulate moves
    for (Point cell : emptyCells) {
      int handIndex = 0; // Always play the first card in hand

      // Play the card
      model.playMove(new GameMoveData(cell.x, cell.y, handIndex));

    }

    // Now the grid should be full
    Assert.assertTrue(model.isGameOver());

    // Ensure bluePlayer has more cards on the grid
    Assert.assertTrue(model.getPlayerScore(true)
            < model.getPlayerScore(false));

    // Then red player must have won
    Player winner = model.getWinner()[0];
    Assert.assertEquals(bluePlayer, winner);
  }


  @Test
  public void testGetGrid() {
    model = createModel(generatePath("AllCardsReachableBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);
    model.cell.Cell[][] gridCopy = model.getGrid();
    Assert.assertNotNull(gridCopy);

    // Modify the copy and ensure it doesn't affect the model's grid
    gridCopy[0][0] = new model.cell.HoleCell();
    model.cell.Cell[][] originalGrid = model.getGrid();
    Assert.assertFalse(originalGrid[0][0] instanceof HoleCell);
  }

  @Test
  public void testGetPlayerInTurn() {
    model = createModel(generatePath("AllCardsReachableBoardConfig.txt"),
            generatePath("BigDeckCardConfig.txt"), rand);
    model.startGame(redPlayer, bluePlayer, false);
    Assert.assertEquals(redPlayer, model.getPlayerInTurn());

    // Simulate a turn
    model.playMove(new GameMoveData(0, 0, 0));
    Assert.assertEquals(bluePlayer, model.getPlayerInTurn());
  }

  // Helper method to find the next empty card cell in the grid
  private Point getNextEmptyCell(model.cell.Cell[][] grid) {
    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid[0].length; x++) {
        model.cell.Cell cell = grid[y][x];
        if (cell instanceof model.cell.CardCell) {
          model.cell.CardCell cardCell = (model.cell.CardCell) cell;
          if (cardCell.getCard().isEmpty()) {
            return new Point(y, x);
          }
        }
      }
    }
    return null;
  }

  private List<Point> getAllEmptyCardCells(model.cell.Cell[][] grid) {
    List<Point> emptyCells = new ArrayList<>();
    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid[0].length; x++) {
        Cell cell = grid[y][x];
        if (cell instanceof model.cell.CardCell) {
          model.cell.CardCell cardCell = (CardCell) cell;
          if (cardCell.getCard().isEmpty()) {
            emptyCells.add(new Point(y, x));
          }
        }
      }
    }
    return emptyCells;
  }

}
