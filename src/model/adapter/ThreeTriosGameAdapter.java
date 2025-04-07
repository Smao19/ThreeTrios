package model.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.ThreeTriosGameModel;
import model.card.CardInterface;
import model.card.Colors;
import player.GameMoveData;
import provider.model.cell.Cell;
import provider.model.cell.Player;
import provider.model.card.ICard;

/**
 * Adapter used to convert our model to the providers' model.
 */
public class ThreeTriosGameAdapter implements provider.model.IReadOnlyTTModel,
        provider.model.IThreeTriosModel {
  private player.Player redPlayer;
  private player.Player bluePlayer;
  private ThreeTriosGameModel gameModel;

  /**
   * Constructor for model adapter.
   * @param model model to be adapted.
   */
  public ThreeTriosGameAdapter(ThreeTriosGameModel model, player.Player redPlayer,
                               player.Player bluePlayer) {
    this.redPlayer = Objects.requireNonNull(redPlayer);
    this.bluePlayer = Objects.requireNonNull(bluePlayer);
    this.gameModel = model;
  }

  @Override
  public void startGame(boolean shuffled) {
    gameModel.startGame(redPlayer, bluePlayer, shuffled);

  }

  @Override
  public void addFeature(provider.model.ModelFeatures features) {
    controller.ModelFeatures adaptedFeatures = new ProviderMFToRegMF(features, gameModel);
    gameModel.addFeatures(adaptedFeatures);
  }

  @Override
  public void playToGrid(int row, int col, int handIndex) {
    GameMoveData move = new GameMoveData(row, col, handIndex);
    gameModel.playMove(move);
  }

  @Override
  public boolean isGameOver() {
    return gameModel.isGameOver();
  }

  @Override
  public List<ICard> getPlayerHand(provider.model.cell.Player player) {
    List<CardInterface> hand;
    List<ICard> convertedHand = new ArrayList<>();
    if (player.equals(provider.model.cell.Player.RED)) {
      return convertList(gameModel.getRedPlayer().getHandCopy());
    } else {
      return convertList(gameModel.getBluePlayer().getHandCopy());
    }
  }

  private List<ICard> convertList(List<CardInterface> list) {
    List<ICard> convertedList = new ArrayList<>();
    for (CardInterface c : list) {
      convertedList.add(new CardInterfaceToICard(c));
    }
    return convertedList;
  }

  @Override
  public Cell[][] getGrid() {
    int[] dims = gameModel.getGridSize();
    Cell[][] newGrid = new Cell[dims[0]][dims[1]];
    model.cell.Cell[][] ourGrid = gameModel.getGrid();
    for (int i = 0; i < dims[0]; i++) {
      for (int j = 0; j < dims[1]; j++) {
        Cell convertedCell = new AdaptCellToProviderCell(ourGrid[i][j]);
        newGrid[i][j] = convertedCell;
      }
    }
    return newGrid;
  }

  @Override
  public int countPlayerCells(provider.model.cell.Player player) {
    if (player == provider.model.cell.Player.RED) {
      return gameModel.getPlayerScore(true);
    } else {
      return gameModel.getPlayerScore(false);
    }
  }

  @Override
  public Player getCurrentTurn() {
    model.card.Colors currentPlayerColor = gameModel.getPlayerInTurn().getColor();
    if (currentPlayerColor.equals(model.card.Colors.RED)) {
      return provider.model.cell.Player.RED;
    } else {
      return provider.model.cell.Player.BLUE;
    }
  }

  @Override
  public Player getWinner() {
    player.Player[] winners = gameModel.getWinner();
    if (winners.length > 1) {
      return null;
      // THIS IS NOT MENTIONED IN JAVADOCS.
      // OUR CODE PROVIDERS DO NOT HANDLE GAME DRAWS.
      // WE HAVE GONE AHEAD AND DONE IT FOR THEM BY MAKING THIS METHOD RETURN NULL WHEN
      // THERE IS NO WINNER (TIE).
    } else {
      return (winners[0].getColor() == Colors.RED) ? Player.RED : Player.BLUE;
    }
  }

  @Override
  public int hypotheticalFlip(int row, int col, ICard card) {
    List<ICard> currentPlayerHand = getPlayerHand(getCurrentTurn());
    int idx = currentPlayerHand.indexOf(card);
    if (idx < 0) {
      throw new IllegalArgumentException("Inputted card is not in current player's hand!");
    }
    return gameModel.potentialCardsFlipped(new GameMoveData(row, col, idx));
  }
}
