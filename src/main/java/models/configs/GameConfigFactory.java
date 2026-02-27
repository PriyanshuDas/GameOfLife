package models.configs;

import models.generation.config.strategy.GospelGunGeneratorStrategy;
import models.generation.config.strategy.GridConfigStrategy;
import models.generation.config.strategy.RandomConfigStrategy;
import models.generation.config.strategy.RandomGliderStrategy;
import models.generation.config.strategy.RandomPulsarConfig;
import models.grid.GridConfig;
import models.grid.GridV3;
import models.rulesets.ClassicRulesetAdjacentAwareCells;
import models.rulesets.GameOfLifeRuleset;

public class GameConfigFactory {

  private static final int WIDTH = 1920;
  private static final int HEIGHT = 1080;

  public static GameConfig simpleRandomConfig(double probability) {
    return simpleRandomConfig(probability, 540, 960, 30);
  }

  public static GameConfig simpleRandomConfig(double probability, int rows, int cols, int fps) {
    GridConfigStrategy gridConfigStrategy = RandomConfigStrategy.builder()
        .aliveProbability(probability)
        .build();

    GridConfig gridConfig = gridConfigStrategy.getConfig(rows, cols);
    return buildConfig(gridConfig, new ClassicRulesetAdjacentAwareCells(), fps);
  }

  public static GameConfig gliderConfig(double probability) {
    return gliderConfig(probability, 540, 960, 30);
  }

  public static GameConfig gliderConfig(double probability, int rows, int cols, int fps) {
    GridConfigStrategy gridConfigStrategy = RandomGliderStrategy.builder()
        .gliderGenerationProbability(probability)
        .build();

    GridConfig gridConfig = gridConfigStrategy.getConfig(rows, cols);
    return buildConfig(gridConfig, new ClassicRulesetAdjacentAwareCells(), fps);
  }

  public static GameConfig pulsarConfig(double probability) {
    return pulsarConfig(probability, 540, 960, 30);
  }

  public static GameConfig pulsarConfig(double probability, int rows, int cols, int fps) {
    GridConfigStrategy gridConfigStrategy = RandomPulsarConfig.builder()
        .pulsarGenerationProbability(probability)
        .build();

    GridConfig gridConfig = gridConfigStrategy.getConfig(rows, cols);
    return buildConfig(gridConfig, new ClassicRulesetAdjacentAwareCells(), fps);
  }

  public static GameConfig simpleGospelGunConfig(double probability) {
    return simpleGospelGunConfig(probability, 540, 960, 30);
  }

  public static GameConfig simpleGospelGunConfig(double probability, int rows, int cols, int fps) {
    GridConfigStrategy gridConfigStrategy = GospelGunGeneratorStrategy.builder()
        .gunGenerationProbability(probability)
        .build();

    GridConfig gridConfig = gridConfigStrategy.getConfig(rows, cols);
    return buildConfig(gridConfig, new ClassicRulesetAdjacentAwareCells(), fps);
  }

  private static GameConfig buildConfig(GridConfig gridConfig, GameOfLifeRuleset ruleset, int fps) {
    return GameConfig.builder()
        .width(WIDTH)
        .height(HEIGHT)
        .rows(gridConfig.getRows())
        .columns(gridConfig.getColumns())
        .boardConfig(gridConfig)
        .ruleset(ruleset)
        .fps(fps)
        .GridClass(GridV3.class)
        .build();
  }
}
