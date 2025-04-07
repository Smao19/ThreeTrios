package provider.view;

import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;

import provider.model.IReadOnlyTTModel;
import provider.model.cell.Player;


/**
 * Container panel for GridPanel and HandPanels. Uses GridBagLayout to set up screen in
 * RedHand Grid Bluehand format.
 */
public class TTContainerPanel extends JPanel {
  private final TTHandPanel redHandPanel;
  private final TTHandPanel blueHandPanel;
  private final TTGridPanel gridPanel;

  /**
   * Constructor method for the ContainerPanel.
   * @param model ReadonlyModel to be displayed.
   */
  public TTContainerPanel(IReadOnlyTTModel model) {
    this.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.gridy = 0;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.1;
    c.weighty = 1;
    c.gridx = 0;
    this.redHandPanel = new TTHandPanel(model, Player.RED);
    this.add(this.redHandPanel, c);
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.8;
    c.gridx = 1;
    this.gridPanel = new TTGridPanel(model);
    this.add(this.gridPanel, c);
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.1;
    c.gridx = 2;
    this.blueHandPanel = new TTHandPanel(model, Player.BLUE);
    this.add(this.blueHandPanel, c);
  }

  /**
   * Adds a feature listener each of the sub panels of ContainerPanel.
   * @param features feature to be added.
   */
  public void addFeatureListener(EventFeatures features) {
    this.redHandPanel.addFeatureListener(features);
    this.blueHandPanel.addFeatureListener(features);
    this.gridPanel.addFeatureListener(features);
  }

  /**
   * Tells the appropriate hand panel to select a certain card.
   * @param handInd index of the card
   * @param forWhom which player to select the card.
   */
  public void selectCard(int handInd, Player forWhom) {
    if (forWhom == Player.RED) {
      this.redHandPanel.selectCard(handInd);
    } else {
      this.blueHandPanel.selectCard(handInd);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(800, 600);
  }
}
