package model.filereader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.card.Card;
import model.card.CardInterface;
import model.card.Value;

/**
 * Offers methods for reading a list of cards in from a card config file.
 */
public class CardConfigFileReader extends AbstractConfigFileReader {
  /**
   * Constructs a CardConfigFileReader with the supplied path to a card config file.
   * @param path the file path of the card config file to be read
   */
  public CardConfigFileReader(String path) {
    super(path);
  }

  /**
   * Turns card config file into actual list of cards game state.
   * @return List of cards.
   * @throws IOException If errors ran into while reading files.
   */
  public List<CardInterface> readCards() throws IOException {
    FileReader fileReader = new FileReader(this.path);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    List<CardInterface> cards = new ArrayList<>();

    String line;
    // Read each subsequent line until the end of the file
    while ((line = bufferedReader.readLine()) != null) {
      String[] cardData = line.split(" "); // Split line by spaces
      cards.add(new Card(cardData[0],stringToValue(cardData[1]), stringToValue(cardData[2]),
              stringToValue(cardData[3]), stringToValue(cardData[4])));
    }

    return cards;
  }

  private Value stringToValue(String str) {
    switch (str) {
      case "1":
        return Value.ONE;
      case "2":
        return Value.TWO;
      case "3":
        return Value.THREE;
      case "4":
        return Value.FOUR;
      case "5":
        return Value.FIVE;
      case "6":
        return Value.SIX;
      case "7":
        return Value.SEVEN;
      case "8":
        return Value.EIGHT;
      case "9":
        return Value.NINE;
      case "A":
        return Value.TEN;
      default:
        throw new IllegalArgumentException("No Value attributed to the string: " + str);
    }
  }
}
