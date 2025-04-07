package model.mock;

import java.util.List;
import java.util.Objects;

import model.card.Card;
import model.card.CardInterface;
import model.ReadOnlyThreeTriosModel;
import model.card.Colors;
import model.card.Positions;
import model.cell.CardCell;
import model.cell.Cell;
import model.cell.HoleCell;
import player.GameMoveData;
import player.Player;
import player.ReadOnlyPlayer;

/**
 * Public class acting as a model simulator for MiniMax method.
 */
public class ReadOnlyMockModel implements ReadOnlyThreeTriosModel {
  boolean isGameStarted;
  boolean isGameOver;
  model.cell.Cell[][] grid;
  ReadOnlyPlayer playerTurn;
  CardInterface newlyPlacedCard;
  ReadOnlyPlayer redPlayer;
  ReadOnlyPlayer bluePlayer;

  /**
   * Constructor taking in a model to deep copy and simulate.
   *
   * @param model model to simulate
   */
  public ReadOnlyMockModel(ReadOnlyThreeTriosModel model) {
    this.isGameStarted = model.isGameStarted();
    this.isGameOver = model.isGameOver();
    this.grid = model.getGrid();
    this.playerTurn = model.getPlayerInTurn();
    this.newlyPlacedCard = model.getNewlyPlacedCard();
    this.redPlayer = model.getRedPlayer();
    this.bluePlayer = model.getBluePlayer();
  }


