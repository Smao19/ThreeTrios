package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import controller.ModelFeatures;
import model.card.Colors;
import model.card.Positions;
import model.cell.CardCell;
import model.cell.HoleCell;
import model.filereader.CardConfigFileReader;
import model.filereader.GridConfigFileReader;
import player.GameMoveData;
import player.Player;
import player.ReadOnlyPlayer;
import model.card.CardInterface;
import model.card.Card;
import model.cell.Cell;

/**
 * Represents a ThreeTriosModel which maintains the game data, preserves game rules,
 * and offers methods for interacting with the game.
 */
public class ThreeTriosGameModel implements ThreeTriosModel {
  protected Cell[][] grid;
  // Grid is row, col indexed i.e. grid[row][col] = cell
  protected Player playerTurn;
  // Class invariant: playerTurn is always equal to either redPlayer or bluePlayer
  protected boolean isGameOver;
  protected boolean isGameStarted;
  protected Player redPlayer;
  protected Player bluePlayer;
  private int numCardCells;
  private int numOccupiedCardCells;
  private CardInterface newlyPlacedCard;
  private int[] newlyPlacedCardLocation;
  private final List<CardInterface> deck;
  private Random rand;
  private List<ModelFeatures> triggerController;

  /**
   * Constructor that initializes the game grid and deck by reading
   * in values from the supplied config files.
   *
   * @param gridConfigFilePath path to grid config file
   * @param cardConfigFilePath path to card config file
   * @throws IllegalStateException    if reading from the config files produces an IOException
   * @throws IllegalArgumentException if any parameter is null
   */
  public ThreeTriosGameModel(String gridConfigFilePath, String cardConfigFilePath) {
    if (gridConfigFilePath == null || cardConfigFilePath == null) {
      throw new IllegalArgumentException("Constructor does not take null parameters.");
    }

    CardConfigFileReader cardConfigReader = new CardConfigFileReader(cardConfigFilePath);
    GridConfigFileReader gridConfigReader = new GridConfigFileReader(gridConfigFilePath);
    try {
      this.deck = cardConfigReader.readCards();
      this.grid = gridConfigReader.readGrid();
    } catch (IOException e) {
      throw new IllegalStateException("Error reading from config files: " + e.getMessage());
    }

    this.numCardCells = gridConfigReader.getNumCardCells();
    this.rand = new Random();
    this.numOccupiedCardCells = 0;
    this.newlyPlacedCard = null;
    this.newlyPlacedCardLocation = new int[2];
    this.isGameStarted = false;
    this.isGameOver = false;
    this.triggerController = new ArrayList<>();
  }

  /**
   * Same constructor with additional param for presetting random for testing.
   *
   * @param gridConfigFilePath path to grid config file
   * @param cardConfigFilePath path to card config file
   * @param rand               Random object which can be preset for testing
   * @throws IllegalStateException    if reading from the config files produces an IOException
   * @throws IllegalArgumentException if any parameter is null
   */
  public ThreeTriosGameModel(String gridConfigFilePath, String cardConfigFilePath, Random rand) {
    if (gridConfigFilePath == null || cardConfigFilePath == null) {
      throw new IllegalArgumentException("Constructor does not take null parameters.");
    }

    CardConfigFileReader cardConfigReader = new CardConfigFileReader(cardConfigFilePath);
    GridConfigFileReader gridConfigReader = new GridConfigFileReader(gridConfigFilePath);
    try {
      this.deck = cardConfigReader.readCards();
      this.grid = gridConfigReader.readGrid();
    } catch (IOException e) {
      throw new IllegalStateException("Error reading from config files: " + e.getMessage());
    }

    this.numCardCells = gridConfigReader.getNumCardCells();
    this.rand = rand;
    this.numOccupiedCardCells = 0;
    this.newlyPlacedCard = null;
    this.newlyPlacedCardLocation = new int[2];
    this.isGameStarted = false;
    this.isGameOver = false;
    this.triggerController = new ArrayList<>();
  }

  protected void shuffle() {
    for (int i = 0; i < deck.size() - 1; i++) {
      // Generate random integer between i+1 inclusive and deck.size()-1 inclusive
      int randomIndex = i + 1 + rand.nextInt(deck.size() - i - 1);
      Collections.swap(deck, i, randomIndex);
    }
  }

