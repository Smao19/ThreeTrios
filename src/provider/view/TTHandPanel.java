package provider.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import provider.model.IReadOnlyTTModel;
import provider.model.card.ICard;
import provider.model.cell.Player;

/**
 * Panel to display the Hand of a player the ThreeTriosGame. Scales to fit all cards in the full
 * height of the window. Clicking on a card in the view will select the chosen card and turn it
 * yellow.
 */
public class TTHandPanel extends JPanel {
  private final IReadOnlyTTModel model;
  private final Player player;
  private final Color color;
  private int selectedIndex = -1;
  private final List<EventFeatures> features = new ArrayList<>();

  private final Color SELECTEDCOLOR = Color.YELLOW;

  /**
   * Constructor for the HandPanel.
   * @param model IReadOnlyModel to be displayed.
   * @param player player for which hand to be displayed
   */
  public TTHandPanel(IReadOnlyTTModel model, Player player) {
    this.model = model;
    this.player = player;
    this.color = this.player == Player.RED ? Color.pink : Color.cyan;
    this.addMouseListener(new MouseEventsListener());
  }

  private Dimension getLogicalSize() {
    return new Dimension(10, 60);
  }

  public void addFeatureListener(EventFeatures features) {
    this.features.add(features);
  }

  public void selectCard(int handInd) {
    this.selectedIndex = handInd;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    drawHand(g2d);
  }

  private void drawHand(Graphics2D g2d) {
    List<ICard> hand = getHand();
    if (hand.isEmpty()) {
      return;
    }
    int width = this.getWidth();
    int height = this.getHeight() / hand.size();

    for (int i = 0; i < hand.size(); i++) {
      drawCard(g2d, hand.get(i), i * height, width, height, i == selectedIndex);
    }
  }

  private void drawCard(Graphics2D g2d, ICard card, int y,
                        int width, int height, boolean isSelected) {
    g2d.setColor(isSelected ? SELECTEDCOLOR : color);
    g2d.fillRect(0, y, width, height );
    g2d.setColor(Color.BLACK);
    g2d.drawRect(0, y, width, height);
    Font font = new Font("serif", Font.BOLD, height / 4);
    drawText(g2d, font, card.getNorth().toString(), width / 2, y + height / 4);
    drawText(g2d, font, card.getSouth().toString(), width / 2, y + 3 * height / 4);
    drawText(g2d, font, card.getWest().toString(), width / 4, y + height / 2);
    drawText(g2d, font, card.getEast().toString(), 3 * width / 4, y + height / 2);
  }

  private void drawText(Graphics2D g, Font font, String text, int x, int y) {
    FontMetrics metrics = g.getFontMetrics(font);
    g.setFont(font);
    g.drawString(text, x - metrics.stringWidth(text) / 2,
            y - metrics.getHeight() / 2 + metrics.getAscent());
  }

  private List<ICard> getHand() {
    return this.model.getPlayerHand(this.player);
  }

  private class MouseEventsListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      int ind = e.getY() / (getHeight() / Math.max(getHand().size(), 1));
      int handIndex = selectedIndex == ind ? -1 : ind;
      for (EventFeatures feature : features) {
        feature.selectCard(handIndex, player);
      }
      repaint();
    }
  }
}
