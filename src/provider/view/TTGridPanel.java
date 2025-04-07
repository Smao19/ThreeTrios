package provider.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

import provider.model.IReadOnlyTTModel;
import provider.model.card.ICard;
import provider.model.cell.CellType;
import provider.model.cell.Player;
import provider.model.cell.Cell;

/**
 * Panel to display the Grid of the ThreeTriosGame. Holes are gray, and playable spaces are yellow.
 * User clicks on panel to play their selected card.
 */
public class TTGridPanel extends JPanel {
  private static final Color REDCOLOR = Color.PINK;
  private static final Color BLUECOLOR = Color.CYAN;
  private static final Color HOLECOLOR = Color.GRAY;
  private static final Color EMPTYCOLOR = Color.YELLOW;
  private final IReadOnlyTTModel model;
  private final List<EventFeatures> features = new ArrayList<>();

  public TTGridPanel(IReadOnlyTTModel model) {
    this.model = model;
    this.addMouseListener(new MouseEventsListener());
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    drawGrid(g2d, this.model.getGrid());
  }

  public void addFeatureListener(EventFeatures features) {
    this.features.add(features);
  }

  private void drawGrid(Graphics2D g, Cell[][] grid) {
    int height = getHeight() / grid.length;
    int width = getWidth() / grid[0].length;
    int x = 0;
    int y = 0;
    for (Cell[] row : grid) {
      x = 0;
      for (Cell cell : row) {
        g.setColor(Color.black);
        g.drawRect(x, y, width, height);
        if (cell.canPlayCard()) {
          g.setColor(EMPTYCOLOR);
          g.fillRect(x, y, width, height);
        } else if (cell.getType() == CellType.HOLE) {
          g.setColor(HOLECOLOR);
          g.fillRect(x, y, width, height);
        } else {
          drawCard(g, cell.getCard(), cell.getPlayer() == Player.RED ? REDCOLOR : BLUECOLOR,
                  x, y, width, height);
        }
        x += width;
      }
      y += height;
    }
  }

  private void drawCard(Graphics2D g2d, ICard card, Color color,
                        int x, int y, int width, int height) {
    g2d.setColor(color);
    g2d.fillRect(x, y, width, height);
    g2d.setColor(Color.BLACK);
    g2d.drawRect(x, y, width, height);
    Font font = new Font("serif", Font.BOLD, height / 4);
    drawText(g2d, font, card.getNorth().toString(), x + width / 2, y + height / 4);
    drawText(g2d, font, card.getSouth().toString(), x + width / 2, y + 3 * height / 4);
    drawText(g2d, font, card.getWest().toString(), x + width / 4, y + height / 2);
    drawText(g2d, font, card.getEast().toString(), x + 3 * width / 4, y + height / 2);
  }

  private void drawText(Graphics2D g, Font font, String text, int x, int y) {
    FontMetrics metrics = g.getFontMetrics(font);
    g.setFont(font);
    g.drawString(text, x - metrics.stringWidth(text) / 2,
            y - metrics.getHeight() / 2 + metrics.getAscent());
  }

  private class MouseEventsListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      Cell[][] grid = model.getGrid();
      int row = Math.min(grid[0].length - 1, Math.floorDiv(e.getX(),
              (getWidth()  / grid[0].length)));
      int col = e.getY() / (getHeight() / grid.length);
      for (EventFeatures feature : features) {
        feature.playToGrid(col, row);
      }
      repaint();
    }
  }
}
