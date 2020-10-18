package models.generation.config.strategy;

import models.grid.GridConfig;

public interface GridConfigStrategy {
  GridConfig getConfig(int rows, int columns);
}
