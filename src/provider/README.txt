OVERVIEW:
ThreeTrios is a 2 player card game where players battle with their cards to control the majority
of the grid. This game is intended for players familiar with the rules of ThreeTrios.

Quickstart:
To play a game first create a configuration for the deck and grid examples can be found in the
test/model/configs folder. Game models be loaded by creating a controller and passing
a string path to the board config and deck config ex.
IThreeTriosController controller = new ThreeTriosController("pathToBoardConfig", "pathToDeckConfig");

Key Components:
IThreeTriosModel: The model of the ThreeTrios game, facilitates the gameplay functionality and
enforces all rules.
IThreeTriosView: Renders the model in a way for the players to interact and understand
the game state.
IThreeTriosController: Controller of the ThreeTrios game, acts as mediator between view and model.
TTStrategy: An AI Strategy interface for a computer-controller player.

Key Subcomponents:
model:
  IGrid: A game board for ThreeTrios, facilitates playing to the board and assessing board states.
  ICell: A tile on the game board. Can hold a card and who controls the cell.
  ICard: A card for ThreeTrios. Enables comparisons between cards and their values.
view:
  TTContainerPanel: Container panel for the grid, and hand panels
  TTHandPanel: Panel for displaying a single player's hand and enabling card selection.
  TTGridPanel: Panel for displaying the Grid state and enabling card placement selection.

Source Organization:
controller: All controller interfaces and class implementations
model: All Model classes, interfaces and related subcomponent folders
  card: Card classes, interfaces, and related enums
  cell: Cell classes, interfaces, and related enums
  grid: Grid classes and interfaces
strategy: ThreeTrios Strategy interface and related class implementations
view: All View classes and interfaces

Changes for part 2:
  ReadME Updated to reflect current project state.
  Added ICard interface for Card. Updated all classes to use ICard instead of concrete class
  Added HypotheticalFlip method to Model and Grid to be used by strategies

Extra Credit: We implemented strategies 3 and 4 (HardestToFlip, NoGoodMoves) and TryTwo which is a class
that lets up stack as many strategies as we want. In the tests for TryTwo, we have a three-layer strategy
that combines BestCorner, FlipMax, and NoGoodMoves.

