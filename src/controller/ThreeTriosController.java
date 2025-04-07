package controller;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import model.ThreeTriosModel;
import model.card.Colors;
import player.ComputerPlayer;
import player.GameMoveData;
import player.Player;
import player.UserPlayer;
import view.ThreeTriosGUI;

/**
 * Represent a controller that can be used for an individual player to interact with and play a
 * game of Three Trios.
 */
public class ThreeTriosController implements ModelFeatures, ViewFeatures {
  private ThreeTriosGUI view;
  private Player activePlayer;
  private ThreeTriosModel model;
  private Map.Entry<Integer, Integer> currentlyHighlightedCard; // player index, card index
  private int playerIdx;

  /**
   * Constructs a controller for a specified player, view, and mode.
   * @param model Game model to be played with
   * @param player who will use this controller to interact with the game
   * @param view view specific to player
   */
  public ThreeTriosController(ThreeTriosModel model, Player player, ThreeTriosGUI view) {
    activePlayer = Objects.requireNonNull(player);
    this.model = Objects.requireNonNull(model);
    this.view = Objects.requireNonNull(view);
    currentlyHighlightedCard = Map.entry(-1, -1); // When no card is highlighted,
    if (activePlayer.getColor() == Colors.RED) {
      playerIdx = 0;
    } else {
      playerIdx = 1;
    }
    this.model.addFeatures(this);
    this.view.addFeatures(this);
  }

  private void showComputerPlayerSafeMessage(String message) {
    if (activePlayer instanceof UserPlayer) {
      view.showMessage(message);
    }
  }

  @Override
  public void selectCard(int playerIndex, int cardIndex) {
    if (model.isGameOver()) {
      gameEnd();
      return;
    }

    if (model.getPlayerInTurn().getColor() != activePlayer.getColor()) {
      showComputerPlayerSafeMessage("Can't make a move when out of turn");
      return;
    }
    if (playerIndex != this.playerIdx) {
      showComputerPlayerSafeMessage("Please select a card from your own hand!");
      return;
    }
    // If there is a currently highlighted card, or if we select the same, already selected card:
    //      Un highlight it using view.unhighlightCardPanel(...);
    if (currentlyHighlightedCard.equals(Map.entry(-1, -1))) { // If no card highlighted
      currentlyHighlightedCard = Map.entry(playerIndex, cardIndex); // update highlighted card
      view.highlightCardPanel(playerIndex, cardIndex);
    } else if (currentlyHighlightedCard.equals(Map.entry(playerIndex, cardIndex))) {
      // If trying to highlight same card as already highlighted card
      view.unhighlightCardPanel(playerIndex, cardIndex); // Un-highlight card
      currentlyHighlightedCard = Map.entry(-1, -1); // Update highlighted card
    } else {
      // Otherwise, there is a currently highlighted card
      int pIdx = currentlyHighlightedCard.getKey();
      int cIdx = currentlyHighlightedCard.getValue();
      view.unhighlightCardPanel(pIdx, cIdx); // Un-highlight current card
      view.highlightCardPanel(playerIndex, cardIndex); // highlight new card
      currentlyHighlightedCard = Map.entry(playerIndex, cardIndex); // update highlighted card
    }

  }

  @Override
  public void selectGridCell(int row, int col) {
    if (model.isGameOver()) {
      gameEnd();
      return;
    }

    if (model.getPlayerInTurn().getColor() != activePlayer.getColor()) {
      showComputerPlayerSafeMessage("Can't make a move when out of turn");
      return;
    }

    if (currentlyHighlightedCard.equals(Map.entry(-1, -1))) { // If card is not selected
      showComputerPlayerSafeMessage("Please select a card before trying to make a move!");
      throw new IllegalStateException("No card currently selected.");
    } else {
      try {
        model.playMove(new GameMoveData(row, col, currentlyHighlightedCard.getValue()));
        // if card is selected, play move
        view.unhighlightCardPanel(currentlyHighlightedCard.getKey(),
                currentlyHighlightedCard.getValue());
        currentlyHighlightedCard = Map.entry(-1, -1);
        // Then unselect just played card and un-highlight
      } catch (Exception e) {
        showComputerPlayerSafeMessage(e.getMessage());
      }
    }
  }


  @Override
  public void playerTurn(int playerIndex) {
    // Render view regardless of turn
    try {
      view.render();
    } catch (IOException e) {
      throw new IllegalStateException("Rendering GUI view failed.");
    }

    if (model.isGameOver()) {
      gameEnd();
      return;
    }

    if (this.playerIdx == playerIndex) {
      showComputerPlayerSafeMessage("It's your turn!");

      // Make moves for computer players
      if (this.activePlayer instanceof ComputerPlayer) {
        GameMoveData computerMove = activePlayer.getNextPlacement();
        while (true) {
          try {
            model.playMove(computerMove);
            break;
          } catch (Exception e) {
            showComputerPlayerSafeMessage(e.getMessage());
          }
        }
      }
    }
  }

  @Override
  public void gameEnd() {
    Player[] winners = model.getWinner();
    String winnerColorStr;
    String winString;
    boolean isRedPlayer;
    if (winners.length == 1) {
      if (winners[0].getColor() == Colors.RED) {
        winnerColorStr = "RED";
        isRedPlayer = true;
      } else {
        winnerColorStr = "BLUE";
        isRedPlayer = false;
      }
      winString = String.format("Player %s wins!!!", winnerColorStr);
    } else {
      winString = "Player RED and BLUE draw!!!";
      isRedPlayer = true;  // Doesn't matter since both players have the same score
    }

    if (model.isGameOver()) {
      showComputerPlayerSafeMessage(String.format("%s\nScore: %d", winString,
              model.getPlayerScore(isRedPlayer)));
    }
  }
}