  @Override
  public void startGame(Player redPlayer, Player bluePlayer, boolean shuffle) {
    if (isGameOver || isGameStarted) {
      throw new IllegalStateException("Cannot start game. Game is over or already started.");
    } else if (redPlayer == null || bluePlayer == null) {
      throw new IllegalArgumentException("Supplied player cannot be null.");
    } else if (deck.size() < numCardCells + 1) {
      throw new IllegalArgumentException(String.format("Not enough cards to start game. "
              + "Deck size = %d. NumCardCells = %d", deck.size(), numCardCells));
    }

    if (shuffle) {
      shuffle();
    }

    // store each player for easy access throughout the game
    this.redPlayer = redPlayer;
    this.bluePlayer = bluePlayer;

    // Deal out half the cards to each player (alternating player for each card dealt)
    for (int i = 0; i < deck.size(); i++) {
      if (i % 2 == 0) {
        deck.get(i).setColor(Colors.RED);
        redPlayer.appendToHand(deck.get(i));
      } else {
        deck.get(i).setColor(Colors.BLUE);
        bluePlayer.appendToHand(deck.get(i));
      }
    }

    isGameStarted = true;
    playerTurn = redPlayer;  // Red always starts
    for (ModelFeatures f : triggerController) {
      f.playerTurn(0);
    }
  }

  @Override
  public void playMove(GameMoveData move) {
    isLegalPlay(move.getCardIndex(), move.getGridRow(), move.getGridCol());

    try {
      newlyPlacedCard = playerTurn.pop(move.getCardIndex());
      setGridCellCard(this.grid, move.getGridRow(), move.getGridCol(), newlyPlacedCard);
      newlyPlacedCardLocation[0] = move.getGridRow();
      newlyPlacedCardLocation[1] = move.getGridCol();
      this.numOccupiedCardCells++;  // New card added to grid, increment tally accordingly
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid grid location: desired cell is "
              + "a hole or not on grid");
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid hand index! " + move.getCardIndex() + " " + e);
    }

