import models.configs.GameConfigFactory;
import models.interfaces.GeneralException;

public class Main {

  private static final String TITLE = "Window";

  public static void main(String[] args) throws GeneralException {
    testGameOfLife();
  }

  private static void testGameOfLife() throws GeneralException {
    GameRunner gameRunner = new GameRunner(GameConfigFactory.simpleGospelGunConfig(0.2));
    gameRunner.runGame();
  }
}
