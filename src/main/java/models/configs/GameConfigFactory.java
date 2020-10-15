package models.configs;

import models.generation.config.strategy.GridConfigStrategy;
import models.generation.config.strategy.RandomConfigStrategy;
import models.generation.config.strategy.RandomGliderStrategy;
import models.generation.config.strategy.RandomPulsarConfig;
import models.grid.GridConfig;
import models.rulesets.ClassicRuleset;
import models.rulesets.GameOfLifeRuleset;

public class GameConfigFactory {

  private static final int WIDTH = 1800;
  private static final int HEIGHT = 1000;
  private static final int COLS = 450;
  private static final int ROWS = 250;
  private static final int FPS = 30;

  public static GameConfig simpleRandomConfig(double probability) {

    GridConfigStrategy gridConfigStrategy = RandomConfigStrategy.builder()
        .aliveProbability(probability)
        .build();

    GridConfig gridConfig = gridConfigStrategy.getConfig(ROWS, COLS);
    GameOfLifeRuleset ruleset = new ClassicRuleset();
    return buildConfig(gridConfig, ruleset);
  }
  public static GameConfig gliderConfig(double probability) {

    GridConfigStrategy gridConfigStrategy = RandomGliderStrategy.builder()
        .gliderGenerationProbability(probability)
        .build();

    GridConfig gridConfig = gridConfigStrategy.getConfig(ROWS, COLS);
    GameOfLifeRuleset ruleset = new ClassicRuleset();
    return buildConfig(gridConfig, ruleset);
  }

  public static GameConfig pulsarConfig(double probability) {

    GridConfigStrategy gridConfigStrategy = RandomPulsarConfig.builder()
        .pulsarGenerationProbability(probability)
        .build();

    GridConfig gridConfig = gridConfigStrategy.getConfig(ROWS, COLS);
    GameOfLifeRuleset ruleset = new ClassicRuleset();
    return buildConfig(gridConfig, ruleset);
  }

  private static GameConfig buildConfig(GridConfig gridConfig, GameOfLifeRuleset ruleset) {
    return GameConfig.builder()
        .width(WIDTH)
        .height(HEIGHT)
        .rows(ROWS)
        .columns(COLS)
        .boardConfig(gridConfig)
        .ruleset(ruleset)
        .fps(FPS)
        .build();
  }
}
