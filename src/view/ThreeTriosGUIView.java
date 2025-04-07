package view;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import controller.ViewFeatures;
import model.card.Colors;
import model.card.Positions;
import model.cell.CardCell;
import model.card.CardInterface;
import model.cell.Cell;
import model.cell.HoleCell;
import model.ReadOnlyThreeTriosModel;

/**
 * This view implementation creates an intractable GUI projecting the state of the game,
 * and both players' hands. Also allows user clicks as inputs.
 * */
public class ThreeTriosGUIView implements ThreeTriosGUI {

  private final ReadOnlyThreeTriosModel model;
  private List<JPanel> redPlayerCards;
  private List<JPanel> bluePlayerCards;
  boolean isHighlighted;
  private final JFrame frame;
  // The main frame we are using to concatenate all sub panels
  private JPanel selectedCardPanel;
  private ViewFeatures features;


  /**
   * Constructs a GUI view based off inputted model.
   * */
  public ThreeTriosGUIView(ReadOnlyThreeTriosModel model) {
    this.model = model;
    this.frame = new JFrame();
    redPlayerCards = new ArrayList<>();
    bluePlayerCards = new ArrayList<>();
    isHighlighted = false;
  }

  @Override
  public void addFeatures(ViewFeatures f) {
    this.features = f;
  }

  @Override
  public void highlightCardPanel(int playerIndex, int cardIndex) {
    if (playerIndex < 0 || playerIndex > 1) {
      throw new IllegalArgumentException("Invalid player chosen");
    }
    if (playerIndex == 0) {
      JPanel relevantPanel = redPlayerCards.get(cardIndex);
      highlightCardPanelHelper(relevantPanel, Color.RED);
    } else {
      JPanel relevantPanel = bluePlayerCards.get(cardIndex);
      highlightCardPanelHelper(relevantPanel, Color.BLUE);
    }
  }