    // Continue to the battle phase which will complete the move
    battlePhase();
  }

  @Override
  public void addFeatures(ModelFeatures features) {
    this.triggerController.add(Objects.requireNonNull(features));
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

  private void comboBattleComparison(Cell[][] desiredGrid, CardInterface center,
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

  private void comboBattleStep(Cell[][] desiredGrid, int[] centerLocation) {
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

  private void battlePhase() {
    if (!isGameStarted || isGameOver) {
      throw new IllegalStateException("Cannot conduct battlePhase: game is over or hasn't started");
    }

    comboBattleStep(this.grid, newlyPlacedCardLocation);

    // If board is full then game is over
    if (numOccupiedCardCells == numCardCells) {
      isGameOver = true;
    }
    // At this point player has finished their turn so transition to next player
    if (playerTurn == redPlayer) {
      playerTurn = bluePlayer;
      featureCallHelper(1);
    } else {
      playerTurn = redPlayer;
      featureCallHelper(0);
    }
  }

  private void featureCallHelper(int playerIdx) {
    for (ModelFeatures f : triggerController) {
      f.playerTurn(playerIdx);
    }
  }

  @Override
  public boolean isGameStarted() {
    return isGameStarted;
  }

  private void setGridCellCard(Cell[][] desiredGrid, int gridRow, int gridCol,
                               CardInterface card) {
    if (card == null) {
      throw new IllegalArgumentException("Supplied card cannot be null.");
    } else if (isGameOver || !isGameStarted) {
      throw new IllegalStateException("Game hasn't started or is over.");
    }

    if (gridRow < 0 || gridRow >= desiredGrid.length || gridCol < 0 ||
            gridCol >= desiredGrid[0].length) {
      throw new IllegalArgumentException("Supplied grid location not on grid.");
    }

    Cell desiredCell = desiredGrid[gridRow][gridCol];

    if (desiredCell instanceof HoleCell) {
      throw new IllegalArgumentException("Supplied grid location is a HoleCell.");
    } else {
      if (((CardCell) desiredCell).getCard().isPresent()) {
        throw new IllegalArgumentException("Supplied grid location is an already filled CardCell");
      } else {
        CardInterface cardDeepCopy = new Card(card.getName(),
                card.getValueFromPos(Positions.NORTH),
                card.getValueFromPos(Positions.SOUTH),
                card.getValueFromPos(Positions.EAST),
                card.getValueFromPos(Positions.WEST));
        cardDeepCopy.setColor(card.getColor());
        ((CardCell) desiredCell).setCard(cardDeepCopy);
      }
    }
  }

  @Override
  public boolean isGameOver() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    return isGameOver;
  }

  @Override
  public Player[] getWinner() {
    if (!isGameOver || !isGameStarted) {
      throw new IllegalStateException("Cannot get winner: game hasn't started or isn't over");
    } else {
      int redPlayerTally = 0;
      int bluePlayerTally = 0;

      // Whoever owns more total when the game is over wins
      for (Cell[] row : grid) {
        for (Cell cell : row) {
          if (cell instanceof CardCell) {
            if (((CardCell) cell).getCard().isPresent()) {
              if (Objects.equals(((CardCell) cell).getCard().get().getColor(), Colors.BLUE)) {
                bluePlayerTally++;
              } else {
                redPlayerTally++;
              }
            }
          }
        }
      }
      redPlayerTally += redPlayer.getHandCopy().size();
      bluePlayerTally += bluePlayer.getHandCopy().size();

      if (redPlayerTally > bluePlayerTally) {
        return new Player[]{redPlayer};
      } else if (bluePlayerTally > redPlayerTally) {
        return new Player[]{bluePlayer};
      } else {
        return new Player[]{redPlayer, bluePlayer};
      }
    }
  }

  @Override
  public Cell[][] getGrid() {
    Cell[][] copyGrid = new Cell[grid.length][grid[0].length];

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        Cell oldCell = grid[i][j];
        if (oldCell instanceof CardCell) {
          if (((CardCell) oldCell).getCard().isEmpty()) {
            copyGrid[i][j] = new CardCell();
          } else {
            CardCell newCell = new CardCell();
            if (((CardCell) oldCell).getCard().isPresent()) {
              newCell.setCard(((CardCell) oldCell).getCard().get());
            }
            copyGrid[i][j] = newCell;
          }
        } else if (oldCell instanceof HoleCell) {
          copyGrid[i][j] = new HoleCell();
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
    return List.of(redPlayer, bluePlayer);
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

    Cell desiredCell = grid[row][col];

    if (desiredCell instanceof HoleCell) {
      return null;
    } else {
      if (((CardCell) desiredCell).getCard().isPresent()) {
        return ((CardCell) desiredCell).getCard().get();
      }
      return null;
    }
  }

  private CardInterface getGridSpecificCellCard(Cell[][] desiredGrid, int row, int col) {
    if (row < 0 || row >= desiredGrid.length || col < 0 || col >= desiredGrid[0].length) {
      return null;
    }

    Cell desiredCell = desiredGrid[row][col];

    if (desiredCell instanceof HoleCell) {
      return null;
    } else {
      if (((CardCell) desiredCell).getActualCard().isPresent()) {
        return ((CardCell) desiredCell).getActualCard().get();
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
    CardInterface desiredCard = getGridCellCard(gridRow, gridCol);

    if (desiredCard != null) {
      if (desiredCard.getColor() == Colors.RED) {
        return redPlayer;
      } else if (desiredCard.getColor() == Colors.BLUE) {
        return bluePlayer;
      }
    }
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
    Cell[][] copyGrid = getGrid();
    cardsOwnedBefore = getPlayerScore(copyGrid, isRed);
    newlyPlacedCard = playerTurn.getHandCopy().get(move.getCardIndex());
    setGridCellCard(copyGrid, move.getGridRow(), move.getGridCol(), newlyPlacedCard);
    comboBattleStep(copyGrid, new int[]{move.getGridRow(), move.getGridCol()});

    // Find number of cards player now owns and return the difference
    int cardsOwnedAfter = getPlayerScore(copyGrid, isRed);
    return cardsOwnedAfter - cardsOwnedBefore;
  }

  @Override
  public int getPlayerScore(boolean isRedPlayer) {
    Player player;
    if (isRedPlayer) {
      player = redPlayer;
    } else {
      player = bluePlayer;
    }
    int score = player.getHandCopy().size();

    // Increment score for every card player owns on the grid
    Colors playerColor = player.getColor();
    for (Cell[] row : grid) {
      for (Cell cell : row) {
        if (cell instanceof CardCell) {
          if (((CardCell) cell).getCard().isPresent()) {
            if (Objects.equals(((CardCell) cell).getCard().get().getColor(), playerColor)) {
              score++;
            }
          }
        }
      }
    }

    return score;
  }

  private int getPlayerScore(Cell[][] desiredGrid, boolean isRedPlayer) {
    Player player;
    if (isRedPlayer) {
      player = redPlayer;
    } else {
      player = bluePlayer;
    }
    int score = player.getHandCopy().size();

    // Increment score for every card player owns on the grid
    Colors playerColor = player.getColor();
    for (Cell[] row : desiredGrid) {
      for (Cell cell : row) {
        if (cell instanceof CardCell) {
          if (((CardCell) cell).getCard().isPresent()) {
            if (Objects.equals(((CardCell) cell).getCard().get().getColor(), playerColor)) {
              score++;
            }
          }
        }
      }
    }

    return score;
  }

  @Override
  public CardInterface getNewlyPlacedCard() {
    return newlyPlacedCard;
  }

  @Override
  public ReadOnlyPlayer getRedPlayer() {
    return redPlayer;
  }

  @Override
  public ReadOnlyPlayer getBluePlayer() {
    return bluePlayer;
  }
}
