package model.filereader;

/**
 * Abstract class encapsulating the common code from the Card and Grid ConfigReader's.
 */
public abstract class AbstractConfigFileReader {
  protected final String path;

  /**
   * Constructs an AbstractConfigFileReader with the path of the config file to be read.
   * @param path file path of config file to be read
   */
  public AbstractConfigFileReader(String path) {
    this.path = path;
  }
}