  @Override
  public void unhighlightCardPanel(int playerIndex, int cardIndex) {
    if (playerIndex < 0 || playerIndex > 1) {
      throw new IllegalArgumentException("Invalid player chosen");
    }
    if (playerIndex == 0) {
      JPanel relevantPanel = redPlayerCards.get(cardIndex);
      unhighlightCardPanelHelper(relevantPanel, Color.RED);
    } else {
      JPanel relevantPanel = bluePlayerCards.get(cardIndex);
      unhighlightCardPanelHelper(relevantPanel, Color.BLUE);
    }
  }

  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(frame, message);
  }

  @Override
  public void render() throws IOException {
    try {
      initFrame(); // Initialize the frame with base settings

      // Create the main panel with BorderLayout to be added to main frame
      JPanel mainPanel = new JPanel(new BorderLayout());

      // Create a panel representing the current grid of the game (Not player cards)
      JPanel gridPanel = createGridLayout();

      // Create the hand panels for both players
      JPanel leftHandPanel = createHandPanel(0); // Red player
      JPanel rightHandPanel = createHandPanel(1); // Blue player

      // Concatenate all panels together to the main panel
      mainPanel.add(leftHandPanel, BorderLayout.WEST);
      mainPanel.add(gridPanel, BorderLayout.CENTER);
      mainPanel.add(rightHandPanel, BorderLayout.EAST);

      // Remove all content from the frame and add the new main panel to the main frame
      frame.getContentPane().removeAll();
      frame.getContentPane().add(mainPanel);

      // Finalize the frame's creation
      frame.revalidate();
      frame.repaint();
      frame.pack();
      frame.setVisible(true);
    } catch (Exception e) {
      throw new IOException("Error rendering state of the game.", e);
    }
  }


  private void initFrame() {
    // Sets title of frame and action on close
    frame.setTitle(String.format("Current player: %s",
            model.getPlayerInTurn().getHandCopy().get(0).getColor().toString().toUpperCase()));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    redPlayerCards.clear();
    bluePlayerCards.clear();
    // Reset card lists
  }

  private JPanel createGridLayout() {
    int[] dims = model.getGridSize();
    int rows = dims[0];
    int cols = dims[1];
    // Get dimensions of grid

    JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
    gridPanel.setPreferredSize(new Dimension(600, 600));
    // Initialize panel with these pixel dimensions

    Cell[][] grid = model.getGrid();
    // For each cell in the grid
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Cell cell = grid[row][col];
        // Initialize the panel that we will want to add to our grid via cellPanel
        JPanel cellPanel;

        // Check the class type of current cell
        if (cell instanceof HoleCell) {
          // If hole cell, set cellPanel to be a gray square
          cellPanel = new JPanel();
          cellPanel.setBackground(Color.GRAY);
        } else if (cell instanceof CardCell) {
          // If card cell,
          CardCell ccell = (CardCell) cell;
          // Check if card cell holds a card.
          if (ccell.getCard().isPresent()) {
            // If it does, set cellPanel to be a helper that will draw the card square for us
            CardInterface card = ccell.getCard().get();
            cellPanel = createCardPanel(card);
          } else {
            // If it doesnt, set cellPanel to be a yellow square
            cellPanel = new JPanel();
            cellPanel.setBackground(Color.YELLOW);
          }
        } else {
          // Should never reach
          cellPanel = new JPanel();
        }

        final int cellRow = row;
        final int cellCol = col;
        // For each individual cellPanel created, we want to hold a place in memory for its
        // specific location.

        // We then initialize a mouse listener object that will print out the individual cell's
        // row and column upon being clicked.
        cellPanel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (features == null) {
              throw new IllegalStateException("Features not set!");
            }
            features.selectGridCell(cellRow, cellCol);
          }
        });

        cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gridPanel.add(cellPanel);
        // Set border to black, and add the current cellPanel to the grid
      }
    }

    // Return final grid panel
    return gridPanel;
  }

  private JPanel createCardPanel(CardInterface card) {
    JPanel cardPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        // Override paintComponent to draw explicitly one card for us upon being called
        super.paintComponent(g);
        // Initialize a graphics2D object to draw on top of the card panel
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // Set background to be color of card panel
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, width, height);

        g2d.setFont(new Font("Arial", Font.BOLD, 20));

        // Write down North, South, East, and West values on each corresponding side for
        // the current card being drawn.
        g2d.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics();
        String north = card.getValueFromPos(Positions.NORTH).toString();
        String south = card.getValueFromPos(Positions.SOUTH).toString();
        String east = card.getValueFromPos(Positions.EAST).toString();
        String west = card.getValueFromPos(Positions.WEST).toString();

        // North
        g2d.drawString(north, (width - fm.stringWidth(north)) / 2, fm.getAscent());
        // South
        g2d.drawString(south, (width - fm.stringWidth(south)) / 2, height - fm.getDescent());
        // West
        g2d.drawString(west, 5, (height + fm.getAscent()) / 2);
        // East
        g2d.drawString(east, width - fm.stringWidth(east) - 5, (height + fm.getAscent()) / 2);
      }
    };
    // Set background of panel to be the cards color
    Color cardColor = card.getColor() == Colors.RED ? Color.RED : Color.BLUE;
    cardPanel.setBackground(cardColor);
    return cardPanel;
  }

  private JPanel createHandPanel(int playerIndex) {
    // Concatenates all card panels into one big hand panel
    JPanel handPanel = new JPanel();
    // Set main panel to be a vertical box
    handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.Y_AXIS));
    List<CardInterface> hand = model.getPlayers().get(playerIndex).getHandCopy();

    // For each card in a players hand
    for (int i = 0; i < hand.size(); i++) {
      CardInterface card = hand.get(i);
      JPanel cardPanel = createCardPanel(card);
      // Create individual card panel
      cardPanel.setPreferredSize(new Dimension(100, 100));
      // Set preferred size of this panel so it is not too small

      final int cardIndex = i;
      final int thisPlayerIndex = playerIndex;
      // Initialize final variables to hold a cards current index in the players hand
      // To later perform an action upon being clicked

      // Add mouse listener to highlight and select clicked cards in a players hand.
      // Only accepts the highlighting of cards in the player in turn's hand.
      cardPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (features == null) {
            throw new IllegalStateException("Features not set!");
          }
          features.selectCard(thisPlayerIndex, cardIndex);
        }
      });

      cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      if (playerIndex == 0) {
        redPlayerCards.add(cardPanel);
      } else {
        bluePlayerCards.add(cardPanel);
      }
      handPanel.add(cardPanel);
      // Set border to black, and add each card to main hand panel and to panel lists
    }

    return handPanel;
  }

  private void highlightCardPanelHelper(JPanel cardPanel, Color originalColor) {
    Color highlightColor = (originalColor == Color.RED) ? Color.PINK : Color.CYAN;
    cardPanel.setBackground(highlightColor);
    cardPanel.repaint();
  }

  private void unhighlightCardPanelHelper(JPanel cardPanel, Color originalColor) {
    cardPanel.setBackground(originalColor);
    cardPanel.repaint();
  }
}
