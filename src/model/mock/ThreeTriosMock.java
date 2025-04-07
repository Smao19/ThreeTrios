package model.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controller.ModelFeatures;
import model.card.Card;
import model.card.CardInterface;
import model.ThreeTriosModel;
import model.card.Value;
import model.cell.CardCell;
import model.cell.Cell;
import player.GameMoveData;
import player.Player;
import player.ReadOnlyPlayer;

/**
 * Mock implementation of ThreeTriosModel. Sole purpose is to log attempts to get card at cell.
 */
public class ThreeTriosMock implements ThreeTriosModel {
  private model.cell.Cell[][] grid;
  private List<String> log = new ArrayList<>();
  private List<ReadOnlyPlayer> players = new ArrayList<>();
  private ReadOnlyPlayer currentPlayer;

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public boolean isGameStarted() {
    return grid != null;
  }

  @Override
  public Player[] getWinner() {
    return new Player[0];
  }

  @Override
  public model.cell.Cell[][] getGrid() {
    return grid;
  }

  @Override
  public ReadOnlyPlayer getPlayerInTurn() {
    return currentPlayer;
  }

  @Override
  public List<ReadOnlyPlayer> getPlayers() {
    return players;
  }

  @Override
  public int[] getGridSize() {
    return new int[]{3, 3};
  }

  @Override
  public CardInterface getGridCellCard(int row, int col) {
    log.add(String.format("getGridCellCard(%d, %d) called", row, col));
    if (grid[row][col] instanceof model.cell.CardCell) {
      model.cell.CardCell ccell = (model.cell.CardCell) grid[row][col];
      if (ccell.getCard().isPresent()) {
        return ccell.getCard().get();
      }
    }
    return null;
  }

  @Override
  public void isLegalPlay(int handIndex, int gridRow, int gridCol) {
    // Does nothing because we don't need the mock to do anything for this.
  }

  @Override
  public Player getOwnerAtCell(int gridRow, int gridCol) {
    return null;
  }

  @Override
  public int potentialCardsFlipped(GameMoveData move) {
    log.add(String.format("potentialCardsFlipped(%s) called", move));
    return 0;
  }

  @Override
  public int getPlayerScore(boolean isRedPlayer) {
    log.add(String.format("getPlayerScore(%b) called", isRedPlayer));
    return 0;
  }

  @Override
  public CardInterface getNewlyPlacedCard() {
    return null;
  }

  @Override
  public ReadOnlyPlayer getRedPlayer() {
    return null;
  }

  @Override
  public ReadOnlyPlayer getBluePlayer() {
    return null;
  }

  @Override
  public void startGame(Player redPlayer, Player bluePlayer, boolean shuffle) {
    grid = new Cell[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid[i][j] = new CardCell();
      }
    }
    players.add(redPlayer);
    players.add(bluePlayer);
    for (int i = 0; i < 6; i++) {
      Card c1 = new Card(String.valueOf(i), chooseRandomValue(), chooseRandomValue(),
              chooseRandomValue(), chooseRandomValue());
      Card c2 = new Card(String.valueOf(-i), chooseRandomValue(), chooseRandomValue(),
              chooseRandomValue(), chooseRandomValue());
      redPlayer.appendToHand(c1);
      bluePlayer.appendToHand(c2);

    }

    currentPlayer = redPlayer;
  }

  private Value chooseRandomValue() {
    Random rand = new Random(22);
    Value[] values = Value.values();
    return values[rand.nextInt(values.length)];
  }

  @Override
  public void playMove(GameMoveData move) {
    // Does nothing because we don't need the mock to do anything for this.
  }

  @Override
  public void addFeatures(ModelFeatures features) {
    // Does nothing because we don't need the mock to do anything for this.
  }

  public List<String> getLog() {
    return log;
  }
}
