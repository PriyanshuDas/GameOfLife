package models.configs;

import lombok.Builder;
import lombok.Getter;
import models.interfaces.IBoardConfig;
import models.rulesets.GameOfLifeRuleset;

@Builder
@Getter
public class GameConfig {
  int width, height;
  int rows, columns;
  int fps;
  IBoardConfig boardConfig;
  GameOfLifeRuleset ruleset;
  Class GridClass;
}