  @Override
  public boolean isGameOver() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    return isGameOver;
  }

  @Override
  public boolean isGameStarted() {
    return isGameStarted;
  }

  @Override
  public Player[] getWinner() {
    return new Player[0];
  }

  @Override
  public model.cell.Cell[][] getGrid() {
    model.cell.Cell[][] copyGrid = new model.cell.Cell[grid.length][grid[0].length];

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        model.cell.Cell oldCell = grid[i][j];
        if (oldCell instanceof model.cell.CardCell) {
          if (((model.cell.CardCell) oldCell).getCard().isEmpty()) {
            copyGrid[i][j] = new model.cell.CardCell();
          } else {
            model.cell.CardCell newCell = new model.cell.CardCell();
            if (((model.cell.CardCell) oldCell).getCard().isPresent()) {
              newCell.setCard(((model.cell.CardCell) oldCell).getCard().get());
            }
            copyGrid[i][j] = newCell;
          }
        } else if (oldCell instanceof model.cell.HoleCell) {
          copyGrid[i][j] = new model.cell.HoleCell();
        }
      }
    }

    return copyGrid;
  }

  @Override
  public ReadOnlyPlayer getPlayerInTurn() {
    return playerTurn;
  }

  @Override
  public List<ReadOnlyPlayer> getPlayers() {
    return null;
  }

  @Override
  public int[] getGridSize() {
    return new int[]{this.grid.length, this.grid[0].length};
  }

  @Override
  public CardInterface getGridCellCard(int row, int col) {
    if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
      return null;
    }

    model.cell.Cell desiredCell = grid[row][col];

    if (desiredCell instanceof model.cell.HoleCell) {
      return null;
    } else {
      if (((model.cell.CardCell) desiredCell).getCard().isPresent()) {
        return ((model.cell.CardCell) desiredCell).getCard().get();
      }
      return null;
    }
  }

  @Override
  public void isLegalPlay(int handIndex, int gridRow, int gridCol) {
    if (isGameOver || !isGameStarted) {
      throw new IllegalStateException("Cannot playToCell: game is over or hasn't started.");
    } else if (handIndex >= playerTurn.getHandCopy().size() || handIndex < 0) {
      throw new IllegalArgumentException("Invalid handIndex: not a valid index of player hand.");
    } else {
      if (getGridCellCard(gridRow, gridCol) != null) {
        throw new IllegalArgumentException("Invalid grid location: desired grid location "
                + "already occupied.");
      }
    }
  }

  @Override
  public Player getOwnerAtCell(int gridRow, int gridCol) {
    return null;
  }

  @Override
  public int potentialCardsFlipped(GameMoveData move) {
    isLegalPlay(move.getCardIndex(), move.getGridRow(), move.getGridCol());

    // Find number of cards player owns
    Colors playerColor = playerTurn.getColor();
    boolean isRed;
    int cardsOwnedBefore;
    if (playerColor == Colors.RED) {
      isRed = true;
    } else {
      isRed = false;
    }

    // Simulate the move being played
    model.cell.Cell[][] copyGrid = getGrid();
    cardsOwnedBefore = getPlayerScore(copyGrid, isRed);
    newlyPlacedCard = playerTurn.getHandCopy().get(move.getCardIndex());
    setGridCellCard(copyGrid, move.getGridRow(), move.getGridCol(), newlyPlacedCard);
    comboBattleStep(copyGrid, new int[]{move.getGridRow(), move.getGridCol()});

    // Find number of cards player now owns and return the difference
    int cardsOwnedAfter = getPlayerScore(copyGrid, isRed);
    return cardsOwnedAfter - cardsOwnedBefore;
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

  /**
   * Setter for mock read only model.
   *
   * @param desiredGrid grid for model
   * @param gridRow     row index
   * @param gridCol     col index
   * @param card        card we want to set cell to
   */
  public void setGridCellCard(model.cell.Cell[][] desiredGrid, int gridRow, int gridCol,
                              CardInterface card) {
    if (card == null) {
      throw new IllegalArgumentException("Supplied card cannot be null.");
    } else if (isGameOver || !isGameStarted) {
      throw new IllegalStateException("Game hasn't started or is over.");
    }

    if (gridRow < 0 || gridRow >= desiredGrid.length || gridCol < 0
            || gridCol >= desiredGrid[0].length) {
      throw new IllegalArgumentException("Supplied grid location not on grid.");
    }

    model.cell.Cell desiredCell = desiredGrid[gridRow][gridCol];

    if (desiredCell instanceof model.cell.HoleCell) {
      throw new IllegalArgumentException("Supplied grid location is a HoleCell.");
    } else {
      if (((model.cell.CardCell) desiredCell).getCard().isPresent()) {
        throw new IllegalArgumentException("Supplied grid location is an already filled CardCell");
      } else {
        CardInterface cardDeepCopy = new Card(card.getName(),
                card.getValueFromPos(Positions.NORTH),
                card.getValueFromPos(Positions.SOUTH),
                card.getValueFromPos(Positions.EAST),
                card.getValueFromPos(Positions.WEST));
        cardDeepCopy.setColor(card.getColor());
        ((model.cell.CardCell) desiredCell).setCard(cardDeepCopy);
      }
    }
  }

  @Override
  public int getPlayerScore(boolean isRedPlayer) {
    return 0;
  }

  private int getPlayerScore(model.cell.Cell[][] desiredGrid, boolean isRedPlayer) {
    ReadOnlyPlayer player;
    if (isRedPlayer) {
      player = redPlayer;
    } else {
      player = bluePlayer;
    }
    int score = player.getHandCopy().size();

    // Increment score for every card player owns on the grid
    Colors playerColor = player.getColor();
    for (model.cell.Cell[] row : desiredGrid) {
      for (model.cell.Cell cell : row) {
        if (cell instanceof model.cell.CardCell) {
          if (((model.cell.CardCell) cell).getCard().isPresent()) {
            if (Objects.equals(((model.cell.CardCell) cell).getCard().get().getColor(),
                    playerColor)) {
              score++;
            }
          }
        }
      }
    }

    return score;
  }

  private void comboBattleStep(model.cell.Cell[][] desiredGrid, int[] centerLocation) {
    CardInterface battleCenterCard = getGridSpecificCellCard(desiredGrid, centerLocation[0],
            centerLocation[1]);

    int[] adjNorthLocation = new int[]{centerLocation[0] - 1, centerLocation[1]};
    int[] adjSouthLocation = new int[]{centerLocation[0] + 1, centerLocation[1]};
    int[] adjEastLocation = new int[]{centerLocation[0], centerLocation[1] + 1};
    int[] adjWestLocation = new int[]{centerLocation[0], centerLocation[1] - 1};

    CardInterface adjacentNorth = getGridSpecificCellCard(desiredGrid, adjNorthLocation[0],
            adjNorthLocation[1]);
    CardInterface adjacentSouth = getGridSpecificCellCard(desiredGrid, adjSouthLocation[0],
            adjSouthLocation[1]);
    CardInterface adjacentEast = getGridSpecificCellCard(desiredGrid, adjEastLocation[0],
            adjEastLocation[1]);
    CardInterface adjacentWest = getGridSpecificCellCard(desiredGrid, adjWestLocation[0],
            adjWestLocation[1]);

    comboBattleComparison(desiredGrid, battleCenterCard, adjacentNorth, adjNorthLocation,
            Positions.NORTH);
    comboBattleComparison(desiredGrid, battleCenterCard, adjacentSouth, adjSouthLocation,
            Positions.SOUTH);
    comboBattleComparison(desiredGrid, battleCenterCard, adjacentEast, adjEastLocation,
            Positions.EAST);
    comboBattleComparison(desiredGrid, battleCenterCard, adjacentWest, adjWestLocation,
            Positions.WEST);
  }

  private void comboBattleComparison(model.cell.Cell[][] desiredGrid, CardInterface center,
                                     CardInterface adj, int[] adjLocation,
                                     Positions adjPosition) {
    // Only do battle with adjacent cards if they are of the opposing color.
    // Recursive calls to cards that have been switched offers the combo phase functionality
    if (adj != null) {
      if (center.getColor() != adj.getColor()) {
        if (battleComparison(center, adj, adjPosition)) {
          comboBattleStep(desiredGrid, adjLocation);
        }
      }
    }
  }

  private boolean battleComparison(CardInterface card1, CardInterface card2,
                                   Positions adjacentPosition) {
    if (card1 == null) {
      throw new IllegalArgumentException("Card1 is assumed the battleCenterCard "
              + "and shouldn't be null.");
    } else if (card2 == null) {
      return false;
    }
    return card1.determineWin(card2, adjacentPosition);
  }

  private CardInterface getGridSpecificCellCard(model.cell.Cell[][] desiredGrid, int row, int col) {
    if (row < 0 || row >= desiredGrid.length || col < 0 || col >= desiredGrid[0].length) {
      return null;
    }

    model.cell.Cell desiredCell = desiredGrid[row][col];

    if (desiredCell instanceof HoleCell) {
      return null;
    } else {
      if (((model.cell.CardCell) desiredCell).getActualCard().isPresent()) {
        return ((CardCell) desiredCell).getActualCard().get();
      }
      return null;
    }
  }

  /**
   * Gets the actual grid from the mock model.
   *
   * @return the actual grid from the mock model. NOT a deep copy.
   */
  public Cell[][] getActualGrid() {
    return this.grid;
  }
}
