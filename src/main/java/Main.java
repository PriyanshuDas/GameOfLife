import models.configs.GameConfig;
import models.configs.GameConfigFactory;
import models.interfaces.GeneralException;
import utils.ColorUtils;

public class Main {

  public static void main(String[] args) throws GeneralException {
    CliArgs cli = CliArgs.parse(args);
    ColorUtils.setTheme(cli.colors);
    GameRunner gameRunner = new GameRunner(buildConfig(cli));
    gameRunner.runGame();
  }

  private static GameConfig buildConfig(CliArgs cli) {
    switch (cli.pattern) {
      case "glider":
        return GameConfigFactory.gliderConfig(0.5, cli.rows, cli.cols, cli.fps);
      case "pulsar":
        return GameConfigFactory.pulsarConfig(0.5, cli.rows, cli.cols, cli.fps);
      case "gospel-gun":
        return GameConfigFactory.simpleGospelGunConfig(0.5, cli.rows, cli.cols, cli.fps);
      default:
        return GameConfigFactory.simpleRandomConfig(cli.density, cli.rows, cli.cols, cli.fps);
    }
  }
}
