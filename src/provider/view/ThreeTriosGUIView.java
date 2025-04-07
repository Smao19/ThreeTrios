package provider.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import provider.model.IReadOnlyTTModel;
import provider.model.cell.Player;


/**
 * Implementation for a GUI based view of ThreeTrios. Uses JFrame and swing to run.
 * Layout is a GridBagLayout ordered RedHand Grid BlueHand.
 */
public class ThreeTriosGUIView extends JFrame implements IThreeTriosGUIView {
  private final IReadOnlyTTModel model;
  private final TTContainerPanel containerPanel;
  List<EventFeatures> listeners = new ArrayList<EventFeatures>();

  /**
   * Constructor for the view. Takes in a model and passes it down to the container panel.
   * @param model the model to be used to view.
   */
  public ThreeTriosGUIView(IReadOnlyTTModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;
    this.containerPanel = new TTContainerPanel(this.model);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.add(containerPanel);
    this.pack();
  }

  @Override
  public void addEventListener(EventFeatures listener) {
    listeners.add(Objects.requireNonNull(listener));
    containerPanel.addFeatureListener(listener);
  }

  @Override
  public void makeVisible() {
    this.setSize(800, 600);
    this.setVisible(true);
    render();
  }

  @Override
  public void selectCard(int handInd, Player forWhom) {
    containerPanel.selectCard(handInd, forWhom);
  }

  @Override
  public void render() {
    repaint();
    this.setTitle("Current Player: " + this.model.getCurrentTurn().toString());
  }

  @Override
  public void error(String message) {
    JOptionPane errorPanel = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
    Thread t = new Thread(new Runnable() {
      public void run() {
        errorPanel.showMessageDialog(containerPanel, message);
      }
    });
    t.start();

  }

}
