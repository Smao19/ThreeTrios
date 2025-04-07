package model.filereader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import model.cell.CardCell;
import model.cell.Cell;
import model.cell.HoleCell;

/**
 * Offers methods for reading a grid in from a grid config file.
 */
public class GridConfigFileReader extends AbstractConfigFileReader {
  private int numCardCells;

  /**
   * Constructs a GridConfigFileReader with the supplied path to a grid config file.
   * @param path the file path of the grid config file to be read
   */
  public GridConfigFileReader(String path) {
    super(path);
    numCardCells = 0;
  }

  /**
   * Method that opens the filePath assigned at construction and turns it into game state.
   * @return A grid of cells matching the config.
   * @throws IOException If file reading fails.
   */
  public Cell[][] readGrid() throws IOException {
    FileReader fileReader = new FileReader(this.path);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    Cell[][] grid = null;

    // Read the first line for ROWS and COLS
    String firstLine = bufferedReader.readLine();
    if (firstLine != null) {
      String[] dimensions = firstLine.split(" ");
      int rows = Integer.parseInt(dimensions[0]);
      int cols = Integer.parseInt(dimensions[1]);

      // Initialize the grid with the specified dimensions
      grid = new Cell[rows][cols];

      // Read each subsequent line to populate the grid
      for (int i = 0; i < rows; i++) {
        String row = bufferedReader.readLine();
        if (row != null) {
          for (int j = 0; j < cols; j++) {
            char cellType = row.charAt(j);
            grid[i][j] = createCell(cellType);
          }
        }
      }
    }

    return grid;
  }

  private Cell createCell(char cellType) {
    switch (cellType) {
      case 'X':
        return new HoleCell();
      case 'C':
        numCardCells++;
        return new CardCell();
      default:
        throw new IllegalArgumentException("Invalid cell type: " + cellType);
    }
  }

  /**
   * Getter for numCardCells read.
   * @return the number of card cells read from the config grid
   */
  public int getNumCardCells() {
    return this.numCardCells;
  }
}
