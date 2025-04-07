package player.strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.card.CardInterface;
import model.mock.ReadOnlyMockModel;
import model.ReadOnlyThreeTriosModel;
import player.GameMoveData;

/**
 * Represents the functionality for executing the MiniMaxStrategy.
 */
public class MiniMaxStrategy extends AbstractStrategy {
  Strategy opponentStrategy;

  /**
   * Constructor for initializing a MiniMaxStrategy with a ReadOnly Model.
   *
   * @param model ReadOnly Model to be read from
   */
  public MiniMaxStrategy(ReadOnlyThreeTriosModel model, Strategy opponentStrategy) {
    super(model);
    this.opponentStrategy = opponentStrategy;
  }

  @Override
  public List<GameMoveData> execute() {
    List<Point> potentialLocations = getEmptyCardCellsPositions();
    HashMap<GameMoveData, Integer> potentialMoves = new HashMap<>();

    for (Point location : potentialLocations) {
      List<CardInterface> hand = model.getPlayerInTurn().getHandCopy();
      for (int i = 0; i < hand.size(); i++) {
        GameMoveData move = new GameMoveData(location.x, location.y, i);
        ReadOnlyMockModel simModel = new ReadOnlyMockModel(model);
        // Play proposed move to simModel
        simModel.setGridCellCard(simModel.getActualGrid(), location.x, location.y, hand.get(i));

        // Find opponents response
        Strategy simOpponentStrategy = createStrategySimCopy(opponentStrategy, simModel);
        simOpponentStrategy.execute();

        // Add opponents response value to map
        potentialMoves.put(move, simOpponentStrategy.getBestMoveValue());
      }
    }

    // Find min opponent response value
    int minOppResponse = Integer.MAX_VALUE;
    List<GameMoveData> bestMoves = new ArrayList<>();
    for (HashMap.Entry<GameMoveData, Integer> entry : potentialMoves.entrySet()) {
      minOppResponse = Math.min(minOppResponse, entry.getValue());
    }

    // Find all best moves that have the minOppResponse
    for (HashMap.Entry<GameMoveData, Integer> entry : potentialMoves.entrySet()) {
      if (entry.getValue() == minOppResponse) {
        bestMoves.add(entry.getKey());
      }
    }

    return bestMoves;
  }

  private Strategy createStrategySimCopy(Strategy strat, ReadOnlyThreeTriosModel model) {
    if (strat instanceof CornerStrategy) {
      return new CornerStrategy(model);
    } else if (strat instanceof LeastFlippableStrategy) {
      return new LeastFlippableStrategy(model);
    } else if (strat instanceof MaxCardsFlipStrategy) {
      return new MaxCardsFlipStrategy(model);
    } else {
      throw new IllegalArgumentException("Inputted strategy is not valid vs MiniMax");
    }
  }

}